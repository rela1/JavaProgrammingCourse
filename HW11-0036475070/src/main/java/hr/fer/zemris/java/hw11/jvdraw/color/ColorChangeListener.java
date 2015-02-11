package hr.fer.zemris.java.hw11.jvdraw.color;

import java.awt.Color;

/**
 * Sučelje za promatrače objekata koji su sposobni pružiti informacije o
 * trenutnoj boji.
 * 
 * @author Ivan Relić
 * 
 */
public interface ColorChangeListener {

	/**
	 * U ovoj metodi se propisuje što se treba obaviti kad se promijeni boja u
	 * primljenom IColorProvideru.
	 * 
	 * @param source
	 *            subjekt koji se promatra i koji je sposoban predati trenutnu
	 *            boju
	 * @param oldColor
	 *            stara boja, prije promjene
	 * @param newColor
	 *            nova boja, nakon promjene
	 */
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor);

}
