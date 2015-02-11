package hr.fer.zemris.java.tecaj.hw6.shell;

import hr.fer.zemris.java.tecaj.hw6.shell.commands.CatCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.CopyCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.ExitCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.HexdumpCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.LsCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.MkdirCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.commands.TreeCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Klasa je container za main metodu koja simulira rad shella.
 * 
 * @author Ivan Relic
 *
 */
public class MyShell {

	private static char PROMPT = '>';
	private static char MULTILINE = '|';
	private static char MORELINES = '\\';
	private static String MORELINES_REGEX = Character.toString(MORELINES) 
			+ Character.toString(MORELINES);
	
	/**
	 * Main metoda koja simulira rad shella.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException  {
		
		//ucitaj dostupne naredbe u mapu
		Map<String, ShellCommand> commands = new HashMap<String, ShellCommand>();
		commands.put("symbol", new SymbolChangeCommand());
		commands.put("charsets", new CharsetsCommand());
		commands.put("cat", new CatCommand());
		commands.put("exit", new ExitCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("ls", new LsCommand());
		commands.put("copy", new CopyCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("tree", new TreeCommand());
		
		//omatanje input i output streama
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		System.out.println("Welcome to MyShell v 1.0");
		
		ShellStatus status = ShellStatus.CONTINUE;
		Scanner lineScanner = new Scanner(System.in, "UTF-8");
		do {
			System.out.print(PROMPT);
			String line = lineScanner.nextLine();
			
			//ako linija zavrsava na znak MORELINES
			if (line.matches(".*" + MORELINES_REGEX)) {
				
				//ucitavaj sve dok linija zavrsava s MORELINES
				while (line.matches(".*" + MORELINES_REGEX)) {
					
					//ispisi znak za multiline
					System.out.print(MULTILINE);				
					
					//konkateniraj sljedecu liniju trenutnoj
					String newLine = lineScanner.nextLine();
					if (newLine.isEmpty()) {
						break;
					}
					line = line.concat(newLine);
				}				
								
				//ukloni pojavljivanja MORELINES charactera s razmakom
				line = line.replace(Character.toString(MORELINES), " ");
			}
			
			//ako duljina linije s argumentima nije 0
			if (!(line.length() == 0)) {
			
				//dohvati ime naredbe
				Scanner commandScanner = new Scanner(line);
				String commandName = commandScanner.next();
				
				//ako je naredba symbol, koristi standardne postavke scannera
				if (commandName.equals("symbol")) {
					commandScanner.reset();
				}
				
				//inace koristi delimiter " za citanje pathova unutar " "
				else {
					commandScanner.useDelimiter("\"");
				}
				
				//dohvati sve arugmente naredbe
				List<String> arguments = new ArrayList<String>();
				while (commandScanner.hasNext()) {
					String arg = commandScanner.next();
					
					//trimaj argumente i dodaj samo one koji nisu prazan string
					arg = arg.trim();
					if (!arg.matches("\\s*")) {
						arguments.add(arg);
					}
				}
				
				commandScanner.close();
				
				//dohvati naredbu iz mape naredbi
				ShellCommand command = commands.get(commandName);
				
				//ako ne postoji naredba, upozori korisnika i nastavi s radom
				if (command == null) {
					System.out.println("Bad command name!");
					status = ShellStatus.CONTINUE;
				}
				
				//inace, u status pohrani rezultat izvedbe komande
				else {
					status = command.executeCommand(
							in,
							out, 
							arguments.toArray(new String[arguments.size()]));
				}
			}
			
		} while(status == ShellStatus.CONTINUE);
		
		lineScanner.close();
	}
	
	/**
	 * Interklasa za implementaciju naredbe za promjenu simbola. Implementira se kao interklasa
	 * zbog lakseg pristupa clanskim varijablama koje sadrze informaciju o trenutnim simbolima
	 * za prompt / morelines / multiline unutar shella.
	 * 
	 * @author Ivan Relic
	 *
	 */
	static class SymbolChangeCommand implements ShellCommand {

		/**
		 * Metoda mijenja simbol ili ispisuje postojeci, ovisno o broju argumenata.
		 * 
		 * @param in input reader
		 * @param out output writer
		 * @param arugments argumenti naredbe
		 */
		public ShellStatus executeCommand(BufferedReader in,
				BufferedWriter out, String[] arguments) {
			
			//ako je zatrazen samo ispis simbola
			if (arguments.length == 1) {
				
				//ako je zatrazen ispis prompt simbola
				if (arguments[0].matches("PROMPT")) {
					try {
						out.write("Symbol for PROMPT is '" +PROMPT+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
				}
				
				//ako je zatrazen ispis morelines simbola
				if (arguments[0].matches("MORELINES")) {
					try {
						out.write("Symbol for MORELINES is '" +MORELINES+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
				}
				
				//ako je zatrazen ispis multiline simbola
				if (arguments[0].matches("MULTILINE")) {
					try {
						out.write("Symbol for MULTILINE is '" +MULTILINE+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
				}
			}
			
			//ako je zatrazena promjena simbola
			else if (arguments.length == 2) {
				
				//ako je zatrazena promjena prompt simbola
				if (arguments[0].matches("PROMPT")) {
					char newSymbol = arguments[1].charAt(0);
					try {
						out.write("Symbol for PROMPT changed from '" +PROMPT+ 
								"' to '" +newSymbol+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
					PROMPT = newSymbol;
				}
				
				//ako je zatrazena promjena morelines simbola
				if (arguments[0].matches("MORELINES")) {
					char newSymbol = arguments[1].charAt(0);
					try {
						out.write("Symbol for MORELINES changed from '" +MORELINES+ 
								"' to '" +newSymbol+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
					MORELINES = newSymbol;
					
					//ako je MORELINES simbol \, moras ga escapeati pa se zato radi dupli regex
					MORELINES_REGEX = (MORELINES == '\\') ? Character.toString(MORELINES) 
							+ Character.toString(MORELINES) : Character.toString(MORELINES);
					
				}
				
				//ako je zatrazena promjena multiline simbola
				if (arguments[0].matches("MULTILINE")) {
					char newSymbol = arguments[1].charAt(0);
					try {
						out.write("Symbol for MULTILINE changed from '" +MULTILINE+ 
								"' to '" +newSymbol+ "'");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException("Can't write to output stream!");
					}
					MULTILINE = newSymbol;
				}				
			}
			
			//inace, nedovoljan broj argumenata
			else {
				try {
					out.write("You should provide either 1 or 2 arguments!");
					out.newLine();
					out.flush();
				} catch (IOException e) {
					throw new RuntimeException("Can't write to output stream!");
				}
			}
			
			return ShellStatus.CONTINUE;
		}		
	}
}
