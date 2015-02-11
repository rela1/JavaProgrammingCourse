package hr.fer.zemris.java.tecaj.hw5.fileinfo;

import java.io.File;

/**
 * Sucelje koje predstavlja elemente koji mogu pruziti neke podatke iz filea u obliku stringa.
 * 
 * @author Ivan Relic
 *
 */
public interface FileInfoGetter {

	/**
	 * Metoda iz filea uzima zeljene podatke i vraca ih u obliku stringa.
	 * 
	 * @param f file iz kojeg se povlace podaci
	 * @return string koji sadrzi zeljene podatke iz filea
	 */
	public String getInfo(File f);
}
