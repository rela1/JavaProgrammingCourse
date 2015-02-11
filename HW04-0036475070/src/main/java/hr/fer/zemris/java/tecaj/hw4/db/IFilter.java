package hr.fer.zemris.java.tecaj.hw4.db;


/**Sucelje koje moraju zadovoljiti razredi za provjeravanje u bazi podataka.
 * 
 * @author Ivan Relic
 *
 */
public interface IFilter {

    //jedina metoda koju objekti koji implementiraju ovo sucelje moraju imati
    public boolean accepts(StudentRecord record);
}
