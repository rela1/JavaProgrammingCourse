package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;

/**
 * Razred implementira sucelje FileInfoGetter i predstavlja konkretnu implementaciju dohvacanja
 * imena filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileNameGetter implements FileInfoGetter {

	/**
	 * Metoda vraca ime filea u obliku stringa.
	 * 
	 * @param f file nad kojim obavljamo dohvat
	 * @return string koji sadrzava ime filea
	 */
	public String getInfo(File f) {
		return f.getName();
	}

}
