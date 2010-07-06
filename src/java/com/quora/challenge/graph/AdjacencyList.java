package com.quora.challenge.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Adjacency List graph representation backed by a {@link Multimap}. Classes inserted into the list should have proper
 * implementations of hashCode() and equals() just like for a {@link Set}
 * <p/>
 * Note that this class is not thread safe.
 * 
 * @author mattt
 */
public final class AdjacencyList<E> {

    private final Multimap<E, E> adjacencylist;

    public AdjacencyList() {
        this.adjacencylist = HashMultimap.create();
    }

    private AdjacencyList(final Multimap<E, E> adjacencylist) {
        this.adjacencylist = adjacencylist;
    }

    /**
     * Adds a directed edge from the given source vertex to the given destination vertex. If neither the source nor the
     * destination exist they will be created.
     * 
     * @param source
     *            is the source vertex to add an outbound edge for.
     * @param dest
     *            is the destination vertex to add an inbound edge for.
     */
    public void addEdge(final E source, final E dest) {
        if (source == null)
            throw new NullPointerException();
        if (dest == null)
            throw new NullPointerException();

        this.adjacencylist.put(source, dest);

    }

    /**
     * Removes an edge between the given source vertex and destination vertex. If no edge exists between the verticies,
     * or the edge could not be remove, <code>false</code> will be returned.
     * 
     * @param source
     *            is the source vertex to remove the edge for.
     * @param dest
     *            is the destination edge to remove the edge for.
     * @return true if the edge was removed, false otherwise.
     */
    public boolean removeEdge(final E source, final E dest) {
        if (source == null)
            throw new NullPointerException();
        if (dest == null)
            throw new NullPointerException();

        final Collection<E> vertices = this.adjacencylist.get(source);
        if (vertices.isEmpty())
            return false;

        return vertices.remove(dest);
    }

    /**
     * Adds a vertex.
     * 
     * @param source
     *            the source vertex to add
     */
    public void addVertex(final E source) {
        if (source == null)
            throw new NullPointerException();

        if (!this.adjacencylist.containsKey(source)) {
            this.adjacencylist.put(source, null);
        }
    }

    /**
     * Removes the given vertex. TODO don't scan all values...
     * 
     * @param source
     *            is the vertex to removes
     */
    public void removeVertex(final E source) {
        this.adjacencylist.removeAll(source);
        this.adjacencylist.values().remove(source);
    }

    /**
     * Obtains the number of outbound edges for the given vertex.
     * 
     * @param vertex
     *            is the vertex to obtain the outdegree count for.
     * @return the outdegree count for the given vertex
     */
    public int getOutdegreeCount(final E vertex) {
        final Collection<E> source = this.adjacencylist.get(vertex);
        if (source.isEmpty())
            return 0;
        return source.size();
    }

    /**
     * Determines if an edge exists between the given source vertex and destination vertex.
     * 
     * @param source
     *            is the source vertex
     * @param dest
     *            is the destination vertex
     * @return true if an edge exists between the two verticies, false otherwise.
     */
    public boolean containsEdge(final E source, final E dest) {
        Collection<E> vertices = this.adjacencylist.get(source);
        return vertices != null && vertices.contains(dest);
    }

    /**
     * Obtains the collection of verticies in this list.
     * 
     * @return an immutable {@link Collection} of verticies in this list.
     */
    public Set<E> getVerticies() {
        return Collections.unmodifiableSet(this.adjacencylist.keySet());
    }

    /**
     * Obtains the collection of verticies accessible from the given source vertex. In a directed graph, this will
     * return all vertices accessible via an outbound arc but not an inbound arc. In an undirected graph it will return
     * all verticies connected to it via an undirected edge.
     * 
     * @param source
     *            is the source vertex to obtain accessible verticies for.
     * @return an immutable {@link Collection} of verticies accessible from the given source vertex.
     */
    public Collection<E> getVerticesFromSource(E source) {
        final Collection<E> dests = this.adjacencylist.get(source);
        if (dests.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableCollection(dests);
    }

    /**
     * Count vertices in this list.
     * 
     * @return the number of vertices in this graph
     */
    public int vertexCount() {
        return this.adjacencylist.keySet().size();
    }

    /**
     * Count edges in this list.
     * 
     * @return the number of edges in this graph
     */
    public int edgeCount() {
        return this.adjacencylist.size();
    }

    /**
     * Determines if the list is empty.
     * 
     * @return true if it is empty, false otherwise.
     */
    public boolean isEmpty() {
        // NOTE: this works there isn't a way to remove edges except sources.
        return this.adjacencylist.isEmpty();
    }

    /**
     * Perform a topological sort against a DAG TODO untested...
     * 
     * @author mattt
     */
    private final class TopoSorter {
        private final Set<E> visited = new HashSet<E>();
        private final List<E> sorted = new LinkedList<E>();

        List<E> reverseTopologicalSort() {

            final Set<E> sources = adjacencylist.keySet();
            for (final E source : sources) {
                visit(source);
            }

            assert vertexCount() == sorted.size();

            return sorted;
        }

        /**
         * @param vertex
         */
        private void visit(final E vertex) {
            if (visited.contains(vertex))
                return;
            visited.add(vertex);

            final Collection<E> dests = getVerticesFromSource(vertex);
            for (final E dest : dests) {
                visit(dest);
            }
            sorted.add(vertex);
        }
    }

    public List<E> topologicalSort() {
        final List<E> sorted = reverseTopologicalSort();
        Collections.reverse(sorted);
        return sorted;
    }

    public List<E> reverseTopologicalSort() {
        return new TopoSorter().reverseTopologicalSort();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" ").append(this.vertexCount()).append(" vertices");
        for (final Entry<E, Collection<E>> entry : this.adjacencylist.asMap().entrySet()) {
            sb.append("\n===========");
            final E src = entry.getKey();
            final Collection<E> dests = entry.getValue();
            sb.append(src).append(" => ");
            if (dests != null) {
                sb.append(dests);
            }
        }
        sb.append("\n\n=========").append(this.adjacencylist.size()).append(" total edges\n");
        return sb.toString();
    }

    /**
     * @return the adjacency list
     */
    private Multimap<E, E> getAdjacencyList() {
        return adjacencylist;
    }

    /**
     * Obtains a new {@link AdjacencyList} instance containing the elements of the given list.
     * 
     * @param <E>
     *            is the element type
     * @param list
     *            is the {@link AdjacencyList} to obtain a new instance for.
     * @return a new {@link AdjacencyList} instance containing the elements of the given list.
     */
    public static <E> AdjacencyList<E> newInstance(final AdjacencyList<E> list) {
        return new AdjacencyList<E>(HashMultimap.create(list.getAdjacencyList()));
    }
}
