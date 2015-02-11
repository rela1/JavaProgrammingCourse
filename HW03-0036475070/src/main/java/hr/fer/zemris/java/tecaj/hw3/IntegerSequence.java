package hr.fer.zemris.java.tecaj.hw3;

import java.util.Iterator;


/**Klasa za generiranje iterable objekata koji se mogu vrtiti u foreach petlji.
 * 
 * @author Ivan Relic
 *
 */
public class IntegerSequence implements Iterable<Integer> {
	
	//privatne clanske varijable
	private int startIndex;
	private int endIndex;
	private int step;

	
	/**Konstruktor metoda postavlja clanske varijable na primljene i poziva metodu iterator.
	 * 
	 * @param startIndex Pocetni indeks.
	 * @param endIndex Zavrsni indeks.
	 * @param step Korak iteracije.
	 */
	public IntegerSequence(int startIndex, int endIndex, int step) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.step = step;
		iterator();
	}
	
	
	/**Metoda poziva IntegerIterator konstruktor ako su primljeni argumenti u redu.
	 * 
	 */
	public Iterator<Integer> iterator() {

		//ako je pocetni veci od zavrsnog, a step je pozitivan, baci exception
		if (startIndex > endIndex && step > 0) {
			throw new IllegalArgumentException("Bad step!");
		}
		
		//ako je pocetni manji od zavrsnog, a step je negativan, baci exception
		else if (startIndex < endIndex && step < 0) {
			throw new IllegalArgumentException("Bad step!");
		}
		
		//ako je step 0, baci exception
		else if (step == 0) {
			throw new IllegalArgumentException("Bad step!");
		}
		
		return new IntegerIterator();
	}
	
	
	/**Privatni interclass koji implementira sucelje iterator, u kojem je potrebno overrideati
	 * metode hasNext i next da bi objekt bio iterable kroz foreach petlje.
	 * 
	 * @author Ivan Relic
	 *
	 */
	private class IntegerIterator implements Iterator<Integer> {

		private int currentNumber;
		
		
		public IntegerIterator() {
			
			//smanji za step da uhvatis i pocetni index
			currentNumber = startIndex - step;
		}
		
		/**Metoda provjerava ima li iterator sljedeci element.
		 * 
		 * @return Vraca true ili false, ovisno o provjeri.
		 */ 
		public boolean hasNext() {
			
			/*ako je startIndex < endIndex, provjeri je li trenutni manji od zavrsnog
			provjera je s endIndex - step + 1, jer sljedeca iteracija ne smije prijeci kraj*/
			if (currentNumber < (endIndex - step + 1) && startIndex <= endIndex) {
				return true;
			}
			
			/*ako je startIndex veci od endIndex, provjeri je li trenutni veci od zavrsnog
			provjera je s endIndex - step - 1, jer sljedeca iteracija ne smije prijeci kraj*/
			if (currentNumber > (endIndex - step - 1) && startIndex > endIndex) {
				return true;
			}
			else {
				return false;
			}
		}

		
		/**Metoda vraca sljedeci element iteracije.
		 * 
		 * @return Integer broj.
		 */
		public Integer next() {
			
			//dodaj step na trenutni broj i vrati ga
			currentNumber += step;
			
			return currentNumber;
		}

		
		/**Metoda remove se ne podrzava.
		 * 
		 */
		public void remove() {
			throw new UnsupportedOperationException("Operation remove is not supported!");
			
		}
		
	}

}
