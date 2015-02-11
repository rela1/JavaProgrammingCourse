package hr.fer.zemris.java.tecaj.hw5.filesort;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Razred implementira Comparator i iz liste Comparatora kreira samo jedan.
 * 
 * @author Ivan Relic
 *
 */
public class CompositionSorter implements Comparator<File> {
	
	private List<Comparator<File>> comparators;
	
	/**
	 * Javni konstruktor prima listu komparatora i referencira svoju clansku varijablu na tu 
	 * listu.
	 * 
	 * @param comparators lista komparatora
	 */
	public CompositionSorter(List<Comparator<File>> comparators) {
		this.comparators = comparators;
	}

	/**
	 * Metoda obavlja kompozitno usporedivanje po listi komparatora.
	 * 
	 * @return rezultat usporedbe
	 */
	public int compare(File f1, File f2) {
		
		//usporedjuj redom po komparatorima dok ne naidjes na vrijednost u kojoj se razlikuju
		 for (Comparator<File> comparator : this.comparators) {
			 int r = comparator.compare(f1, f2);
				if(r!=0) {
					return r;
			    }
		 }
		 
		 //ako ne pronadjes razliku u cijeloj listi komparatora, vrati 0 jer su jednaki
		 return 0;
	}
}
