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
package com.syncleus.dann.graph.cycle;

import com.syncleus.dann.graph.Cycle;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.SimpleCycle;
import com.syncleus.dann.graph.TraversableCloud;
import com.syncleus.dann.graph.topological.Topography;

import java.io.Serializable;
import java.util.*;

public class ExhaustiveDepthFirstSearchCycleFinder<N, E extends TraversableCloud<N>> extends ColoredDepthFirstSearchDetector implements CycleFinder<N, E> {
    private static <N, E extends TraversableCloud<N>> void cyclesFromStart(final Graph<N, E> graph, final Set<N> untouchedNodes, final Set<Cycle<N, E>> cycles, final N startNode) {
        final Stack<N> parentNodes = new Stack<>();
        final Stack<E> parentEdges = new Stack<>();

        ExhaustiveDepthFirstSearchCycleFinder.traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, null, startNode);
    }

    private static <N, E extends TraversableCloud<N>> List<N> neighborsFromEdge(final E edge, final N sourceNode) {
        final List<N> destinations = new ArrayList<>(edge.getNodes());
        destinations.remove(sourceNode);
        return destinations;
    }

    private static <N, E extends TraversableCloud<N>> void traverse(final Graph<N, E> graph, final Set<N> untouchedNodes, final Set<Cycle<N, E>> cycles, final Stack<N> parentNodes, final Stack<E> parentEdges, final E lastEdge, final N currentNode) {
        untouchedNodes.remove(currentNode);
        parentNodes.push(currentNode);
        parentEdges.push(lastEdge);

        final List<E> unexploredPaths = new ArrayList<>(graph.getTraversableEdges(currentNode));
        if (lastEdge != null)
            unexploredPaths.remove(lastEdge);
        int cyclesFound = 0;

        for (final E unexploredPath : unexploredPaths) {
            for (final N neighborNode : neighborsFromEdge(unexploredPath, currentNode)) {
                if (parentNodes.contains(neighborNode)) {
                    final List<N> parentNodesCopy = new ArrayList<>(parentNodes);
                    final List<E> parentEdgesCopy = new ArrayList<>(parentEdges);
                    final List<E> cycleEdges = new ArrayList<>();
                    final List<N> cycleNodes = new ArrayList<>();
                    cycleEdges.add(unexploredPath);
                    cycleNodes.add(neighborNode);

                    N currentCycleNode = parentNodesCopy.get(parentNodesCopy.size() - 1);
                    cycleNodes.add(currentCycleNode);
                    parentNodesCopy.remove(currentCycleNode);
                    while (currentCycleNode != neighborNode) {
                        currentCycleNode = parentNodesCopy.get(parentNodesCopy.size() - 1);
                        parentNodesCopy.remove(currentCycleNode);

                        final E currentCycleEdge = parentEdgesCopy.get(parentEdgesCopy.size() - 1);
                        parentEdgesCopy.remove(currentCycleEdge);

                        if ((currentCycleEdge != null)) {
                            cycleEdges.add(currentCycleEdge);
                            cycleNodes.add(currentCycleNode);
                        }
                    }

                    cycles.add(new SimpleCycle<>(cycleEdges, cycleNodes));
                    cyclesFound++;
                } else {
                    traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, unexploredPath, neighborNode);
                }
            }
        }

        parentNodes.pop();
        parentEdges.pop();
    }

    @Override
    public boolean isPancyclic(final Graph<N, E> graph) {
        if (!Topography.isSimple(graph))
            return false;

        final int graphOrder = Topography.getOrder(graph);
        final Set<Cycle<N, E>> cycles = this.findCycles(graph);
        final SortedSet<Cycle<N, E>> sortedCycles = new TreeSet<>(new CycleLengthComparator<>());
        sortedCycles.addAll(cycles);

        int neededCycleSize = 3;
        for (final Cycle<N, E> sortedCycle : sortedCycles) {
            final int currentCycleSize = sortedCycle.getLength();
            if (currentCycleSize == neededCycleSize) {
                if (currentCycleSize == graphOrder)
                    return true;
                else
                    neededCycleSize++;
            } else if (currentCycleSize > neededCycleSize)
                return false;
        }
        return false;
    }

    public boolean isUnicyclic(final Graph<N, E> graph) {
        return ((this.findCycles(graph).size() == 1) && (Topography.isSimple(graph)));
    }

    public int cycleCount(final Graph<N, E> graph) {
        return this.findCycles(graph).size();
    }

    public int girth(final Graph<N, E> graph) {
        final Set<Cycle<N, E>> cycles = this.findCycles(graph);
        final SortedSet<Cycle<N, E>> sortedCycles = new TreeSet<>(new CycleLengthComparator<>());
        sortedCycles.addAll(cycles);
        return sortedCycles.first().getLength();
    }

    public int circumference(final Graph<N, E> graph) {
        final Set<Cycle<N, E>> cycles = this.findCycles(graph);
        final SortedSet<Cycle<N, E>> sortedCycles = new TreeSet<>(new CycleLengthComparator<>());
        sortedCycles.addAll(cycles);
        return sortedCycles.last().getLength();
    }

    public Set<Cycle<N, E>> findCycles(final Graph<N, E> graph) {
        final Set<N> untouchedNodes = new HashSet<>(graph.getNodes());
        final Set<Cycle<N, E>> cycles = new HashSet<>();
        while (!untouchedNodes.isEmpty()) {
            final N startingNode = untouchedNodes.iterator().next();
            untouchedNodes.remove(startingNode);
            ExhaustiveDepthFirstSearchCycleFinder.cyclesFromStart(graph, untouchedNodes, cycles, startingNode);
        }
        return cycles;
    }

    private static class CycleLengthComparator<N, E extends TraversableCloud<N>> implements Comparator<Cycle<N, E>>, Serializable {
        private static final long serialVersionUID = 5175815460016788908L;

        public int compare(final Cycle<N, E> first, final Cycle<N, E> second) {
            if (first.getLength() < second.getLength())
                return -1;
            else if (first.getLength() > second.getLength())
                return 1;
            return 0;
        }

        @Override
        public boolean equals(final Object compareWith) {
            return (compareWith instanceof CycleLengthComparator);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
