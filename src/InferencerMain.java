import bn.core.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.core.BayesianNetwork;
import bn.inference.*;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;;


public class InferencerMain {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		//Read in inputs
		String inferencer = args[0];
		String fileName = args[1];
		String queryVar = args[2];
		
		HashMap<String, Boolean> evidenceVars = new HashMap<String, Boolean>();
		for(int i=3; i<args.length; i=i+2) {
			String key = args[i];
			String val = args[i+1];
			if(val.equals("true"))
				evidenceVars.put(key, true);
			else evidenceVars.put(key, false);
		}
		
		//Get bif/xml
		String fileType = fileName.substring(args.length-3);
		
		BayesianNetwork bn = null;
		//Parse accordingly
		if(fileType.equals("xml")) {
			XMLBIFParser parser = new XMLBIFParser();
			bn = parser.readNetworkFromFile(fileName);
		}
		else if(fileType.equals("bif")) {
			BIFParser parser = new BIFParser(new FileInputStream(fileName));
			bn = parser.parseNetwork();
		}
	}

}
