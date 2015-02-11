package hr.fer.zemris.java.gui.calc.functions;

/**
 * Sucelje koje mora implementirati svaka funkcija koju kalkulator podržava.
 * 
 * @author Ivan Relic
 * 
 */
public interface CalculatorFunction {

	/**
	 * Metoda koju svaka funkcija kalkulatora mora implementirati: kao argumente
	 * prima trenutnu vrijednost koja je "pohranjena" u kalkulatoru i podatak
	 * traži li se preslikavanje broja čistom ili inverznom funkcijom od
	 * navedene.
	 * 
	 * @param calcValue vrijednost čije se funkcijsko preslikavanje traži
	 * @param inverted preslikava li se čistom funkcijom ili inverzom
	 * @return funkcijska vrijednost 
	 */
	double executeFunction(double calcValue, boolean inverted);
}
