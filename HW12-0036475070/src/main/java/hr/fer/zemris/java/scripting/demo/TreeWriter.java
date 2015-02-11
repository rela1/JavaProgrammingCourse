package hr.fer.zemris.java.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Razred je container za main metodu.
 * 
 * @author Ivan Relić
 * 
 */
public class TreeWriter {

	/**
	 * Prima path do filea koji treba parsirati, parsira ga te pomoću visitor
	 * obrasca rekreira originalni tekst iz parserskog stabla.
	 * 
	 * @param args
	 *            path do filea koji treba parsirati
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out
					.println("You should provide path to file you want to parse!");
			System.exit(0);
		}

		// inace, procitaj file u string
		Path filePath = Paths.get(args[0]);
		if (!filePath.toFile().exists()) {
			System.out.println("Path you provided doesn't exist!");
			System.exit(0);
		}
		String document = DemoMethods.readFromDisk(filePath);
		SmartScriptParser p = new SmartScriptParser(document);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

	/**
	 * Visitor čvorova koji ispisuje, tj. rekreira originalni tekst iz
	 * parserskog stabla.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node.toString());
			for (int i = 0, length = node.numberOfChildren(); i < length; i++) {
				node.getChild(i).accept(this);
			}
			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, length = node.numberOfChildren(); i < length; i++) {
				node.getChild(i).accept(this);
			}
		}

	}
}
