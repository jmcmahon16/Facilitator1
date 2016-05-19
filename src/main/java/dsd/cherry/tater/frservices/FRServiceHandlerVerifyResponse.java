package dsd.cherry.tater.frservices;

/**
 * Created by James Beach on 4/29/2016.
 */
public class FRServiceHandlerVerifyResponse {
    private String serviceName;
    private boolean serviceResponded;
    private float confidence, cutoff;
    private String FRPersonID;

    /**
     * <b>Constructor.</b> Build this return type to provide accurate information about the results provided by the
     * facial recognition service. This constructor is protected and can only be accessed from within the package or
     * from subclasses. This constructor is the only place these fields can be initialized such that they cannot be
     * changed later.
     * @param serviceName The name of the service, should be identical to the service name or ID provided elsewhere.
     * @param serviceResponded True if the service responded within the allotted timeout period; false otherwise.
     * @param confidence A value between 0 and 1 (inclusive) representing the raw confidence reported by the service.
     * @param cutoff A value between 0 and 1 (inclusive) indicating the minimum confidence constituting a match.
     * @param FRPersonID The ID of the person whose identity was being verified.
     */
    protected FRServiceHandlerVerifyResponse(String serviceName,
                                             boolean serviceResponded,
                                             float confidence,
                                             float cutoff,
                                             String FRPersonID) {
        this.serviceName = serviceName;
        this.serviceResponded = serviceResponded;
        this.confidence = confidence;
        this.cutoff = cutoff;
        this.FRPersonID = FRPersonID;
    }

    public String getServiceName() { return serviceName; }

    public boolean getServiceResponded() { return serviceResponded; }

    public float getConfidence() { return confidence; }

    public float getCutoff() { return cutoff; }

    public String getFRPersonID() { return FRPersonID; }
}
