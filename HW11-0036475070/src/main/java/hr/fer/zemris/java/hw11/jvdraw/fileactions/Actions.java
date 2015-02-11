package hr.fer.zemris.java.hw11.jvdraw.fileactions;

import hr.fer.zemris.java.hw11.jvdraw.drawing.JVDrawDrawingModel;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.FilledCircle;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.Line;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.UnfilledCircle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Razred kreira instance akcija potrebnih za menu File aplikacije JVDraw.
 * 
 * @author Ivan Relić
 * 
 */
public class Actions {

	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action exportAction;
	private Action exitAction;
	private Path openedFilePath;
	private JVDrawDrawingModel model;
	private JFrame parent;

	/**
	 * Vraća referencu na gotovu open akciju.
	 * 
	 * @return open akcija
	 */
	public Action getOpenAction() {
		return openAction;
	}

	/**
	 * Vraća referencu na gotovu exit akciju.
	 * 
	 * @return exit akcija
	 */
	public Action getExitAction() {
		return exitAction;
	}

	/**
	 * Vraća referencu na gotovu save akciju.
	 * 
	 * @return save akcija
	 */
	public Action getSaveAction() {
		return saveAction;
	}

	/**
	 * Vraća referencu na gotovu saveAs akciju.
	 * 
	 * @return saveAs akcija
	 */
	public Action getSaveAsAction() {
		return saveAsAction;
	}

	/**
	 * Vraća referencu na gotovu export akciju.
	 * 
	 * @return export akcija
	 */
	public Action getExportAction() {
		return exportAction;
	}

	/**
	 * Konstruktor. Prima referencu na path trenutno otvorenog filea u
	 * aplikaciji JVDraw i na crtaći model koji sadrži trenutne objekte iz
	 * aplikacije.
	 * 
	 * @param openedFilePath
	 *            otvoreni file
	 * @param model
	 *            crtaći model
	 */
	public Actions(Path openedFilePath, JVDrawDrawingModel model, JFrame parent) {
		this.parent = parent;
		this.model = model;
		this.openedFilePath = openedFilePath;
		createOpenAction();
		createSaveAction();
		createSaveAsAction();
		createExitAction();
		createExportAction();
	}

