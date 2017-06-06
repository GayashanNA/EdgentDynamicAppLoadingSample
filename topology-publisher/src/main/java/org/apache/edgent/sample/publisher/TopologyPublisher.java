package org.apache.edgent.sample.publisher;

import org.apache.edgent.sample.common.Configurations;
import org.apache.edgent.sample.common.Utils;
import org.apache.edgent.sample.pubsub.Publisher;
import org.apache.edgent.sample.pubsub.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TopologyPublisher {
    private static final Logger logger = LoggerFactory.getLogger(TopologyPublisher.class);

    public static void main(String[] args) {
        Utils.configureLogging("topology-publisher/src/main/resources/log4j.xml");
        try {
            Publisher publisher = new Publisher(Configurations.ENDPOINT_NAME);
            File cpuTempAppJar = new File(Configurations.TOPOLOGY_JAR_PATH);
            Topology topology = new Topology(Configurations.TOPOLOGY_APP_NAME, cpuTempAppJar);

            publisher.publish(topology);
            logger.info("Published the jar at : " + cpuTempAppJar.getAbsolutePath());
            publisher.close();
        } catch (IOException e) {
            logger.error("Error while publishing the jar", e);
        }
    }
}
