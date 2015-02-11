package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Razred implementira sucelje FileFilter i predstavlja konkretnu implementaciju za filtriranje
 * po ekstenziji filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileExtensionFilter implements FileFilter {

	/**
	 * Metoda provjerava ima li file ekstenziju.
	 * 
	 * @param f file nad kojim se provjerava
	 * @return true ili false, ovisno o provjeri
	 */
	public boolean accepts(File f) {
		
		//ako je file oblika ime.ekstenzija, vrati true
		if (f.getName().matches(".*\\..*") && f.isFile()) {
			return true;
		}
		return false;
	}

}
