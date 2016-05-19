package dsd.cherry.tater.types;

import java.util.List;

/**
 * Created by James Beach on 5/3/2016.
 */
public class SMVerifyData {
    private String internalID;
    private List<FRServiceHandlerVerifyResponse> inConsensus;
    private List<FRServiceHandlerVerifyResponse> notInConsensus;
    private int     totalServices,  totalResponded;
    private float   confidence,     cutoff;
    private boolean match,          consensus;

    public SMVerifyData(String                                  internalID,
                        boolean                                 match,
                        List<FRServiceHandlerVerifyResponse>    inConsensus,
                        List<FRServiceHandlerVerifyResponse>    notInConsensus,
                        int                                     totalServices,
                        int                                     totalResponded,
                        float                                   confidence,
                        float                                   cutoff,
                        boolean                                 consensus) {
        this.internalID = internalID;
        this.inConsensus = inConsensus;
        this.totalServices = totalServices;
        this.totalResponded = totalResponded;
        this.confidence = confidence;
        this.cutoff = cutoff;
        this.match = match;
        this.consensus = consensus;
    }

    public String getInternalID() { return internalID; }

    public boolean isMatch() { return match; }

    public List<FRServiceHandlerVerifyResponse> getInConsensus() { return inConsensus; }

    public List<FRServiceHandlerVerifyResponse> getNotInConsensus() { return notInConsensus; }

    public int getTotalServices() { return totalServices; }

    public int getTotalResponsed() { return totalResponded; }

    public float getConfidence() { return confidence; }

    public float getCutoff() { return cutoff; }

    public boolean isConsensus() { return consensus; }
}
