package hr.fer.zemris.java.hw11.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Razred modelira sučelje IColorProvider i predstavlja komponentu koja otvara
 * izbornik za odabir boje kada se klikne na nju i poprima tu novoizabranu boju.
 * 
 * @author Ivan Relić
 * 
 */
public class JColorArea extends JComponent implements IColorProvider {

	private static final long serialVersionUID = 1L;
	private Color selectedColor;
	private Set<ColorChangeListener> listeners;

	/**
	 * Konstruktor. Prima referencu na defaultnu boju koju će komponenta
	 * prikazivati.
	 * 
	 * @param defaultColor
	 *            defaultna boja
	 */
	public JColorArea(Color defaultColor) {
		selectedColor = defaultColor;
		listeners = new HashSet<ColorChangeListener>();
		this.repaint();
		// dodaj mouse listener koji će kada se pritisne na komponentu prikazati
		// JColorChooser i dobivenu boju pohraniti u člansku varijablu i
		// pobojati komponentu
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(JColorArea.this,
						"Please choose new color.", selectedColor);
				if (newColor != null) {
					fire(selectedColor, newColor);
					selectedColor = newColor;
					JColorArea.this.repaint();
				}
			}
		});
	}

	/**
	 * Iscrtava komponentu u containeru.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.selectedColor);
		Insets insets = this.getInsets();
		Dimension dim = this.getPreferredSize();
		g2.fillRect(insets.left, insets.top, dim.width - insets.left
				- insets.right, dim.height - insets.top - insets.bottom);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	/**
	 * Dodaje promatrača koji prati promjenu boje ovog subjekta.
	 * 
	 * @param listener
	 *            promatrač
	 */
	public void addColorChangeListener(ColorChangeListener listener) {
		listeners = new HashSet<ColorChangeListener>(listeners);
		listeners.add(listener);
	}

	/**
	 * Uklanja promatrača koji prati promjenu boje ovog subjekta.
	 * 
	 * @param listener
	 *            promatrač
	 */
	public void removeColorChangeListener(ColorChangeListener listener) {
		listeners = new HashSet<ColorChangeListener>(listeners);
		listeners.remove(listener);
	}

	/**
	 * Obavještava sve promatrače koji promatraju ovaj objekt da je došlo do
	 * promjene u boji.
	 */
	public void fire(Color oldColor, Color newColor) {
		for (ColorChangeListener listener : listeners) {
			listener.newColorSelected(this, oldColor, newColor);
		}
	}
}
