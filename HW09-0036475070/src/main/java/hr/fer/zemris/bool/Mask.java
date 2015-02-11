package hr.fer.zemris.bool;

import java.util.Arrays;

/**
 * Klasa za instanciranje objekata koji sadrze maske.
 * 
 * @author Ivan Relic
 * 
 */
public class Mask {

	// privatna clanska varijabla u kojoj se cuva vrijednost maske
	private MaskValue[] value;

	// privatna clanska varijabla u kojoj se cuva je li maska dont care ili
	// obicna
	private boolean dontCare;

	/**
	 * Javni konstruktor koji prima MaskValue vrijednosti i pohranjuje ih u svoj
	 * container.
	 * 
	 * @param value
	 *            Niz MaskValue vrijednosti.
	 */
	public Mask(boolean dontCare, MaskValue... value) {

		// ako je primljena null referenca, baci exception
		if (value == null) {
			throw new IllegalArgumentException(
					"MaskValue array should not be null!");
		}

		this.dontCare = dontCare;

		// alociraj niz MaskValue velicine broja primljenih argumenata i
		// prekopiraj ih
		this.value = new MaskValue[value.length];
		for (int i = 0, length = value.length; i < length; i++) {

			// ako je bilo koja od vrijednosti polja null, baci exception
			if (value[i] == null) {
				throw new IllegalArgumentException(
						"MaskValue array shouldn't contain null!");
			}

			this.value[i] = value[i];
		}
	}

	/**
	 * Metoda prima string koji je reprezentacija maske i vraca novi objekt tipa
	 * Mask.
	 * 
	 * @param maskAsString
	 *            String iz kojeg zelimo isparsirati vrijednost maske.
	 * @return Vraca novi objekt tipa Mask.
	 */
	public static Mask parse(boolean dontCare, String maskAsString) {

		// ako je bilo koja od maski null, baci exception
		if (maskAsString == null) {
			throw new IllegalArgumentException("Mask should not be null!");
		}

		// ako primljeni argument nije oblika jednog ili vise znakova 0,1,x,
		// baci exception
		if (!maskAsString.matches("[01x]+")) {
			throw new IllegalArgumentException(
					"Mask must contain one or more 0, 1 or x!");
		}

		// kreiraj niz MaskValue velicine duljine stringa
		MaskValue[] maska = new MaskValue[maskAsString.length()];

		// prodji kroz clanove primljenog stringa i postavi MaskValue na trazenu
		// vrijednost
		for (int i = 0, length = maskAsString.length(); i < length; i++) {

			// ako je procitana 1, postavi MaskValue.ONE
			if (maskAsString.charAt(i) == '1') {
				maska[i] = MaskValue.ONE;
			}

			// ako je procitana 0, postavi MaskValue.ZERO
			else if (maskAsString.charAt(i) == '0') {
				maska[i] = MaskValue.ZERO;
			}

			// inace, procitan je x, pa postavi MaskValue.DONT_CARE
			else {
				maska[i] = MaskValue.DONT_CARE;
			}
		}

		// vrati novu masku koja se sastoji od kreiranog niza MaskValue
		return new Mask(dontCare, maska);
	}

