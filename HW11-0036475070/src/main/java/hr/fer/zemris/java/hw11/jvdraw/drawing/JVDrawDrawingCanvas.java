package hr.fer.zemris.java.hw11.jvdraw.drawing;

import hr.fer.zemris.java.hw11.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw11.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw11.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 * Razred modelira radnu površinu za crtanje linija, kružnica i krugova.
 * 
 * @author Ivan Relić
 * 
 */
public class JVDrawDrawingCanvas extends JComponent {

	private static final long serialVersionUID = 1L;
	private DrawingModel model;
	private Color foregroundColor;
	private Color backgroundColor;
	private GeometricalObject currentObject;
	private Point startingPoint;

	/**
	 * Konstruktor. Prima referencu na crtaći model i odmah postavlja sebe kao
	 * promatrača na model da može pravovremeno iscrtavati promjene u modelu.
	 * 
	 * @param model
	 *            crtaći model
	 */
	public JVDrawDrawingCanvas(DrawingModel model,
			JColorArea foregroundColorArea, JColorArea backgroundColorArea,
			ButtonGroup buttonGroup) {
		this.model = model;
		// na model odmah zakvači sebe kao listener jer kad god se desi promjena
		// trebaš ponovno iscrtati sve
		addModelListener();
		// zakvači se kao listener na obe JColorArea komponente
		addColorAreaListeners(foregroundColorArea, backgroundColorArea);
		// dodaj mouse listener za dodavanje komponenti u model
		addMouseCanvasListeners();
	}

	/**
	 * Postavlja trenutni objekt na onaj koji je primljen.
	 */
	public void setCurrentObject(GeometricalObject currentObject) {
		this.currentObject = currentObject;
	}

	/**
	 * Resetira crtanje trenutnog objekta, tj. postavlja starting point na null.
	 */
	public void resetStartingPoint() {
		startingPoint = null;
	}

	/**
	 * Dodaje potrebne mouse listenere u canvas kojima se omogućuje dodavanje
	 * elemenata u model.
	 */
	private void addMouseCanvasListeners() {
		// dodaj click listener
		this.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentObject == null) {
					return;
				}
				if (e.getClickCount() == 1) {
					if (startingPoint != null) {
						setEndingPoint(e);
						addObjectToModel();
						return;
					}
					setStartingPoint(e);
				}
			}
		});
		// dodaj motion listener
		this.addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (startingPoint == null) {
					return;
				}
				setEndingPoint(e);
				repaint();
			}
		});

	}

	/**
	 * Postavlja početnu točku za trenutni objekt i poziciju miša.
	 * 
	 * @param e
	 *            događaj miša
	 */
	private void setStartingPoint(MouseEvent e) {
		startingPoint = e.getPoint();
		currentObject.paintingStarted(startingPoint, foregroundColor,
				backgroundColor);
	}

	/**
	 * Postavlja završnu točku za trenutni objekt i poziciju miša.
	 * 
	 * @param e
	 *            događaj miša
	 */
	private void setEndingPoint(MouseEvent e) {
		Point endingPoint = e.getPoint();
		currentObject.paintingEnded(endingPoint);
	}

	/**
	 * Dodaje novi objekt u model.
	 */
	private void addObjectToModel() {
		currentObject.addToModel(model);
		startingPoint = null;
	}

	/**
	 * Postavlja trenutni canvas kao listener na model, tako da kad se nešto u
	 * modelu promijeni on to odma iscrtava.
	 */
	private void addModelListener() {
		this.model.addDrawingModelListener(new DrawingModelListener() {
			@Override
			public void objectsRemoved(DrawingModel source, int index0,
					int index1) {
				JVDrawDrawingCanvas.this.repaint();
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0,
					int index1) {
				JVDrawDrawingCanvas.this.repaint();
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				JVDrawDrawingCanvas.this.repaint();
			}
		});
	}

	/**
	 * Dodaje listenere za promjenu boje kada se ona dogodi i u JColorArea
	 * 
	 * @param foregroundColorArea
	 *            foreground color area
	 * @param backgroundColorArea
	 *            background color area
	 */
	private void addColorAreaListeners(JColorArea foregroundColorArea,
			JColorArea backgroundColorArea) {
		this.foregroundColor = foregroundColorArea.getCurrentColor();
		foregroundColorArea.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				JVDrawDrawingCanvas.this.foregroundColor = newColor;
			}
		});
		this.backgroundColor = backgroundColorArea.getCurrentColor();
		backgroundColorArea.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				JVDrawDrawingCanvas.this.backgroundColor = newColor;
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		// ispuni cijelu svoju površinu bijelim pravokutnikom
		Insets insets = this.getInsets();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.fillRect(insets.left, insets.right, this.getWidth() - insets.left
				- insets.right, this.getHeight() - insets.top - insets.bottom);
		// iscrtaj sve likove iz modela
		for (int i = 0, length = model.getSize(); i < length; i++) {
			model.getObject(i).paint(g2);
		}
		// ako postoji neki lik koji se trenutno definira, crtaj i njega
		if (startingPoint != null) {
			currentObject.paint(g2);
		}
	}
}
