package hr.fer.zemris.java.hw11.jnotepad.localization;

/**
 * Promatrač subjekata koji zadovoljavaju sučelje ILocalizationProvider.
 * 
 * @author Ivan Relić
 * 
 */
public interface ILocalizationListener {

	/**
	 * Akcija koju listener treba obaviti kada se promijeni lokalizacija
	 * subjekta kojeg promatraju.
	 */
	public void localizationChanged();
}
