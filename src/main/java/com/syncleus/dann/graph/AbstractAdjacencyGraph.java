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

import com.syncleus.dann.graph.search.pathfinding.DijkstraPathFinder;
import com.syncleus.dann.graph.search.pathfinding.PathFinder;
import com.syncleus.dann.graph.xml.*;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;
import org.apache.log4j.Logger;
import javax.jnlp.ExtendedService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * An AbstractAdjacencyGraph is a Graph implemented using adjacency lists.
 * @param <N> The node type
 * @param <E> The type of edge for the given node type
 */
@XmlJavaTypeAdapter( com.syncleus.dann.xml.XmlSerializableAdapter.class )
public abstract class AbstractAdjacencyGraph<
/*
	  	PA,
	  	N extends PA,
	  	E extends Edge<? extends N,? extends Edge.Endpoint<? extends N, ? extends N>>,
	  	NEP extends Graph.NodeEndpoint<? extends N,? extends N,? extends E>,
	  	EEP extends Graph.EdgeEndpoint<? extends N,? extends E,? extends E>
*/
	  	PA,
	  	N extends PA,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends Graph.NodeEndpoint<PA, N, E>,
	  	EEP extends Graph.EdgeEndpoint<PA, N, E>
	  > extends AbstractEdge<PA,Graph.Endpoint<PA,N,E,PA>> implements Graph<PA, N, E, NEP, EEP>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractAdjacencyGraph.class);
//	private Set<E> edges;
//	private Map<N, Set<E>> adjacentEdges = new HashMap<N, Set<E>>();
//	private Map<N, List<N>> adjacentNodes = new HashMap<N, List<N>>();
/*
	private Set<? extends EdgeEndpoint<? extends Object, N, E, E>> edges;
	private Map<? extends NodeEndpoint<? extends Object, E, N, N>, Set<E>> adjacentEdges = new HashMap<NodeEndpoint<? extends Object, E, N, N>, Set<E>>();
	private Map<? extends NodeEndpoint<? extends Object, E, N, N>, List<N>> adjacentNodes = new HashMap<NodeEndpoint<? extends Object, E, N, N>, List<N>>();
*/
//	private final boolean contextEnabled;