	/**
	 * Metoda prima broj varijabli i mintermu i iz toga kreira novu masku.
	 * 
	 * @param size
	 *            Velicina, tj. broj varijabli od kojih se maska sastoji.
	 * @param index
	 *            Minterm koji predstavlja zeljenu masku.
	 * @return Novi objekt tipa Mask.
	 */
	public static Mask fromIndex(boolean dontCare, int size, int index) {

		// ako je index veci od 2^size - 1, baci exception
		if (index > Math.pow(2, size) - 1) {
			throw new IllegalArgumentException(
					"Index should be smaller than 2^size!");
		}

		// ako je index negativan, ili size nije pozitivan, baci exception
		if (index < 0 || size <= 0) {
			throw new IllegalArgumentException("Index and/or size is illegal!");
		}

		// kreiraj novi niz MaskValuea velicine size
		MaskValue[] maska = new MaskValue[size];

		// prodji nizom i postavi vrijednosti MaskValue
		int i = 0;
		while (i < size) {

			// trenutna potencija broja 2, krecemo od pocetka i smanjujemo prema
			// kraju
			int trenPotencija = (int) Math.pow(2, size - 1 - i);

			/*
			 * ako mozes oduzeti potenciju broja dva na trenutnu poziciju i ako
			 * vrijednost indexa nije 0, postavi MaskValue na 1
			 */
			if (index - trenPotencija >= 0 && index != 0) {

				// oduzmi tu vrijednost od indexa
				index -= trenPotencija;

				// postavi MaskValue na 1
				maska[i] = MaskValue.ONE;
			}

			// inace, postavi MaskValue na 0
			else {
				maska[i] = MaskValue.ZERO;
			}

			i++;
		}

		// vrati novu Masku koja se sastoji od niza MaskValuesa
		return new Mask(dontCare, maska);
	}

	/**
	 * Metoda koja stapa maske koje se razlikuju u tocno jednoj varijabli u
	 * jednu u novu u kojoj je na mjestu te varijable don't care vrijednost.
	 * 
	 * @param maska1
	 *            Prva maska za provjeru, treba biti razlicita od null.
	 * @param maska2
	 *            Druga maska za provjeru, treba biti razlicita od null.
	 * @return Nova kombinirana maska, ako je to moguce, null ako nije.
	 */
	public static Mask combine(Mask maska1, Mask maska2) {

		// ako je bilo koja od maski null, baci exception
		if (maska1 == null || maska2 == null) {
			throw new IllegalArgumentException("Mask should not be null!");
		}

		// ako maske nisu iste duljine, baci exception
		if (maska1.value.length != maska2.value.length) {
			throw new IllegalArgumentException(
					"Masks should be equal in length!");
		}

		if (maska1.equals(maska2)) {
			return null;
		}

		// kreiraj novo polje MaskValue duljine iste kao primljene maske
		int numberOfCombinings = 0;
		MaskValue[] novaMaska = new MaskValue[maska1.value.length];
		for (int i = 0, length = novaMaska.length; i < length; i++) {
			if (maska1.getValue(i) != maska2.getValue(i)) {
				if (!(maska1.getValue(i) == MaskValue.DONT_CARE || maska2
						.getValue(i) == MaskValue.DONT_CARE)) {
					novaMaska[i] = MaskValue.DONT_CARE;
					numberOfCombinings++;
					if (numberOfCombinings > 1) {
						return null;
					}
				} else {
					return null;
				}
			} else {
				novaMaska[i] = maska1.getValue(i);
			}
		}
		if (numberOfCombinings != 1) {
			return null;
		}
		return new Mask(maska1.isDontCare() && maska2.isDontCare(), novaMaska);

	}

	/**
	 * Metoda vraca broj nula iz maske.
	 * 
	 * @return Broj nula.
	 */
	public int getNumberOfZeroes() {

		// ako je velicina maske 0, vrati 0
		if (this.value.length == 0) {
			return 0;
		}

		int brojNula = 0;

		// inace, prodji kroz sve MaskValue i izbroji nule
		for (int i = 0, length = this.value.length; i < length; i++) {
			if (this.value[i] == MaskValue.ZERO) {
				brojNula++;
			}
		}

		return brojNula;
	}

