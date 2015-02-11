package hr.fer.zemris.bool;


/**Sucelje za sve razrede koji su sposobni generirati legalni BooleanValue i imaju pridruzeno
 * ime.
 * 
 * @author Ivan Relic
 *
 */
public interface NamedBooleanSource extends BooleanSource {

    //jedina metoda koju razred koji implementira ovo sucelje mora podrzavati
    public String getName();
}
