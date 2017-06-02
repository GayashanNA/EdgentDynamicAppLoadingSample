package org.apache.edgent.topology;

import com.google.gson.JsonObject;
import org.apache.edgent.function.BiConsumer;
import org.apache.edgent.function.Supplier;
import org.apache.edgent.topology.sensors.RandomTemperatureSensor;
import org.apache.edgent.topology.services.TopologyBuilder;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class RandomTemperatureTopology implements TopologyBuilder{
    @Override
    public String getName() {
        return "RandomTemperatureTopologyApp";
    }

    @Override
    public BiConsumer<Topology, JsonObject> getBuilder() {
        Supplier<Double> sensor = new RandomTemperatureSensor();
        return (t, c) -> t.poll(sensor, 2, TimeUnit.SECONDS)
                .filter(temp -> temp > 30.0)
                .last(5, tuple -> 0)
                .aggregate((temps, key) -> temps.stream().mapToDouble(temp -> temp).average())
                .sink(tuple -> System.out.println(tuple.orElse(0.0)));
    }
}
