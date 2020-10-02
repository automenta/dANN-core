package koryk.binpack.gwa;

import com.syncleus.dann.genetics.wavelets.*;

import java.util.*;

public class WaveletBinCell extends Cell {
    public WaveletBinCell(Nucleus nucleus) {
        super(nucleus, new HashSet<>());

        Set<SignalKey> localSignals = this.nucleus.getExpressedSignals(false);
        for (SignalKey localSignal : localSignals) {
            SignalKeyConcentration newConcentration = new SignalKeyConcentration(localSignal);
            this.localConcentrations.add(newConcentration);
            this.nucleus.bind(newConcentration, false);
        }
    }

    public WaveletBinCell mate(WaveletBinCell cell) {
        return new WaveletBinCell(((WaveletBinNucleus) nucleus).mate((WaveletBinNucleus) cell.nucleus));//return nucleus.mate(cell.nucleus);
    }

    public List<AbstractWaveletGene> getGenes() {
        return ((WaveletBinNucleus) nucleus).getGenes();
    }

    public Set<SignalKeyConcentration> getConcentrations() {
        Set<SignalKeyConcentration> keys = new HashSet<>();
        for (AbstractWaveletGene g : ((WaveletBinNucleus) nucleus).getGenes()) {
            if (g instanceof SignalGene) {
                SignalKeyConcentration conc = ((SignalGene) g).getExpressingConcentration();
                //conc.setConcentration(g.expressionActivity());
                if (conc!=null)
                    keys.add(conc);
            }
        }
        return keys;
    }

    public Set<SignalKeyConcentration> getOrderedSignals() {
        TreeSet<SignalKeyConcentration> sortedKeys = new TreeSet<>(
                Comparator.comparingDouble(SignalKeyConcentration::getConcentration));
        sortedKeys.addAll(this.getConcentrations());
        return sortedKeys;
    }

    public void mutate(Random rng) {
        nucleus.mutate(rng);
    }

    public Set<AbstractWaveletGene> getOrderedGenes() {
        TreeSet<AbstractWaveletGene> sortedGenes = new TreeSet<>(Comparator.comparingDouble(AbstractWaveletGene::expressionActivity));

        sortedGenes.addAll(this.getGenes());

        return Collections.unmodifiableSortedSet(sortedGenes);
    }
}
