package hr.fer.zemris.java.gui.calc.functions;

/**
 * Implementacija metode koja neovisno o inverted zastavici pretvara trenutni
 * broj u negirani broj.
 * 
 * @author Ivan Relic
 * 
 */
public class NegateNumberFunctionImpl implements CalculatorFunction {

	/**
	 * Metoda prima trenutni broj i vraća negativnu vrijednost njega samoga.
	 * 
	 * @param calcValue
	 *            vrijednost broja
	 * @param inverted
	 *            ne koristi se - svejedno je koja vrijednost se preda
	 * @return negirana vrijednost broja
	 * 
	 */
	public double executeFunction(double calcValue, boolean inverted) {
		return calcValue * (-1);
	}

}
