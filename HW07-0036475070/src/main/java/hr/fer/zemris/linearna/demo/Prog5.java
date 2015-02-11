package hr.fer.zemris.linearna.demo;

import java.util.Scanner;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

/**
 * Klasa je container za main metodu.
 * 
 * @author Ivan Relic
 *
 */
public class Prog5 {

	/**
	 * Main metoda demonstrira implementaciju i koristenje kreirane biblioteke za linearnu algebru.
	 * Konkretno, radi se izračun reflektiranog vektora oko nekog zadanog.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		
		System.out.print("Upisite 2D/3D vektor koji zelite reflektirati"
				+ "(vrijednosti koordinata odvojite razmakom): ");
		
		Scanner scanner = new Scanner(System.in, "UTF-8");
		
		IVector v = Vector.parseSimple(scanner.nextLine());
		
		if (v.getDimension() != 2 && v.getDimension() != 3) {
			System.out.println("Trebate upisati 2D ili 3D vektor!");
			scanner.close();
			System.exit(1);
		}
		
		System.out.print("Upisite " + ((v.getDimension() == 2) ? "2D" : "3D" )+ " vektor oko kojeg"
				+ " zelite reflektirati (vrijednosti koordinata odvojite razmakom): ");
		
		IVector n = Vector.parseSimple(scanner.nextLine());
		
		if (v.getDimension() != n.getDimension()) {
			System.out.println("Vektor koji se reflektira i vektor oko kojeg se reflektira moraju"
					+ " biti istih dimenzija (2D ili 3D)!");
			scanner.close();
			System.exit(1);
		}
		
		double scalar = v.scalarProduct(n) * 2 / Math.pow(n.norm(), 2);
		IVector r = n.nScalarMultiply(scalar).nSub(v);

		System.out.println("Originalni vektor: " + v.toString());
		System.out.println("Vektor oko kojeg se reflektira: " + n.toString());
		System.out.println("Reflektirani vektor: " + r.toString());
	}

}
