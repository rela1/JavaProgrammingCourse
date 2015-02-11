package hr.fer.zemris.java.filechecking.syntax;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.filechecking.lexical.FCTokenType;
import hr.fer.zemris.java.filechecking.lexical.FCTokenizer;
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
import hr.fer.zemris.java.filechecking.syntax.nodes.TestNode;

/**
 * Implementacija parsera za izvorni kod programa.
 * 
 * @author Ivan Relic
 * 
 */
public class FCParser {

	// regex za valjane varijable
	private static final String VARIABLE = "[_a-zA-Z\\.][a-zA-Z0-9_\\.]*";

	// regex za valjane file tipove
	private static final String FILE_TYPE = "(d)|(di)|(dir)|(f)|(fi)|(fil)|(file)";

	private FCTokenizer tokenizer;
	private ProgramNode tree;
	private Stack<FCNode> stack;

	public FCParser(FCTokenizer tokenizer) {
		stack = new Stack<>();
		tree = new ProgramNode();
		stack.push(tree);
		this.tokenizer = tokenizer;
		parse();
	}

	/**
	 * Dohvaca stablo naredbi nastalo parsiranjem.
	 * 
	 * @return program node tree
	 */
	public ProgramNode getTree() {
		return tree;
	}

	/**
	 * Metoda predstavlja implementaciju parsera rekurzivnim spustom.
	 */
	private void parse() {
		while (true) {

			// ako naidjes na zatvaranje bloka znakom '}', makni jedan cvor sa
			// stoga
			if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.BLOCK_CLOSE) {
				popFromStack();
				while (tokenizer.getCurrentToken().getTokenType() == FCTokenType.BLOCK_CLOSE) {
					popFromStack();
				}
			}

			// ako je kraj programa, gotovo je
			if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.EOF) {
				if (stack.size() > 1) {
					throw new FCParserException("Too many '{' tags");
				}
				if (stack.size() < 1) {
					throw new FCParserException("Too many '}' tags");
				}
				break;
			}

