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
package com.syncleus.dann.genetics;

import org.junit.Assert;
import org.junit.Test;

public class TestLongValueGene {
    @Test
    public void testConstructors() {
        ValueGene test = new LongValueGene(78101237423L);
        Assert.assertEquals("value constructor failed", 78101237423L, test.getValue().getNumber().longValue());
        test = new LongValueGene(new MutableLong(78101237423L));
        Assert.assertEquals("MutableByte value constructor failed", 78101237423L, test.getValue().getNumber().longValue());
        test = new LongValueGene(78101237423L);
        Assert.assertEquals("Number value constructor failed", 78101237423L, test.getValue().getNumber().longValue());
        test = new LongValueGene();
        Assert.assertEquals("default constructor failed", 0L, test.getValue().getNumber().longValue());
    }

    @Test
    public void testMutation() {
        final ValueGene center = new LongValueGene(0L);
        long averageSum = 0;
        long testCount;
        for (testCount = 0; testCount < 1000; testCount++) {
            averageSum += center.mutate(100.0).getValue().byteValue();
        }
        final double average = ((double) averageSum) / testCount;
        Assert.assertTrue("average deviation is more than 100.0", average < 100.0);
    }
}
