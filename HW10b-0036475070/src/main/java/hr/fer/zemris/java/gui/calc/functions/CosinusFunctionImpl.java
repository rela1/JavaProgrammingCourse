package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija metode kosinus, odnosno arkus kosinus, ovisno o zastavici koja
 * se prima u osnovnoj metodi implementacije funkcije.
 * 
 * @author Ivan Relic
 * 
 */
public class CosinusFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa kosinus vrijednost predane vrijednosti ako je zastavica
	 * inverted postavljena na false, a arkus kosinus vrijednost ako je
	 * zastavica postavljena na true.
	 * 
	 * @param calcValue
	 *            vrijednost čija se funkcijska vrijednost izračunava
	 * @param inverted
	 *            traži li se kosinus ili arkus kosinus funkcija
	 * @return funkcijska vrijednost predane
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (inverted && Math.abs(calcValue) > 1) {
			throw new IllegalArgumentException(
					"Arcus cosinus function is not defined for this value!");
		}
		if (!inverted) {
			return Math.cos(calcValue);
		} else {
			return Math.acos(calcValue);
		}
	}

}
