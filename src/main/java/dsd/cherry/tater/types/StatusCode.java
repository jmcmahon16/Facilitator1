package dsd.cherry.tater.types;

import dsd.cherry.tater.types.jax_serializers.StatusCodeSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Corresponds to 'appCode' in the Facilitator Interface Specification.
 * Created by James Beach on 5/1/2016.
 */
@JsonSerialize(using = StatusCodeSerializer.class, as = String.class)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public enum StatusCode {
    IMAGE_OK(1300) {
        @Override
        public String getMessage() {
            return "IMAGE_OK";
        }
        @Override
        public int getHTTPCode() {
            return 200;
        }
    },
    IMAGE_ERROR_UNSUPPORTED_FORMAT(1301) {
        @Override
        public String getMessage() {
            return "IMAGE_ERROR_UNSUPPORTED_FORMAT";
        }
        @Override
        public int getHTTPCode() {
            return 431;
        }
    },
    IMAGE_ERROR_FAILED_TO_DOWNLOAD(1302) {
        @Override
        public String getMessage() {
            return "IMAGE_ERROR_FAILED_TO_DOWNLOAD";
        }
        @Override
        public int getHTTPCode() {
            return 432;
        }
    },
    IMAGE_ERROR_FILE_TOO_LARGE(1303) {
        @Override
        public String getMessage() {
            return "IMAGE_ERROR_FILE_TOO_LARGE";
        }
        @Override
        public int getHTTPCode() {
            return 433;
        }
    },
    IMAGE_ERROR(1304) {
        @Override
        public String getMessage() {
            return "IMAGE_ERROR";
        }
        @Override
        public int getHTTPCode() {
            return 434;
        }
    },
    BAD_TAG(1502) {
        @Override
        public String getMessage() { return "BAD_TAG"; }
        @Override
        public int getHTTPCode() { return 452; }
    };

    private int value;

    @JsonProperty("message")
    public abstract String getMessage();

    public abstract int getHTTPCode();

    @JsonProperty("appCode")
    public int getAppCode() {
        return value;
    }

    StatusCode(int value) { this.value = value; }
}