/*
	/**
	 * Creates a new AbstractAdjacencyGraph with no edges and no adjacencies.
	 * nodeContext and edgeContext is enabled.
	 *
	protected AbstractAdjacencyGraph()
	{
		this(true);
	}

	/**
	 * Creates a new AbstractAdjacencyGraph with no edges and no adjacencies.
	 *
	protected AbstractAdjacencyGraph(final boolean contextEnabled)
	{
//		this.edges = new HashSet<E>();
		this.edges = new HashSet<EdgeEndpoint<? extends Object, N, E, E>>();
//		this.contextEnabled = contextEnabled;
	}

	/**
	 * Creates a new AbstractAdjacencyGraph as a copy of the current Graph.
	 * nodeContext is enabled.
	 * @param copyGraph The Graph to copy
	 *
	protected AbstractAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		this(copyGraph.getTargets(), copyGraph.getEdges(), true);
	}

	/**
	 * Creates a new AbstractAdjacencyGraph as a copy of the current Graph.
	 * @param copyGraph The Graph to copy
	 *
	protected AbstractAdjacencyGraph(final Graph<N, E> copyGraph, final boolean contextEnabled)
	{
		this(copyGraph.getTargets(), copyGraph.getEdges(), contextEnabled);
	}

	/**
	 * Creates a new AbstractAdjacencyGraph from the given list of nodes, and
	 * the given list of ourEdges.
	 * The adjacency lists are created from this structure. nodeContext is
	 * enabled.
	 *
	 * @param nodes The set of all nodes
	 * @param edges The set of all ourEdges
	 *
	protected AbstractAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		this(nodes, edges, true);
	}

	/**
	 * Creates a new AbstractAdjacencyGraph from the given list of nodes, and
	 * the given list of ourEdges.
	 * The adjacency lists are created from this structure.
	 *
	 * @param attemptNodes The set of all nodes
	 * @param attemptEdges The set of all ourEdges
	 *
	protected AbstractAdjacencyGraph(final Set<N> attemptNodes, final Set<E> attemptEdges, final boolean contextEnabled)
	{
		if(attemptNodes == null)
			throw new IllegalArgumentException("attemptNodes can not be null");
		if(attemptEdges == null)
			throw new IllegalArgumentException("attemptEdges can not be null");
		//make sure all the edges only join to contained nodes
		for( E attemptEdge : attemptEdges )
			if( !attemptNodes.containsAll(attemptEdge.getTargets()) )
			    throw new IllegalArgumentException("A node that is an end point in one of the attemptEdges was not in the nodes list");

		this.contextEnabled = contextEnabled;
		//add all the nodes before we worry about edges. check for NodeContext
		for(final N attemptNode : attemptNodes)
		{

			// lets see if this ContextEdge will allow itself to join the graph
			if(this.contextEnabled
					&& (attemptNode instanceof ContextGraphElement)
					&& !((ContextGraphElement)attemptNode).joiningGraph(this) )
				continue;

			this.adjacentNodes.put(attemptNode, new ArrayList<N>());
			this.adjacentEdges.put(attemptNode, new HashSet<E>());
		}

		//Add the edges checking for Edge Context.
		if( this.contextEnabled )
		{
			this.edges = new HashSet<E>(attemptEdges.size());
			for(E attemptEdge : attemptEdges)
			{
				// lets see if this ContextEdge will allow itself to join the graph
				if(this.contextEnabled
						&& (attemptEdge instanceof ContextGraphElement)
						&& !((ContextGraphElement)attemptEdge).joiningGraph(this) )
					continue;


				this.edges.add(attemptEdge);
				//populate adjacency maps
				for(N currentNode : attemptEdge.getTargets())
				{
					boolean passedCurrent = false;
					for(N neighborNode : attemptEdge.getTargets())
					{
						if( !passedCurrent && (neighborNode == currentNode))
						{
							passedCurrent = true;
							continue;
						}

						//this is a neighbor node
						if( !this.adjacentNodes.containsKey(currentNode) )
							throw new IllegalStateException("After edges and nodes have applied their context restrictions an edge remained connected to a node not in this graph");

						this.adjacentNodes.get(currentNode).add(neighborNode);
					}

					//this is a neighbor edge
					if( !this.adjacentEdges.containsKey(currentNode) )
						throw new IllegalStateException("After edges and nodes have applied their context restrictions an edge remained connected to a node not in this graph");
					this.adjacentEdges.get(currentNode).add(attemptEdge);
				}
			}
		}
		else
		{
			this.edges = new HashSet<E>(attemptEdges);
		}
	}

	/**
	 * Gets the internal edges of the list.
	 * @return The set of internal edges
	 *
	protected Set<E> getInternalEdges()
	{
		return this.edges;
	}

	/**
	 * Gets the map of nodes to their associated set of edges.
	 * @return The internal adjacency edges to nodes
	 *
	protected Map<N, Set<E>> getInternalAdjacencyEdges()
	{
		return this.adjacentEdges;
	}

	/**
	 * Gets each node's list of adjacent nodes.
	 * @return The map of nodes to adjacent nodes
	 *
	protected Map<N, List<N>> getInternalAdjacencyNodes()
	{
		return this.adjacentNodes;
	}

	@Override
	public boolean isContextEnabled()
	{
		return this.contextEnabled;
	}

	/**
	 * Gets all nodes in the map.
	 * @return The unmodifiable set of nodes
	 *
	@Override
	public Set<N> getTargets()
	{
		return Collections.unmodifiableSet(this.adjacentEdges.keySet());
	}

	/**
	 * Gets all edges in the map.
	 * @return The unmodifiable set of edges
	 *
	@Override
	public Set<E> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	/**
	 * Gets all edges adjacent to a given node.
	 * @param node the end point for all edges to retrieve.
	 * @return The edges adjacent to that node.
	 *
	@Override
	public Set<E> getAdjacentEdges(final N node)
	{
		if( this.adjacentEdges.containsKey(node) )
			return Collections.unmodifiableSet(this.adjacentEdges.get(node));
		else
			return Collections.<E>emptySet();
	}

	/**
	 * Gets all nodes adjacent to the given node.
	 * @param node The whose neighbors are to be returned.
	 * @return All adjacent nodes to the given node
	 *
	@Override
	public List<N> getAdjacentNodes(final N node)
	{
		return Collections.unmodifiableList(new ArrayList<N>(this.adjacentNodes.get(node)));
	}

	/**
	 * Gets the traversable nodes adjacent to the given node.
	 * @param node The whose traversable neighbors are to be returned.
	 * @return The traversable nodes adjacent to the given node
	 * @see com.syncleus.dann.graph.Edge#getTraversableNodes(Object)
	 *
	@Override
	public List<N> getTraversableAdjacentNodes(final N node)
	{
		final List<N> traversableNodes = new ArrayList<N>();
		for(final E adjacentEdge : this.getAdjacentEdges(node))
			traversableNodes.addAll(adjacentEdge.getTraversableNodes(node));
		return Collections.unmodifiableList(traversableNodes);
	}

	/**
	 * Gets the traversable edges from this node.
	 * @param node edges returned will be traversable from this node.
	 * @return The traversable edges from the given node
	 * @see com.syncleus.dann.graph.Edge#isTraversable(Object)
	 *
	@Override
	public Set<E> getTraversableAdjacentEdges(final N node)
	{
		final Set<E> traversableEdges = new HashSet<E>();
		for(final E adjacentEdge : this.getAdjacentEdges(node))
			if( adjacentEdge.isTraversable(node) )
				traversableEdges.add(adjacentEdge);
		return Collections.unmodifiableSet(traversableEdges);
	}

	protected boolean add(final E newEdge)
	{
		if( newEdge == null )
			throw new IllegalArgumentException("newEdge can not be null");
		if( !this.getTargets().containsAll(newEdge.getTargets()) )
			throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");

		// if context is enabled lets check if it can join
		if( this.isContextEnabled() && (newEdge instanceof ContextGraphElement)
				&& !((ContextGraphElement)newEdge).joiningGraph(this) )
			return false;

		if( this.getInternalEdges().add(newEdge) )
		{
			for(final N currentNode : newEdge.getTargets())
			{
				this.getInternalAdjacencyEdges().get(currentNode).add(newEdge);

				final List<N> newAdjacentNodes = new ArrayList<N>(newEdge.getTargets());
				newAdjacentNodes.remove(currentNode);
				for(final N newAdjacentNode : newAdjacentNodes)
					this.getInternalAdjacencyNodes().get(currentNode).add(newAdjacentNode);
			}
			return true;
		}

		return false;
	}

	public boolean add(final N newNode)
	{
		if( newNode == null )
			throw new IllegalArgumentException("newNode can not be null");

		if( this.getInternalAdjacencyEdges().containsKey(newNode) )
			return false;

		// if context is enabled lets check if it can join
		if( this.isContextEnabled() && (newNode instanceof ContextGraphElement)
				&& !((ContextGraphElement)newNode).joiningGraph(this) )
			return false;

		this.getInternalAdjacencyEdges().put(newNode, new HashSet<E>());
		this.getInternalAdjacencyNodes().put(newNode, new ArrayList<N>());
		return true;
	}

	protected boolean remove(final E edgeToRemove)
	{
		if( edgeToRemove == null )
			throw new IllegalArgumentException("removeSynapse can not be null");

		if( !this.getInternalEdges().contains(edgeToRemove) )
			return false;

		// if context is enabled lets check if it can join
		if( this.isContextEnabled()
				&& (edgeToRemove instanceof ContextGraphElement)
				&& !((ContextGraphElement)edgeToRemove).leavingGraph(this) )
			return false;

		if( !this.getInternalEdges().remove(edgeToRemove) )
			throw new IllegalStateException("could not remove edge even though it is present");

		for(final N removeNode : edgeToRemove.getTargets())
		{
			this.getInternalAdjacencyEdges().get(removeNode).remove(edgeToRemove);

			final List<N> removeAdjacentNodes = new ArrayList<N>(edgeToRemove.getTargets());
			removeAdjacentNodes.remove(removeNode);
			for(final N removeAdjacentNode : removeAdjacentNodes)
				this.getInternalAdjacencyNodes().get(removeNode).remove(removeAdjacentNode);
		}
		return true;
	}

	protected boolean remove(final N nodeToRemove)
	{
		if( nodeToRemove == null )
			throw new IllegalArgumentException("node can not be null");

		if( !this.getInternalAdjacencyEdges().containsKey(nodeToRemove) )
			return false;

		// if context is enabled lets check if it can join
		if( this.isContextEnabled()
				&& (nodeToRemove instanceof ContextGraphElement)
				&& !((ContextGraphElement)nodeToRemove).leavingGraph(this) )
			return false;

		final Set<E> removeEdges = this.getInternalAdjacencyEdges().get(nodeToRemove);

		//remove all the edges
		for(final E removeEdge : removeEdges)
			this.remove(removeEdge);

		//modify edges by removing the node to remove
		final Set<E> newEdges = new HashSet<E>();
		for(final E removeEdge : removeEdges)
		{
			E newEdge = (E) removeEdge.disconnect(nodeToRemove);
			while( (newEdge != null) && (newEdge.getTargets().contains(nodeToRemove)) )
				newEdge = (E) removeEdge.disconnect(nodeToRemove);
			if( newEdge != null )
				newEdges.add(newEdge);
		}

		//add the modified edges
		for(final E newEdge : newEdges)
			this.add(newEdge);

		//remove the node itself
		this.getInternalAdjacencyEdges().remove(nodeToRemove);
		this.getInternalAdjacencyNodes().remove(nodeToRemove);

		return true;
	}

	protected boolean clear()
	{
		boolean removedSomething = false;

		//first lets remove all the edges
		for(E edge : this.getEdges())
		{
			//lets just make sure we arent some how getting an we dont actually own, this shouldnt be possible so its
			//an assert. This ensures that if remove() comes back false it must be because the context didnt allow it.
			assert this.getInternalEdges().contains(edge);

			if( !this.remove(edge) )
				throw new IllegalStateException("one of the edges will not allow itself to leave this graph");

			removedSomething = true;
		}

		//now lets remove all the nodes
		for(N node : this.getTargets())
		{
			//lets just make sure we arent some how getting an we dont actually own, this shouldnt be possible so its
			//an assert. This ensures that if remove() comes back false it must be because the context didnt allow it.
			assert ( !this.getInternalAdjacencyEdges().containsKey(node) );

			if( !this.remove(node) )
				throw new IllegalStateException("one of the nodes will not allow itself to leave this graph");

			removedSomething = true;
		}

		return removedSomething;
	}


	/**
	 * Clones the current object.
	 * @return A clone of the current object, with no changes
	 *
	@Override
	protected AbstractAdjacencyGraph<N, E> clone()
	{
		try
		{
			final AbstractAdjacencyGraph<N, E> cloneGraph = (AbstractAdjacencyGraph<N, E>) super.clone();

			//lets instantiate some new datastructurs for our clone
			cloneGraph.adjacentEdges = new HashMap<N, Set<E>>();
			cloneGraph.adjacentNodes = new HashMap<N, List<N>>();

			//add all the nodes before we worry about edges. check for NodeContext
			for(N attemptNode : this.getTargets())
			{
				// lets see if this ContextEdge will allow itself to join the graph
				if(this.contextEnabled
						&& (attemptNode instanceof ContextGraphElement)
						&& !((ContextGraphElement)attemptNode).joiningGraph(cloneGraph) )
					continue;

				cloneGraph.adjacentNodes.put(attemptNode, new ArrayList<N>());
				cloneGraph.adjacentEdges.put(attemptNode, new HashSet<E>());
			}

			//Add the edges checking for Edge Context.
			if( this.contextEnabled )
			{
				cloneGraph.edges = new HashSet<E>(this.getEdges().size());
				for(E attemptEdge : this.getEdges())
				{
					// lets see if this ContextEdge will allow itself to join the graph
					if(this.contextEnabled
							&& (attemptEdge instanceof ContextGraphElement)
							&& !((ContextGraphElement)attemptEdge).joiningGraph(cloneGraph) )
						continue;

					cloneGraph.edges.add(attemptEdge);
					//populate adjacency maps
					for(N currentNode : attemptEdge.getTargets())
					{
						boolean passedCurrent = false;
						for(N neighborNode : attemptEdge.getTargets())
						{
							if( !passedCurrent && (neighborNode == currentNode))
							{
								passedCurrent = true;
								continue;
							}

							//this is a neighbor node
							if( !cloneGraph.adjacentNodes.containsKey(currentNode) )
								throw new IllegalStateException("After edges and nodes have applied their context restrictions an edge remained connected to a node not in this graph");

							cloneGraph.adjacentNodes.get(currentNode).add(neighborNode);
						}

						//this is a neighbor edge
						if( !cloneGraph.adjacentEdges.containsKey(currentNode) )
							throw new IllegalStateException("After edges and nodes have applied their context restrictions an edge remained connected to a node not in this graph");
						cloneGraph.adjacentEdges.get(currentNode).add(attemptEdge);
					}
				}
			}
			else
			{
				cloneGraph.edges = new HashSet<E>(this.getEdges());
			}

			return cloneGraph;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Unexpectedly could not clone Graph.", caught);
			throw new UnexpectedDannError("Unexpectedly could not clone graph", caught);
		}
	}
*/

	protected abstract Set<Graph.EdgeEndpoint<PA, N, E>> getAdjacentEdgeEndPoint(Graph.NodeEndpoint<PA, N, E> nodeEndPoint);

	protected PathFinder<N,E> getPathFinder()
	{
		return new DijkstraPathFinder<N,E>(this);
	}

	@Override
	public Set<N> getNodes()
	{
		final Set<N> nodes = new HashSet<N>();
		for(NEP destinationEndpoint : this.getNodeEndpoints() )
			nodes.add(destinationEndpoint.getTarget());
		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<E> getEdges()
	{
		final Set<E> edges = new HashSet<E>();
		for(EEP destinationEndpoint : this.getEdgeEndpoints() )
			edges.add(destinationEndpoint.getTarget());
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Set<Graph.Endpoint<PA,N,E,PA>> getEndPoints()
	{
		final Set<Graph.Endpoint<PA,N,E,PA>> endpoints = new HashSet<Graph.Endpoint<PA,N,E,PA>>();
		endpoints.addAll(this.getNodeEndpoints());
		endpoints.addAll(this.getEdgeEndpoints());
		return Collections.unmodifiableSet(endpoints);
	}

	@Override
	public Set<EEP> getEdgeEndpoints(E edge)
	{
		Set<EEP> matchingEndpoints  = new HashSet<EEP>();
		for(final EEP endpoint : this.getEdgeEndpoints() )
		{
			if( endpoint.getTarget().equals(edge))
				matchingEndpoints.add(endpoint);
		}

		return Collections.unmodifiableSet(matchingEndpoints);
	}

	@Override
	public Set<NEP> getNodeEndpoints(N node)
	{
		Set<NEP> matchingEndpoints  = new HashSet<NEP>();
		for(NEP endpoint : this.getNodeEndpoints() )
		{
			if( endpoint.getTarget().equals(node))
				matchingEndpoints.add(endpoint);
		}

		return Collections.unmodifiableSet(matchingEndpoints);
	}

	@Override
	public Set<N> getAdjacentNodes(N node)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NEP destinationEndpoint : this.getNodeEndpoints(node) )
			for(Graph.EdgeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				for(Graph.NodeEndpoint<PA, N, E> nodeEndpoint : sourceEndpoint.getAdjacentNodes())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<E> getAdjacentEdges(N node)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NEP destinationEndpoint : this.getNodeEndpoints(node) )
			for(Graph.EdgeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public boolean isTraversable(Object source, Object destination)
	{
		return this.isTraversable(source, destination, this.getPathFinder());
	}

	private boolean isTraversable(Object source, Object destination, final PathFinder pathFinder)
	{
		if(this.getNodes().contains(source) && this.getNodes().contains(destination))
			return this.getPathFinder().isReachable((N)source, (N)destination);
		//edges are handled recusively
		else if(this.getEdges().contains(source))
		{
			final E sourceEdge = (E) source;
			final Set<N> sourceNodes = new HashSet<N>();
			for(Endpoint<? extends N> endpoint : sourceEdge.getEndPoints())
				if((endpoint.getTraversableNeighborsFrom().size() > 0)&&(!sourceNodes.contains(endpoint.getTarget())))
				{
					if(this.isTraversable(endpoint.getTarget(), destination))
						return true;
					sourceNodes.add(endpoint.getTarget());
				}
			return false;
		}
		else if(this.getEdges().contains(destination))
		{
			final E destinationEdge = (E) destination;
			final Set<N> destinationNodes = new HashSet<N>();
			for(Endpoint<? extends N> endpoint : destinationEdge.getEndPoints())
				if((endpoint.isTraversable())&&(!destinationNodes.contains(endpoint.getTarget())))
				{
					if(this.isTraversable(source, endpoint.getTarget()))
						return true;
					destinationNodes.add(endpoint.getTarget());
				}
			return false;
		}
		else
			throw new IllegalArgumentException("both source and destination need to be contained within this graph");
	}

	@Override
	public Set<E> getTraversableEdgesFrom(N source)
	{
		if( !this.getNodes().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(source, neighbor, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesFrom(E source)
	{
		if( !this.getEdges().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesTo(N destination)
	{
		if( !this.getNodes().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(neighbor,destination,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesTo(E destination)
	{
		if( !this.getEdges().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(neighbor,destination,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesFrom(N source)
	{
		if( !this.getNodes().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(source, neighbor, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesFrom(E source)
	{
		if( !this.getEdges().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesTo(N destination)
	{
		if( !this.getNodes().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(neighbor, destination, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesTo(E destination)
	{
		if( !this.getEdges().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(neighbor, destination, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesFrom(N source)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(NEP sourceEndpoint : this.getNodeEndpoints(source) )
			for(Graph.EdgeEndpoint<PA, N, E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesFrom(E source)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EEP sourceEndpoint : this.getEdgeEndpoints(source) )
			for(Graph.EdgeEndpoint<PA, N, E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}


	@Override
	public Set<E> getTraversableAdjacentEdgesTo(N destination)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NEP destinationEndpoint : this.getNodeEndpoints(destination) )
			for(Graph.EdgeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesTo())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesTo(E destination)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EEP destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(Graph.EdgeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(N source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(NEP sourceEndpoint : this.getNodeEndpoints(source) )
			for(Graph.EdgeEndpoint<PA, N, E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesTo())
				for(Graph.NodeEndpoint<PA, N, E> nodeEndpoint : destinationEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(E source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(EEP sourceEndpoint : this.getEdgeEndpoints(source) )
			for(Graph.NodeEndpoint<PA, N, E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(N destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NEP destinationEndpoint : this.getNodeEndpoints(destination) )
			for(Graph.EdgeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				for(Graph.NodeEndpoint<PA, N, E> nodeEndpoint : sourceEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(E destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(EEP destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(Graph.NodeEndpoint<PA, N, E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	/**
	 * Clones the current object.
	 * @return A clone of the current object, with no changes
	 */
	@Override
	protected AbstractAdjacencyGraph<PA, N, E, NEP, EEP> clone()
	{
		return (AbstractAdjacencyGraph<PA, N, E, NEP, EEP>) super.clone();
	}

	/**
	 * Converts the current AbstractAdjacencyGraph to a GraphXML.
	 * @return The GraphXML representation of this AbstractAdjacencyGraph
	 */
	@Override
	public GraphXml toXml()
	{
		final GraphElementXml xml = new GraphElementXml();
		final Namer<Object> namer = new Namer<Object>();

		xml.setNodeInstances(new GraphElementXml.NodeInstances());
		for(N node : this.getNodes())
		{
			final String nodeName = namer.getNameOrCreate(node);

			final Object nodeXml;
			if(node instanceof XmlSerializable)
				nodeXml = ((XmlSerializable)node).toXml(namer);
			else
				//if the object isnt XmlSerializable lets try to just serialize it as a regular JAXB object
				nodeXml = node;

			final NamedValueXml encapsulation = new NamedValueXml();
			encapsulation.setName(nodeName);
			encapsulation.setValue(nodeXml);

			xml.getNodeInstances().getNodes().add(encapsulation);
		}

		this.toXml(xml, namer);
		return xml;
	}

	/**
	 * Converts a given Namer to its GraphXML representation.
	 * @param namer The namer to convert
	 * @return The GraphXML representation of this namer
	 */
	@Override
	public GraphXml toXml(final Namer<Object> namer)
	{
		if(namer == null)
			throw new IllegalArgumentException("namer can not be null");

		final GraphXml xml = new GraphXml();
		this.toXml(xml, namer);
		return xml;
	}
/*
	/**
	 * Adds a current Namer to the given GraphXML object.
	 * @param jaxbObject The graph to add the object to
	 * @param namer THe namer to add to the GraphXML
	 */
/*
	@Override
	public void toXml(final GraphXml jaxbObject, final Namer<Object> namer)
	{
		if(namer == null)
			throw new IllegalArgumentException("nodeNames can not be null");
		if(jaxbObject == null)
			throw new IllegalArgumentException("jaxbObject can not be null");

		for(N node : this.getNodes())
		{
			final String nodeName = namer.getNameOrCreate(node);

			final Object nodeXml;
			if(node instanceof XmlSerializable)
				nodeXml = ((XmlSerializable)node).toXml(namer);
			else
				// if the object isnt XmlSerializable lets try to just serialize
				// it as a regular JAXB object
				nodeXml = node;

			final NameXml encapsulation = new NameXml();
			encapsulation.setName(nodeName);

			if( jaxbObject.getNodes() == null )
				jaxbObject.setNodes(new GraphXml.Nodes());
			jaxbObject.getNodes().getNodes().add(encapsulation);
		}

		for(E edge : this.getEdges())
		{
			final EdgeXml edgeXml = edge.toXml(namer);

			if( jaxbObject.getEdges() == null )
				jaxbObject.setEdges(new GraphXml.Edges());
			jaxbObject.getEdges().getEdges().add(edgeXml);
		}
	}
*/
	protected abstract class AbstractNodeEndpoint extends AbstractEdge<N,Graph.Endpoint<PA,N,E,PA>>.AbstractEndpoint implements Graph.NodeEndpoint<PA,N,E>
	{

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getAdjacentEdges()
		{
			return getAdjacentEdgeEndPoint(this);
		}

		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getAdjacentNodes()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Graph.EdgeEndpoint<PA, N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Edge.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndPoints(this.getTarget()) )
					for( Edge.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getNeighbors() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Graph.EdgeEndpoint<PA, N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Edge.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndPoints(this.getTarget()) )
					for( Edge.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsTo() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Graph.EdgeEndpoint<PA, N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Edge.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndPoints(this.getTarget()) )
					for( Edge.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsFrom() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<Graph.EdgeEndpoint<PA, N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<PA, N, E>>();

			for(Graph.EdgeEndpoint<PA, N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Edge.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndPoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<Graph.EdgeEndpoint<PA, N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<PA, N, E>>();

			for(Graph.EdgeEndpoint<PA, N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Edge.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndPoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}
	};

//	protected abstract class AbstractNodeEndpoint<MN extends N> extends AbstractEdge<PA,Graph.Endpoint<? extends PA,? extends PA,?>>.AbstractEndpoint<MN> implements Graph.NodeEndpoint<PA,N,MN,E,NEP,EEP>
//	protected abstract class AbstractEdgeEndpoint<ME extends E> extends AbstractEdge<PA,Graph.Endpoint<? extends PA,? extends PA, ?>>.AbstractEndpoint<ME> implements Graph.EdgeEndpoint<N,E,ME>
//	protected abstract class AbstractEdgeEndpoint<ME extends E> extends AbstractEdge<E,Graph.Endpoint<? extends PA,? extends PA, ?>>.AbstractEndpoint<E,ME> implements Graph.EdgeEndpoint<PA,N,E,E,NEP,EEP>

//	protected abstract class AbstractEdgeEndpoint<ME extends E> extends AbstractEdge<PA,Graph.Endpoint<PA,PA>>.AbstractEndpoint<ME> implements Graph.EdgeEndpoint<PA,N,E,ME>
//	protected abstract class AbstractEdgeEndpoint<ME extends E> implements Edge.Endpoint<PA,ME>, Graph.EdgeEndpoint<PA,N,E,ME>
	protected abstract class AbstractEdgeEndpoint extends AbstractEdge<E,Graph.Endpoint<PA,N,E,PA>>.AbstractEndpoint implements Graph.EdgeEndpoint<PA,N,E>
	{
		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getAdjacentNodes()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> adjacentEndpoint : this.getTarget().getEndPoints())
				adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getAdjacentEdges()
		{
			final Set<Graph.EdgeEndpoint<PA, N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndPoints())
				for( NEP neighborNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()) )
					adjacentEdges.addAll(AbstractAdjacencyGraph.this.getAdjacentEdgeEndPoint(neighborNode));

			adjacentEdges.remove(this);
			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<Graph.EdgeEndpoint<PA, N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndPoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					for( NEP adjacentNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( Graph.EdgeEndpoint<PA, N, E> adjacentEdge : AbstractAdjacencyGraph.this.getAdjacentEdgeEndPoint(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableFrom(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<PA, N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<Graph.EdgeEndpoint<PA, N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndPoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					for( NEP adjacentNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( Graph.EdgeEndpoint<PA, N, E> adjacentEdge : AbstractAdjacencyGraph.this.getAdjacentEdgeEndPoint(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableTo(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndPoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<PA, N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<Graph.NodeEndpoint<PA, N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<PA, N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndPoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}
	};
}
