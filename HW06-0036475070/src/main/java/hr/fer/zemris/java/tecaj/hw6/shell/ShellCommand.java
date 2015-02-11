package hr.fer.zemris.java.tecaj.hw6.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Sucelje koje svaka naredba shella mora implementirati.
 * 
 * @author Ivan Relic
 *
 */
public interface ShellCommand {

	/**
	 * Metoda koju svaka implementacija sucelja ShellComand mora overrideati. Metoda izvrsava 
	 * naredbu.
	 * 
	 * @param in stream s kojeg komanda cita podatke
	 * @param out stream na koji komanda zapisuje podatke
	 * @param arguments niz korisnickih argumenata
	 * @return status za shell
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments);
}
