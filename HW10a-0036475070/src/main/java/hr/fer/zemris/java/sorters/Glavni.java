package hr.fer.zemris.java.sorters;

import java.util.Random;

/**
 * Klasa je samo container za main metodu.
 * 
 * @author Ivan Relić
 * 
 */
public class Glavni {

	/**
	 * Main metoda za sortoranje 100 milijuna elemenata.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final int SIZE = 100_000_000;
		Random rand = new Random();
		int[] data = new int[SIZE];
		for (int i = 0; i < data.length; i++) {
			data[i] = rand.nextInt();
		}
		long t0 = System.currentTimeMillis();
		QSortParallel2.sort(data);
		long t1 = System.currentTimeMillis();
		System.out.println("Sortirano: " + QSortParallel2.isSorted(data));
		System.out.println("Vrijeme: " + (t1 - t0) + " ms");
	}
}