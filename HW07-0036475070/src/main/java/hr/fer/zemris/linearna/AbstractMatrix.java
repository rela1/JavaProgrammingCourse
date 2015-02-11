package hr.fer.zemris.linearna;

import java.util.Locale;

/**
 * Apstraktna implementacija sucelja IMatrix implementira neke od propisanih metoda koje nisu 
 * specificne za pojedine ostvarene konkretne implementacije matrica, dok te specificne metode 
 * ostavlja apstraktnima i prepusta konkretnim razredima koji nasljeduju AbstractMatrix da ih
 * implementiraju.
 * 
 * @author Ivan Relic
 *
 */
public abstract class AbstractMatrix implements IMatrix {

	/**
	 * Metoda za dohvat broja redaka matrice.
	 * 
	 * @return broj redaka
	 */
	public abstract int getRowsCount();

	/**
	 * Metoda za dohvat broja stupaca matrice.
	 * 
	 * @return broj stupaca
	 */
	public abstract int getColsCount();

	/**
	 * Metoda za dohvat elementa matrice u zadanom retku i stupcu.
	 * 
	 * @param row redak, ide od 0 na vise
	 * @param col stupac, ide od 0 na vise
	 * @return trazeni element
	 */
	public abstract double get(int row, int col);

	/**
	 * Metoda za postavljanje vrijednosti u zadani redak i stupac.
	 * 
	 * @param row redak, ide od 0 na vise
	 * @param col stupac, ide od 0 na vise
	 * @param value vrijednost koju treba postaviti
	 * @return referencu na matricu
	 */
	public abstract IMatrix set(int row, int col, double value);

	/**
	 * Metoda za vracanje nove kopije trenutne matrice.
	 * 
	 * @return referencu na novu kopiju koja nije nikako
	 *         povezana s izvornom matricom.
	 */
	public abstract IMatrix copy();

	/**
	 * Metoda za stvaranja nove matrice zadanih dimenzija.
	 * 
	 * @param rows zeljeni broj redaka
	 * @param cols zeljeni broj stupaca
	 * @return referenca na novu matricu
	 */
	public abstract IMatrix newInstance(int rows, int cols);

	/**
	 * Metoda omogucava stvaranje transponirane matrice u odnosu
	 * na originalnu matricu. Ako je parametar liveView postavljen
	 * na true, objekt koji se vrati mora biti "zivi" pogled na
	 * originalnu matricu. U suprotnom stvara se nova matrica koja
	 * cuva vlastitu kopiju podataka.
	 * 
	 * @param liveView zeli li se dobiti zivi pogled
	 * @return referenca na transponiranu matricu
	 */
	public IMatrix nTranspose(boolean liveView) {
		
		//ako je zatrazen zivi pogled kreiraj novu TransposeView matricu sa zivom referencom na
		//ovu matricu, inace kreiraj kopiju ove matrice i posalji referencu na nju
		return new MatrixTransposeView((liveView) ? this : this.copy());
	}

