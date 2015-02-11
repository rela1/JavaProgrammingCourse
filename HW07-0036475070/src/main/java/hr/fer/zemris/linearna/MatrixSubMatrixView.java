package hr.fer.zemris.linearna;

/**
 * Klasa kreira novu matricu koja je "zivi" pogled na samo dio primljene matricu. Drugim rijecima,
 * ako se izvrsi promjena na vrijednost ove matrice, ta promjena ce se prolongirati i do
 * originalne matrice iz koje je pogled na dio matrice s izbacenim zeljenim retkom i stupcem.
 * 
 * @author Ivan Relic
 *
 */
public class MatrixSubMatrixView extends AbstractMatrix {

	private IMatrix original;
	private int rowIndexes[];
	private int colIndexes[];
	
	/**
	 * Javni konstruktor prima referencu na originalnu matricu na ciji dio zelimo pogled. 
	 * Konstruktor prima originalnu matricu te redak i stupac koji zelimo izbaciti iz "zivog" 
	 * pogleda na originalnu matricu.
	 * 
	 * @param original referenca na originalnu matricu
	 * @param row redak koji zelimo izbaciti
	 * @param col stupac koji zelimo izbaciti
	 */
	public MatrixSubMatrixView(IMatrix original, int row, int col) {
		
		//delegiraj kreiranje objekta privatnom konstruktoru
		this(original, createIndexes(original, row, true), createIndexes(original, col, false));
	}

	/**
	 * Metoda kreira listu indeksa koje pogled na dio originalne matrice sadrzi. Metoda prima
	 * referencu na originalnu matricu, vrijednost koja se izbacuje, te boolean vrijednost koja
	 * odredjuje izbacuje li se redak ili stupac.
	 * 
	 * @param original referenca na originalnu matricu
	 * @param value vrijednost retka/stupca koji se izbacuje, tj. kojeg pogled ne sadrzi
	 * @param isRow je li predana vrijednost za redak ili stupac
	 * @return
	 */
	private static int[] createIndexes(IMatrix original, int value, boolean isRow) {
		
		//ako je row ili col nedozvoljen, baci exception
		if ((value < 0 || value > original.getRowsCount() - 1) && isRow) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (original.getRowsCount() - 1) + "] interval!");
		}
		if ((value < 0 || value > original.getColsCount() - 1)  && !isRow) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (original.getColsCount() - 1) + "] interval!");
		}
		
		//dohvati duljinu indeksa originalne matrice (ovisno o atributu isRows)
		int length = (isRow) ? (original.getRowsCount()) : (original.getColsCount());
		
		//duljina kreiranih indexa je duljina originala - 1
		int[] indexes = new int[length - 1];
		
		//prodji kroz sve indexe originalne matrice i uzmi samo one koji su razliciti od indexa
		//za koji je zadano da se izbacuje
		for (int i = 0, j = 0; i < length; i++) {
			if (i != value) {
				indexes[j] = i;
				j++;
			}
		}
		return indexes;
	}
	
	/**
	 * Privatni konstruktor prima referencu na originalnu matricu na ciji dio zelimo pogled. 
	 * Konstruktor prima originalnu matricu te retke i stupce koje ce pogled sadrzavati, bez onog
	 * retka i stupca koji su izbaceni.
	 * 
	 * @param original referenca na originalnu matricu
	 * @param rows retci koje pogled sadrzava
	 * @param cols stupci koje pogled sadrzava
	 */
	private MatrixSubMatrixView(IMatrix original, int[] rows, int[] cols) {
		this.original = original;
		this.rowIndexes = rows;
		this.colIndexes = cols;
	}
	
	@Override
	public int getRowsCount() {
		return this.rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return this.colIndexes.length;
	}

	@Override
	public double get(int row, int col) {
		
		//ako je row ili col nedozvoljen, baci exception
		if (row < 0 || row > this.rowIndexes.length - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.rowIndexes.length - 1) + "] interval!");
		}
		if (col < 0 || col > this.colIndexes.length - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.colIndexes.length - 1) + "] interval!");
		}
		
		//vrati element iz originalne matrice s pozicije koja je u rowIndexes na poziciji row,
		//i koja je u colIndexes na poziciji col jer to predstavlja stvarnu lokaciju trazenog
		//elementa u originalnoj matrici
		return original.get(this.rowIndexes[row], this.colIndexes[col]);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		
		//ako je row ili col veci od duljine rowIndexes ili colIndexes, baci exception
		if (row < 0 || row > this.rowIndexes.length - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.rowIndexes.length - 1) + "] interval!");
		}
		if (col < 0 || col > this.colIndexes.length - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.colIndexes.length - 1) + "] interval!");
		}
		
		//postavi element u originalnoj matrice na poziciji koja je u rowIndexes na poziciji row,
		//i koja je u colIndexes na poziciji col jer to predstavlja stvarnu lokaciju trazenog
		//elementa u originalnoj matrici i vrati referencu na originalnu matricu
		original.set(this.rowIndexes[row], this.colIndexes[col], value);
		return original;
	}

	@Override
	public IMatrix copy() {
		
		//vrati novu submatricu koja je zivi pogled na kopiju originalne matrice
		return new MatrixSubMatrixView(original.copy(), this.rowIndexes, this.colIndexes);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		
		//kreiraj novu instancu originalne matrice velicine rows, cols
		IMatrix newMatrix = this.original.newInstance(rows, cols);
		
		//kreiraj polje redaka
		int[] rowIndexes = new int[rows];
		int[] colIndexes = new int[cols];
		for (int i = 0; i < rows; i++) {
			rowIndexes[i] = i;
		}
		for (int i = 0; i < cols; i++) {
			colIndexes[i] = i;
		}
		
		//vrati novu MatrixSubMatrix instancu s novom instancom originalne matrice i stupcima i
		//retcima koje smo kreirali
		return new MatrixSubMatrixView(newMatrix, rowIndexes, colIndexes);
	}

}
