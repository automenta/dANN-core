package koryk.binpack.gwa;

import java.awt.Graphics;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import koryk.binpack.BinPackProblem;

import com.syncleus.dann.genetics.wavelets.AbstractOrganism;

import koryk.ga.Problem;
import koryk.gui.ProblemPanel;

public class WaveletBinProblemPanel extends ProblemPanel{

	private AbstractOrganism currentWinner = null;
	private WaveletBinFitnessFunction finalWinner = null;
	private WaveletBinPopulation population = null;
	
	private int generation = -1;
	public int getGeneration() {
		return generation;
	}
	public WaveletBinProblemPanel(Problem problem){
		super(problem);
	}
	@Override
	public void loadPanel() {
		// TODO Auto-generated method stub	
	}
	public void paintComponent(Graphics g){
		((BinPackProblem)problem).paint(g);
			
	}
	public ArrayList<Integer> getItemOrder(){
		return finalWinner.getItemsOrder();
	}
	@Override
	public void runGA() {
		// TODO Auto-generated method stub
		System.out.println(problem);
		population = new WaveletBinPopulation(.2, .4, .4, (BinPackProblem)problem);

		Random rng = ThreadLocalRandom.current();

		population.initializePopulation(50, (BinPackProblem)problem, rng);
		//try{Thread.sleep(10000);}catch(Exception e){;}
		int i=0, max = 50;
		double prevFull = 0;
		int convergeGeneration =0;
		WaveletBinFitnessFunction winner, oldWinner = null;
		try
		{
			for(; i < max; i++){
				population.nextGeneration();
				//System.out.println(population.getGenerations());
				currentWinner = population.getWinner();
				winner = (WaveletBinFitnessFunction)population.packageChromosome(currentWinner);
				winner.process();
				//System.out.println(winner);
				System.out.print(" " + population.getGenerations() + " ");
				if (prevFull < winner.percentFull){
					
					//System.out.println(winner);					
					oldWinner = winner;
					convergeGeneration = population.getGenerations();
					prevFull = winner.percentFull;
					System.out.println("Wooooo !!" + (prevFull = winner.percentFull) + " " + population.getGenerations() + " generation.");				
					if (prevFull == 1)
						break;
				}
			}//this.currentWinner = this.futureWinner.get();
			System.out.println("ga over");
			((BinPackProblem)problem).setSolution(oldWinner.getSolution());
			this.repaint();
			finalWinner = oldWinner;
			FileOutputStream os = new FileOutputStream(new File("gwa.txt"),true);
			os.write((problem.toString() + ": " + prevFull + " at " + convergeGeneration + " : ").getBytes());
			for (int j = 0 ; j < oldWinner.getSolution().length; j++){
				os.write(("\n[" + j + "]:").getBytes());
				int tmp = 0;
				for (Double d : oldWinner.getSolution()[j]){
					os.write((d + "-" + ((BinPackProblem)problem).getItems()[d.intValue()] + "*" + ((BinPackProblem)problem).getWeights()[d.intValue()] + "=" + ((BinPackProblem)problem).getItems()[d.intValue()]*((BinPackProblem)problem).getWeights()[d.intValue()] + " ").getBytes());
					tmp += ((BinPackProblem)problem).getItems()[d.intValue()]*((BinPackProblem)problem).getWeights()[d.intValue()];
				}
				os.write(("(" + tmp + "/"+ ((BinPackProblem)problem).getBins()[0]+")").getBytes());
			}
			os.write("\n\n".getBytes());
			os.close();
			generation = convergeGeneration;
			System.out.println(problem.toString() + ": " + prevFull + " at " + convergeGeneration);
		}		
		catch(Exception caught)
		{
			caught.printStackTrace();				
			throw new RuntimeException("Throwable was caught" + caught.getMessage(), caught);
		}
		catch(Error caught)
		{
			caught.printStackTrace();
			throw new Error("Throwable was caught" + caught.getMessage());
		}	
	}

}
