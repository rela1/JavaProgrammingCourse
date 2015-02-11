package hr.fer.zemris.java.hw11.jvdraw;

import hr.fer.zemris.java.hw11.jvdraw.color.ColorAreaLabelListener;
import hr.fer.zemris.java.hw11.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw11.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw11.jvdraw.drawing.JVDrawDrawingCanvas;
import hr.fer.zemris.java.hw11.jvdraw.drawing.JVDrawDrawingModel;
import hr.fer.zemris.java.hw11.jvdraw.drawing.MouseListModifyListener;
import hr.fer.zemris.java.hw11.jvdraw.fileactions.Actions;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.FilledCircle;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.Line;
import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.UnfilledCircle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred kreira instancu programa JVDraw.
 * 
 * @author Ivan Relić
 * 
 */
public class JVDraw extends JFrame {

	private static final long serialVersionUID = 1L;
	private JColorArea foreground;
	private JColorArea background;
	private JVDrawDrawingModel model;
	private JVDrawDrawingCanvas canvas;
	private ColorAreaLabelListener colorStatusBar;
	private JToggleButton line;
	private JToggleButton circle;
	private JToggleButton filledCircle;
	private ButtonGroup buttonGroup;
	private JButton clearCanvas;
	private DrawingObjectListModel listModel;
	private Path openedFilePath;
	private Actions actions;

	// final static primjerci razreda Line, FilledCircle, UnfilledCircle
	private static final GeometricalObject LINE = new Line(0, 0, 0, 0, null);
	private static final GeometricalObject FILLED_CIRCLE = new FilledCircle(0,
			0, 0, null, null);
	private static final GeometricalObject UNFILLED_CIRCLE = new UnfilledCircle(
			0, 0, 0, null);

	/**
	 * Konstruktor. Inicijalizira GUI i postavlja veličinu prozora.
	 */
	public JVDraw() {
		super("JVDraw v1.00");
		this.setSize(800, 500);
		initGUI();
	}

	/**
	 * Metoda inicijalizira GUI.
	 */
	private void initGUI() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		model = new JVDrawDrawingModel();
		actions = new Actions(openedFilePath, model, this);
		addMenuBar(container);
		addCanvasAndModelList(container);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// postavi window listener za izlaženje i provjeravanje je li došlo do
		// promjene u trenutnom fileu
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actions.getExitAction().actionPerformed(null);
			}
		});
	}

	/**
	 * Kreira menubar za GUI i postavlja ga u container.
	 * 
	 * @param container
	 *            container za dodati menu bar
	 */
	private void addMenuBar(Container container) {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem(actions.getOpenAction());
		JMenuItem save = new JMenuItem(actions.getSaveAction());
		JMenuItem saveAs = new JMenuItem(actions.getSaveAsAction());
		JMenuItem export = new JMenuItem(actions.getExportAction());
		JMenuItem exit = new JMenuItem(actions.getExitAction());
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(export);
		file.add(exit);
		menuBar.add(file);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(menuBar, BorderLayout.NORTH);
		addToolbar(panel);
		container.add(panel, BorderLayout.NORTH);
	}

	/**
	 * Kreira i postavlja model, canvas, model listu i status bar.
	 * 
	 * @param container
	 *            container za postavljanje
	 */
	private void addCanvasAndModelList(Container container) {
		canvas = new JVDrawDrawingCanvas(model, foreground, background,
				buttonGroup);
		container.add(canvas, BorderLayout.CENTER);
		colorStatusBar = new ColorAreaLabelListener(foreground, background);
		container.add(colorStatusBar, BorderLayout.SOUTH);
		listModel = new DrawingObjectListModel(model);
		JList<GeometricalObject> list = new JList<>(listModel);
		list.addMouseListener(new MouseListModifyListener(model, JVDraw.this,
				list));
		container.add(new JScrollPane(list), BorderLayout.EAST);
	}

	/**
	 * Metoda dodaje toolbar u GUI.
	 * 
	 * @param container
	 *            container za dodavanje
	 */
	private void addToolbar(Container container) {
		JToolBar toolbar = new JToolBar("Tools");
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		// kreiraj JColorArea i dodaj ih u toolbar
		foreground = new JColorArea(Color.RED);
		background = new JColorArea(Color.BLUE);
		toolbar.add(foreground);
		toolbar.add(background);
		toolbar.addSeparator();
		// kreiraj isključive tipke za liniju, krug i kružnicu i dodaj ih u
		// toolbar
		line = new JToggleButton("Line");
		circle = new JToggleButton("Circle");
		filledCircle = new JToggleButton("Filled circle");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(line);
		buttonGroup.add(circle);
		buttonGroup.add(filledCircle);
		clearCanvas = new JButton("Clear canvas");
		// kreiraj listenere za tipke
		addGeometricalObjectListeners();
		toolbar.add(line);
		toolbar.add(circle);
		toolbar.add(filledCircle);
		toolbar.addSeparator();
		toolbar.add(clearCanvas);
		// dodaj čitavi toolbar u container
		container.add(toolbar, BorderLayout.SOUTH);
	}

	/**
	 * Postavlja akcije koje se trebaju obaviti kada se klikne na neki od
	 * gumbova line, circle ili filledCircle.
	 */
	private void addGeometricalObjectListeners() {
		line.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setCurrentObject(LINE);
				canvas.resetStartingPoint();
				canvas.repaint();
			}
		});
		circle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setCurrentObject(UNFILLED_CIRCLE);
				canvas.resetStartingPoint();
				canvas.repaint();
			}
		});
		filledCircle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setCurrentObject(FILLED_CIRCLE);
				canvas.resetStartingPoint();
				canvas.repaint();
			}
		});
		clearCanvas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.clear();
				canvas.resetStartingPoint();
				canvas.repaint();
			}
		});
	}

	/**
	 * Main metoda. Kreira objekt razreda JVDraw i pokreće ga na event dispatch
	 * dretvi.
	 * 
	 * @param args
	 *            ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				(new JVDraw()).setVisible(true);
			}
		});
	}

}
