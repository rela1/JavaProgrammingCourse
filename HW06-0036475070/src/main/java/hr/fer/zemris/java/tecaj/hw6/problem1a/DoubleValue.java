package hr.fer.zemris.java.tecaj.hw6.problem1a;

/**
 * Konkretna implementacija sucelja IntegerStorageObserver, ispisuje dvostruku vrijednost subjekta, 
 * i to samo prve dvije promjene vrijednosti, nakon toga se automatski uklanja iz liste
 * registriranih promatraca subjekta.
 * 
 * @author Ivan Relic
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	private int timesCalled = 0;
	
	//ID za potrebe metoda equals i hashCode (za valjano funkcioniranje u listama)
	private final int ID = 3;
	
	/**
	 * Metoda ispisuje dvostruku vrijednost subjekta, i to samo prve dvije promjene vrijednosti, 
	 * nakon toga se automatski uklanja iz liste registriranih promatraca subjekta.
	 */
	public void valueChanged(IntegerStorage istorage) {
		
		timesCalled++;
		
		//ako je vec bilo 2 poziva, izbrisi se iz liste registriranih promatraca
		if (timesCalled == 2) {
			istorage.removeObserver(this);
		}
		
		//inace, ispisi dvostruku vrijednost subjekta
		System.out.println("Double value: " + 2*istorage.getValue());		
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
		if (!(obj instanceof DoubleValue))
			return false;
		DoubleValue other = (DoubleValue) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

}
