package koryk.binpack.sga;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.syncleus.dann.genetics.AbstractValueGene;
import com.syncleus.dann.genetics.GeneticAlgorithmChromosome;






import koryk.binpack.BinPackProblem;
import koryk.ga.Problem;
import koryk.gui.ProblemPanel;

public class SimpleBinProblemPanel extends ProblemPanel{
	private int generation = -1;
	public int getGeneration() {
		return generation;
	}
	public SimpleBinProblemPanel(Problem problem){
		super(problem);
	}
	private SimpleBinPopulation population;
//	private Future<SimpleBinChromosome> futureWinner = null;
//	private final ExecutorService executor = Executors.newFixedThreadPool(1);
	private SimpleBinChromosome currentWinner = null;
	private SimpleBinFitnessFunction finalWinner = null;
	public void loadPanel() {
		// TODO Auto-generated method stub
		
	}
	public ArrayList<Integer> getItemOrder(){
		return finalWinner.getChosenItems();
	}
@Override
	public void runGA() {		
		population = new SimpleBinPopulation(10, .4, .4, (BinPackProblem)(problem));
		population.initializePopulation(50);
		//try{Thread.sleep(10000);}catch(Exception e){;}
		int i=0, max = 50;
		SimpleBinFitnessFunction oldWinner = null;
		int convergeGeneration=0;
		double prevFull = 0;
		try
		{
			for(; i < max; i++){
				population.nextGeneration();
				currentWinner = population.getWinner();
				for (GeneticAlgorithmChromosome c : population.getChromosomes()){
					for (AbstractValueGene g : ((SimpleBinChromosome)c).getSortedGenes())
						System.out.print(g.getValue().longValue()+" ");
					System.out.println();
				}
				SimpleBinFitnessFunction winner = (SimpleBinFitnessFunction)population.packageChromosome(currentWinner);
				winner.process();
				if (prevFull == 1){ 
					//System.out.println("Found solution in " + i + " generations");
					break;}
				if (prevFull < winner.percentFull){
					//System.out.println(winner);
					oldWinner = winner;
					convergeGeneration = population.getGenerations();
					prevFull = winner.percentFull;
					//System.out.println("Wooooo !!" + (prevFull = winner.percentFull) + " " + population.getGenerations() + " generations");				
				for (AbstractValueGene g : ((SimpleBinChromosome)currentWinner).getSortedGenes())
					;//System.out.print(currentWinner.getGenes().indexOf(g) + ":" + g.getValue() + " ");
				//System.out.println();
				}
				
			}//this.currentWinner = this.futureWinner.get();
			finalWinner = oldWinner;
			FileOutputStream os = new FileOutputStream(new File("sga.txt"),true);
			os.write((problem.toString() + ": " + prevFull + " at " + convergeGeneration + " : ").getBytes());
			for (int j = 0 ; j < oldWinner.getSolution().length; j++){
				os.write((j + ":").getBytes());
				for (Double d : oldWinner.getSolution()[j])
					os.write((d + ",").getBytes());
			}
			os.write("\n\n".getBytes());
			os.close();
			generation = convergeGeneration;
			System.out.println(problem.toString() + ": " + prevFull + " at " + convergeGeneration );
			System.out.println("ga over");
			
		}
		catch(Exception caught)
		{
			caught.printStackTrace();				
			throw new RuntimeException("Throwable was caught" + caught.getMessage(), caught);
		}
		catch(Error caught)
		{
			throw new Error("Throwable was caught" + caught.getMessage());
		}			
	}
	
}



	

