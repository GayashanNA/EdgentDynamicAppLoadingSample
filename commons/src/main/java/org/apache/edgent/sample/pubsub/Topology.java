package org.apache.edgent.sample.pubsub;

import java.io.File;
import java.io.Serializable;

/**
 *
 */
public class Topology implements Serializable {
    private String name;
    private File topology;

    public Topology(){
    }

    public Topology(String name, File topology){
        this.setName(name);
        this.setTopology(topology);
    }

    public Topology(String name, String topologyUrl){
        this.setName(name);
        this.setTopology(new File(topologyUrl));
    }

    public File getTopology() {
        return topology;
    }

    public void setTopology(File topology) {
        this.topology = topology;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
