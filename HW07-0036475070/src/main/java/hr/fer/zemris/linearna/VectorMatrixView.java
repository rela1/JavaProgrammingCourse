package hr.fer.zemris.linearna;

/**
 * Klasa koja instancira vektore koje predstavljaju matrice, tj. vektore koje podatke vuku iz
 * matrice.
 * 
 * @author Ivan Relic
 *
 */
public class VectorMatrixView extends AbstractVector {

	private int dimension;
	private boolean rowMatrix;
	private IMatrix original;
	
	/**
	 * Javni konstruktor preuzima referencu na originalnu matricu koju cemo predstavljati kao
	 * vektor i pohranjuje je u svoju clansku varijablu.
	 * 
	 * @param original referenca na originalnu matricu
	 */
	public VectorMatrixView(IMatrix original) {
		this.original = original;
		
		//ako je broj redaka 1, postavi rowMatrix na true, ako nije, provjeri je li broj stupaca 1,
		//ako nije baci exception jer se ta matrica ne moze prikazati kao vektor
		if (original.getRowsCount() == 1) {
			this.rowMatrix = true;
		}
		else if (original.getColsCount() == 1) {
			this.rowMatrix = false;
		}
		else {
			throw new IncompatibleOperandException("Cannot look at that matrix as vector!");
		}
		
		this.dimension = this.rowMatrix ? 
				this.original.getColsCount() : this.original.getRowsCount();
	}
	
	@Override
	public double get(int index) throws IndexOutOfBoundsException {
		
		//ako je zatrazen nedozvoljen indeks, baci exception
		if (this.rowMatrix && (index < 0 || index > (original.getColsCount() - 1))) {
			throw new IndexOutOfBoundsException("Index must be in interval "
					+ "[0, " + (this.original.getColsCount() - 1) + "]");
		}
		if (!this.rowMatrix && (index < 0 || index > (original.getRowsCount() - 1))) {
			throw new IndexOutOfBoundsException("Index must be in interval "
					+ "[0, " + (this.original.getRowsCount() - 1) + "]");
		}
		
		return this.rowMatrix ? this.original.get(0, index) : this.original.get(index, 0);
	}

	@Override
	public IVector set(int index, double value) {
		
		//ako je zatrazen nedozvoljen indeks, baci exception
		if (this.rowMatrix && (index < 0 || index > (original.getColsCount() - 1))) {
			throw new IndexOutOfBoundsException("Index must be in interval "
					+ "[0, " + (this.original.getColsCount() - 1) + "]");
		}
		if (!this.rowMatrix && (index < 0 || index > (original.getRowsCount() - 1))) {
			throw new IndexOutOfBoundsException("Index must be in interval "
					+ "[0, " + (this.original.getRowsCount() - 1) + "]");
		}
		
		//postavi vrijednost u matrici koju gledas kao vektor i vrati referencu na sebe
		this.original.set(this.rowMatrix ? 0 : index, this.rowMatrix ? index : 0, value);
		return this;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		return new VectorMatrixView(this.original.copy());
	}

	@Override
	public IVector newInstance(int dimension) {
		return new VectorMatrixView(LinAlgDefaults.defaultMatrix(
				this.rowMatrix ? 1 : dimension, this.rowMatrix ? dimension : 1));
	}

}
