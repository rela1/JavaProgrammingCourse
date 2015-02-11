package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Implementacija operacije oduzimanja.
 * 
 * @author Ivan Relić
 *
 */
public class SubOperationImpl implements AccumulationOperation {

	/**
	 * Implementacija operacije oduzimanja. Uzima dva operanda i neovisno o
	 * zastavici inverted vraća njihovu razliku.
	 * 
	 * @return razlika dva operanda
	 */
	public double execute(double operand1, double operand2, boolean inverted) {
		return operand1 - operand2;
	}

}
