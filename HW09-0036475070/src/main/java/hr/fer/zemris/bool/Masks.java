package hr.fer.zemris.bool;

import java.util.ArrayList;
import java.util.List;


/**Klasa s factory static metodama za kreiranje vise Mask objekata odjednom.
 * 
 * @author Ivan Relic
 *
 */
public class Masks {

    
    /**Privatni konstruktor koji se ne koristi.
     * 
     */
    private Masks() {
	
    }
    
    
    /**Factory metoda koja vraca listu maski kreiranih iz indeksa i zeljene velicine.
     * 
     * @param size Velicina maske.
     * @param indexes Indeksi za koje ce se stvoriti maske.
     * @return Lista stvorenih maski.
     */
    public static List<Mask> fromIndexes(boolean dontCare, int size, int ... indexes) {
	
	//ako size nije pozitivan broj, vrati exception
	if (size <= 0) {
	    throw new IllegalArgumentException("Size should be positive number!");
	}
	
	//kreiraj novu listu
	List<Mask> listaMaski = new ArrayList<Mask>();
	
	//proseci se kroz sve indekse i za svaki kreiraj novu masku i dodaj u listu
	for (int i = 0, length = indexes.length; i < length; i++) {
	    listaMaski.add(Mask.fromIndex(false, size, indexes[i]));
	}
	
	return listaMaski;
    }
    
    
    /**Factory metoda koja vraca listu maski kreiranih iz stringova.
     * 
     * @param masksAsString Stringovi koji predstavljaju maske.
     * @return Lista stvorenih maski.
     */
    public static List<Mask> fromStrings(boolean dontCare, String ... masksAsString) {
	
	//kreiraj novu listu
	List<Mask> listaMaski = new ArrayList<Mask>();
	
	//procesi se kroz sve indekse i za svaki kreiraj novu masku i dodaj u listu
	for (int i = 0, length = masksAsString.length; i < length; i++) {
	    listaMaski.add(Mask.parse(dontCare, masksAsString[i]));
	}
	
	return listaMaski;	
    }
}
