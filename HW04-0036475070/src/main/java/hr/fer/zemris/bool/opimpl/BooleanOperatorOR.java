package hr.fer.zemris.bool.opimpl;

import java.util.List;

import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanValue;


/**Klasa kreira objekt koji je container za BooleanSourceove i na zahtjev moze izracunati
 * i vratiti vrijednost koja je jednaka OR operatoru Sourceova koji drzi.
 * 
 * @author Ivan Relic
 *
 */
public class BooleanOperatorOR extends BooleanOperator {

    
    /**Konstruktor poziva nadredjeni i predaje mu listu sourceova.
     * 
     * @param sources Lista sourceova.
     */
    public BooleanOperatorOR(List<BooleanSource> sources) {
	super(sources);
    }
    
    
    /**Metoda vraca OR vrijednost svih izraza koji sadrzi u sebi.
     * 
     * @return Vrijednost izraza.
     */
    public BooleanValue getValue() {
	//dohvati listu sourceova i protrci kroz nju, ako je barem jedan TRUE, value je TRUE
	List<BooleanSource> lista = this.getSources();
		
	//boolean varijabla kojom provjeravamo sadrzi li lista koju DONT CARE vrijednost
	boolean sadrziDontCare = false;
	
	for(int i = 0, length = lista.size(); i < length; i++) {
	    
	    //ako je TRUE, vrati TRUE jer je za OR dovoljno da je barem 1 TRUE
	    if (lista.get(i).getValue() == BooleanValue.TRUE) {
		return BooleanValue.TRUE;
	    }
	    
	    else if (lista.get(i).getValue() == BooleanValue.DONT_CARE) {
		sadrziDontCare = true;
	    }
	}
		
	//prosao si kroz listu, nisi naisao ni na jedan TRUE, vrati DONT CARE ili FALSE
	return sadrziDontCare ? BooleanValue.DONT_CARE : BooleanValue.FALSE;
    }

}
