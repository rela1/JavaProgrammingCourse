package hr.fer.zemris.java.tecaj.hw5.filters;

import java.io.File;

/**
 * Klasa implementira sucelje file filter i predstavlja "obrnuti" filter od onog koji mu je 
 * predan kao argument.
 * 
 * @author Ivan Relic
 *
 */
public class ReverseFileFilter implements FileFilter {

	private FileFilter original;
	
	/**
	 * Javni konstruktor prihvaca file filter i postavlja ga u clansku varijablu.
	 * 
	 * @param original originalni file filter
	 */
	public ReverseFileFilter(FileFilter original) {
		this.original = original;
	}
	
	/**
	 * Metoda vraca suprotnu boolean vrijednost od originala jer je zatrazeni suprotan filter.
	 * 
	 * @param f file nad kojim se vrsi provjera
	 * @return true ili false, ovisno o rezultatu
	 */
	public boolean accepts(File f) {
		return !(this.original.accepts(f));
	}

}
