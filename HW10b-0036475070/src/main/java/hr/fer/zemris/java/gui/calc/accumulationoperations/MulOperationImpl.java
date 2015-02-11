package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Implementacija operacije množenja.
 * 
 * @author Ivan Relić
 * 
 */
public class MulOperationImpl implements AccumulationOperation {

	/**
	 * Implementacija operacije množenja. Uzima dva operanda i neovisno o
	 * zastavici inverted vraća njihov umnožak.
	 * 
	 * @return umnožak dva operanda
	 */
	public double execute(double operand1, double operand2, boolean inverted) {
		return operand1 * operand2;
	}

}