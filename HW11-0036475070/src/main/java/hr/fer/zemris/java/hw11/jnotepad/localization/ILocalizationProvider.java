package hr.fer.zemris.java.hw11.jnotepad.localization;

/**
 * Sučelje čije će implementacije biti sposobne ponuditi prijevod za predani
 * ključ.
 * 
 * @author Ivan Relić
 * 
 */
public interface ILocalizationProvider {

	/**
	 * Vraća prijevod predanog ključa.
	 * 
	 * @param key
	 *            ključ riječi za prijevod
	 * @return prijevod predanog ključa
	 */
	public String getString(String key);

	/**
	 * Dodaje u svoju listu novog promatrača (samo ako već ne postoji).
	 * 
	 * @param listener
	 *            novi promatrač
	 */
	public void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Uklanja iz svoje liste predanog promatrača.
	 * 
	 * @param listener
	 *            promatrač kojeg je potrebno ukloniti
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
}
