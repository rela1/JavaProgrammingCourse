package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class DecimalDotListener implements ActionListener {

	private boolean[] append;
	private JLabel numberLabel;

	/**
	 * Konstruktor. Prima referencu na polje booleanova u ovisnosti s kojim će
	 * ili prebrisati vrijednost trenutne labele s izrazom 0. ili će ako treba
	 * nadodavati elemente provjeriti postoji li već decimalna točka unutra i
	 * postupiti u skladu s tim.
	 * 
	 * @param append
	 *            polje boolean vrijednosti
	 * @param numberLabel
	 *            labela u koju se zapisuje rezultat
	 */
	public DecimalDotListener(boolean[] append, JLabel numberLabel) {
		super();
		this.append = append;
		this.numberLabel = numberLabel;
	}

	/**
	 * Pri pritisku na gumb za decimalnu točku, ova metoda je treba dodati samo
	 * ako ne postoji u trenutnom prikazu.
	 */
	public void actionPerformed(ActionEvent e) {
		// ako treba samo prebrisati vrijednost labele
		if (append[0] == false) {
			numberLabel.setText("0.");
			append[0] = true;
		} else {
			String labelText = numberLabel.getText();
			if (labelText.contains(".")) {
				return;
			} else {
				numberLabel.setText(labelText.concat("."));
			}
		}
	}

}
