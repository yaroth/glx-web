package ch.yaro.geologix.rest.service.v1;

import ch.yaro.geologix.rest.pojos.*;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.predicate.AbstractPredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.EndpointDefinition;
import info.magnolia.rest.registry.ConfiguredEndpointDefinition;

import javax.inject.Inject;
import javax.jcr.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;

/**
 * A REST endpoint producing json for content app items.<br/>
 * For the moment only providing GET methods.
 *
 * @param <D> the ConfiguredEndpointDefinition.
 */
@Api(value = "/demo/v1", description = "The demo endpoint")
@Path("/demo/v1")
//public class DemoEndpoint<D extends EndpointDefinition> extends AbstractEndpoint<D> {
public class DemoEndpoint<D extends ConfiguredEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_NOT_ACCEPTABLE = "Not acceptable";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";
    private static final String STATUS_MESSAGE_BAD_REQUEST = "Bad request";

    private static final Logger log = LoggerFactory.getLogger(EndpointDefinition.class);
    private final BlsPojoService blsPojoService;

//    public DemoEndpoint(D endpointDefinition) {
//        super(endpointDefinition);
////        this.blsPojoService = new BlsPojoService();
//    }

    @Inject
    public DemoEndpoint(BlsPojoService blsPojoService, final D endpointDefinition) {
        super(endpointDefinition);
        this.blsPojoService = blsPojoService;
    }

    @Path("/zugservices")
    @Consumes({MediaType.APPLICATION_JSON})
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get Zugservices providing departure time, departure and destination.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 406, message = STATUS_MESSAGE_NOT_ACCEPTABLE),
            @ApiResponse(code = 400, message = STATUS_MESSAGE_BAD_REQUEST)
    })
    public Response zugservices(TrainServiceRequest trainServiceRequest) {
        if (trainServiceRequest != null) {
            try {
                log.info("Trainservice request received: " + trainServiceRequest);

                Session session = MgnlContext.getJCRSession("wagen");
//                QueryManager queryManager = session.getWorkspace().getQueryManager();
//                Query query = queryManager.createQuery("select * from [wagen]", "JCR-SQL2");
//                QueryResult queryResult = query.execute();
//                NodeIterator nodeIterator = queryResult.getNodes();
//                ArrayList<Node> nodesList = new ArrayList<>();
//                while (nodeIterator.hasNext()) {
//                    nodesList.add(nodeIterator.nextNode());
//                }

                Node foundNode = session.getNodeByIdentifier("8989382e-4016-4d9d-9ff7-1b5cd71ca42c");
                Wagen wagen = getWagenByNode(foundNode);
                return Response.ok(wagen).build();
            } catch (Exception e) {
                log.error("Failed to get the Wagen using uuid: 8989382e-4016-4d9d-9ff7-1b5cd71ca42c");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } else {
            log.error("UUID not valid: 8989382e-4016-4d9d-9ff7-1b5cd71ca42c");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/allwagen")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all wagen.", notes = "Returns the list of all wagen objects.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = List.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response allwagen() {
        List<Wagen> result = null;
        try {
            result = getAllWagen();
            return Response.ok(result).build();
        } catch (RepositoryException e) {
            log.warn("Could not compute the list of all wagen.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
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

    /** Prädikat, das true zurückgibt, wenn der NodeType dem WagenNodeType entspricht. */
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
     * Returns a {@link Wagen} POJO by a given {@link Node}.
     */
    protected Wagen getWagenByNode(Node node) throws RepositoryException {
        if (node == null || !Wagen.NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Wagen wagen = createBasicNodeItem(node, Wagen.class);

        String wagenNumber = PropertyUtil.getString(node, Wagen.NUMBER, "");
        wagen.setNumber(wagenNumber);

        wagen.setSitzplan(PropertyUtil.getString(node, Wagen.SITZPLAN, ""));

        List<String> wagentypList = getPropertyValuesList(node, Wagen.WAGENTYP);
        wagen.setWagentyp(wagentypList);

        return wagen;
    }


    /**
     * The methods instantiates and returns an object by the given type and sets the base <br/>
     * properties of the base class {@link NodeItem}.
     */
    protected <T extends NodeItem> T createBasicNodeItem(Node node, Class<T> clazz) throws RepositoryException {
        T nodeItem = null;
        try {
            Class classDefinition = Class.forName(clazz.getName());
            nodeItem = (T) classDefinition.newInstance();
            nodeItem.setNodeName(node.getName());
            nodeItem.setUuid(node.getIdentifier());
            nodeItem.setPath(node.getPath());
            nodeItem.setWorkspace(node.getSession().getWorkspace().getName());

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
     * This methods returns a list of Strings containing the values of a multi value property.<br/>
     * The method assumes the values are strings. If a value is not a String, it is not added <br/>
     * to the returned list.
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





}