package org.apache.edgent.sample.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.edgent.runtime.jsoncontrol.JsonControlService;
import org.apache.edgent.topology.mbeans.ApplicationServiceMXBean;
import org.apache.edgent.topology.services.ApplicationService;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 */
public class Utils {
    public static JsonObject newOperationRequest(String payload, String operation) {
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

    public static void configureLogging(String log4jXmlPath) {
        // configure log4j
        DOMConfigurator.configure(log4jXmlPath);
    }
}
