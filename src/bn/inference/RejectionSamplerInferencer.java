package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class RejectionSamplerInferencer implements Inferencer {

	public static void main(String[] args) {
		
	}

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		// To generate 1000 samples
		return rejectionSampling(X, e, network, 1000);
	}
	
	public Distribution rejectionSampling(RandomVariable X, Assignment e, BayesianNetwork network, double numSamples) {
		Distribution Q_X = new bn.base.Distribution(X);
		// N is variable of counts for each value of X, initially =0
		double[] N = new double[X.getDomain().size()];
		
		for(int j=1; j<numSamples; j++) {
			//generate sample using Prior Sample
			Assignment x = PriorSample(network);
			
			
			if(isConsistent(x,e)) {
				int i=0;
				for(Value v: X.getDomain()) {
					if(v.equals(x.get(X))) {
						N[i]=N[i]+1;
						i++;
					}
				}
			}
			//if x consistent with e, then
			
			//N[x] = N[x]+1
			
		}
				
		Q_X.normalize();
		return Q_X;
		
	}
	
	//checking for consistency
	private boolean isConsistent(Assignment x, Assignment e) {
		for(RandomVariable var : e.keySet()) {
			for(RandomVariable xi : x.keySet()) {
				if(var.equals(xi)) {
					if(!(e.get(var).equals(x.get(xi)))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public Assignment PriorSample(BayesianNetwork network) {
		
		
		return null;
		
	}
}