			// inace, mora doci kljucna rijec
			// ako je naredba invertirana, zapamti to
			boolean inverted = false;
			if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.INVERTER) {
				inverted = true;
				tokenizer.nextToken();
			}
			if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.KEYWORD) {
				throw new FCParserException("Keyword expected!");
			}

			// ucitaj kljucnu rijec i obavi parsiranje
			parseStatement(inverted);
		}
	}

	/**
	 * Metoda za parsiranje naredbi.
	 * 
	 * @param inverted
	 *            je li trenutna nareda koja se cita invertirana ili ne
	 * @return node s odgovarajucom naredbom
	 */
	private void parseStatement(boolean inverted) {

		if (tokenizer.getCurrentToken().getValue().equals("def")) {
			// naredba def ne smije biti invertirana
			if (inverted) {
				throw new FCParserException("Bad keyword after inverter!");
			}
			tokenizer.nextToken();
			addChildOnTopElement(parseDef());
			return;
		}
		if (tokenizer.getCurrentToken().getValue().equals("terminate")) {
			// naredba terminate ne smije biti invertirana
			if (inverted) {
				throw new FCParserException("Bad keyword after inverter!");
			}
			tokenizer.nextToken();
			addChildOnTopElement(parseTerminate());
			return;
		}
		if (tokenizer.getCurrentToken().getValue().equals("exists")) {
			tokenizer.nextToken();
			ExistsNode exists = parseExists(inverted);
			checkForOpenBlocks(exists);
			return;
		}
		if (tokenizer.getCurrentToken().getValue().equals("filename")) {
			tokenizer.nextToken();
			FilenameNode fileName = parseFilename(inverted);
			checkForOpenBlocks(fileName);
			return;
		}
		if (tokenizer.getCurrentToken().getValue().equals("format")) {
			tokenizer.nextToken();
			FormatNode format = parseFormat(inverted);
			checkForOpenBlocks(format);
			return;
		}
		if (tokenizer.getCurrentToken().getValue().equals("fail")) {
			tokenizer.nextToken();
			FailNode fail = parseFail(inverted);
			checkForOpenBlocks(fail);
			return;
		}

		// inace, nepoznata naredba
		throw new FCParserException("Unknown keyword in statement!");
	}

	/**
	 * Kreira parsirani cvor za FailNode.
	 * 
	 * @param inverted
	 *            je li test invertiran ili ne
	 * @return novi FailNode cvor
	 */
	private FailNode parseFail(boolean inverted) {
		MessageNode message = checkForWarningMessage();
		return new FailNode(inverted, message);
	}

	/**
	 * Kreira parsirani cvor za FormatNode.
	 * 
	 * @param inverted
	 *            je li test invertiran ili ne
	 * @return novi FormatNode cvor
	 */
	private FormatNode parseFormat(boolean inverted) {
		if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.IDENT) {
			throw new FCParserException("Identifier for format name expected!");
		}
		String format = (String) tokenizer.getCurrentToken().getValue();
		tokenizer.nextToken();
		MessageNode message = checkForWarningMessage();
		return new FormatNode(inverted, format, message);
	}

	/**
	 * Kreira parsirani cvor za FilenameNode.
	 * 
	 * @param inverted
	 *            je li test invertiran ili ne
	 * @return novi FilenameNode cvor
	 */
	private FilenameNode parseFilename(boolean inverted) {
		boolean caseSensitive = caseSensitiveStringCheck();
		MessageNode fileName = parseMessage(caseSensitive);
		MessageNode message = checkForWarningMessage();
		return new FilenameNode(inverted, fileName, message);
	}

	/**
	 * Vraca cvor parsirane naredbe exists.
	 * 
	 * @param inverted
	 *            je li naredba invertirana ili ne
	 * @return cvor naredbe exists
	 */
	private ExistsNode parseExists(boolean inverted) {
		if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.IDENT) {
			throw new FCParserException("File type identifier expected!");
		}
		String fileType = (String) tokenizer.getCurrentToken().getValue();
		if (!fileType.matches(FILE_TYPE)) {
			throw new FCParserException("Bad file type identifier!");
		}

		// provjeri je li zastavica za case sensitivity ispred stringa za put i
		// procitaj put u PathNode
		tokenizer.nextToken();
		boolean caseSensitive = caseSensitiveStringCheck();
		checkStringIdent();
		PathNode path = parsePath(caseSensitive);
		checkStringIdent();

		// ako je definiran warning string procitaj ga kao message node
		MessageNode message = checkForWarningMessage();

		return new ExistsNode(inverted, isDir(fileType), message, path);
	}

	/**
	 * Parsira naredbu terminate.
	 * 
	 * @return
	 */
	private FCNode parseTerminate() {
		return new TerminateNode();
	}

	/**
	 * Vraca cvor parsirane naredbe def.
	 * 
	 * @param inverted
	 *            je li naredba invertirana ili ne
	 * @return cvor naredbe def
	 */
	private DefNode parseDef() {
		if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.IDENT) {
			throw new FCParserException("Variable name expected!");
		}
		String variableName = (String) tokenizer.getCurrentToken().getValue();
		if (!variableName.matches(VARIABLE)) {
			throw new FCParserException("Bad variable name!");
		}
		tokenizer.nextToken();

		// provjeri ima li path oznaku za insensitivity
		boolean caseSensitive = caseSensitiveStringCheck();
		checkStringIdent();
		PathNode path = parsePath(caseSensitive);
		checkStringIdent();
		return new DefNode(variableName, path);
	}

	/**
	 * Kreira parsirani MessageNode cvor.
	 * 
	 * @param caseSensitive
	 *            je li string koji slijedi case sensitive
	 * @return novi MessageNode
	 */
	private MessageNode parseMessage(boolean caseSensitive) {
		List<String> messageElements = new ArrayList<>();
		checkStringIdent();
		readIdentAndVarSupst(messageElements);
		checkStringIdent();
		return new MessageNode(messageElements, caseSensitive);
	}

	/**
	 * Vraca parsirani put.
	 * 
	 * @param caseSensitive
	 *            je li put case sensitive ili nije
	 * @return path node
	 */
	private PathNode parsePath(boolean caseSensitive) {
		List<String> pathElements = new ArrayList<String>();

		readIdentAndVarSupst(pathElements);

		// provjeri slijedi li definicija paketa
		if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.COLON) {
			tokenizer.nextToken();
			if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.IDENT) {
				throw new FCParserException("Package expected after colon!");
			}
			String packageDefinition = (String) tokenizer.getCurrentToken()
					.getValue();
			pathElements.add(packageConverter(packageDefinition));
			tokenizer.nextToken();
		}

		// ako je lista path elemenata prazna, baci exception jer nisi ništa
		// procitao
		if (pathElements.size() == 0) {
			throw new FCParserException("Path should not be empty string!");
		}
		return new PathNode(pathElements, caseSensitive);
	}

	/**
	 * Provjerava ima li otvorenih blokova naredbe iza trenutne test naredbe, te
	 * ako ima, poduzima potrebne korake za uspjesno daljnje parsiranje.
	 * 
	 * @param node
	 *            cvor za kojeg se provjerava
	 */
	private void checkForOpenBlocks(TestNode node) {

		// dodaj trenutno parsirani cvor kao dijete trenutnom cvoru na vrhu
		// stoga
		addChildOnTopElement(node);

		// ako token odgovara znaku '{' nakon parsirane naredbe, postavi
		// parsiranu naredbu
		// na vrh stoga
		if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.BLOCK_OPEN) {
			stack.push(node);
			tokenizer.nextToken();
		}
	}

	/**
	 * Uklanja trenutni cvor s vrha stoga ukoliko je to moguce, ukoliko nije,
	 * baca exception zbog krivog broja '}' znakova
	 */
	private void popFromStack() {
		try {
			stack.pop();
		} catch (EmptyStackException e) {
			throw new FCParserException("Too many '}' tags");
		}
		tokenizer.nextToken();
	}

	/**
	 * Metoda prima cvor kao argument i pokusava ga postaviti kao dijete
	 * trenutnom cvoru koji je na vrhu stoga.
	 * 
	 * @param node
	 *            cvor koji zelimo postaviti
	 */
	private void addChildOnTopElement(FCNode node) {
		try {
			((ProgramNode) stack.peek()).add(node);
		} catch (EmptyStackException e) {
			throw new FCParserException("Too many '}' tags!");
		}
	}

	/**
	 * Provjerava slijedi li warning message te ako slijedi, parsira ga u
	 * MessageNode.
	 * 
	 * @param caseSensitive
	 * @return
	 */
	private MessageNode checkForWarningMessage() {
		MessageNode message = null;
		if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.WARNING_STRING) {
			tokenizer.nextToken();
			message = parseMessage(false);
		}
		return message;
	}

	/**
	 * Metoda provjerava slijedi li identifikator za pocetak/kraj stringa.
	 */
	private void checkStringIdent() {
		if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.STRING_IDENT) {
			throw new FCParserException("String identifier '\"' expected!");
		}
		tokenizer.nextToken();
	}

	/**
	 * Provjerava identifikator za filetype i vraca true ako je identifikator za
	 * dir, a false ako je identifikator za file.
	 * 
	 * @param fileType
	 *            string identifikatora za filetype
	 * @return true ako je dir, false ako je file
	 */
	private boolean isDir(String fileType) {
		return (fileType.charAt(0) == 'd' ? true : false);
	}

	/**
	 * Metoda provjerava je li string koji slijedi case sensitive ili ne.
	 * 
	 * @return true ako je case sensitive, false inace (false ako ima zastavicu
	 *         'i' ispred)
	 */
	private boolean caseSensitiveStringCheck() {
		if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.INSENSITIVE_STRING) {
			tokenizer.nextToken();
			return false;
		}
		return true;
	}

	/**
	 * Metoda prima listu u koju sprema redom procitane identifikatore i
	 * supstitucije varijabli.
	 * 
	 * @param pathElements
	 *            lista u koju se pohranjuju procitani elementi
	 */
	private void readIdentAndVarSupst(List<String> pathElements) {
		while (true) {
			// ako je obican identifikator, procitaj ga i pohrani u listu
			if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.IDENT) {
				pathElements.add((String) tokenizer.getCurrentToken()
						.getValue());
				tokenizer.nextToken();
				continue;
			}

			// ako je supstitucija za varijablu, provjeri jesu li escaperi i
			// zagrade u ispravnom redoslijedu
			if (tokenizer.getCurrentToken().getTokenType() == FCTokenType.ESCAPER) {
				tokenizer.nextToken();
				if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.BLOCK_OPEN) {
					throw new FCParserException(
							"Block opener expected after supstitution escaper!");
				}
				tokenizer.nextToken();
				if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.IDENT) {
					throw new FCParserException(
							"Variable expected after supstitution!");
				}
				if (!((String) tokenizer.getCurrentToken().getValue()).trim()
						.matches(VARIABLE)) {
					throw new FCParserException("Bad variable name!");
				}
				pathElements.add(variableToString((String) tokenizer
						.getCurrentToken().getValue()));
				tokenizer.nextToken();
				if (tokenizer.getCurrentToken().getTokenType() != FCTokenType.BLOCK_CLOSE) {
					throw new FCParserException(
							"Block closer expected after variable supstitution!");
				}
				tokenizer.nextToken();
				continue;
			}

			// ako nije nijedan od elemenata, breakaj
			break;
		}
	}

	/**
	 * Metoda konvertira definiciju paketa u strukturu foldera.
	 * 
	 * @param packageDefinition
	 *            definicija paketa
	 * @return struktura foldera u obliku stringa
	 */
	private String packageConverter(String packageDefinition) {
		return "/" + packageDefinition.replace('.', '/');
	}

	/**
	 * Metoda wrappa supstitucijske varijable u oblik ${'varijabla'}
	 * 
	 * @param value
	 *            ime varijable
	 * @return wrappana varijabla
	 */
	private String variableToString(String value) {
		return "${" + value + "}";
	}

}
