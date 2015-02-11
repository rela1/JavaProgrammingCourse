package hr.fer.zemris.linearna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Razred nasljeđuje AbstractVector i nudi implementacije apstraktnih metoda tog razreda za 
 * potpuno funkcioniranje konkretne implementacije vektora.
 * 
 * @author Ivan Relic
 *
 */
public class Vector extends AbstractVector {

	private double[] elements;
	private int dimension;
	private boolean readOnly;
	
	/**
	 * Javni konstruktor kreira novi vektor na temelju primljenih argumenata. Prvi argument je za
	 * postavljanje promjenjivosti vektora, ako je postavljen na true, vektor se ne može mijenjati,
	 * može ga se samo čitati. Drugi arugment je da se vektoru kaže hoće li se predano polje 
	 * elemenata izvana mijenjati ili brisati što u prijevodu znači može li vektor preuzeti samo
	 * referencu na to polje ili ga mora iskopirati. Treći argument je polje elemenata za vrijednost
	 * vektora.
	 * 
	 * @param readOnly je li vektor samo za čitanje
	 * @param arrayImmutable hoće li polje elemenata biti mijenjano ili brisano
	 * @param elements polje elemenata za vrijednosti vektora
	 */
	public Vector(boolean readOnly, boolean arrayImmutable, double[] elements) {
		this.readOnly = readOnly;
		this.dimension = elements.length;
		
		//ako korisnik garantira da se polje elemenata nece mijenjati izvana, samo preuzmi 
		//referencu inace iskopiraj to polje u svoje polje elemenata
		this.elements = (arrayImmutable ? elements : Arrays.copyOf(elements, elements.length));
	}
	
	/**
	 * Javni konstruktor kreira novi promjenjivi vektor s kopijom elemenata predanih kao argument. 
	 * 
	 * @param elements elementi s kojima zelimo inicijalizirati vektor
	 */
	public Vector(double ... elements) {
		this(false, false, elements);
	}
	
	@Override
	public double get(int index) throws IndexOutOfBoundsException {
		
		//ako je indeks izvan intervala 0 do dimension-1, baci exception
		if (index < 0  ||  index > this.dimension - 1) {
			throw new IndexOutOfBoundsException("Index should be "
					+ "in [0, " + (this.dimension - 1) + "] interval!");
		}
		
		return this.elements[index];
	}

	@Override
	public IVector set(int index, double value) throws UnmodifiableObjectException {		
		
		//ako je vektor read only, baci exception
		if (readOnly) {
			throw new UnmodifiableObjectException("Vector is read only!");
		}
		
		//ako je indeks izvan intervala 0 do dimension-1, baci exception
		if (index < 0  ||  index > this.dimension - 1) {
			throw new IndexOutOfBoundsException("Index should be "
					+ "in [0, " + (this.dimension - 1) + "] interval!");
		}
		
		//postavi vrijednost vektoru i vrati referencu na ovaj vektor
		this.elements[index] = value;
		return this;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		
		//vrati promjenjivu kopiju ovog vektora (drugi argument je false pa ce konstruktor 
		//napraviti kopiju predanih elemenata)
		return new Vector(false, false, this.elements);
	}

	@Override
	public IVector newInstance(int dimension) {
		
		//vrati novi vektor velicine n ispunjen nulama
		return new Vector(false, false, new double[dimension]);
	}
	
	/**
	 * Metoda iz stringa uzima vrijednosti elemenata i postavlja ih u vektor.
	 * 
	 * @param elements string prikaz vrijednosti elemenata razdvojenih razmacima
	 * @return novi vektor s vrijednostima predanima kao string
	 */
	public static IVector parseSimple(String elementsString) {		
		
		//ako je predan null, baci exception
		if (elementsString == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako nije predan prihvatljiv zapis elemenata vektora, baci exception
		if (!elementsString.matches("[([-]?[0-9](\\.[0-9])?)+[\\s]+]+")) {
			throw new IncompatibleOperandException("Cannot parse that string!");
		}
		
		//pospremi u listu sve double elemente iz predanog stringa
		Scanner scanner = new Scanner(elementsString);
		List<Double> pomocnaLista = new ArrayList<Double>();
		while (scanner.hasNext()) {
			pomocnaLista.add(Double.parseDouble(scanner.next()));
		}
		scanner.close();
		
		//kopiraj elemente iz liste u niz doubleova
		double[] elements = new double[pomocnaLista.size()];
		for (int i = 0, length = pomocnaLista.size(); i < length; i++) {
			elements[i] = pomocnaLista.get(i).doubleValue();
		}
		
		//vrati novi vektor s vrijednostima procitanih elemenata
		return new Vector(elements);
	}
}
