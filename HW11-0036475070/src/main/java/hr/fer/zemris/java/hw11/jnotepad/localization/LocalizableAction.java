package hr.fer.zemris.java.hw11.jnotepad.localization;

import javax.swing.AbstractAction;

/**
 * Lokalizirana inačica Action razreda koja automatski po promjeni lokalizacije
 * mijenja svoje atribute.
 * 
 * @author Ivan Relić
 * 
 */
public abstract class LocalizableAction extends AbstractAction {

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
	public LocalizableAction(final String keyName, final String keyDescription,
			final ILocalizationProvider lp) {
		this.putValue(NAME, lp.getString(keyName));
		this.putValue(SHORT_DESCRIPTION, lp.getString(keyDescription));
		lp.addLocalizationListener(new ILocalizationListener() {
			// kada dođe do promjene lokalizacije, promijeni ime i opis akcije
			@Override
			public void localizationChanged() {
				putValue(NAME, lp.getString(keyName));
				putValue(SHORT_DESCRIPTION, lp.getString(keyDescription));
			}
		});
	}
}
