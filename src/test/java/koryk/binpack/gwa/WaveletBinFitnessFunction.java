package koryk.binpack.gwa;

import com.syncleus.dann.genetics.wavelets.SignalKeyConcentration;
import koryk.ga.AbstractWaveletFitnessFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WaveletBinFitnessFunction extends AbstractWaveletFitnessFunction<WaveletBinFitnessFunction> {
    protected double percentFull = 0;
    private double[] bins, items;
    private double[] weights;
    private boolean first = true;
    private WaveletBinIndividual individual;
    private ArrayList<Integer> usedInts = new ArrayList<>();

    public WaveletBinFitnessFunction(WaveletBinIndividual chrom, double[] bins, double[] items, double[] weights) {
        super(chrom);

        this.individual = chrom;
        WaveletBinCell c = individual.getCell();
        this.bins = bins;
        this.items = items;
        this.weights = weights;
    }

    public ArrayList<Integer> getItemsOrder() {
        return usedInts;
    }

    @Override
    public int compareTo(WaveletBinFitnessFunction comp) {
        // TODO Auto-generated method stub
        if (percentFull < comp.percentFull)
            return -1;
        else if (percentFull > comp.percentFull)
            return 1;
        return (comp.hashCode() > hashCode()) ? 1 : (comp.hashCode() < hashCode()) ? -1 : 0;
    }

    @Override
    public void process() {
        // TODO Auto-generated method stub
        percentFull = 0;
        WaveletBinCell c = individual.getCell();
        double[] binSpace = bins.clone();
        if (first) {
            c.preTick();
            c.tick();
            first = false;
        }
        List<SignalKeyConcentration> orderedConcentrations = new ArrayList<>(c.getOrderedSignals()), indexedConcentrations = new ArrayList<>(c.getConcentrations());
        List<Integer> usedNums = new ArrayList<>();
        int currBin = 0;
        if (orderedConcentrations.size() == 0)
            return;
        double item;
        for (SignalKeyConcentration con : orderedConcentrations) {
            boolean used = false;
            currBin = 0;
            for (int i : usedNums)
                if (i == indexedConcentrations.indexOf(con) % items.length) {
                    used = true;
                    break;
                }
            if (used)
                continue;
            item = items[indexedConcentrations.indexOf(con) % items.length];
            for (currBin = 0; currBin < bins.length; currBin++)
                if (item <= binSpace[currBin]) {
                    binSpace[currBin] -= item;
                    usedNums.add(indexedConcentrations.indexOf(con) % items.length);
                    break;
                }
        }


        for (int i : usedNums) {
            percentFull += (items[i] * weights[i]);
            //System.out.print("!" + i + ":" + items[i] + " " + weights[i] + ":" + items[i]*weights[i] +"!");
        }
        usedInts = new ArrayList<>(usedNums);
        percentFull /= (bins[0] * bins.length);
        //System.out.println(" " + percentFull);
    }

    public ArrayList<Double>[] getSolution() {
        WaveletBinCell c = individual.getCell();
        double[] binSpace = bins.clone();
        ArrayList<Double>[] solution = new ArrayList[bins.length];
        for (int i = 0; i < solution.length; i++)
            solution[i] = new ArrayList<>();
        List<SignalKeyConcentration> orderedConcentrations = new ArrayList<>(c.getOrderedSignals()), indexedConcentrations = new ArrayList<>(c.getConcentrations());
        List<Integer> usedNums = new ArrayList<>();
        int currBin = 0;
        double totalConc = 0;
        for (SignalKeyConcentration con : orderedConcentrations)
            totalConc += Math.abs(con.getConcentration());
        if (orderedConcentrations.size() == 0)
            return solution;

        double item;
        for (SignalKeyConcentration con : orderedConcentrations) {
            boolean used = false;
            currBin = 0;
            for (int i : usedNums)
                if (i == indexedConcentrations.indexOf(con) % items.length) {
                    used = true;
                    break;
                }
            if (used)
                continue;
            item = items[indexedConcentrations.indexOf(con) % items.length];
            while (currBin <= bins.length - 1)
                if (item <= binSpace[currBin]) {
                    binSpace[currBin] -= item;
                    solution[currBin].add((double) indexedConcentrations.indexOf(con) % items.length);
                    usedNums.add(indexedConcentrations.indexOf(con) % items.length);
                    break;
                } else if (currBin < bins.length - 1) {
                    currBin++;
                } else
                    break;
        }


        return solution;
    }

    public String toString() {
        String ret = "";
        WaveletBinCell c = individual.getCell();
        List<SignalKeyConcentration> indexedConcentrations = new ArrayList<>(c.getConcentrations());
        double[] binSpace = bins.clone();
        Set<SignalKeyConcentration> orderedConcentrations = c.getOrderedSignals();
        List<Integer> usedNums = new ArrayList<>();
        int currBin = 0, totalWeight = 0, currBinWeight = 0;

        if (orderedConcentrations.size() == 0)
            return ret;
        int zerocount = 0;

        int itemIndex = 0, itemCount = 0;
        Integer place = orderedConcentrations.size() % items.length;

        for (SignalKeyConcentration conc : orderedConcentrations) {
            place = (indexedConcentrations.indexOf(conc)) % items.length;
            boolean used = false;
            //System.out.print(".");
            for (int j : usedNums)
                if (place == j) {
                    used = true;
                    break;
                }
            if (used)
                continue;
            double item = items[place];
            if (item > binSpace[currBin]) {
                if (currBin < bins.length - 1)
                    currBin++;
            } else {

                currBinWeight += item;
                itemCount++;
                usedNums.add(place);
            }
            if (itemCount >= items.length)
                break;
        }


        for (int i : usedNums) {
            ret += i + " : " + items[i] * weights[i] + " ";
        }
        return ret;
    }

}