	/**
	 * Metoda trenutnoj matrici dodaje zadanu matricu (provodi
	 * operaciju zbrajanja); pri tome se originalna matrica direktno
	 * mijenja. 
	 * 
	 * @param other matrica koju treba pribrojiti
	 * @return referenca na trenutnu matricu
	 */
	public IMatrix add(IMatrix other) {
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako matrice nisu istih dimenzija redaka i stupaca, baci exception
		if (this.getColsCount() != other.getColsCount() ||
				this.getRowsCount() != other.getRowsCount()) {
			throw new IncompatibleOperandException("Matrixes are different in size!");
		}
		
		//inace, prodji kroz elemente matrica i nadodaj ih na trenutnu
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				this.set(i, j, this.get(i, j) + other.get(i, j));
			}
		}
		
		return this;
	}

	/**
	 * Metoda stvara novu matricu koja odgovara zbroju trenutne matrice
	 * i predane matrice.
	 * 
	 * @param other matrica koju treba pribrojiti
	 * @return referenca na novu matricu
	 */
	public IMatrix nAdd(IMatrix other) {
		
		//kreiraj kopiju originalne matrice i na nju nadodaj other i vrati je
		return this.copy().add(other);
	}

	/**
	 * Metoda od trenutne matrice oduzima zadanu matricu (provodi
	 * operaciju odbijanja); pri tome se originalna matrica direktno
	 * mijenja. 
	 * 
	 * @param other matrica koju treba odbiti
	 * @return referenca na trenutnu matricu
	 */
	public IMatrix sub(IMatrix other) {
		
		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako matrice nisu istih dimenzija redaka i stupaca, baci exception
		if (this.getColsCount() != other.getColsCount() ||
				this.getRowsCount() != other.getRowsCount()) {
			throw new IncompatibleOperandException("Matrixes are different in size!");
		}
		
		//inace, prodji kroz elemente matrica i oduzmi other od trenutne
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				this.set(i, j, this.get(i, j) - other.get(i, j));
			}
		}
		
		return this;
	}

	/**
	 * Metoda stvara novu matricu koja odgovara razlici trenutne matrice
	 * i predane matrice.
	 * 
	 * @param other matrica koju treba odbiti
	 * @return referenca na novu matricu
	 */
	public IMatrix nSub(IMatrix other) {
		
		//kreiraj kopiju originalne matrice od nje oduzmi other i vrati je
		return this.copy().sub(other);
	}

	/**
	 * Metoda stvara novu matricu koja odgovara matricnom umnosku trenutne 
	 * matrice i predane matrice.
	 * 
	 * @param other matrica s kojom treba pomnoziti trenutnu
	 * @return referenca na novu matricu
	 */
	public IMatrix nMultiply(IMatrix other) {

		//ako je predan null, baci exception
		if (other == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}
		
		//ako matrice nisu ulancane, baci exception ([a,b] X [b,c])
		if (this.getColsCount() != other.getRowsCount()) {
			throw new IncompatibleOperandException("Matrixes should be linked!");
		}
		
		//kreiraj novu matricu velicine [retciOriginalne, stupciMnožitelja], koristi se LinAlg
		//zbog toga jer je moguce mnoziti MatrixVectorView, a kod njih je newInstance definiran
		//tako da ponovno napravi MatrixVector view koji moze prikazivati samo vektor
		//sto nam u slucaju da dobijemo matricu dimenzija razlicitih od one koja se moze prikazati
		//kao vektor ne odgovara
		IMatrix multiplied = LinAlgDefaults.defaultMatrix(this.getRowsCount(), other.getColsCount());
		
		//popuni matricu umnoska
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = other.getColsCount(); j < cols; j++) {
				
				//mnozi i pribrajaj u value vrijednosti i-tog reda originalne s j-tim stupcem
				//matrice s kojom se mnozi
				double value = 0;
				for (int k = 0, length = this.getColsCount(); k < length; k++) {
					value += this.get(i, k) * other.get(k, j);
				}
				
				//postavi dobivenu vrijednost u matricu umnoska
				multiplied.set(i, j, value);
			}
		}
		
		//vrati matricu umnoska
		return multiplied;		
	}

	/**
	 * Metoda racuna determinantu trenutne matrice.
	 * @return determinantu
	 * @throws IncompatibleOperandException ako matrica nije kvadratna
	 */
	public double determinant() throws IncompatibleOperandException {
		
		//ako matrica nema isti broj redaka i stupaca baci exception
		if (this.getRowsCount() != this.getColsCount()) {
			throw new IncompatibleOperandException("Matrix should be square matrix!");
		}
		
		//ako je matrica dimenzija 1*1 vrati vrijednost [0,0]
		if (this.getRowsCount() == 1) {
			return this.get(0, 0);
		}
		
		//ako je matrica dimenzija 2*2, vrati vrijednost [0,0]*[1,1] - [0,1]*[1,0]
		if (this.getRowsCount() == 2) {
			return ((this.get(0, 0) * this.get(1, 1)) - (this.get(0, 1) * this.get(1, 0)));
		}
		
		//inace, racunaj determinantu rekurzivno
		double determinant = 0;
		for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
			
			determinant += (
					this.get(0, j) 
					* Math.pow(-1, 0+j) 
					* (new MatrixSubMatrixView(this, 0, j)).determinant()
					);
		}
		
		//vrati vrijednost determinante izracunate rekurzivno
		return determinant;
	}

	/**
	 * Metoda vraca matricu koja odgovara trenutnoj matrici nakon izbacivanja
	 * zadanog retka i zadanog stupca (oboje se numerira od 0). Ta nova matrica
	 * imat ce za jedan manje broj redaka i za jedan manje broj stupaca u odnosu
	 * na trenutnu matricu. Ako je parametar liveView postavljen na true,
	 * metoda mora vratiti zivi pogled na izvornu matricu; to znaci da, primjerice,
	 * ako u ovom pogledu izmjenimo neki element, promjena ce se vidjeti i u
	 * originalnoj matrici na odgovarajucem mjestu. Ako je liveView postavljen
	 * na false, stvara se nova matrica koja ima svoju vlastitu kopiju podataka.
	 * 
	 * @param row redak koji treba izbaciti
	 * @param col stupac koji treba izbaciti
	 * @param liveView zeli li se dobiti zivi pogled
	 * @return matricu koja predstavlja podmatricu
	 */
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		
		//ako je row ili col nedozvoljen, baci exception
		if (row < 0 || row > this.getRowsCount() - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.getRowsCount() - 1) + "] interval!");
		}
		if (col < 0 || col > this.getColsCount() - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.getColsCount() - 1) + "] interval!");
		}
		
		//ako je zatrazen zivi pogled kreiraj novu MatrixSubMatrixView matricu sa zivom referencom
		//na ovu matricu, inace kreiraj kopiju ove matrice i posalji referencu na nju
		return new MatrixSubMatrixView((liveView) ? this : this.copy(), row, col);
	}

	/**
	 * Metoda provodi postupak invertiranja zadane matrice i vraca novu matricu
	 * koja je inverz. Inverz se izracunava Cramerovom metodom.
	 * @see <a> href="Cramer's rule">http://en.wikipedia.org/wiki/Cramer%27s_rule</a>
	 * U slucaju da matrica nije invertibilna, ocekuje se da ce biti izazvana
	 * {@link UnsupportedOperationException}.
	 * 
	 * @return referencu na novu matricu koja je inverz trenutne
	 */
	public IMatrix nInvert() {
		
		try {
			//ako je determinanta matrice 0, baci exception
			if (this.determinant() == 0) {
				throw new UnsupportedOperationException("Matrix doesn't have inverse! It's "
						+ "determinant is 0!");
			}
		} catch (IncompatibleOperandException e) {
			
			//ako ulovis exception iz prilikom trazenja determinante, znaci da matrica nije
			//kvadratna
			throw new UnsupportedOperationException("Matrix doesn't have inverse! It should be"
					+ "square matrix!");
		}
		
		//kreiraj matricu kofaktora (na mjestima i,j ima determinantu originalne matrice koja se
		//dobije kad se iz originalne matrice izbace i-ti redak i j-ti stupac)
		IMatrix cofactorMatrix = this.newInstance(this.getRowsCount(), this.getColsCount());
		for (int i = 0, rows = cofactorMatrix.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = cofactorMatrix.getColsCount(); j < cols; j++) {
				cofactorMatrix.set(i, j, 
						Math.pow(-1, i + j) * this.subMatrix(i, j, true).determinant());
			}
		}
		
		//transponiraj matricu kofaktora
		cofactorMatrix = cofactorMatrix.nTranspose(true);
		
		//inverzna matrica je transponirana matrica kofaktora skalarno podijeljena sa determinantom
		//originalne matrice		
		return cofactorMatrix.scalarMultiply(1.0 / this.determinant());		
	}

	/**
	 * Sadrzaj trenutne matrice kopira u dvodimenzijsko polje koje potom vraca.
	 * 
	 * @return polje s kopijom sadrzaja matrice
	 */
	public double[][] toArray() {
		
		//kreiraj novo polje velicine polja iz matrice
		double[][] copyElements = new double[this.getRowsCount()][this.getColsCount()];
		
		//prekopiraj elemente u copyElements
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				copyElements[i][j] = this.get(i, j);
			}
		}
		
		//vrati dvodimenzijsko polje
		return copyElements;
	}

	/**
	 * Temeljem trenutne matrice stvara vektor. Ovo je dakako legalno samo ako
	 * je matrica jednoretcana ili jednostupcana. Ako je liveView postavljen na
	 * true, treba se vratiti zivi pogled na trenutnu matricu. Ako je liveView
	 * postavljen na false, treba se vratiti referenca na vektor koji cuva svoju
	 * kopiju podataka.
	 * 
	 * @param liveView zeli li se dobiti zivi pogled
	 * @return referenca na odgovarajuci vektor
	 */
	public IVector toVector(boolean liveView) {
		
		//ako matrica ima 1 redak ili stupac, kreiraj vektor iz nje, inace baci exception jer se
		//iz nje ne moze kreirati vektor
		if (this.getColsCount() == 1 || this.getRowsCount() == 1) {
			return new VectorMatrixView(liveView ? this : this.copy());
		}
		else {
			throw new IncompatibleOperandException("Cannot create vector from this matrix!");
		}
	}

	/**
	 * Vraća novu matricu čiji su svi elementi jednaki elementima trenutne matrice
	 * pomnoženima s zadanom vrijednosti.
	 * 
	 * @param value vrijednost s kojom se množe svi elementi
	 * @return novu matricu
	 */
	public IMatrix nScalarMultiply(double value) {
		
		//vrati kopiju ove matrice pomnozenu sa skalarom
		return this.copy().scalarMultiply(value);
	}

	/**
	 * Sve elemente trenutne matrice množi sa zadanom vrijednostima.
	 * Vraća referencu na trenutnu matricu.
	 * 
	 * @param value vrijednost s kojom se množe svi elementi
	 * @return referencu na trenutnu matricu
	 */
	public IMatrix scalarMultiply(double value) {

		// prodji kroz elemente matrice i pomnozi sa skalarom
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				this.set(i, j, this.get(i, j) * value);
			}
		}
		
		//vrati referencu na ovu matricu
		return this;
	}

	/**
	 * Modificira trenutnu matricu tako da postaje jedinična matrica.
	 * 
	 * @return referencu na trenutnu matricu
	 */
	public IMatrix makeIdentity() {
		
		//ako matrica nije kvadratna, baci exception
		if (this.getColsCount() != this.getRowsCount()) {
			throw new IncompatibleOperandException("Matrix should be square matrix!");
		}
		
		//prodji kroz sve elemente matrice i one na glavnoj dijagonali postavi na 1, ostale na 0
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				this.set(i, j, (i == j) ? 1 : 0);
			}
		}
		
		//vrati referencu na trenutnu matricu
		return this;
	}
	
	/**
	 * Metoda vraca matricu u string zapisu s onoliko znamenaka iza decimalne tocke koliko je 
	 * predano kao argument.
	 * 
	 * @param precision broj znamenaka iza decimalne tocke, precisnost ispisa
	 * @return string prikaz matrice
	 */
	public String toString(int precision) {
		
		StringBuilder builder = new StringBuilder();
		
		//prodji svim komponentama vektora i ispisi ih u zadanoj preciznosti
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			builder.append("[");
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				builder.append(String.format(Locale.ENGLISH, "%." + precision + "f", this.get(i, j)));
				builder.append(String.format(Locale.ENGLISH, (j == cols - 1) ? "]" : ", "));
			}
			builder.append((i < rows - 1) ? "\n" : "");
		}
		
		return builder.toString();
	}
	
	/**
	 * Metoda vraca matricu u string zapisu s po 3 znamenke iza decimalne tocke. 
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
		
		//ako se usporedjujes s necim sto nije instanca IMatrixa, vrati false
		if (!(obj instanceof IMatrix))
		    return false;
		
		//dereferenciraj ono s cim se usporedjujes na IMatrix i usporedi elemente
		IMatrix other = (IMatrix) obj;	
		
		//ako su razlicitih dimenzija sigurno nisu isti
		if ((this.getColsCount() != other.getColsCount()) || 
				this.getRowsCount() != other.getRowsCount()) {
			return false;
		}
		
		//vrati false ako se razlikuju i u jednom elementu
		for (int i = 0, rows = this.getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = this.getColsCount(); j < cols; j++) {
				if (Math.abs(Math.abs(this.get(i, j)) - Math.abs(other.get(i, j))) > 1E-6) {
					return false;
				}
			}
		}
		
		return true;		
	}
	
	@Override
	public int hashCode() {
		return this.toArray().hashCode();
	}
}
