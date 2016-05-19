package dsd.cherry.tater.types.jax_pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dsd.cherry.tater.types.FacilitatorID;
import dsd.cherry.tater.types.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Beach on 5/2/2016.
 */
public class AuthResponseTrain {
    private String internalID;
    private List<FacilitatorID> FACIDs;
    private List<ImageData> images;
    private boolean isTrained;
    private int HTTPCode;

    public AuthResponseTrain() {
        FACIDs = new ArrayList<FacilitatorID>();
        images = new ArrayList<ImageData>();
        isTrained = false;
        HTTPCode = 200;
    }

    @JsonProperty("UserId")
    public void setInternalID(String internalID) { this.internalID = internalID; }
    @JsonProperty("UserId")
    public String getInternalID() { return internalID; }

    @JsonProperty("FacilitatorId")
    public void setFACIDs(List<FacilitatorID> FACIDs) { this.FACIDs = FACIDs; }
    @JsonProperty("FacilitatorId")
    public List<FacilitatorID> getFACIDs() { return FACIDs; }

    @JsonProperty("Images")
    public void setImages(List<ImageData> images) { this.images = images; }
    @JsonIgnore
    public void addImage(ImageData image) { images.add(image); }
    @JsonProperty("Images")
    public List<ImageData> getImages() { return images; }

    @JsonProperty("Success")
    public void setTrainingStatus(boolean isTrained) { this.isTrained = isTrained; }
    @JsonIgnore
    public void setAsTrained() { this.isTrained = true; }
    @JsonIgnore
    public void setAsUntrained() { this.isTrained = false; }
    @JsonProperty("Success")
    public boolean getTrainingStatus() { return isTrained; }

    @JsonIgnore
    public void setHTTPCode(int HTTPCode) { this.HTTPCode = HTTPCode; }
    @JsonIgnore
    public int getHTTPCode() { return HTTPCode; }
}
