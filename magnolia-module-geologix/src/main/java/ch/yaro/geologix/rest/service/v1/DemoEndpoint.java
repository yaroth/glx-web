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
    public Response getTrainServices(TrainServiceRequest trainServiceRequest) {
        if (trainServiceRequest != null) {
            try {
                log.info("Trainservice request received: " + trainServiceRequest);
                Wagen wagen = blsPojoService.getTrainServices(trainServiceRequest);
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
    public Response getAllWagen() {
        List<Wagen> result = null;
        try {
            result = blsPojoService.getAllWagen();
            return Response.ok(result).build();
        } catch (RepositoryException e) {
            log.warn("Could not compute the list of all wagen.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}