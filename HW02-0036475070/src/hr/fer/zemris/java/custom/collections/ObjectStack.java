package hr.fer.zemris.java.custom.collections;


/**Klasa za kolekciju objekata zasnovanoj na strukturi stoga.
 * 
 * @author Ivan Relic
 *
 */
public class ObjectStack {
	
	//privatna clanska kolekcija koja se koristi za implementaciju stoga
	private ArrayBackedIndexedCollection stack = new ArrayBackedIndexedCollection();

	
	/**Metoda provjerava je li stog prazan.
	 * 
	 * @return True ako je stog prazan, false ako nije.
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	
	/**Metoda vraca broj elemenata na stogu.
	 * 
	 * @return Broj elemenata na stogu
	 */
	public int size() {
		return stack.size();
	}


	/**Stavlja objekt na stog.
	 * 
	 * @param value Objekt koji stavljamo na stog, mora biti razlicit od null.
	 * 
	 */
	public void push(Object value) {
		
			/*pushamo objekt na stog, tj. niz koristene klase, u metodi je vec implementiran
			 throw exceptiona za dodavanje null vrijednosti */
			stack.add(value);	
	}


	/**Uzima element sa vrha stoga, vraca ga i brise sa stoga.
	 * 
	 * @return Vraca element uzet s vrha stoga.
	 */
	public Object pop() {
		
		//pomocna varijabla za spremanje podatka s vrha stoga
		Object podatak;
		
		//ako je stog prazan, baci iznimku
		if (stack.size() == 0) {
			throw new EmptyStackException("Stog je prazan!");
		}
		
		//inace, uzmi element s vrha stoga, i makni ga (indeks mu je velicina - 1)
		else {
			podatak = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);
		}
		return podatak;
	}


	/**Uzima element sa vrha stoga i vraca ga, ali ga ne brise.
	 * 
	 * @return Vraca objekt koji je na vrhu stoga.
	 */ 
	public Object peek() {
		
		//pomocna varijabla za spremanje podatka s vrha stoga
		Object podatak;
		
		//ako je stog prazan, baci iznimku
		if (stack.size() == 0) {
			throw new EmptyStackException("Stog je prazan!");
		}
		
		//inace, uzmi element s vrha stoga, ali ga ne brisi (indeks mu je velicina - 1)
		else {
			podatak = stack.get(stack.size()-1);
		}
		return podatak;
	}	


	/**Funkcija prazni stog.
	 * 
	 */
	public void clear() {
		stack.clear();
	}

}
