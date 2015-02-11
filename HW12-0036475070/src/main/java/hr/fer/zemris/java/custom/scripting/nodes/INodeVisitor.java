package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Sučelje posjetioca čvora parsiranog stabla.
 * 
 * @author Ivan Relić
 * 
 */
public interface INodeVisitor {

	/**
	 * Operacije koje treba izvršiti pri posjeti text nodeu.
	 * 
	 * @param node
	 *            node koji se posjećuje
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Operacije koje treba izvršiti pri posjeti for loop nodeu.
	 * 
	 * @param node
	 *            node koji se posjećuje
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Operacije koje treba izvršiti pri posjeti echo nodeu.
	 * 
	 * @param node
	 *            node koji se posjećuje
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Operacije koje treba izvršiti pri posjeti document nodeu.
	 * 
	 * @param node
	 *            node koji se posjećuje
	 */
	public void visitDocumentNode(DocumentNode node);

}
