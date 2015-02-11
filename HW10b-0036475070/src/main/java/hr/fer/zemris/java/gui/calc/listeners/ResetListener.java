package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JLabel;

/**
 * Implementacija listenera za tipku reset.
 * 
 * @author Ivan Relić
 * 
 */
public class ResetListener implements ActionListener {

	private Stack<String> operationStack;
	private Stack<String> memoryStack;
	private JLabel numberLabel;

	/**
	 * Konstruktor. Prima referencu na operacijski stog i na labelu za ispis.
	 * 
	 * @param operationString
	 *            operacijski stog
	 * @param numberLabel
	 *            labela za ispis brojeva
	 * @param memoryStack
	 *            memorijski stog
	 */
	public ResetListener(Stack<String> operationStack,
			Stack<String> memoryStack, JLabel numberLabel) {
		super();
		this.operationStack = operationStack;
		this.numberLabel = numberLabel;
		this.memoryStack = memoryStack;
	}

	/**
	 * Pri pritisku na tipku treba se isprazniti operacijski i memorijski stog i
	 * labela za ispis se treba postaviti na 0.
	 */
	public void actionPerformed(ActionEvent e) {
		while (!operationStack.isEmpty()) {
			operationStack.pop();
		}
		while (!memoryStack.isEmpty()) {
			memoryStack.pop();
		}
		numberLabel.setText("0");
	}

}
