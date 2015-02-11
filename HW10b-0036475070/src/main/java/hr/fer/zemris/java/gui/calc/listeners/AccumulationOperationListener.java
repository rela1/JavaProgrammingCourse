package hr.fer.zemris.java.gui.calc.listeners;

import hr.fer.zemris.java.gui.calc.accumulationoperations.AddOperationImpl;
import hr.fer.zemris.java.gui.calc.accumulationoperations.DivOperationImpl;
import hr.fer.zemris.java.gui.calc.accumulationoperations.MulOperationImpl;
import hr.fer.zemris.java.gui.calc.accumulationoperations.PowOperationImpl;
import hr.fer.zemris.java.gui.calc.accumulationoperations.SubOperationImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Implementacija action listenera za funkcije kalkulatora koje akumulirano
 * izračunavaju vrijednost.
 * 
 * @author Ivan Relić
 * 
 */
public class AccumulationOperationListener implements ActionListener {

	/** regex izraz za prepoznavanje operatora */
	private static final String OPERATOR = "[\\+-/\\*\\^]";

	private Stack<String> operationStack;
	private String operator;
	private JLabel numberLabel;
	private boolean[] append;
	private JCheckBox invertorCheckBox;
	private JFrame parentFrame;

	/**
	 * Konstruktor. Prima referencu na operacijski stog iz kalkulatora na koji
	 * se redom stavljaju operandi i operatori svih akumulacijski operanada
	 * kalkulatora, prima oznaku koji je ovo operator, prima referencu na labelu
	 * na kojoj se morjau prikazivati rezultati i međurezultati, prima referencu
	 * na polje od jednog boolean elementa kojim može postavljati informacije za
	 * number listenere trebaju li nadodavati broj na trenutni ili prebrisati
	 * trenutni u cijelosti, referencu na check box koji govori je li zatražena
	 * funkcija invertirana ili ne (koristi se u slučaju x^n i n-tog korijena),
	 * te na kraju roditeljski frame koji je pozvao ovu akciju.
	 * 
	 * @param operationStack
	 *            operacijski stog
	 * @param operator
	 *            oznaka koji je ovo operator
	 * @param numberLabel
	 *            labela za prikaz rezultata
	 * @param append
	 *            zastavica za number gumbe
	 * @param invertorCheckBox
	 *            checkbox za informaciju je li funkcija invertirana ili ne
	 * @param parentFrame
	 *            frame koji je pozvao ovu akciju
	 */
	public AccumulationOperationListener(Stack<String> operationStack,
			String operator, JLabel numberLabel, boolean[] append,
			JCheckBox invertorCheckBox, JFrame parentFrame) {
		super();
		this.operationStack = operationStack;
		this.operator = operator;
		this.numberLabel = numberLabel;
		this.append = append;
		this.invertorCheckBox = invertorCheckBox;
		this.parentFrame = parentFrame;
	}

	/**
	 * Pri okidanju gumba s akumulacijskim operacijama, obavlja se niz operacija
	 * koje pohranjuju operande i operatore na stog i u međukoraku računanja i
	 * traženja rezultata se ti rezultati prikazuju i na labeli predanoj
	 * razredu.
	 */
	public void actionPerformed(ActionEvent e) {

		// ako je operator =, a već je izračunato potrebno, ništa ne radi
		if (operator.equals("=") && operationStack.isEmpty()) {
			return;
		}

		// ako jos nije unesen broj nakon konkretne operacije, znači da je riječ
		// samo o promjeni operatora u hodu, pa to i napravi
		if (append[0] == false && !operationStack.isEmpty()) {
			if (operationStack.peek().matches(OPERATOR)
					&& !operator.equals("=")) {
				operationStack.pop();
				operationStack.push(operator);
				return;
			}
		}

		// dodaj trenutnu vrijednost labele na stog
		operationStack.push(numberLabel.getText());

		// ako je samo 1 element na stogu (trenutna vrijednost) postavi append
		// na false
		if (operationStack.size() == 1) {
			append[0] = false;

		}

		// ako postoji 3 elementa na stogu, znači da je ispod trenutne
		// vrijednosti još jedan operator
		// međukoraka i broj, pa ih izračunaj, zapiši na numberLabel i
		// stavi tu vrijednost na stog
		if (operationStack.size() == 3) {
			double operand2 = Double.parseDouble(operationStack.pop());
			String operator = operationStack.pop();
			double operand1 = Double.parseDouble(operationStack.pop());
			double result = 0;
			switch (operator) {
			case "+":
				result = (new AddOperationImpl()).execute(operand1, operand2,
						false);
				break;
			case "-":
				result = (new SubOperationImpl()).execute(operand1, operand2,
						false);
				break;
			case "*":
				result = (new MulOperationImpl()).execute(operand1, operand2,
						false);
				break;
			case "/":
				try {
					result = (new DivOperationImpl()).execute(operand1,
							operand2, invertorCheckBox.isSelected());
					break;
				} catch (IllegalArgumentException exception) {
					// ako ulovis exception iz dohvaćanja funkcijske
					// vrijednosti, dojavi
					// to korisniku preko message dialoga i ostavi vrijednost
					// labela
					// kakav je i bio
					final String message = exception.getMessage();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(parentFrame, message,
									"Error!", JOptionPane.OK_OPTION);
						}
					});
					break;
				}
			case "^":
				try {
					result = (new PowOperationImpl()).execute(operand1,
							operand2, invertorCheckBox.isSelected());
					break;
				} catch (IllegalArgumentException exception) {
					// ako ulovis exception iz dohvaćanja funkcijske
					// vrijednosti, dojavi
					// to korisniku preko message dialoga i ostavi vrijednost
					// labela
					// kakav je i bio
					final String message = exception.getMessage();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(parentFrame, message,
									"Error!", JOptionPane.OK_OPTION);
						}
					});
					break;
				}
			}
			// zapiši međurezultat u labelu
			numberLabel.setText(String.valueOf(result));

			// postavi append na false, jer se trenutna vrijednost mora
			// prebrisati unosom sljedećih brojeva
			append[0] = false;

			// gurni trenutni rezultat na stog
			operationStack.push(String.valueOf(result));
		}
		// gurni trenutni operator na stog ako je različit od "=", ako je "=",
		// isprazni stog
		if (operator.equals("=")) {
			while (!operationStack.isEmpty()) {
				operationStack.pop();
			}
		} else {
			operationStack.push(operator);
		}
	}
}
