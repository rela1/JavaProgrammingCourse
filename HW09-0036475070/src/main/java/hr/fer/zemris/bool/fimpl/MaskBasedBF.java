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
 * baziranu na maskama minterma/maksterma i dont careova.
 * 
 * @author Ivan Relic
 * 
 */
public class MaskBasedBF implements BooleanFunction {

	private String name;
	private List<BooleanVariable> domain;
	private boolean masksAreMinterms;
	private List<Mask> masks;
	private List<Mask> dontCareMasks;

	// pomocna lista u kojoj se cuvaju svi moguci implikanti nad domenom
	// funkcije
	private List<Integer> domainIndexes;

	// pomocna lista u kojoj su sacuvani indeksi rekreirani iz maska
	private List<Integer> indexList;

	// pomocna lista u kojoj su sacuvani ostali indeksi (indeksi domene -
	// indeksi - dont care)
	private List<Integer> otherIndexList;

	// pomocna lista u kojoj su sacuvani indeksi rekreirani iz dontcare maska
	private List<Integer> dontCareIndexList;

	/**
	 * Vraca listu ostalih indexa funkcije.
	 * 
	 * @return lista integera
	 */
	public List<Integer> getOtherIndexList() {
		return otherIndexList;
	}

	/**
	 * Javni konstruktor postavlja clanske varijable i racuna indekse iz maski.
	 * 
	 * @param name
	 *            Ime funkcije.
	 * @param domain
	 *            Domena, tj. varijable funkcije.
	 * @param masksAreMinterms
	 *            Jesu li maske mintermi ili makstermi.
	 * @param masks
	 *            Lista maski.
	 * @param dontCareMasks
	 *            Lista dont care maski.
	 */
	public MaskBasedBF(String name, List<BooleanVariable> domain,
			boolean masksAreMinterms, List<Mask> masks, List<Mask> dontCareMasks) {

		// ako je bilo koji od argumenata null, baci exception
		if (name == null || domain == null || masks == null
				|| dontCareMasks == null) {
			throw new IllegalArgumentException("Arguments shouldn't be null!");
		}

		this.name = name;
		this.domain = new ArrayList<BooleanVariable>(domain);
		this.masksAreMinterms = masksAreMinterms;
		this.masks = new ArrayList<Mask>(masks);
		this.dontCareMasks = new ArrayList<Mask>(dontCareMasks);

		// kreiraj pomocnu listu svih implikanata od 0 do 2^velicina domene
		this.domainIndexes = createDomainIndexes();

		// kreiraj listu indexa iz maski
		this.indexList = createIndexList(masks);

		// kreiraj listu indexa dont care maski
		this.dontCareIndexList = createIndexList(dontCareMasks);

		// kreiraj listu ostalih indexa
		this.otherIndexList = createOtherIndexes();

		// provjeri domene varijabli i maska nakon sto su kreirani indexi
		checkDomain();
	}

	/**
	 * Metoda vraca ime funkcije.
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Vraca listu predanih maski koje definiraju lokacije na kojima funkcija
	 * poprima vrijednost 1 ili 0 (ovisno o zastavici predanoj u konstruktoru).
	 * 
	 * @return lista maski
	 */
	public List<Mask> getMasks() {
		return new ArrayList<Mask>(masks);
	}
	
	/**
	 * Vraca listu maski koje definiraju don't care lokacije.
	 * 
	 * @return lista dont care maski
	 */
	public List<Mask> getDontCareMasks() {
		return new ArrayList<Mask>(dontCareMasks);
	}
	
	/**
	 * Vraca jesu li maske produkti (true) ili sume (false).
	 * 
	 * @return true ili false
	 */
	public boolean areMasksProducts() {
		return !masksAreMinterms;
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
		Mask maska = new Mask(false, maskVal);

		// kreiraj sve indexe koje ova maska definira
		List<Integer> maskIndex = createIndexesFromMask(maska);

		// ako je u mintermima i makstermima, vrati dont care
		if (!Collections.disjoint(maskIndex, indexList)
				&& !Collections.disjoint(maskIndex, otherIndexList)) {
			return BooleanValue.DONT_CARE;
		}

		// ako indexi predstavljaju minterme
		if (masksAreMinterms) {

			// ako je u makstermima sigurno je FALSE
			if (!Collections.disjoint(maskIndex, otherIndexList)) {
				return BooleanValue.FALSE;
			}

			// ako je u DONT CARE, a nije u mintermima, vrati DONT CARE
			if (!Collections.disjoint(maskIndex, dontCareIndexList)
					&& Collections.disjoint(maskIndex, indexList)) {
				return BooleanValue.DONT_CARE;
			}

			// za sve ostalo vrati TRUE
			return BooleanValue.TRUE;
		}

		// ako indexi predstavljaju maksterme
		else {

			// ako je u mintermima, sigurno je TRUE
			if (!Collections.disjoint(maskIndex, otherIndexList)) {
				return BooleanValue.TRUE;
			}

			// ako je u DONT CARE, a nije u makstermima, vrati DONT CARE
			if (!Collections.disjoint(maskIndex, dontCareIndexList)
					&& Collections.disjoint(maskIndex, indexList)) {
				return BooleanValue.DONT_CARE;
			}

			// za sve ostalo vrati FALSE
			return BooleanValue.FALSE;
		}
	}

