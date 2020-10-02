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
package com.syncleus.dann.dataprocessing.language.parsing.stem;

import org.junit.Assert;
import org.junit.Test;

public class TestPorterStemmer {
    @Test
    public void testWords() {
        final Stemmer stemmer = new PorterStemmer();

        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("bowling").compareToIgnoreCase("bowl"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("happiness").compareToIgnoreCase("happi"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("jeffrey").compareToIgnoreCase("jeffrei"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("running").compareToIgnoreCase("run"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("napping").compareToIgnoreCase("nap"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("runner").compareToIgnoreCase("runner"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("hiker").compareToIgnoreCase("hiker"));
        Assert.assertEquals("word stem incorrect!", 0, stemmer.stemWord("Nonsense").compareToIgnoreCase("Nonsens"));
    }
}
