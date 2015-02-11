package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Sucelje za sve razrede koji garantiraju da imaju metodu accepts koja govori zadovoljava li file
 * neke uvjete ili ne.
 * 
 * @author Ivan Relic
 *
 */
public interface FileFilter {
	
	/**
	 * Metoda provjerava zadovoljava li file zadani implementirani filter, ovisno o razredu.
	 * 
	 * @param f file nad kojim se provjerava 
	 * @return true ili false, ovisno o uspjesnosti provjere
	 */
	public boolean accepts(File f);

}
