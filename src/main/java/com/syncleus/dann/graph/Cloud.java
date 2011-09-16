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

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import com.syncleus.dann.graph.event.context.ContextReporter;
import com.syncleus.dann.graph.xml.EdgeXml;
import com.syncleus.dann.xml.XmlSerializable;

public interface Cloud<
	  	T,
	  	EP extends Cloud.Endpoint<? extends T>
	  > extends Serializable, Cloneable, XmlSerializable<EdgeXml, Object>, ContextReporter
{
	interface Endpoint<T>
	{
		Set<? extends Cloud.Endpoint<? extends T>> getNeighbors();
		Set<? extends Cloud.Endpoint<? extends T>> getTraversableNeighborsTo();
		Set<? extends Cloud.Endpoint<? extends T>> getTraversableNeighborsFrom();
		boolean isTraversable();
		boolean isTraversable(Cloud.Endpoint<?> destination);
		T getTarget();
	};

	Set<EP> getEndpoints();
	Set<EP> getEndpoints(Object target);
	Set<T> getTargets();
	Set<T> getNeighbors(Object target);
	Set<T> getTraversableFrom(Object target);
	Set<T> getTraversableTo(Object target);
	boolean isTraversable(Object sourceTarget, Object destinationTarget);
	int getDegree();
	boolean contains( Object endpoint);
	boolean containsAny(Collection<? extends Endpoint<?>> endpoint);
	boolean containsAll(Collection<? extends Endpoint<?>> endpoint);
	boolean containsTarget(Object target);
	boolean containsAnyTargets(Collection<?> target);
	boolean containsAllTargets(Collection<?> target);
}