	/**
	 * Provjerava je li maska nad kojom se zove metoda "generalnija" od maske
	 * koja je primljena kao argument.
	 * 
	 * @param maska
	 *            Maska s kojom zelimo provjeriti je li trenutni objekt
	 *            generalniji od nje.
	 * @return True ili false, ovisno o rezultatu.
	 */
	public boolean isMoreGeneralThan(Mask maska) {

		// ako je predan null argument, baci exception
		if (maska == null) {
			throw new IllegalArgumentException(
					"Mask should not be null reference!");
		}

		// ako maske nisu iste duljine, baci exception
		if (this.value.length != maska.value.length) {
			throw new IllegalArgumentException("Masks should be same size!");
		}

		/*
		 * prodji kroz obe maske i provjeri podudaraju li im se vrijednosti, ne
		 * gledajuci dont care vrijednosti
		 */
		for (int i = 0, length = maska.value.length; i < length; i++) {
			if (this.value[i] != maska.value[i]
					&& !(this.value[i] == MaskValue.DONT_CARE)) {
				return false;
			}
		}

		// ako maska this ima vise DONT CARE vrijednosti, generalnija je
		if (this.value.length - this.getNumberOfOnes()
				- this.getNumberOfZeroes() > maska.value.length
				- maska.getNumberOfOnes() - maska.getNumberOfZeroes()) {
			return true;
		}

		// inace, vrati false
		else {
			return false;
		}
	}

	/**
	 * Metoda vraca broj jedinica iz maske.
	 * 
	 * @return Broj jedinica.
	 */
	public int getNumberOfOnes() {

		// ako je velicina maske 0, vrati 0
		if (this.value.length == 0) {
			return 0;
		}

		int brojJedinica = 0;

		// inace, prodji kroz sve MaskValue i izbroji jedinice
		for (int i = 0, length = this.value.length; i < length; i++) {
			if (this.value[i] == MaskValue.ONE) {
				brojJedinica++;
			}
		}

		return brojJedinica;
	}

	/**
	 * Metoda vraca velicinu maske nad kojom se metoda poziva.
	 * 
	 * @return Velicina maske.
	 */
	public int getSize() {
		return this.value.length;
	}

	/**
	 * Metoda vraca vrijednost iz privatnog niza MaskValuea s trazenog indeksa.
	 * 
	 * @param index
	 *            Indeks s kojeg zelimo vrijednost MaskValuea.
	 * @return Vrijednost MaskValuea.
	 */
	public MaskValue getValue(int index) {

		// ako je privatni clanski niz MaskValuea null ili je nedozvoljen
		// indeks, baci exception
		if (this.value == null || index < 0 || index > this.value.length - 1) {
			throw new IllegalArgumentException(
					"Can't reach element from that index!");
		}

		return this.value[index];
	}

	/**
	 * Vraca je li maska dont care(true) ili obicna - minterm ili maksterm
	 * (false).
	 * 
	 * @return true ili false
	 */
	public boolean isDontCare() {
		return dontCare;
	}

	/**
	 * Override metode koja vraca hash vrijednost objekta.
	 * 
	 * @return Hash vrijednost objekta.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	/**
	 * Override metode za usporedjivanje.
	 * 
	 * @return True ili false.
	 */
	public boolean equals(Object obj) {

		// ako si dobio referencu na samog sebe, vrati true
		if (this == obj) {
			return true;
		}

		// ako je obj null, vrati false
		if (obj == null) {
			return false;
		}

		// ako objekt nije instanca klase Mask, vrati false
		if (!(obj instanceof Mask)) {
			return false;
		}

		// dereferenciraj ojekt na klasu Mask i usporedi im polje value, ako
		// nisu isti - false
		Mask other = (Mask) obj;
		if (!Arrays.equals(value, other.value)) {
			return false;
		}

		// inace vrati true
		return true;
	}

	/**
	 * Override metode toString.
	 * 
	 * @return String sadrzaja maske.
	 * 
	 */
	public String toString() {

		// builder za brzu konkatenaciju
		StringBuilder builder = new StringBuilder();

		// prodji kroz cijelu masku
		for (int i = 0, length = this.value.length; i < length; i++) {

			// ako je vrijednost 1, appendaj to u builder
			if (this.value[i] == MaskValue.ONE) {
				builder.append('1');
			}

			// ako je vrijednost 0, appendaj to u builder
			else if (this.value[i] == MaskValue.ZERO) {
				builder.append('0');
			}

			// inace, appendaj x
			else {
				builder.append('x');
			}
		}

		return builder.toString();
	}

}
