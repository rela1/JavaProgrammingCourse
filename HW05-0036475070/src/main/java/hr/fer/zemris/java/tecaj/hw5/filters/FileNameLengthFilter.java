package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Razred implementira sucelje FileFilter i predstavlja konkretnu implementaciju za filtriranje
 * po duljini imena filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileNameLengthFilter implements FileFilter {

	private long nameLength;
	
	/**
	 * Javni konstruktor postavlja duljinu u clansku varijablu.
	 * 
	 * @param nameLength duljina imena do koje se file prihvaca
	 */
	public FileNameLengthFilter(long nameLength) {
		this.nameLength = nameLength;
	}
	
	/**
	 * Metoda provjerava je li duljina imena filea manja ili jednaka zadanoj.
	 * 
	 * @param f file nad kojim se provjerava
	 * @return true ili false, ovisno o provjeri
	 */
	public boolean accepts(File f) {
		if (f.getName().length() <= this.nameLength) {
			return true;
		}
		return false;
	}
}
