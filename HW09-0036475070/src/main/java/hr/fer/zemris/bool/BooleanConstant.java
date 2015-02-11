package hr.fer.zemris.bool;

import java.util.ArrayList;
import java.util.List;


/**Klasa za neimenovane boolean vrijednosti.
 * 
 * @author Ivan Relic
 *
 */
public class BooleanConstant implements BooleanSource {

    //privatna clanska varijabla
    private BooleanValue value;
    
    //static vrijednosti koje se mogu pozivati bez prethodnog kreiranja objekta klase
    public static BooleanConstant TRUE = new BooleanConstant(BooleanValue.TRUE);
    public static BooleanConstant FALSE = new BooleanConstant(BooleanValue.FALSE);
    
    /**Private konstruktor koji prima vrijednost value.
     * 
     * @param value Vrijednost koju zelimo zapisati u clansku varijablu.
     */
    private BooleanConstant(BooleanValue value) {
	this.value = value;
    }
    
    
    /**Getter metoda za vracanje vrijednosti iz varijable value.
     * 
     * @return Vrijednost value varijable.
     */
    public BooleanValue getValue() {
	return this.value;
    }

    
    /**Getter metoda za vracanje domene.
     * 
     * @return Lista varijabli iz domene.
     */
    public List<BooleanVariable> getDomain() {
	return new ArrayList<BooleanVariable>();
    }

}
