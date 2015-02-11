package hr.fer.zemris.web.ankete.dao;

//import java.util.ResourceBundle;

/**
 * Singleton razred koji vraća implementaciju DAO-a tako da kreira razred koji
 * pročita iz daoprovider.properties filea pod ključem providerClass.
 * 
 * @author Ivan Relić
 * 
 */
public class DAOProvider {

	// hardkodirano je da je DAO provider primjerak razreda SQLDAO
	private static DAO dao = new SQLDAO();

	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	public static DAO getDao() {
		return dao;
	}

}
