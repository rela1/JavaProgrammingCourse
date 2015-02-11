package hr.fer.zemris.java.tecaj.hw4.db;


/**Klasa implementira sucelje IFilter i sluzi za provjeru odgovara li prezime danoj maski.
 * 
 * @author Ivan Relic
 *
 */
public class LastNameFilter implements IFilter {

    private String mask;
    
    
    /**Javni konstruktor koji primljenu masku pretvara u java regex za uspjesno koristenje
     * String.match() metode.
     * 
     * @param mask Maska s kojom se provjerava prezime.
     */
    public LastNameFilter(String mask) {
	
	//ako maska nije pravilno zadana, baci exception
	if (!mask.matches("(\\*[\\p{L}\\s]*)|([\\p{L}\\s]*\\*[\\p{L}\\s]*)"
		+ "|([\\p{L}\\s]*\\*)|([\\p{L}\\s]*)|(\\*[\\p{L}\\s]*\\*)")) {
	    
	    throw new IllegalArgumentException("Illegal value for mask!");
	}
	
	/*zamijeni znak * sa [a-zA-Z\\s]* jer je to u regexu nula ili vise pojavljivanja
	nekog znaka i dodaj mu ( na pocetak i ) na kraj */
	this.mask = "(" + mask.replace("*", ".*") + ")";
    }
    
    
    /**Metoda provjerava odgovara li prezime predanog StudentRecorda regexu iz maske.
     * 
     * @param record StudentRecord zapis o studentu.
     * @return True ili false, ovisno o rezultatu metode.
     */
    public boolean accepts(StudentRecord record) {
	
	//ako prezime primljenog studenta odgovara maski po regexu, vrati true, false inace
	return record.getLastName().matches(this.mask) ? true : false;
    }

}
