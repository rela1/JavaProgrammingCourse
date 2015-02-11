package hr.fer.zemris.java.filechecking.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.filechecking.FCFileVerifier;
import hr.fer.zemris.java.filechecking.FCProgramChecker;

/**
 * Klasa je container za main metodu koja testira rad FCFileVerifiera koji
 * izvrsava ulazni program.
 * 
 * @author Ivan Relic
 * 
 */
public class FCDemo {

	/**
	 * Main metoda koja vrsi ucitavanje i izvodenje izvornog koda programa za
	 * provjeru.
	 * 
	 * @param args
	 *            argumenti komandne linije; put do datoteke, originalni naziv
	 *            datoteke i put do ulaznog, izvornog koda programa za provjeru
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out
					.println("Program zahtijeva 3 argumenta: put do datoteke, "
							+ "originalni naziv datoteke, staza do ulaznog izvornog koda.");
			System.exit(-1);
		}
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("Predan zip file ne postoji!");
			System.exit(-1);
		}
		String program = ucitaj(args[2]);
		String fileName = args[1];

		FCProgramChecker checker = new FCProgramChecker(program);
		if (checker.hasErrors()) {
			System.out.println("Predani program sadrži pogreške!");
			for (String error : checker.errors()) {
				System.out.println(error);
			}
			System.out.println("Odustajem od daljnjeg rada.");
			System.exit(0);
		}

		//ovdje unijeti svoje inicijalne varijable
		Map<String, Object> initialData = new HashMap<>();
		initialData.put("jmbag", "0036475070");
		initialData.put("lastName", "Relić");
		initialData.put("firstName", "Ivan");

		FCFileVerifier verifier = new FCFileVerifier(file, fileName, program,
				initialData);

		if (!verifier.hasErrors()) {
			System.out.println("Yes! Uspjeh! Ovakav upload bi bio prihvaćen.");
		} else {
			System.out
					.println("Ups! Ovaj upload se odbija! Što nam još ne valja?");
			for (String error : verifier.errors()) {
				System.out.println(error);
			}
		}
	}

	/**
	 * Cita liniju po liniju iz ulaznog filea s izvornim kodom.
	 * 
	 * @param file
	 *            file iz kojeg citamo
	 * @return ucitan tekst
	 */
	private static String ucitaj(String file) {
		StringBuilder builder = null;
		BufferedReader br = null;
		try {
			builder = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), StandardCharsets.UTF_8));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				line = removeCommentFromLine(line);
				line = line.trim();
				if (line.isEmpty())
					continue;
				builder.append(line + System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			System.out
					.println("File s izvornim kodom programa nije pronadjen!");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Pogreska pri citanju izvornog koda!");
			System.exit(-1);
		} finally {
			try {
				br.close();
			} catch (IOException ignorable) {
			}
		}
		return builder.toString();
	}

	/**
	 * Metoda uklanja pojavljivanje komentara iz ulaznog izvornog koda programa.
	 * 
	 * @param line linija iz koje zelimo ukloniti komentare
	 * @return linija bez komentara
	 */
	private static String removeCommentFromLine(String line) {

		int position = line.indexOf('#');
		if (position >= 0) {
			line = line.substring(0, position);
		}

		return line;
	}
}