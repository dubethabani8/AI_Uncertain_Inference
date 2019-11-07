package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class LikelihoodWeightingInferencer implements Inferencer{
	
	protected class EventAndWeight{
		Assignment x;
		double w;
	}

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		return likelihoodWeighting(X, e, network, 100000);
	}
	
	private Distribution likelihoodWeighting (RandomVariable X, Assignment e, BayesianNetwork network, int numSample) {
		Distribution Q_X = new bn.base.Distribution(X);
		double[] W = new double[X.getDomain().size()];
		double sumWeight = 0;
		
		for(int j=1; j<=numSample; j++) {
			EventAndWeight xw = weightedSample(network, e);
			
			
			int i = 0;
			for(Value val: X.getDomain()) {
				if(val.equals(xw.x.get(X)))
					W[i] += xw.w;
				i++;
			}
			sumWeight += xw.w;
		}
		
		int i = 0;
		for(Value val: X.getDomain()) {
			Q_X.set(val, W[i++]/sumWeight);
		}
		
		Q_X.normalize();
		return Q_X;
		
	}
	
	private EventAndWeight weightedSample(BayesianNetwork network, Assignment e) {
		EventAndWeight xw = new EventAndWeight();
		Assignment x = new bn.base.Assignment();
		x.putAll(e);
		double w = 1;
		for(RandomVariable Xi: network.getVariablesSortedTopologically()) {
			if(e.containsKey(Xi)) {
				x.put(Xi, x.get(Xi));
				w = w * network.getProbability(Xi, x);
			}
			else x.put(Xi, getRandVarAssignment(Xi, x, network));
		}
		xw.x = x;
		xw.w = w;
		return xw;
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
