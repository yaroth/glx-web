package ch.yaro.geologix.rest.service.v1;

import ch.yaro.geologix.rest.WagenCollection;
import ch.yaro.geologix.rest.pojos.Lunch;
import ch.yaro.geologix.rest.pojos.NodeItem;
import ch.yaro.geologix.rest.pojos.TrainServiceRequest;
import ch.yaro.geologix.rest.pojos.Wagen;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.predicate.AbstractPredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.EndpointDefinition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/demo/v1", description = "The demo endpoint")
@Path("/demo/v1")
public class DemoEndpoint<D extends EndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";

    private static final Logger log = LoggerFactory.getLogger(EndpointDefinition.class);


    public DemoEndpoint(D endpointDefinition) {
        super(endpointDefinition);
    }


    @Path("/zugservices")
    @Consumes({MediaType.APPLICATION_JSON})
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get Zugservices providing departure time, departure and destination.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 406, message = "NOT_ACCEPTABLE"),
            @ApiResponse(code = 400, message = "BAD_REQUEST")
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
    @ApiOperation(value = "Get all wagen.", notes = "Returns json for the list of all a wagen objects.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = List.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public List<Wagen> allwagen() {
        List<Wagen> result = null;
        try {
            result = getAllWagen();
        } catch (RepositoryException e) {
            log.warn("Could not compute the list of all wagen.");
        }
        return result;
    }


    /**
     * Returns a list of all {@link Wagen} POJOs.
     */
    public List<Wagen> getAllWagen() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(WagenCollection.WORKSPACE);
        Node wagenRootNode = session.getNode(WagenCollection.WAGEN_BASEPATH);
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


    private static AbstractPredicate<Node> WAGEN_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(WagenCollection.WAGEN_NODETYPE);
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
        if (node == null || !WagenCollection.WAGEN_NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        Wagen wagen = createBasicNodeItem(node, Wagen.class);

        wagen.setWagenPath(wagen.getPath().substring(WagenCollection.WAGEN_BASEPATH.length()));

        String wagenNumber = PropertyUtil.getString(node, "number", "");
        wagen.setNumber(wagenNumber);

        wagen.setSitzplan(PropertyUtil.getString(node, "sitzplan", ""));

        return wagen;
    }


    /**
     * The methods instantiates and returns an object by the given type and sets the base properties of the base class {@link NodeItem}.
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
}