package hr.fer.zemris.bool.opimpl;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;


/**Klasa kreira objekt koji je container za BooleanSource i na zahtjev moze izracunati
 * i vratiti vrijednost koja je jednaka komplementu Sourcea koji drzi.
 * 
 * @author Ivan Relic
 *
 */
public class BooleanOperatorNOT extends BooleanOperator {

    
    /**Konstruktor koji stvara listu sourceova.
     * 
     * @param source BooleanSource kojeg predajemo super konstruktoru.
     */
    public BooleanOperatorNOT(BooleanSource source) {
	super(addToList(source));
    }

    
    /**Metoda vraca NOT vrijednost izraza koji sadrzi u sebi.
     * 
     * @return Vrijednost izraza.
     */
    public BooleanValue getValue() {
	
	//uzmi BooleanValue prvog(i jedinog) clana iz liste
	BooleanValue value = this.getSources().get(0).getValue();
	
	//ako mu je vrijednost TRUE, vrati FALSE
	if (value == BooleanValue.FALSE) {
	    return BooleanValue.TRUE;
	}
	
	//ako mu je vrijednost FALSE, vrati TRUE
	else if (value == BooleanValue.TRUE) {
	    return BooleanValue.FALSE;
	}
	
	//ako mu je vrijednost DONT CARE, isto to i vrati
	else {
	    return BooleanValue.DONT_CARE;
	}
	
    }

    
    /**Metoda koja stavlja BooleanSource u listu tog tipa, za potrebe konstruktora.
     * 
     * @param source Objekt kojeg stavljamo u listu.
     * @return Nova lista BooleanSourcea.
     */
    private static List<BooleanSource> addToList(BooleanSource source) {
	List<BooleanSource> lista = new ArrayList<BooleanSource>();
	lista.add(source);
	return lista;
    }
}
