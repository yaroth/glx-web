/**
 * This file Copyright (c) 2014 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is licensed under the MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package info.magnolia.documentation.apps.cameracollection.rest.v1;

import info.magnolia.documentation.apps.cameracollection.CameraCollectionModule;
import info.magnolia.documentation.apps.cameracollection.pojos.Camera;
import info.magnolia.documentation.apps.cameracollection.pojos.CameraCollectionPojoService;
import info.magnolia.documentation.apps.cameracollection.pojos.Maker;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.registry.ConfiguredEndpointDefinition;

import java.util.List;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A REST endpoint producing json for content app items.<br/>
 * Json is produced by RestEasy using simple POJOs such as {@link Camera}, {@link Maker}, etc..<br/>
 * For the moment only providing GET methods.
 *
 * @param <D> the ConfiguredEndpointDefinition.
 */
@Api(value = "/cameracollection/v1", description = "The camera collection API.")
@Path("/cameracollection/v1")
public class CameraCollectionEndpoint<D extends ConfiguredEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";
    private static final Logger log = LoggerFactory.getLogger(CameraCollectionEndpoint.class);

    private final CameraCollectionPojoService cameraCollectionPojoService;

    @Inject
    public CameraCollectionEndpoint(CameraCollectionPojoService cameraCollectionPojoService, final D endpointDefinition) {
        super(endpointDefinition);
        this.cameraCollectionPojoService = cameraCollectionPojoService;
    }


    @Path("/allMakers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all makers.", notes = "Returns json for the list of all a maker objects.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = List.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public List<Maker> getAllMakers() {
        List<Maker> result = null;

        try {
            result = cameraCollectionPojoService.getAllMakers();
        } catch (RepositoryException e) {
            log.warn("Could not compute the list of all makers");
        }
        return result;
    }


    @Path("/allCameras")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all cameras.", notes = "Returns json for the list of all a camera objects.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = List.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public List<Camera> getAllCameras() {
        List<Camera> result = null;
        try {
            result = cameraCollectionPojoService.getAllCameras();
        } catch (RepositoryException e) {
            log.warn("Could not compute the list of all cameras.");
        }
        return result;
    }


    @Path("/makers{makerPath:(/.+)?}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a maker by maker path.", notes = "Returns json for a maker.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = Maker.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Maker getMakerByPath(@PathParam("makerPath") String makerPath) {
        Maker maker = null;
        try {
            maker = cameraCollectionPojoService.getMakerByPath(CameraCollectionModule.MAKERS_BASEPATH + makerPath);
        } catch (RepositoryException e) {
            log.warn("Could not find maker with relative path [{}]", new Object[]{makerPath, e});
        }
        return maker;
    }


    @Path("/makerById/{uuid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a maker by uuid.", notes = "Returns json for a maker.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = Maker.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Maker getMakerById(@PathParam("uuid") String uuid) {
        Maker maker = null;
        try {
            maker = cameraCollectionPojoService.getMakerById(uuid);
        } catch (RepositoryException e) {
            log.warn("Could not find maker with uuid [{}]", new Object[]{uuid, e});
        }
        return maker;
    }


    @Path("/cameras{cameraPath:(/.+)?}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a camera by cameraPath", notes = "Returns json for a camera.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = Camera.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Camera getCameraByPath(@PathParam("cameraPath") String cameraPath) {
        Camera camera = null;
        try {
            camera = cameraCollectionPojoService.getCameraByPath(CameraCollectionModule.CAMERAS_BASEPATH + cameraPath);
        } catch (RepositoryException e) {
            log.warn("Could not find camera with relative cameraPath [{}]", new Object[]{cameraPath, e});
        }
        return camera;
    }


    @Path("/cameraById/{uuid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a camera by uuid", notes = "Returns json for a camera.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = Camera.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Camera getCameraById(@PathParam("uuid") String uuid) {
        Camera camera = null;
        try {
            camera = cameraCollectionPojoService.getCameraById(uuid);
        } catch (RepositoryException e) {
            log.warn("Could not find camera with uuid [{}]", new Object[]{uuid, e});
        }
        return camera;
    }


    @Path("/searchCameras")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Search a camera", notes = "Returns json for a list of cameras.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_MESSAGE_OK, response = Camera.class),
            @ApiResponse(code = 401, message = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(code = 404, message = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(code = 500, message = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public List<Camera> searchCameras(@QueryParam("q") String q){
        List<Camera> result = null;

        try{
            result = cameraCollectionPojoService.searchCameras(q);
        }catch (RepositoryException repex){
            log.warn("Could not find camera with query param [{}]", new Object[]{q});
        }

        return result;
    }

}
