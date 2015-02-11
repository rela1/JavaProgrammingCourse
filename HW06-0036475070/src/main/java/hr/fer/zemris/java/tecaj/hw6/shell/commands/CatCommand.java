package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu cat.
 * 
 * @author Ivan Relic
 *
 */
public class CatCommand implements ShellCommand {

	private static final int BUFFER = 4096;
	
	/**
	 * Metoda ispisuje sadrzaj filea u predanom charsetu, ako je predan.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		Charset charset = Charset.defaultCharset();
		
		//ako je predan dobar broj argumenata
		if (arguments.length >= 1 && arguments.length<=2) {
			
			//ako je predan i charset, pokusaj ga dohvatiti
			if (arguments.length == 2) {
				charset = Charset.availableCharsets().get(arguments[1]);
			}
			
			//ako nije pronadjen taj charset u mapi charseta, dojavi to korisniku
			if (charset == null) {
				try {
					out.write("You have passed wrong charset argument!");
					out.newLine();
					out.flush();
					return ShellStatus.CONTINUE;
				} catch (IOException e) {
					throw new RuntimeException("Can't write on output stream!");
				}
			}
			
			try {
				
				//tu ce izbaciti FileNotFound exception ako ne uspije
				InputStreamReader input = new InputStreamReader(
						new BufferedInputStream(
								new FileInputStream(arguments[0]), BUFFER), charset);
				
				//citaj file i zapisi ga na out s bufferom 4096
				char[] data = new char[BUFFER];
				int size;
				
				do {
					size = input.read(data);
					if (size != -1) {
						out.write(data, 0, size);
						out.flush();
					}
				} while (size != -1);
				
				//flushaj ako je jos sto ostalo
				out.newLine();
				out.flush();
				input.close();
				
			} catch (FileNotFoundException e) {
				
				//ispisi korisniku ako je predan los file path
				try {
					out.write("Bad file path! Please put your path in \"signs\"");
					out.newLine();
					out.flush();
					return ShellStatus.CONTINUE;
				} catch (IOException e1) {
					throw new RuntimeException("Can't write on output stream!");
				}
				
			} catch (IOException e) {
				throw new RuntimeException("Error reading/writing file!");
			}
		}
		
		//ako nije predan dobar broj argumenata
		else {
			try {
				out.write("Command cat should contain 1 or 2 arguments! "
						+ "Please put your path in \"signs\"");
				out.newLine();
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException("Can't write to output stream!");
			}
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}

}
