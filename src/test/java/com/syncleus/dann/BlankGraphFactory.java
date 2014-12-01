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
package com.syncleus.dann;

import com.syncleus.dann.backprop.*;
import com.syncleus.grail.graph.GrailGraph;
import com.syncleus.grail.graph.TinkerGrailGraphFactory;

import java.io.File;
import java.util.*;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.*;

public final class BlankGraphFactory {
    private static final Set<Class<?>> DANN_TYPES =
            new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{
                    ActivationNeuron.class,
                    AbstractActivationNeuron.class,
                    BackpropNeuron.class,
                    AbstractBackpropNeuron.class,
                    BackpropSynapse.class,
                    AbstractBackpropSynapse.class}));

    public static GrailGraph makeTinkerGraph() {
        return new TinkerGrailGraphFactory(DANN_TYPES).subgraph("0");
    }
}