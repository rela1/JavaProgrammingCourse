package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Razred implementira sucelje FileInfoGetter i predstavlja konkretnu implementaciju dohvacanja
 * zadnjeg vremena modificiranja filea.
 * 
 * @author Ivan Relic
 *
 */
public class FileDateModifyGetter implements FileInfoGetter {

	/**
	 * Metoda vraca datum i vrijeme zadnje modifikacije filea.
	 * 
	 * @param f file nad kojim se vrsi dohvacanje podataka
	 * @return datum i vrijeme zadnje modifikacije u obliku stringa
	 */
	public String getInfo(File f) {
		Date date = new Date(f.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
