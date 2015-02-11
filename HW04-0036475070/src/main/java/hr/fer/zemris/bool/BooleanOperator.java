package hr.fer.zemris.bool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**Osnovna klasa za boolean operatore.
 * 
 * @author Ivan Relic
 *
 */
public abstract class BooleanOperator implements BooleanSource {

    //clanska varijabla liste sourcesa
    private List<BooleanSource> sources;
    
    
    /**Konstruktor metoda postavlja privatnu listu sourceova na one koji su predani kao lista.
     * 
     * @param sources Lista svih sourceova.
     */
    protected BooleanOperator(List<BooleanSource> sources) {
	
	//ako je predan null argument, baci exception
	if (sources == null || sources.contains(null)) {
	    throw new IllegalArgumentException("Sources shouldnt be null reference!");
	}
	
	//ako je predana prazna lista, baci exception
	if (sources.size() == 0) {
	    throw new IllegalArgumentException("Operator must contain at least one element!");
	}
	
	this.sources = new ArrayList<BooleanSource>(sources);
    }
    
    
    /**Getter metoda koja vraca sourceove iz liste, i to kao novu unmodifiable listu.
     * 
     * @return Lista sourceova.
     */
    protected List<BooleanSource> getSources() {
	List<BooleanSource> lista = new ArrayList<BooleanSource>(sources);
	return Collections.unmodifiableList(lista);
    }
    
    
    /**Prolazi kroz listu sourceova i vraca uniju svih domena, i to kao unmodifiable listu.
     * 
     * @return Vraca domenu operatora.
     */
    public List<BooleanVariable> getDomain() {
	
	//prodji kroz cijelu listu sourceova i dodaj u novu listu domenu od svakoga
	List<BooleanVariable> domain = new ArrayList<BooleanVariable>();
	for(int i = 0, length = sources.size(); i < length; i++) {
	    domain.addAll(sources.get(i).getDomain());
	}
	
	return Collections.unmodifiableList(domain);
    }
}
