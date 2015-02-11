package hr.fer.zemris.java.custom.collections;

/**Klasa za kolekciju objekata zasnovanoj na nizu promjenjive velicine, null vrijednosti
 *  kao objekti nisu dozvoljeni.
 * @author Ivan Relic
 *
 */
public class ArrayBackedIndexedCollection {
	
	/*clanske varijable klase, size je trenutna velicina kolekcije, capacity je trenutni
	kapacitet, a elements je niz objekata koji su u kolekciji */
	private int size;
	private int capacity;
	private Object[] elements;
	
	//pocetni kapacitet za osnovni konstruktor
	private final int defaultCapacity = 16;
	
	
	/**Konstruktor bez argumenata - kapacitet postavlja na 16 i stvara niz objekata 
	 * iste velicine.
	 */
	public ArrayBackedIndexedCollection() {
		capacity = defaultCapacity;
		elements = new Object[defaultCapacity];
	}
	
	
	/**Konstruktor s jednim arugmentom - kapacitet postavlja na zeljeni i stvara niz objekata
	 * zeljene velicine.
	 * 
	 * @param initialCapacity Zeljeni kapacitet kolekcije.
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		else {
			capacity = initialCapacity;
			elements = new Object[initialCapacity];
		}
	}
	
	
	/**Funkcija koja provjerava je li kolekcija prazna ili nije.
	 * 
	 * @return Vraca true ako je kolekcija prazna, false ako nije.
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**Funkcija koja vraca trenutni broj objekata koji se nalaze u kolekciji.
	 * 
	 * @return Vraca trenutni broj objekata u kolekciji.
	 */
	public int size() {
		return size;
	}
	
	
	/**Funkcija koja dodaje objekt u kolekciju.
	 * 
	 * @param value Dozvoljeni su svi objekti osim null reference.
	 */
	public void add(Object value) {
		
		//ako je objekt null, baci iznimku
		if (value == null) {
			throw new IllegalArgumentException("Nije dozvoljena null vrijednost!");
		}
		
		
		/*ako ima praznih mjesta u kolekciji, dodaj element unutra i povecaj broj elemenata
		u kolekciji */
		else if (capacity > size) {
			elements[size] = value;
			size++;
		}
		
		//ako nema mjesta, realociraj velicinu polja elemenata na duplu velicinu i dodaj
		else {
			
			//realociraj velicinu polja
			this.reallocate();
			
			//dodaj objekt u polje elemenata i povecaj broj elemenata u kolekciji
			elements[size] = value;
			size++;
		}
	}
	
	
	/**Funkcija vraca objekt s trazenog indeksa polja elemenata kolekcije.
	 * 
	 * @param index Indeks objekta koji zelimo dobiti, u intervalu [0, size-1].
	 * @return Objekt s trazenog indeksa.
	 */
	public Object get(int index) {
		
		/*ako trazeni indeks nije unutar intervala trenutno pohranjenih objekata
		ili je kolekcija prazna, baci iznimku */
		if (index<0 || index>size-1 || size == 0) {
			throw new IndexOutOfBoundsException("Ne postoji objekt na trazenom indeksu!");
		}
		
		//inace vrati objekt s trazenog indeksa
		else {
			return elements[index];
		}
	}
	
	
	/**Funkcija uklanja objekt s trazenog indeksa i pomice elemente desno od njega, ako
	 * postoje, da popuni rupu.
	 * 
	 * @param index Indeks liste elemenata s kojega zelimo ukloniti objekt.
	 */
	public void remove (int index) {
		
		//ako trazeni indeks nije unutar intervala trenutno pohranjenih objekata, baci iznimku
		if (index<0 || index>size-1) {
			throw new IndexOutOfBoundsException("Ne postoji objekt na trazenom indeksu!");
		}
		
		//ako ne uklanjas krajnji desni element, moras pomicati elemente da popunis rupu
		if (index < size-1) {
			
			/*prodji do kraja niza, shiftaj svaki element ulijevo i smanji velicinu niza
			elemenata za 1 */
			for (int i = index; i < size-1; i++) {
				elements[i] = elements[i+1];
			}
			size--;
		}
		
		//inace, uklanjas krajnji desni element i samo smanji velicinu niza za 1
		else {
			size--;
		}
	}
	
	
	/**Funkcija umece zeljeni objekt na zeljenu poziciju, elemente desno od pozicije shifta
	 * udesno za 1.
	 * 
	 * @param value Objekt koji pohranjujemo.
	 * @param position Indeks polja gdje zelimo umetnuti objekt, s intervala [0, size].
	 */
	public void insert(Object value, int position) {
		
		//ako je objekt null, baci iznimku
		if (value == null) {
			throw new IllegalArgumentException("Nije dozvoljena null vrijednost!");
		}
		
		//ako je izabran nedozvoljen indeks, baci iznimku
		else if (position<0 || position>size) {
			throw new IndexOutOfBoundsException("Indeks nije iz dozvoljenog intervala!");
		}
		
		//ako kapacitet nije barem size+1, realociraj memoriju jer dodajes jos jedan objekt
		if (capacity < size+1) {
			this.reallocate();
		}
		
		//ako ne dodajes na kraj, moras elemente desno od pozicije shiftati za jedan
		if (position < size) {
			
			//pomakni sve elemente za jedan udesno do trazene pozicije, pocevsi od zadnjega
			for (int i = size-1; i >= position; i--) {
				elements[i+1] = elements[i];
			}
			
			//ubaci objekt na trazenu poziciju
			elements[position] = value;
		}
		
		//dodaj element na krajnju desnu poziciju
		else {
			elements[position] = value;
		}
		
		//uvecaj velicinu trenutnih objekata u nizu kad zavrsis s umetanjem
		size++;
	}
	
	
	/**Funkcija koja trazi mjesto pojavljivanja trazenog objekta.
	 * 
	 * @param value Objekt za koji zelimo pronaci indeks pojavljivanja.
	 * @return Indeks mjesta pojavljivanja.
	 */
	public int indexOf(Object value) {
		
		//ako takvog objekta uopce nema u kolekciji, vrati -1
		if (!this.contains(value)) {
			return -1;
		}
		
		int pozicija = 0;
		
		//inace prodji po kolekciji i vrati indeks pojavljivanja objekta
		for(int i = 0; i < size; i++) {
			if (value.equals(elements[i])) {
				pozicija = i;
				break;
			}
		}
		return pozicija;
	}
	
	
	/**Funkcija pregledava nalazi li se objekt u kolekciji ili ne.
	 * 
	 * @param value Objekt koji zelimo provjeriti.
	 * @return True ili false, ovisno o rezultatu.
	 */
	public boolean contains(Object value) {
		
		boolean postoji = false;
		
		//prodji kroz kolekciju i provjeri postoji li objekt u njoj
		for(int i = 0; i < size; i++) {
			if (value.equals(elements[i])) {
				postoji = true;
				break;
			}
		}
		return postoji;
	}

	
	public void clear() {
		
		//postavi velicinu niza kolekcije na 0
		size = 0;
	}
	
	
	/** Funkcija koja realocira velicinu polja elemenata na duplu velicinu ne mijenjajuci
	 * sadrzaj iste.
	 */
	private void reallocate() {
		
		//pomocno polje objekata u koje spremas trenutne objekte
		Object[] pom = new Object[capacity];
		for(int i = 0; i < capacity; i++) {
			pom[i] = elements[i];
		}
		
		//realociraj polje elemenata na duplu velicinu
		elements = new Object[2*capacity];
		
		//vrati objekte iz pomocnog polja natrag u polje elemenata
		for(int i = 0; i < capacity; i++) {
			elements[i] = pom[i];
		}
		
		//postavi kapacitet na dupli od trenutnoga
		capacity = 2*capacity;
	}
	
}
