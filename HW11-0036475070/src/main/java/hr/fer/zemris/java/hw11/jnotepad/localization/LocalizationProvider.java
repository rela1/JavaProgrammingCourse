package hr.fer.zemris.java.hw11.jnotepad.localization;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Učitava resource boundle s prijevodima za postavljeni jezik. Po defaultu,
 * koristi se engleski jezik.
 * 
 * @author Ivan Relić
 * 
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	private static final LocalizationProvider PROVIDER = new LocalizationProvider();
	private static final String BASE_NAME = "hr.fer.zemris.java.hw11.jnotepad.localization.translation";
	private String language;
	private ResourceBundle bundle;

	/**
	 * Privatni konstruktor. Kreira bundle koji po defaultu koristi engleski
	 * jezik.
	 */
	private LocalizationProvider() {
		bundle = ResourceBundle.getBundle(BASE_NAME, Locale.ENGLISH);
	}

	/**
	 * Postavlja jezik čiji se prijevodi dohvaćaju na onaj koji je predan.
	 * 
	 * @param language
	 *            jezik čije prijevode želimo
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle = ResourceBundle.getBundle(BASE_NAME, new Locale(this.language));
		fire();
	}

	/**
	 * Vraća instancu ovog razreda potrebnu za dohvaćanje prijevoda po ključu.
	 * 
	 * @return
	 */
	public static LocalizationProvider getInstance() {
		return PROVIDER;
	}

	@Override
	public String getString(String key) {
		String value = bundle.getString(key);
		try {
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException ignorable) {
		}
		return value;

	}

}
