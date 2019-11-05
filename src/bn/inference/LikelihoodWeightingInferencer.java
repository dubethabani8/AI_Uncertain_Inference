package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;

public class LikelihoodWeightingInferencer implements Inferencer{

	public static void main(String[] args) {

	}

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		return likelihoodWeighting(X, e, network, 1000);
	}
	
	public Distribution likelihoodWeighting (RandomVariable X, Assignment e, BayesianNetwork bn, int numSample) {
		Distribution Q_X = new bn.base.Distribution(X);
		double[] W = new double[X.getDomain().size()];
		
		for(int j=1; j<=numSample; j++) {
			Assignment x = WeightedSample(network);

		}
		
		return null;
		
	}
	
	
	
	

}
