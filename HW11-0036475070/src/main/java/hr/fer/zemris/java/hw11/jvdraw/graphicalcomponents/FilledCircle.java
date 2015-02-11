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
 * Razred predstavlja ispunjeni krug određen središtem, radijusom, bojom oboda i
 * bojom ispune.
 * 
 * @author Ivan Relić
 * 
 */
public class FilledCircle extends GeometricalObject {

	private int startX;
	private int startY;
	private int radius;
	private Color foregroundColor;
	private Color backgroundColor;

	/**
	 * Konstruktor. Kreira krug određen točkom središta, radijusom, bojom oboda
	 * i bojom ispune.
	 * 
	 * @param startX
	 *            koordinata x točke središta
	 * @param startY
	 *            koordinata y točke središta
	 * @param radius
	 *            radijus
	 * @param foregroundColor
	 *            boja oboda
	 * @param backgroundColor
	 *            boja ispune
	 */
	public FilledCircle(int startX, int startY, int radius,
			Color foregroundColor, Color backgroundColor) {
		this.startX = startX;
		this.startY = startY;
		this.radius = radius;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
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
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * Postavlja boju oboda kružnice.
	 * 
	 * @param color
	 *            boja oboda
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * Dohvaća boju ispune.
	 * 
	 * @return boja ispune
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Postavlja boju ispune.
	 * 
	 * @param backgroundColor
	 *            boja ispune
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public String toString() {
		return String
				.format("Filled circle (%d, %d), r=%d, FC=(%d, %d, %d), BC=(%d, %d, %d)%n",
						startX, startY, radius, foregroundColor.getRed(),
						foregroundColor.getGreen(), foregroundColor.getBlue(),
						backgroundColor.getRed(), backgroundColor.getGreen(),
						backgroundColor.getBlue());
	}

	@Override
	public String toFileString() {
		return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", startX,
				startY, radius, foregroundColor.getRed(),
				foregroundColor.getGreen(), foregroundColor.getBlue(),
				backgroundColor.getRed(), backgroundColor.getGreen(),
				backgroundColor.getBlue());
	}

	@Override
	public void paint(Graphics2D g2) {
		g2.setColor(backgroundColor);
		g2.fillOval(startX - radius, startY - radius, radius * 2, radius * 2);
		g2.setColor(foregroundColor);
		g2.drawOval(startX - radius, startY - radius, radius * 2, radius * 2);
		return;
	}

	@Override
	public void paintingStarted(Point startingPoint, Color foregroundColor,
			Color backgroundColor) {
		this.startX = startingPoint.x;
		this.startY = startingPoint.y;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}

	@Override
	public void paintingEnded(Point endingPoint) {
		this.radius = (new Double(
				new Point(startX, startY).distance(endingPoint)).intValue());
	}

	@Override
	public void addToModel(DrawingModel model) {
		model.add(new FilledCircle(startX, startY, radius, foregroundColor,
				backgroundColor));
	}

	@Override
	public void getModifyPane(final DrawingModel model, JFrame parent,
			final int index) {
		// napravi panel s textboxovima za modificiranje parametara i checkboxom
		// za brisanje objekta
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));
		JCheckBox delete = new JCheckBox("Delete object");
		panel.add(new JLabel("StartX coordinate: "));
		JTextField startX = new JTextField(
				String.valueOf(FilledCircle.this.startX));
		panel.add(startX);
		panel.add(new JLabel("StartY coordinate: "));
		JTextField startY = new JTextField(
				String.valueOf(FilledCircle.this.startY));
		panel.add(startY);
		panel.add(new JLabel("Radius: "));
		JTextField radius = new JTextField(
				String.valueOf(FilledCircle.this.radius));
		panel.add(radius);
		// dodaj 2 JColorArea za promjene boje objekta
		JColorArea foregroundColorArea = new JColorArea(foregroundColor);
		panel.add(new JLabel("Foreground color change: "));
		panel.add(foregroundColorArea);
		JColorArea backgroundColorArea = new JColorArea(backgroundColor);
		panel.add(new JLabel("Background color change: "));
		panel.add(backgroundColorArea);
		panel.add(delete);
		updateObject(model, parent, panel, startX, startY, radius,
				foregroundColorArea, backgroundColorArea, delete, index);
	}

	/**
	 * Prima parametre u koje se pohranjuju korisničke promjene i izvršava
	 * promjene objekta kroz JOptionPane ako je to zatraženo.
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
	 * @param foregroundColorArea
	 *            JColorArea za promjenu foreground boje
	 * @param backgroundColorArea
	 *            JColorArea za promjenu background boje
	 * @param delete
	 *            checkbox želi li se brisanje objekta ili ne
	 * @param index
	 *            indeks objekta u modelu i listi
	 */
	private void updateObject(final DrawingModel model, JFrame parent,
			JPanel panel, JTextField startX, JTextField startY,
			JTextField radius, JColorArea foregroundColorArea,
			JColorArea backgroundColorArea, JCheckBox delete, int index) {
		// ponavljaj dok god se pritišće OK, a parametri nisu dobro brojčano
		// upisani
		while (true) {
			int response = JOptionPane.showOptionDialog(parent, panel,
					"Modify filled circle parameters:",
					JOptionPane.OK_CANCEL_OPTION,
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
			// postavi unesene parametre u objekt i osvježi objekt u modelu
			FilledCircle.this.startX = Integer.parseInt(startX.getText());
			FilledCircle.this.startY = Integer.parseInt(startY.getText());
			FilledCircle.this.radius = Integer.parseInt(radius.getText());
			FilledCircle.this.foregroundColor = foregroundColorArea
					.getCurrentColor();
			FilledCircle.this.backgroundColor = backgroundColorArea
					.getCurrentColor();
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
