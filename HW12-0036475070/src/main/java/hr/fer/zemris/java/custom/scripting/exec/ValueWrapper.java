package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Klasa je wrapper za vrijednosti koje se mogu nalaziti na stacku kod klase
 * ObjectMultiStack.
 * 
 * @author Ivan Relic
 * 
 */
public class ValueWrapper {

	private Object value;

	/**
	 * Javni konstruktor postavlja value na primljenu vrijednost.
	 * 
	 * @param value
	 *            vrijednost koja se postavlja u clansku varijablu
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Metoda uvecava vrijednost clanske varijable za predanu.
	 * 
	 * @param incValue
	 *            vrijednost za koju povecavamo
	 */
	public void increment(Object incValue) {
		this.value = operation(this.value, incValue, '+');
	}

	/**
	 * Metoda umanjuje vrijednost clanske varijable za predanu.
	 * 
	 * @param decValue
	 *            vrijednost za koju smanjujemo
	 */
	public void decrement(Object decValue) {
		this.value = operation(this.value, decValue, '-');
	}

	/**
	 * Metoda mnozi vrijednost clanske varijable s predanom.
	 * 
	 * @param mulValue
	 *            vrijednost s kojom mnozimo
	 */
	public void multiply(Object mulValue) {
		this.value = operation(this.value, mulValue, '*');
	}

	/**
	 * Metoda dijeli vrijednost clanske varijable s predanom.
	 * 
	 * @param divValue
	 *            vrijednost s kojom se dijeli
	 */
	public void divide(Object divValue) {
		this.value = operation(this.value, divValue, '/');
	}

	/**
	 * Metoda usporedjuje clansku varijablu s predanom vrijednosti.
	 * 
	 * @param withValue
	 *            vrijednost za usporedbu
	 * @return 1 ako je clanska varijabla veca, -1 ako je manja, 0 ako su
	 *         jednake
	 */
	public int numCompare(Object withValue) {
		return (int) operation(this.value, withValue, '=');
	}

	/**
	 * Metoda prima dva objekta i znak operacije medju njima i vraca rezultat te
	 * operacije.
	 * 
	 * @param originalValue
	 *            vrijednost prvog operatora
	 * @param operationValue
	 *            vrijednost drugog operatora
	 * @param operator
	 *            operatori: + zbrajanje, - oduzimanje, * mnozenje, /
	 *            dijeljenje, = usporedjivanje
	 * @return
	 */
	private Object operation(Object originalValue, Object operationValue,
			char operator) {

		// ako je bilo koja od vrijednosti null, postavi ih na Integer(0)
		originalValue = isNull(originalValue);
		operationValue = isNull(operationValue);

		// ako je bilo koja od vrijednosti double
		if (isDouble(originalValue) || isDouble(operationValue)) {

			// ako je originalValue Integer, kreiraj novi Double iz njega
			if (isInteger(originalValue)) {
				originalValue = toDouble(toInteger(originalValue) * 1.0);
			}

			// ako je Double, kreiraj ga
			else if (isDouble(originalValue)) {
				originalValue = toDouble(originalValue);
			}

			// inace je nepodrzan oblik, exception
			else {
				throw new RuntimeException("Unsupported type of object!");
			}

			// ako je operationValue Integer, kreiraj novi Double iz njega
			if (isInteger(operationValue)) {
				operationValue = toDouble(toInteger(operationValue) * 1.0);
			}

			// ako je Double, kreiraj ga
			else if (isDouble(operationValue)) {
				operationValue = toDouble(operationValue);
			}

			// inace je nepodrzan oblik, exception
			else {
				throw new RuntimeException("Unsupported type of object!");
			}
		}

		// inace, ako su oba integera
		else if (isInteger(originalValue) && isInteger(operationValue)) {

			// kreiraj nove Integere za oba argumenta
			originalValue = toInteger(originalValue);
			operationValue = toInteger(operationValue);
		}

		// inace, nepodrzan oblik, exception
		else {
			throw new RuntimeException("Unsupported type of object!");
		}

		// obavi trazenu operaciju s obzirom na operator
		if (operator == '+') {

			// ako je originalValue double, castaj ih tako
			if (originalValue instanceof Double) {
				return (Double) originalValue + (Double) operationValue;
			}

			// inace, sigurno je integer
			return (Integer) originalValue + (Integer) operationValue;
		}

		else if (operator == '-') {

			// ako je originalValue double, castaj ih tako
			if (originalValue instanceof Double) {
				return (Double) originalValue - (Double) operationValue;
			}

			// inace, sigurno je integer
			return (Integer) originalValue - (Integer) operationValue;
		}

		else if (operator == '*') {

			// ako je originalValue double, castaj ih tako
			if (originalValue instanceof Double) {
				return (Double) originalValue * (Double) operationValue;
			}

			// inace, sigurno je integer
			return (Integer) originalValue * (Integer) operationValue;
		}

		else if (operator == '/') {

			// ako je originalValue double, castaj ih tako
			if (originalValue instanceof Double) {
				return (Double) originalValue / (Double) operationValue;
			}

			// inace, sigurno je integer
			return (Integer) originalValue / (Integer) operationValue;
		}

		else {

			// ako je originalValue double, castaj ih tako
			if (originalValue instanceof Double) {
				return ((Double) originalValue)
						.compareTo((Double) operationValue);
			}

			// inace, sigurno je integer
			return ((Integer) originalValue)
					.compareTo((Integer) operationValue);
		}

	}

