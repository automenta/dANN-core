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

public class TestMutableByte {
    @Test
    public void testConstructors() {
        MutableByte test = new MutableByte((byte) 123);
        Assert.assertEquals("value constructor failed", (byte) test.getNumber(), 123);
        test = new MutableByte("57");
        Assert.assertEquals("string value constructor failed", (byte) test.getNumber(), 57);
        test = new MutableByte((byte) 83);
        Assert.assertEquals("Number value constructor failed", (byte) test.getNumber(), 83);
    }

    @Test
    public void testMax() {
        final MutableByte highValue = new MutableByte(Byte.MAX_VALUE);
        for (int testCount = 0; testCount < 1000; testCount++) {
            final MutableByte mutated = highValue.mutate(100.0);
            Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.byteValue() >= (byte) -1);
        }
    }

    @Test
    public void testMin() {
        final MutableByte lowValue = new MutableByte(Byte.MIN_VALUE);
        for (int testCount = 0; testCount < 1000; testCount++) {
            final MutableByte mutated = lowValue.mutate(100.0);
            Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.byteValue() <= (byte) 1);
        }
    }

    @Test
    public void testDeviation() {
        final MutableByte center = new MutableByte((byte) 0);
        double averageSum = 0;
        double testCount;
        for (testCount = 0.0; testCount < 1000; testCount++) {
            averageSum += center.mutate(1.0).byteValue();
        }
        final double average = averageSum / testCount;
        Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
    }
}
