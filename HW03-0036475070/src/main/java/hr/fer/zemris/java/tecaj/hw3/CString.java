package hr.fer.zemris.java.tecaj.hw3;


/**Klasa za implementaciju stringa iz starijih verzija Jave, s pratecim metodama za rad sa
 * stringovima, objekti ove klase su immutable.
 * 
 * @author Ivan Relic
 *
 */
public class CString {
	
	//clanske varijable klase
	private char[] data;
	private int offset;
	private int length;
	
	
	/**Konstruktor metoda koja od primljenog niza znakova, pomaka i duljine kreira string.
	 * 
	 * @param data Niz znakova.
	 * @param offset Od kojeg znaka string pocinje.
	 * @param length Koliko je string dug.
	 */
	public CString(char[] data, int offset, int length) {
		
		/*ako je offset veci od duljine niza, ako je length veci od ukupne duljine niza, ili
		ako je offset + length veci od duljine polja, baci exception */
		if (offset > data.length || length > data.length || offset + length > data.length) {
			throw new IllegalArgumentException("Bad offset or length!");
		}
		
		this.data = new char[length];
		this.offset = 0;
		this.length = length;
		
		//prekopiraj charove iz data u interni spremnik CStringa
		for (int i = 0; i < length; i++) {
			this.data[i] = data[i + offset];
		}
	}
	
	
	/**Private konstruktor za potrebe implementacije metoda klase.
	 * 
	 * @param data Niz znakova.
	 * @param offset Od kojeg znaka string pocinje.
	 * @param length Koliko je string dug.
	 * @param c Kontrolni znak za private konstruktor.
	 */
	private CString(char[] data, int offset, int length, char c) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}
	
	
	/**Konstruktor metoda koja od primljenog niza znakova kreira string.
	 * 
	 * @param data Niz znakova.
	 */
	public CString(char[] data) {
		this.data = new char[data.length];
		this.offset = 0;
		this.length = data.length;
		
		//prekopiraj charove iz data u interni spremnik CStringa
		for (int i = 0; i < data.length; i++) {
			this.data[i] = data[i];
		}
	}
	
	
	/**Konstruktor metoda koja primljenome stringu realocira memoriju niza znakova
	 * na najmanju što je potrebno, ako je to već zadovoljeno, ostavlja mu stari niz znakova.
	 * 
	 * @param original Originalni string.
	 */
	public CString(CString original) {
		
		//ako primiš null referencu, baci exception
		if (original == null) {
			throw new IllegalArgumentException("Argument can't be null!");
		}
		
		//ako je duljina internog niza charova dulja od lengtha, realociraj
		if (original.data.length > original.length) {
			
			//alociraj memorije koliko je string dugacak i prekopiraj ga u nju
			this.data = new char[original.length];
			for (int i = 0; i < original.length; i++) {
				this.data[i] = original.data[i + original.offset];
			}
			this.offset = 0;
			this.length = original.length;
		}
		
		//inace, samo iskorisi stari niz
		else {
			this.data = original.data;
			this.offset = original.offset;
			this.length = original.length;
		}
	}
	
	
	/**Konstruktor metoda prima string kojeg pohranjuje u svoj interni spremnik.
	 * 
	 * @param s String iz kojeg stvaramo svoj string objekt.
	 */
	public CString(String s) {
		this.data = s.toCharArray();
		this.offset = 0;
		this.length = data.length;
	}
	
	
	/**Metoda vraca duljinu stringa kojeg objekt predstavlja.
	 * 
	 * @return Integer broj.
	 */
	public int length() {
		return this.length;
	}

	
	/**Metoda vraca character koji se nalazi u objektu na trazenoj poziciji.
	 * 
	 * @param index Integer broj.
	 * @return Character.
	 */
	public char charAt(int index) {
		
		//baci exception ako si na nedozvoljenom indexu
		if (index < 0 || index > this.data.length - 1) {
			throw new IllegalArgumentException("Index ne postoji!");			
		}
		else {
			return this.data[index + this.offset];
		}
	}
	
	
	/**Metoda string iz objekta vraca kao niz charactera.
	 * 
	 * @return Niz znakova.
	 */
	public char[] toCharArray() {
		
		//kreiraj novi niz charova velicine length
		char[] array = new char[this.length];
		
		//prekopiraj charove u novi niz i vrati ga
		for (int i = 0; i < this.length; i++) {
			array[i] = this.data[i + this.offset];
		}
		return array;		
	}
	
	
	/**Overrideana metoda za ispis objekta.
	 * 
	 * @return Vraca CString u obliku stringa.
	 */
	public String toString() {
		String pom = "";
		for(int i = 0; i < this.length; i++) {
			pom = pom + this.data[i + this.offset];
		}
		return pom;
	}
	
	
	/**Metoda prima znak i vraca index pojavljivanja tog znaka, ili -1 ako ga nema.
	 * 
	 * @param c Znak koji provjeravamo.
	 * @return Index znaka.
	 */
	public int indexOf(char c) {
		
		for (int i = 0; i < this.length; i++) {
			if (this.data[i + this.offset] == c) {
				return i;
			}
		}
				
		//ako si prosao kroz petlju i nisi nigdje nasao znak c
		return -1;
	}
	
	
	/**Provjerava pocinje li string nad kojim pozivamo metodu s predanim stringom.
	 * 
	 * @param s String s kojim provjeravam.
	 * @return True ili false, ovisno o provjeri.
	 */
	public boolean startsWith(CString s) {
		
		//ako primiš null referencu, baci exception
		if (s == null) {
			throw new IllegalArgumentException("Argument can't be null!");
		}
		
		/*ako je duljina stringa s kojim se provjerava veca od duljine stringa nad kojom se 
		provjerava, vrati false */
		if (s.length > this.length) {
			return false;
		}
		
		//ako je s prazan string, vrati true, svaki niz sadrzi prazan niz
		if (s.length == 0) {
			return true;
		}
		
		//provjeri slaze li se broj elemenata koji odgovara duljini primljenog stringa
		for (int i = 0; i < s.length; i++) {
			
			//ako bilo koji od znakova ne odgovara primljenom stringu, vrati false
			if (this.data[i + this.offset] != s.data[i + s.offset]) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/**Provjerava zavrsava li string nad kojim pozivamo metodu s predanim stringom.
	 * 
	 * @param s String s kojim provjeravamo.
	 * @return True ili false.
	 */
	public boolean endsWith(CString s) {
		
		//ako primiš null referencu, baci exception
		if (s == null) {
			throw new IllegalArgumentException("Argument can't be null!");
		}
		
		/*ako je duljina stringa s kojim se provjerava veca od duljine stringa nad kojom se 
		provjerava, vrati false */
		if (s.length > this.length) {
			return false;
		}
		
		//ako je s prazan string, vrati true, svaki niz sadrzi prazan niz
		if (s.length == 0) {
			return true;
		}
		
		//provjeri slaze li se broj elemenata koji odgovara duljini primljenog stringa
		for (int i = 0; i < s.length; i++) {
			
			//ako bilo koji od znakova ne odgovara predanom stringu, vrati false
			if (this.data[this.offset + this.length - i - 1] 
					!= s.data[s.offset + s.length - i - 1]) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/**Provjerava nalazi li se predani string u stringu nad kojim pozivamo metodu.
	 * 
	 * @param s String za koji zelimo provjeriti pojavljuje li se.
	 * @return True ili false, ovisno o rezultatu.
	 */
	public boolean contains(CString s) {
		
		//ako primiš null referencu, baci exception
		if (s == null) {
			throw new IllegalArgumentException("Argument can't be null!");
		}
		
		/*ako je duljina stringa s kojim se provjerava veca od duljine stringa nad kojom se 
		provjerava, vrati false */
		if (s.length > this.length) {
			return false;
		}
		
		//ako je s prazan string, vrati true, svaki niz sadrzi prazan niz
		if (s.length == 0) {
			return true;
		}
		
		//brojac jednakih elemenata, mora odgovarati duljini CStringa koji se trazi
		int brojacJednakih = 0;
		
		//kreni kroz string koji provjeravas
		for (int i = 0; i < (this.length - s.length + 1); i++) {
			
			//ako trenutni znak odgovara prvom znaku stringa koji se trazi, udji u petlju
			if (this.data[i + this.offset] == s.data[s.offset]) {
				
				//provjeri ostale znakove ako prvi odgovara
				for (int j = 0; j < s.length; j++) {
					
					//breakaj iz petlje ako sljedeci znakovi nisu isti i resetiraj brojac
					if (this.data[i + j + this.offset] != s.data[j + s.offset]) {
						brojacJednakih = 0;
						break;
					}
					
					//inace uvecaj brojac
					else {
						brojacJednakih++;
						
						//ako je brojac jednak duljini trazenog niza, vrati true
						if (brojacJednakih == s.length) {
							return true;
						}
					}
				}
			}
		}
		
		//ako nakon petlje broj pronadjenih nije jednak velicini trazenog niza, vrati false
		if (brojacJednakih != s.length) {
			return false;
		}
		
		else {
			return true;
		}
				
	}
	
	
	/**Metoda vraca substring zadane "sirine" od stringa nad kojim se poziva.
	 * 
	 * @param startIndex Indeks od kojeg zelimo substring, u intervalu [0, length-1]
	 * @param endIndex Indeks do kojeg zelimo substring, u intervalu [startIndex, length-1]
	 * @return Vraca novi CString.
	 */
	public CString substring(int startIndex, int endIndex) {
		
		//ako su indexi nedozvoljeni, baci exception
		if (startIndex < 0 || startIndex > (length) ||
				endIndex < startIndex || endIndex > (length)) {
			throw new IllegalArgumentException("Bad substring indexes!");
		}
		
		return new CString(this.data, this.offset + startIndex, endIndex - startIndex, '1');
	}
	
	
	/**Metoda vraca novi string koji sadrzi n prvih znakova stringa nad kojim se metoda
	 * poziva.
	 * 
	 * @param n Koliko prvih znakova zelimo, n mora biti u intervalu [0, length].
	 * @return Novi CString.
	 */
	public CString left(int n) {
		
		/*ako je zatrazeno vise znakova nego sto je duljina stringa, ili je 
		zatrazen negativan broj znakova, baci exception*/
		if (n > this.length || n < 0) {
			throw new IllegalArgumentException("Bad argument for left!");
		}
		
		return new CString(this.data, this.offset, n, '1');
	}
	
	
	/**Metoda vraca novi string koji sadrzi n zadnjih znakova stringa nad kojim se metoda 
	 * poziva.
	 * 
	 * @param n Koliko zadnjih znakova zelimo, n mora biti u intervalu [0, length].
	 * @return Novi CString.
	 */
	public CString right(int n) {
		
		/*ako je zatrazeno vise znakova nego sto je duljina stringa, ili je 
		zatrazen negativan broj znakova, baci exception*/
		if (n > this.length || n < 0) {
			throw new IllegalArgumentException("Bad argument for left!");
		}
		
		return new CString(this.data, this.offset + this.length - n, n, '1');
	}
	
	
	/**Metoda za konkatenaciju, tj. spajanje dva stringa u jedan novi.
	 * 
	 * @param s String koji nadodajemo.
	 * @return Novi CString.
	 */
	public CString add(CString s) {
		
		//ako je kao argument predana null referenca baci exception
		if (s == null) {
			throw new IllegalArgumentException("Argument can't be null!");
		}
		
		//kreiraj novi CString velicine stringa nad kojim se poziva metoda + velicina arg
		CString novi = new CString(new char[this.length + s.length]);
		
		//postavi offset i velicinu novoga stringa
		novi.offset = 0;
		novi.length = this.length + s.length;
		
		//prekopiraj sve iz trenutnog stringa
		int i = 0;
		for (; i < this.length; i++) {
			novi.data[i] = this.data[i + this.offset];
		}
				
		//prekopiraj sve iz novog stringa
		for (int j = 0; j < s.length; i++, j++) {
			novi.data[i] = s.data[j + s.offset];
		}
		
		return novi;
	}
	
	
	/**Metoda vraca novi string u kojem su sva pojavljivanja zeljenog znaka zamijenjena 
	 * zeljenim znakom.
	 * 
	 * @param oldChar Znak koji zelimo zamijeniti.
	 * @param newChar Znak kojim zelimo zamijeniti pojavljivanje staroga.
	 * @return Novi CString.
	 */
	public CString replaceAll(char oldChar, char newChar) {
		
		//kreiraj novi string velicine stringa nad kojim vrsis zamjenu
		CString novi = new CString(new char[this.length]);
		
		//prodji kroz string
		for (int i = 0; i < this.length; i++) {
			
			/*ako je trenutni znak stringa jednak onom kojeg zelimo zamijeniti, pospremi
			zamjenu umjesto njega */
			if (this.data[i + this.offset] == oldChar) {
				novi.data[i] = newChar;
			}
			
			//inace, samo prekopiraj iz stringa u novi string
			else {
				novi.data[i] = this.data[i + this.offset];
			}
		}
		
		return novi;
	}
	
	
	/**Metoda vraca novi string u kojem su sva pojavljivanja zeljenog stringa zamijenjena 
	 * zeljenim stringom.
	 * @param oldStr String koji zelimo zamijeniti.
	 * @param newStr String kojim zelimo zamijeniti oldStr.
	 * @return Novi string sa zamijenjenim svim pojavljivanjima oldStr.
	 */
	public CString replaceAll(CString oldStr, CString newStr) {
		
		//ako je kao oldStr ili newStr predan null, baci exception
		if (oldStr == null || newStr == null) {
			throw new IllegalArgumentException("Arguments can't be null!");
		}
		
		//ako je oldStr prazan niz, ne trebas nista mijenjati, samo vrati isti CString
		if (oldStr.length == 0) {
			return new CString(this);
		}
		
		/*kreiraj novi string velicine stringa nad kojim je pozvana metoda + 
		duljina stringa nad kojim je pozvano * duljina newStr jer je to maksimalno sto se 
		moze dobiti zamjenom*/
		CString novi = new CString(new char[this.length + this.length*newStr.length]);
		
		//postavi duljinu na 0 jer ce se povecavati kako se elementi dodaju
		novi.length = 0;
		
		/*prodji kroz string, noviBrojac oznacava iterator za kopiranje elemenata u 
		novi CString*/
		int noviBrojac = 0;
		for(int i = 0; i < this.length; i++) {
			
			boolean nasao = false;
			//ako trenutni znak odgovara prvom znaku stringa koji se trazi, udji u petlju
			if (this.data[i + this.offset] == oldStr.data[oldStr.offset]) {
				
				/*provjeri ostale znakove ako prvi odgovara, uvjet i+j < this.length je tu
				da u provjeri unaprijed za ostale znakove oldStr ne iskocis iz dopustenih
				indexa polja this.data */
				for (int j = 0; j < oldStr.length && i+j < this.length; j++) {
					if (this.data[i + j + this.offset] != oldStr.data[j + oldStr.offset]) {
						nasao = false;
						break;
					}
					
					/* ako si dosao do zadnjeg znaka, i do tada nisi breakao i taj zadnji
					znak odgovara zadnjem znaku oldStr nasao postavi na true */
					if (j == oldStr.length - 1 && 
							this.data[i + j + this.offset] == oldStr.data[j + oldStr.offset]) {
						nasao = true;
					}
				}
			}
			
			//ako je pronadjen oldString
			if (nasao) {
				
				/*ako newStr nije duljine 0, ako je, nista ne kopiras nego samo preskaces
				pojavljivanje oldStr*/
				if(newStr.length != 0) {
				
					//kopiraj mu newString umjesto oldStringa
					for (int k = 0; k < newStr.length; k++) {
						novi.data[noviBrojac] = newStr.data[k + newStr.offset];
						noviBrojac++;
					
						//povecaj velicinu CStringa novi
						(novi.length)++;
					}
				}
				
				//povecaj iterator i za velicinu oldStr jer to sad preskaces
				i += oldStr.length - 1;
			}
			
			//ako nije pronadjen podniz, samo iskopiraj trenutni element u CString novi
			else {
				novi.data[noviBrojac] = this.data[i + this.offset];
				(novi.length)++;
				noviBrojac++;
			}
		}
		
		//realociraj novi string na minimalnu velicinu i vrati ga
		return new CString(novi);
	}
}
