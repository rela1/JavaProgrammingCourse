package hr.fer.zemris.web.aplikacija5.dao.jpa;

import hr.fer.zemris.web.aplikacija5.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Klasa je pružatelj usluga konekcije prema bazi podataka preko entity
 * managera.
 * 
 * @author Ivan Relić
 * 
 */
public class JPAEMProvider {

	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Dohvaća entity managera za rad s bazom podataka.
	 * 
	 * @return entity manager
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Zatvara vezu s entity managerom.
	 * 
	 * @throws DAOException
	 *             u slučaju pogreške prilikom zatvaranja veze
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null) {
			throw dex;
		}
	}

	/**
	 * Klasa enkapsulira jedan entity manager.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	private static class LocalData {
		EntityManager em;
	}

}