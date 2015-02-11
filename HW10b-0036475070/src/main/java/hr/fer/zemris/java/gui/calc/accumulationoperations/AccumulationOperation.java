package hr.fer.zemris.java.gui.calc.accumulationoperations;

/**
 * Sučelje koje moraju zadovoljiti sve operacije koje djeluju kroz više
 * učitavanja operanada. (+, -, /, *, x^n, n-ti korijen iz x)
 * 
 * @author Ivan Relić
 * 
 */
public interface AccumulationOperation {

	/**
	 * Metoda prima dvije double vrijednosti i obavlja operaciju među njima.
	 * Konkretna operacija koja se obavlja je definirana u konkretnim
	 * implementacijama ovog sučelja. Zastavica inverted također za neke
	 * implementacije daje mogućnost invertiranih funkcija. (x^n i n-ti korijen
	 * iz x).
	 * 
	 * @param operand1
	 *            prvi operand operacije
	 * @param operand2
	 *            drugi operand operacije
	 * @param inverted
	 *            zastavica je li funkcija invertirana ili nije
	 * @return vrijednost rezultata operacije
	 */
	public double execute(double operand1, double operand2, boolean inverted);
}
