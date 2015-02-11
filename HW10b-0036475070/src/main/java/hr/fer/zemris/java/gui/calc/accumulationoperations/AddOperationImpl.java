package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Implementacija operacije zbrajanja.
 * 
 * @author Ivan Relić
 * 
 */
public class AddOperationImpl implements AccumulationOperation {

	/**
	 * Implementacija operacije zbrajanja. Uzima dva operanda i neovisno o
	 * zastavici inverted vraća njihov zbroj.
	 * 
	 * @return zbroj dva operanda
	 */
	public double execute(double operand1, double operand2, boolean inverted) {
		return operand1 + operand2;
	}

}
