package bn.inference;
 import java.util.Random;
import java.util.Set;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.util.ArraySet;

public class GibbsSamplingInferencer implements Inferencer {

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		return gibbsAsk(X, e, network, 100);
	}

	private Distribution gibbsAsk(RandomVariable X, Assignment e, BayesianNetwork network, int numSamples) {
		Distribution Q_X = new bn.base.Distribution(X);
		double N[] = new double[X.getDomain().size()];
		Set<RandomVariable> Z = new ArraySet<RandomVariable>();
		double total = 0;
		
		for(RandomVariable Xi: network.getVariablesSortedTopologically()) {
			if(!e.containsKey(Xi))
				Z.add(Xi);
		}
		Assignment x = new bn.base.Assignment();
		x.putAll(e);
		
		for(RandomVariable z: Z) {
			Random rn = new Random();
			int rand = rn.nextInt(z.getDomain().size());
			int i = 0;
			Value randVal = new bn.base.Value(null);
			for(Value val: z.getDomain()) {
				if(i == rand) {
					randVal = val;
					break;
				}
				i++;
			}
			x.put(z, randVal);
		}
		
		
		for(int j=1; j<=numSamples; j++) {
			for(RandomVariable Zi: Z) {
				Assignment mbAssignment = new bn.base.Assignment();
				Set<RandomVariable> mbSet = network.getMarkovBlanket(Zi);
				for(RandomVariable var: x.keySet()) {
					if(mbSet.contains(var))
						mbAssignment.put(var, x.get(var));
				}
				x.put(Zi, getRandValAssignment(Zi, mbAssignment, network));
				
				int i=0;
				for(Value v: X.getDomain()) {
					if(v.equals(x.get(X)))
						N[i] += 1;
					i++;
				}
			}
			total++;
		}
		int i = 0;
		for(Value val: X.getDomain()) {
			Q_X.set(val, N[i++]/total);}
		
		Q_X.normalize();
		return Q_X;
	}
	
	
	private Value getRandValAssignment(RandomVariable Z, Assignment x, BayesianNetwork network) {
		double rand = Math.random();
		double sum = 0;
		Value randomVal = new bn.base.Value(null);
		for(Value val: Z.getDomain()) {
			x.put(Z, val);
			sum += network.getProbability(Z, x);
			if(rand < sum) {
				randomVal = val;
				return randomVal;
			}
			x.remove(Z);
		}
		return randomVal;
	}

}
