package hr.fer.zemris.java.filechecking.syntax.nodes;

import hr.fer.zemris.java.filechecking.visitor.FCNodeVisitor;

/**
 * Opceniti, apstraktni razred za cvorove parserskog stabla ulaznog programa pri
 * cemu ce konkretne implementacije ovog razreda predstavljati naredbe ili
 * definiranja varijabli.
 * 
 * @author Ivan Relic
 * 
 */
public abstract class FCNode {

	/**
	 * Apstraktna metoda koju svaki razred izveden iz FCNode mora
	 * implementirati.
	 * 
	 * @param visitor
	 *            posjetitelj cvora
	 */
	public abstract void acceptVisitor(FCNodeVisitor visitor);

}
