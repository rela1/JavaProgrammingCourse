package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija metode kotangens, odnosno arkus kotangens, ovisno o zastavici
 * koja se prima u osnovnoj metodi implementacije funkcije.
 * 
 * @author Ivan Relic
 * 
 */
public class CotangensFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa kotangens vrijednost predane vrijednosti ako je zastavica
	 * inverted postavljena na false, a arkus kotangens vrijednost ako je
	 * zastavica postavljena na true.
	 * 
	 * @param calcValue
	 *            vrijednost čija se funkcijska vrijednost izračunava
	 * @param inverted
	 *            traži li se kotangens ili arkus kotangens funkcija
	 * @return funkcijska vrijednost predane
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (Math.tan(calcValue) == 0) {
			throw new IllegalArgumentException(
					"Cotangens function is not defined for this value!");
		}
		if (!inverted) {
			return 1 / Math.tan(calcValue);
		} else {
			return (Math.PI / 2) - Math.atan(calcValue);
		}
	}
}
