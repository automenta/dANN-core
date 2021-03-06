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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractHyperedge<N> extends AbstractTraversableCloud<N> implements Hyperedge<N> {
    private static final long serialVersionUID = -3657973823101515199L;

    protected AbstractHyperedge(final Collection<N> nodes) {
        super(nodes);
    }

    protected AbstractHyperedge(final N[] nodes) {
        super(nodes);
    }

    protected AbstractHyperedge(final Collection<N> nodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled) {
        super(nodes, allowJoiningMultipleGraphs, contextEnabled);
    }

    protected AbstractHyperedge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N[] nodes) {
        super(allowJoiningMultipleGraphs, contextEnabled, nodes);
    }

    @Override
    public Collection<N> getTraversableNodes(final N node) {
        final List<N> traversableNodes = new ArrayList<>(this.getNodes());
        if (!traversableNodes.remove(node))
            throw new IllegalArgumentException("node is not one of the end points!");
        return Collections.unmodifiableList(traversableNodes);
    }

    @Override
    public int getDegree() {
        return this.getNodes().size();
    }

    @Override
    public boolean isSymmetric(final Hyperedge symmetricEdge) {
        throw new UnsupportedOperationException("this operation is not yet supported");
    }

    @Override
    public AbstractHyperedge<N> connect(final N node) {
        if (node == null)
            throw new IllegalArgumentException("node can not be null");
        if (this.getNodes().contains(node))
            throw new IllegalArgumentException("node is already connected");
        return (AbstractHyperedge<N>) this.add(node);
    }

    @Override
    public AbstractHyperedge<N> connect(final List<N> nodes) {
        if (nodes == null)
            throw new IllegalArgumentException("node can not be null");
        for (final N node : nodes)
            if (this.getNodes().contains(node))
                throw new IllegalArgumentException("node is already connected");
        return (AbstractHyperedge<N>) this.add(nodes);
    }

    @Override
    public AbstractHyperedge<N> disconnect(final N node) {
        if (node == null)
            throw new IllegalArgumentException("node can not be null");
        if (!this.getNodes().contains(node))
            throw new IllegalArgumentException("node is not currently connected to");
        return (AbstractHyperedge<N>) this.remove(node);
    }

    @Override
    public AbstractHyperedge<N> disconnect(final List<N> nodes) {
        if (nodes == null)
            throw new IllegalArgumentException("node can not be null");
        if (!this.getNodes().containsAll(nodes))
            throw new IllegalArgumentException("node is not currently connected to");
        return (AbstractHyperedge<N>) this.remove(nodes);
    }

}
