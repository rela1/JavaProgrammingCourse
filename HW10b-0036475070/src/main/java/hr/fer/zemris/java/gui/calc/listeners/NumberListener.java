package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

/**
 * Implementacija action listenera za brojčane tipke kalkulatora.
 * 
 * @author Ivan Relić
 * 
 */
public class NumberListener implements ActionListener {

	private String number;
	private JLabel numberLabel;
	private boolean[] append;

	/**
	 * Konstruktor. Prima string prikaz broja koji je pritisnut i JLabel
	 * numberLabel u koji se broj zapisuje.
	 * 
	 * @param number
	 *            broj koji se zapisuje u labelu
	 * @param numberLabel
	 *            labela u koju se zapisuje
	 * @param append
	 *            nadodaju li se brojevi na labelu ili se će se labela
	 *            prebrisati
	 */
	public NumberListener(String number, JLabel numberLabel, boolean[] append) {
		this.number = number;
		this.numberLabel = numberLabel;
		this.append = append;
	}

	/**
	 * Kada se gumb pritisne, treba se provjeriti sadržaj numberLabele i ovisno
	 * o tome u labelu upisati broj.
	 */
	public void actionPerformed(ActionEvent e) {
		// ako je sadržaj labele samo "0", infinitiy ili je append postavljen na
		// false, pregazi to i upiši samo broj koji je
		// primljen i postavi append na true
		String labelText = numberLabel.getText();
		if (labelText.equals("0") || labelText.equals("Infinity")
				|| append[0] == false || append[1] == true) {
			numberLabel.setText(number);
			append[0] = true;
			append[1] = false;
		}
		// inače samo nadodaj vrijednost ovog broja na trenutnu vrijednost (na
		// kraj)
		else {
			numberLabel.setText(labelText.concat(number));
		}
	}

}
