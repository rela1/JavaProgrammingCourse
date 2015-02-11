package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu charsets.
 * 
 * @author Ivan Relic
 *
 */
public class CharsetsCommand implements ShellCommand {

	/**
	 * Metoda ispisuje podrzane charsetove na trenutnoj platformi.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		if (arguments.length != 0) {
			try {
				out.write("Command charsets should not contain any argument!");
				out.newLine();
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException("Can't write to output stream!");
			}
			return ShellStatus.CONTINUE;
		}
		
		//dohvati listu dostupnih charsetova i ispisi ih
		try {
			
			List<String> charsetovi = new ArrayList<String>(Charset.availableCharsets().keySet());
			out.write("Supported charsets are: ");
			out.newLine();
			
			//iteriraj kroz dostupne charsetove i ispisuj ih svakog u novi red
			for (int i = 0, length = charsetovi.size(); i < length; i++) {
				out.write(charsetovi.get(i) + " ");
				out.newLine();
			}	

			out.flush();
			
		} catch (IOException e) {
			throw new RuntimeException("Can't write to output stream!");
		}
		
		return ShellStatus.CONTINUE;
	}

}
