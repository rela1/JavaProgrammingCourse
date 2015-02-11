package hr.fer.zemris.java.gui.calc.listeners;

import hr.fer.zemris.java.gui.calc.functions.CalculatorFunction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Implementacija action listenera za funkcije kalkulatora koje trenutno
 * izračunavaju vrijednost.
 * 
 * @author Ivan Relić
 * 
 */
public class FunctionListener implements ActionListener {

	private CalculatorFunction function;
	private JLabel numberLabel;
	private JCheckBox checkBox;
	private JFrame parentFrame;
	private boolean[] append;

	/**
	 * Konstruktor. Prima funkciju koja je vezana uz trenutno dugme koje koristi
	 * ovaj listener, labelu na koju se ispisuju rezultati, parent frame na koji
	 * se option pane u slučaju pogreške stavlja preko i checkBox preko kojeg
	 * provjerava je li funkcija invertirana ili ne.
	 * 
	 * @param numberLabel
	 *            labela za zapis rezultata
	 * @param checkBox
	 *            check box za provjeru invertiranosti funkcije
	 * @param function
	 *            funkcija koja se izvodi
	 * @param parentFrame
	 *            parent frame na za koji je tipka vezana
	 * @param append
	 *            hoce li se nakon izvrsenja funkcije appendati u labelu ili ne
	 */
	public FunctionListener(JLabel numberLabel, JCheckBox checkBox,
			CalculatorFunction function, JFrame parentFrame, boolean[] append) {
		this.function = function;
		this.numberLabel = numberLabel;
		this.checkBox = checkBox;
		this.parentFrame = parentFrame;
		this.append = append;
	}

	/**
	 * Kada se gumb pritisne, dohvaća se sadržaj labele s brojem, dohvaća se
	 * vrijednost checkBoxa i računa se vrijednost funkcije za pročitanu
	 * vrijednost iz labele, i ta nova vrijednost se upisuje u funkciju.
	 */
	public void actionPerformed(ActionEvent e) {
		double currentValue = Double.parseDouble(numberLabel.getText());
		boolean inverted = checkBox.isSelected();
		double functionValue;
		try {
			functionValue = function.executeFunction(currentValue, inverted);
			numberLabel.setText(String.valueOf(functionValue));
			// ako se funkcija uspjesno izvrsi, sredi appendanje
			append[0] = true;
			append[1] = true;
		} catch (IllegalArgumentException exception) {
			// ako ulovis exception iz dohvaćanja funkcijske vrijednosti, dojavi
			// to korisniku preko message dialoga i ostavi vrijednost labela
			// kakav je i bio
			final String message = exception.getMessage();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(parentFrame, message,
							"Error!", JOptionPane.OK_OPTION);
				}
			});
		}
	}
}
