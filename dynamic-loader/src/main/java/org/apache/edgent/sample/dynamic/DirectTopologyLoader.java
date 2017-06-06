package org.apache.edgent.sample.dynamic;

import com.google.gson.JsonObject;
import org.apache.edgent.execution.services.ControlService;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.runtime.appservice.AppService;
import org.apache.edgent.runtime.jsoncontrol.JsonControlService;
import org.apache.edgent.sample.common.Configurations;
import org.apache.edgent.sample.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DirectTopologyLoader implements TopologyLoader{
    private static final Logger logger = LoggerFactory.getLogger(DirectTopologyLoader.class);

    @Override
    public String getTopologyJarUrl() {
        return "file:" + Configurations.TOPOLOGY_JAR_PATH;
    }

    @Override
    public void loadAndRunTopology() {
        DirectProvider provider = new DirectProvider();
        JsonControlService jsonControlService = new JsonControlService();
        provider.getServices().addService(ControlService.class, jsonControlService);
        AppService service = (AppService) AppService.createAndRegister(provider, provider);

        try {
            String topologyJarUrl = getTopologyJarUrl();
            logger.info("Jar url: " + topologyJarUrl);
            // There are two methods to register an external jar containing a topology.
            // Only one of these methods should be used to load a jar.

            // First method is to call the AppService#registerJar() method as shown below
            // service.registerJar(topologyJarUrl, null);

            // Second method is to load the jar through a control message (this executes the AppService#registerJar
            // method via reflection.
            JsonObject registerJarRequest =
                    Utils.newOperationRequest(topologyJarUrl, "registerJar");
            jsonControlService.controlRequest(registerJarRequest);

            // Once the app has been loaded from one of the above two methods, submit the app to the Provider to
            // execute the loaded topology.
            JsonObject submitCpuTempAppRequest = Utils.newOperationRequest(Configurations.TOPOLOGY_APP_NAME, "submit");
            jsonControlService.controlRequest(submitCpuTempAppRequest);
            logger.info(service.getApplicationNames().toString());
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
    }
}
