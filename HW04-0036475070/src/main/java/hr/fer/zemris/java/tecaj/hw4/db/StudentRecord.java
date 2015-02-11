package hr.fer.zemris.java.tecaj.hw4.db;


/**Klasa cije instance predstavljaju jednog studenta s pripadajucim varijablama.
 * 
 * @author Ivan Relic
 *
 */
public class StudentRecord {

    //privatne clanske varijable koje drze informacije o studentu
    private String jmbag;
    private String lastName;
    private String firstName;
    private int finalGrade;


    /**Javni konstruktor koji postavlja vrijednosti za objekt StudentRecord.
     * 
     * @param jmbag JMBAG studenta.
     * @param lastName Prezime studenta.
     * @param firstName Ime studenta.
     * @param finalGrade Zavrsna ocjena studenta.
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
	this.jmbag = jmbag;
	this.lastName = lastName;
	this.firstName = firstName;
	this.finalGrade = finalGrade;
    }
    
    
    /**Override metode hashCode.
     * 
     * @return Hash vrijednost StudentRecord objekta.
     */
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
	return result;
    }
    
    
    /**Override equals metode.
     * 
     * @param obj obj Objekt s kojim usporedjujemo StudentRecord.
     * @return True ili false, ovisno jesu li objekti StudentRecord jednaki.
     */
    public boolean equals(Object obj) {
	
	//ako se usporedjujes sa samim sobom, vrati true
	if (this == obj)
	    return true;
	
	//ako se usporedjujes s null, vrati false
	if (obj == null)
	    return false;
	
	//ako se usporedjujes s necim sto nije instanca StudentRecorda, vrati false
	if (!(obj instanceof StudentRecord))
	    return false;
	
	//dereferenciraj ono s cim se usporedjujes na StudentRecord i usporedi jmbagove
	StudentRecord other = (StudentRecord) obj;
	if (jmbag == null) {
	    if (other.jmbag != null)
		return false;	    
	} 
	
	else if (!jmbag.equals(other.jmbag)) {
	    return false;
	}
	
	//inace, ako je sve jednako, vrati true
	return true;
    }


    /**Getter metoda koja vraca JMBAG studenta.
     * 
     * @return String JMBAG-a studenta.
     */
    public String getJmbag() {
        return jmbag;
    }


    /**Getter metoda koja vraca prezime studenta.
     * 
     * @return String prezimena studenta.
     */
    public String getLastName() {
        return lastName;
    }


    /**Getter metoda koja vraca ime studenta.
     * 
     * @return String imena studenta.
     */
    public String getFirstName() {
        return firstName;
    }


    /**Getter metoda koja vraca zavrsnu ocjenu studenta.
     * 
     * @return Integer ocjena.
     */
    public int getFinalGrade() {
        return finalGrade;
    }
    
    
    
    
}
