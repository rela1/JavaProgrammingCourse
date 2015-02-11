package hr.fer.zemris.linearna;

/**
 * Klasa kreira transponiranu matricu koja je "zivi" pogled na primljenu matricu. Drugim rijecima,
 * ako se izvrsi promjena na vrijednost transponirane matrice, ta promjena ce se prolongirati i do
 * originalne matrice iz koje je transponirana dobivena.
 * 
 * @author Ivan Relic
 *
 */
public class MatrixTransposeView extends AbstractMatrix implements IMatrix {

	//referenca na originalnu matricu od koje se radi zivi pogled na njen transponirani oblik
	private IMatrix original;
	
	/**
	 * Javni konstruktor prima referencu na originalnu matricu koju treba transponirati. Konstruktor
	 * samo preusmjerava referencu za sebe jer je potreban "zivi" pogled.
	 * 
	 * @param original originalna matrica od koje zelimo transponiranu
	 */
	public MatrixTransposeView(IMatrix original) {
		this.original = original;
	}
	
	@Override
	public int getRowsCount() {
		return this.original.getColsCount();
	}

	@Override
	public int getColsCount() {
		return this.original.getRowsCount();
	}

	@Override
	public double get(int row, int col) {
		
		//ako su primljeni indeksi nedozvoljeni, baci exception
		if (row < 0 || row > this.original.getColsCount() - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.original.getColsCount() - 1) + "] interval!");
		}
		if (col < 0 || col > this.original.getRowsCount() - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.original.getRowsCount() - 1) + "] interval!");
		}
		
		//vrati vrijednost sa zadanog indeksa
		return this.original.get(col, row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		
		//ako su primljeni indeksi izvan granica matrice, baci exception
		if (row < 0 || row > this.original.getColsCount() - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.original.getColsCount() - 1) + "] interval!");
		}
		if (col < 0 || col > this.original.getRowsCount() - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.original.getRowsCount() - 1) + "] interval!");
		}
		
		//postavi vrijednost u matrici i vrati referencu na nju
		this.original.set(col, row, value);
		return original;
	}

	@Override
	public IMatrix copy() {

		//vrati novu transpose matricu koja je zivi pogled na kopiju originalne matrice
		return new MatrixTransposeView(this.original.copy());
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		
		//vrati novu transpose matricu koja je zivi pogled na kopiju nove instance originalne 
		//matrice sa trazenim dimenzijama suprotno zadanima
		return new MatrixTransposeView(this.original.newInstance(cols, rows));
	}

}
