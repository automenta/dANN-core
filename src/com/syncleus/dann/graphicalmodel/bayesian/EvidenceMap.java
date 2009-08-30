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
package com.syncleus.dann.graphicalmodel.bayesian;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EvidenceMap extends HashMap<Map<BayesianNode,Enum>,StateEvidence>
{
	private final Set<BayesianNode> influencingNodes;

	public EvidenceMap(final Set<BayesianNode> influencingNodes)
	{
		this.influencingNodes = Collections.unmodifiableSet(new HashSet<BayesianNode>(influencingNodes));
	}

	public EvidenceMap(final BayesianNode influencingNode)
	{
		Set<BayesianNode> newInfluences = new HashSet<BayesianNode>();
		newInfluences.add(influencingNode);
		this.influencingNodes = Collections.unmodifiableSet(newInfluences);
	}

	public Set<BayesianNode> getInfluencingNodes()
	{
		return Collections.unmodifiableSet(this.influencingNodes);
	}

	public int incrementState(Map<BayesianNode,Enum> influence, Enum state)
	{
		this.verifyInfluencingStates(influence);

		StateEvidence stateEvidence = this.get(influence);
		if(stateEvidence == null)
		{
			stateEvidence = new StateEvidence();
			this.put(influence, stateEvidence);
		}

		Integer evidence = stateEvidence.get(state);
		if(evidence == null)
			evidence = Integer.valueOf(1);
		else
			evidence = Integer.valueOf(evidence++);
		stateEvidence.put(state, evidence);

		return evidence;
	}

	private void verifyInfluencingStates(Map<BayesianNode,Enum> influencingStates)
	{
		if(! influencingStates.keySet().equals(this.influencingNodes))
			throw new IllegalArgumentException ("wrong number of influencing nodes");
		for(Entry<BayesianNode,Enum> entry : influencingStates.entrySet())
			if(entry.getKey().getStateDeclaringClass() != entry.getValue().getDeclaringClass())
				throw new IllegalArgumentException ("influencing node and incluencing state dont have matching enum types");
	}

	@Override
	public StateEvidence put(Map<BayesianNode,Enum> key, StateEvidence value)
	{
		this.verifyInfluencingStates(key);

		return super.put(key,value);
	}

	@Override
	public void putAll(Map<? extends Map<BayesianNode,Enum>,? extends StateEvidence> map)
	{
		for(Map<BayesianNode,Enum> inputStates : map.keySet())
			this.verifyInfluencingStates(inputStates);
		super.putAll(map);
	}
}
