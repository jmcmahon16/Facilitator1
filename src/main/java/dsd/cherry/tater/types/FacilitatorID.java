package dsd.cherry.tater.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by James Beach on 4/29/2016.
 */
public class FacilitatorID {
    private String FR_service;
    private String FR_userID;

    @JsonProperty("FacType")
    public void setFRService(String FR_service) { this.FR_service = FR_service; }
    @JsonProperty("FacType")
    public String getFRService() { return FR_service; }

    @JsonProperty("FacId")
    public void setFRPersonID(String FR_userID) { this.FR_userID = FR_userID; }
    @JsonProperty("FacId")
    public String getFRPersonID() { return FR_userID; }
}
