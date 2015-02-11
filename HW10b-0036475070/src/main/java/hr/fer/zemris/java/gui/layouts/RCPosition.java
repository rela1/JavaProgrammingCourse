package hr.fer.zemris.java.gui.layouts;

/**
 * Klasa predstavlja ograničenja s kojima CalcLayout razred radi.
 * 
 * @author Ivan Relić
 * 
 */
public class RCPosition {

	private int row;
	private int column;

	/**
	 * Konstruktor. Preuzima ograničenja za redak i stupac u koji će se u
	 * layoutu komponenta dodati.
	 * 
	 * @param row
	 *            redak u koji se dodaje
	 * @param column
	 *            stupac u koji se dodaje
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Getter metoda za column koji ograničenje pokriva.
	 * 
	 * @return int vrijednost stupca
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Getter metoda za row koji ograničenje pokriva.
	 * 
	 * @return int vrijednost retka
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Parsira string koji sadrži podatke o ćeliji u RCPosition objekt.
	 * 
	 * @param constraints
	 *            string oblika int,int
	 * @return novi RCPosition objekt
	 */
	public static RCPosition parse(String constraints) {
		if (!constraints.matches("[\\s]*[0-9]+[\\s]*[,][\\s]*[0-9]+[\\s]*")) {
			throw new IllegalArgumentException("Cannot parse string "
					+ constraints
					+ ". It must be in format: intNumber, intNumber");
		}
		String[] elements = constraints.split(",");
		int row = Integer.parseInt(elements[0].trim());
		int column = Integer.parseInt(elements[1].trim());
		return new RCPosition(row, column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
