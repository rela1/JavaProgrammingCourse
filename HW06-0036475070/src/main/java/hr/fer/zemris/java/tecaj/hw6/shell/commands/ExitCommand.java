package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu exit.
 * 
 * @author Ivan Relic
 *
 */
public class ExitCommand implements ShellCommand {

	/**
	 * Metoda izlazi iz shella.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nema 0 argumenata, dojavi korisniku pogresku
		if (arguments.length != 0) {			
			try {
				out.write("Command exit should not have any arguments!");
				out.newLine();
				out.flush();
				return ShellStatus.CONTINUE;
			} catch (Exception e) {
				throw new RuntimeException("Can't write to output stream!");
			}
		}
		
		//ako je sve u redu, vrati TERMINATE status
		return ShellStatus.TERMINATE;
	}

}
