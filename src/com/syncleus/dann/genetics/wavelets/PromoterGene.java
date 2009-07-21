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
package com.syncleus.dann.genetics.wavelets;
import com.syncleus.dann.genetics.MutableInteger;
import java.util.Set;
public class PromoterGene extends WaveletGene
{
	private MutableInteger targetDistance;

	public PromoterGene(PromoterGene copy)
	{
		super(copy);

		this.targetDistance = copy.targetDistance;
	}

	public int getTargetDistance()
	{
		return this.targetDistance.intValue();
	}

	@Override
	public PromoterGene clone()
	{
		return new PromoterGene(this);
	}

	@Override
	public void mutate(Set<Key> keyPool)
	{
		super.mutate(keyPool);

		this.targetDistance = this.targetDistance.mutate(this.getMutability());
	}
}