package org.apache.edgent.sample.dynamic;

/**
 *
 */
public interface TopologyLoader {
    String getTopologyJarUrl();

    void loadAndRunTopology();
}
