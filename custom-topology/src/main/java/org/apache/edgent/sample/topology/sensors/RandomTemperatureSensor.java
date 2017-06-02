package org.apache.edgent.sample.topology.sensors;

import org.apache.edgent.function.Supplier;

import java.util.Random;

/**
 *
 */
public class RandomTemperatureSensor implements Supplier<Double>{
    private Random random;
    private double currentTemp;

    public RandomTemperatureSensor(){
        random = new Random();
        currentTemp = 65.0;
    }

    @Override
    public Double get() {
        double newTemp = random.nextGaussian() + currentTemp;
        currentTemp = newTemp;
        return newTemp;
    }
}
