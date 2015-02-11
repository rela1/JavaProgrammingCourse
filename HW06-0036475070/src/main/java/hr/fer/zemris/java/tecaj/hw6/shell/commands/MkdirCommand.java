package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu mkdir.
 * 
 * @author Ivan Relic
 *
 */
public class MkdirCommand implements ShellCommand {

	/**
	 * Metoda kreira novi direktorij s predanim imenom.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nije primljen path do direktorija
		if (arguments.length != 1) {
			try {
				out.write("mkdir command should have only 1 argument! Please put your path in"
					+ " \"signs\"");
				out.newLine();
				out.flush();
				return ShellStatus.CONTINUE;
			} catch (IOException e) {
				throw new RuntimeException("Can't write to output stream!");
			}
		}
		
		try {
			
			//kreiraj cijelo stablo predanih direktorija ako direktorij vec ne postoji
			File dir = new File(arguments[0]);
			if (!dir.exists()) {
				
				dir.mkdirs();
				
				try {
					out.write("Created directory with name: " + arguments[0]);
					out.newLine();
					out.flush();
					return ShellStatus.CONTINUE;
				} catch (IOException e1) {
					throw new RuntimeException("Can't write to output stream!");
				}			
		}
			
		} catch (Exception e) {
			
			try {
				out.write("Couldn't create directory!");
				out.newLine();
				out.flush();
				return ShellStatus.CONTINUE;
			} catch (IOException e1) {
				throw new RuntimeException("Can't write to output stream!");
			}			
		}
		
		return ShellStatus.CONTINUE;
	}

}
