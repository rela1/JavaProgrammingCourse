package hr.fer.zemris.linearna;

import java.util.Locale;

/**
 * Apstraktna implementacija sucelja IVector implementira neke od propisanih metoda koje nisu 
 * specificne za pojedine ostvarene konkretne implementacije vektora, dok te specificne metode 
 * ostavlja apstraktnima i prepusta konkretnim razredima koji nasljeduju AbstractVector da ih
 * implementiraju.
 * 
 * @author Ivan Relic
 *
 */
public abstract class AbstractVector implements IVector {

	/**
	 * Metoda dohvaca zadanu komponentu vektora.
	 * @param index indeks komponente; kreće se od 0 pa do dimension-1
	 * @return trazenu vrijednost ili baca iznimku ako je indeks izvan dozvoljenih granica
	 * @throws IndexOutOfBoundsException ako se zatraži nepostojeći element
	 */
	public abstract double get(int index) throws IndexOutOfBoundsException;

	/**
	 * Postavlja zadanu komponentu na vrijednost koja je predana.
	 * @param index indeks komponente; kreće od 0 pa do dimension-1
	 * @param value vrijednost na koju treba postaviti komponentu
	 * @return referencu na trenutni vektor
	 * @throws UnmodifiableObjectException ako je vektor nepromjenjiv (read-only)
	 */
	public abstract IVector set(int index, double value) throws UnmodifiableObjectException;

	/**
	 * Metoda vraca dimenzionalnost vektora.
	 * @return dimenzionalnost
	 */
	public abstract int getDimension();

	/**
	 * Vraca novu kopiju trenutnog vektora koja se moze slobodno mijenjati
	 * i koja nije povezana s originalnim vektorom.
	 * @return referencu na novu kopiju vektora
	 */
	public abstract IVector copy();

	/**
	 * Vraca novi vektor cija je dimenzionalnost jednaka n (argument metode).
	 * Pri tome se iz trenutnog vektora u novi kopira onoliko elemenata
	 * koliko to 'n' dopusti. Ako je novi vektor vece dimenzionalnosti,
	 * ostatak se inicijalizira na vrijednost 0.
	 * @return referencu na novi vektor
	 */
	public IVector copyPart(int n) {
		
		//ako je predan negativan broj, baci exception
		if (n < 0) {
			throw new IncompatibleOperandException("Argument should be greater or equal to 0!");
		}
		
		//kreiraj novi vektor iste implementacije kao onaj nad kojim je pozvana metoda
		IVector novi = this.newInstance(n);
		
		//ako je n veci od velicine vektora nad kojim se poziva, iskoristi dimenziju vektora nad
		//kojim se poziva, inace iskoristi n
		for (int i = 0, length = Math.min(this.getDimension(), n); i < length; i++) {
			novi.set(i, this.get(i));
		}
		
		//vrati novi vektor
		return novi;
	}

	/**
	 * Metoda vraća novi primjerak vektora zadane dimenzije. Vektor ima svoj
	 * zaseban spremnik vrijednosti komponenata i ni na koji način nije
	 * povezan s trenutnim vektorom (osim što dijeli logiku koja odlučuje
	 * koji će se konkretni razred koristiti za taj novi vektor).
	 * 
	 * @param dimension dimenzionalnost vektora
	 * @return referenca na novi vektor
	 */
	public abstract IVector newInstance(int dimension);

	/**
	 * Trenutni vektor modificira tako sto mu dodaje predani vektor.
	 * @param other vektor za koji se treba uvecati trenutni vektor
	 * @return referencu na trenutni vektor
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim ili je
	 * null
	 */
	public IVector add(IVector other) throws IncompatibleOperandException {
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako su vektori razlicitih dimenzija, baci exception
		if (!(this.getDimension() == other.getDimension())) {
			throw new IncompatibleOperandException("Vectors should be same dimensions!");
		}
		
		//prodji kroz sve elemente vektora i pribroji mu vrijednosti predanog 
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			this.set(i, this.get(i) + other.get(i));
		}
		
