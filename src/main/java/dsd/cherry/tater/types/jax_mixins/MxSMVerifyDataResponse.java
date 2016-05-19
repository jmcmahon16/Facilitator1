package dsd.cherry.tater.types.jax_mixins;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by James Beach on 5/4/2016.
 */
// following annotation causes Jackson to ignore everything except the @JsonProperty annotated methods
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class MxSMVerifyDataResponse {
    @JsonProperty("UserId")
    public abstract String getInternalID();
    @JsonProperty("IsSamePerson")
    public abstract boolean isMatch();
}