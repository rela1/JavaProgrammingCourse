package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;

/**
 * Razred implementira sucelje FileInfoGetter i predstavlja konkretnu implementaciju dohvacanja
 * informacije o skrivenosti filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileHiddenGetter implements FileInfoGetter {

	/**
	 * Metoda vraca informaciju o tome je li file skriven ili ne.
	 * 
	 * @param f file nad kojim se vrsi dohvacanje
	 * @return h ako je file skriven, inace nista
	 */
	public String getInfo(File f) {
		if (f.isHidden()) {
			return "h";
		}
		return "";
	}

}
