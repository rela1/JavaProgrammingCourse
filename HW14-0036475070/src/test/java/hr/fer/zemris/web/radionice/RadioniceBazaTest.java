package hr.fer.zemris.web.radionice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class RadioniceBazaTest {

	@Test
	public void CitanjeIZapisivanjeBazeTest() {
		Path dir = null;
		try {
			RadioniceBaza bazaProcitana = RadioniceBaza.ucitaj("./baza");
			dir = Files.createTempDirectory(Paths.get("."), "test");
			bazaProcitana.snimi(dir.toString());
			Path radionice1 = Paths.get("./baza/radionice.txt");
			Path radionice2 = Paths.get(dir + "/radionice.txt");
			if (!checkFiles(radionice1, radionice2)) {
				deleteTempFolder(dir);
				fail();
			}
			Path radioniceOprema1 = Paths.get("./baza/radionice_oprema.txt");
			Path radioniceOprema2 = Paths.get(dir + "/radionice_oprema.txt");
			if (!checkFiles(radioniceOprema1, radioniceOprema2)) {
				deleteTempFolder(dir);
				fail();
			}
			Path radionicePublika1 = Paths.get("./baza/radionice_publika.txt");
			Path radionicePublika2 = Paths.get(dir + "/radionice_publika.txt");
			if (!checkFiles(radionicePublika1, radionicePublika2)) {
				deleteTempFolder(dir);
				fail();
			}
			deleteTempFolder(dir);
			assertTrue(true);
		} catch (IOException e) {
			try {
				deleteTempFolder(dir);
			} catch (IOException ignorable) {
			}
		}
	}

	@Test
	public void DatabaseIncosistencyExceptionTest() {
		Path dir = null;
		try {
			RadioniceBaza bazaProcitana = RadioniceBaza.ucitaj("./baza");
			Radionica radionica = bazaProcitana.dohvati((long) 1);
			radionica.getOprema().add(new Opcija("101", "USB stick"));
			dir = Files.createTempDirectory(Paths.get("."), "test");
			bazaProcitana.snimi(dir.toString());
			fail();
		} catch (InconsistentDatabaseException e) {
			try {
				deleteTempFolder(dir);
			} catch (IOException ignorable) {
			}
			assertTrue(true);
		} catch (IOException e2) {
			try {
				deleteTempFolder(dir);
			} catch (IOException ignorable) {
			}
		}
	}

	/**
	 * Briše folder i sve fileove u njemu.
	 * 
	 * @param dir
	 *            direktorij za brisanje
	 * @throws IOException
	 */
	private void deleteTempFolder(Path dir) throws IOException {
		File[] files = dir.toFile().listFiles();
		for (File file : files) {
			Files.delete(file.toPath());
		}
		Files.delete(dir);
	}

	/**
	 * Provjerava jesu li dva filea lista (liniju po liniju).
	 * 
	 * @param file1
	 *            prvi file za usporedbu
	 * @param file2
	 *            drugi file za usporedbu
	 * @return true ako su fileovi isti, false inace
	 */
	private boolean checkFiles(Path file1, Path file2) {
		try {
			List<String> file1Lines = Files.readAllLines(file1,
					StandardCharsets.UTF_8);
			List<String> file2Lines = Files.readAllLines(file2,
					StandardCharsets.UTF_8);
			return file1Lines.equals(file2Lines);
		} catch (Exception e) {
			return false;
		}

	}
}
