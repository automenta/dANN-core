/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

import java.util.*;
import java.util.Map.Entry;

public class ImmutableHyperAdjacencyGraph<N, E extends Hyperedge<N>> extends AbstractHyperAdjacencyGraph<N, E> {
    private static final long serialVersionUID = 8503954045220921240L;

    public ImmutableHyperAdjacencyGraph() {
        super();
    }

    public ImmutableHyperAdjacencyGraph(final Graph<N, E> copyGraph) {
        super(copyGraph);
    }

    public ImmutableHyperAdjacencyGraph(final Set<N> nodes, final Set<E> edges) {
        super(nodes, edges);
    }

    @Override
    protected Set<E> getInternalEdges() {
        return Collections.unmodifiableSet(super.getInternalEdges());
    }

    @Override
    protected Map<N, Set<E>> getInternalAdjacencyEdges() {
        final Map<N, Set<E>> newAdjacentEdges = new HashMap<>();
        for (final Entry<N, Set<E>> neighborEdgeEntry : super.getInternalAdjacencyEdges().entrySet())
            newAdjacentEdges.put(neighborEdgeEntry.getKey(), new HashSet<>(neighborEdgeEntry.getValue()));
        return newAdjacentEdges;
    }

    @Override
    protected Map<N, List<N>> getInternalAdjacencyNodes() {
        final Map<N, List<N>> newAdjacentNodes = new HashMap<>();
        for (final Entry<N, List<N>> neighborNodeEntry : super.getInternalAdjacencyNodes().entrySet())
            newAdjacentNodes.put(neighborNodeEntry.getKey(), new ArrayList<>(neighborNodeEntry.getValue()));
        return newAdjacentNodes;
    }

}
