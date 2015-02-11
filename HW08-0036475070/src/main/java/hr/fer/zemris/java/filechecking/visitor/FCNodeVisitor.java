package hr.fer.zemris.java.filechecking.visitor;

import hr.fer.zemris.java.filechecking.syntax.nodes.DefNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.ExistsNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FailNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FilenameNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FormatNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.MessageNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.PathNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.TerminateNode;

/**
 * Sucelje za posjetitelje cvorova parserskog stabla.
 * 
 * @author Ivan Relic
 *
 */
public interface FCNodeVisitor {
	
	/**
	 * Operacija posjecivanja DefNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(DefNode node);
	
	/**
	 * Operacija posjecivanja ExistsNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(ExistsNode node);
	
	/**
	 * Operacija posjecivanja FailNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(FailNode node);
	
	/**
	 * Operacija posjecivanja FilenameNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(FilenameNode node);
	
	/**
	 * Operacija posjecivanja FormatNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(FormatNode node);
	
	/**
	 * Operacija posjecivanja MessageNode cvora.
	 * 
	 * @param node cvor
	 * @return vraca poruku koju MessageNode sadrzi.
	 */
	public String visit(MessageNode node);
	
	/**
	 * Operacija posjecivanja PathNode cvora.
	 * 
	 * @param node cvor
	 * @return vraca path koji PathNode sadrzi
	 */
	public String visit(PathNode node);
	
	/**
	 * Operacija posjecivanja TerminateNode cvora.
	 * 
	 * @param node cvor
	 */
	public void visit(TerminateNode node);
}
