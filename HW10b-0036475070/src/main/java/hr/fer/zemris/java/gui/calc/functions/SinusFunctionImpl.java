package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija metode sinus, odnosno arkus sinus, ovisno o zastavici koja se
 * prima u osnovnoj metodi implementacije funkcije.
 * 
 * @author Ivan Relic
 * 
 */
public class SinusFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa sinus vrijednost predane vrijednosti ako je zastavica
	 * inverted postavljena na false, a arkus sinus vrijednost ako je zastavica
	 * postavljena na true.
	 * 
	 * @param calcValue
	 *            vrijednost čija se funkcijska vrijednost izračunava
	 * @param inverted
	 *            traži li se sinus ili arkus sinus funkcija
	 * @return funkcijska vrijednost predane
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (inverted && Math.abs(calcValue) > 1) {
			throw new IllegalArgumentException(
					"Arcus sinus function is not defined for this value!");
		}
		if (!inverted) {
			return Math.sin(calcValue);
		} else {
			return Math.asin(calcValue);
		}
	}

}
