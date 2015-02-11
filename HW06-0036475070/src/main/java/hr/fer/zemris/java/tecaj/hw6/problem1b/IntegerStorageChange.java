package hr.fer.zemris.java.tecaj.hw6.problem1b;

/**
 * Objekt ove klase sadrzi referencu na IntegerStorage, originalnu vrijednost prije promjene i 
 * promijenjenu vrijednost.
 * 
 * @author Ivan Relic
 *
 */
public class IntegerStorageChange {
	
	private IntegerStorage original;
	private int oldValue;
	private int newValue;
	
	/**
	 * Javni konstruktor prima referencu na IntegerStorage i koristi njegove vrijednosti.
	 * 
	 * @param original referenca na originalni IntegerStorage
	 */
	public IntegerStorageChange(IntegerStorage original, int newValue) {
		this.original = original;
		oldValue = original.getValue();
		this.newValue = newValue;
	}

	/**
	 * Metoda vraca originalni IntegerStorage.
	 * 
	 * @return the original
	 */
	public IntegerStorage getOriginal() {
		return original;
	}

	/**
	 * Metoda vraca staru vrijednost originala.
	 * 
	 * @return the oldValue
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Metoda vraca novu vrijednost originala.
	 * 
	 * @return the newValue
	 */
	public int getNewValue() {
		return newValue;
	}
	
	
}
