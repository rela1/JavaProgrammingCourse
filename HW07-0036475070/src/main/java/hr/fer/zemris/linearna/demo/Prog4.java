package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

/**
 * Klasa je container za main metodu.
 * 
 * @author Ivan Relic
 *
 */
public class Prog4 {

	/**
	 * Main metoda demonstrira implementaciju i koristenje kreirane biblioteke za linearnu algebru.
	 * Konkretno, racunaju se baricentricne koordinate, ali na drugi nacin.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		
		IMatrix a = Matrix.parseSimple("1 5 3 | 0 0 8 | 1 1 1");
		IMatrix r = Matrix.parseSimple("3 | 4 | 1");
		IMatrix v = a.nInvert().nMultiply(r);
		System.out.println("Rjesenje sustava je: ");
		System.out.println(v);
	}

}
