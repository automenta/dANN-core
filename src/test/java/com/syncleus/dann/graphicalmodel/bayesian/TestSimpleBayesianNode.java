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

import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.ImmutableDirectedEdge;
import com.syncleus.dann.graphicalmodel.GraphicalModelNode;
import com.syncleus.dann.graphicalmodel.SimpleGraphicalModelNode;
import org.junit.Assert;
import org.junit.Test;

public class TestSimpleBayesianNode {
    @Test
    public void testSingleNode() {
        final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
        final GraphicalModelNode<SimpleEnum> testNode = new SimpleGraphicalModelNode<>(SimpleEnum.TRUE);

        network.add(testNode);

        Assert.assertSame("initial state not retained!", testNode.getState(), SimpleEnum.TRUE);

        testNode.setState(SimpleEnum.FALSE);
        Assert.assertSame("set state not retained!", testNode.getState(), SimpleEnum.FALSE);

        testNode.learnState();
        Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.FALSE));
        Assert.assertFalse("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.TRUE));

        testNode.learnState();
        testNode.learnState();
        testNode.setState(SimpleEnum.TRUE);
        testNode.learnState();
        Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.FALSE));
        Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.TRUE));
        Assert.assertTrue("bad state probability!", Math.abs(testNode.stateProbability() - 0.25) < 0.0001);
    }

    @Test
    public void testDependentNode() {
        final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
        final GraphicalModelNode<SimpleEnum> parentNode = new SimpleGraphicalModelNode<>(SimpleEnum.TRUE);
        final GraphicalModelNode<SimpleEnum> childNode = new SimpleGraphicalModelNode<>(SimpleEnum.TRUE);

        network.add(parentNode);
        network.add(childNode);

        final DirectedEdge<GraphicalModelNode<SimpleEnum>> testEdge = new ImmutableDirectedEdge<>(parentNode, childNode);
        network.add(testEdge);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.TRUE);
        Assert.assertTrue("bad state probability (TRUE,TRUE)! stateProbability: " + childNode.stateProbability(), Math.abs(childNode.stateProbability() - 0.25) < 0.0001);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        Assert.assertTrue("bad state probability (TRUE,FALSE)!: " + childNode.stateProbability(), Math.abs(childNode.stateProbability() - 0.75) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        Assert.assertTrue("bad state probability (FALSE,TRUE)!: " + childNode.stateProbability(), Math.abs(childNode.stateProbability() - 0.75) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.FALSE);
        Assert.assertTrue("bad state probability (FALSE,FALSE)!: " + childNode.stateProbability(), Math.abs(childNode.stateProbability() - 0.25) < 0.0001);
    }

    private enum SimpleEnum {
        TRUE, FALSE
    }
}