	/**
	 * Metoda vraca integer prikaz primljenog objekta. Obavezno je prije toga
	 * pozvati metodu is integer.
	 * 
	 * @param value
	 *            objekt koji zelimo kao integer
	 * @return integer vrijednost objekta
	 */
	private Integer toInteger(Object value) {

		// ako je objekt instanca stringa, parsiraj ga u Integer i vrati
		if (value instanceof String) {
			return Integer.parseInt((String) value);
		}

		// inace, samo ga vrati castanog u integer
		return (Integer) value;
	}

	/**
	 * Metoda vraca double prikaz primljenog objekta. Obavezno je prije toga
	 * pozvati metodu is Double.
	 * 
	 * @param value
	 *            objekt koji zelimo kao double
	 * @return double vrijednost objekta
	 */
	private Double toDouble(Object value) {

		// ako je objekt instanca stringa, parsiraj ga u Integer i vrati
		if (value instanceof String) {
			return Double.parseDouble((String) value);
		}

		// inace, samo ga vrati castanog u integer
		return (Double) value;
	}

	/**
	 * Metoda provjerava je li primljeni argument null, ako je vraca novi
	 * integer vrijednosti 0.
	 * 
	 * @param Value
	 *            argument koji provjeravamo
	 * @return integer sadrzaja 0 ako je objekt null, inace sami objekt.
	 */
	private Object isNull(Object value) {
		return (value == null) ? new Integer(0) : value;
	}

	/**
	 * Metoda provjerava je li objekt integer broj ili ne.
	 * 
	 * @param value
	 *            argument za koji provjeravamo je li integer
	 * @return true ako je, false inace
	 */
	private boolean isInteger(Object value) {

		// prvo probaj castati u string, pa parseInt metodom u Integer, ako sve
		// prodje u redu, true
		try {
			Integer.parseInt(((String) value));
			return true;
		} catch (Exception e) {
			// ne radi nista ako je exception
		}

		// probaj castati direktno u Integer, ako prodje, vrati true, inace
		// false
		try {
			@SuppressWarnings("unused")
			Integer pom = (Integer) value;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Metoda provjerava je li objekt double broj ili ne.
	 * 
	 * @param value
	 *            argument za koji provjeravamo je li double
	 * @return true ako je, false inace
	 */
	private boolean isDouble(Object value) {

		// ako string odgovara obliku [+-][BROJ].[BROJ] ili
		// [+-][BROJ]E[+-][BROJ] vrati true
		try {
			if (((String) value).matches("([\\+-]?[0-9]+\\.[0-9]+)"
					+ "|([\\+-]?[0-9]+(\\.[0-9]+)?[eE][\\+-]?[0-9]+)")) {
				return true;
			}
		} catch (Exception e) {
			// ne radi nista ako je exception
		}

		// probaj castati direktno u Double, ako prodje, vrati true, inace false
		try {
			@SuppressWarnings("unused")
			Double pom = (Double) value;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Metoda vraca vrijednost varijable value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Metoda postavlja vrijednost varijable value.
	 * 
	 * @param value
	 *            vrijednost koju zelimo postaviti
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ValueWrapper))
			return false;
		ValueWrapper other = (ValueWrapper) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
