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
import com.syncleus.dann.genetics.MutableDouble;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * A Chromosome is a mutable collection of Chromatids for use in a genetic algorithm.
 */
public class Chromosome implements Cloneable {
    private static final Logger LOGGER = Logger.getLogger(Chromosome.class);
    private WaveletChromatid left;
    private WaveletChromatid right;
    private double mutability;

//    /**
//     * Creates a Chromasome with random left and right chromatids.
//     */
//    public Chromosome() {
//        this(WaveletChromatid.newRandomWaveletChromatid(), WaveletChromatid.newRandomWaveletChromatid());
//    }

    public Chromosome(WaveletChromatid l, WaveletChromatid r) {
        this.left = l;
        this.right = r;
        this.mutability = 0.5;
    }

    /**
     * Creates a chromosome as a copy of another.
     *
     * @param copy The chromosome to copy
     */
    public Chromosome(final Chromosome copy) {
        this.left = new WaveletChromatid(copy.left);
        this.right = new WaveletChromatid(copy.right);
        this.mutability = copy.mutability;
    }

    /**
     * Gets the expressed signals of this chromosome.
     *
     * @param external Whether the caller is external to the Chromosome
     * @return The set of SignalKeys
     */
    Set<SignalKey> getExpressedSignals(final boolean external) {
        final Set<SignalKey> allSignals = new HashSet<>(this.left.getExpressedSignals(external));
        allSignals.addAll(this.right.getExpressedSignals(external));
        return Collections.unmodifiableSet(allSignals);
    }

    /**
     * Gets an unmodifiable set keys of this Chromosome.
     *
     * @return The expressed keys
     */
    public Set<AbstractKey> getKeys() {
        final HashSet<AbstractKey> allKeys = new HashSet<>();
        allKeys.addAll(this.left.getKeys());
        allKeys.addAll(this.right.getKeys());
        return Collections.unmodifiableSet(allKeys);
    }

    /**
     * Prepares both chromatids to tick.
     *
     * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#preTick()
     */
    public void preTick() {
        this.left.preTick();
        this.right.preTick();
    }

    /**
     * Ticks both chromatids.
     *
     * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#tick()
     */
    public void tick() {
        this.left.tick();
        this.right.tick();
    }

    /**
     * Binds a SignalKeyConcentration to both Chromatids.
     *
     * @param concentration The signal key concentration
     * @param isExternal    Whether this signal is external to the Chromosome
     * @return Whether the bind was successful
     * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#bind(SignalKeyConcentration, boolean)
     */
    public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal) {
        boolean bound = false;
        if (this.left.bind(concentration, isExternal))
            bound = true;
        if (this.right.bind(concentration, isExternal))
            bound = true;
        return bound;
    }

    /**
     * Gets the left chromatid.
     *
     * @return The left chromatid
     */
    protected WaveletChromatid getLeftChromatid() {
        return this.left;
    }

    /**
     * Gets the right chromatid.
     *
     * @return The right chromatid
     */
    protected WaveletChromatid getRight() {
        return this.right;
    }

    /**
     * Gets all genes in the chromosome.
     *
     * @return An unmodifiable list of genes
     */
    public List<AbstractWaveletGene> getGenes() {
        final List<AbstractWaveletGene> genes = new ArrayList<>(this.left.getGenes());
        genes.addAll(this.right.getGenes());
        return Collections.unmodifiableList(genes);
    }

    /**
     * Gets all promoter genes in the chromosome.
     *
     * @return An unmodifiable list of promoter genes.
     * @see WaveletChromatid#getPromoterGenes()
     */
    public List<PromoterGene> getPromoterGenes() {
        final List<PromoterGene> promoters = new ArrayList<>(this.left.getPromoterGenes());
        promoters.addAll(this.right.getPromoterGenes());
        return Collections.unmodifiableList(promoters);
    }

    /**
     * Gets all local signal genes expressed on the chromosome.
     *
     * @return An unmodifiable list of local signal genes
     * @see WaveletChromatid#getLocalSignalGenes()
     */
    public List<SignalGene> getLocalSignalGenes() {
        final List<SignalGene> localSignalGenes = new ArrayList<>(this.left.getLocalSignalGenes());
        localSignalGenes.addAll(this.right.getLocalSignalGenes());
        return Collections.unmodifiableList(localSignalGenes);
    }

    /**
     * Gets all external signal genes expressed on the chromosome.
     *
     * @return An unmodifiable list of external signal genes
     * @see WaveletChromatid#getExternalSignalGenes()
     */
    public List<ExternalSignalGene> getExternalSignalGenes() {
        final List<ExternalSignalGene> externalSignalGenes = new ArrayList<>(this.left.getExternalSignalGenes());
        externalSignalGenes.addAll(this.right.getExternalSignalGenes());
        return Collections.unmodifiableList(externalSignalGenes);
    }

    /**
     * Crosses over the two chromatids.
     *
     * @param deviation The amount of deviation allowable
     */
    private void crossover(final double deviation, Random RANDOM) {
        //find the crossover position
        final int crossoverPosition;
        if (RANDOM.nextBoolean()) {
            final int length = (Math.min(this.left.getCentromerePosition(), this.right.getCentromerePosition()));

            final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
            if (fromEnd > length)
                crossoverPosition = 0;
            else
                crossoverPosition = -1 * (length - fromEnd);
        } else {
            final int leftLength = this.left.getGenes().size() - this.left.getCentromerePosition();
            final int rightLength = this.right.getGenes().size() - this.left.getCentromerePosition();

            final int length = (Math.min(leftLength, rightLength));

            final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
            if (fromEnd > length)
                crossoverPosition = 0;
            else
                crossoverPosition = (length - fromEnd);
        }

        //perform the crossover.
        final List<AbstractWaveletGene> leftGenes = this.left.crossover(crossoverPosition);
        if (leftGenes!=null) {
            final List<AbstractWaveletGene> rightGenes = this.right.crossover(crossoverPosition);
            if (rightGenes!=null) {
                this.left.crossover(rightGenes, crossoverPosition);
                this.right.crossover(leftGenes, crossoverPosition);
            }
        }
    }

    /**
     * Creates a deep clone of the current object.
     *
     * @return A clone of a chromosome.
     */
    @Override
    public Chromosome clone() {
        try {
            final Chromosome copy = (Chromosome) super.clone();
            copy.left = this.left.clone();
            copy.right = this.right.clone();
            copy.mutability = this.mutability;
            return copy;
        } catch (CloneNotSupportedException caught) {
            final String userMessage = "CloneNotSupportedException caught but not expected!";
            LOGGER.error(userMessage, caught);
            throw new UnexpectedDannError(userMessage, caught);
        }
    }

    /**
     * Mutates the chromosome by the given key pool.
     *
     * @param keyPool The key pool to mutate by
     * //@see com.syncleus.dann.genetics.wavelets.WaveletChromatid#mutate(java.util.Set)
     */
    public void mutate(final Set<AbstractKey> keyPool, Random rng) {
        if (Mutations.mutationEvent(this.mutability))
            this.crossover(this.mutability, rng);
        this.left.mutate(keyPool, rng);
        this.right.mutate(keyPool, rng);
        if (Mutations.mutationEvent(this.mutability))
            this.mutability = Mutations.mutabilityMutation(this.mutability);
    }
}
