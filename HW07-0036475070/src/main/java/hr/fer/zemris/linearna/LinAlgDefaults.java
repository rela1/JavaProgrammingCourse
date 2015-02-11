package hr.fer.zemris.linearna;

/**
 * Klasa definira defaultne instance za implementacije klasa IVector i IMatrix
 * 
 * @author Ivan Relic
 *
 */
public class LinAlgDefaults {

	/**
	 * Metoda vraca novi vektor zeljene dimenzije, promjenjiv, s vlastitom kopijom polja doubleova.
	 * 
	 * @param dimension velicina vektora
	 * @return novi vektor
	 */
	public static IVector defaultVector(int dimension) {
		return new Vector(new double[dimension]);
	}
	
	/**
	 * Metoda vraca novu matricu zeljene dimenzije, promjenjivu, s vlastitom kopijom polja 
	 * doubleova.
	 * 
	 * @param dimension velicina vektora
	 * @return novi vektor
	 */
	public static IMatrix defaultMatrix(int rows, int cols) {
		return new Matrix(rows, cols);
	}
}
