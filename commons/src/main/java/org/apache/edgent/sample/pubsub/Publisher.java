package org.apache.edgent.sample.pubsub;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 */
public class Publisher extends EndPoint {

    public Publisher(String endpointName) throws IOException {
        super(endpointName);
    }

    public void publish(Serializable object) throws IOException {
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
    }
}
