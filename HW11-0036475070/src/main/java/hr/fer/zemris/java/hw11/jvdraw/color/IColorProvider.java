package hr.fer.zemris.java.hw11.jvdraw.color;

import java.awt.Color;

/**
 * Razredi koji implementiraju ovo sučelje sposobni su reći koja im je trenutna
 * boja.
 * 
 * @author Ivan Relić
 * 
 */
public interface IColorProvider {

	/**
	 * Vraća trenutnu boju.
	 * 
	 * @return boja
	 */
	public Color getCurrentColor();

}
