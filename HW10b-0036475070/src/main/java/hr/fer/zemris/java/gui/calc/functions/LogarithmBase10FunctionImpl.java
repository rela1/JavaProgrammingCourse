package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija logaritamske funkcije po bazi 10, odnosno u slučaju inverzne
 * funkcije, potenciranja baze 10 na predanu vrijednost.
 * 
 * @author Ivan Relić
 * 
 */
public class LogarithmBase10FunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa logaritam po bazi 10 od predanog broja u slučaju da je
	 * zastavica inverted postavljena na false, a u slučaju da je zastavica na
	 * true, računa potenciju broja 10 na predanu vrijednost.
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
					"Logarithm by base 10 is not defined for value 0 or less than zero!");
		}
		if (!inverted) {
			return Math.log10(calcValue);
		} else {
			return Math.pow(10, calcValue);
		}
	}
}
