package hr.fer.zemris.java.filechecking;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Klasa sluzi za testiranje metode za izvodenje ulaznog programa. Uspjesnost
 * ovih testova ovisi o uspjesnosti tokenizatora i parsera pa su ovim testovima
 * pokriveni i oni.
 * 
 * Koristi se konkretni zip file za testiranje izvodjenja programa, dostupan je
 * za pregled u lib folderu projekta.
 * 
 * 
 * @author Ivan Relic
 * 
 */
public class FCFileVerifierTest {

	private File zip;
	private Map<String, Object> initialData;
	private String fileName;

	public FCFileVerifierTest() throws IOException {
		zip = new File("./lib/test.zip");
		initialData = new HashMap<String, Object>();
		initialData.put("jmbag", "0036475070");
		initialData.put("firstName", "Ivan");
		initialData.put("lastName", "Relic");
		fileName = "HW08 - 0036475070";
	}

	@Test
	public void InvertedExistStatementWithoutErrorMessageTest() {
		String program = "!exists d \"test folder\""
				+ "!exists d \"test folder/\"" + "!exists dir \"tes fold\" {"
				+ "!fail {fail @\"Direktorij ne postoji!\"}}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("test folder postoji u predanom fileu!");
		errors.add("test folder/ postoji u predanom fileu!");
		errors.add("Direktorij ne postoji!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void InvertedExistStatementWithErrorMessageTest() {
		String program = "!exists d \"test folder\" @\"Folder test postoji u fileu!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Folder test postoji u fileu!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void InvertedExistStatementForFileWithDefStatementTest() {
		String program = "def dir \"test folder\"  "
				+ "def dir2 \"${dir}/test2\"  "
				+ "def dir3 \"${dir2}/test3\" "
				+ "!exists f \"${dir3}/slika.bmp\" @\"Taj file postoji u zipu!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Taj file postoji u zipu!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void ExistStatementForFileWithDefStatementTest() {
		String program = "def dir \"test folder\"  "
				+ "def dir2 \"${dir}/test2\"  "
				+ "def dir3 \"${dir2}/test3\" "
				+ "exists f \"${dir3}/slika.bmp\" @\"Taj file postoji u zipu!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void ExistStatementForFileWithDefStatementAndBlockTest() {
		String program = "def dir \"test folder\"  "
				+ "def dir2 \"${dir}/test2\"  "
				+ "def dir3 \"${dir2}/test3\" "
				+ "exists f \"${dir3}/slika.bmp\" @\"Taj file postoji u zipu!\" {"
				+ "		fail @\"Ovo je u bloku!\"}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Ovo je u bloku!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void TerminatedExistStatementForFileWithDefStatementAndBlockTest() {
		String program = "def dir \"test folder\"  "
				+ "def dir2 \"${dir}/test2\"  "
				+ "def dir3 \"${dir2}/test3\""
				+ "terminate "
				+ "exists f \"${dir3}/slika.bmp\" @\"Taj file postoji u zipu!\" {"
				+ "		fail @\"Ovo je u bloku!\"}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void ExistCaseInsensitiveStatementForFileWithDefStatementAndBlockTest() {
		String program = "def dir \"test folder\"  "
				+ "def dir2 \"${dir}/test2\"  "
				+ "def dir3 \"${dir2}/test3\" "
				+ "exists f i\"${dir3}/SlIkA.bmp\" @\"Taj file postoji u zipu!\" {"
				+ "		fail @\"Ovo je u bloku!\"}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Ovo je u bloku!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void FileNameTest() {
		String program = "filename \"HW08 - 0036475070\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void InvertedFileNameTest() {
		String program = "!filename \"HW08 - 0036475070\"  @\"Ovo je stvarno ime!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Ovo je stvarno ime!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void CaseInsensitiveFileNameWithFailStatementInBlockTest() {
		String program = "filename i\"hw08 - 0036475070\"  {"
				+ "				fail @\"Korektno ime filea!\"   }";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Korektno ime filea!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void CaseInsensitiveBadFileNameTest() {
		String program = "!filename i\"h08 - 0036475070\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void CaseInsensitiveBadFileNameWithEmptyMessageTest() {
		String program = "filename i\"h08 - 0036475070\" @\"\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void CaseInsensitiveFileNameWithFailStatementInBlockAndVariableSupstTest() {
		String program = "filename i\"hw08 - ${jmbag}\"  {"
				+ "				fail @\"Korektno ime filea!\"   }";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Korektno ime filea!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void CaseInsensitiveFileNameWithTerminatedFailStatementInBlockAndVariableSupstTest() {
		String program = "filename i\"hw08 - ${jmbag}\"  {" + "terminate"
				+ "				fail @\"Korektno ime filea!\"   }";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void FormatTest() {
		String program = "format ziP @\"Format mora biti zip!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void InvertedFormatTest() {
		String program = "! format ziP @\"Format mora biti zip!\"";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Format mora biti zip!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void FormatWithBlockTest() {
		String program = " format ziP @\"Format mora biti zip!\" {"
				+ "				fail @\"Format je zip!\"  }";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Format je zip!");
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test
	public void InvertedFormatWithBlockTest() {
		String program = "! format asd  {}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		assertEquals("Ne smije biti poruka pogreske!", verifier.hasErrors(),
				false);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test(expected = FCExecutorException.class)
	public void UndefinedVariableTest() {
		String program = "fail @\"Autor testova - ${firstName} ${Name}, ${jmbag}.\"";
		new FCFileVerifier(zip, fileName, program, initialData);
	}

	@Test
	public void ProgramTest() {
		String program = "def src \"test folder/test1/primjer paketa:hr.fer.zemris.java.test\""
				+ "exists f \"${src}/build.xml\"  "
				+ "{fail @\"Build file postoji!\""
				+ "def src \"test folder\""
				+ "!exists fil \"${src}/test.txt\" @\"File postoji!\"}"
				+ "exists fi \"${src}/test.txt\""
				+ "{fail @\"Autor testova - ${firstName} ${lastName}, ${jmbag}.\"}";
		FCFileVerifier verifier = new FCFileVerifier(zip, fileName, program,
				initialData);
		List<String> errors = new ArrayList<String>();
		errors.add("Build file postoji!");
		errors.add("File postoji!");
		errors.add("Autor testova - Ivan Relic, 0036475070.");
		assertEquals("Moraju postojati errori!", verifier.hasErrors(), true);
		assertEquals("Liste pogresaka bi trebale biti iste!", errors,
				verifier.errors());
	}

	@Test(expected = FCException.class)
	public void BadFileTest() {
		new FCFileVerifier(new File("a"), fileName, new String(), initialData);
	}
}
