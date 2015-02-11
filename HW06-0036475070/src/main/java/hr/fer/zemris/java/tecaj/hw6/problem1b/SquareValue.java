package hr.fer.zemris.java.tecaj.hw6.problem1b;

/**
 * Konkretna implementacija sucelja IntegerStorageObserver, ispisuje kvadrat broja subjekta koji se
 * promatra.
 * 
 * @author Ivan Relic
 *
 */
public class SquareValue implements IntegerStorageObserver {

	//ID za potrebe metoda equals i hashCode (za valjano funkcioniranje u listama)
	private final int ID = 1;
	
	/**
	 * Metoda na System.out ispisuje vrijednost na koju je subjekt promijenjen i kvadrat te 
	 * vrijednosti.
	 */
	public void valueChanged(IntegerStorageChange istorage) {
		int value = istorage.getNewValue();
		System.out.println("Provided new value: " + value + ", square is: " + value*value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SquareValue))
			return false;
		SquareValue other = (SquareValue) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
}
