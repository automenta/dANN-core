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

import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.graph.context.AbstractContextGraphElement;
import com.syncleus.dann.graph.context.ContextNode;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractTraversableCloud<N> extends AbstractContextGraphElement<Graph<N, ?>> implements TraversableCloud<N> {
    private static final Logger LOGGER = Logger.getLogger(AbstractTraversableCloud.class);
    private final boolean contextEnabled;
    private List<N> nodes;

    protected AbstractTraversableCloud() {
        this(true, true);
    }

    protected AbstractTraversableCloud(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled) {
        super(allowJoiningMultipleGraphs);
        this.contextEnabled = contextEnabled;
    }

    protected AbstractTraversableCloud(final Collection<N> ourNodes) {
        this(ourNodes, true, true);
    }

    protected AbstractTraversableCloud(final Collection<N> ourNodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled) {
        super(allowJoiningMultipleGraphs);
        this.contextEnabled = contextEnabled;

        //make sure each node with context allows us to connect to it
        if (contextEnabled) {
            final List<N> nodesCopy = new ArrayList<>(ourNodes.size());
            for (N ourNode : ourNodes) {
                if (this.contextEnabled && (ourNode instanceof ContextNode) && (!((ContextNode) ourNode).connectingEdge(this)))
                    continue;
                nodesCopy.add(ourNode);
            }
            this.nodes = List.copyOf(nodesCopy);
        } else
            this.nodes = List.copyOf(ourNodes);
    }

    protected AbstractTraversableCloud(final N[] ourNodes) {
        this(true, true, ourNodes);
    }

    protected AbstractTraversableCloud(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... ourNodes) {
        this(List.of(ourNodes), allowJoiningMultipleGraphs, contextEnabled);
    }

    @Override
    public boolean isContextEnabled() {
        return this.contextEnabled;
    }

    protected AbstractTraversableCloud<N> add(final N node) {
        if (node == null)
            throw new IllegalArgumentException("node can not be null");

        final List<N> newNodes = new ArrayList<>(this.nodes);
        newNodes.add(node);

        return createDeepCopy(newNodes);
    }

    protected AbstractTraversableCloud<N> add(final List<N> addNodes) {
        if (addNodes == null)
            throw new IllegalArgumentException("node can not be null");
        final List<N> newNodes = new ArrayList<>(this.nodes);
        newNodes.addAll(addNodes);

        return createDeepCopy(newNodes);
    }

    protected AbstractTraversableCloud<N> remove(final N node) {
        if (node == null)
            throw new IllegalArgumentException("node can not be null");
        if (!this.nodes.contains(node))
            throw new IllegalArgumentException("is not an endpoint");

        final List<N> newNodes = new ArrayList<>(this.nodes);
        newNodes.remove(node);

        return createDeepCopy(newNodes);
    }

    protected AbstractTraversableCloud<N> remove(final List<N> removeNodes) {
        if (removeNodes == null)
            throw new IllegalArgumentException("removeNodes can not be null");
        if (!this.nodes.containsAll(removeNodes))
            throw new IllegalArgumentException("removeNodes do not contain all valid end points");
        final List<N> newNodes = new ArrayList<>(this.nodes);
        for (final N node : removeNodes)
            newNodes.remove(node);

        return createDeepCopy(newNodes);
    }

    /**
     * Create a deep copy of this edge, but with a new set of nodes.
     *
     * @param newNodes the set of nodes to use instead of the current ones.
     * @return a deep copy of this edge, but with a new set of nodes.
     */
    private AbstractTraversableCloud<N> createDeepCopy(final List<N> newNodes) {
        try {
            final AbstractTraversableCloud<N> clonedEdge = (AbstractTraversableCloud<N>) super.clone();
            final List<N> clonedNodes = new ArrayList<>(this.nodes.size());
            //add each node at a time to the clone considering context
            for (N newNode : newNodes) {
                if (this.contextEnabled && (newNode instanceof ContextNode) && (!((ContextNode) newNode).connectingEdge(clonedEdge)))
                    continue;
                clonedNodes.add(newNode);
            }
            clonedEdge.nodes = Collections.unmodifiableList(clonedNodes);
            return clonedEdge;
        } catch (CloneNotSupportedException caught) {
            LOGGER.error("Edge was unexpectidly not cloneable", caught);
            throw new UnexpectedDannError("Edge was unexpectidly not cloneable", caught);
        }
    }

    @Override
    public boolean isTraversable(final N node) {
        return (!this.getTraversableNodes(node).isEmpty());
    }

    @Override
    public final Collection<N> getNodes() {
        return this.nodes;
    }

    @Override
    public String toString() {
        final StringBuilder outString = new StringBuilder(this.nodes.size() * 10);
        for (final N node : this.nodes) {
            outString.append(':').append(node);
        }
        return outString.toString();
    }

}
