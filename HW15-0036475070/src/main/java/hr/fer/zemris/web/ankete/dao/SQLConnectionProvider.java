package hr.fer.zemris.web.ankete.dao;

import java.sql.Connection;

/**
 * Pohrana veza prema bazi podataka u ThreadLocal object koji dohvaća objekte po
 * ključu trenutne dretve.
 * 
 * @author Ivan Relić
 * 
 */
public class SQLConnectionProvider {

	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Postavi vezu za trenutnu dretvu (ili obriši zapis iz mape ako je argument
	 * <code>null</code>).
	 * 
	 * @param con
	 *            veza prema bazi
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Dohvati vezu koju trenutna dretva (pozivatelj) smije koristiti.
	 * 
	 * @return vezu prema bazi podataka
	 */
	public static Connection getConnection() {
		return connections.get();
	}
}
