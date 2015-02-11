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
public class Demo3SmartScriptEngine {

	/**
	 * Main metoda otvara file sa skriptom koja se treba izvesi i izvodi je.
	 * 
	 * @param args
	 *            ne koriste se
	 */
	public static void main(String[] args) {

		String documentBody = DemoMethods.readFromDisk(Paths
				.get("lib/brojPoziva.smscr"));
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters,
				persistentParameters, cookies);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc)
				.execute();
		System.out.println("Vrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));
	}
}
