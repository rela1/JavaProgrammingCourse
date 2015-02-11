package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu copy.
 * 
 * @author Ivan Relic
 *
 */
public class CopyCommand implements ShellCommand {

	private static final int BUFFER = 4096;

	/**
	 * Metoda kopira file s predanog patha na zadani.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nisu predana dva argumenta
		if (arguments.length != 2) {
			return writeError(out, "Command copy should have 2 arguments! Please put your path in"
					+ " \"signs\"");
		}
		
		File original = new File(arguments[0]);
		File copy = new File(arguments[1]);
				
		//ako original nije file dojavi to korisniku
		if (!original.isFile()) {
			return writeError(out, "First argument should be file! Please put your path in"
					+ " \"signs\"");
		}
		
		//ako je copy direktorij, promijeni path u direktorij/original
		if (copy.isDirectory()) {
			copy = new File(arguments[1] + "/" + (new File(arguments[0])).getName());
		}
		
		//ako su original i copy isti, ne radi nista i dojavi korisniku
		if (original.equals(copy)) {
			return writeError(out, "Original file and copy are the same, copy done!");
		}
		
		//ako copy nije direktorij i postoji, pitaj korisnika zeli li overwrite
		if (!copy.isDirectory() && copy.exists()) {
			try {
				out.write("Destination file already exists, Y for overwrite N for stop: ");
				out.flush();
				
				//procitaj odgovor korisnika s input streama, ako je ne, zavrsi izvodenje,
				//inace, nastavi
				String response = in.readLine();
				if (response.equals("N")) {
					return ShellStatus.CONTINUE;
				}
				
				//ponavljaj dok odgovor nije jednak Y ili N
				while (!response.equals("Y") && !response.equals("N")) {
					out.write("Destination file already exists, Y for overwrite N for stop: ");
					out.flush();
					response = in.readLine();
					if (response.equals("N")) {
						return ShellStatus.CONTINUE;
					}
				}
				
			} catch (IOException e1) {
				throw new RuntimeException("Can't write to output stream!");
			}
		}
				
		try {
			
			//prekopiraj fileove
			copyFiles(original, copy);
			
			//ispisi korisniku sto si napravio
			try {
				out.write("Copied file: " + original + " to: " + copy);
				out.newLine();
				out.flush();
			} catch (IOException e1) {
				throw new RuntimeException("Can't write to output stream!");
			}
			return ShellStatus.CONTINUE;
			
		} catch (IOException e) {
			return writeError(out, "Error while copying files!");
		}
	}
	
	private void copyFiles(File original, File copy) throws IOException {
		
		//tu ce izbaciti FileNotFound exception ako ne uspije
		InputStream input = new BufferedInputStream(
				new FileInputStream(original), BUFFER);
		
		OutputStream output = new BufferedOutputStream(
				new FileOutputStream(copy, true), BUFFER);
		
		//citaj original i pisi u kopiju
		byte[] data = new byte[BUFFER];
		while ((input.read(data)) != -1) {
            output.write(data);
        };
		
		//zatvori citac/pisac
		input.close();
		output.close();
	}

	/**
	 * Metoda ispisuje pogresku korisniku i vraca CONTINUE shellu.
	 * 
	 * @param out izlazni writer
	 * @param message poruka za ispis
	 * @return ShellStatus
	 */
	private ShellStatus writeError(BufferedWriter out, String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e1) {
			throw new RuntimeException("Can't write to output stream!");
		}
		return ShellStatus.CONTINUE;
	}

}
