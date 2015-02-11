package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija logaritamske funkcije po bazi "e", odnosno u slučaju inverzne
 * funkcije, potenciranja baze "e" na predanu vrijednost.
 * 
 * @author Ivan Relić
 * 
 */
public class LogarithmBaseEFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa logaritam po bazi "e" od predanog broja u slučaju da je
	 * zastavica inverted postavljena na false, a u slučaju da je zastavica na
	 * true, računa potenciju broja "e" na predanu vrijednost.
	 * 
	 * @param calcValue
	 *            vrijednost koju preslikavamo funkcijski
	 * @param inverted
	 *            tražimo li običnu ili invertiranu funkciju
	 * @return vrijednost funkcije
	 * 
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (!inverted && calcValue <= 0) {
			throw new IllegalArgumentException(
					"Logarithm by base \"e\" is not defined for value 0 or less than zero!");
		}
		if (!inverted) {
			return Math.log(calcValue);
		} else {
			return Math.pow(Math.E, calcValue);
		}
	}

}
