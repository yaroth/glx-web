package ch.yaro.geologix.rest.service.v1;

import ch.yaro.geologix.rest.pojos.*;
import com.google.gson.Gson;
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
public class DemoEndpoint<D extends ConfiguredEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_NOT_ACCEPTABLE = "Not acceptable";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";
    private static final String STATUS_MESSAGE_BAD_REQUEST = "Bad request";
    private static final String STATUS_MESSAGE_SEAT_NO_LONGER_AVAILABLE = "SeatNoLongerAvailable.";
    private static final String STATUS_MESSAGE_NO_RESERVATION_PROVIDED = "NoReservationProvided.";
    private static final String STATUS_MESSAGE_UNKNOWN_ERROR = "UnknownError";
    private static final String STATUS_MESSAGE_RESERVATION_NOT_VALID = "ReservationNotValid";
    private static final String STATUS_MESSAGE_TRAINSERVICE_REQUEST_NOT_VALID = "TrainServiceRequestNotValid";

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
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get Zugservices providing departure time, departure and destination.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 406, message = STATUS_MESSAGE_NOT_ACCEPTABLE),
            @ApiResponse(code = 400, message = STATUS_MESSAGE_BAD_REQUEST)
    })
    public Response getTrainServices(TrainServiceRequest trainServiceRequest) {
        boolean requestIsValid = trainServiceRequest.isValid();
        if (requestIsValid) {
            List<TrainService> result;
            try {
                result = blsPojoService.getTrainServicesForRequest(trainServiceRequest);
                return Response.ok(result).build();
            } catch (RepositoryException e) {
                log.warn("Could not compute the request.");
                Gson gson = new Gson();
                String errorJson = gson.toJson(STATUS_MESSAGE_UNKNOWN_ERROR);
                return Response.status(Response.Status.BAD_REQUEST).entity(errorJson).build();
            }
        } else {
            Gson gson = new Gson();
            String errorJson = gson.toJson(STATUS_MESSAGE_TRAINSERVICE_REQUEST_NOT_VALID);
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorJson).build();
        }
    }


    @Path("/reservation")
    @Consumes({MediaType.APPLICATION_JSON})
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Request reservation. Provide user, trainservice, waggon, seat number, from and to.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 406, message = STATUS_MESSAGE_NOT_ACCEPTABLE),
            @ApiResponse(code = 400, message = STATUS_MESSAGE_BAD_REQUEST)
    })
    public Response reservation(Reservation reservation) {
        if (reservation != null) {
            try {
                boolean isReservationValid = blsPojoService.validateReservation(reservation);
                if (isReservationValid) {
                    boolean isReservationAllowed = blsPojoService.checkReservation(reservation);
                    if (isReservationAllowed) {
                        ReservationConfirmation reservationConfirmation = blsPojoService.makeReservation(reservation);
                        return Response.ok(reservationConfirmation).build();
                    } else {
                        Gson gson = new Gson();
                        String errorJson = gson.toJson(STATUS_MESSAGE_SEAT_NO_LONGER_AVAILABLE);
                        return Response.status(Response.Status.CONFLICT).entity(errorJson).build();
                    }
                } else {
                    Gson gson = new Gson();
                    String errorJson = gson.toJson(STATUS_MESSAGE_RESERVATION_NOT_VALID);
                    return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorJson).build();
                }
            } catch (Exception e) {
                log.error("Failed !");
                Gson gson = new Gson();
                String errorJson = gson.toJson(STATUS_MESSAGE_UNKNOWN_ERROR);
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorJson).build();
            }
        } else {
            log.error("No Reservation provided");
            Gson gson = new Gson();
            String errorJson = gson.toJson(STATUS_MESSAGE_NO_RESERVATION_PROVIDED);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorJson).build();
        }
    }



}