package hr.fer.zemris.java.hw11.jnotepad;

import hr.fer.zemris.java.hw11.jnotepad.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepad.localization.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepad.localization.LJMenu;
import hr.fer.zemris.java.hw11.jnotepad.localization.LJToolBar;
import hr.fer.zemris.java.hw11.jnotepad.localization.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepad.localization.LocalizationProvider;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

/**
 * Razred predstavlja aplikaciju JNotepad.
 * 
 * @author Ivan Relić
 * 
 */
public class JNotepad extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea editor;
	private Path openedFilePath;
	private ILocalizationProvider lp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);

	/**
	 * Konstruktor. Postavlja grafičke komponente i pokreće JNotepad.
	 */
	public JNotepad() {

		// dodavanje window listenera za zatvaranje modificiranih fileova
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(600, 600);
		initGUI();
	}

	/**
	 * Kreira i postavlja grafičke komponente JNotepada.
	 */
	private void initGUI() {

		editor = new JTextArea();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);

		createActions();
		createMenus();
		createToolbars();

	}

	/**
	 * Akcijama dodjeljuje shortcut keyeve i mnemoničke tipke.
	 */
	private void createActions() {
		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control shift S"));
		saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		deleteSelectedPartAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("F2"));
		deleteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		toggleCaseAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control F3"));
		toggleCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
		cutAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control Y"));

		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));

		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		pasteAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));

		exitAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		engLanguage.putValue(Action.ACCELERATOR_KEY, KeyEvent.VK_E);
		engLanguage.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control 1"));

		croLanguage.putValue(Action.ACCELERATOR_KEY, KeyEvent.VK_H);
		croLanguage.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control 2"));
	}

	/**
	 * Kreira menije s opcijama.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new LJMenu("file", lp);
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new LJMenu("edit", lp);
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(deleteSelectedPartAction));
		editMenu.add(new JMenuItem(toggleCaseAction));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));

		JMenu languageMenu = new LJMenu("languages", lp);
		languageMenu.add(new JMenuItem(croLanguage));
		languageMenu.add(new JMenuItem(engLanguage));
		menuBar.add(languageMenu);

		this.setJMenuBar(menuBar);
	}

	/**
	 * Kreira toolbar s naredbama.
	 */
	private void createToolbars() {
		JToolBar toolBar = new LJToolBar("toolbar", lp);
		toolBar.setFloatable(true);

		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(toggleCaseAction));
		toolBar.add(new JButton(deleteSelectedPartAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(pasteAction));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Provjerava ima li promjene u trenutnom fileu koji je pohranjen na disku i
	 * u trenutnom tekstu u prostoru za pisanje.
	 * 
	 * @return true ako ima promjene, false inače
	 */
	private boolean currentFileChanged() {
		// ako nije otvoren nijedan file i nešto je mijenjano u textarea, vrati
		// true
		if (openedFilePath == null) {
			if (editor.getText().length() != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			// pročitaj i usporedi sadržaj textarea i spremljenog filea
			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes(openedFilePath);
			} catch (IOException ignorable) {
			}
			String fileText = new String(bytes, StandardCharsets.UTF_8);
			String editorText = editor.getText();
			return !fileText.equals(editorText);
		}
	}

	/**
	 * Izbacuje pane koji korisnika upituje želi li pohraniti promjene ili ne.
	 * 
	 * @return
	 */
	private int askToSave() {
		return JOptionPane.showConfirmDialog(JNotepad.this,
				lp.getString("fileChanged"), lp.getString("warning"),
				JOptionPane.YES_NO_CANCEL_OPTION);
	}

	/**
	 * SaveAs naredba. Za svaki poziv ove naredbe otvara se FileSelector kojim
	 * se odabire lokacija spremanja filea.
	 */
	private Action saveAsAction = new LocalizableAction("saveAs", "saveAsDesc",
			lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// zapamti trenutni path i postavi path na null (tako da se sigurno
			// otvori file chooser save akcije)
			Path oldPath = openedFilePath;
			openedFilePath = null;
			saveDocumentAction.actionPerformed(e);
			// ako nije spremljen nikakav file, vrati stari path (prije
			// spremanja)
			if (openedFilePath == null) {
				openedFilePath = oldPath;
			}
		}
	};

	/**
	 * Naredba izreži - izrezuje selektirani tekst i postavlja ga u sistemski
	 * međuspremnik.
	 */
	private Action cutAction = new LocalizableAction("cut", "cutDesc", lp) {

		private static final long serialVersionUID = 1L;
		private Action cut = new DefaultEditorKit.CutAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			cut.actionPerformed(e);
		}
	};

	/**
	 * Naredba kopiraj - kopira selektirani tekst i postavlja ga u sistemski
	 * međuspremnik.
	 */
	private Action copyAction = new LocalizableAction("copy", "copyDesc", lp) {

		private static final long serialVersionUID = 1L;
		private Action copy = new DefaultEditorKit.CopyAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			copy.actionPerformed(e);
		}
	};

	/**
	 * Naredba zalijepi - tekst iz sistemskog međuspremnika lijepi na označeno
	 * mjesto ili na mjesto kursora.
	 */
	private Action pasteAction = new LocalizableAction("paste", "pasteDesc", lp) {

		private static final long serialVersionUID = 1L;
		private Action paste = new DefaultEditorKit.PasteAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			paste.actionPerformed(e);
		}
	};

	/**
	 * Naredba koja postavlja jezik na hrvatski.
	 */
	private Action croLanguage = new LocalizableAction("cro", "croDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};

	/**
	 * Naredba koja postavlja jezik na engleski.
	 */
	private Action engLanguage = new LocalizableAction("eng", "engDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("eng");
		}
	};

	/**
	 * Naredba otvara postojeći file s diska. Ako je trenutno otvoren
	 * modificiran, prvo upituje želi li se njega pohraniti.
	 */
	private Action openDocumentAction = new LocalizableAction("open",
			"openDesc", lp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// ako je trenutno otvoreni file promijenjen, prvo upitaj za njegovo
			// spremanje
			if (currentFileChanged()) {
				int response = askToSave();
				if (response == JOptionPane.YES_OPTION) {
					saveDocumentAction.actionPerformed(e);
					if (openedFilePath == null) {
						return;
					}
				} else if (response == JOptionPane.CANCEL_OPTION) {
					return;
				}
				openedFilePath = null;
				editor.setText("");
			}
			// dohvati path i pročitaj tekst iz tog filea
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(lp.getString("openDoc"));
			if (fc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
						JNotepad.this,
						lp.getString("file") + fileName.getAbsolutePath()
								+ lp.getString("notExists"),
						lp.getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] okteti;
			try {
				okteti = Files.readAllBytes(filePath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(
						JNotepad.this,
						lp.getString("errorReading")
								+ fileName.getAbsolutePath() + ".",
						lp.getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			String tekst = new String(okteti, StandardCharsets.UTF_8);
			editor.setText(tekst);
			openedFilePath = filePath;
		}
	};

	/**
	 * Otvara novi file.Ako je trenutno otvoren modificiran, prvo upituje želi
	 * li se njega pohraniti.
	 */
	private Action newDocumentAction = new LocalizableAction("new", "newDesc",
			lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// provjeri je li trenutni file mijenjan i upitaj korisnika želi li
			// pohraniti promjene
			if (currentFileChanged()) {
				int response = askToSave();
				if (response == JOptionPane.YES_OPTION) {
					saveDocumentAction.actionPerformed(e);
					if (openedFilePath == null) {
						return;
					}
				}
				if (response == JOptionPane.CANCEL_OPTION) {
					return;
				}
				openedFilePath = null;
				editor.setText("");
			}
		}
	};

	/**
	 * Sprema trenutni file. Ako je trenutni file već prije pohranjen, samo
	 * overwritea njegov path, inače, ako još nije spremljen, otvara se
	 * FileChooser za odabir mjesta spremanja.
	 */
	private Action saveDocumentAction = new LocalizableAction("save",
			"saveDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (openedFilePath == null) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle(lp.getString("saveDoc"));
				if (jfc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(JNotepad.this,
							lp.getString("nothingSaved"),
							lp.getString("warning"),
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				openedFilePath = jfc.getSelectedFile().toPath();
				// ako file postoji, pitaj korisnika želi li overwrite
				if (openedFilePath.toFile().exists()) {
					int reply = JOptionPane.showConfirmDialog(jfc,
							lp.getString("overwrite"), lp.getString("warning"),
							JOptionPane.YES_NO_OPTION);
					if (reply != JOptionPane.YES_OPTION) {
						openedFilePath = null;
						return;
					}
				}
			}
			byte[] podatci = editor.getText().getBytes(StandardCharsets.UTF_8);
			try {
				Files.write(openedFilePath, podatci);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepad.this,
						lp.getString("errorWriting")
								+ openedFilePath.toFile().getAbsolutePath()
								+ lp.getString("unknownFileState"),
						lp.getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(JNotepad.this,
					lp.getString("fileSaved"), lp.getString("info"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Naredba briše označeni dio teksta.
	 */
	private Action deleteSelectedPartAction = new LocalizableAction("delete",
			"deleteDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot()
					- editor.getCaret().getMark());
			if (len == 0)
				return;
			int offset = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			try {
				doc.remove(offset, len);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	};

	/**
	 * Ako je označen dio teksta, naredba u označenom dijelu velika slova
	 * pretvara u mala i obratno, ako nije označeno ništa, naredba djeluje u
	 * cijelom prozoru.
	 */
	private Action toggleCaseAction = new LocalizableAction("toggle",
			"toggleDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot()
					- editor.getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(editor.getCaret().getDot(), editor.getCaret()
						.getMark());
			} else {
				len = doc.getLength();
			}
			try {
				String text = doc.getText(offset, len);
				text = changeCase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		/**
		 * Predanom stringu mala slova pretvara u velika i velika u mala.
		 * 
		 * @param text
		 *            string za toggle
		 * @return togglean predani string
		 */
		private String changeCase(String text) {
			char[] znakovi = text.toCharArray();
			for (int i = 0; i < znakovi.length; i++) {
				char c = znakovi[i];
				if (Character.isLowerCase(c)) {
					znakovi[i] = Character.toUpperCase(c);
				} else if (Character.isUpperCase(c)) {
					znakovi[i] = Character.toLowerCase(c);
				}
			}
			return new String(znakovi);
		}
	};

	/**
	 * Naredba za izlaz. Ako je trenutno otvoren modificiran, prvo upituje želi
	 * li se njega pohraniti.
	 */
	private Action exitAction = new LocalizableAction("exit", "exitDesc", lp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentFileChanged()) {
				int response = askToSave();
				if (response == JOptionPane.YES_OPTION) {
					saveDocumentAction.actionPerformed(null);
				} else if (response == JOptionPane.CANCEL_OPTION) {
					return;
				} else if (response == JOptionPane.NO_OPTION) {
					dispose();
				}
			} else {
				dispose();
			}
		}
	};

	/**
	 * Pokreće lokalizirani JNotepad.
	 * 
	 * @param args
	 *            ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new JNotepad().setVisible(true);
			}
		});
	}

}
