package org.apache.edgent.sample.dynamic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.edgent.execution.services.ControlService;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.runtime.appservice.AppService;
import org.apache.edgent.runtime.jsoncontrol.JsonControlService;
import org.apache.edgent.topology.mbeans.ApplicationServiceMXBean;
import org.apache.edgent.topology.services.ApplicationService;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 *
 */
public class DynamicTopologyLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicTopologyLoader.class);

    public static void main(String[] args) {
        configureLogging();

        DirectProvider provider = new DirectProvider();
        JsonControlService jsonControlService = new JsonControlService();
        provider.getServices().addService(ControlService.class, jsonControlService);
        AppService service = (AppService) AppService.createAndRegister(provider, provider);

        File cpuTempAppJar =
                new File("custom-topology/target/custom-topology-1.0.0-jar-with-dependencies.jar");
        try {
            URL cpuTempAppJarUrl = cpuTempAppJar.toURI().toURL();
            // There are two methods to register an external jar containing a topology.
            // Only one of these methods should be used to load a jar.

            // First method is to call the AppService#registerJar() method as shown below
            // service.registerJar(cpuTempAppJarUrl.toExternalForm(), null);

            // Second method is to load the jar through a control message (this executes the AppService#registerJar
            // method via reflection.
            JsonObject registerJarRequest =
                    newOperationRequest(cpuTempAppJarUrl.toExternalForm(), "registerJar");
            jsonControlService.controlRequest(registerJarRequest);

            // Once the app has been loaded from one of the above two methods, submit the app to the Provider to
            // execute the loaded topology.
            JsonObject submitCpuTempAppRequest =
                    newOperationRequest("RandomTemperatureTopologyApp", "submit");
            jsonControlService.controlRequest(submitCpuTempAppRequest);
            LOGGER.info(service.getApplicationNames().toString());
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getLocalizedMessage());
        }
    }


    private static JsonObject newOperationRequest(String payload, String operation) {
        JsonObject request = new JsonObject();
        request.addProperty(JsonControlService.TYPE_KEY, ApplicationServiceMXBean.TYPE);
        request.addProperty(JsonControlService.ALIAS_KEY, ApplicationService.ALIAS);
        JsonArray args = new JsonArray();
        args.add(new JsonPrimitive(payload));
        args.add(new JsonObject());
        request.addProperty(JsonControlService.OP_KEY, operation);
        request.add(JsonControlService.ARGS_KEY, args);

        return request;
    }

    private static void configureLogging() {
        // configure log4j
        DOMConfigurator.configure("dynamic-loader/src/main/resources/log4j.xml");
    }
}
