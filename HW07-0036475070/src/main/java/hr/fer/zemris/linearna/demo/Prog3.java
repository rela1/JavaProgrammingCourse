package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

/**
 * Klasa je container za main metodu.
 * 
 * @author Ivan Relic
 *
 */
public class Prog3 {

	/**
	 * Main metoda demonstrira implementaciju i koristenje kreirane biblioteke za linearnu algebru.
	 * Konkretno, rjesava se sustav jednadzbi.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		
		IMatrix a = Matrix.parseSimple("3 5 | 2 10");
		IMatrix r = Matrix.parseSimple("2 | 8");
		IMatrix v = a.nInvert().nMultiply(r);
		System.out.println("Rjesenje sustava je: ");
		System.out.println(v);
	}

}
