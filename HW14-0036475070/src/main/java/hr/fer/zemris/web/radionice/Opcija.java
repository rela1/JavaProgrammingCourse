package hr.fer.zemris.web.radionice;

/**
 * Razred modelira opciju kojoj je pridružen id i vrijednost.
 * 
 * @author Ivan Relić
 * 
 */
public class Opcija implements Comparable<Opcija> {

	private String id;
	private String vrijednost;

	/**
	 * Konstruktor. Kreira opciju iz primljenog id-a i vrijednosti opcije.
	 * 
	 * @param id
	 *            id opcije; mora biti pozitivna long vrijednost
	 * @param vrijednost
	 *            vrijednost opcije
	 */
	public Opcija(String id, String vrijednost) {
		try {
			Long l = Long.parseLong(id);
			if (l < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Vrijednost identifikatora opcije mora biti pozitivna long vrijednost!");
		}
		this.id = id;
		this.vrijednost = vrijednost;
	}

	/**
	 * Dohvaća vrijednost opcije.
	 * 
	 * @return vrijednost opcije
	 */
	public String getVrijednost() {
		return vrijednost;
	}

	/**
	 * Postavlja vrijednost opcije na predanu.
	 * 
	 * @param vrijednost
	 *            nova vrijednost opcije
	 */
	public void setVrijednost(String vrijednost) {
		this.vrijednost = vrijednost;
	}

	/**
	 * Dohvaća vrijednost id-a opcije.
	 * 
	 * @return vrijednost id-a opcije
	 */
	public String getId() {
		return id;
	}

	@Override
	public int compareTo(Opcija o) {
		if (this.id == null) {
			if (o.id == null) {
				return 0;
			}
			return -1;
		} else if (o.id == null) {
			return 1;
		}
		return this.id.compareTo(o.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((vrijednost == null) ? 0 : vrijednost.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Opcija))
			return false;
		Opcija other = (Opcija) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (vrijednost == null) {
			if (other.vrijednost != null)
				return false;
		} else if (!vrijednost.equals(other.vrijednost))
			return false;
		return true;
	}

}
