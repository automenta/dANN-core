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
package com.syncleus.dann.genetics.wavelets;

import com.syncleus.dann.UnexpectedDannError;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

/**
 * A Nucleus is a collection of Chromosomes with a known mutability.
 *
 * @see com.syncleus.dann.genetics.wavelets.Chromosome
 */
public class Nucleus implements Cloneable {
    private static final Logger LOGGER = Logger.getLogger(Nucleus.class);
    private static final double MUTABILITY_ADJUSTMENT = 10.0;
    protected List<Chromosome> chromosomes;

    public Nucleus(Supplier<Chromosome> b) {
        this(new ArrayList<>());
        final double mutability = Mutations.getRandom().nextDouble() * 0.5;// * MUTABILITY_ADJUSTMENT;
        //make sure there is at least one starting chromosome.
        this.chromosomes.add(b.get());
        //there is a chance more chromosomes can be created
        while (Mutations.mutationEvent(mutability))
            this.chromosomes.add(b.get());
    }
    /**
     * Creates a new Nucleus with at least one chromosome. Additional
     * chromosomes could be added based on the random mutability factor.
     */
    public Nucleus(List<Chromosome> chromosomes) {

        this.chromosomes = chromosomes;
    }

    /**
     * Creates a new Nucleus as a copy of the old Nucleus.
     *
     * @param copy The Nucleus to copy
     */
    public Nucleus(final Nucleus copy) {
        this.chromosomes = new ArrayList<>(copy.getChromosomes());
    }

    /**
     * Gets all chromosomes associated with this object.
     *
     * @return A list of chromosomes
     */
    protected List<Chromosome> getChromosomes() {
        return Collections.unmodifiableList(this.chromosomes);
    }

    /**
     * Performs a preTick() operation on each Chromosome.
     *
     * @see Chromosome#preTick()
     */
    public void preTick() {
        for (final Chromosome chromosome : this.chromosomes)
            chromosome.preTick();
    }

    /**
     * Performs a tick() operation on each Chromosome.
     *
     * @see Chromosome#tick()
     */
    public void tick() {
        for (final Chromosome chromosome : this.chromosomes)
            chromosome.tick();
    }

    public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal) {
        boolean bound = true;
        for (final Chromosome chromosome : this.chromosomes)
            if (chromosome.bind(concentration, isExternal))
                bound = true;
        return bound;
    }

    @Override
    public Nucleus clone() {
        try {
            final Nucleus copy = (Nucleus) super.clone();
            copy.chromosomes = new ArrayList<>();
            for (final Chromosome chromosome : this.chromosomes)
                copy.chromosomes.add(chromosome.clone());
            return copy;
        } catch (CloneNotSupportedException caught) {
            LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
            throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
        }
    }

    public void mutate(Random random) {
        final Set<AbstractKey> allKeys = new HashSet<>();
        for (final Chromosome chromosome : this.chromosomes)
            allKeys.addAll(chromosome.getKeys());

        for (final Chromosome chromosome : this.chromosomes)
            chromosome.mutate(allKeys, random);
    }

    public void mutate(final Set<AbstractKey> keyPool, Random random) {
        final Set<AbstractKey> allKeys = new HashSet<>(keyPool);
        for (final Chromosome chromosome : this.chromosomes)
            allKeys.addAll(chromosome.getKeys());

        for (final Chromosome chromosome : this.chromosomes)
            chromosome.mutate(allKeys, random);
    }

    public Set<SignalKey> getExpressedSignals(final boolean external) {
        final Set<SignalKey> allSignals = new HashSet<>();
        for (final Chromosome chromosome : this.chromosomes)
            allSignals.addAll(chromosome.getExpressedSignals(external));
        return Collections.unmodifiableSet(allSignals);
    }
}
