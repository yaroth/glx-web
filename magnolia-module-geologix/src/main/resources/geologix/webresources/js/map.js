import {cMap} from './cantons.js';
import {gMap} from './gemeinden.js';

const modulesContainer = document.getElementById("modules-container");
let isCantons = false;
let isGemeinden = false;
let sourceURL = '';
let cantonsGemeindenMap = new Map();
if (modulesContainer.classList.contains('kantone')) {
    isCantons = true;
    sourceURL = '/site/templates/data/kantone.geojson';
    cantonsGemeindenMap = new Map([...cMap]);
}
else if (modulesContainer.classList.contains('gemeinden')) {
    isGemeinden = true;
    sourceURL = '/site/templates/data/gemeinden.geojson';
    cantonsGemeindenMap = new Map([...gMap]);
}


const BernLonLat = [8.1822, 46.8743];
const BernWebMercator = ol.proj.fromLonLat(BernLonLat);
const view = new ol.View({
    center: BernWebMercator,
    minZoom: 7,
    zoom: 8
});

/** Color definitions*/
const defaultFillColor = 'rgba(128, 128, 128, 0.4)';
const strokeColor = 'rgba(128, 128, 128, 1)';
const logoFillColor = 'rgba(239, 121, 58, 0.4)';
const logoFillHoverColor = 'rgba(239, 121, 58, 0.6)';
const logoFillSelectedColor = 'rgba(239, 121, 58, 0.8)';

/** Auf Level Gemeinde werden LOGO Betreiber unterschieden: Gemeinde vs. Ingenieurbüro.
 * Hier die entsprechenden Styles*/
const logoIngenieurFillColor = 'rgba(255, 0, 0, 0.4)';
const logoIngenieurFillHoverColor = 'rgba(255, 0, 0, 0.6)';
const logoIngenieurFillSelectedColor = 'rgba(255, 0, 0, 0.8)';

/** Style definitions */
const defaultStyle = new ol.style.Style({
    fill: new ol.style.Fill({
        color: defaultFillColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor
    })
});
const logoStyle = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoFillColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor
    })
});
const logoStyleHover = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoFillHoverColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor, width: 2
    })
});
const logoStyleSelected = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoFillSelectedColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor, width: 2
    })
});

const logoIngenieurStyle = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoIngenieurFillColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor
    })
});
const logoIngenieurStyleHover = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoIngenieurFillHoverColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor, width: 2
    })
});
const logoIngenieurStyleSelected = new ol.style.Style({
    fill: new ol.style.Fill({
        color: logoIngenieurFillSelectedColor
    }),
    stroke: new ol.style.Stroke({
        color: strokeColor, width: 2
    })
});


/** Layers: Kantone GEOJSON & OpenStreetMap Tile */
const vectorSource = new ol.source.Vector({
    url: sourceURL,
    format: new ol.format.GeoJSON()
});
const kantoneVectorLayer = new ol.layer.Vector({
    source: vectorSource,
    style: function (feature, resolution) {
        const featureName = feature.get('NAME');
        if (featureName != null) {
            const featureID = cantonsGemeindenMap.get(featureName);
            const featureHTMLElement = document.getElementById(featureID);
            if (featureHTMLElement != null) {
                if (featureHTMLElement.classList.contains('ingenieur')) return logoIngenieurStyle;
                else return logoStyle;
            }
            else return defaultStyle;
        }
        else return defaultStyle;
    }
});
const osmTile = new ol.layer.Tile({
    source: new ol.source.OSM()
});

/** Define the map*/
const map = new ol.Map({
    target: 'map-container',
    layers: [osmTile, kantoneVectorLayer],
    view: view
});

/** Add tooltip showing the canton's name */
const tooltip = document.getElementById('tooltip');
const overlay = new ol.Overlay({
// const overlay = new Overlay({
    element: tooltip,
    offset: [10, 0],
    positioning: 'bottom-left'
});
map.addOverlay(overlay);

if (isGemeinden) addLegend();

/** Set a global 'active canton/gemeinde' to prevent styling when hovering.*/
let selectedCantonFeature = new ol.Feature();
let hoveredCantonFeature = new ol.Feature();


map.on('click', function (evt) {
    const pixel = evt.pixel;
    displayModules(pixel);
});

map.on('pointermove', function (evt) {
    const pixel = evt.pixel;
    highlightFeature(pixel);
    displayTooltip(evt);
});


/** Called when user clicks on a canton
 * @param {pixel} pixel : coordinates used to display the modules of the clicked canton */
function displayModules(pixel) {
    /** hide the 'old' feature info and set standard style to 'old' feature */
    ;
    showFeatureInfo('none');
    if (selectedCantonFeature !== null) {
        selectedCantonFeature.setStyle(setStyleInFeature(selectedCantonFeature, "std"));
        selectedCantonFeature = null;
    }
    const feature = map.forEachFeatureAtPixel(pixel, function (feature) {
        return feature;
    });
    if (feature) {
        selectedCantonFeature = feature;
        selectedCantonFeature.setStyle(setStyleInFeature(selectedCantonFeature, "selected"));
        showFeatureInfo('block');
    }
}

