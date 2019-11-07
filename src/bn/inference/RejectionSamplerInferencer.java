package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class RejectionSamplerInferencer implements Inferencer {
	
	public RejectionSamplerInferencer() {
		
	}

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		// To generate 100 000 samples
		return rejectionSampling(X, e, network, 100000);
	}
	
	private Distribution rejectionSampling(RandomVariable X, Assignment e, BayesianNetwork network, double numSamples) {
		Distribution Q_X = new bn.base.Distribution(X);
		// N is variable of counts for each value of X, initially = 0
		double[] N = new double[X.getDomain().size()];
		int numOfConsistent = 0;
		
		for(int j=1; j<=numSamples; j++) {
			Assignment x = PriorSample(network);
			if(isConsistent(x,e)) {
				numOfConsistent++;
				int i=0;
				for(Value v: X.getDomain()) {
					if(v.equals(x.get(X)))
						N[i] += 1;
					i++;
				}
			}
			
		}
		int i = 0;
		for(Value val: X.getDomain()) {
			Q_X.set(val, N[i++]/numOfConsistent);}
		
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
		Assignment x = new bn.base.Assignment();
		for(RandomVariable Xi: network.getVariablesSortedTopologically()) {
			Value val = getRandVarAssignment(Xi, x, network);
			x.put(Xi, val);
		}
		return x;
	}
	
	private Value getRandVarAssignment(RandomVariable X, Assignment x, BayesianNetwork network) {
		double rand = Math.random();
		double sum = 0;
		Value randomVal = new bn.base.Value(null);
		for(Value val: X.getDomain()) {
			x.put(X, val);
			sum += network.getProbability(X, x);
			if(rand < sum) {
				randomVal = val;
				return randomVal;
			}
			x.remove(X);
		}
		return randomVal;
	}
}
