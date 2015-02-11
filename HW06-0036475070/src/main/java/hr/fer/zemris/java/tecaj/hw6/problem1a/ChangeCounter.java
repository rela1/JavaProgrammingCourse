package hr.fer.zemris.java.tecaj.hw6.problem1a;

/**
 * Konkretna implementacija sucelja IntegerStorageObserver, ispisuje koliko je puta mijenjana
 * vrijednost subjekta od trenutka registracije ove klase u promatrace subjekta.
 * 
 * @author Ivan Relic
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	private int timesChanged = 1;
	
	//ID za potrebe metoda equals i hashCode (za valjano funkcioniranje u listama)
	private final int ID = 2;
	
	/**
	 * Metoda ispisuje koliko je puta mijenjana vrijednost subjekta od trenutka registracije ove
	 * klase u promatrace subjekta.
	 */
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Number of value changes since tracking: " + timesChanged);
		timesChanged++;
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
		if (!(obj instanceof ChangeCounter))
			return false;
		ChangeCounter other = (ChangeCounter) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

}
