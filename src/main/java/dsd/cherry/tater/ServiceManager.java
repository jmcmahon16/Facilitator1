package dsd.cherry.tater;

import dsd.cherry.tater.types.FacilitatorID;
import dsd.cherry.tater.types.ImageData;
import dsd.cherry.tater.types.SMTrainData;
import dsd.cherry.tater.types.SMVerifyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James Beach on 4/30/2016.
 */
public class ServiceManager {
    private int timeoutSec;
    private Map<String,FRServiceHandler> services;

    public ServiceManager(int timeoutSeconds) {
        this.timeoutSec = timeoutSeconds;
        services = new HashMap<String, FRServiceHandler>();
    }

    public int getTimeout() { return timeoutSec; }
    public void setTimeout(int timeoutSeconds) { this.timeoutSec = timeoutSeconds; }

    public void addService(FRServiceHandler service) {
        service.setTimeout(timeoutSec);
        services.put(service.getFRServiceName(), service);
    }

    public SMTrainData train(final String internalID, List<FacilitatorID> FACIDs, List<ImageData> images) {
        boolean trained = true;

        Map<String,String> FRPersonIDs = new HashMap<String, String>();

        for (FacilitatorID f : FACIDs) {
            FRPersonIDs.put(f.getFRService(), f.getFRPersonID());
        }

        List<FRServiceHandlerTrainResponse> responses = new ArrayList<FRServiceHandlerTrainResponse>();

        for (Map.Entry<String,FRServiceHandler> s : services.entrySet()) {
            FRServiceHandlerTrainResponse response;
            if (FRPersonIDs.containsKey(s.getKey())) {
                response = s.getValue().train(FRPersonIDs.get(s.getKey()), images);
            }
            else {
                response = s.getValue().train(internalID, images);
            }
        }

        if (responses.isEmpty()) {
            return new SMTrainData(internalID, trained = false, responses, images);
        }

        for (FRServiceHandlerTrainResponse r : responses) {
            trained = trained && r.getTrainingStatus();
        }

        return new SMTrainData(internalID, trained, responses, images);
    }

    public SMVerifyData verify(final String internalID, List<FacilitatorID> FACIDs, ImageData image) {
        boolean match = false;
        float confidence = 0, cutoff = 0;
        int totalServices = services.size();
        int totalResponded = 0;
        boolean consensus = true;
        List<FRServiceHandlerVerifyResponse> inConsensus = new ArrayList<FRServiceHandlerVerifyResponse>();
        List<FRServiceHandlerVerifyResponse> notInConsensus = new ArrayList<FRServiceHandlerVerifyResponse>();

        for (FacilitatorID facID : FACIDs) {
            FRServiceHandlerVerifyResponse response =
                services.get(facID.getFRService()).verify(facID.getFRPersonID(), image);

            if (response.getServiceResponded()) {
                ++totalResponded;
                confidence += response.getConfidence();
                cutoff += response.getCutoff();
                if (!response.getFRPersonID().equals(facID.getFRPersonID())) {
                    consensus = false;
                    notInConsensus.add(response);
                }
                else {
                    inConsensus.add(response);
                }
            }
        }

        confidence = confidence / totalResponded;
        cutoff = cutoff / totalResponded;
        match = consensus && (confidence >= cutoff);

        return new SMVerifyData(internalID,
                                match,
                                inConsensus,
                                notInConsensus,
                                totalServices,
                                totalResponded,
                                confidence,
                                cutoff,
                                consensus);
    }
}