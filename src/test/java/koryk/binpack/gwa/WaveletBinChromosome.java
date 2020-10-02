package koryk.binpack.gwa;

import com.syncleus.dann.genetics.MutableDouble;
import com.syncleus.dann.genetics.wavelets.AbstractKey;
import com.syncleus.dann.genetics.wavelets.Chromosome;
import com.syncleus.dann.genetics.wavelets.Mutations;
import com.syncleus.dann.genetics.wavelets.WaveletChromatid;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WaveletBinChromosome extends Chromosome {
    private int size;

    public WaveletBinChromosome(int size) {
        this(size, ThreadLocalRandom.current());
    }

    public WaveletBinChromosome(int size, Random rng) {
        super(WaveletChromatid.newRandomWaveletChromatid(size / 2, rng), WaveletChromatid.newRandomWaveletChromatid(size / 2 + size % 2, rng));
        this.size = size;
    }

    public WaveletBinChromosome(WaveletChromatid left, WaveletChromatid right) {
        super(left, right);
    }

    public WaveletBinChromosome crossOver(WaveletBinChromosome chrom, List<AbstractKey> keypool) {
        WaveletChromatid left, right;
        final Random random = Mutations.getRandom();
        if (random.nextBoolean()) {
            left = getLeftChromatid();
            right = chrom.getRight();
        } else {
            left = chrom.getLeftChromatid();
            right = getRight();
        }
        WaveletBinChromosome child = new WaveletBinChromosome(left, right);
        while (Mutations.mutationEvent(random.nextDouble()))
            child.mutate(new HashSet<>(keypool), random);
        return child;
    }

    public int getSize() {
        return size;
    }

    private int findCrossoverPosition() {
        float deviation = Mutations.getRandom().nextFloat();
        WaveletChromatid left, right;

        left = getLeftChromatid();
        right = getRight();
        int crossoverPosition = 0;
        if (Mutations.getRandom().nextBoolean()) {
            final int length = (left.getCentromerePosition() < right.getCentromerePosition() ? left.getCentromerePosition() : right.getCentromerePosition());

            final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
            if (fromEnd > length)
                crossoverPosition = 0;
            else
                crossoverPosition = -1 * (length - fromEnd);
        } else {
            final int leftLength = left.getGenes().size() - left.getCentromerePosition();
            final int rightLength = right.getGenes().size() - left.getCentromerePosition();

            final int length = (leftLength < rightLength ? leftLength : rightLength);

            final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
            if (fromEnd > length)
                crossoverPosition = 0;
            else
                crossoverPosition = (length - fromEnd);
        }
        return crossoverPosition;

    }
}