	/**
	 * Kreira export akciju koja može exportati trenutnu sliku u jpg, gif ili
	 * png formatu.
	 */
	private void createExportAction() {
		exportAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Path imagePath = null;
				JFileChooser jfc;
				// dok nije učitana dobra ekstenzija
				while (true) {
					jfc = new JFileChooser();
					jfc.setDialogTitle("Save image: ");
					if (jfc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(parent,
								"Nothing was saved!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					// provjeri ima li path dobru ekstenziju
					imagePath = jfc.getSelectedFile().toPath();
					if (!correctImageExtension(imagePath)) {
						JOptionPane
								.showMessageDialog(
										parent,
										"Image extension should be 'jpg' 'gif' or 'png'!",
										"Warning!", JOptionPane.WARNING_MESSAGE);
						continue;
					} else {
						break;
					}
				}
				// ako file postoji, pitaj korisnika želi li overwrite
				if (imagePath.toFile().exists()) {
					int reply = JOptionPane.showConfirmDialog(jfc,
							"Image exists, do you want to overwrite it?",
							"Warning!", JOptionPane.YES_NO_OPTION);
					if (reply != JOptionPane.YES_OPTION) {
						imagePath = null;
						return;
					}
				}
				// dohvati lijevi i desni rub bounding boxa
				Dimension[] area = new Dimension[2];
				area = getAreaDimensions(model);
				// kreiraj kopiju crtaćeg modela u kojem ćeš svaki objekt
				// pomaknuti za lijevi rub bounding boxa
				JVDrawDrawingModel modelCopy = createModelCopy(model);
				shiftObjectPoints(area[0], modelCopy);
				int width = area[1].width - area[0].width;
				int height = area[1].height - area[0].height;
				// kreiraj image file
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = image.createGraphics();
				// iscrtaj objekte nad kopijom modela
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, width, height);
				for (int i = 0, length = modelCopy.getSize(); i < length; i++) {
					modelCopy.getObject(i).paint(g);
				}
				g.dispose();
				// spremi image u traženi file path
				try {
					ImageIO.write(image, getExtension(imagePath),
							imagePath.toFile());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(parent,
							"Error exporting to image "
									+ openedFilePath.toFile().getAbsolutePath()
									+ ".", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(parent, "Image exported!",
						"Information", JOptionPane.INFORMATION_MESSAGE);
			}
		};
		exportAction.putValue(Action.NAME, "Export");
		exportAction.putValue(Action.SHORT_DESCRIPTION,
				"Used for exporting workspace to image.");
		exportAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control E"));
		exportAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
	}

	/**
	 * Kreira novi crtaći model sa svim kopijama objekata iz originalnoga.
	 * 
	 * @param model
	 *            originalni crtaći model
	 * @return kopija crtaćeg modela
	 */
	private JVDrawDrawingModel createModelCopy(JVDrawDrawingModel model) {
		JVDrawDrawingModel copy = new JVDrawDrawingModel();
		for (int i = 0, length = model.getSize(); i < length; i++) {
			model.getObject(i).addToModel(copy);
		}
		return copy;
	}

	/**
	 * Metoda pomiče točke objekta u skladu s gornjim lijevim kutom bounding
	 * boxa.
	 * 
	 * @param dimension
	 *            lijevi gornji kut bounding boxa
	 * @param model
	 *            crtaći model
	 */
	private void shiftObjectPoints(Dimension dimension, JVDrawDrawingModel model) {
		for (int i = 0, length = model.getSize(); i < length; i++) {
			model.getObject(i).shiftPoint(dimension);
		}
	}

	/**
	 * Dohvaća ekstenziju filea.
	 * 
	 * @param path
	 *            path filea
	 * @return ekstenzija filea
	 */
	private String getExtension(Path path) {
		String pathString = path.toString();
		return pathString.substring(pathString.lastIndexOf('.') + 1)
				.toLowerCase();
	}

	/**
	 * Provjerava ima li path dobru image ekstenziju (jpg, gif ili png).
	 * 
	 * @param imagePath
	 *            path
	 * @return true ako path ima dobru ekstenziju, false inače
	 */
	private boolean correctImageExtension(Path imagePath) {
		String path = imagePath.toString();
		int index = path.lastIndexOf('.');
		if (index == -1) {
			return false;
		}
		String extension = path.substring(index + 1).toUpperCase();
		if (extension.equals("JPG") || extension.equals("PNG")
				|| extension.equals("GIF")) {
			return true;
		}
		return false;
	}

	/**
	 * Vraća dimenzije potrebne za obuhvaćanje cijele slike canvasa (krajnje
	 * gornja lijeva i donja desna).
	 * 
	 * @param model
	 *            crtaći model
	 * @return polje dimenzija
	 */
	private Dimension[] getAreaDimensions(JVDrawDrawingModel model) {
		Dimension min = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Dimension max = new Dimension(Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (int i = 0, length = model.getSize(); i < length; i++) {
			GeometricalObject object = model.getObject(i);
			Dimension leftMost = object.getLeftUpperPoint();
			Dimension rightMost = object.getRightLowerPoint();
			min.width = Math.min(min.width, leftMost.width);
			min.height = Math.min(min.height, leftMost.height);
			max.width = Math.max(max.width, rightMost.width);
			max.height = Math.max(max.height, rightMost.height);
		}
		return new Dimension[] { min, max };
	}

	/**
	 * Kreira exit akciju koja provjerava ima li promjena u modelu i pita
	 * korisnika želi li ih pohraniti.
	 */
	private void createExitAction() {
		exitAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentFileChanged()) {
					int response = askToSave();
					if (response == JOptionPane.YES_OPTION) {
						saveAction.actionPerformed(null);
						parent.dispose();
					} else if (response == JOptionPane.CANCEL_OPTION) {
						return;
					} else if (response == JOptionPane.NO_OPTION) {
						parent.dispose();
					}
				} else {
					parent.dispose();
				}
			}
		};
		exitAction.putValue(Action.NAME, "Exit");
		exitAction
				.putValue(Action.SHORT_DESCRIPTION,
						"Checks for any changes in current file and prompts user, then exits.");
		exitAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
	}

	/**
	 * SaveAs naredba. Za svaki poziv ove naredbe otvara se FileSelector kojim
	 * se odabire lokacija spremanja filea.
	 */
	private void createSaveAsAction() {
		saveAsAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Path oldPath = openedFilePath;
				openedFilePath = null;
				saveAction.actionPerformed(e);
				if (openedFilePath == null) {
					openedFilePath = oldPath;
				}
			}
		};
		saveAsAction.putValue(Action.NAME, "Save As");
		saveAsAction.putValue(Action.SHORT_DESCRIPTION,
				"Used for saving workspace to file.");
		saveAsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control shift S"));
		saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
	}

	/**
	 * Sprema trenutni file. Ako je trenutni file već prije pohranjen, samo
	 * overwritea njegov path, inače, ako još nije spremljen, otvara se
	 * FileChooser za odabir mjesta spremanja.
	 */
	private void createSaveAction() {
		saveAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (openedFilePath == null) {
					JFileChooser jfc = new JFileChooser();
					jfc.setDialogTitle("Save document: ");
					if (jfc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(parent,
								"Nothing was saved!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					openedFilePath = jfc.getSelectedFile().toPath();
					if (!correctExtension(openedFilePath)) {
						openedFilePath = Paths.get(openedFilePath.toString()
								+ ".jvd");
					}
					// ako file postoji, pitaj korisnika želi li overwrite
					if (openedFilePath.toFile().exists()) {
						int reply = JOptionPane.showConfirmDialog(jfc,
								"File exists, do you want to overwrite it?",
								"Warning!", JOptionPane.YES_NO_OPTION);
						if (reply != JOptionPane.YES_OPTION) {
							openedFilePath = null;
							return;
						}
					}
				}
				// priredi string za spremanje
				StringBuilder sb = new StringBuilder();
				for (int i = 0, length = model.getSize(); i < length; i++) {
					sb.append(model.getObject(i).toFileString() + "\n");
				}
				try {
					// pohrani string po bajtovima
					Files.write(openedFilePath,
							sb.toString().getBytes(StandardCharsets.UTF_8));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(parent,
							"Error writing to file "
									+ openedFilePath.toFile().getAbsolutePath()
									+ ".Unknown file state!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;

				}
				JOptionPane.showMessageDialog(parent, "File saved!",
						"Information", JOptionPane.INFORMATION_MESSAGE);
			}
		};
		saveAction.putValue(Action.NAME, "Save");
		saveAction.putValue(Action.SHORT_DESCRIPTION,
				"Used for saving workspace to file.");
		saveAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
	}

	/**
	 * Kreira akciju open.
	 */
	private void createOpenAction() {
		openAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// ako ima razlike između canvasa i trenutno otvorenog filea,
				// upitaj korisnika za pohranu promjena
				if (currentFileChanged()) {
					int response = askToSave();
					if (response == JOptionPane.YES_OPTION) {
						saveAction.actionPerformed(e);
						if (openedFilePath == null) {
							return;
						}
					} else if (response == JOptionPane.CANCEL_OPTION) {
						return;
					}
					openedFilePath = null;
					model.clear();
				}
				// daj korisniku za izabere file i provjeri postoji li
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open file: ");
				if (fc.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File fileName = fc.getSelectedFile();
				Path filePath = fileName.toPath();
				if (!filePath.toFile().exists()) {
					JOptionPane.showMessageDialog(parent,
							"File " + fileName.getAbsolutePath()
									+ " doesn't exist!", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// pročitaj sve linije iz filea
				List<String> objects = new ArrayList<String>();
				try {
					objects = Files.readAllLines(filePath,
							StandardCharsets.UTF_8);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(parent, "Error reading "
							+ fileName.getAbsolutePath() + ".", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				openedFilePath = filePath;
				// prođi svakom linijom i iz nje isparsiraj objekt i dodaj ga u
				// model
				for (int i = 0, length = objects.size(); i < length; i++) {
					GeometricalObject object = objectFromString(objects.get(i));
					if (object == null) {
						JOptionPane.showMessageDialog(parent, "Error reading "
								+ fileName.getAbsolutePath() + ".", "Error!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					model.add(object);
				}
			}
		};
		openAction.putValue(Action.NAME, "Open");
		openAction.putValue(Action.SHORT_DESCRIPTION,
				"Used for opening existing files into workspace.");
		openAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
	}

	/**
	 * Iz dijelova ulaznog stringa pokušava pročitati i-ti dio.
	 * 
	 * @param parts
	 *            dijelovi ulaznog stringa
	 * @param i
	 *            koji dio se čita
	 */
	private int extractPartFromString(String[] parts, int i) {
		try {
			return Integer.parseInt(parts[i]);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parent, "Error reading "
					+ openedFilePath.toAbsolutePath() + "at line " + i + ".",
					"Error!", JOptionPane.ERROR_MESSAGE);
		}
		return -1;
	}

	/**
	 * Metoda provjerava ima li file dobru (.jvd) ekstenziju.
	 * 
	 * @param openedFilePath
	 *            path otvorenog filea
	 * @return true ako file ima dobru ekstenziju, false inače
	 */
	private boolean correctExtension(Path openedFilePath) {
		String path = openedFilePath.toString();
		int index = path.lastIndexOf('.');
		if (index == -1) {
			return false;
		}
		if (!path.substring(index + 1).toUpperCase().equals("JVD")) {
			return false;
		}
		return true;
	}

	/**
	 * Izbacuje pane koji korisnika pita želi li pohraniti promjene ili ne.
	 * 
	 * @return odgovor korisnika
	 */
	private int askToSave() {
		return JOptionPane.showConfirmDialog(parent,
				"Current file changed, do you want to save?", "Warning!",
				JOptionPane.YES_NO_CANCEL_OPTION);
	}

	/**
	 * Metoda za parsiranje string zapisa u konkretni geometrijski objekt.
	 * 
	 * @param string
	 *            zapis geometrijskog objekta
	 * @return geometrijski objekt
	 */
	private GeometricalObject objectFromString(String string) {
		// podijeli po razmaku i zapamti tip objekta
		String[] parts = string.split(" ");
		String type = parts[0];
		if (!type.equals("LINE") && !type.equals("CIRCLE")
				&& !type.equals("FCIRCLE")) {
			return null;
		}
		// svaki tip ima startX i startY koordinatu
		int startX = extractPartFromString(parts, 1);
		if (startX == -1) {
			return null;
		}
		int startY = extractPartFromString(parts, 2);
		if (startY == -1) {
			return null;
		}
		// linija ima endX i endY koordinatu i boju
		if (type.equals("LINE")) {
			return getLine(parts, startX, startY);
		}
		// obe vrste krugova/kružnica imaju radijus i foreground color
		return getCircle(parts, type, startX, startY);
	}

	/**
	 * Provjerava je li trenutno otvoreni file promijenjen u odnosu na zadnju
	 * spremljenu verziju.
	 * 
	 * @return true ako je bilo promjena, false inače
	 */
	private boolean currentFileChanged() {
		// ako nijedan file nije otvoren i ako postoji nešto u canvasu, vrati
		// true
		if (openedFilePath == null) {
			if (model.getSize() != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			// ako trenutno otvoreni file ne postoji (npr. obrisan je dok je
			// program otvoren)
			if (!openedFilePath.toFile().exists()) {
				JOptionPane.showMessageDialog(parent, "Error reading "
						+ openedFilePath.toAbsolutePath()
						+ ".File doesn't exist!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				openedFilePath = null;
				return true;
			}
			// pročitaj i usporedi file u canvasu i file na disku
			List<String> openedFileLines;
			try {
				openedFileLines = Files.readAllLines(openedFilePath,
						StandardCharsets.UTF_8);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(parent, "Error reading "
						+ openedFilePath.toAbsolutePath() + ".", "Error!",
						JOptionPane.ERROR_MESSAGE);
				openedFilePath = null;
				return true;
			}
			// ako nije isti broj objekata u canvasu i u fileu sigurno nisu isti
			int modelSize = model.getSize();
			if (openedFileLines.size() != modelSize) {
				return true;
			}
			// provjeri je li isti svaki objekt u canvasu i u fileu
			for (int i = 0; i < modelSize; i++) {
				if (!openedFileLines.get(i).equals(
						model.getObject(i).toFileString())) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Učitava krajnju točku linije i boju linije i vraća null ako je zapis u
	 * pogrešnom formatu ili novi geometrijski objekt ako je sve u redu.
	 * 
	 * @param parts
	 *            dijelovi ulaza
	 * @param startX
	 *            početna koordinata x
	 * @param startY
	 *            početna koordinata y
	 * @return null ili geometrijski objekt
	 */
	private GeometricalObject getLine(String[] parts, int startX, int startY) {
		int endX = extractPartFromString(parts, 3);
		if (endX == -1) {
			return null;
		}
		int endY = extractPartFromString(parts, 4);
		if (endY == -1) {
			return null;
		}
		int colorR = extractPartFromString(parts, 5);
		if (colorR == -1 || colorR < 0 || colorR > 255) {
			return null;
		}
		int colorG = extractPartFromString(parts, 6);
		if (colorG == -1 || colorG < 0 || colorG > 255) {
			return null;
		}
		int colorB = extractPartFromString(parts, 7);
		if (colorB == -1 || colorB < 0 || colorB > 255) {
			return null;
		}
		return new Line(startX, startY, endX, endY, new Color(colorR, colorG,
				colorB));
	}

	/**
	 * Učitava radijus i boje kruga/kružnice i vraća novi geometrijski objekt
	 * ili null ako je ulazni zapis pogrešno napisan.
	 * 
	 * @param parts
	 *            dijelovi ulaznog zapisa
	 * @param type
	 *            tip objekta
	 * @param startX
	 *            početna koordinata x
	 * @param startY
	 *            početna koordinata y
	 * @return geometrijski objekt ili null
	 */
	private GeometricalObject getCircle(String[] parts, String type,
			int startX, int startY) {
		int radius = extractPartFromString(parts, 3);
		if (radius == -1 || radius < 0) {
			return null;
		}
		int foregroundR = extractPartFromString(parts, 4);
		if (foregroundR == -1 || foregroundR < 0 || foregroundR > 255) {
			return null;
		}
		int foregroundG = extractPartFromString(parts, 5);
		if (foregroundG == -1 || foregroundG < 0 || foregroundG > 255) {
			return null;
		}
		int foregroundB = extractPartFromString(parts, 6);
		if (foregroundB == -1 || foregroundB < 0 || foregroundB > 255) {
			return null;
		}
		// ako je običan krug, vrati ga
		if (type.equals("CIRCLE")) {
			return new UnfilledCircle(startX, startY, radius, new Color(
					foregroundR, foregroundG, foregroundB));
		}
		// inače pročitaj još background boju i vrati popunjen krug
		else {
			int backgroundR = extractPartFromString(parts, 7);
			if (backgroundR == -1 || backgroundR < 0 || backgroundR > 255) {
				return null;
			}
			int backgroundG = extractPartFromString(parts, 8);
			if (backgroundG == -1 || backgroundG < 0 || backgroundG > 255) {
				return null;
			}
			int backgroundB = extractPartFromString(parts, 9);
			if (backgroundB == -1 || backgroundB < 0 || backgroundB > 255) {
				return null;
			}
			return new FilledCircle(startX, startY, radius, new Color(
					foregroundR, foregroundG, foregroundB), new Color(
					backgroundR, backgroundG, backgroundB));
		}
	}
}
