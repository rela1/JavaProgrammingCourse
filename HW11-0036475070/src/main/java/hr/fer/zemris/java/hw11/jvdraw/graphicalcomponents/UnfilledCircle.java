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
 * Razred predstavlja neispunjeni krug (kružnicu) određenu središtem, radijusom
 * i bojom oboda kružnice.
 * 
 * @author Ivan Relić
 * 
 */
public class UnfilledCircle extends GeometricalObject {

	private int startX;
	private int startY;
	private int radius;
	private Color color;

	/**
	 * Konstruktor. Kreira kružnicu određenu središtem, radijusom i bojom oboda.
	 * 
	 * @param startX
	 *            koordinata x točke središta
	 * @param startY
	 *            koordinata y točke središta
	 * @param radius
	 *            radijus
	 * @param color
	 *            boja oboda
	 */
	public UnfilledCircle(int startX, int startY, int radius, Color color) {
		this.startX = startX;
		this.startY = startY;
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Dohvaća x koordinatu točku središta kružnice.
	 * 
	 * @return x koordinata točke središta
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Postavlja koordinatu x točke središta kružnice.
	 * 
	 * @param start
	 *            koordinata x točke središta
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	/**
	 * Dohvaća y koordinatu točku središta kružnice.
	 * 
	 * @return y koordinata točke središta
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Postavlja koordinatu y točke središta kružnice.
	 * 
	 * @param start
	 *            koordinata y točke središta
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	/**
	 * Dohvaća radijus kružnice.
	 * 
	 * @return radijus kružnice
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Postavlja radijus kružnice.
	 * 
	 * @param radius
	 *            radijus kružnice
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Dohvaća boju oboda kružnice.
	 * 
	 * @return boja oboda
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Postavlja boju oboda kružnice.
	 * 
	 * @param color
	 *            boja oboda
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return String.format(
				"Unfilled circle (%d, %d), r=%d, FC=(%d, %d, %d)%n", startX,
				startY, radius, color.getRed(), color.getGreen(),
				color.getBlue());
	}

	@Override
	public String toFileString() {
		return String.format("CIRCLE %d %d %d %d %d %d", startX, startY,
				radius, color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void paint(Graphics2D g2) {
		g2.setColor(color);
		g2.drawOval(startX - radius, startY - radius, radius * 2, radius * 2);
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
		this.radius = (new Double(
				new Point(startX, startY).distance(endingPoint)).intValue());
	}

	@Override
	public void addToModel(DrawingModel model) {
		model.add(new UnfilledCircle(startX, startY, radius, color));
	}

	@Override
	public void getModifyPane(final DrawingModel model, JFrame parent,
			final int index) {
		// napravi panel s textboxovima za modificiranje parametara i checkboxom
		// za brisanje objekta
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));
		JCheckBox delete = new JCheckBox("Delete object");
		panel.add(new JLabel("StartX coordinate: "));
		JTextField startX = new JTextField(
				String.valueOf(UnfilledCircle.this.startX));
		panel.add(startX);
		panel.add(new JLabel("StartY coordinate: "));
		JTextField startY = new JTextField(
				String.valueOf(UnfilledCircle.this.startY));
		panel.add(startY);
		panel.add(new JLabel("Radius: "));
		JTextField radius = new JTextField(
				String.valueOf(UnfilledCircle.this.radius));
		panel.add(radius);
		// dodaj JColorArea za promjenu boje objekta
		JColorArea colorArea = new JColorArea(color);
		panel.add(new JLabel("Foreground color change: "));
		panel.add(colorArea);
		panel.add(delete);
		updateObject(model, parent, panel, startX, startY, radius, colorArea,
				delete, index);
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
	 * @param radius
	 *            radijus kružnice
	 * @param colorArea
	 *            JColorArea za promjenu boje
	 * @param delete
	 *            checkbox želi li se brisanje objekta ili ne
	 * @param index
	 *            indeks objekta u modelu i listi
	 */
	private void updateObject(final DrawingModel model, JFrame parent,
			JPanel panel, JTextField startX, JTextField startY,
			JTextField radius, JColorArea colorArea, JCheckBox delete, int index) {
		// ponavljaj dok god se pritišće OK, a parametri nisu dobro brojčano
		// upisani
		while (true) {
			int response = JOptionPane.showOptionDialog(parent, panel,
					"Modify unfilled circle parameters:", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// ako nije odgovoreno s OK, samo izađi
			if (!(response == JOptionPane.OK_OPTION)) {
				break;
			}
			// provjeri je li označen checkbox za brisanje, ako je obriši ga i
			// izađi
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
			if (!radius.getText().matches("[0-9]+")) {
				radius.setText("Radius should be positive integer!");
				continue;
			}
			// postavi unesene parametre u objekt i osvježi model
			UnfilledCircle.this.startX = Integer.parseInt(startX.getText());
			UnfilledCircle.this.startY = Integer.parseInt(startY.getText());
			UnfilledCircle.this.radius = Integer.parseInt(radius.getText());
			UnfilledCircle.this.color = colorArea.getCurrentColor();
			model.updateObject(this);
			break;
		}
	}

	@Override
	public Dimension getLeftUpperPoint() {
		return new Dimension(startX - radius, startY - radius);
	}

	@Override
	public Dimension getRightLowerPoint() {
		return new Dimension(startX + radius, startY + radius);
	}

	@Override
	public void shiftPoint(Dimension dimension) {
		startX = startX - dimension.width;
		startY = startY - dimension.height;
	}
}
