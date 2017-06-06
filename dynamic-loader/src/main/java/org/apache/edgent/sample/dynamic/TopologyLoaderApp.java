package org.apache.edgent.sample.dynamic;

import org.apache.edgent.sample.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class TopologyLoaderApp {
    private static final Logger logger = LoggerFactory.getLogger(TopologyLoaderApp.class);

    public static void main(String[] args) {
        Utils.configureLogging("dynamic-loader/src/main/resources/log4j.xml");

        // default topology loader is DirectTopologyLoader
        TopologyLoader topologyLoader = new DirectTopologyLoader();

        List<String> arguments = Arrays.asList(args);
        if (!arguments.isEmpty()){
            if (arguments.contains("queue")){
                topologyLoader = new QueuedTopologyLoader();
            }
        }
        topologyLoader.loadAndRunTopology();
    }
}
