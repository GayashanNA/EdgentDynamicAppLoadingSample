package org.apache.edgent.sample.dynamic;

import org.apache.edgent.execution.services.ControlService;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.runtime.appservice.AppService;
import org.apache.edgent.runtime.jsoncontrol.JsonControlService;
import org.apache.edgent.sample.common.Configurations;
import org.apache.edgent.sample.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 */
public class QueuedTopologyLoader implements TopologyLoader{
    private static final Logger logger = LoggerFactory.getLogger(QueuedTopologyLoader.class);

    @Override
    public String getTopologyJarUrl() {
        throw new UnsupportedOperationException("In the queued topology loader, jar url is not avaialble.");
    }

    @Override
    public void loadAndRunTopology() {
        Utils.configureLogging("dynamic-loader/src/main/resources/log4j.xml");

        DirectProvider provider = new DirectProvider();
        JsonControlService jsonControlService = new JsonControlService();
        provider.getServices().addService(ControlService.class, jsonControlService);
        AppService service = (AppService) AppService.createAndRegister(provider, provider);

        try {
            TopologyConsumer topologyConsumer = new TopologyConsumer(Configurations.ENDPOINT_NAME, jsonControlService);
            Thread consumerThread = new Thread(topologyConsumer);
            consumerThread.start();
            logger.info(service.getApplicationNames().toString());
        } catch (IOException e) {
            logger.error("Unable to load the jar : " + e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Unable to submit the control request : " + e.getLocalizedMessage());
        }
    }
}
