package hr.fer.zemris.java.filechecking;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import hr.fer.zemris.java.filechecking.lexical.FCTokenizer;
import hr.fer.zemris.java.filechecking.syntax.FCParser;
import hr.fer.zemris.java.filechecking.syntax.nodes.FCNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.ProgramNode;
import hr.fer.zemris.java.filechecking.visitor.FCFileExecutorVisitor;

/**
 * Predstavlja razred koji provjerava ispravnost primljenog filea.
 * 
 * @author Ivan Relic
 * 
 */
public class FCFileVerifier {

	private File file;
	private String fileName;
	private Map<String, Object> initialData;
	private ProgramNode tree;
	private List<String> errors;

	/**
	 * Konstruktor. Preuzima datoteku koja se provjerava, njeno ime, ulazni
	 * program iz kojeg gradi parsersko stablo pri konstruiranju novog objekta
	 * ovog tipa, te inicijalnu mapu s podacima varijabli.
	 * 
	 * @param file
	 * @param fileName
	 * @param program
	 * @param intialData
	 */
	public FCFileVerifier(File file, String fileName, String program,
			Map<String, Object> inData) {
		if (file == null || fileName == null || program == null
				|| inData == null) {
			throw new IllegalArgumentException("Arguments should not be null!");
		}
		this.file = file;
		this.fileName = fileName;
		this.initialData = new HashMap<>(inData);
		tree = (new FCParser(new FCTokenizer(program))).getTree();
		verifyFile();
	}

	/**
	 * Izvrsava predani ulazni program i provjerava predani file i filename.
	 */
	private void verifyFile() {
		FCFileExecutorVisitor visitor;
		try {
			visitor = new FCFileExecutorVisitor(file, fileName, initialData);
		} catch (ZipException e) {
			throw new FCException("Error reading zip file!");
		} catch (IOException e) {
			throw new FCException("Error reading file!");
		}
		for (FCNode node : tree.getStatements()) {
			// provjeri je li izvrsivac terminiran
			if (visitor.isTerminated()) {
				break;
			}
			node.acceptVisitor(visitor);
		}
		errors = visitor.getMessages();
	}

	/**
	 * Vraca true ako je ispisana barem jedna poruka o pogresci, a false ako
	 * nema nijedne.
	 * 
	 * @return true ili false
	 */
	public boolean hasErrors() {
		return (errors.size() == 0) ? false : true;
	}

	/**
	 * Dohvaca skup pogresaka ulaznog programa.
	 * 
	 * @return lista pogresaka
	 */
	public List<String> errors() {
		return errors;
	}
}
