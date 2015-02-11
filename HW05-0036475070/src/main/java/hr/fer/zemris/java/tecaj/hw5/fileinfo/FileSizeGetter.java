package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;

/**
 * Razred implementira sucelje FileInfoGetter i predstavlja konkretnu implementaciju dohvacanja
 * velicine filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileSizeGetter implements FileInfoGetter {

	/**
	 * Metoda vraca velicinu predanog filea.
	 * 
	 * @param f file nad kojim obavljamo dohvacanje
	 * @return string koji sadrzi velicinu predanog filea u bajtovima
	 */
	public String getInfo(File f) {
		return Long.toString(f.length());
	}

}
