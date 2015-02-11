package hr.fer.zemris.bool.opimpl;

import java.util.List;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;


/**Klasa kreira objekt koji je container za BooleanSourceove i na zahtjev moze izracunati
 * i vratiti vrijednost koja je jednaka AND operatoru Sourceova koji drzi.
 * 
 * @author Ivan Relic
 *
 */
public class BooleanOperatorAND extends BooleanOperator {

    
    /**Konstruktor poziva nadredjeni i predaje mu listu sourceova.
     * 
     * @param sources Lista sourceova.
     */
    public BooleanOperatorAND(List<BooleanSource> sources) {
	super(sources);
    }

    /**Metoda vraca AND vrijednost svih izraza koji sadrzi u sebi.
     * 
     * @return Vrijednost izraza.
     */
    public BooleanValue getValue() {
	
	//dohvati listu sourceova i protrci kroz nju, ako je barem jedan FALSE, value je FALSE
	List<BooleanSource> lista = this.getSources();
	
	boolean imaDontCare = false;
	for(int i = 0, length = lista.size(); i < length; i++) {
	    
	    //pomocni BooleanValue
	    BooleanValue pom = lista.get(i).getValue();
	    
	    //ako je vrijednost trenutnog iz liste jednaka FALSE, vrati FALSE
	    if (pom == BooleanValue.FALSE) {
		return BooleanValue.FALSE;
	    }
	    
	    if (pom == BooleanValue.DONT_CARE) {
		imaDontCare = true;
	    }
	}
	
	/*ako si prosao kroz cijelu listu i nisi naisao na FALSE, ako ima koji DONT CARE,
	vrati DONT CARE, inace vrati TRUE */
	return imaDontCare ? BooleanValue.DONT_CARE : BooleanValue.TRUE;
	
    }

}
