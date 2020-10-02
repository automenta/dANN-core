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

public class TestMutableLong {
    @Test
    public void testConstructors() {
        MutableLong test = new MutableLong(123);
        Assert.assertEquals("value constructor failed", 123, (long) test.getNumber());
        test = new MutableLong("456");
        Assert.assertEquals("string value constructor failed", 456, (long) test.getNumber());
        test = new MutableLong(789L);
        Assert.assertEquals("Number value constructor failed", 789, (long) test.getNumber());
    }

    @Test
    public void testMax() {
        final MutableLong highValue = new MutableLong(Long.MAX_VALUE);

        for (int testCount = 0; testCount < 1000; testCount++) {
            final MutableLong mutated = highValue.mutate(100.0);

            Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.longValue() >= -1);
        }
    }

    @Test
    public void testMin() {
        final MutableLong lowValue = new MutableLong(Long.MIN_VALUE);

        for (int testCount = 0; testCount < 1000; testCount++) {
            final MutableLong mutated = lowValue.mutate(100.0);

            Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.longValue() <= 1);
        }
    }

    @Test
    public void testDeviation() {
        final MutableLong center = new MutableLong(0);
        double averageSum = 0;
        double testCount;
        for (testCount = 0.0; testCount < 10000; testCount++) {
            averageSum += center.mutate(1.0).longValue();
        }
        final double average = averageSum / testCount;
        Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
    }
}
