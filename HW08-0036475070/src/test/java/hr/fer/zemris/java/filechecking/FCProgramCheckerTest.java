package hr.fer.zemris.java.filechecking;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Klasa sluzi za testiranje ispravnosti ulaznog programa, tj. na nailaske
 * mogucih sintaksnih pogresaka. Uspjesnost ovih testova ovisi o uspjesnosti
 * pokrivenosti razreda FCParser pa je ovim testovima i on pokriven.
 * 
 * @author Ivan Relic
 * 
 */
public class FCProgramCheckerTest {

	@Test
	public void ProgramDoesntStartWithKeywordTest() {
		String program = "exis d \"test_folder\" @\"Folder ne postoji!\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: keyword expected", checker.errors()
				.get(0), "Keyword expected!");
	}

	@Test
	public void UnknownTokenTest() {
		String program = "exists d \"test_folder\" @\"Folder ne postoji!\" @ ";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Unknown token!", checker.errors()
				.get(0), "Unknown token!");
	}

	@Test
	public void DefShouldNotBeInvertedTest() {
		String program = " !def src \"src/test\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Bad keyword after inverter!", checker
				.errors().get(0), "Bad keyword after inverter!");
	}

	@Test
	public void TerminateShouldNotBeInvertedTest() {
		String program = " !terminate";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Bad keyword after inverter!", checker
				.errors().get(0), "Bad keyword after inverter!");
	}

	@Test
	public void FormatWithoutFormatIdentifierTest() {
		String program = "format @\"Poruka pogreske!\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals(
				"Pogreska mora biti: Identifier for format name expected!",
				checker.errors().get(0), "Identifier for format name expected!");
	}

	@Test
	public void ExistsWithoutFormatIdentifierTest() {
		String program = "exists \"src/main\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: File type identifier expected!",
				checker.errors().get(0), "File type identifier expected!");
	}

	@Test
	public void ExistsWithBadFormatIdentifierTest() {
		String program = "exists dirr \"src/main\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Bad file type identifier!", checker
				.errors().get(0), "Bad file type identifier!");
	}

	@Test
	public void DefWithoutVariableIdentifierTest() {
		String program = "def \"src/main\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Variable name expected!", checker
				.errors().get(0), "Variable name expected!");
	}

	@Test
	public void DefWithBadVariableIdentifierTest() {
		String program = "def 1._src \"src/main\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Bad variable name!", checker.errors()
				.get(0), "Bad variable name!");
	}

	@Test
	public void DefWithoutPackageAfterColonTest() {
		String program = "def ._._src \"src/main:\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Package expected after colon!",
				checker.errors().get(0), "Package expected after colon!");
	}

	@Test
	public void DefWithoutPathTest() {
		String program = "def ._._src \"\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Path should not be empty string!",
				checker.errors().get(0), "Path should not be empty string!");
	}

	@Test
	public void DefWithoutQuotesOnPathTest() {
		String program = "def ._._src src/main\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: String identifier '\"' expected!",
				checker.errors().get(0), "String identifier '\"' expected!");
	}

	@Test
	public void ErrorMessageWithBadVarSupst1() {
		String program = "filename \"$sdf}\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals(
				"Pogreska mora biti: Block opener expected after supstitution escaper!",
				checker.errors().get(0),
				"Block opener expected after supstitution escaper!");
	}

	@Test
	public void ErrorMessageWithBadVarSupst2() {
		String program = "filename \"${\"}\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals(
				"Pogreska mora biti: Variable expected after supstitution!",
				checker.errors().get(0),
				"Variable expected after supstitution!");
	}

	@Test
	public void ErrorMessageWithBadVarSupst3() {
		String program = "filename \"${18ser._}\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Bad variable name!", checker.errors()
				.get(0), "Bad variable name!");
	}

	@Test
	public void ErrorMessageWithBadVarSupst4() {
		String program = "filename \"${s18er._op\"";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals(
				"Pogreska mora biti: Block closer expected after variable supstitution!",
				checker.errors().get(0),
				"Block closer expected after variable supstitution!");
	}

	@Test
	public void TooManyOpenedBlocksTest() {
		String program = "filename \"${src}\" {" + "exists f \"src/main\" { }";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Too many '{' tags!", checker.errors()
				.get(0), "Too many '{' tags");
	}

	@Test
	public void TooManyClosedBlocksTest() {
		String program = "filename \"${src}\" {" + "exists f \"src/main\" } }";
		FCProgramChecker checker = new FCProgramChecker(program);
		assertEquals("Mora postojati pogreska u ulaznom programu!",
				checker.hasErrors(), true);
		assertEquals("Pogreska mora biti: Too many '}' tags!", checker.errors()
				.get(0), "Too many '}' tags");
	}
}
