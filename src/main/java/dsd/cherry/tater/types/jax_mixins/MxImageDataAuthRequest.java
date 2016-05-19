package dsd.cherry.tater.types.jax_mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the annotations to support necessary JSON data binding to ImageData objects for Authorization
 * Server requests only.
 * Created by James Beach on 5/1/2016.
 */
public abstract class MxImageDataAuthRequest {
    @JsonProperty("base64_image")
    public abstract void setImageBinary(byte[] image);
    @JsonProperty("base64_image")
    public abstract byte[] getImageBinary();

    @JsonProperty("internal_id")
    public abstract void setImageID(String imageID);
    @JsonProperty("internal_id")
    public abstract String getImageID();
}
