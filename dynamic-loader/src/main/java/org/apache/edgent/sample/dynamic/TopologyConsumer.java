package org.apache.edgent.sample.dynamic;

import com.google.gson.JsonObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import org.apache.commons.lang.SerializationUtils;
import org.apache.edgent.runtime.jsoncontrol.JsonControlService;
import org.apache.edgent.sample.common.Utils;
import org.apache.edgent.sample.pubsub.SubscribedConsumer;
import org.apache.edgent.sample.pubsub.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class TopologyConsumer extends SubscribedConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TopologyConsumer.class);
    private final JsonControlService jsonControlService;

    public TopologyConsumer(String endpointName, JsonControlService controlService) throws IOException {
        super(endpointName);
        this.jsonControlService = controlService;
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes)
            throws IOException {
        logger.info("Received message from : " + s);
        Topology publishedTopology = (Topology) SerializationUtils.deserialize(bytes);
        File publishedTopologyJar = publishedTopology.getTopology();
        URL publishedTopologyJarUrl = publishedTopologyJar.toURI().toURL();
        logger.info("Jar path : " + publishedTopologyJarUrl.toExternalForm());

        String topologyAppName = publishedTopology.getName();
        logger.info("Topology: " + topologyAppName);

        try {
            JsonObject registerJarRequest =
                    Utils.newOperationRequest(publishedTopologyJarUrl.toExternalForm(), "registerJar");
            jsonControlService.controlRequest(registerJarRequest);

            JsonObject submitCpuTempAppRequest = Utils.newOperationRequest(topologyAppName, "submit");
            jsonControlService.controlRequest(submitCpuTempAppRequest);
        } catch (Exception e) {
            logger.error("Unable to register jar", e);
        }
    }

    @Override
    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, true, this);
        } catch (IOException e) {
            logger.error("Unable to consume the endpoint", e);
        } catch (Exception e) {
            logger.error("Unable to submit the control request", e);

        }
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
