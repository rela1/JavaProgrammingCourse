package hr.fer.zemris.linearna;

/**
 * Klasa koja instancira matrice koje predstavljaju vektore, tj. matrice koje podatke vuku iz
 * vektora.
 * 
 * @author Ivan Relic
 *
 */
public class MatrixVectorView extends AbstractMatrix {

	private IVector original;
	private boolean asRowMatrix;
	
	/**
	 * Javni konstruktor prima vektor koji ce predstaviti kao jednoretcanu ili jednostupcanu 
	 * matricu, ovisno o drugom argumentu konstruktora.
	 * 
	 * @param original vektor koji ce se predstavljati kao matrica
	 * @param asRowMatrix hoce li vektor biti jednostupcana ili jednoretcana matrica
	 */
	public MatrixVectorView(IVector original, boolean asRowMatrix) {
		this.original = original;
		this.asRowMatrix = asRowMatrix;
	}
	
	@Override
	public int getRowsCount() {
		
		//ako je vektor predstavljen jednoretcanom matricom, broj redaka je 1, inace je broj redaka
		//jednak dimenzionalnosti vektora
		return this.asRowMatrix ? 1 : this.original.getDimension();
	}

	@Override
	public int getColsCount() {
		
		//ako je vektor predstavljen jednoretcanom matricom, broj stupaca je jednak dimenzionalnosti
		//vektora, inace je broj redaka jednak dimenzionalnosti vektora
		return this.asRowMatrix ? this.original.getDimension() : 1;
	}

	@Override
	public double get(int row, int col) {
		
		//ako je zatrazen nedozvoljen indeks, baci exception
		if (this.asRowMatrix && (row != 0 || 
				(col < 0 || col > this.original.getDimension() - 1))) {
			throw new IndexOutOfBoundsException("Row index must be 0, and col index must be in"
					+ " interval [0, " + (this.original.getDimension() - 1) + "]");
		}
		if (!this.asRowMatrix && (col != 0 || 
				(row < 0 || row > this.original.getDimension() - 1))) {
			throw new IndexOutOfBoundsException("Col index must be 0, and row index must be in"
					+ " interval [0, " + (this.original.getDimension() - 1) + "]");
		}
		
		return this.asRowMatrix ? this.original.get(col) : this.original.get(row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		
		//ako je zatrazen nedozvoljen indeks, baci exception
		if (this.asRowMatrix && (row != 0 || 
				(col < 0 || col > this.original.getDimension() - 1))) {
			throw new IndexOutOfBoundsException("Row index must be 0, and col index must be in"
					+ " interval [0, " + (this.original.getDimension() - 1) + "]");
		}
		if (!this.asRowMatrix && (col != 0 || 
				(row < 0 || row > this.original.getDimension() - 1))) {
			throw new IndexOutOfBoundsException("Col index must be 0, and row index must be in"
					+ " interval [0, " + (this.original.getDimension() - 1) + "]");
		}
		
		//postavi vrijednost na zeljenu
		this.original.set(this.asRowMatrix ? col : row, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixVectorView(this.original.copy(), this.asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixVectorView(
				LinAlgDefaults.defaultVector(this.asRowMatrix ? cols : rows), this.asRowMatrix);
	}

}
