package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.functions.CosinusFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.CotangensFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.LogarithmBase10FunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.LogarithmBaseEFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.NegateNumberFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.ReciprocialFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.SinusFunctionImpl;
import hr.fer.zemris.java.gui.calc.functions.TangensFunctionImpl;
import hr.fer.zemris.java.gui.calc.listeners.AccumulationOperationListener;
import hr.fer.zemris.java.gui.calc.listeners.ClearListener;
import hr.fer.zemris.java.gui.calc.listeners.DecimalDotListener;
import hr.fer.zemris.java.gui.calc.listeners.FunctionListener;
import hr.fer.zemris.java.gui.calc.listeners.MemoryStackListener;
import hr.fer.zemris.java.gui.calc.listeners.NumberListener;
import hr.fer.zemris.java.gui.calc.listeners.ResetListener;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import java.awt.Container;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Implementacija jednostavnog Windows-like kalkulatora s osnovnim aritmetičkim
 * operacijama i matematičkim funkcijama. Kalkulator pamti više operacija i
 * omogućava izračunavanje vrijednosti matematičkom funkcijom u koraku računanja
 * operacije - primjerice - ako se utipka 52 * 2, pa se zatraži kosinus broja 2,
 * to će se trenutno izvršiti i pritiskom na = kalkulator će u stvari izračunati
 * 52 * cos(2). Također, omogućeno je skraćeno zbrajanje, množenje, dijeljenje,
 * oduzimanje i potenciranje/korjenovanje, primjerice, ako se upiše broj 5, pa
 * *, i odma nakon toga =, kalkulator će to protumačiti kao 5 * 5 i ispisat će
 * 25. Višestruko obavljanje operacije pritiskanjem na dugme = neće se
 * obavljati, pritiskom na tipku = proračun je gotov i rezultat se ispisuje na
 * ekran, iza toga se može krenuti dalje s novom operacijom ili funkcijskom
 * vrijendosti.
 * 
 * @author Ivan Relić
 * 
 */
