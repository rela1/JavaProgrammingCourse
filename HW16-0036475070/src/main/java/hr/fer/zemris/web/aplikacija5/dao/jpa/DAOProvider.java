package hr.fer.zemris.web.aplikacija5.dao.jpa;

import hr.fer.zemris.web.aplikacija5.dao.DAO;

/**
 * Razred enkapsulira providera za pristup perzistencijskom sloju podataka.
 * 
 * @author Ivan Relić
 * 
 */
public class DAOProvider {

	private static DAO dao = new JPADAOImpl();

	/**
	 * Vraća trenutnu implementaciju DAO sučelja.
	 * 
	 * @return implementacija DAO sučelja
	 */
	public static DAO getDAO() {
		return dao;
	}

}