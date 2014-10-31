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

import com.syncleus.dann.UnexpectedDannError;
import org.apache.log4j.Logger;

import java.util.*;

public abstract class AbstractFunction implements Cloneable, Function {
    private static final Logger LOGGER = Logger.getLogger(AbstractFunction.class);
    private final String[] parameterNames;
    private final Map<String, Integer> indexNames;
    private double[] parameters;

    protected AbstractFunction(final AbstractFunction copy) {
        this.parameters = copy.parameters.clone();
        this.parameterNames = copy.parameterNames;
        this.indexNames = copy.indexNames;
    }

    protected AbstractFunction(final String[] parameterNames) {
        if (parameterNames.length <= 0) {
            this.indexNames = new HashMap<String, Integer>();
            this.parameters = null;
            this.parameterNames = null;
            return;
        }
        this.parameters = new double[parameterNames.length];
        this.parameterNames = parameterNames.clone();
        final Map<String, Integer> newIndexNames = new HashMap<String, Integer>();
        for (int index = 0; index < this.parameterNames.length; index++)
            newIndexNames.put(this.parameterNames[index], index);
        this.indexNames = Collections.unmodifiableMap(newIndexNames);
    }

    protected static String[] combineLabels(final String[] first, final String[] second) {
        final String[] result = new String[first.length + second.length];
        int resultIndex = 0;
        System.arraycopy(first, 0, result, resultIndex, first.length);
        resultIndex += first.length;
        System.arraycopy(second, 0, result, resultIndex, second.length);
        return result;
    }

    @Override
    public final void setParameter(final int parameterIndex, final double value) {
        if (parameterIndex >= this.parameters.length || parameterIndex < 0)
            throw new IllegalArgumentException("parameterIndex of " + parameterIndex + " is out of range");
        this.parameters[parameterIndex] = value;
    }

    @Override
    public final void setParameter(final String parameterName, final double value) {
        this.setParameter(this.getParameterNameIndex(parameterName), value);
    }

    @Override
    public final double getParameter(final int parameterIndex) {
        if (parameterIndex >= this.parameters.length || parameterIndex < 0)
            throw new IllegalArgumentException("parameterIndex out of range");
        return this.parameters[parameterIndex];
    }

    @Override
    public final double getParameter(final String parameterName) {
        return this.getParameter(this.getParameterNameIndex(parameterName));
    }

    @Override
    public final String getParameterName(final int parameterIndex) {
        if (parameterIndex >= this.parameterNames.length || parameterIndex < 0)
            throw new IllegalArgumentException("parameterIndex is not within range");
        return this.parameterNames[parameterIndex];
    }

    @Override
    public final int getParameterNameIndex(final String parameterName) {
        if (!this.indexNames.containsKey(parameterName))
            throw new IllegalArgumentException("parameterName: " + parameterName + " does not exist");
        return this.indexNames.get(parameterName);
    }

    @Override
    public final int getParameterCount() {
        return this.parameters.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractFunction clone() {
        try {
            final AbstractFunction copy = (AbstractFunction) super.clone();
            copy.parameters = this.parameters.clone();
            return copy;
        }
        catch (CloneNotSupportedException caught) {
            LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
            throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
        }
    }

    @Override
    public abstract double calculate();

    @Override
    public abstract String toString();
}
