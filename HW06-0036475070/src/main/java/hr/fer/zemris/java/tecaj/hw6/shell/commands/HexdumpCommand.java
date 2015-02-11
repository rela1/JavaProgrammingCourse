package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu hexdump.
 * 
 * @author Ivan Relic
 *
 */
public class HexdumpCommand implements ShellCommand {

	/**
	 * Metoda radi hexdump predanog filea.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nije predan valjani path u "znakovima"
		if (arguments.length != 1) {
			return writeError(out, "Pass only 1 argument! Please put your path in"
					+ " \"signs\"");
		}	
		
		//dohvati argument i provjeri je li valjani file i postoji li
		File file = new File(arguments[0]);		
		if (!file.exists() || !file.isFile()) {
			return writeError(out, "Argument should be file! Please put your path in"
						+ " \"signs\"");
		}
		
		//dohvati velicinu filea u double da izracunas potreban broj redaka
		Double size = new Double(file.length());
		int rows = (int) Math.ceil(size.doubleValue() / 16);
		
		StringBuilder dump = new StringBuilder();
		
		try {
			InputStream	input = new FileInputStream(file);

			//kreiraj sve retke
			for (int i = 0; i < rows; i++) {
				
				//dumpu dodaj trenutni broj retka * 16 zapisan u hexa kodu
				String location = Integer.toHexString(i * 16).toUpperCase();
				
				//ako je hexa vrijednost manje duljine od 8, paddaj je do te duljine
				location = (location.length() < 8) ? hexPadding(location) : location;
				
				dump.append(location + ": ");
				
				//2 buildera koji simultano grade dio s hex i char vrijednostima
				StringBuilder hexRow = new StringBuilder();
				StringBuilder charRow = new StringBuilder();
				
				charRow.append(" ");
				
				//popuni redak
				for (int j = 0; j < 16; j++) {
					
					//procitaj bajt
					int bajt = input.read();
					
					//ako je procitan -1, u oba rowa zapisi razmak (u hex dvostruki)
					if (bajt == -1) {
						hexRow.append("  ");
						charRow.append(" ");
					}
					
					//inace, procitan je valjani bajt
					else {
						
						//zapisi u hexrow hex vrijednost bajta
						String hex = Integer.toHexString(bajt).toUpperCase();
						
						//ako je hex vrijednost duljine manje od 2, dodaj 0 ispred
						hexRow.append((hex.length() < 2) ? "0"+hex : hex);
						
						//dodaj u charRow char vrijednost bajta ako je unutar 32-127, inace .
						charRow.append((bajt>=32 && bajt <=127) ? (char)bajt : ".");
					}
					
					//ako si na 8. bajtu dodaj |, inace razmak
					hexRow.append((j == 7) ? "|" : " ");
				}
				
				//dodaj hexRowu razmak na kraj i appendaj mu charRow
				hexRow.append(" |" + charRow.toString());
				
				//apendaj dumpu hexRow i newline
				dump.append(hexRow.toString() + System.getProperty("line.separator"));
			}
			
			input.close();
			
		} catch (FileNotFoundException e) {
			return writeError(out, "File doesn't exist! Please put your path in"
						+ " \"signs\"");
		} catch (IOException e) {
			return writeError(out, "Error reading from file!");
		}

		//ispisi dump na output stream
		try {
			out.write(dump.toString());
			out.flush();
		} catch (IOException e1) {
			throw new RuntimeException("Can't write to output stream!");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Metoda hex nizu znakova nadodaje 0 ako je duljine manje od 8.
	 * 
	 * @param hex niz hex znakova
	 * @return novi paddan hex niz
	 */
	private String hexPadding(String hex) {
		
		StringBuilder padder = new StringBuilder();
		
		for (int i = 0, length = 8 - hex.length(); i < length; i++) {
			padder.append("0");
		}
		
		return padder.append(hex).toString();
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
