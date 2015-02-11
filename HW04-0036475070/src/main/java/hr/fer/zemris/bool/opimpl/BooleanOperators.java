package hr.fer.zemris.bool.opimpl;

import java.util.ArrayList;
import java.util.Arrays;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;


/**Klasa koja pruza static metode NOT, OR i AND.
 * 
 * @author Ivan Relic
 *
 */
public class BooleanOperators {
    
    
    /**Privatni konstruktor koji se ne koristi.
     * 
     */
    private BooleanOperators() {
	
    }
    
    /**Static factory metoda koja vraca novi objekt tipa BooleanOperatorAND.
     * 
     * @param sources Argumenti tipa BooleanSoure, varijabilan broj.
     * @return Novi objekt tipa BooleanOperatorAND
     */
    public static BooleanOperator and(BooleanSource ... sources) {
	return new BooleanOperatorAND(new ArrayList<BooleanSource>(Arrays.asList(sources)));
    }
    
    
    /**Static factory metoda koja vraca novi objekt tipa BooleanOperatorOR.
     * 
     * @param sources Argumenti tipa BooleanSoure, varijabilan broj.
     * @return Novi objekt tipa BooleanOperatorOR
     */
    public static BooleanOperator or(BooleanSource ... sources) {
	return new BooleanOperatorOR(new ArrayList<BooleanSource>(Arrays.asList(sources)));
    }
    
    
    /**Static factory metoda koja vraca novi objekt tipa BooleanOperatorNOT.
     * 
     * @param source Argument tipa BooleanSoure.
     * @return Novi objekt tipa BooleanOperatorNOT
     */
    public static BooleanOperator not(BooleanSource source) {
	return new BooleanOperatorNOT(source);
    }


}
