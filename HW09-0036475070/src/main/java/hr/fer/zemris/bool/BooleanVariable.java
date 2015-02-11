package hr.fer.zemris.bool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa za imenovane varijable.
 * 
 * @author Ivan Relic
 * 
 */
public class BooleanVariable implements NamedBooleanSource {

	// privatne clanske varijable
	private BooleanValue value = BooleanValue.FALSE;
	private String name;

	/**
	 * Public konstruktor koji prima ime varijable i postavlja ga.
	 * 
	 * @param name
	 *            Ime varijable.
	 */
	public BooleanVariable(String name) {
		this.name = name;
	}

	/**
	 * Getter metoda za vracanje vrijednosti iz varijable value.
	 * 
	 * @return Vrijednost varijable.
	 */
	public BooleanValue getValue() {
		return this.value;
	}

	/**
	 * Setter metoda za clansku varijablu value.
	 * 
	 * @param value
	 *            Vrijednost na koju se postavlja.
	 */
	public void setValue(BooleanValue value) {
		this.value = value;
	}

	/**
	 * Getter metoda za vracanje domene, vraca unmodifiable listu.
	 * 
	 * @return Domena varijable.
	 */
	public List<BooleanVariable> getDomain() {
		List<BooleanVariable> lista = new ArrayList<BooleanVariable>();
		lista.add(this);
		return Collections.unmodifiableList(lista);
	}

	/**
	 * Getter metoda za vracanje imena varijable.
	 * 
	 * @return Ime varijable.
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BooleanVariable))
			return false;
		BooleanVariable other = (BooleanVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
