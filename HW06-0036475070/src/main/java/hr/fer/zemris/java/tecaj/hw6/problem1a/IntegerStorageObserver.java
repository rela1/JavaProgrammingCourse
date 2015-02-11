package hr.fer.zemris.java.tecaj.hw6.problem1a;

/**
 * Sucelje za sve implementacije koje su sposobne promatrati "subjekt" i izvrsiti odredjenu akciju
 * kada dodje do promjene nad objektom koji promatraju.
 * 
 * @author Ivan Relic
 *
 */
public interface IntegerStorageObserver {
	
	/**
	 * Jedina metoda koju implementacije sucelja moraju podrzavati. Metoda izvodi akciju koju je
	 * potrebno izvrsiti kada dodje do promjene nad objektom kojeg promatraju.
	 * 
	 * @param istorage subjekt koji se promatra
	 */
	public void valueChanged(IntegerStorage istorage);

}
