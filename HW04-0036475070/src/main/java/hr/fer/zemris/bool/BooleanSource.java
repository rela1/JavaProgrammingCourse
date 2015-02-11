package hr.fer.zemris.bool;

import java.util.List;


/**Sucelje za sve razrede koji su sposobni generirati legalni BooleanValue.
 * 
 * @author Ivan Relic
 *
 */
public interface BooleanSource {

    //propisane metode koje klasa koja implementira ovo sucelje mora imati
    public BooleanValue getValue();
    public List<BooleanVariable> getDomain();
}
