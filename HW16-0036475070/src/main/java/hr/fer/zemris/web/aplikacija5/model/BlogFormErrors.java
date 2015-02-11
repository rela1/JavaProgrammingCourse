package hr.fer.zemris.web.aplikacija5.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Razred pohranjuje informacije za registracijsku/login formu koji se moraju
 * ispisivati.
 * 
 * @author Ivan Relić
 * 
 */
public abstract class BlogFormErrors {

	private Map<String, String> errors;
	

	/**
	 * Konstruktor. Inicijalizira mapu za pogreške i dodjeljuje atribute
	 * nickname i password.
	 */
	public BlogFormErrors() {
		errors = new HashMap<>();
	}

	/**
	 * Vraća informaciju ima li namapiranih pogrešaka vezanih uz predani naziv.
	 * 
	 * @param value
	 *            naziv za vrstu pogreške
	 * @return true ako ima pogrešaka, false inače
	 */
	public boolean hasErrors(String value) {
		if (errors.get(value) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Vraća informaciju jesu li podaci registracijske forme ispravno uneseni.
	 * 
	 * @return true ako jesu, false inače
	 */
	public boolean isCorrect() {
		return errors.size() == 0;
	}

	/**
	 * Dohvaća pogrešku pohranjenu uz predani naziv.
	 * 
	 * @param value
	 *            naziv za vrstu pogreške
	 * @return pogreška
	 */
	public String getError(String value) {
		return errors.get(value);
	}

	/**
	 * Postavlja poruku pogreške na željeni ključ u mapu pogrešaka.
	 * 
	 * @param key
	 *            ključ za spremanje pogreške
	 * @param errorMessage
	 *            poruka pogreške
	 */
	public void setError(String key, String errorMessage) {
		errors.put(key, errorMessage);
	}

	/**
	 * Metoda za primljenu null vrijednost vraća prazan string, a inače vraća
	 * samu primljenu vrijednost.
	 * 
	 * @param value
	 *            vrijednost za parametre korisnika; može biti null
	 * @return sama vrijednost ili prazan string ako je primljena null
	 *         vrijednost
	 */
	protected String getPreparedValue(String value) {
		return value == null ? "" : value;
	}
}
