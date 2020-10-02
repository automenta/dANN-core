package koryk.binpack.gwa;

import com.syncleus.dann.genetics.wavelets.AbstractOrganism;

import java.util.Random;

public class WaveletBinIndividual extends AbstractOrganism{
	private final WaveletBinCell cell;
	public WaveletBinIndividual(int itemSize){
		cell = new WaveletBinCell(new WaveletBinNucleus(itemSize));
	}
	public WaveletBinIndividual(WaveletBinCell c){
		cell = c;
	}
	@Override
	public AbstractOrganism mate(AbstractOrganism partner) {
		if (partner instanceof WaveletBinIndividual)
			return new WaveletBinIndividual(((WaveletBinIndividual)partner).getCell().mate(cell));
		return null;
	}
	public WaveletBinCell getCell(){
		return cell;
	}

	public void tick(){
		cell.tick();
	}

	public void preTick(){
			cell.preTick();
	}

	@Override public void mutate(Random rng){
		cell.mutate(rng);
	}
}
