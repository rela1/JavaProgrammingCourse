package hr.fer.zemris.java.hw11.jvdraw.color;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * Razred istovremeno modelira sučelje promatrača nad subjektima koji mogu
 * mijenjati boje i te informacije koje primi automatski prikazuje kroz svoju
 * tekstualnu labelu.
 * 
 * @author Ivan Relić
 * 
 */
public class ColorAreaLabelListener extends JLabel implements
		ColorChangeListener {

	private static final long serialVersionUID = 1L;
	private JColorArea backgroundColorArea;
	private JColorArea foregroundColorArea;

	/**
	 * Konstruktor. Postavlja ovaj objekt kao promatrač na objekte primljene u
	 * konsturktoru i preuzima njihove reference.
	 * 
	 * @param backgroundColor
	 *            subjekt za background boju
	 * @param foregroundColor
	 *            subjekt za foreground boju
	 */
	public ColorAreaLabelListener(JColorArea foregroundColorArea,
			JColorArea backgroundColorArea) {
		this.backgroundColorArea = backgroundColorArea;
		this.foregroundColorArea = foregroundColorArea;
		this.backgroundColorArea.addColorChangeListener(this);
		this.foregroundColorArea.addColorChangeListener(this);
		this.setText(this.foregroundColorArea.getCurrentColor(),
				this.backgroundColorArea.getCurrentColor());
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor) {
		if (source.equals(foregroundColorArea)) {
			this.setText(newColor, this.backgroundColorArea.getCurrentColor());
		} else {
			this.setText(this.foregroundColorArea.getCurrentColor(), newColor);
		}
	}

	/**
	 * Postavlja tekst labele tako da prikazuje RGB komponente foreground i
	 * background boje.
	 * 
	 * @param foregroundColor
	 *            foreground boja
	 * @param backgroundColor
	 *            background boja
	 */
	private void setText(Color foregroundColor, Color backgroundColor) {
		StringBuilder sb = new StringBuilder();
		sb.append("Foreground color: (");
		sb.append(foregroundColor.getRed());
		sb.append(", ");
		sb.append(foregroundColor.getGreen());
		sb.append(", ");
		sb.append(foregroundColor.getBlue());
		sb.append("), background color: (");
		sb.append(backgroundColor.getRed());
		sb.append(", ");
		sb.append(backgroundColor.getGreen());
		sb.append(", ");
		sb.append(backgroundColor.getBlue());
		sb.append(").");
		this.setText(sb.toString());
	}
}
