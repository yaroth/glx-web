/**
 * This file Copyright (c) 2014 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 * <p>
 * <p>
 * This file is licensed under the MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.yaro.bls.rest.pojos;

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.predicate.AbstractPredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.rest.service.node.definition.ConfiguredNodeEndpointDefinition;
import info.magnolia.rest.service.node.v1.NodeEndpoint;
import info.magnolia.rest.service.node.v1.RepositoryNode;
import info.magnolia.rest.service.node.v1.RepositoryProperty;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utilities to get different JCR items from the repositories.
 */
@Singleton
public class BlsPojoService {

    private static final Logger log = LoggerFactory.getLogger(BlsPojoService.class);

    private final DamTemplatingFunctions damfn;
    private final TemplatingFunctions cmsfn;
    private final NodeEndpoint<ConfiguredNodeEndpointDefinition> nodeEndpoint;
    private final int MAX_RESULT_SIZE = 6;


    @Inject
    public BlsPojoService(final DamTemplatingFunctions damfn, final TemplatingFunctions cmsfn, final NodeEndpoint<ConfiguredNodeEndpointDefinition> nodeEndpoint) {
        this.damfn = damfn;
        this.cmsfn = cmsfn;
        this.nodeEndpoint = nodeEndpoint;
    }


    /**
     * Returns a list of all {@link TrainComposition} POJOs.
     */
    public List<TrainComposition> getAllZugkompositionen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(TrainComposition.WORKSPACE);
        Node wagenRootNode = session.getNode(TrainComposition.BASEPATH);
        List<TrainComposition> trainCompositionList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(wagenRootNode, ZUGKOMPOSITION_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                TrainComposition trainComposition = getZugkompositionByNode(node);
                if (trainComposition != null) {
                    trainCompositionList.add(trainComposition);
                }
            }
        }
        return trainCompositionList;
    }

    /**
     * Returns a single {@link Waggon} POJO.
     *
     * @param id
     */
    public Waggon getWagenById(String id) throws RepositoryException {
        Waggon waggon = new Waggon();
        Session session = MgnlContext.getJCRSession(Waggon.WORKSPACE);
        Node wagenNode = session.getNodeByIdentifier(id);
        if (wagenNode != null) {
            waggon = getWagenByNode(wagenNode);
        }
        return waggon;
    }


    /**
     * Returns a single {@link WaggonPlan} by a given id POJOs.
     *
     * @param id
     */
    private WaggonPlan getWagenplanById(String id) throws RepositoryException {
        WaggonPlan waggonPlan = new WaggonPlan();
        Session session = MgnlContext.getJCRSession(WaggonPlan.WORKSPACE);
        Node wagentypNode = session.getNodeByIdentifier(id);
        if (wagentypNode != null) {
            waggonPlan = getWagenplanByNode(wagentypNode);
        }
        return waggonPlan;
    }

    /**
     * Returns a {@link WaggonPlan} POJO by a given {@link Node}.
     */
    private WaggonPlan getWagenplanByNode(Node node) throws RepositoryException {
        if (node == null || !WaggonPlan.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        WaggonPlan waggonPlan = createBasicNodeItem(node, WaggonPlan.class);

        String wagenCode = PropertyUtil.getString(node, WaggonPlan.CODE, "");
        waggonPlan.setCode(wagenCode);

        waggonPlan.setDescription(PropertyUtil.getString(node, WaggonPlan.DESCRIPTION, ""));

        String imageAssetKey = PropertyUtil.getString(node, WaggonPlan.IMAGE, "");
        if (imageAssetKey != null) {
            Image img = getImage(imageAssetKey);
            if (img != null) {
                waggonPlan.setImageLink(img.getLink());
            }
        }

        List<String> wagentypIDsList = getPropertyValuesList(node, WaggonPlan.WAGENTYP);
        waggonPlan.setWagentypIDs(wagentypIDsList);

        List<String> wagentypen = new ArrayList<>();
        for (String typID : wagentypIDsList) {
            String wagentyp = getPropertyValueById(WaggonType.WORKSPACE, typID, WaggonType.NAME);
            wagentypen.add(wagentyp);
        }
        waggonPlan.setWagentypen(wagentypen);

        Iterable<Node> nodeIterable = NodeUtil.collectAllChildren(node, SEAT_FILTER);
        if (nodeIterable != null) {
            for (Node seatNode : nodeIterable) {
                Seat seat = getSeatByNode(seatNode);
                if (seat != null) {
                    waggonPlan.getSeats().add(seat);
                }
            }
        }
        return waggonPlan;
    }

    /**
     * Returns a {@link Seat} by a given {@link Node}
     *
     * @param node
     */
    private Seat getSeatByNode(Node node) throws RepositoryException {
        if (node == null || !Seat.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Seat seat = new Seat();

        String klasseString = PropertyUtil.getString(node, Seat.KLASSE, "");
        seat.setKlasse(Integer.parseInt(klasseString));

        String id = PropertyUtil.getString(node, Seat.ID, "");
        seat.setId(id);

        String location = PropertyUtil.getString(node, Seat.LOCATION, "");
        seat.setLocation(location);

        seat.setOptions(getPropertyValuesList(node, Seat.OPTIONS));

        return seat;
    }

    /**
     * Returns a list of all {@link Station} POJOs.
     */
    public List<Station> getAllHaltestellen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Station.WORKSPACE);
        Node haltestelleRootNode = session.getNode(Station.BASEPATH);
        List<Station> stationList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(haltestelleRootNode, HALTESTELLE_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Station station = getHaltestelleByNode(node);
                if (station != null) {
                    stationList.add(station);
                }
            }
        }
        return stationList;
    }

    /**
     * Returns the UUID of the first occurrence of a {@link Station} given by its name
     *
     * @param name
     */
    public String getHaltestelleIdByName(String name) throws RepositoryException {
        if (name == null) return null;
        List<Station> stationList = getAllHaltestellen();
        for (Station station : stationList) {
            if (station.getName().equals(name)) return station.getUuid();
        }
        return null;
    }


    /**
     * Returns a single {@link Line} given by id.
     *
     * @param id
     */
    public Line getStreckeById(String id) throws RepositoryException {
        Line line = new Line();
        Session session = MgnlContext.getJCRSession(Line.WORKSPACE);
        Node node = session.getNodeByIdentifier(id);
        if (node != null) {
            line = getStreckeByNode(node);
        }
        return line;
    }

    /**
     * Returns a single {@link TrainComposition} by id.
     *
     * @param id
     */
    public TrainComposition getZugkompositionById(String id) throws RepositoryException {
        TrainComposition trainComposition = new TrainComposition();
        Session session = MgnlContext.getJCRSession(TrainComposition.WORKSPACE);
        Node node = session.getNodeByIdentifier(id);
        if (node != null) {
            trainComposition = getZugkompositionByNode(node);
        }
        return trainComposition;
    }

    /**
     * Returns a single {@link TrainService} by id.
     *
     * @param id
     */
    public TrainService getTrainserviceById(String id) throws RepositoryException {
        TrainService trainService = new TrainService();
        Session session = MgnlContext.getJCRSession(TrainService.WORKSPACE);
        Node node = session.getNodeByIdentifier(id);
        if (node != null) {
            trainService = getTrainserviceByNode(node);
        }
        return trainService;
    }


    /**
     * Returns a {@link Waggon} by a given {@link Node}.
     *
     * @param node
     */
    private Waggon getWagenByNode(Node node) throws RepositoryException {
        if (node == null || !Waggon.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Waggon waggon = createBasicNodeItem(node, Waggon.class);

        String wagenNumber = PropertyUtil.getString(node, Waggon.NUMBER, "");
        waggon.setNumber(wagenNumber);

        String wagenplanID = PropertyUtil.getString(node, Waggon.WAGENPLAN_ID, "");
        waggon.setWagenplanID(wagenplanID);

        WaggonPlan waggonPlan = getWagenplanById(wagenplanID);
        waggon.setWagenplan(waggonPlan);

        return waggon;
    }

    /**
     * Returns a {@link TrainService} by a given {@link Node}.
     *
     * @param node
     */
    private TrainService getTrainserviceByNode(Node node) throws RepositoryException {
        if (node == null || !TrainService.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        TrainService trainService = createBasicNodeItem(node, TrainService.class);

        String trainserviceName = PropertyUtil.getString(node, TrainService.NAME, "");
        trainService.setName(trainserviceName);

        String departureTime = PropertyUtil.getString(node, TrainService.DEPARTURE, "");
        trainService.setDeparture(departureTime);

        LocalTime departureAsLocalTime = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm"));

        String streckeID = PropertyUtil.getString(node, TrainService.STRECKE_ID, "");
        trainService.setStreckeID(streckeID);

        Line line = getStreckeById(streckeID);

        LinkedList<Stop> timetable = getTimetable(line, departureAsLocalTime);
        trainService.setTimetable(timetable);

        String zugkompositionID = PropertyUtil.getString(node, TrainService.ZUGKOMPOSITION_ID, "");
        trainService.setZugkompositionID(zugkompositionID);

        LinkedList<Waggon> zugkomposition = getZugkompositionById(zugkompositionID).getWagenList();
        trainService.setZugkomposition(zugkomposition);

        return trainService;
    }

    /**
     * Given a {@link Line} and a departureTime, establishes the corresponding timetable.
     *
     * @param line       of {@link Line}
     * @param departureTime : time of departure, as {@link LocalTime}
     */
    private LinkedList<Stop> getTimetable(Line line, LocalTime departureTime) {
        LinkedList<Stop> timetable = new LinkedList<>();
        if (line != null) {
            LocalTime tempTime = departureTime;
            for (Iterator abschnittIterator = line.getFahrstrecke().iterator(); abschnittIterator.hasNext(); ) {
                Section section = (Section) abschnittIterator.next();
                Stop stop = new Stop(section.getStopName(), tempTime, tempTime.plusMinutes(section.getStopDuration()));
                tempTime = tempTime.plusMinutes(section.getTripDuration());
                timetable.add(stop);
            }
            timetable.getFirst().setTimeIN(null);
            timetable.getLast().setTimeOut(null);
        }
        return timetable;
    }

    /**
     * Returns a {@link Station} by a given {@link Node}.
     *
     * @param node
     */
    private Station getHaltestelleByNode(Node node) throws RepositoryException {
        if (node == null || !Station.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Station station = createBasicNodeItem(node, Station.class);
        String name = PropertyUtil.getString(node, Station.NAME, "");
        station.setName(name);

        return station;
    }

    /**
     * Returns a {@link TrainComposition} by a given {@link Node}.
     *
     * @param node
     */
    private TrainComposition getZugkompositionByNode(Node node) throws RepositoryException {
        if (node == null || !TrainComposition.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        TrainComposition trainComposition = new TrainComposition();
        String name = PropertyUtil.getString(node, TrainComposition.NAME, "");
        trainComposition.setName(name);

        LinkedList<Waggon> waggonKomposition = new LinkedList<>();


        List<String> wagenIDs = getPropertyValuesList(node, TrainComposition.WAEGEN);
        if (wagenIDs != null) {
            for (String id : wagenIDs) {
                Waggon waggon = getWagenById(id);
                waggonKomposition.add(waggon);
            }
        }
        trainComposition.setWagenList(waggonKomposition);
        return trainComposition;
    }

    /**
     * Returns a {@link Line} by a given {@link Node}
     * {@link Line} contains a Linkedlist of {@Link Abschnitte} for the Fahrstrecke.
     */
    private Line getStreckeByNode(Node node) throws RepositoryException {
        if (node == null || !Line.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Line line = new Line();
        String name = node.getName();
        line.setName(name);

        LinkedList<Section> fahrStrecke = new LinkedList<>();

        Iterable<Node> abschnitte = NodeUtil.collectAllChildren(node);
        if (abschnitte != null) {
            for (Node abschnittNode : abschnitte) {
                Section section = getAbschnittByNode(abschnittNode);
                if (section != null) {
                    fahrStrecke.add(section);
                }
            }
        }

        line.setFahrstrecke(fahrStrecke);
        return line;
    }

    /**
     * Returns a {@link Section} by a given {@link Node}.
     *
     * @param node
     */
    private Section getAbschnittByNode(Node node) throws RepositoryException {
        if (node == null || !Section.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Section section = new Section();

        String stopID = PropertyUtil.getString(node, Section.STOP_ID, "");
        String stopName = getPropertyValueById(Station.WORKSPACE, stopID, Station.NAME);
        section.setStopName(stopName);

        String stopduration = PropertyUtil.getString(node, Section.STOP_DURATION, "");
        if (!stopduration.isEmpty()) {
            section.setStopDuration(Integer.parseInt(stopduration));
        } else {
            section.setStopDuration(0);
        }

        String tripDuration = PropertyUtil.getString(node, Section.TRIP_DURATION, "");
        if (!tripDuration.isEmpty()) {
            section.setTripDuration(Integer.parseInt(tripDuration));
        } else {
            section.setTripDuration(0);
        }

        return section;
    }

    /**
     * Returns a {@link WaggonType} by a given {@link Node}.
     *
     * @param node
     */
    private WaggonType getWagentypByNode(Node node) throws RepositoryException {
        if (node == null || !WaggonType.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        WaggonType waggonType = new WaggonType();

        String wagentypName = PropertyUtil.getString(node, WaggonType.NAME);
        waggonType.setName(wagentypName);


        return waggonType;
    }


    /**
     * Returns a list of all {@link TrainService} .
     */
    public List<TrainService> getAllTrainServices() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(TrainService.WORKSPACE);
        Node trainserviceRootNode = session.getNode(TrainService.BASEPATH);
        List<TrainService> trainServiceList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(trainserviceRootNode, TRAINSERVICE_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                TrainService trainService = getTrainserviceByNode(node);
                if (trainService != null) {
                    trainServiceList.add(trainService);
                }
            }
        }
        return trainServiceList;
    }

    /**
     * Returns a list of all {@link TrainService} for a give {@link TrainServiceRequest}.
     * Updates the timetable of corresponding trainservices according to request.
     * Sets all reservation status' of the seats accordingly.
     */
    public List<TrainService> getTrainServicesForRequest(TrainServiceRequest request) throws RepositoryException {
        List<TrainService> allTrainServices = getAllTrainServices();
        List<TrainService> trainServicesChronological = new ArrayList<>();

        for (TrainService trainService : allTrainServices) {
            if (trainService.fitsRequest(request)) {
                trainService.adaptTimetableToRequest(request);
                trainServicesChronological.add(trainService);
            }
        }
        trainServicesChronological.sort((ts1, ts2) -> {
            LocalTime departureTS1 = ts1.getTimetable().getFirst().getTimeOut();
            LocalTime departureTS2 = ts2.getTimetable().getFirst().getTimeOut();
            int result = 0;
            if (departureTS1.isBefore(departureTS2)) {
                result = -1;
            } else if (departureTS1.isAfter(departureTS2)) {
                result = 1;
            }
            return result;
        });
        // TrainServices AFTER request time are put to the end -> next day!
        List<TrainService> tempTooEarly = new ArrayList<>();
        List<TrainService> trainServicesForRequest = new ArrayList<>();
        LocalTime requestTime = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
        for (TrainService trainService : trainServicesChronological) {
            if (trainService.getTimetable().getFirst().getTimeOut().isBefore(requestTime)) {
                trainService.setDate(LocalDate.now().plusDays(1));
                setReservedSeats(trainService, request);
                tempTooEarly.add(trainService);
            } else {
                trainService.setDate(LocalDate.now());
                setReservedSeats(trainService, request);
                trainServicesForRequest.add(trainService);
            }
        }
        if (tempTooEarly.size() > 0) {
            tempTooEarly.get(0).setNextDay(true);
        }
        trainServicesForRequest.addAll(tempTooEarly);
        if (trainServicesForRequest.size() > MAX_RESULT_SIZE) {
            trainServicesForRequest.subList(0, MAX_RESULT_SIZE - 1);
        }
        return trainServicesForRequest;
    }


    /**
     * Returns an {@link Image} by a given assetKey (as String).
     * The Image contains contains an AssetKey and an AssetLink.
     *
     * @param assetKey
     */
    public Image getImage(String assetKey) {
        String assetLink = damfn.getAssetLink(assetKey);
        if (StringUtils.isNotBlank(assetLink)) {
            Image image = new Image();
            image.setAssetKey(assetKey);
            image.setLink(assetLink);
            return image;
        }
        return null;
    }

    /**
     * Returns a {@link Category} by a given uuid.
     *
     * @param uuid
     */
    public Category getCategory(String uuid) throws RepositoryException {
        Category category = null;
        if (StringUtils.isNotBlank(uuid)) {
            Session session = MgnlContext.getJCRSession(CategorizationModule.CATEGORIZATION_WORKSPACE);
            Node node = session.getNodeByIdentifier(uuid);
            if (node != null) {
                category = createBasicNodeItem(node, Category.class);
                category.setName(PropertyUtil.getString(node, "name", ""));
            }
        }
        return category;
    }

    /**
     * The methods instantiates and returns an object by the given type and sets the base
     * properties of the base class {@link NodeItem}.
     */
    protected <T extends NodeItem> T createBasicNodeItem(Node node, Class<T> clazz) throws RepositoryException {
        T nodeItem = null;
        try {
            Class classDefinition = Class.forName(clazz.getName());
            nodeItem = (T) classDefinition.newInstance();
            nodeItem.setUuid(node.getIdentifier());

        } catch (InstantiationException e) {
            log.error("NodeItem instantiation failed due to InstantiationException.", e);
        } catch (IllegalAccessException e) {
            log.error("NodeItem instantiation failed due to illegal access.", e);
        } catch (ClassNotFoundException e) {
            log.error("NodeItem instantiation failed due to class not found.", e);
        }
        return nodeItem;
    }


    /**
     * Add the bean property "categories" to the given nodeItem.
     */
    private List<Category> getCategoriesList(Node node) throws RepositoryException {
        List<Category> categoryList = new ArrayList<>();
        List<String> categoryUuids = getPropertyValuesList(node, "categories");
        if (categoryUuids.size() > 0) {

            for (String catUuid : categoryUuids) {
                Category cat = getCategory(catUuid);
                if (cat != null) {
                    categoryList.add(cat);
                }
            }
        }
        return categoryList;
    }

    /**
     * This methods returns a list of Strings containing the values of a multi value property.<br/>
     * The method assumes the values are strings. If a value is not a String, it is not added to
     * the returned list.
     */
    private List<String> getPropertyValuesList(Node node, String propertyName) throws RepositoryException {
        List<String> result = new ArrayList<>();
        if (node.hasProperty(propertyName)) {
            Property property = node.getProperty(propertyName);
            if (property != null) {
                if (property.getValues() != null) {
                    for (Value value : property.getValues()) {
                        try {
                            String propertyValue = value.getString();
                            result.add(propertyValue);
                        } catch (Exception e) {
                            log.warn("Failed to get string from value from value-property-list");
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * Predicate returns 'true' if NodeType corresponds to {@link Reservation} node type.
     */
    private static AbstractPredicate<Node> RESERVATION_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Reservation.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Predicate returns 'true' if NodeType corresponds to {@link Station} node type.
     */
    private static AbstractPredicate<Node> HALTESTELLE_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Station.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Predicate returns 'true' if NodeType corresponds to {@link Line} node type.
     */
    private static AbstractPredicate<Node> STRECKE_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Line.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Predicate returns 'true' if NodeType corresponds to {@link TrainComposition} node type.
     */
    private static AbstractPredicate<Node> ZUGKOMPOSITION_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(TrainComposition.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Predicate returns 'true' if NodeType corresponds to {@link TrainService} node type.
     */
    private static AbstractPredicate<Node> TRAINSERVICE_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(TrainService.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Prädikat, das true zurückgibt, wenn der NodeType dem {@link Seat} nodeType entspricht.
     * I.e. nodetype = mgnl:contentNode und Nodename beinhaltet 'sitz'
     */
    private static AbstractPredicate<Node> SEAT_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Seat.NODETYPE) && node.getName().contains("sitz");
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Returns the property value of a node in a workspace with a given uuid
     *
     * @param id
     * @param propertyName
     * @param workspace
     */
    private String getPropertyValueById(String workspace, String id, String propertyName) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(workspace);
        Node node = session.getNodeByIdentifier(id);
        String str = PropertyUtil.getString(node, propertyName, "");
        return str;
    }

    /**
     * Returns the {@link ReservationConfirmation} after making a {@link Reservation}.
     *
     * @param reservation Uses the nodeEndpoint service of Magnolia to save node to JCR.
     */
    public ReservationConfirmation makeReservation(Reservation reservation) throws RepositoryException {
        log.info("Reservation: " + reservation);

        RepositoryNode repositoryNode = new RepositoryNode();
        // unique node name set up using all the reservation properties
        String departureName = getPropertyValueById(Station.WORKSPACE, reservation.getFromID(), Station.NAME);
        String destinationName = getPropertyValueById(Station.WORKSPACE, reservation.getToID(), Station.NAME);
        String zugserviceName = getPropertyValueById(TrainService.WORKSPACE, reservation.getZugserviceID(), TrainService.NAME);
        String nodeName = reservation.getFirstname() + "-" + reservation.getLastname() + "-" + reservation.getDateOfBirth() +
                "-" + zugserviceName + "-" + reservation.getWagenNumber() + "-" + reservation.getSitzNumber() +
                "-" + departureName + "-" + destinationName;
        repositoryNode.setName(nodeName);
        repositoryNode.setPath(Reservation.BASEPATH + repositoryNode.getName());
        repositoryNode.setType(Reservation.NODETYPE);

        ArrayList<RepositoryProperty> properties = setProperties(reservation);
        repositoryNode.setProperties(properties);

        // all reservations are saved at root level
        Response response = nodeEndpoint.createNode(Reservation.WORKSPACE, Reservation.BASEPATH, repositoryNode);
        ReservationConfirmation reservationConfirmation = new ReservationConfirmation(reservation);
        //reservationConfirmation.setUuid();
        reservationConfirmation.setMessage(response.getStatusInfo().toString());
        return reservationConfirmation;
    }

    /**
     * Sets the properties of the {@link Reservation} to be made.
     */
    private ArrayList<RepositoryProperty> setProperties(Reservation reservation) {
        ArrayList<RepositoryProperty> properties = new ArrayList<>();

        RepositoryProperty firstNameProperty = new RepositoryProperty();
        firstNameProperty.setMultiple(false);
        firstNameProperty.setName(Reservation.FIRSTNAME);
        firstNameProperty.setType("String");
        ArrayList<String> firstnamePropertyValues = new ArrayList<>();
        firstnamePropertyValues.add(reservation.getFirstname());
        firstNameProperty.setValues(firstnamePropertyValues);
        properties.add(firstNameProperty);

        RepositoryProperty lastNameProperty = new RepositoryProperty();
        lastNameProperty.setMultiple(false);
        lastNameProperty.setName(Reservation.LASTNAME);
        lastNameProperty.setType("String");
        ArrayList<String> lastnamePropertyValues = new ArrayList<>();
        lastnamePropertyValues.add(reservation.getLastname());
        lastNameProperty.setValues(lastnamePropertyValues);
        properties.add(lastNameProperty);

        RepositoryProperty dateOfBirthProperty = new RepositoryProperty();
        dateOfBirthProperty.setMultiple(false);
        dateOfBirthProperty.setName(Reservation.DATEOFBIRTH);
        dateOfBirthProperty.setType("Date");
        ArrayList<String> dobPropertyValues = new ArrayList<>();
        dobPropertyValues.add(reservation.getDateOfBirth());
        dateOfBirthProperty.setValues(dobPropertyValues);
        properties.add(dateOfBirthProperty);

        RepositoryProperty dateProperty = new RepositoryProperty();
        dateProperty.setMultiple(false);
        dateProperty.setName(Reservation.DATE);
        dateProperty.setType("Date");
        ArrayList<String> datePropertyValues = new ArrayList<>();
        datePropertyValues.add(reservation.getDate().toString());
        dateProperty.setValues(datePropertyValues);
        properties.add(dateProperty);

        RepositoryProperty zugserviceIDProperty = new RepositoryProperty();
        zugserviceIDProperty.setMultiple(false);
        zugserviceIDProperty.setName(Reservation.ZUGSERVICEID);
        zugserviceIDProperty.setType("String");
        ArrayList<String> zugservicePropertyValues = new ArrayList<>();
        zugservicePropertyValues.add(reservation.getZugserviceID());
        zugserviceIDProperty.setValues(zugservicePropertyValues);
        properties.add(zugserviceIDProperty);

        RepositoryProperty wagenNumberProperty = new RepositoryProperty();
        wagenNumberProperty.setMultiple(false);
        wagenNumberProperty.setName(Reservation.WAGENNUMBER);
        wagenNumberProperty.setType("String");
        ArrayList<String> wageNumberValues = new ArrayList<>();
        wageNumberValues.add(reservation.getWagenNumber());
        wagenNumberProperty.setValues(wageNumberValues);
        properties.add(wagenNumberProperty);

        RepositoryProperty sitzNumberProperty = new RepositoryProperty();
        sitzNumberProperty.setMultiple(false);
        sitzNumberProperty.setName(Reservation.SITZNUMBER);
        sitzNumberProperty.setType("String");
        ArrayList<String> sitznumberPropertyValues = new ArrayList<>();
        sitznumberPropertyValues.add(reservation.getSitzNumber());
        sitzNumberProperty.setValues(sitznumberPropertyValues);
        properties.add(sitzNumberProperty);

        RepositoryProperty fromIDProperty = new RepositoryProperty();
        fromIDProperty.setMultiple(false);
        fromIDProperty.setName(Reservation.FROMID);
        fromIDProperty.setType("String");
        ArrayList<String> fromIDPropertyValues = new ArrayList<>();
        fromIDPropertyValues.add(reservation.getFromID());
        fromIDProperty.setValues(fromIDPropertyValues);
        properties.add(fromIDProperty);

        RepositoryProperty toIDProperty = new RepositoryProperty();
        toIDProperty.setMultiple(false);
        toIDProperty.setName(Reservation.TOID);
        toIDProperty.setType("String");
        ArrayList<String> toIDPropertyValues = new ArrayList<>();
        toIDPropertyValues.add(reservation.getToID());
        toIDProperty.setValues(toIDPropertyValues);
        properties.add(toIDProperty);

        return properties;
    }

    /**
     * Precondition: reservation is valid.
     * Checks if seat in waggon is available
     * for the specified Strecke on the
     * reservation date.
     *
     * @param reservation
     */
    public boolean reservationIsAllowed(Reservation reservation) throws RepositoryException {
        String zugserviceID = reservation.getZugserviceID();
        LocalDate date = reservation.getDate();
        List<Reservation> reservationsForZugserviceOnDate = getReservationsForZugserviceAndDate(zugserviceID, date);
        List<Reservation> sameSeatReservations = new ArrayList<>();
        for (Reservation res : reservationsForZugserviceOnDate) {
            if (res.getWagenNumber().equals(reservation.getWagenNumber()) && res.getSitzNumber().equals(reservation.getSitzNumber())) {
                sameSeatReservations.add(res);
            }
        }
        TrainService trainService = getTrainserviceById(zugserviceID);
        Line line = getStreckeById(trainService.getStreckeID());
        for (Reservation res : sameSeatReservations) {
            line.setTakenAbschnitteForReservation(res);
        }
        boolean seatAvailable = line.seatIsAvailable(reservation.getDeparture(), reservation.getDestination(),
                Integer.parseInt(reservation.getWagenNumber()), Integer.parseInt(reservation.getSitzNumber()));
        return seatAvailable;
    }

    /**
     * Returns all the {@link Reservation} given a zugservice ID.
     *
     * @param zugserviceID
     */
    private List<Reservation> getReservationsForZugserviceAndDate(String zugserviceID, LocalDate date) throws RepositoryException {
        List<Reservation> allReservations = getAllReservations();
        List<Reservation> reservationsForZugserviceAndDate = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            if (reservation.getZugserviceID().equals(zugserviceID) && reservation.getDate().equals(date)) {
                reservationsForZugserviceAndDate.add(reservation);
            }
        }
        return reservationsForZugserviceAndDate;
    }

    /**
     * Returns all {@link Reservation}s
     */
    private List<Reservation> getAllReservations() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Reservation.WORKSPACE);
        Node reservationRootNode = session.getNode(Reservation.BASEPATH);
        List<Reservation> reservationList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(reservationRootNode, RESERVATION_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Reservation reservation = getReservationByNode(node);
                if (reservation != null) {
                    reservationList.add(reservation);
                }
            }
        }
        return reservationList;
    }

    /**
     * Returns a {@link Reservation} given a node.
     *
     * @param node
     */
    private Reservation getReservationByNode(Node node) throws RepositoryException {
        if (node == null || !Reservation.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Reservation reservation = createBasicNodeItem(node, Reservation.class);

        String firstname = PropertyUtil.getString(node, Reservation.FIRSTNAME, "");
        reservation.setFirstname(firstname);

        String lastname = PropertyUtil.getString(node, Reservation.LASTNAME, "");
        reservation.setFirstname(lastname);

        String dateofbirth = PropertyUtil.getString(node, Reservation.DATEOFBIRTH, "");
        reservation.setDateOfBirth(dateofbirth);

        Calendar calendar = PropertyUtil.getDate(node, Reservation.DATE);
        LocalDate localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
        reservation.setDate(localDate);

        String zugserviceID = PropertyUtil.getString(node, Reservation.ZUGSERVICEID, "");
        reservation.setZugserviceID(zugserviceID);

        String wagenNumber = PropertyUtil.getString(node, Reservation.WAGENNUMBER, "");
        reservation.setWagenNumber(wagenNumber);

        String sitzNumber = PropertyUtil.getString(node, Reservation.SITZNUMBER, "");
        reservation.setSitzNumber(sitzNumber);

        String fromID = PropertyUtil.getString(node, Reservation.FROMID, "");
        reservation.setFromID(fromID);

        String departureName = getPropertyValueById(Station.WORKSPACE, fromID, Station.NAME);
        reservation.setDeparture(departureName);

        String toID = PropertyUtil.getString(node, Reservation.TOID, "");
        reservation.setToID(toID);

        String destinationName = getPropertyValueById(Station.WORKSPACE, toID, Station.NAME);
        reservation.setDestination(destinationName);

        return reservation;
    }


    /**
     * Checks whether reservation is valid, i.e. if all reservation properties
     * are valid trainservice properties?
     * Check if that
     * - waggon with that
     * - seat and
     * - from-to strecke of the trainservice is valid.
     */
    public boolean reservationIsValid(Reservation reservation) throws RepositoryException {
        reservation.setFromID(getHaltestelleIdByName(reservation.getDeparture()));
        reservation.setToID(getHaltestelleIdByName(reservation.getDestination()));
        // TODO: catch possible errors!
        boolean reservationIsValid = false;
        String zugserviceID = reservation.getZugserviceID();
        TrainService trainService = getTrainserviceById(zugserviceID);
        TrainComposition trainComposition = getZugkompositionById(trainService.getZugkompositionID());
        Line line = getStreckeById(trainService.getStreckeID());
        for (Waggon waggon : trainComposition.getWagenList()) {
            if (waggon.getNumber().equals(reservation.getWagenNumber())) {
                String wagenplanID = waggon.getWagenplanID();
                WaggonPlan waggonPlan = getWagenplanById(wagenplanID);
                for (Seat seat : waggonPlan.getSeats()) {
                    if (seat.getId().equals(reservation.getSitzNumber())) {
                        reservationIsValid = line.validateStreckenReservation(reservation);
                        return reservationIsValid;
                    }
                }
            }
        }
        return reservationIsValid;
    }


    /**
     * Sets for a given {@link TrainService} and a given {@link TrainServiceRequest} all the seats that are
     * reserved.
     */
    private void setReservedSeats(TrainService trainService, TrainServiceRequest trainServiceRequest) throws RepositoryException {
        List<Reservation> reservationsInTrainservice = getReservationsForZugserviceAndDate(trainService.getUuid(), trainService.getDate());
        Line line = getStreckeById(trainService.getStreckeID());
        for (Reservation reservation : reservationsInTrainservice) {
            line.setTakenAbschnitteForReservation(reservation);
        }
        LinkedList<Waggon> waggons = trainService.getZugkomposition();
        if (waggons != null) {
            for (Waggon w : waggons) {
                List<Seat> seats = w.getWagenplan().getSeats();
                if (seats != null) {
                    for (Seat s : seats) {
                        String from = trainServiceRequest.getFrom();
                        String to = trainServiceRequest.getTo();
                        Integer wagNb = Integer.parseInt(w.getNumber());
                        Integer seatNb = Integer.parseInt(s.getId());
                        boolean seatIsAvailable = line.seatIsAvailable(from, to, wagNb, seatNb);
                        s.setReserved(!seatIsAvailable);
                    }
                }
            }
        }


    }
}
