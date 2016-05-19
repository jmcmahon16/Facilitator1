package dsd.cherry.tater.types;

import java.util.List;

/**
 * Created by James Beach on 5/1/2016.
 */
public class SMTrainData {
    private String internalID;
    private List<FRServiceHandlerTrainResponse> responses;
    private List<ImageData> images;
    private boolean isTrained;

    public SMTrainData(String internalID,
                       boolean isTrained,
                       List<FRServiceHandlerTrainResponse> responses,
                       List<ImageData> images) {
        this.internalID = internalID;
        this.isTrained = isTrained;
        this.responses = responses;
        this.images = images;
    }

    public String getInternalID() { return internalID; }

    public boolean getTrainingStatus() { return isTrained; }

    public List<ImageData> getImageData() { return images; }

    public List<FRServiceHandlerTrainResponse> getResponses() { return responses; }
}
