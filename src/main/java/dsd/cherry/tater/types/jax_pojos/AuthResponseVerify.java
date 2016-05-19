package dsd.cherry.tater.types.jax_pojos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dsd.cherry.tater.types.StatusCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Beach on 5/3/2016.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class AuthResponseVerify {
    private String internalID;
    private boolean match;
    private List<StatusCode> codes;
    private int HTTPStatusCode;

    public AuthResponseVerify() {
        codes = new ArrayList<StatusCode>();
    }

    public void setInternalID(String internalID) { this.internalID = internalID; }

    @JsonProperty("UserId")
    public String getInternalID() { return internalID; }

    public void addStatusCode(StatusCode code) {
        codes.add(code);
    }

    @JsonProperty("CodeError")
    public List<StatusCode> getStatusCodes() {
        return codes;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    @JsonProperty("IsSamePerson")
    public boolean getMatch() { return match; }

    public void setHTTPStatusCode(int code) { this.HTTPStatusCode = code; }

    public int getHTTPStatusCode() { return HTTPStatusCode; }
}
