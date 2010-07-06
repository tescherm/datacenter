package com.quora.challenge;

import com.quora.challenge.graph.Graph;

/**
 * Holds path generation statistics.
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type
 */
public final class PathStatistics<V> {

    public static class Builder<V> {

        private Graph<V> graph;

        private long elapsedTime; // in milliseconds
        private long depth;
        private long pathCount;

        public final PathStatistics<V> build() {
            if (graph == null)
                throw new NullPointerException();
            return new PathStatistics<V>(this);
        }

        public final Builder<V> depth(final long depth) {
            this.depth = depth;
            return this;
        }

        public final Builder<V> count(final long count) {
            this.pathCount = count;
            return this;
        }

        public final Builder<V> graph(final Graph<V> graph) {
            this.graph = graph;
            return this;
        }

        public final Builder<V> elapsedTime(final long elapsedTime) {
            this.elapsedTime = elapsedTime;
            return this;
        }
    }

    private final long depth;
    private final long pathCount;
    private final long elapsedTime;

    private final Graph<V> graph;

    public PathStatistics(final Builder<V> builder) {
        this.depth = builder.depth;
        this.pathCount = builder.pathCount;
        this.elapsedTime = builder.elapsedTime;
        this.graph = builder.graph;
    }

    /**
     * @return the search depth
     */
    public long getDepth() {
        return depth;
    }

    /**
     * @return the number of paths
     */
    public long getPathCount() {
        return pathCount;
    }

    /**
     * @return the elapsed time
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * @return the graph associated with these statistics
     */
    public Graph<V> getGraph() {
        return graph;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Path statistics for graph: \n" + graph);
        sb.append("\n=============");
        sb.append("\n");
        sb.append("Search depth: " + depth);
        sb.append("\n");
        sb.append("Elapsed time: " + elapsedTime + " ms");
        sb.append("\n");
        sb.append(" Total paths: " + pathCount);
        sb.append("\n=============");
        return sb.toString();
    }
}
