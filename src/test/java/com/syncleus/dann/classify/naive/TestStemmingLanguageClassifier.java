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
package com.syncleus.dann.classify.naive;

import org.junit.Assert;
import org.junit.Test;

public class TestStemmingLanguageClassifier {
    @Test
    public void testClassify() {
        final TrainableLanguageNaiveClassifier<Integer> classifier = new StemmingLanguageNaiveClassifier<>();

        //train
        classifier.train("Money is the root of all evil!", 1);
        classifier.train("Money destroys the soul", 1);
        classifier.train("Money kills!", 1);
        classifier.train("The quick brown fox.", 1);
        classifier.train("Money should be here once", 2);
        classifier.train("some nonsense to take up space", 2);
        classifier.train("Even more nonsense cause we can", 2);
        classifier.train("nonsense is the root of all good", 2);
        classifier.train("just a filler to waste space", 2);

        //test
        Assert.assertEquals("Feature had incorrect category!", 1, (int) classifier.featureClassification("Money"));
        Assert.assertEquals("Feature had incorrect category!", 1, (int) classifier.featureClassification("Fox"));
        Assert.assertEquals("Feature had incorrect category!", 2, (int) classifier.featureClassification("Nonsense"));
        Assert.assertEquals("Feature had incorrect category!", 2, (int) classifier.featureClassification("Waste"));
        Assert.assertEquals("Feature had incorrect category!", 1, (int) classifier.featureClassification("Evil"));
        Assert.assertEquals("Feature had incorrect category!", 2, (int) classifier.featureClassification("Good"));

        Assert.assertEquals("Item had incorrect category!", 2, (int) classifier.classification("Money was here once"));
        Assert.assertEquals("Item had incorrect category!", 1, (int) classifier.classification("Money destroys the quick brown fox!"));
        Assert.assertEquals("Item had incorrect category!", 1, (int) classifier.classification("kills the soul"));
        Assert.assertEquals("Item had incorrect category!", 1, (int) classifier.classification("nonsense is the root of good"));
    }
}
