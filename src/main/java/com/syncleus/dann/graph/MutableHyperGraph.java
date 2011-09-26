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

public interface MutableHypergraph<
	  	N,
	  	E extends Hyperedge<N,? extends Hyperedge.Endpoint<? extends N, ? extends N>> & MutableCloud<? extends N, ? extends MutableCloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends MutableHypergraph.Endpoint<Object, N, E>,
	  	NE extends MutableHypergraph.NodeEndpoint<N, E>,
	  	EE extends MutableHypergraph.EdgeEndpoint<N, E>
	  > extends MutableCloudGraph<Object, N, E, AE, NE, EE>, Hypergraph<N,E,AE,NE,EE>
{
	interface Endpoint<
		  	T,
		  	N,
		  	E extends Hyperedge<N,? extends Hyperedge.Endpoint<? extends N, ? extends N>> & MutableCloud<? extends N, ? extends MutableCloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends MutableCloudGraph.Endpoint<Object,T,N,E>, Hypergraph.Endpoint<T,N,E>
	{
	};

	interface NodeEndpoint<
		  	N,
		  	E extends Hyperedge<N,? extends Hyperedge.Endpoint<? extends N, ? extends N>> & MutableCloud<? extends N, ? extends MutableCloud.Endpoint<? extends N, ? extends N>>
	  > extends MutableCloudGraph.NodeEndpoint<Object,N,E>, Hypergraph.NodeEndpoint<N,E>, Endpoint<N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	N,
		  	E extends Hyperedge<N,? extends Hyperedge.Endpoint<? extends N, ? extends N>> & MutableCloud<? extends N, ? extends MutableCloud.Endpoint<? extends N, ? extends N>>
		> extends MutableCloudGraph.EdgeEndpoint<Object,N,E>, Hypergraph.EdgeEndpoint<N,E>, Endpoint<E,N,E>
	{
	};
}
