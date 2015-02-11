package hr.fer.zemris.java.hw11.jnotepad.localization;

import javax.swing.JToolBar;

/**
 * Lokalizirana inačica JToolBar razreda koja automatski po promjeni lokalizacije
 * mijenja svoj sadržaj.
 *  
 * @author Ivan Relić
 *
 */
public class LJToolBar extends JToolBar {

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
	public LJToolBar(final String key, final ILocalizationProvider lp) {
		this.setName(lp.getString(key));
		lp.addLocalizationListener(new ILocalizationListener() {
			// kada dođe do promjene lokalizacije, promijeni sadržaj imena toolbara
			@Override
			public void localizationChanged() {
				setName(lp.getString(key));
			}
		});
	}
}
