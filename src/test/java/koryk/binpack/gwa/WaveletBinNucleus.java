package koryk.binpack.gwa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.syncleus.dann.genetics.wavelets.AbstractKey;
import com.syncleus.dann.genetics.wavelets.Chromosome;
import com.syncleus.dann.genetics.wavelets.Mutations;
import com.syncleus.dann.genetics.wavelets.Nucleus;
import com.syncleus.dann.genetics.wavelets.AbstractWaveletGene;;


public class WaveletBinNucleus extends Nucleus{
	private final int maxChroms = 1, geneSize;

	public WaveletBinNucleus(ArrayList<Chromosome> chroms){
		super(chroms);
		this.geneSize = ((WaveletBinChromosome)chroms.get(0)).getSize();
		checkChromosomeSize();
	}

	public WaveletBinNucleus(int size){
		super(()->new WaveletBinChromosome(size));
		this.geneSize =size;
		checkChromosomeSize();
	}

	public List<AbstractWaveletGene> getGenes(){
		List<AbstractWaveletGene> retVal = new ArrayList<>();
		for (Chromosome c : this.getChromosomes()){
			for (AbstractWaveletGene g : c.getGenes())
				retVal.add(g);
		}
		return retVal;
	}
	public void mutate(Random rng)
	{
		HashSet<AbstractKey> allKeys = new HashSet<>();
		for(Chromosome chromosome : this.chromosomes)
			allKeys.addAll(chromosome.getKeys());
		for(Chromosome chromosome : this.chromosomes)
			chromosome.mutate(allKeys, rng);
		checkChromosomeSize();
	}
	protected void checkChromosomeSize(){
		if (chromosomes.size() > maxChroms){
			while (chromosomes.size() > maxChroms)
				chromosomes.remove(Mutations.getRandom().nextInt(chromosomes.size()));
		}			
	}
	public WaveletBinNucleus mate (WaveletBinNucleus partner){
		List<Chromosome> chroms = new ArrayList<>(this.getChromosomes());
		List<Chromosome> partnerChroms = new ArrayList<>(partner.getChromosomes());
		ArrayList<Chromosome> resultingChroms = new ArrayList<>();
		List<AbstractKey> keys = new ArrayList<>();
		if (Math.min(chroms.size(), partnerChroms.size()) != chroms.size()){
			chroms = partnerChroms;
			partnerChroms = new ArrayList<>(this.getChromosomes());
		}
		for (Chromosome chrom : partnerChroms)
			keys.addAll(chrom.getKeys());
			
		for (Chromosome chrom : chroms)
			keys.addAll(chrom.getKeys());

		int i = 0;
		
		for (i=0; i < chroms.size(); i++){
			resultingChroms.add(((WaveletBinChromosome)chroms.get(i)).crossOver((WaveletBinChromosome)partnerChroms.get(i),keys));			
			resultingChroms.get(i);
		}
		
		return new WaveletBinNucleus(resultingChroms);	
			
	}
}
