package hr.fer.zemris.linearna;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Razred nasljeđuje AbstractMatrix i nudi implementacije apstraktnih metoda tog razreda za 
 * potpuno funkcioniranje konkretne implementacije matrica.
 * 
 * @author Ivan Relic
 *
 */
public class Matrix extends AbstractMatrix {

	private double[][] elements;
	private int rows;
	private int cols;
	
	/**
	 * Javni konstruktor kreira novu matricu koja nije read only i
	 * ispunjena je nulama, te je velicine rows*cols.
	 * 
	 * @param rows zeljeni broj redaka matrice
	 * @param cols zeljeni broj stupaca matrice
	 */
	public Matrix(int rows, int cols) {
		this(rows, cols, new double[rows][cols], false);
	}
	
	/**
	 * Javni konstruktor kreira novu matricu dimenzija rows*cols, ako je postavljena zastavica
	 * immutableArray, korisnik garantira da se predano polje nece izvana mijenjati niti brisati
	 * pa matrica moze samo preuzeti referencu na nju, u protivnome konstruktor kopira primljeno
	 * dvodmenzionalno polje u svoju clansku varijablu.
	 * 
	 * @param rows broj redaka matrice
	 * @param cols broj stupaca matrice
	 * @param elements dvodimenzionalno polje elemenata za pohranu u matricu
	 * @param immutableArray ako je true, matrica preuzima samo referencu na polje, inace ga kopira
	 */
	public Matrix(int rows, int cols, double[][] elements, boolean immutableArray) {
		this.rows = rows;
		this.cols = cols;
		
		//ako je garantirano da se polje nece mijenjati, preuzmi referencu na primljeno polje
		if (immutableArray) {
			this.elements = elements;
		}
		
		//inace, iskopiraj dvodimenzionalno polje u svoju clansku varijablu
		else {
			this.elements = new double[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					this.elements[i][j] = elements[i][j];
				}
			}
		}
	}

	@Override
	public int getRowsCount() {
		return this.rows;
	}

	@Override
	public int getColsCount() {
		return this.cols;
	}

	@Override
	public double get(int row, int col) {
		
		//ako je row ili col nedozvoljen, baci exception
		if (row < 0 || row > this.rows - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.rows - 1) + "] interval!");
		}
		if (col < 0 || col > this.cols - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.cols - 1) + "] interval!");
		}
		
		//vrati element iz dvodimenzionalnog polja sa zeljene lokacije
		return this.elements[row][col];
	}

	@Override
	public IMatrix set(int row, int col, double value) {		
		
		//ako je row ili col nedozvoljen, baci exception
		if (row < 0 || row > this.rows - 1) {
			throw new IndexOutOfBoundsException("Row index should be in "
					+ "[0, " + (this.rows - 1) + "] interval!");
		}
		if (col < 0 || col > this.cols - 1) {
			throw new IndexOutOfBoundsException("Column index should be in "
					+ "[0, " + (this.cols - 1) + "] interval!");
		}
		
		//postavi zeljeni element na zeljenu vrijednost i vrati referencu na ovu matricu
		this.elements[row][col] = value;
		return this;
	}

	@Override
	public IMatrix copy() {
		
		//kao zadnji arugment se salje false, sto znaci da ce konstruktor prekopirati predano
		//dvodimenzijsko polje i kopija matrice nece nikako biti vezana za originalnu
		return new Matrix(this.rows, this.cols, this.elements, false);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	public static IMatrix parseSimple(String matrixElements) {
		
		//ako je predan null, baci exception
		if (matrixElements == null) {
			throw new IncompatibleOperandException("Argument should not be null reference!");
		}

		//ako string ne odgovara obliku kakav treba biti, baci exception
		if (!matrixElements.replace("|", "").matches("[([-]?[0-9](\\.[0-9])?)+[\\s]+]+")) {
			throw new IncompatibleOperandException("Invalid string input for parsing!");
		}
		
		//kreiraj novi scanner koji ce citati retke, delimiter mu je |
		Scanner rowScanner = new Scanner(matrixElements);
		rowScanner.useDelimiter("\\|");
		
		//u listu redaka spremi ono sto procitas
		List<String> rowList = new ArrayList<String>();
		while (rowScanner.hasNext()) {
			rowList.add(rowScanner.next());
		}
		
		rowScanner.close();
		
		//broj stupaca se odredjuje prema broju elemenata u prvom retku, ostali retci moraju 
		//sadrzavati isto toliko elemenata
		int cols = 0;
		Scanner colScanner = new Scanner(rowList.get(0));
		while (colScanner.hasNext()) {
			cols++;
			colScanner.next();
		}
		colScanner.close();
		
		//kreiraj dvodimenzionalno polje za matricu, broj redaka je velicina liste rowList
		int rows = rowList.size();
		double[][] elements = new double[rows][cols];
		
		//prodji svim retcima iz liste
		for (int i = 0; i < rows; i++) {
			colScanner = new Scanner(rowList.get(i));
			int j = 0;
			while (colScanner.hasNext()) {
				Double value = Double.parseDouble(colScanner.next());
				elements[i][j] = value.doubleValue();
				j++;
			}
			colScanner.close();
			
			//ako je broj procitanih elemenata u trenutnom retku razlicit od cols, baci exception
			if (j != cols) {
				throw new IncompatibleOperandException("Every row should have same number of "
						+ "elements!");
			}
		}
		
		return new Matrix(rows, cols, elements, true);
	}

}
