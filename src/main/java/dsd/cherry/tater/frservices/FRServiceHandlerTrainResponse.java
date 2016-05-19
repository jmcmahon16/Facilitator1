package dsd.cherry.tater.frservices;

import dsd.cherry.tater.types.ImageData;

import java.util.List;

/**
 * Created by James Beach on 4/29/2016.
 */
public class FRServiceHandlerTrainResponse {
    private String serviceName;
    private boolean serviceResponded;
    private String FRPersonID;
    private boolean isTrained;
    private List<ImageData> images;

    /**
     * <b>Constructor.</b> Build this return type to provide accurate information about the results of training a
     * facial recognitions service. This constructor is protected such that it can only be accessed from within the
     * package or from subclasses. This is the only place these fields can be initialized such that they cannot be
     * changed later.
     * @param serviceName The name of the service, should be identical to the service name or ID provided elsewhere.
     * @param serviceResponded True if the service responded within the allotted timeout period; false otherwise.
     * @param FRPersonID The ID of the person whose identity has been or is being trained. Note that in the case that
     *                   the service returns its own internally-generated ID, that ID should be returned here.
     *                   Otherwise, return the internal ID supplied by the ServiceManager unchanged.
     * @param isTrained True if the service is sufficiently trained; false otherwise.
     */
    protected FRServiceHandlerTrainResponse(String serviceName,
                                            boolean serviceResponded,
                                            String FRPersonID,
                                            boolean isTrained,
                                            List<ImageData> images) {
        this.serviceName = serviceName;
        this.serviceResponded = serviceResponded;
        this.FRPersonID = FRPersonID;
        this.isTrained = isTrained;
        this.images = images;
    }

    public String getServiceName() { return serviceName; }

    public boolean getServiceResponded() { return serviceResponded; }

    public String getFRPersonID() { return FRPersonID; }

    public boolean getTrainingStatus() { return isTrained; }

    public List<ImageData> getImageData() { return images; }
}
