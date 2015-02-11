package hr.fer.zemris.java.hw11.jnotepad.localization;

/**
 * "Providerski most" između originalnog providera i ostalih komponenti koje
 * promatraju lokalizacijskog providera. Omogućava bolji memory management u
 * slučaju da se prozor disposa (singleton razred providera ne bi otpuštao
 * reference na promatrače cijelo vrijeme dok se virtual machine vrti pa bi
 * došlo do nagomilavanja listenera u njegovoj listi i do usporavanja GUI-a).
 * 
 * @author Ivan Relić
 * 
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	private ILocalizationProvider original;
	private boolean connected;
	private ILocalizationListener listener;

	/**
	 * Konstruktor. Prima referencu na originalni provider lokalizacije i
	 * promatra ga te kad dođe do promjene kod njega obavještava sve svoje
	 * promatrače.
	 * 
	 * @param original
	 *            originalni provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider original) {
		this.original = original;
	}

	@Override
	public String getString(String key) {
		return original.getString(key);
	}

	/**
	 * Spaja se na svoj roditeljski provider i prati lokalizacijske promjene.
	 */
	public void connect() {
		if (!connected) {
			connected = true;
			// dodaj novog promatrača u originalni provider kojim ćeš
			// obavijestiti sve svoje promatrače kada dođe do promjene
			// lokalizacije
			listener = new ILocalizationListener() {
				@Override
				public void localizationChanged() {
					fire();
				}
			};
			original.addLocalizationListener(listener);
		}
	}

	/**
	 * Odspaja se od svojeg roditeljskog providera ako već to nije.
	 */
	public void disconnect() {
		if (connected) {
			connected = false;
			original.removeLocalizationListener(listener);
		}
	}

}
