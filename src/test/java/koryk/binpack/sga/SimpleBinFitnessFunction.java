package koryk.binpack.sga;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import com.syncleus.dann.genetics.AbstractGeneticAlgorithmFitnessFunction;
import com.syncleus.dann.genetics.AbstractValueGene;


public class SimpleBinFitnessFunction extends AbstractGeneticAlgorithmFitnessFunction<SimpleBinFitnessFunction>{
	double[] bins, items;
	double[] weights;
	private ArrayList<Integer> chosenItems = null;
	public double percentFull = 0;
	public SimpleBinFitnessFunction(SimpleBinChromosome chromosome, double[] bins, double[] items, double[] weights) {
		super(chromosome);
		this.weights = weights;
		this.bins = bins;
		this.items = items;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Double>[] getSolution(){
		int currBin = 0, totalWeight = 0, currBinWeight = 0;
		final SortedSet<AbstractValueGene> sortedGenes = this.getChromosome().getSortedGenes();
		final List<AbstractValueGene> indexedGenes = this.getChromosome().getGenes();
		ArrayList<Double>[] solution = new ArrayList[bins.length];
		for (int i = 0; i < solution.length; i++)
			solution[i] = new ArrayList<Double>();
		for (AbstractValueGene g : sortedGenes) {
			double item = items[indexedGenes.indexOf(g)];
			if (currBinWeight+item > bins[currBin])
				if (currBin < bins.length-1){
					currBin++;
					currBinWeight = 0;
				}
				else
					;
			else {
				solution[currBin].add(item*weights[indexedGenes.indexOf(g)]);
				currBinWeight+=item;
				totalWeight+=item;
			}				
		}
		return solution;
	}

	@Override
	public void process() {
		int currBin = 0;
		double totalWeight = 0, currBinWeight = 0;
		final SortedSet<AbstractValueGene> sortedGenes = this.getChromosome().getSortedGenes();
		final List<AbstractValueGene> indexedGenes = this.getChromosome().getGenes();
		List<Integer> usedList = new ArrayList<Integer>();
		for (AbstractValueGene g : sortedGenes) {
			double item = items[indexedGenes.indexOf(g)];
			if (currBinWeight+item > bins[currBin]){
				if (currBin < bins.length-1){
					currBin++;
					currBinWeight = 0;
				}
				else
					break;
			}
			else {
				currBinWeight+=item;
				totalWeight+=item;
				usedList.add(indexedGenes.indexOf(g));
			}				
		}
		percentFull = 0;
		for ( int i : usedList)
			percentFull += items[i] * weights[i];
		percentFull /= bins[0]*bins.length;
		chosenItems = (ArrayList<Integer>) usedList;
		
	}

	public ArrayList<Integer> getChosenItems() {
		return chosenItems;
	}

	public SimpleBinChromosome getChromosome(){
		return (SimpleBinChromosome)super.getChromosome();
	}
	@Override
	public int compareTo(SimpleBinFitnessFunction comp) {
		if (comp.percentFull > percentFull)
			return -1;
		else if (comp.percentFull < percentFull)
			return 1;
		return 0;
	}
	public String toString() {
		String ret ="";
		int currBin = 0, totalWeight = 0, currBinWeight = 0;
		final SortedSet<AbstractValueGene> sortedGenes = this.getChromosome().getSortedGenes();
		final List<AbstractValueGene> indexedGenes = this.getChromosome().getGenes();
		ret+="[0] ";
		for (AbstractValueGene g : sortedGenes) {
			double item = items[indexedGenes.indexOf(g)];
			if (currBinWeight+item > bins[currBin])
				if (currBin < bins.length-1){
					currBin++;
					ret +=" - " + currBinWeight;
					ret+="\n["+currBin+"] ";
					currBinWeight = 0;
				}
				else ;
			else {
				currBinWeight+=item;
				totalWeight+=item;
				ret += indexedGenes.indexOf(g) + ":" + item + " "; 
			}				
		}
		ret +=" - " + currBinWeight;
		int binSpace=0;
		ret += "\n " + (double)percentFull;
		return ret;
	}



}
