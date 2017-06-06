package org.apache.edgent.sample.pubsub;

import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;

import java.io.IOException;

/**
 *
 */
public abstract class SubscribedConsumer extends EndPoint implements com.rabbitmq.client.Consumer {

    public SubscribedConsumer(String endpointName) throws IOException {
        super(endpointName);
    }

    @Override
    public void handleConsumeOk(String s) {
        getLogger().info("Consumer : " + s + " was registered.");
    }

    @Override
    public void handleCancelOk(String s) {

    }

    @Override
    public void handleCancel(String s) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    @Override
    public void handleRecoverOk(String s) {

    }

    public abstract Logger getLogger();
}
