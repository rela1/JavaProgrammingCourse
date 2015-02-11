package hr.fer.zemris.java.hw11.jnotepad.localization;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Pruža lokalizacijske mogućnosti za prozore uz mogućnost da se otvaranjem
 * predanog prozora automatski registrira kao listener predanom originalnom
 * provideru, a zatvaranjem tog prozora se automatski odjavljuje i deregistrira
 * iz originalnog providera.
 * 
 * @author Ivan Relić
 * 
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Konstruktor. Pohranjuje originalni provider i dodaje listenere na predani
	 * frame - kada se frame otvori, ovaj provider se spaja na svoj originalni,
	 * a kada se frame zatvori, provider se odspaja od svojeg originalnog
	 * providera čiju referencu drži.
	 * 
	 * @param original
	 *            originalni provider
	 * @param frame
	 *            frame čije otvaranje/zatvaranje pratimo
	 */
	public FormLocalizationProvider(ILocalizationProvider original, JFrame frame) {
		super(original);
		frame.addWindowListener(new WindowAdapter() {
			// kada se prozor otvori, spoji se
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			// kada se prozor zatvori odspoji se
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}

}
