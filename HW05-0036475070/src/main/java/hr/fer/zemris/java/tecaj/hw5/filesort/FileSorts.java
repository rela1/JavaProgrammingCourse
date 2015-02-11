package hr.fer.zemris.java.tecaj.hw5.filesort;

import java.io.File;
import java.util.Comparator;

/**
 * Klasa koja sadrzi static varijable implementacija svih nacina sortiranja fileova.
 * 
 * @author Ivan Relic
 *
 */
public class FileSorts {

	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_SIZE = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po velicini filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako je f1 manji od f2, 
		 * 0 ako su jednaki i pozitivna vrijednost ako je f1 veći od f2
		 */
		public int compare(File f1, File f2) {
			Long l1 = new Long(f1.length());
			Long l2 = new Long(f2.length());
			return l1.compareTo(l2); 
		}
		
	};
	
	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_NAME = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po imenu filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako je ime f1 leksikografski
		 * manje od imena f2, 0 ako su jednakih imena, pozitivna vrijednost ako je ime f1 
		 * leksikografski vece od imena f2
		 */
		public int compare(File f1, File f2) {
			return f1.getName().compareTo(f2.getName());
		}
		
	};
	
	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_LAST_MODIFIED = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po vremenu zadnjeg modificiranja filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako je ime f1 modificiran
		 * prije f2, 0 ako su jednakih vremena modifikacija, pozitivna vrijednost ako je f1 
		 * modificiran kasnije od f2
		 */
		public int compare(File f1, File f2) {
			Long m1 = new Long(f1.lastModified());
			Long m2 = new Long(f2.lastModified());
			return m1.compareTo(m2);
		}
		
	};
	
	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_TYPE = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po tipu filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako je f1 direktorij, a f2
		 * datoteka, 0 ako su oba filea istog tipa, pozitivna vrijednost ako je f1 datoteka, a
		 * f2 direktorij
		 */
		public int compare(File f1, File f2) {
			
			//ako je f1 direktorij, a f2 file, vrati -1
			if (f1.isDirectory() && f2.isFile()) {
				return -1;
			}
			
			//ako je f1 file, a f2 direktorij, vrati 1
			if (f1.isFile() && f2.isDirectory()) {
				return 1;
			}
			
			//inace, oba filea su istog tipa, vrati 0
			return 0;
		}
		
	};
	
	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_NAME_LENGTH = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po duljini imena filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako je duljina imena f1 
		 * manja od f2, 0 ako fileovi imaju duljine imena jednake, pozitivna ako f1 ima dulje
		 * ime od f2
		 */
		public int compare(File f1, File f2) {
			
			//dohvati duljine imena fileova u integer wrapper
			Integer l1 = new Integer(f1.getName().length());
			Integer l2 = new Integer(f2.getName().length());
			
			//vrati vrijednost njihove usporedbe
			return l1.compareTo(l2);
		}
		
	};
	
	/**
	 * Kreiranje static final instance anonimnog razreda koji implementira sucelje Comparator.
	 */
	public static final Comparator<File> BY_EXEC = new Comparator<File>() {

		/**
		 * Override metode za usporedjivanje po izvršivosti filea.
		 * 
		 * @param f1 prvi file za usporedjivanje
		 * @param f2 drugi file za usporedjivanje
		 * @return vrijednost usporedjivanja; negativna vrijednost ako se f1 moze izvrsiti, a
		 * f2 ne, 0 ako su oba filea iste izvrsivosti, pozitivna vrijednost ako se f1 ne moze,
		 * a f1 se moze izvrsiti
		 */
		public int compare(File f1, File f2) {
			
			//ako se f1 moze izvrsiti, a f2 ne, vrati -1
			if (f1.canExecute() && !f2.canExecute()) {
				return -1;
			}
			
			//ako se f1 ne moze izvrsiti, a f2 moze, vrati 1
			if (!f1.canExecute() && f2.canExecute()) {
				return 1;
			}
			
			//inace, oba filea su iste izvrsivosti, vrati 0
			return 0;
		}
		
	};
}
