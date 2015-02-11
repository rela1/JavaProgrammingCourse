package hr.fer.zemris.bool.fimpl;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.MaskValue;

/**
 * Klasa za generiranje objekata koji su container za kompletnu funkciju
 * baziranu na funkciji zadanoj preko operatora.
 * 
 * @author Ivan Relic
 * 
 */
public class OperatorTreeBF implements BooleanFunction {

	private String name;
	private List<BooleanVariable> domain;
	private BooleanOperator operatorTree;

	// pomocne liste za pohranu indexa mintermi, maxtermi i dont care
	// vrijednosti
	private List<Integer> mintermIndex;
	private List<Integer> maxtermIndex;
	private List<Integer> dontCareIndex;

	/**
	 * Javni konstruktor za postavljanje clanskih varijabli.
	 * 
	 * @param name
	 *            Ime funkcije.
	 * @param domain
	 *            Domena, tj. varijable.
	 * @param operatorTree
	 *            Operatorsko stablo koji predstavlja funkcijski izraz-
	 */
	public OperatorTreeBF(String name, List<BooleanVariable> domain,
			BooleanOperator operatorTree) {

		// ako je bilo koji od argumenata null, baci exception
		if (name == null || domain == null || operatorTree == null) {
			throw new IllegalArgumentException("Arguments shouldn't be null!");
		}

		this.name = name;
		this.domain = domain;
		this.operatorTree = operatorTree;

		// provjeri domene
		checkDomain();

		// alociraj memoriju listi indexa
		mintermIndex = new ArrayList<Integer>();
		maxtermIndex = new ArrayList<Integer>();
		dontCareIndex = new ArrayList<Integer>();

		// kreiraj liste indexa
		createIndexLists();
	}

	/**
	 * Metoda vraca ime funkcije.
	 * 
	 * @return String name.
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
		return this.operatorTree.getValue();
	}

	/**
	 * Metoda vraca domenu, tj. listu varijabli funkcije.
	 * 
	 * @return Lista BooleanVariable.
	 */
	public List<BooleanVariable> getDomain() {
		return new ArrayList<BooleanVariable>(this.domain);
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni minterm.
	 * 
	 * @param index
	 *            Indeks za koji zelimo provjeriti je li minterm.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni minterm.
	 */
	public boolean hasMinterm(int index) {
		return this.mintermIndex.contains(new Integer(index));
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni maksterm.
	 * 
	 * @param index
	 *            Indeks za koji zelimo provjeriti je li maksterm.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni maksterm.
	 */
	public boolean hasMaxterm(int index) {
		return this.maxtermIndex.contains(new Integer(index));
	}

	/**
	 * Metoda provjerava sadrzi li funkcija trazeni dont care.
	 * 
	 * @param index
	 *            Indeks za koji zelimo provjeriti je li dont care.
	 * @return True ili false, ako sadrzi ili ne sadrzi trazeni dont care.
	 */
	public boolean hasDontCare(int index) {
		return this.dontCareIndex.contains(new Integer(index));
	}

	/**
	 * Metoda vraca iterabilnu listu minterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> mintermIterable() {
		return new ArrayList<Integer>(this.mintermIndex);
	}

	/**
	 * Metoda vraca iterabilnu listu maksterma funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> maxtermIterable() {
		return new ArrayList<Integer>(this.maxtermIndex);
	}

	/**
	 * Metoda vraca iterabilnu listu dont careova funkcije.
	 * 
	 * @return Iterabilna lista.
	 */
	public Iterable<Integer> dontCareIterable() {
		return this.dontCareIndex;
	}

	private void createIndexLists() {

		// prodji svakim indeksom i za svaku vrijednost maske indeksa postavi
		// varijable funkcije na te vrijednosti
		int domainSize = this.domain.size();
		for (int i = 0, length = (int) Math.pow(2, domainSize); i < length; i++) {

			// prodji svim vrijednostima maske i postavi trenutne varijable na
			// vrijednosti koje
			// su u maski
			Mask maska = Mask.fromIndex(domainSize, i);
			for (int j = 0; j < domainSize; j++) {
				this.domain.get(j).setValue(
						MaskValueToVariableValue(maska.getValue(j)));
			}

			// za postavljenu vrijednost varijabli procitaj vrijednost funkcije
			// i ubaci index u neku od listi
			FunctionValueToIndex(i);
		}
	}

	/**
	 * Za neku vrijednost funkcije postavlja odgovarajuću kombinaciju u neku od
	 * listu indeksa.
	 * 
	 * @param value
	 */
	private void FunctionValueToIndex(int index) {
		BooleanValue functionValue = this.getValue();
		if (functionValue == BooleanValue.TRUE) {
			mintermIndex.add(new Integer(index));
		} else if (functionValue == BooleanValue.FALSE) {
			maxtermIndex.add(new Integer(index));
		} else {
			dontCareIndex.add(new Integer(index));
		}
	}

	/**
	 * Metoda provjerava jesu li metode operatorTree-a i funkcije legalne, tj
	 * jesu li u funkciji navedene sve varijable koje se koriste u
	 * operatorTree-u. Obrat ne treba vrijediti.
	 * 
	 */
	private void checkDomain() {

		/*
		 * ako je domena operatorTree-a drukcija od domene funkcije, tj. ako se
		 * u operatorTree pojavljuje neka varijabla koja nije deklarirana u
		 * funkciji, baci exception
		 */
		if (!this.getDomain().containsAll(operatorTree.getDomain())) {
			throw new IllegalArgumentException(
					"Operator tree and function domain " + "should be equal!");
		}
	}

	/**
	 * Prima vrijednost maske i vraca vrijednost za varijablu.
	 * 
	 * @param value
	 *            vrijednost maske
	 * @return vrijednost varijable
	 */
	private BooleanValue MaskValueToVariableValue(MaskValue value) {
		if (value == MaskValue.ONE) {
			return BooleanValue.TRUE;
		} else if (value == MaskValue.ZERO) {
			return BooleanValue.FALSE;
		} else {
			return BooleanValue.DONT_CARE;
		}
	}

}