	/**
	 * Metoda vraca domenu, tj. varijable funkcije.
	 * 
	 * @return Lista varijabli.
	 */
	public List<BooleanVariable> getDomain() {
		return new ArrayList<BooleanVariable>(this.domain);
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni minterm.
	 * 
	 * @param index
	 *            Indeks trazenog minterma.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni minterm.
	 */
	public boolean hasMinterm(int index) {

		// ako su predane maske mintermi
		if (masksAreMinterms) {
			return this.indexList.contains(new Integer(index));
		}

		// inace, provjeri u listi ostalih indeksa
		else {
			return otherIndexList.contains(new Integer(index));
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

		// ako su predane maske makstermi
		if (!masksAreMinterms) {
			return this.indexList.contains(new Integer(index));
		}

		// inace, provjeri u listi ostalih indeksa
		else {
			return otherIndexList.contains(new Integer(index));
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
		return this.dontCareIndexList.contains(new Integer(index));
	}

	/**
	 * Metoda vraca iterabilnu listu minterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> mintermIterable() {

		// ako su maske mintermi, vrati njih kao listu
		if (masksAreMinterms) {
			return new ArrayList<Integer>(this.indexList);
		}

		// inace, vrati other indexes
		else {
			return new ArrayList<Integer>(otherIndexList);
		}
	}

	/**
	 * Metoda vraca iterabilnu listu maksterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> maxtermIterable() {

		// ako su maske makstermi, vrati njih kao listu
		if (!masksAreMinterms) {
			return new ArrayList<Integer>(this.indexList);
		}

		// inace, vrati other indexes
		else {
			return new ArrayList<Integer>(otherIndexList);
		}
	}

	/**
	 * Metoda vraca iterabilnu listu dont careova funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> dontCareIterable() {
		return new ArrayList<Integer>(this.dontCareIndexList);
	}

	/**
	 * Kreira listu komplementarnih indeksa (ako je primio minterme, kreira
	 * maksterme, a ako je primio maksterme, kreira minterme).
	 * 
	 * @return lista komplementarnih indeksa
	 */
	private List<Integer> createOtherIndexes() {
		Set<Integer> indexes = new HashSet<Integer>(indexList);
		Set<Integer> dontCares = new HashSet<Integer>(dontCareIndexList);
		List<Integer> otherIndexes = new ArrayList<Integer>();
		for (int i = 0, length = (int) Math.pow(2, this.domain.size()); i < length; i++) {
			Integer index = new Integer(i);
			if (!indexes.contains(index) && !dontCares.contains(index)) {
				otherIndexes.add(index);
			}
		}
		return otherIndexes;
	}

	/**
	 * Kreira sve moguce indekse nad domenom ove funkcije.
	 * 
	 * @return lista svih mogucih implikanata
	 */
	private List<Integer> createDomainIndexes() {
		List<Integer> listaImplikanata = new ArrayList<Integer>();
		for (int i = 0, length = (int) Math.pow(2, domain.size()); i < length; i++) {
			listaImplikanata.add(new Integer(i));
		}
		return listaImplikanata;
	}

	/**
	 * Metoda vraca listu integera koji predstavljaju indekse kreirane iz maski
	 * argumenta.
	 * 
	 * @param maskList
	 *            Lista maski iz kojeg zelimo kreirati listu indexa.
	 * @return Lista integera.
	 */
	private List<Integer> createIndexList(List<Mask> maskList) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (int i = 0, length = maskList.size(); i < length; i++) {
			indexList.addAll(createIndexesFromMask(maskList.get(i)));
		}
		return indexList;
	}

	/**
	 * Prima masku i listu u koju treba pohraniti sve indekse koje ta maska
	 * sacinjava.
	 * 
	 * @param indexList
	 *            lista u koju se indeksi pohranjuju
	 * @param maska
	 *            maska iz koje se rade indeksi
	 */
	private List<Integer> createIndexesFromMask(Mask maska) {
		List<Integer> indexList = new ArrayList<Integer>();
		int domainSize = domain.size();
		for (int i = 0, length = domainIndexes.size(); i < length; i++) {
			Mask pom = Mask.fromIndex(false, domainSize, i);
			if (maska.isMoreGeneralThan(pom) || maska.equals(pom)) {
				indexList.add(new Integer(i));
			}
		}
		return indexList;
	}

	/**
	 * Provjerava jesu li domene varijabli i maska uskladjene. Provjerava je li
	 * unesen element izvan domene broja varijabli funkcija i preklapaju li se
	 * vrijednosti minterma/maksterma i dont careova.
	 * 
	 */
	private void checkDomain() {

		// velicina domene varijabli
		int size = getDomain().size();

		// pomocna varijabla tipa Mask
		Mask pom;

		/*
		 * prodji kroz sve maske i dont careove, ako pronadjes bar 1
		 * nesukladnost, baci exception
		 */
		for (int i = 0, length = masks.size(); i < length; i++) {
			pom = masks.get(i);
			if (pom.getSize() != size) {
				throw new IllegalArgumentException("Domain on variables and "
						+ "function should be the same!");
			}
		}

		for (int i = 0, length = dontCareMasks.size(); i < length; i++) {
			pom = dontCareMasks.get(i);
			if (pom.getSize() != size) {
				throw new IllegalArgumentException("Domain on variables and "
						+ "function should be the same!");
			}
		}

		// ako lista indeksa ima koji zajednicki element s listom dont care
		// indeksa, exception
		if (!Collections.disjoint(indexList, dontCareIndexList)) {
			throw new IllegalArgumentException(
					"Same mask in dont care and minterm/maxterm!");
		}
	}

}
