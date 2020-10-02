package koryk.binpack.sga;

import java.util.HashSet;
import java.util.Set;

import koryk.binpack.BinPackProblem;

import com.syncleus.dann.genetics.AbstractGeneticAlgorithmFitnessFunction;
import com.syncleus.dann.genetics.AbstractGeneticAlgorithmPopulation;
import com.syncleus.dann.genetics.GeneticAlgorithmChromosome;

import org.apache.log4j.Logger;

public class SimpleBinPopulation extends AbstractGeneticAlgorithmPopulation{
	private final static Logger LOGGER = Logger.getLogger(SimpleBinPopulation.class);
	private BinPackProblem problem;
	double mut;
	public SimpleBinPopulation(double mutationDeviation,
			double crossoverPercentage, double dieOffPercentage, BinPackProblem problem) {
		super(mutationDeviation, crossoverPercentage, dieOffPercentage);
		this.mut = mutationDeviation;
		this.problem = problem;
		
	}

	@Override
	protected AbstractGeneticAlgorithmFitnessFunction packageChromosome(
			GeneticAlgorithmChromosome chrom) {
		// TODO Auto-generated method stub
		return new SimpleBinFitnessFunction((SimpleBinChromosome)chrom, problem.getBins(), problem.getItems(), problem.getWeights());
	}
	private static Set<GeneticAlgorithmChromosome> initialChromosomes(final int populationSize, int itemSize)
	{
		final HashSet<GeneticAlgorithmChromosome> returnValue = new HashSet<GeneticAlgorithmChromosome>();
		for (int i = 0; i < populationSize;i++)
			returnValue.add(new SimpleBinChromosome(itemSize));

			
		return returnValue;
	}
	public void initializePopulation(final int populationSize)
	{
		
		if(populationSize < 4)
			throw new IllegalArgumentException("populationSize must be at least 4");
		Set<GeneticAlgorithmChromosome> chroms  = initialChromosomes(populationSize, problem.getItems().length);
		for (int i = 0; i < 3; i++)
			for (GeneticAlgorithmChromosome c : chroms)
				c.mutate(mut);
		this.addAll(chroms);
	
	}

	public final SimpleBinChromosome getWinner()
	{
		GeneticAlgorithmChromosome winner = super.getWinner();
		assert(winner instanceof SimpleBinChromosome);
		return (SimpleBinChromosome) winner;
	}
	
}
