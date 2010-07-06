package com.quora.challenge.graph.ham;

import com.quora.challenge.graph.ham.pruning.BiConnectedPruningStrategy;
import com.quora.challenge.graph.ham.pruning.ConnectedPruningStrategy;
import com.quora.challenge.graph.ham.pruning.EdgePruningStrategy;
import com.quora.challenge.graph.ham.pruning.AdmissibleDegreePruningStrategy;

/**
 * Factory for a {@link HamiltonianPathCounter}
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type associated with this path counter.
 */
public final class HamiltonianPathCounterFactory<V> {

    /**
     * suppress default constructor
     */
    private HamiltonianPathCounterFactory() {
        throw new AssertionError();
    }

    /**
     * Obtains a path counter instance.
     * 
     * @param <V>
     *            is the vertex type associated with this path counter.
     * @return a {@link HamiltonianPathCounter} instance.
     */
    public static <V> HamiltonianPathCounter<V> getInstance() {
        return new HamiltonianPathCounter<V>(
                new BiConnectedPruningStrategy<V>(), 
                new ConnectedPruningStrategy<V>(),
                new AdmissibleDegreePruningStrategy<V>(), 
                new EdgePruningStrategy<V>()
        );
    }

}