		//vrati referencu na sebe
		return this;
	}

	/**
	 * Vraca novi vektor koji je jednak trenutnom vektoru uvecanom za predani vektor.
	 * @param other vektor za koji se treba uvecati trenutni vektor
	 * @return referencu na novi vektor koji predstavlja sumu
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim ili je
	 * null
	 */
	public IVector nAdd(IVector other) throws IncompatibleOperandException {
		
		//vrati kopiju vektora nad kojim se poziva metoda i uvecaj ga za primljeni
		return this.copy().add(other);
	}

	/**
	 * Trenutni vektor modificira tako sto mu oduzima predani vektor.
	 * @param other vektor za koji se treba umanjiti trenutni vektor
	 * @return referencu na trenutni vektor
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim ili 
	 * je null
	 */
	public IVector sub(IVector other) throws IncompatibleOperandException {
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako su vektori razlicitih dimenzija, baci exception
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException("Vectors should be same dimensions!");
		}
		
		//prodji kroz sve elemente vektora i oduzmi mu vrijednosti predanog 
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			this.set(i, this.get(i) - other.get(i));
		}
		
		//vrati referencu na sebe
		return this;
	}

	/**
	 * Vraca novi vektor koji je jednak trenutnom vektoru umanjenom za predani vektor.
	 * @param other vektor za koji se treba umanjiti trenutni vektor
	 * @return referencu na novi vektor koji predstavlja razliku
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim ili je
	 * null
	 */
	public IVector nSub(IVector other) throws IncompatibleOperandException {
		
		//vrati kopiju vektora nad kojim se poziva metoda i umanji ga za primljeni
		return this.copy().sub(other);
	}

	/**
	 * Trenutni vektor modificira tako sto ga skalarno mnozi sa zadanim skalarom.
	 * @param byValue vrijednost skalara
	 * @return referencu na trenutni vektor
	 */
	public IVector scalarMultiply(double byValue) {
		
		//prodji kroz sve elemente vektora i pomnozi ga sa predanim skalarom 
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			this.set(i, this.get(i) * byValue);
		}
		
		//vrati referencu na sebe
		return this;
	}

	/**
	 * Vraca novi vektor koji je jednak trenutnom koji je pomnozen sa
	 * zadanim skalarom.
	 * @param byValue vrijednost skalara
	 * @return referencu na novi vektor
	 */
	public IVector nScalarMultiply(double byValue) {
		
		//vrati kopiju vektora nad kojim se poziva metoda pomnozenog sa skalarom
		return this.copy().scalarMultiply(byValue);
	}

	/**
	 * Metoda vraca normu trenutnog vektora.
	 * @return normu
	 */
	public double norm() {
		
		//prodji kroz sve elemente vektora i zbroji kvadrate tih vrijednosti
		double sum = 0;
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			sum += Math.pow(this.get(i), 2);
		}
		
		//vrati korijen sume kvadrata elemenata vektora
		return Math.sqrt(sum);
	}

	/**
	 * Metoda normalizira trenutni vektor.
	 * @return referencu na trenutni vektor
	 */
	public IVector normalize() {
		
		//ako je vektor nul-vektor, baci exception
		if (this.getDimension() == 0) {
			throw new IncompatibleOperandException("Can't normalize null-vector!");
		}
		
		//prodji svim elementima vektora i normaliziraj ih (podijeli s normom)
		double norma = this.norm();
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			this.set(i, this.get(i) / norma);
		}
		
		//vrati referencu na sebe
		return this;
	}

	/**
	 * Metoda vraca novi vektor koji je jednak normaliziranom trenutnom
	 * vektoru.
	 * @return referencu na novi vektor
	 */
	public IVector nNormalize() throws IncompatibleOperandException {
		
		//vrati normaliziranu kopiju vektora nad kojim se poziva metoda
		return this.copy().normalize();
	}

	/**
	 * Metoda računa i vraća kosinus kuta između trenutnog vektora i
	 * predanog vektora.
	 * @param other drugi vektor
	 * @return kosinus pripadnog kuta
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim 
	 */
	public double cosine(IVector other) throws IncompatibleOperandException {
		
		//vrati vrijednost skalarnog produkta vektora nad kojim se zove i vektora koji je arugment
		//podijeljenu s normama ta dva vektora
		
		//kompatibilnost vektora je provjerena u metodi scalarProduct
		return (this.scalarProduct(other) / (this.norm() * other.norm()));
	}

	/**
	 * Metoda racuna skalarni produkt trenutnog vektora i zadanog vektora.
	 * @param other vektor s kojim se trenutni mnozi
	 * @return vrijednost skalarnog produkta
	 * @throws IncompatibleOperandException ako predani vektor nije kompatibilan s trenutnim
	 */
	public double scalarProduct(IVector other)
			throws IncompatibleOperandException {		
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako su vektori razlicitih dimenzija, baci exception
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException("Vectors should be same dimensions!");
		}
		
		//prodji kroz sve elemente vektora i mnozi ih skalarno 
		double scalarProduct = 0;
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			scalarProduct += this.get(i) * other.get(i);
		}
		
		//vrati iznos skalarnog produkta
		return scalarProduct;
	}

	/**
	 * Metoda vraca novi vektor koji je jednak vektorskom produktu
	 * izmedu trenutnog vektora i zadanog vektora.
	 * @param other vektor s kojim se racuna vektorski produkt
	 * @return referencu na novi vektor
	 * @throws IncompatibleOperandException ako dimenzionalnost trenutnog ili predanog vektora
	 *         nije 3, ili je predan null
	 */
	public IVector nVectorProduct(IVector other) throws IncompatibleOperandException {
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako oba vektora nisu dimenzije 3, baci exception
		if (this.getDimension() != 3 || other.getDimension() != 3) {
			throw new IncompatibleOperandException("Vectors should be dimension 3!");
		}
		
		//kreiraj komponente novog vektora (determinanta matrice kojoj je prvi redak i, j, k - 
		//jedinicni vektori, a druga dva retka su vrijednosti elemenata vektora)
		double i = this.get(1) * other.get(2) - this.get(2) * other.get(1);
		double j = this.get(0) * other.get(2) - this.get(2) * other.get(0);
		double k = this.get(0) * other.get(1) - this.get(1) * other.get(0);
		
		//kreiraj novi vektor koji je vektorski produkt ova dva vektora
		IVector novi = this.newInstance(3);
		novi.set(0, i);
		novi.set(1, j);
		novi.set(2, k);
		
		return novi;
	}

	/**
	 * Vraca vektor u radnom prostoru koji se dobije iz trenutnog ako se
	 * trenutni promatra kao da je u homogenom prostoru. To ce biti
	 * vektor za jedan manje dimenzionalnosti od trenutnog kod kojeg su
	 * sve komponente jednake trenutnim podijeljenim sa zadnjom.
	 * @return referencu na novi vektor
	 * @throws IncompatibleOperandException ako je predan vektor dimenzionalnosti 1 
	 */
	public IVector nFromHomogeneus() throws IncompatibleOperandException {
		
		//ako se metoda poziva nad vektorom dimenzionalnosti 1
		if (this.getDimension() == 1) {
			throw new IncompatibleOperandException("Vector dimension should be greater than 1!");
		}
		
		//inace, dohvati zadnji element vektora nad kojim se poziva metoda
		double lastElement = this.get(this.getDimension() - 1);
		
		//kreiraj kopiju vektora nad kojim se poziva metoda dimenzionalnosti n-1
		IVector novi = this.copyPart(this.getDimension() - 1);
		
		//pomnozi novi skalarno s vrijednosti 1/lastElement i vrati ga
		return novi.scalarMultiply(1 / lastElement);
	}

	/**
	 * Trenutni vektor konvertira u jednoretcanu matricu. Ako je liveView postavljen
	 * na true, vraca objekt koji je zivi pogled na taj vektor -- primjerice, ako se
	 * u vektoru promijeni neka komponenta, i ovaj pogled ce toga biti svjestan; ako
	 * korisnik preko dobivene matrice napravi promjenu, ta ce promjena biti vidljiva
	 * i u vektoru. Ako je liveView postavljen na false, generirana matrica cuva
	 * kopiju izvornih podataka.
	 *  
	 * @param liveView treba li vratiti zivi pogled
	 * @return matrica dobivena temeljem trenutnog vektora
	 */
	public IMatrix toRowMatrix(boolean liveView) {
		
		//ako je liveView true, posalji samo referencu na ovaj vektor, inace posalji referencu na
		//novu kopiju ovog vektora koja nema veze s trenutnim
		return new MatrixVectorView((liveView == true) ? this : this.copy(), true);
	}

	/**
	 * Trenutni vektor konvertira u jednostupcanu matricu. Ako je liveView postavljen
	 * na true, vraca objekt koji je zivi pogled na taj vektor -- primjerice, ako se
	 * u vektoru promijeni neka komponenta, i ovaj pogled ce toga biti svjestan; ako
	 * korisnik preko dobivene matrice napravi promjenu, ta ce promjena biti vidljiva
	 * i u vektoru. Ako je liveView postavljen na false, generirana matrica cuva
	 * kopiju izvornih podataka.
	 *  
	 * @param liveView treba li vratiti zivi pogled
	 * @return matrica dobivena temeljem trenutnog vektora
	 */
	public IMatrix toColumnMatrix(boolean liveView) {
		
		//ako je liveView true, posalji samo referencu na ovaj vektor, inace posalji referencu na
		//novu kopiju ovog vektora koja nema veze s trenutnim
		return new MatrixVectorView((liveView == true) ? this : this.copy(), false);
	}

	/**
	 * Vraca kopiju vektora predstavljenog kao polje.
	 * @return polje
	 */
	public double[] toArray() {
		
		//kreiraj novo polje velicine dimenzije vektora
		double[] array = new double[this.getDimension()];
		
		//prodji svim elementima vektora i kopiraj ih u polje
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			array[i] = this.get(i);
		}
		
		//vrati polje
		return array;
	}
	
	/**
	 * Metoda vraca vektor u string zapisu s onoliko znamenaka iza decimalne tocke koliko je 
	 * predano kao argument.
	 * 
	 * @param precision broj znamenaka iza decimalne tocke, precisnost ispisa
	 * @return string prikaz vektora
	 */
	public String toString(int precision) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		
		//prodji svim komponentama vektora i ispisi ih u zadanoj preciznosti
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			builder.append(String.format(Locale.ENGLISH, "%." + precision + "f", this.get(i)));
			builder.append(String.format(Locale.ENGLISH, (i == length - 1) ? ")" : ", "));
		}

		return builder.toString();
	}
	
	/**
	 * Metoda vraca vektor u string zapisu s po 3 znamenke iza decimalne tocke. 
	 */
	public String toString() {
		return this.toString(3);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		//ako se usporedjujes sa samim sobom, vrati true
		if (this == obj)
		    return true;
		
		//ako se usporedjujes s null, vrati false
		if (obj == null)
		    return false;
		
		//ako se usporedjujes s necim sto nije instanca IVectora, vrati false
		if (!(obj instanceof IVector))
		    return false;
		
		//dereferenciraj ono s cim se usporedjujes na IVector i usporedi elemente
		IVector other = (Vector) obj;	
		
		//ako su razlicitih dimenzija sigurno nisu isti
		if (this.getDimension() != other.getDimension()) {
			return false;
		}
		
		//vrati false ako se razlikuju i u jednom elementu
		for (int i = 0, length = this.getDimension(); i < length; i++) {
			if (Math.abs(Math.abs(this.get(i)) - Math.abs(other.get(i))) > 1E-6) {
				return false;
			}
		}
		
		return true;		
	}
	
	@Override
	public int hashCode() {
		return this.toArray().hashCode();
	}
}
