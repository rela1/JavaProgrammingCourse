package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Implementacija operacije dijeljenja.
 * 
 * @author Ivan Relić
 * 
 */
public class DivOperationImpl implements AccumulationOperation {

	/**
	 * Implementacija operacije dijeljenja. Uzima dva operanda i neovisno o
	 * zastavici inverted vraća njihov kvocijent.
	 * 
	 * @return kvocijent dva operanda
	 */
	public double execute(double operand1, double operand2, boolean inverted) {
		if (operand2 == 0) {
			throw new IllegalArgumentException("Cannot divide with 0!");
		}
		return operand1 / operand2;
	}

}