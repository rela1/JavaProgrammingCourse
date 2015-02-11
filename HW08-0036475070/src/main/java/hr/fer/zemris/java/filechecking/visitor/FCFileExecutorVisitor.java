package hr.fer.zemris.java.filechecking.visitor;

import hr.fer.zemris.java.filechecking.FCExecutorException;
import hr.fer.zemris.java.filechecking.syntax.nodes.DefNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.ExistsNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FCNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FailNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FilenameNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.FormatNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.MessageNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.PathNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.ProgramNode;
import hr.fer.zemris.java.filechecking.syntax.nodes.TerminateNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Predstavlja implementaciju sucelja visitora cvorova koji pri posjeti cvoru
 * izvrsava ono sto ulazni program zahtijeva.
 * 
 * @author Ivan Relic
 * 
 */
public class FCFileExecutorVisitor implements FCNodeVisitor {

	private String fileName;
	private Map<String, Object> variables;
	private Set<String> files;
	private Set<String> folders;
	private List<String> messages;
	private boolean terminated = false;

	/**
	 * Metoda vraca informaciju je li izvrsivac terminiran naredbom terminate.
	 * 
	 * @return true ako je terminiran, false ako nije
	 */
	public boolean isTerminated() {
		return terminated;
	}

	/**
	 * Dohvaca skup poruka generiranih od strane izvodenja programa.
	 * 
	 * @return lista poruka
	 */
	public List<String> getMessages() {
		return messages;
	}

	public FCFileExecutorVisitor(File file, String fileName,
			Map<String, Object> initialData) throws ZipException, IOException {
		if (file == null || fileName == null || initialData == null) {
			throw new IllegalArgumentException("Arguments should not be null!");
		}
		this.fileName = fileName;
		this.variables = new HashMap<String, Object>(initialData);
		this.messages = new ArrayList<String>();
		this.files = new HashSet<String>();
		this.folders = new HashSet<String>();
		// dohvati sve fileove i foldere iz predanog zip filea
		getAllFilesAndFolders(file);
	}

	/**
	 * Pri posjeti DefNode cvoru potrebno je definirati varijablu imenom
	 * definiranim u samom cvoru koja ce predstavljati path takodjer definiran u
	 * cvoru.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(DefNode node) {
		String variableName = node.getVariableName();
		String path = visit(node.getPath());
		variables.put(variableName, path);
	}

	/**
	 * Pri posjeti ExistsNode cvoru potrebno je provjeriti ispravnost testa i
	 * ovisno o tome izvrsiti ili blok naredbi ili ispisati poruku pogreske ako
	 * postoji, ako ne, onda genericku.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(ExistsNode node) {
		String message = visit(node.getErrorMessage());
		// dohvati path i njegov case sensitivity
		String pathString = visit(node.getPath());
		boolean caseSensitive = node.getPath().getCaseSensitivity();
		// napravi XOR izmedju invertiranosti testa i uspjesnosti testa jer to
		// odredjuje koji
		// blok naredbi se mora izvrsavati
		boolean inverted = node.isInverted();
		boolean isDir = node.isDir();
		boolean testPass = (isDir ? checkForPath(pathString, caseSensitive,
				folders) : checkForPath(pathString, caseSensitive, files))
				^ inverted;
		if (testPass) {
			executeChilds(node);
			return;
		}
		messages.add(message == null ? pathString + (inverted ? ("") : (" ne"))
				+ " postoji u predanom fileu!" : message);
		return;
	}

	/**
	 * Cvor predstavlja test koji je po definiciji neuspjesan, pa ovisno o
	 * invertiranosti, izvodi se ili blok naredbi ili se ispisuje string
	 * upozorenja.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(FailNode node) {
		String message = visit(node.getErrorMessage());
		boolean inverted = node.isInverted();
		if (inverted) {
			executeChilds(node);
			return;
		}
		messages.add(message == null ? ((inverted ? ("U") : ("Neu")) + "spjesan test!")
				: message);
		return;
	}

	/**
	 * Pri posjeti cvoru potrebno je provjeriti odgovara li ime filea onome
	 * kojeg cvor sadrzi, i ovisno o tome izvrsiti blok naredbi ili ispisuje
	 * odgovarajucu poruku pogreske.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(FilenameNode node) {
		String fileName = visit(node.getName());
		String message = visit(node.getErrorMessage());
		boolean caseSensitive = node.getName().getCaseSensitivity();
		boolean inverted = node.isInverted();

		// napravi XOR izmedju invertiranosti testa i uspjesnosti testa jer to
		// odredjuje koji
		// blok naredbi se mora izvrsavati
		boolean testPass = inverted ^ checkName(fileName, caseSensitive);
		if (testPass) {
			executeChilds(node);
			return;
		}
		messages.add(message == null ? ("Ime predano testu " + (inverted ? ("o")
				: ("ne o") + "dgovara stvarnome!"))
				: message);
		return;

	}

	/**
	 * Metoda provjerava odgovara li trazeni format datoteke stvarnome i ovisno
	 * o tome izvodi ili blok naredbi ili ispisuje poruku o padu testa.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(FormatNode node) {
		String message = visit(node.getErrorMessage());
		String format = node.getFormat();
		boolean inverted = node.isInverted();

		// napravi XOR izmedju invertiranosti testa i uspjesnosti testa jer to
		// odredjuje koji
		// blok naredbi se mora izvrsavati
		boolean testPass = inverted ^ checkFormat(format);
		if (testPass) {
			executeChilds(node);
			return;
		}
		messages.add(message == null ? ("Predan format filea"
				+ (inverted ? (" ") : (" ne ")) + "odgovara stvarnome!")
				: message);
		return;
	}

	/**
	 * Vraca string koji predstavlja cjelokupnu poruku cvora (sa supstituiranim
	 * varijablama, itd.) Ako je message node null (kod parsiranja, ako poruka
	 * nije definirana) vraca null, a ako je velicina liste poruka 0, nema
	 * elemenata pa se ispisuje prazna poruka.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 * @return string s cjelokupnom porukom cvora
	 */
	public String visit(MessageNode node) {
		if (node == null) {
			return null;
		}
		if (node.getMessageElements().size() == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String element : node.getMessageElements()) {
			builder.append(checkVarSupst(element));
		}
		return builder.toString();
	}

