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
package ch.yaro.geologix.rest.pojos;

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.predicate.AbstractPredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.StringList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.*;


@Singleton
public class BlsPojoService {

    private static final Logger log = LoggerFactory.getLogger(BlsPojoService.class);

    private final DamTemplatingFunctions damfn;
    private final TemplatingFunctions cmsfn;


    @Inject
    public BlsPojoService(final DamTemplatingFunctions damfn, final TemplatingFunctions cmsfn) {
        this.damfn = damfn;
        this.cmsfn = cmsfn;
    }

    /**
     * Returns a list of all {@link Wagen} POJOs.
     */
    public List<Wagen> getAllWagen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Wagen.WORKSPACE);
        Node wagenRootNode = session.getNode(Wagen.BASEPATH);
        List<Wagen> wagenList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(wagenRootNode, WAGEN_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Wagen wagen = getWagenByNode(node);
                if (wagen != null) {
                    wagenList.add(wagen);
                }
            }
        }
        return wagenList;
    }

    /**
     * Returns a list of all {@link Zugkomposition} POJOs.
     */
    public List<Zugkomposition> getAllZugkompositionen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Zugkomposition.WORKSPACE);
        Node wagenRootNode = session.getNode(Zugkomposition.BASEPATH);
        List<Zugkomposition> zugkompositionList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(wagenRootNode, ZUGKOMPOSITION_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Zugkomposition zugkomposition = getZugkompositionByNode(node);
                if (zugkomposition != null) {
                    zugkompositionList.add(zugkomposition);
                }
            }
        }
        return zugkompositionList;
    }

    /**
     * Returns a single {@link Wagen} POJOs.
     * @param id
     */
    public Wagen getWagenById(String id) throws RepositoryException {
        Wagen wagen = new Wagen();
        Session session = MgnlContext.getJCRSession(Wagen.WORKSPACE);
        Node wagenNode = session.getNodeByIdentifier(id);
        if (wagenNode != null) {
            wagen = getWagenByNode(wagenNode);
        }
        return wagen;
    }

    /**
     * Returns a single {@link Wagentyp} POJOs.
     * @param id
     */
    public Wagentyp getWagentypById(String id) throws RepositoryException {
        Wagentyp wagentyp = new Wagentyp();
        Session session = MgnlContext.getJCRSession(Wagentyp.WORKSPACE);
        Node wagentypNode = session.getNodeByIdentifier(id);
        if (wagentypNode != null) {
            wagentyp = getWagentypByNode(wagentypNode);
        }
        return wagentyp;
    }

    /**
     * Returns a list of all {@link Haltestelle} POJOs.
     */
    public List<Haltestelle> getAllHaltestellen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Haltestelle.WORKSPACE);
        Node haltestelleRootNode = session.getNode(Haltestelle.BASEPATH);
        List<Haltestelle> haltestelleList = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(haltestelleRootNode, HALTESTELLE_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Haltestelle haltestelle = getHaltestelleByNode(node);
                if (haltestelle != null) {
                    haltestelleList.add(haltestelle);
                }
            }
        }
        return haltestelleList;
    }

    /**
     * Returns a list of all {@link Strecke} POJOs.
     */
    public List<Strecke> getAllStrecken() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(Strecke.WORKSPACE);
        Node streckeRootNode = session.getNode(Strecke.BASEPATH);
        List<Strecke> streckeList = new ArrayList<>();
        Iterable<Node> strecken = NodeUtil.collectAllChildren(streckeRootNode, STRECKE_FILTER);
        if (strecken != null) {
            for (Node streckeNode : strecken) {
                Strecke strecke = getStreckeByNode(streckeNode);
                if (strecke != null) {
                    streckeList.add(strecke);
                }
            }
        }
        return streckeList;
    }

    /**
     * Returns a single {@link Strecke} POJOs.
     */
    public Strecke getStreckeById(String id) throws RepositoryException {
        Strecke strecke = new Strecke();
        Session session = MgnlContext.getJCRSession(Strecke.WORKSPACE);
        Node node = session.getNodeByIdentifier(id);
        if (node != null) {
            strecke = getStreckeByNode(node);
        }
        return strecke;
    }

    /**
     * Returns a single {@link Zugkomposition} POJOs.
     */
    public Zugkomposition getZugkompositionById(String id) throws RepositoryException {
        Zugkomposition zugkomposition = new Zugkomposition();
        Session session = MgnlContext.getJCRSession(Zugkomposition.WORKSPACE);
        Node node = session.getNodeByIdentifier(id);
        if (node != null) {
            zugkomposition = getZugkompositionByNode(node);
        }
        return zugkomposition;
    }


    /**
     * Returns a {@link Wagen} POJO by a given {@link Node}.
     */
    private Wagen getWagenByNode(Node node) throws RepositoryException {
        if (node == null || !Wagen.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Wagen wagen = createBasicNodeItem(node, Wagen.class);

        String wagenNumber = PropertyUtil.getString(node, Wagen.NUMBER, "");
        wagen.setNumber(wagenNumber);

        wagen.setWagenplanID(PropertyUtil.getString(node, Wagen.WAGENPLAN, ""));

        List<String> wagentypList = getPropertyValuesList(node, Wagen.WAGENTYP);
        wagen.setWagentypIDs(wagentypList);

        return wagen;
    }

    /**
     * Returns a {@link TrainService} POJO by a given {@link Node}.
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

        String streckeID = PropertyUtil.getString(node, TrainService.STRECKE, "");
        trainService.setStreckeID(streckeID);
        Strecke strecke = getStreckeById(streckeID);
        LinkedList<Stop> timetable = getTimetable(strecke, departureAsLocalTime);
        trainService.setTimetable(timetable);

        trainService.setZugkompositionID(PropertyUtil.getString(node, TrainService.ZUGKOMPOSITION, ""));

        return trainService;
    }

    private LinkedList<Stop> getTimetable(Strecke strecke, LocalTime departureTime) {
        LinkedList<Stop> timetable = new LinkedList<>();
        if (strecke != null) {
            LocalTime tempTime = departureTime;
            for (Iterator abschnittIterator = strecke.getFahrstrecke().iterator(); abschnittIterator.hasNext(); ) {
                Abschnitt abschnitt = (Abschnitt) abschnittIterator.next();
                Stop stop = new Stop(abschnitt.getStopName(), tempTime, tempTime.plusMinutes(abschnitt.getStopDuration()));
                tempTime = tempTime.plusMinutes(abschnitt.getTripDuration());
                timetable.add(stop);
            }
            timetable.getFirst().setTimeIN(null);
            timetable.getLast().setTimeOut(null);
        }
        return timetable;
    }

    /**
     * Returns a {@link Haltestelle} POJO by a given {@link Node}.
     */
    private Haltestelle getHaltestelleByNode(Node node) throws RepositoryException {
        if (node == null || !Haltestelle.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Haltestelle haltestelle = createBasicNodeItem(node, Haltestelle.class);
        String name = PropertyUtil.getString(node, Haltestelle.NAME, "");
        haltestelle.setName(name);

        return haltestelle;
    }

    /**
     * Returns a {@link Zugkomposition} POJO by a given {@link Node}.
     */
    private Zugkomposition getZugkompositionByNode(Node node) throws RepositoryException {
        if (node == null || !Zugkomposition.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Zugkomposition zugkomposition = new Zugkomposition();
        String name = PropertyUtil.getString(node, Zugkomposition.NAME, "");
        zugkomposition.setName(name);

        List<Wagen> wagenKomposition = new LinkedList<>();


        List<String> wagenIDs = getPropertyValuesList(node, Zugkomposition.WAEGEN);
        if (wagenIDs != null) {
            for (String id : wagenIDs) {
                Wagen wagen = getWagenById(id);
                wagenKomposition.add(wagen);
            }
        }
        zugkomposition.setWagenList(wagenKomposition);
        return zugkomposition;
    }

    /**
     * Returns a {@link Strecke} POJO by a given {@link Node}
     * contains a Linkedlist of {@Link Abschnitte} for the Fahrstrecke.
     */
    private Strecke getStreckeByNode(Node node) throws RepositoryException {
        if (node == null || !Strecke.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Strecke strecke = new Strecke();
        String name = node.getName();
        strecke.setName(name);

        LinkedList<Abschnitt> fahrStrecke = new LinkedList<>();

        Iterable<Node> abschnitte = NodeUtil.collectAllChildren(node);
        if (abschnitte != null) {
            for (Node abschnittNode : abschnitte) {
                Abschnitt abschnitt = getAbschnittByNode(abschnittNode);
                if (abschnitt != null) {
                    fahrStrecke.add(abschnitt);
                }
            }
        }

        strecke.setFahrstrecke(fahrStrecke);
        return strecke;
    }

    /**
     * Returns a {@link Abschnitt} POJO by a given {@link Node}.
     */
    private Abschnitt getAbschnittByNode(Node node) throws RepositoryException {
        if (node == null || !Abschnitt.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Abschnitt abschnitt = new Abschnitt();

        String stopID = PropertyUtil.getString(node, Abschnitt.STOP_ID, "");
        String haltestelleNamen = getPropertyNameById(Haltestelle.WORKSPACE, stopID, Haltestelle.NAME);
        abschnitt.setStopName(haltestelleNamen);

        String stopduration = PropertyUtil.getString(node, Abschnitt.STOP_DURATION, "");
        if (!stopduration.isEmpty()) {
            abschnitt.setStopDuration(Integer.parseInt(stopduration));
        } else {
            abschnitt.setStopDuration(0);
        }

        String tripDuration = PropertyUtil.getString(node, Abschnitt.TRIP_DURATION, "");
        if (!tripDuration.isEmpty()) {
            abschnitt.setTripDuration(Integer.parseInt(tripDuration));
        } else {
            abschnitt.setTripDuration(0);
        }

        return abschnitt;
    }

    /**
     * Returns a {@link Wagentyp} POJO by a given {@link Node}.
     */
    private Wagentyp getWagentypByNode(Node node) throws RepositoryException {
        if (node == null || !Wagentyp.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Wagentyp wagentyp = new Wagentyp();

        String wagentypName = PropertyUtil.getString(node, Wagentyp.NAME);
        wagentyp.setName(wagentypName);


        return wagentyp;
    }


    /**
     * Returns a list of all {@link TrainService} POJO .
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
     * Returns an {@link Image} POJO by a given assetKey (as String).
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
     * Returns a {@link Category} POJO by a given uuid.
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
     * The methods instantiates and returns an object by the given type and sets the base properties of the base class {@link NodeItem}.
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
     * The method assumes the values are strings. If a value is not a STring, it is not added to the returned list.
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
     * Prädikat, das true zurückgibt, wenn der NodeType dem WagenNodeType entspricht.
     */
    private static AbstractPredicate<Node> WAGEN_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Wagen.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Prädikat, das true zurückgibt, wenn der NodeType dem HaltestelleNodeType entspricht.
     */
    private static AbstractPredicate<Node> HALTESTELLE_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Haltestelle.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Prädikat, das true zurückgibt, wenn der NodeType dem StreckeNodeType entspricht.
     */
    private static AbstractPredicate<Node> STRECKE_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Strecke.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Prädikat, das true zurückgibt, wenn der NodeType dem ZugkompositionNodeType entspricht.
     */
    private static AbstractPredicate<Node> ZUGKOMPOSITION_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(Zugkomposition.NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Prädikat, das true zurückgibt, wenn der NodeType dem TrainServiceNodeType entspricht.
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
     * Returns the property name of a node in a workspace with a given uuid
     *
     * @param id
     * @param property
     * @param workspace
     */
    private String getPropertyNameById(String workspace, String id, String property) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(workspace);
        Node node = session.getNodeByIdentifier(id);
        String str = PropertyUtil.getString(node, property, "");
        // TODO: when do we logout of a session?
//        session.logout();
        return str;
    }

    public ReservationConfirmation makeReservation(Reservation reservation) {
        ReservationConfirmation reservationConfirmation = new ReservationConfirmation();
        return reservationConfirmation;
    }
}
