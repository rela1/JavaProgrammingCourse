package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

/**
 * Implementacija listenera za tipku clear koja mora očistiti sadržaj ekrana.
 * 
 * @author Ivan Relić
 * 
 */
public class ClearListener implements ActionListener {

	private JLabel numberLabel;

	/**
	 * Konstruktor. Prima referencu na labelu na koju ispisuje rezultat.
	 * 
	 * @param numberLabel
	 *            labela na koju se zapisuje rezultat
	 */
	public ClearListener(JLabel numberLabel) {
		super();
		this.numberLabel = numberLabel;
	}

	/**
	 * Kada se dogodi pritisak na ovaj gumb, labela s brojevima, tj. ekran se
	 * treba postaviti na "0".
	 */
	public void actionPerformed(ActionEvent e) {
		numberLabel.setText("0");
	}
}
