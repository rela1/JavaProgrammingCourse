package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Razred implementira sucelje FileFilter i predstavlja konkretnu implementaciju za filtriranje
 * po velicini filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileSizeFilter implements FileFilter {

	private long size;
	
	/**
	 * Javni konstruktor primljenu velicinu postavlja u clansku varijablu.
	 * 
	 * @param size
	 */
	public FileSizeFilter(long size) {
		this.size = size;
	}
	
	/**
	 * Metoda prihvaca samo one fileove cija je velicina manja ili jednaka zadanoj.
	 * 
	 * @param f file nad kojim se provjerava
	 * @return true ili false, ovisno o provjeri
	 */
	public boolean accepts(File f) {
		if (f.length() <= this.size) {
			return true;
		}
		return false;
	}

}
