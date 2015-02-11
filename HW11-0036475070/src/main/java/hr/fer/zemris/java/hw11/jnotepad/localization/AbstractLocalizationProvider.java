package hr.fer.zemris.java.hw11.jnotepad.localization;

import java.util.HashSet;
import java.util.Set;

/**
 * Klasa implementira neke od metoda sučelja ILocalizationProvider.
 * 
 * @author Ivan Relić
 * 
 */
public abstract class AbstractLocalizationProvider implements
		ILocalizationProvider {

	private Set<ILocalizationListener> listeners;

	public AbstractLocalizationProvider() {
		listeners = new HashSet<ILocalizationListener>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners = new HashSet<ILocalizationListener>(listeners);
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners = new HashSet<ILocalizationListener>(listeners);
		listeners.remove(listener);
	}

	/**
	 * Obavještava sve promatrače koji promatraju ovaj objekt da je došlo do
	 * promjene.
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
