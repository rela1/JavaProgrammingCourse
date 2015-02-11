package hr.fer.zemris.bool;


/**Sucelje za sve razrede koji su sposobni generirati legalnu boolean funkciju.
 * 
 * @author Ivan Relic
 *
 */
public interface BooleanFunction extends NamedBooleanSource {
    
    //propisane metode koje klasa koja implementira ovo sucelje mora imati
    public boolean hasMinterm(int index);
    public boolean hasMaxterm(int index);
    public boolean hasDontCare(int index);
    
    public Iterable<Integer> mintermIterable();
    public Iterable<Integer> maxtermIterable();
    public Iterable<Integer> dontCareIterable();
}