public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * niz od jednog boolean elementa (zbog prijenosa referenci među funkcijama)
	 * koji služi kao indikator unosu brojeva kada treba nadodavati brojeve, a
	 * kada prebrisati vrijednost
	 */
	private boolean[] append = new boolean[] { true, false };

	/** stog koji se koristi pri računanju akumulacijskih operacija */
	private Stack<String> operationStack;

	/**
	 * stog koji se koristi za pohranjivanje vrijednosti iz kalkulatora za
	 * operacije PUSH i POP
	 */
	private Stack<String> memoryStack;

	/** labela za pohranjivanje rezultata */
	private JLabel numberLabel;

	/** niz gumba za brojčane gumbe */
	private JButton[] numberButtons;

	/** niz gumba za funkcije koje trenutno računaju vrijednost */
	private JButton[] functionButtons;

	/** niz gumba za akumulacijske operacije */
	private JButton[] accumulationOperationButtons;

	/** niz gumba za operacije clr, res, push i pop */
	private JButton[] memoryButtons;

	/** checkbox za invertiranje funkcija */
	private JCheckBox invertorCheckBox;

	/** gumb za decimalnu točku */
	private JButton decimalDot;

	/**
	 * Konstruktor. Inicijalizira i postavlja GUI.
	 */
	public Calculator() {
		initializeGUI();
		this.pack();
	}

	/**
	 * Metoda kreira i priprema kompletni GUI i funkcionalnost kalkulatora.
	 */
	private void initializeGUI() {
		this.setTitle("Calculator v1.00");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Container pane = this.getContentPane();
		pane.setLayout(new CalcLayout(3));

		// inicijaliziraj potrebne elemente
		operationStack = new Stack<String>();
		memoryStack = new Stack<String>();
		numberLabel = new JLabel("0");
		numberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		pane.add(numberLabel, "1, 1");
		invertorCheckBox = new JCheckBox("Inv");
		pane.add(invertorCheckBox, "5, 7");

		// dodaj brojčane gumbe na pane
		addNumberButtons(pane);

		// dodaj funkcijske gumbe na pane
		addFunctionButtons(pane);

		// dodaj gumbe akumulacijskih operacija na pane
		addAccumulationOperationButtons(pane);

		// dodaj memorijske gumbe na pane
		addMemoryButtons(pane);
	}

	/**
	 * Dodaje gumbe push i pop te cler i res u container.
	 * 
	 * @param pane
	 *            container u koji se dodaju gumbi.
	 */
	private void addMemoryButtons(Container pane) {
		memoryButtons = new JButton[4];

		memoryButtons[0] = new JButton("push");
		memoryButtons[0].addActionListener(new MemoryStackListener(memoryStack,
				numberLabel, true, this));
		pane.add(memoryButtons[0], "3, 7");

		memoryButtons[1] = new JButton("pop");
		memoryButtons[1].addActionListener(new MemoryStackListener(memoryStack,
				numberLabel, false, this));
		pane.add(memoryButtons[1], "4, 7");

		memoryButtons[2] = new JButton("clr");
		memoryButtons[2].addActionListener(new ClearListener(numberLabel));
		pane.add(memoryButtons[2], "1, 7");

		memoryButtons[3] = new JButton("res");
		memoryButtons[3].addActionListener(new ResetListener(operationStack,
				memoryStack, numberLabel));
		pane.add(memoryButtons[3], "2, 7");
	}

	/**
	 * Dodaje akumulacijske operacije u container.
	 * 
	 * @param pane
	 *            container u koji se dodaje
	 */
	private void addAccumulationOperationButtons(Container pane) {
		accumulationOperationButtons = new JButton[6];

		accumulationOperationButtons[0] = new JButton("+");
		accumulationOperationButtons[0]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "+", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[0], "5, 6");

		accumulationOperationButtons[1] = new JButton("-");
		accumulationOperationButtons[1]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "-", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[1], "4, 6");

		accumulationOperationButtons[2] = new JButton("*");
		accumulationOperationButtons[2]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "*", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[2], "3, 6");

		accumulationOperationButtons[3] = new JButton("/");
		accumulationOperationButtons[3]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "/", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[3], "2, 6");

		accumulationOperationButtons[4] = new JButton("x^n");
		accumulationOperationButtons[4]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "^", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[4], "5, 1");

		accumulationOperationButtons[5] = new JButton("=");
		accumulationOperationButtons[5]
				.addActionListener(new AccumulationOperationListener(
						operationStack, "=", numberLabel, append,
						invertorCheckBox, this));
		pane.add(accumulationOperationButtons[5], "1, 6");
	}

	/**
	 * Dodaje funkcijske gumbe u container.
	 * 
	 * @param pane
	 *            container u koji se dodaje
	 */
	private void addFunctionButtons(Container pane) {
		functionButtons = new JButton[8];

		functionButtons[0] = new JButton("1/x");
		functionButtons[0].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new ReciprocialFunctionImpl(), this, append));
		pane.add(functionButtons[0], "2, 1");

		functionButtons[1] = new JButton("log");
		functionButtons[1].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new LogarithmBase10FunctionImpl(), this,
				append));
		pane.add(functionButtons[1], "3, 1");

		functionButtons[2] = new JButton("ln");
		functionButtons[2].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new LogarithmBaseEFunctionImpl(), this,
				append));
		pane.add(functionButtons[2], "4, 1");

		functionButtons[3] = new JButton("sin");
		functionButtons[3].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new SinusFunctionImpl(), this, append));
		pane.add(functionButtons[3], "2, 2");

		functionButtons[4] = new JButton("cos");
		functionButtons[4].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new CosinusFunctionImpl(), this, append));
		pane.add(functionButtons[4], "3, 2");

		functionButtons[5] = new JButton("tg");
		functionButtons[5].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new TangensFunctionImpl(), this, append));
		pane.add(functionButtons[5], "4, 2");

		functionButtons[6] = new JButton("ctg");
		functionButtons[6].addActionListener(new FunctionListener(numberLabel,
				invertorCheckBox, new CotangensFunctionImpl(), this, append));
		pane.add(functionButtons[6], "5, 2");

		functionButtons[7] = new JButton("+/-");
		functionButtons[7]
				.addActionListener(new FunctionListener(numberLabel,
						invertorCheckBox, new NegateNumberFunctionImpl(), this,
						append));
		pane.add(functionButtons[7], "5, 4");
	}

	/**
	 * Kreira niz brojčanih gumba i dodjeljuje im potrebne listenere i postavlja
	 * u container.
	 * 
	 * @param pane
	 *            container u koji se dodaje
	 */
	private void addNumberButtons(Container pane) {
		decimalDot = new JButton(".");
		decimalDot
				.addActionListener(new DecimalDotListener(append, numberLabel));
		pane.add(decimalDot, "5, 5");

		numberButtons = new JButton[10];
		for (int i = 0; i <= 9; i++) {
			numberButtons[i] = new JButton(String.valueOf(i));
			numberButtons[i].addActionListener(new NumberListener(String
					.valueOf(i), numberLabel, append));
		}
		pane.add(numberButtons[0], "5,3");
		int iterator = 1;
		for (int i = 4; i >= 2; i--) {
			for (int j = 3; j <= 5; j++) {
				pane.add(numberButtons[iterator++], new RCPosition(i, j));
			}
		}
	}

	/**
	 * Kreira novi objekt tipa calculator i prosljeđuje ga event dispatch dretvi
	 * da izvršava operacije nad njime.
	 * 
	 * @param args
	 *            ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Calculator().setVisible(true);
			}
		});
	}
}
