package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Cvor koji sadrzi kompletan slijed ulaznog programa (sve naredbe ulaznog
 * programa su slijedno poredana kao djeca ovog cvora). Iz ovog razreda se
 * izvode svi cvorovi kojima je potrebna lista djece.
 * 
 * @author Ivan Relic
 * 
 */
public class ProgramNode extends FCNode {

	private List<FCNode> statements;

	/**
	 * Dohvat slijeda naredbi.
	 * 
	 * @return slijed naredbi
	 */
	public List<FCNode> getStatements() {
		if (statements == null) {
			return null;
		}
		return new ArrayList<>(statements);
	}

	/**
	 * Metoda dodaje "dijete", tj. dodaje clan svojoj listi djece.
	 * 
	 * @param statement
	 *            cvor koji program node dodaje kao dijete
	 */
	public void add(FCNode statement) {
		if (statements == null) {
			statements = new ArrayList<FCNode>();
		}
		statements.add(statement);
	}

	@Override
	public void acceptVisitor(FCNodeVisitor visitor) {
		// metoda nista ne radi jer nema potrebe na ovoj razini
	}
}
