package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija metode tangens, odnosno arkus tangens, ovisno o zastavici koja
 * se prima u osnovnoj metodi implementacije funkcije.
 * 
 * @author Ivan Relic
 * 
 */
public class TangensFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda računa tangens vrijednost predane vrijednosti ako je zastavica
	 * inverted postavljena na false, a arkus tangens vrijednost ako je
	 * zastavica postavljena na true.
	 * 
	 * @param calcValue
	 *            vrijednost čija se funkcijska vrijednost izračunava
	 * @param inverted
	 *            traži li se tangens ili arkus tangens funkcija
	 * @return funkcijska vrijednost predane
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		if (!inverted && 2*calcValue == Math.PI) {
			throw new IllegalArgumentException(
					"Tangens function is not defined for this value!");
		}
		if (!inverted) {
			return Math.tan(calcValue);
		} else {
			return Math.atan(calcValue);
		}
	}

}
