package hr.fer.zemris.java.hw11.jnotepad.localization;

import javax.swing.JLabel;

/**
 * Lokalizirana inačica JLabel razreda koja automatski po promjeni lokalizacije
 * mijenja svoj sadržaj.
 * 
 * @author Ivan Relić
 * 
 */
public class LJLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor. Prima kljuc koji se koristi u properties fileovima za
	 * dohvaćanje prijevoda za navedenu akciju i lokalizacijski provider koji
	 * može dohvatiti prijevod po ključu.
	 * 
	 * @param key
	 *            ključ za prijevode
	 * @param lp
	 *            lokalizacijski provider
	 */
	public LJLabel(final String key, final ILocalizationProvider lp) {
		this.setText(lp.getString(key));
		lp.addLocalizationListener(new ILocalizationListener() {
			// kada dođe do promjene lokalizacije, promijeni sadržaj labele
			@Override
			public void localizationChanged() {
				setText(lp.getString(key));
			}
		});
	}
}
