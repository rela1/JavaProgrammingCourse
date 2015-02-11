package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Implementacija operacije potenciranja, ili u slučaju invertiranosti,
 * korjenovanja.
 * 
 * @author Ivan Relić
 * 
 */
public class PowOperationImpl implements AccumulationOperation {

	/**
	 * Implementacija operacije potenciranja, odnosno korjenovanja. Uzima dva
	 * operanda i ovisno o zastavici inverted vraća njihovu potenciranu
	 * vrijednost ili korijen.
	 * 
	 * @return korijen ili potencija dva operanda
	 */
	public double execute(double operand1, double operand2, boolean inverted) {
		if (inverted && operand2 < 0) {
			throw new IllegalArgumentException(
					"n-th square from negative number is not defined!");
		}
		if (!inverted) {
			return Math.pow(operand1, operand2);
		} else {
			return Math.pow(operand1, 1 / operand2);
		}
	}

}