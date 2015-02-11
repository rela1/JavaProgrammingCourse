package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;

/**
 * Razred implementira sucelje FileInfoGetter i predstavlja konkretnu implementaciju dohvacanja
 * tipa filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileTypeGetter implements FileInfoGetter {

	/**
	 * Metoda vraca tip filea koji joj je predan kao argument.
	 * 
	 * @param f file nad kojim obavljamo dohvat
	 * @return d ako je file direktorij, f ako je datoteka
	 */
	public String getInfo(File f) {
		
		//ako je file direktorij, vrati d
		if (f.isDirectory()) {
			return "d";
		}
		
		//inace, file je datoteka, vrati f
		else {
			return "f";
		}
	}

}
