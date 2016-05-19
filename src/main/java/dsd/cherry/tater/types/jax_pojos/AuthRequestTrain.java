package dsd.cherry.tater.types.jax_pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dsd.cherry.tater.types.FacilitatorID;
import dsd.cherry.tater.types.ImageData;

import java.util.List;

/**
 * Created by James Beach on 4/29/2016.
 */
public class AuthRequestTrain {
    private String internalID;
    private List<FacilitatorID> FACIDs;
    private List<ImageData> images;

    @JsonProperty("UserId")
    public void setInternalID(String internalID) { this.internalID = internalID; }
    @JsonProperty("UserId")
    public String getInternalID() { return internalID; }

    @JsonProperty("FacilitatorIds")
    public void setFACIDs(List<FacilitatorID> FACIDs) { this.FACIDs = FACIDs; }
    @JsonProperty("FacilitatorIds")
    public List<FacilitatorID> getFACIDs() { return FACIDs; }

    @JsonProperty("Images")
    public void setImages(List<ImageData> images) { this.images = images; }
    @JsonProperty("Images")
    public List<ImageData> getImages() { return images; }
}
