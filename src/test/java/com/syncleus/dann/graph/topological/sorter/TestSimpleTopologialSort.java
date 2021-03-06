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
package com.syncleus.dann.graph.topological.sorter;

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.ImmutableDirectedAdjacencyGraph;
import com.syncleus.dann.graph.ImmutableDirectedEdge;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSimpleTopologialSort {
    @Test
    public void testTopologicalSort() {
        final Set<Object> nodes = new HashSet<>();
        final Object centerNode = "centerNode";
        nodes.add(centerNode);
        final Object topNode = "topNode";
        nodes.add(topNode);
        final Object leftNode = "leftNode";
        nodes.add(leftNode);
        final Object leftiestNode = "leftiestNode";
        nodes.add(leftiestNode);
        final Object rightNode = "rightNode";
        nodes.add(rightNode);

        final Set<DirectedEdge<Object>> edges = new HashSet<>();
        final DirectedEdge<Object> centerTopEdge = new ImmutableDirectedEdge<>(centerNode, topNode);
        edges.add(centerTopEdge);
        final DirectedEdge<Object> centerLeftEdge = new ImmutableDirectedEdge<>(centerNode, leftNode);
        edges.add(centerLeftEdge);
        final DirectedEdge<Object> leftLeftiestEdge = new ImmutableDirectedEdge<>(leftNode, leftiestNode);
        edges.add(leftLeftiestEdge);
        final DirectedEdge<Object> centerRightEdge = new ImmutableDirectedEdge<>(centerNode, rightNode);
        edges.add(centerRightEdge);

        final BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedAdjacencyGraph<>(nodes, edges);

        final TopologicalSorter<Object> sorter = new SimpleTopologicalRanker<>();
        final List<Object> sortedNodes = sorter.sort(graph);

        Assert.assertSame("center node is not the first node!", sortedNodes.get(0), centerNode);
        Assert.assertTrue("left node is not before leftiest node!", sortedNodes.indexOf(leftNode) < sortedNodes.indexOf(leftiestNode));
    }
}
