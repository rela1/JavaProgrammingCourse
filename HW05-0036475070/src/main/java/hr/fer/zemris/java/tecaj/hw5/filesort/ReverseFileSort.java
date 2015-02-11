package hr.fer.zemris.java.tecaj.hw5.filesort;

import java.io.File;
import java.util.Comparator;

	/**
	 * Klasa koja omogucava reverzno sortiranje od onog koje mu je predano kao
	 * originalno.
	 * 
	 * @author Ivan Relic
	 *
	 */
	public class ReverseFileSort implements Comparator<File> {
		
		//privatna clanska varijabla koja cuva originalni komparator
		private Comparator<File> original;
		
		/**
		 * Javni konstruktor pri kreiranju samo pohranjuje originalni komparator tako da 
		 * metodi compare koju treba implementirati bude dostupan na koristenje.
		 * 
		 * @param original originalni komparator od kojega zelimo reverzno sortiranje
		 */
		public ReverseFileSort(Comparator<File> original) {
			this.original = original;
		}
		
		/**
		 * Metoda vraca negativnu vrijednost od one primljene od originala jer zelimo totalni
		 * suprotni poredak.
		 * 
		 * @return vrijednost usporedbe obratna od originala
		 */
		public int compare(File f1, File f2) {
			return -this.original.compare(f1, f2);
		}
	}