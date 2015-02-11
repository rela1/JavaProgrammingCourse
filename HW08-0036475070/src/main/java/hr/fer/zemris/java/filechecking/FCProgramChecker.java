package hr.fer.zemris.java.filechecking;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.filechecking.lexical.FCTokenizer;
import hr.fer.zemris.java.filechecking.lexical.FCTokenizerException;
import hr.fer.zemris.java.filechecking.syntax.FCParser;
import hr.fer.zemris.java.filechecking.syntax.FCParserException;

/**
 * Provjera sadrzava li ulazna datoteka koju pogresku, koju zatim dodaje u listu
 * pogresaka ovisno o tipu nastanka.
 * 
 * @author Ivan Relic
 * 
 */
public class FCProgramChecker {

	private List<String> errors;

	/**
	 * Konstruktor. Pokusava parsirati predani program, ako sadrzi pogreske,
	 * dodaje opis pogreske u listu pogresaka.
	 * 
	 * @param program
	 *            ulazni program
	 */
	public FCProgramChecker(String program) {
		if (program == null) {
			throw new IllegalArgumentException("Program should not be null!");
		}
		errors = new ArrayList<String>();
		// pokusaj parsirati, ako ne uspijes dohvati poruku iz exceptiona koji
		// dobijes od parsera
		try {
			new FCParser(new FCTokenizer(program));
		} catch (FCTokenizerException tokenizerException) {
			errors.add(tokenizerException.getMessage());
		} catch (FCParserException parserException) {
			errors.add(parserException.getMessage());
		}
	}

	/**
	 * Metoda vraca true ako ulazni program sadrzi sintakticke pogreske, a false
	 * inace.
	 * 
	 * @return true ili false
	 */
	public boolean hasErrors() {
		return (this.errors.size() == 0) ? false : true;
	}

	/**
	 * Metoda vraca listu sintaksnih pogresaka.
	 * 
	 * @return lista pogresaka
	 */
	public List<String> errors() {
		return this.errors;
	}
}
