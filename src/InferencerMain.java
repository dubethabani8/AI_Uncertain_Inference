import bn.core.*;
import bn.base.*;
import bn.base.Assignment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.inference.*;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;;


public class InferencerMain {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		//Read in inputs
		String inferencer = args[0];
		int N;
		String fileName;
		String queryVar;
		int x;
		
		if(inferencer.equals("enumeration")) {
			fileName = args[1];
			queryVar = args[2];
			x = 3;
		}
		else {
			N = Integer.parseInt(args[1]);
			fileName = args[2];
			queryVar = args[3];
			x = 4;
		}
		
		HashMap<String, String> evidenceVars = new HashMap<String, String>();
		for(int i=x; i<args.length-1; i=i+2)
			evidenceVars.put(args[i], args[i+1]);
		
		//Get bif/xmls
		String fileType = fileName.substring(fileName.length()-3);
		
		BayesianNetwork network = null;
		//Parse accordingly
		if(fileType.equals("xml")) {
			XMLBIFParser parser = new XMLBIFParser();
			network = parser.readNetworkFromFile("src/bn/examples/"+fileName);
		}
		else if(fileType.equals("bif")) {
			BIFParser parser = new BIFParser(new FileInputStream("src/bn/examples/"+fileName));
			network = parser.parseNetwork();
		}
		
		//Query Var
		RandomVariable X = network.getVariableByName(queryVar);
		
		Assignment e = new bn.base.Assignment();
		
		for(String var: evidenceVars.keySet()) {
			e.put(network.getVariableByName(var), new StringValue(evidenceVars.get(var)));
		}
		
		Inferencer exact = null;
		String statement = "";
		switch(inferencer) {
		case "enumeration":
			exact = new EnumerationInferencer();
			statement = "Exact Inferencer using Enumeration: \n";
			break;
		case "rejection":
			exact = new RejectionSamplerInferencer();
			statement = "Approximate Inference using Rejection Sampling: \n";
			break;
		case "likelihood":
			//exact = new LikelihoodWeightingIferencer();
			statement = "Approximate Inference using Likelihood Weighting Inferencer: \n";
			break;
		case "gibbs":
			statement = "Approximate Inference using Gibbs Sampling: \n";
			exact = new GibbsSamplingInferencer();
			break;
		}
		
		
		Distribution dist = exact.query(X, e, network);
		System.out.println(statement);
		System.out.println(dist);
		
	}

}
