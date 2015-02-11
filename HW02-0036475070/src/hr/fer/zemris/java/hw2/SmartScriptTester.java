package hr.fer.zemris.java.hw2;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;


/**Klasa za testiranje rada SmartScriptParsera.
 * 
 * @author Ivan Relic
 *
 */
public class SmartScriptTester {

	
	/**Main metoda koja predaje zadani tekst parseru, koji kreira parsersko stablo i vraca 
	 * originalni izgled predanog teksta.
	 * 
	 * @param args Argumenti command linea, ne koriste se.
	 */
	public static void main(String[] args) {
		
		String docBody = "This is sample text.\n"
				+ "{$ FOR i 1 10 1 $}\n"
				+ "  This is {$= i $}-th time this message is generated.\n"
				+ "{$END$}\n"
				+ "{$FOR i 0 10 2 $}\n"
				+ "  sin({$=i$})^2 = {$= i i * @sin \"0.000\"   @decfmt $}\n"
				+ "{$END$}";
		SmartScriptParser parser = null;
		
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like original
		// content of docBody
	}

	
	/**Stvara tekst koji je isparsiran u nodeu kojeg prima.
	 * 
	 * @param node Document node iz kojeg rekreira tekst.
	 * @return Rekreirani tekst.
	 */
	private static String createOriginalDocumentBody(Node node) {
		
		String pom = "";
		
		//ako trenutni dokument nema djece, ispisi ga, inace ispisi svu njegovu djecu
		if (node.numberOfChildren() == 0) {
			return "";
		}
		else {
			
			//prodji kroz svu djecu trenutne node i spremi njihove ispise u varijablu pom
			for (int i = 0, duljina = node.numberOfChildren(); i < duljina; i++) {
				pom = pom + node.getChild(i).toString() 
						+ createOriginalDocumentBody(node.getChild(i));
			}
			
			/*ako je trenutna noda koja je zavrsila s printanjem svoje djece ForLoopNode, na 
			kraj joj dodaj i END tag */
			if (node instanceof ForLoopNode) {
				pom = pom + "{$END$}";
			}
		}
		return pom;
	}
}
