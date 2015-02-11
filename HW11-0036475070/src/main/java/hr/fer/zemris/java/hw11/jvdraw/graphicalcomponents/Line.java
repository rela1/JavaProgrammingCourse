package hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents;

import hr.fer.zemris.java.hw11.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw11.jvdraw.drawing.DrawingModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Razred predstavlja liniju određenu početnom i završnom točkom.
 * 
 * @author Ivan Relić
 * 
 */
public class Line extends GeometricalObject {

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private Color color;

	/**
	 * Konstruktor. Kreira liniju određenu početnom, završnom točkom i bojom.
	 * 
	 * @param startX
	 *            x koordinata početka linije
	 * @param startY
	 *            y koordinata početka linije
	 * @param endX
	 *            x koordinata kraja linije
	 * @param endY
	 *            y koordinata kraja linije
	 * @param end
	 *            završna točka linije
	 * @param color
	 *            boja linije
	 */
	public Line(int startX, int startY, int endX, int endY, Color color) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = color;
	}

	/**
	 * Vraca koordinatu x točke početka linije.
	 * 
	 * @return x koordinata točke početka linije
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Postavlja koordinatu x točke početka linije.
	 * 
	 * @param startX
	 *            koordinata x
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	/**
	 * Vraca koordinatu y točke početka linije.
	 * 
	 * @return y koordinata točke početka linije
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Postavlja koordinatu y točke početka linije.
	 * 
	 * @param starty
	 *            koordinata y
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	/**
	 * Vraca koordinatu x točke kraja linije.
	 * 
	 * @return x koordinata točke kraja linije
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Postavlja koordinatu x točke kraja linije.
	 * 
	 * @param endX
	 *            koordinata x
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}

	/**
	 * Vraca koordinatu y točke kraja linije.
	 * 
	 * @return y koordinata točke kraja linije
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * Postavlja koordinatu y točke kraja linije.
	 * 
	 * @param endY
	 *            koordinata y
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}

	/**
	 * Dohvaća boju linije.
	 * 
	 * @return boja linije
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Postavlja boju linije.
	 * 
	 * @oaram color boja linije
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return String.format("Line (%d, %d), (%d, %d) FC=(%d, %d, %d)%n",
				startX, startY, endX, endY, color.getRed(), color.getGreen(),
				color.getBlue());
	}

	@Override
	public String toFileString() {
		return String.format("LINE %d %d %d %d %d %d %d", startX, startY, endX,
				endY, color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void paint(Graphics2D g2) {
		g2.setColor(color);
		g2.drawLine(startX, startY, endX, endY);
		return;
	}

	@Override
	public void paintingStarted(Point startingPoint, Color foregroundColor,
			Color backgroundColor) {
		this.startX = startingPoint.x;
		this.startY = startingPoint.y;
		this.color = foregroundColor;
	}

	@Override
	public void paintingEnded(Point endingPoint) {
		this.endX = endingPoint.x;
		this.endY = endingPoint.y;
	}

	@Override
	public void addToModel(DrawingModel model) {
		model.add(new Line(startX, startY, endX, endY, color));
	}

	@Override
	public void getModifyPane(final DrawingModel model, JFrame parent,
			final int index) {
		// napravi panel s textboxovima za modificiranje parametara i checkboxom
		// za brisanje objekta
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));
		JCheckBox delete = new JCheckBox("Delete object");
		// dodaj labele i text fieldove za svaki pojedini parametar
		panel.add(new JLabel("StartX coordinate: "));
		JTextField startX = new JTextField(String.valueOf(Line.this.startX));
		panel.add(startX);
		panel.add(new JLabel("StartY coordinate: "));
		JTextField startY = new JTextField(String.valueOf(Line.this.startY));
		panel.add(startY);
		panel.add(new JLabel("EndX coordinate: "));
		JTextField endX = new JTextField(String.valueOf(Line.this.endX));
		panel.add(endX);
		panel.add(new JLabel("EndY coordinate: "));
		JTextField endY = new JTextField(String.valueOf(Line.this.endY));
		panel.add(endY);
		// dodaj JColorArea za promjenu boje linije
		JColorArea colorArea = new JColorArea(color);
		panel.add(new JLabel("Foreground color change: "));
		panel.add(colorArea);
		panel.add(delete);
		updateObject(model, parent, panel, startX, startY, endX, endY,
				colorArea, delete, index);
	}

	/**
	 * Prima parametre u koje se pohranjuju korisničke promjene i izvršava
	 * promjene objekta ako je to zatraženo.
	 * 
	 * @param model
	 *            crtaći model
	 * @param parent
	 *            parent frame
	 * @param panel
	 *            panel koji treba prikazati
	 * @param startX
	 *            textfield start koordinate x
	 * @param startY
	 *            textfield start koordinate y
	 * @param endX
	 *            textfield end koordinate x
	 * @param endY
	 *            textfield end koordinate y
	 * @param colorArea
	 *            color area za promjenu boje
	 * @param delete
	 *            checkbox želi li se brisanje objekta ili ne
	 * @param index
	 *            indeks objekta u listi i modelu
	 */
	private void updateObject(final DrawingModel model, JFrame parent,
			JPanel panel, JTextField startX, JTextField startY,
			JTextField endX, JTextField endY, JColorArea colorArea,
			JCheckBox delete, int index) {
		// ponavljaj dok god se pritišće OK, a parametri nisu dobro brojčano
		// upisani
		while (true) {
			int response = JOptionPane.showOptionDialog(parent, panel,
					"Modify line parameters:", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// ako nije odgovoreno s OK, samo izađi
			if (!(response == JOptionPane.OK_OPTION)) {
				break;
			}
			// ako je selektirano brisanje, obriši objekt i breakaj
			if (delete.isSelected()) {
				model.removeObject(index);
				break;
			}
			// provjeri jesu li svi parametri uneseni u dobrom formatu
			if (!startX.getText().matches("-?[0-9]+")) {
				startX.setText("StartX coordinate should be integer!");
				continue;
			}
			if (!startY.getText().matches("-?[0-9]+")) {
				startY.setText("StartY coordinate should be integer!");
				continue;
			}
			if (!endX.getText().matches("-?[0-9]+")) {
				endX.setText("EndX coordinate should be integer!");
				continue;
			}
			if (!endY.getText().matches("-?[0-9]+")) {
				endY.setText("EndY coordinate should be integer!");
				continue;
			}
			// postavi unesene parametre u objekt i osvježi model
			Line.this.startX = Integer.parseInt(startX.getText());
			Line.this.startY = Integer.parseInt(startY.getText());
			Line.this.endX = Integer.parseInt(endX.getText());
			Line.this.endY = Integer.parseInt(endY.getText());
			Line.this.color = colorArea.getCurrentColor();
			model.updateObject(this);
			break;
		}
	}

	@Override
	public Dimension getLeftUpperPoint() {
		return new Dimension(Math.min(startX, endX), Math.min(startY, endY));
	}

	@Override
	public Dimension getRightLowerPoint() {
		return new Dimension(Math.max(startX, endX), Math.max(startY, endY));
	}

	@Override
	public void shiftPoint(Dimension dimension) {
		startX = startX - dimension.width;
		startY = startY - dimension.height;
		endX = endX - dimension.width;
		endY = endY - dimension.height;
	}
}
