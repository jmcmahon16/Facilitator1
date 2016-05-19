package dsd.cherry.tater.frservices;

import dsd.cherry.tater.types.ImageData;

import java.util.List;

/**
 * Created by James Beach on 4/29/2016.
 */
public abstract class FRServiceHandler {
    private int timeoutSec;

    abstract public FRServiceHandlerTrainResponse train(String userID, List<ImageData> images);

    abstract public FRServiceHandlerVerifyResponse verify(String personID, ImageData image);

    abstract public String getFRServiceName();

    abstract public float getFRServiceCutoff();

    final public void setTimeout(int seconds) {
        timeoutSec = seconds;
    }
}
