package hr.fer.zemris.java.scripting.demo;

import hr.fer.zemris.java.webserver.RequestContext;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;

/**
 * Container za main metodu.
 * 
 * @author Ivan Relić
 * 
 */
public class Demo2SmartScriptEngine {

	/**
	 * Main metoda otvara file sa skriptom koja se treba izvesi i izvodi je.
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		String documentBody = DemoMethods.readFromDisk(Paths
				.get("lib/zbrajanje.smscr"));
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}
}
