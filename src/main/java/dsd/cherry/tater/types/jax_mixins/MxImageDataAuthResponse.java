package dsd.cherry.tater.types.jax_mixins;

import com.fasterxml.jackson.annotation.*;
import dsd.cherry.tater.types.StatusCode;

import java.util.List;
import java.util.Map;

/**
 * This class provides the annotations to support necessary JSON data binding to ImageData objects for Authorization
 * Server replies only.
 * Created by James Beach on 5/1/2016.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class MxImageDataAuthResponse {
    @JsonProperty("internal_id")
    public abstract String getImageID();

    @JsonAnyGetter
    public abstract Map<String,Object> getCodeJSON();

    @JsonProperty("isSuccess")
    public abstract boolean getAcceptedByService();
}