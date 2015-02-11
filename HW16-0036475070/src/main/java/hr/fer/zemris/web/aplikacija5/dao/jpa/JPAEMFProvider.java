package hr.fer.zemris.web.aplikacija5.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider koji vraća factory entity managera.
 * 
 * @author Ivan Relić
 * 
 */
public class JPAEMFProvider {

	public static EntityManagerFactory emf;

	/**
	 * Vraća entity manager factory.
	 * 
	 * @return entity manager factoriy
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Postavlja entity manager factory.
	 * 
	 * @param emf
	 *            entity manager factory
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}