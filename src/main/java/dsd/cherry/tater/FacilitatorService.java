package dsd.cherry.tater;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dsd.cherry.tater.types.*;
import dsd.cherry.tater.types.jax_mixins.MxImageDataAuthRequest;
import dsd.cherry.tater.types.jax_mixins.MxImageDataAuthResponse;
import dsd.cherry.tater.types.jax_pojos.AuthRequestTrain;
import dsd.cherry.tater.types.jax_pojos.AuthRequestVerify;
import dsd.cherry.tater.types.jax_pojos.AuthResponseTrain;
import dsd.cherry.tater.types.jax_pojos.AuthResponseVerify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The FacilitatorService exposes the Facilitator API as a RESTful web service for use by the Authentication Server.
 *
 * @author Andrew James Beach
 * @version 0.1
 * Created by James Beach on 4/27/2016.
 */
@Path("")
public class FacilitatorService {
    private ObjectMapper mapper;
    private ServiceManager services;

    public FacilitatorService() {
        mapper = new ObjectMapper();
        services = new ServiceManager(10);
    }

    /**
     * Exposes a training function through which the Authentication Server can commence the training of facial
     * recognition services and at the same time get the training status of those services.
     * @param JSON A JSON training request from the Authentication Server.
     * @return An HTTP response and a JSON data-bound object. See the Facilitator Interface Specification and the
     *            definition for the AuthResponseTrain object.
     */
    @POST
    @Path("/train")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response train(String JSON) {
        ObjectMapper mapIn = this.mapper.copy();
        mapIn.addMixIn(ImageData.class, MxImageDataAuthRequest.class);
        AuthRequestTrain req;
        try {
            req = mapIn.readValue(JSON, AuthRequestTrain.class);
        } catch (IOException e) {
            System.out.println("Error reading JSON Train Request: " + e.getMessage());
            e.printStackTrace();
            return Response.status(452).entity("Error reading JSON request.").build();
        }
        System.out.println("UserId: " + req.getInternalID());
        System.out.println(req.getImages().toString());

        ObjectMapper mapOut = this.mapper.copy();
        mapOut.addMixIn(ImageData.class, MxImageDataAuthResponse.class);

        SMTrainData result = services.train(req.getInternalID(), req.getFACIDs(), req.getImages());
        AuthResponseTrain reply = new AuthResponseTrain();
        reply.setInternalID(result.getInternalID());
        reply.setTrainingStatus(result.getTrainingStatus());
        reply.setImages(result.getImageData());
        reply.setHTTPCode(StatusCode.IMAGE_OK.getHTTPCode());
        for (ImageData img : result.getImageData()) {
            if (!img.getCode().equals(StatusCode.IMAGE_OK)) {
                reply.setHTTPCode(img.getCode().getHTTPCode());
                break;
            }
        }

        try {
            return Response.status(reply.getHTTPCode()).entity(mapOut.writeValueAsString(reply)).build();
        } catch (JsonProcessingException e) {
            System.out.println("/train: Error producing JSON reply: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("tater/train: Error producing JSON reply.").build();
        }
    }

    /**
     * Exposes a verification function through which the Authentication Server can attempt to verify a photo of a face
     * against a person's ID.
     * @param JSON A JSON verification request from the Authentication Server.
     * @return An HTTP response and a JSON data-bound object. See the Facilitator Interface Specifcation and the
     *            definition for the AuthResponseVerify object.
     */
    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(String JSON) {
        ObjectMapper mapIn = this.mapper.copy();
        mapIn.addMixIn(ImageData.class, MxImageDataAuthRequest.class);
        AuthRequestVerify req;
        try {
            req = mapIn.readValue(JSON, AuthRequestVerify.class);
        } catch (IOException e) {
            System.out.println("Error reading JSON Verify Request: " + e.getMessage());
            e.printStackTrace();
            return Response.status(400).entity("Error reading JSON request.").build();
        }
        System.out.println("UserId: " + req.getInternalID());
        System.out.println("ImageId: " + req.getImage().getImageID());
        System.out.println("ImageB64: " + DatatypeConverter.printBase64Binary(req.getImage().getImageBinary()));

        // Dummy list until spec is fixed to include FacilitatorIDs
        List<FacilitatorID> dummy = new ArrayList<FacilitatorID>();

        SMVerifyData result = services.verify(req.getInternalID(), dummy, req.getImage());
        AuthResponseVerify reply = new AuthResponseVerify();
        reply.setInternalID(result.getInternalID());
        reply.setHTTPStatusCode(200);
        reply.setMatch(result.isMatch());

        return Response.status(reply.getHTTPStatusCode()).entity(reply).build();
    }

    /**
     * A diagnostic function for determining if the FacilitatorService is live and functioning.
     * @param message A string that will be repeated back to the requesting client.
     * @return A hello message featuring the provided string argument.
     */
    @GET
    @Path("/test/{param}")
    public Response test(@PathParam("param") String message) {
        String output = "Tater, the Facilitating Potato, is alive and says " + message;
        return Response.status(Response.Status.OK).entity(output).build();
    }
}
