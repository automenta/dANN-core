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
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import com.syncleus.dann.neural.backprop.SimpleBackpropNeuron;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestHyperassociativeMap {
    @Test
    public void testRefresh() throws InvalidConnectionTypeDannException {
        final TestBrain testBrain = new TestBrain();

        final SimpleBackpropNeuron neuron1 = new SimpleBackpropNeuron(testBrain);
        final SimpleBackpropNeuron neuron2 = new SimpleBackpropNeuron(testBrain);

        testBrain.add(neuron1);
        testBrain.add(neuron2);

        Synapse<BackpropNeuron> synapse = new SimpleSynapse<>(neuron1, neuron2);
        testBrain.connect(synapse, true);

        final TestMap testMap;
        final int cores = Runtime.getRuntime().availableProcessors();
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(cores + 1, cores * 2, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());
        try {
            testMap = new TestMap(testBrain, 3, executor);
            testMap.align();

            Assert.assertTrue("neuron1 is not in the map", testMap.getGraph().getNodes().contains(neuron1));
            Assert.assertTrue("neuron2 is not in the map", testMap.getGraph().getNodes().contains(neuron2));

            Assert.assertTrue("neuron1 is not associated to neuron2", testMap.getGraph().getAdjacentNodes(neuron1).contains(neuron2));
            Assert.assertTrue("neuron2 is not associated to neuron1", testMap.getGraph().getAdjacentNodes(neuron2).contains(neuron1));
        } finally {
            executor.shutdown();
        }
    }

    private static class TestBrain extends AbstractLocalBrain {
        private static final long serialVersionUID = 6640257805440602087L;

        @Override
        public boolean add(final Neuron newNeuron) {
            return super.add(newNeuron);
        }

        @Override
        public boolean connect(final Synapse synapse, final boolean initialize) {
            return super.connect(synapse, initialize);
        }
    }

    private static class TestMap extends HyperassociativeMap<AbstractLocalBrain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>>, Neuron> {
        public TestMap(final AbstractLocalBrain brain, final int dimensions, final ThreadPoolExecutor executor) {
            super(brain, dimensions, executor);
        }
    }
}
