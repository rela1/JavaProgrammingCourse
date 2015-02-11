package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Razred implementira sucelje FileFilter i predstavlja konkretnu implementaciju za filtriranje
 * po tipu filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileTypeFilter implements FileFilter {

	/**
	 * Metoda provjerava je li file datoteka ili folder.
	 * 
	 * @param f file nad kojim se provjerava
	 * @return true ili false, ovisno o provjeri
	 */
	public boolean accepts(File f) {
		if (f.isFile()) {
			return true;
		}
		return false;
	}

}
