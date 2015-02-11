package hr.fer.zemris.bool.fimpl;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.MaskValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa za generiranje objekata koji su container za kompletnu funkciju
 * baziranu na indeksima minterma/maksterma i dont careova.
 * 
 * @author Ivan Relic
 * 
 */
public class IndexedBF implements BooleanFunction {

	private String name;
	private List<BooleanVariable> domain;
	private boolean indexesAreMinterms;
	private List<Integer> indexes;
	private List<Integer> dontCares;

	// pomocna lista u kojoj se cuvaju mintermi/maxtermi
	private List<Integer> otherIndexes;

	// lista sa svim indeksima domene
	private List<Integer> domainIndexes;

	/**
	 * Javni konstruktor koji postavlja clanske varijable objekta.
	 * 
	 * @param name
	 *            Ime funkcije.
	 * @param domain
	 *            Varijable funkcije.
	 * @param indexesAreMinterms
	 *            Jesu li dani indeksi mintermi ili makstermi.
	 * @param indexes
	 *            Lista indeksa, tj. minterma/maksterma.
	 * @param dontCares
	 *            Lista indeksa DONT CARE-ova.
	 */
	public IndexedBF(String name, List<BooleanVariable> domain,
			boolean indexesAreMinterms, List<Integer> indexes,
			List<Integer> dontCares) {

		// ako je bilo koji od argumenata null, baci exception
		if (name == null || domain == null || indexes == null
				|| dontCares == null) {
			throw new IllegalArgumentException("Arguments shouldn't be null!");
		}

		this.name = name;
		this.domain = new ArrayList<BooleanVariable>(domain);
		this.indexesAreMinterms = indexesAreMinterms;
		this.indexes = new ArrayList<Integer>(indexes);
		this.dontCares = new ArrayList<Integer>(dontCares);

		// kreiraj listu svih indeksa domene
		this.domainIndexes = createDomainIndexes();

		// kreiraj listu ostalih indeksa
		this.otherIndexes = createOtherIndexes();

		// provjeri domenu
		checkDomain();
	}

	/**
	 * Metoda koja vraca ime funkcije.
	 * 
	 * @return Ime funkcije.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Metoda vraca vrijednost funkcije za trenutno stanje varijabli.
	 * 
	 * @return Vrijednost funkcije - TRUE, FALSE ili DONT CARE
	 */
	public BooleanValue getValue() {

		MaskValue[] maskVal = new MaskValue[this.domain.size()];
		// prodji kroz domenu i dohvati stanja varijabli
		for (int i = 0, length = this.domain.size(); i < length; i++) {

			// ako je varijabla postavljena na TRUE, u masku dodaj 1
			if (this.domain.get(i).getValue() == BooleanValue.TRUE) {
				maskVal[i] = MaskValue.ONE;
			}

			// ako je varijabla postavljena na FALSE, u masku dodaj 0
			else if (this.domain.get(i).getValue() == BooleanValue.FALSE) {
				maskVal[i] = MaskValue.ZERO;
			}

			// inace, varijabla je postavljena na DONT CARE, pa dodaj x
			else {
				maskVal[i] = MaskValue.DONT_CARE;
			}
		}

		// kreiraj masku iz stringa
		Mask maska = new Mask(maskVal);

		// kreiraj sve indexe koje ova maska definira
		List<Integer> maskIndex = new ArrayList<Integer>();
		for (int i = 0, length = domainIndexes.size(), maskSize = domain.size(); i < length; i++) {
			Mask pom = Mask.fromIndex(maskSize, i);
			if (maska.isMoreGeneralThan(pom) || maska.equals(pom)) {
				maskIndex.add(new Integer(i));
			}
		}

		// ako je u mintermima i makstermima, vrati dont care
		if (!Collections.disjoint(maskIndex, indexes)
				&& !Collections.disjoint(maskIndex, otherIndexes)) {
			return BooleanValue.DONT_CARE;
		}

		// ako indexi predstavljaju minterme
		if (indexesAreMinterms) {

			// ako je u makstermima, sigurno je FALSE
			if (!Collections.disjoint(maskIndex, otherIndexes)) {
				return BooleanValue.FALSE;
			}

			// ako je u DONT CARE, a nije u mintermima, vrati DONT CARE
			if (!Collections.disjoint(maskIndex, this.dontCares)
					&& Collections.disjoint(maskIndex, this.indexes)) {
				return BooleanValue.DONT_CARE;
			}

			// za sve ostalo vrati TRUE
			return BooleanValue.TRUE;
		}

		// ako indexi predstavljaju maksterme
		else {

			// ako je u mintermima, sigurno je TRUE
			if (!Collections.disjoint(maskIndex, otherIndexes)) {
				return BooleanValue.TRUE;
			}

			// ako je u DONT CARE, a nije u makstermima, vrati DONT CARE
			if (!Collections.disjoint(maskIndex, dontCares)
					&& Collections.disjoint(maskIndex, indexes)) {
				return BooleanValue.DONT_CARE;
			}

			// za sve ostalo vrati FALSE
			return BooleanValue.FALSE;
		}
	}

