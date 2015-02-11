package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Implementacija listenera za tipke push i pop.
 * 
 * @author Ivan Relić
 * 
 */
public class MemoryStackListener implements ActionListener {

	private Stack<String> memoryStack;
	private JLabel numberLabel;
	private boolean isPush;
	private JFrame parentFrame;

	/**
	 * Konstruktor. Prima referencu na memorijski stog na koji će pohranjivati,
	 * ili s kojeg će prihvaćati podatke i brojčanu labelu s koje uzima podatke
	 * ili na koju stavlja podatke sa stoga.
	 * 
	 * @param memoryStack
	 *            memorijski stog
	 * @param numberLabel
	 *            labela za prikaz
	 * @param isPush
	 *            je li listener za push ili pop gumb
	 * @param parentFrame
	 *            frame u kojem je stisnuta tipka
	 */
	public MemoryStackListener(Stack<String> memoryStack, JLabel numberLabel,
			boolean isPush, JFrame parentFrame) {
		super();
		this.memoryStack = memoryStack;
		this.numberLabel = numberLabel;
		this.isPush = isPush;
		this.parentFrame = parentFrame;
	}

	/**
	 * Pritiskom na ovaj gumb, ovisno o tome je li listener na push ili pop
	 * gumbu, u memoriju se pohranjuje ili se iz memorije uzima podatak.
	 */
	public void actionPerformed(ActionEvent e) {
		if (isPush) {
			memoryStack.push(numberLabel.getText());
		} else {
			if (memoryStack.isEmpty()) {
				JOptionPane.showMessageDialog(parentFrame,
						"Can't push from stack! It is empty!", "Error!",
						JOptionPane.OK_OPTION);
			} else {
				numberLabel.setText(memoryStack.pop());
			}
		}
	}
}
