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
package com.syncleus.dann.math;

import org.junit.Assert;
import org.junit.Test;

public class TestComplexNumber {
    @Test
    public void testComplexMath() {
        final ComplexNumber valueA = new ComplexNumber(-8.0, 2.0);
        final ComplexNumber valueB = new ComplexNumber(7.0, 5.0);
        ComplexNumber result;
        ComplexNumber expected;

        result = valueA.add(valueB);
        expected = new ComplexNumber(-1.0, 7.0);
        Assert.assertEquals("addition failed: " + valueA + " + " + valueB + " = " + result, result, expected);

        result = valueA.subtract(valueB);
        expected = new ComplexNumber(-15.0, -3.0);
        Assert.assertEquals("subtraction failed: " + valueA + " - " + valueB + " = " + result, result, expected);

        result = valueA.multiply(valueB);
        expected = new ComplexNumber(-66.0, -26.0);
        Assert.assertEquals("multiplication failed: " + valueA + " * " + valueB + " = " + result, result, expected);

        result = valueA.multiply(10.0);
        expected = new ComplexNumber(-80.0, 20.0);
        Assert.assertEquals("multiplication failed: " + valueA + " * " + 10.0 + " = " + result, result, expected);

        result = valueA.divide(valueB);
        expected = new ComplexNumber(-0.6216216216216217, 0.7297297297297298);
        Assert.assertTrue("division failed: " + valueA + " / " + valueB + " = " + result, result.subtract(expected).absScalar() < 0.0001);

        result = valueA.conjugate();
        expected = new ComplexNumber(-8.0, -2.0);
        Assert.assertEquals("conjugation failed: conjugate(" + valueA + ") = " + result, result, expected);

        Assert.assertTrue("absolute failed: abs(" + valueA + ") = " + valueA.absScalar(), (valueA.absScalar() - 8.246211251235321) < 0.0001);

        result = valueA.tan();
        expected = new ComplexNumber(0.010925884335752506, 1.0356479469632376);
        Assert.assertTrue("conjugation failed: conjugate(" + valueA + ") = " + result, result.subtract(expected).absScalar() < 0.0001);
    }
}