	/**
	 * Metoda vraca domenu, tj. varijable funkcije, i to kao unmodifiable listu.
	 * 
	 * @return Lista varijabli.
	 */
	public List<BooleanVariable> getDomain() {
		List<BooleanVariable> lista = new ArrayList<BooleanVariable>(
				this.domain);
		return Collections.unmodifiableList(lista);
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni minterm.
	 * 
	 * @param index
	 *            Indeks trazenog minterma.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni minterm.
	 */
	public boolean hasMinterm(int index) {

		// ako su predani indexi mintermi
		if (indexesAreMinterms) {
			return this.indexes.contains(new Integer(index));
		}

		// inace provjeri u ostalim indeksima
		else {
			return otherIndexes.contains(new Integer(index));
		}
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni maksterm.
	 * 
	 * @param index
	 *            Indeks trazenog maksterma.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni maksterm.
	 */
	public boolean hasMaxterm(int index) {
		// ako su predani indexi makstermi
		if (!indexesAreMinterms) {
			return this.indexes.contains(new Integer(index));
		}

		// inace provjeri u ostalim indeksima
		else {
			return otherIndexes.contains(new Integer(index));
		}
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni dont care.
	 * 
	 * @param index
	 *            Indeks trazenog dont carea.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni dont care.
	 */
	public boolean hasDontCare(int index) {
		return this.dontCares.contains(new Integer(index));
	}

	/**
	 * Metoda vraca iterabilnu listu minterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> mintermIterable() {

		// ako su indeksi mintermi, vrati njih kao listu
		if (indexesAreMinterms) {
			List<Integer> lista = new ArrayList<Integer>(this.indexes);

			// vrati listu sortirano jer indexi mogu biti zadani raznim
			// redoslijedima
			Collections.sort(lista);
			return lista;
		}

		// inace vrati otherIndexes listu
		else {
			List<Integer> lista = new ArrayList<Integer>(this.otherIndexes);
			// sortiraj listu prije vracanja
			Collections.sort(lista);
			return lista;
		}
	}

	/**
	 * Metoda vraca iterabilnu listu maksterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> maxtermIterable() {

		// ako su indeksi makstermi, vrati njih kao listu
		if (!indexesAreMinterms) {
			List<Integer> lista = new ArrayList<Integer>(this.indexes);

			// vrati listu sortirano jer indexi mogu biti zadani raznim
			// redoslijedima
			Collections.sort(lista);
			return lista;
		}

		// inace vrati otherIndexes listu
		else {
			List<Integer> lista = new ArrayList<Integer>(this.otherIndexes);
			// sortiraj listu prije vracanja
			Collections.sort(lista);
			return lista;
		}
	}

	/**
	 * Metoda vraca iterabilnu listu dont careova funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> dontCareIterable() {
		List<Integer> lista = new ArrayList<Integer>(this.dontCares);

		// sortiraj listu prije vracanja
		Collections.sort(lista);
		return lista;
	}

	/**
	 * Metoda provjerava jesu li domene varijabli i indeksa uskladjene.
	 * Provjerava je li unesen element izvan domene broja varijabli funkcija i
	 * preklapaju li se vrijednosti minterma/maksterma i dont careova.
	 * 
	 */
	private void checkDomain() {

		// ako lista indeksa ima koji zajednicki element s listom dont care
		// indeksa, exception
		if (!Collections.disjoint(indexes, dontCares)) {
			throw new IllegalArgumentException(
					"Same index in dont care and minterm/maxterm!");
		}

		// provjeri sadrzi li lista indeksa neki clan izvan domene
		List<Integer> pom = new ArrayList<Integer>(indexes);
		pom.removeAll(domainIndexes);
		if (pom.size() > 0) {
			throw new IllegalArgumentException(
					"Index greater than variable domain!");
		}

		// provjeri sadrzi li lista dont careova neki clan izvan domene
		pom = new ArrayList<Integer>(dontCares);
		pom.removeAll(domainIndexes);
		if (pom.size() > 0) {
			throw new IllegalArgumentException(
					"Dont care index greater than variable domain!");
		}
	}

	/**
	 * Kreira sve moguce indekse nad domenom ove funkcije.
	 * 
	 * @return lista sa svim indeksima domene
	 */
	private List<Integer> createDomainIndexes() {
		List<Integer> domainIndexes = new ArrayList<Integer>();
		for (int i = 0, length = (int) Math.pow(2, this.domain.size()); i < length; i++) {
			domainIndexes.add(new Integer(i));
		}
		return domainIndexes;
	}

	/**
	 * Kreira listu komplementarnih indeksa (ako je primio minterme, kreira
	 * maksterme, a ako je primio maksterme, kreira minterme).
	 * 
	 * @return lista komplementarnih indeksa
	 */
	private List<Integer> createOtherIndexes() {
		Set<Integer> pomIndexes = new HashSet<Integer>(indexes);
		Set<Integer> pomDontCares = new HashSet<Integer>(dontCares);
		List<Integer> otherIndexes = new ArrayList<Integer>();
		for (int i = 0, length = (int) Math.pow(2, this.domain.size()); i < length; i++) {
			Integer index = new Integer(i);
			if (!pomIndexes.contains(index) && !pomDontCares.contains(index)) {
				otherIndexes.add(index);
			}
		}
		return otherIndexes;
	}

}