/** Called when user hovers over a canton
 * @param {pixel} pixel : coordinates used to highlight the canton hovered */
function highlightFeature(pixel) {
    /** set the hovered on feature style to 'hover'*/
    const feature = map.forEachFeatureAtPixel(pixel, function (feature) {
        return feature;
    });
    if (feature) {
        if (feature !== selectedCantonFeature) {
            if (hoveredCantonFeature !== selectedCantonFeature) {
                // set old hovered feature to 'std'
                hoveredCantonFeature.setStyle(setStyleInFeature(hoveredCantonFeature, "std"));
            }
            hoveredCantonFeature = feature;
            if (featureHasLogo(feature)) {
                feature.setStyle(setStyleInFeature(feature, "hover"));
            }
        }
        else {
            hoveredCantonFeature.setStyle(setStyleInFeature(hoveredCantonFeature, "std"));
            hoveredCantonFeature = feature;
            selectedCantonFeature.setStyle(setStyleInFeature(selectedCantonFeature, "selected"));
        }
    }
    /** if it's not a feature, we're outside of the map -> hover to standard, selected to selected ! */
    else {
        hoveredCantonFeature.setStyle(setStyleInFeature(hoveredCantonFeature, "std"));
        if (selectedCantonFeature !== null) {
            selectedCantonFeature.setStyle(setStyleInFeature(selectedCantonFeature, "selected"));
        }
    }
}

/** Displays the canton name as a tooltip
 * @param {event} evt : the event to pass */
function displayTooltip(evt) {
    const pixel = evt.pixel;
    const feature = map.forEachFeatureAtPixel(pixel, function (feature) {
        return feature;
    });
    tooltip.style.display = feature ? '' : 'none';
    if (feature) {
        overlay.setPosition(evt.coordinate);
        tooltip.innerHTML = feature.get('NAME');
    }
}

/** function to set the logoStyle of non logo style (for features only, not for Vectors!)*/
function setStyleInFeature(feature, kind) {
    const featureName = feature.get('NAME');
    if (featureName != null) {
        const featureID = cantonsGemeindenMap.get(featureName);
        const featureHTMLElement = document.getElementById(featureID);
        if (featureHTMLElement !== null) {
            if (kind == "std") {
                if (featureHTMLElement.classList.contains('ingenieur')) return logoIngenieurStyle;
                else return logoStyle;
            }
            else if (kind == "hover") {
                if (featureHTMLElement.classList.contains('ingenieur')) return logoIngenieurStyleHover;
                else return logoStyleHover;
            }
            else if (kind == "selected") {
                if (featureHTMLElement.classList.contains('ingenieur')) return logoIngenieurStyleSelected
                else return logoStyleSelected;
            }
        }
        else return defaultStyle;
    }
}

/** Returns true if the feature has LOGO*/
function featureHasLogo(feature) {
    var hasLogo = false;
    const featureName = feature.get('NAME');
    if (featureName != null) {
        const featureID = cantonsGemeindenMap.get(featureName);
        const cantonElement = document.getElementById(featureID);
        // if there is an element with that ID, the Kanton/Gemeinde HAS Logo
        if (cantonElement != null) {
            hasLogo = true;
        }
    }
    return hasLogo;
}

function showFeatureInfo(showHide) {
    if (selectedCantonFeature != null) {
        let cantonID = cantonsGemeindenMap.get(selectedCantonFeature.get('NAME'));
        const activeCantonHTMLElement = document.getElementById(cantonID);
        if (activeCantonHTMLElement !== null) {
            activeCantonHTMLElement.style.display = showHide;
        }
    }
}

function addLegend() {
    const mapContainer = document.getElementById('map-container');
    const olViewport = mapContainer.firstChild;
    // create 'legend' DIV
    var legendDiv = document.createElement("div");
    legendDiv.id = "legend";
    // create 'Gemeinde' DIV
    var gemeindeDiv = document.createElement("div");
    gemeindeDiv.classList.add("gemeinde");
    var gemeindeNode = document.createTextNode("Gemeinde");
    gemeindeDiv.appendChild(gemeindeNode);
    // create 'ingenieur' DIV
    var ingDiv = document.createElement("div");
    ingDiv.classList.add("ingenieur");
    var ingNode = document.createTextNode("Ingenieurbüro");
    ingDiv.appendChild(ingNode);
    // assemble everything
    legendDiv.appendChild(gemeindeDiv);
    legendDiv.appendChild(ingDiv);
    olViewport.appendChild(legendDiv);
    // set styling
    setLegendStyling();
}

/** 'legend' styling*/
function setLegendStyling() {
    const legendHTMLElement = document.getElementById("legend");
    const legendChildren = legendHTMLElement.children;
    for (var i = 0; i < legendChildren.length; i++) {
        if (legendChildren[i].classList.contains("gemeinde")) legendChildren[i].style.color = logoFillSelectedColor;
        else if (legendChildren[i].classList.contains("ingenieur")) legendChildren[i].style.color = logoIngenieurFillSelectedColor;
    }

}