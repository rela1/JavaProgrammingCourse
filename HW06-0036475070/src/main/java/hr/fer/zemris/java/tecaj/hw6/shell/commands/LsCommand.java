package hr.fer.zemris.java.tecaj.hw6.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.java.tecaj.hw6.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw6.shell.ShellStatus;

/**
 * Implementacija sucelja ShellCommand s konkretnom implementacijom za naredbu ls.
 * 
 * @author Ivan Relic
 *
 */
public class LsCommand implements ShellCommand {

	/**
	 * Metoda ispisuje podatke o sadrzaju predanog direktorija. Pathovi se obavezno pisu u " ".
	 * 
	 * @param in input reader
	 * @param out output writer
	 * @param arguments argumenti naredbe
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		//ako nije dobar broj argumenata
		if (arguments.length != 1) {
			return writeError(out, "Command ls should contain 1 argument! Please put your path in"
					+ " \"signs\"");
		}
		
		File dir = new File(arguments[0]);
		
		//ako direktorij ne postoji
		if (!dir.exists() || !dir.isDirectory()) {
			return writeError(out, "Directory doesn't exist! Please put your path in"
					+ " \"signs\"");
		}
		
		//kreiraj niz fileova unutar tog direktorija
		File[] files = dir.listFiles();
		
		//iteriraj fileovima i ispisuj red po red
		for (File f : files) {
			try {
				String line = getLine(f);
				out.write(line);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				return writeError(out, "Error reading file!");
			}
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
	 * Metoda uzima file i vraca informacije o njemu u obliku stringa.
	 * 
	 * @param f file
	 * @return string s informacijama
	 * @throws IOException 
	 */
	private String getLine(File f) throws IOException {
		
		StringBuilder line = new StringBuilder();
		int sizeWidth = Long.toString(f.length()).length();
		
		//appendaj potrebne informacije - je li direktorij, moze li se citati, pisati i izvrsavati
		line.append((f.isDirectory()) ? "d" : "-");
		line.append((f.canRead()) ? "r" : "-");
		line.append((f.canWrite()) ? "w" : "-");
		line.append((f.canExecute()) ? "x" : "-");
		
		line.append(" ");
		
		//appendaj 10 - sirina velicine filea razmaka
		for (int i = 0, length = (10-sizeWidth); i < length; i++) {
			line.append(" ");
		}
		
		//appendaj velicinu
		line.append(f.length());
		
		line.append(" ");
		
		//appendaj vrijeme nastanka
		line.append(getDate(f));
		
		line.append(" ");
		
		//appendaj ime
		line.append(f.getName());
		
		return line.toString();
	}

	/**
	 * Metoda vraca datum nastanka filea u obliku stringa.
	 * 
	 * @param f file
	 * @return string 
	 * @throws IOException
	 */
	private String getDate(File f) throws IOException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(f.toPath(), 
				BasicFileAttributeView.class, 
				LinkOption.NOFOLLOW_LINKS
				);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		return sdf.format(new Date(fileTime.toMillis()));
	}

}
