package ch.yaro.geologix.rest.service.v1;

import ch.yaro.geologix.rest.datastore.Store;
import ch.yaro.geologix.rest.pojos.Lunch;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.EndpointDefinition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/demo/v1", description = "The demo endpoint")
@Path("/demo/v1")
public class DemoEndpoint<D extends EndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_METHOD_NOT_ALLOWED = "Method Not Allowed";

    private final Logger log = LoggerFactory.getLogger(EndpointDefinition.class);


    public DemoEndpoint(D endpointDefinition) {
        super(endpointDefinition);
    }


    @Path("/hello")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "Say hello to the endpoint.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 405, message = STATUS_MESSAGE_METHOD_NOT_ALLOWED),
    })
    public Response hello() {
        return Response.ok().build();
    }

    @Path("/lunch")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Produce lunch.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 405, message = STATUS_MESSAGE_METHOD_NOT_ALLOWED),
    })
    public Response lunch() {
        Lunch pojo = new Lunch("Rösti mit Geschnetzeltem", "Ueli Weizen");
        return Response.ok(pojo).build();
    }

    @Path("/store-lunch")
    @Consumes({MediaType.APPLICATION_JSON})
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Store lunch.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK),
            @ApiResponse(code = 406, message = "NOT_ACCEPTABLE"),
            @ApiResponse(code = 400, message = "BAD_REQUEST")
    })
    public Response storeLunch(Lunch lunch){
        if(lunch != null){
            try {
                log.info("Lunch received: " + lunch);
                return Response.ok(lunch).build();
            } catch (Exception e) {
                log.error("Failed to store the lunch");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}