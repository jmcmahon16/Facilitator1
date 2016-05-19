package dsd.cherry.tater.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James Beach on 4/29/2016.
 */
public class ImageData {
    private String imageID;
    private byte[] image;
    private List<StatusCode> codes;
    private boolean accepted;

    private ImageData() {
        codes = new ArrayList<StatusCode>();
        accepted = false;
    }

    public void setImageBinary(byte[] image) {
        this.image = image;
    }
    public byte[] getImageBinary() {
        return this.image;
    }

    public void setImageID(String imageID) { this.imageID = imageID; }
    public String getImageID() { return imageID; }

    public void addCode(StatusCode code) { codes.add(code); }
    public void setCodes(List<StatusCode> codes) { this.codes = codes; }

    public StatusCode getCode() {
        for (StatusCode code : codes) {
            if (!code.equals(StatusCode.IMAGE_OK)) return code;
        }
        return StatusCode.IMAGE_OK;
    }

    public Map<String,Object> getCodeJSON() {
        StatusCode notable = StatusCode.IMAGE_OK;
        for (StatusCode code : codes) {
            if (!code.equals(StatusCode.IMAGE_OK)) notable = code;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("appCode",notable.getAppCode());
        result.put("message",notable.getMessage());

        return result;
    }

    public void setAcceptedByService(boolean accepted) { this.accepted = accepted; }
    public boolean getAcceptedByService() { return accepted; }
}
