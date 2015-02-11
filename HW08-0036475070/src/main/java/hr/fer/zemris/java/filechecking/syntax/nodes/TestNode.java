package hr.fer.zemris.java.filechecking.syntax.nodes;

/**
 * Vrsni razred za cvorove koji provode testove.
 * 
 * @author Ivan Relic
 * 
 */
public class TestNode extends ProgramNode {

	private boolean inverted;
	private MessageNode errorMessage;

	/**
	 * Konstruktor. Prima informaciju je li test invertiran te koju poruku treba
	 * ispisati ukoliko test ne uspije.
	 * 
	 * @param inverted
	 * @param path
	 * @param message
	 */
	public TestNode(boolean inverted, MessageNode errorMessage) {
		super();
		this.inverted = inverted;
		this.errorMessage = errorMessage;
	}

	/**
	 * Vraca informaciju predstavlja li test invertirani ili ne.
	 * 
	 * @return true ako je invertiran, false inace
	 */
	public boolean isInverted() {
		return inverted;
	}

	/**
	 * Vraca message node koji se treba ispisati ako je test neuspjesan.
	 * 
	 * @return message node
	 */
	public MessageNode getErrorMessage() {
		return errorMessage;
	}

}