	/**
	 * Vraca string koji predstavlja cjelokupni path cvora (sa supstituiranim
	 * varijablama, itd.)
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 * @return string s cjelokupnim pathom cvora
	 */
	public String visit(PathNode node) {
		StringBuilder builder = new StringBuilder();
		for (String element : node.getPathElements()) {
			builder.append(checkVarSupst(element));
		}
		return builder.toString();
	}

	/**
	 * Nailaskom na ovaj cvor treba se terminirati izvodenje ulaznog programa.
	 * 
	 * @param node
	 *            cvor koji se posjecuje
	 */
	public void visit(TerminateNode node) {
		this.terminated = true;
	}

	/**
	 * Provjerava nalazi li se predani path u setu svih fileova ili foldera
	 * predanog filea.
	 * 
	 * @param pathString
	 *            path koji zelimo provjeriti
	 * @param caseSensitive
	 *            je li provjera case sensitive ili ne
	 * @param pathSet
	 *            u kojem setu se provjerava postoji li path
	 * @return true ako je taj file sadrzan, false inace
	 */
	private boolean checkForPath(String pathString, boolean caseSensitive,
			Set<String> pathSet) {
		// ako path na kraju sadrzi znak '/', ukloni ga
		if (pathString.charAt(pathString.length() - 1) == '/') {
			pathString = pathString.substring(0, pathString.length() - 1);
		}
		if (caseSensitive) {
			return pathSet.contains(pathString);
		} else {
			for (String path : pathSet) {
				if (path.toUpperCase().equals(pathString.toUpperCase())) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Metoda prima cvor i izvrsava operacije koje zahtijevaju cvorovi koji su
	 * njegova djeca.
	 * 
	 * @param node
	 *            cvor ciju djecu egzekutiramo
	 */
	private void executeChilds(ProgramNode node) {
		List<FCNode> childs = node.getStatements();
		if (childs == null) {
			return;
		}
		for (FCNode childNodes : childs) {
			if (terminated) {
				break;
			}
			childNodes.acceptVisitor(this);
		}
	}

	/**
	 * Ovisno o case sensitivityu provjerava odgovara li predano ime stvarnom
	 * imenu ulaznog filea koji se provjerava.
	 * 
	 * @param fileName
	 *            ime s kojim provjeravamo
	 * @param caseSensitive
	 *            je li provjera case sensitive ili nije
	 * @return true ako imena odgovaraju, false inace
	 */
	private boolean checkName(String fileName, boolean caseSensitive) {
		if (caseSensitive) {
			return fileName.equals(this.fileName);
		}
		return fileName.toUpperCase().equals(this.fileName.toUpperCase());
	}

	/**
	 * Metoda provjerava odgovara li predani format stvarnom u ulaznoj datoteci.
	 * 
	 * @param format
	 *            predani format
	 * @return true ako odgovaraju, false inace
	 */
	private boolean checkFormat(String format) {
		return format.toUpperCase().equals("ZIP");
	}

	/**
	 * Provjerava je li trenutni element supstitucija varijable ili obican
	 * string, ako je supstitucija, trazi navedenu varijablu u mapi varijabli,
	 * ako ne postoji, izaziva exception, a ako postoji uzima vrijednost i vraca
	 * je. Ako vrijednost nije supstitucija varijable, vraca istu tu vrijednost.
	 * 
	 * @param element
	 *            vrijednost za koju se provjerava je li supstitucija varijable
	 *            ili obican string
	 * @return string s podacima
	 */
	private String checkVarSupst(String element) {
		// ako prvi znak stringa ne odgovara supstituciji varijable samo ga
		// vrati, inace potrazi
		// u mapi vrijednosti
		if (element.charAt(0) != '$') {
			return element;
		}
		element = element.substring(2, element.length() - 1);
		if (!variables.containsKey(element)) {
			throw new FCExecutorException("Variable " + element
					+ " doesn't exist!");
		}
		return (String) variables.get(element);
	}

	/**
	 * Iz predanog filea dohvaca sva imena fileova i foldera koji su
	 * hijerarhijski "ispod" njega, tj. koji mu pripadaju.
	 * 
	 * @param file
	 *            file ciju hijerarhiju imena zelimo
	 * @throws IOException
	 * @throws ZipException
	 */
	private void getAllFilesAndFolders(File file) throws ZipException,
			IOException {
		@SuppressWarnings("resource")
		ZipFile zip = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				String name = entry.getName();
				// pohrani ime foldera bez zadnjeg znaka '/'
				folders.add(name.substring(0, name.length() - 1));
			} else {
				files.add(entry.getName());
			}
		}
	}

}
