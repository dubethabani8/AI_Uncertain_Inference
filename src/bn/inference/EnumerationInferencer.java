package bn.inference;



import java.util.ArrayList;
import java.util.List;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class EnumerationInferencer implements Inferencer {
	
	public EnumerationInferencer() {
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		return enumerationAsk(X, e, network);
	}
	
	public Distribution enumerationAsk(RandomVariable X, Assignment e, BayesianNetwork network) {
		Distribution Q_X = new bn.base.Distribution(X);
		for(Value xi: X.getDomain()) {
			e.put(X, xi);
			Q_X.set(xi, enumerateAll(network.getVariablesSortedTopologically(), e, network));
			e.remove(X);
		}
		Q_X.normalize();
		return Q_X;
	}
	
	public double enumerateAll(List<RandomVariable> vars, Assignment e, BayesianNetwork network) {
		if(vars.size() == 0) return 1.0;
		RandomVariable Y = vars.get(0);
		vars.remove(0);
		if(e.containsKey(Y))
			return network.getProbability(Y, e) * enumerateAll(new ArrayList<>(vars), e, network);
		else {
			double sum = 0;
			for(Value y: Y.getDomain()) {
				e.put(Y, y);
				sum += network.getProbability(Y, e) * enumerateAll(new ArrayList<>(vars), e, network);
				e.remove(Y);
			}
			return sum;
		}
	}
}
