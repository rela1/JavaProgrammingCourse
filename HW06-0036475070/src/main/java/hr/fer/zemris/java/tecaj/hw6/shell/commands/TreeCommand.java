package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu tree.
 * 
 * @author Ivan Relic
 *
 */
public class TreeCommand implements ShellCommand {

	/**
	 * Metoda rekurzivno ispisuje sadrzaj primljenog direktorija.
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nije predan tocno 1 argument
		if (arguments.length != 1) {
			return writeError(out, "The only one argument should be directory! Please put your "
					+ "path in \"signs\"");
		}
		
		//kreiraj file i provjeri postoji li i je li direktorij
		File dir = new File(arguments[0]);		
		if (!dir.exists() || !dir.isDirectory()) {
			return writeError(out, "Argument should be directory! Please put your path in"
						+ " \"signs\"");
		}
		
		//proseci se rekurzivno svim mapama i datotekama u danoj mapi
		try {
			Files.walkFileTree(dir.toPath(), new TreePrinter(out));
		} catch (IOException e) {
			return writeError(out, "Printing dir tree failed!");
		}
		
		return ShellStatus.CONTINUE;
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
	
	/**
	 * Interklasa za implementaciju filevisitora koji nam je potreban za setanje mapom.
	 * 
	 * @author Ivan Relic
	 *
	 */
	static class TreePrinter implements FileVisitor<Path> {

		private BufferedWriter out;
		private int tab = 0;
		
		/**
		 * Javni konstruktor prima buffered writer na koji mora pisati.
		 * 
		 * @param out buffered writer
		 */
		public TreePrinter(BufferedWriter out) {
			this.out = out;
		}
		
		/**
		 * Metoda obavlja ono sto treba napraviti prije ulaska u direktorij.
		 */
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			
			String name;
			
			if (tab == 0) {
				name = String.format("%s%s%n", "", dir.getName(dir.getNameCount()-1));
			}
			
			else {
				name = String.format("%"+tab+"s%s%n", "", dir.getName(dir.getNameCount()-1));
			}
			tab += 3;
			writeOut(out, name);
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Metoda obavlja ono sto treba napraviti kada je naisla na file.
		 */
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			
			String name;
			
			name = String.format("%"+tab+"s%s%n", "", file.getName(file.getNameCount()-1));
			writeOut(out, name);
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Metoda obavlja ono sto treba napraviti ako ne uspije posjetiti file.
		 */
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Metoda obavlja ono sto treba napraviti nakon izlaska iz direktorija.
		 */
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			
			tab -= 3;
			return FileVisitResult.CONTINUE;
		}	
		
		/**
		 * Metoda prima buffered writer i poruku i ispisuje poruku na writer.
		 * 
		 * @param out buffered writer
		 * @param message poruka za ispis
		 */
		private void writeOut(BufferedWriter out, String message) {
			
			try {
				out.write(message);
				out.flush();
			} catch (IOException e1) {
				throw new RuntimeException("Can't write to output stream!");
			}
		}
	}
}