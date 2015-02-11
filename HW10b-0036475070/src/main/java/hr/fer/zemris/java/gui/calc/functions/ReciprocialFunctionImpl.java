package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija recipročne funkcije za kalkulator.
 * 
 * @author Ivan Relić
 * 
 */
public class ReciprocialFunctionImpl implements CalculatorFunction {

	/**
	 * Izračunava recipročnu vrijednost primljenog argumenta. Ako je primljena
	 * vrijednost 0, metoda baca exception.
	 * 
	 * @param calcValue
	 *            vrijednost broja čija se funkcija izračunava
	 * @param inverted
	 *            u slučaju ove funkcije je nevažno na što je inverted
	 *            postavljeno jer je i originalna i inverzna funkcija ista
	 * @return vrijednost funkcije
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (calcValue == 0) {
			throw new IllegalArgumentException(
					"Reciprocial function of 0 is not defined!");
		}
		return 1 / calcValue;
	}

}
