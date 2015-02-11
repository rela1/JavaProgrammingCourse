package hr.fer.zemris.java.filechecking.lexical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Tokenizator izvornog ulaznog programa.
 * 
 * @author Ivan Relic
 * 
 */
public class FCTokenizer {

	private char[] data;
	private int position;
	private FCToken currentToken;

	// varijabla odredjuje nalazimo li se trenutno u stringu ili izvan njega
	boolean inString = false;

	private static final Map<Character, FCTokenType> typeMap;
	// inicijalizacija staticke mape tipova tokena koji se mogu generirati
	// iskljucivo jednim znakom
	static {
		typeMap = new HashMap<Character, FCTokenType>();
		typeMap.put('{', FCTokenType.BLOCK_OPEN);
		typeMap.put('}', FCTokenType.BLOCK_CLOSE);
		typeMap.put('$', FCTokenType.ESCAPER);
		typeMap.put(':', FCTokenType.COLON);
		typeMap.put('\"', FCTokenType.STRING_IDENT);
	}

	private static final Set<String> keywords;
	// inicijalizacija statickog seta stringova koji generiraju tokene tipa
	// keyword
	static {
		keywords = new HashSet<String>();
		keywords.add("terminate");
		keywords.add("def");
		keywords.add("exists");
		keywords.add("filename");
		keywords.add("format");
		keywords.add("fail");
	}

	/**
	 * Konstruktor. Prima izvorni kod ulaznog programa i extracta prvi token.
	 * Baca RuntimeException ako dodje do pogreske pri extractanju tokena.
	 * 
	 * @param input
	 *            izvorni kod ulaznog programa
	 */
	public FCTokenizer(String input) {
		position = 0;
		data = input.toCharArray();
		extractNextToken();
	}

	/**
	 * Vraca trenutni token, moze se zvati neogranicen broj puta i uvijek vraca
	 * isti token sve dok se ne izluci novi metodom extractNextToken.
	 * 
	 * @return trenutni token
	 */
	public FCToken getCurrentToken() {
		return currentToken;
	}

	/**
	 * Metoda izlucuje sljedeci token ukoliko je to moguce (ako nije, baca
	 * RuntimeException), postavlja ga kao trenutnog te ga odmah i vraca.
	 * 
	 * @return novoizluceni token
	 */
	public FCToken nextToken() {
		extractNextToken();
		return currentToken;
	}

	/**
	 * Metoda izlucuje sljedeci token iz izvornog koda programa ukoliko je to
	 * moguce (ako nije kraj inputa) i ako ne dodje do nikakve greske (u tom
	 * slucaju RuntimeException).
	 * 
	 */
	private void extractNextToken() {

		// ako je utvrdjen kraj, ponovni poziv ove metode je pogreska, baci
		// exception
		if (currentToken != null
				&& currentToken.getTokenType() == FCTokenType.EOF) {
			throw new FCTokenizerException(
					"There are no more tokens available!");
		}

		// invertiraj trenutno stanje varijable koja odredjuje je li citanje
		// unutar stringa
		// ili izvan njega
		if (currentToken != null
				&& currentToken.getTokenType() == FCTokenType.STRING_IDENT) {
			inString = !inString;
		}

		// preskoci razmake i provjeri jesi li dosao do kraja inputa, ako jesi,
		// generiraj eof token
		skipSpaces();
		if (position >= data.length) {
			currentToken = new FCToken(FCTokenType.EOF, null);
			return;
		}

		// ako je znak neki od onih koji se mogu generirati na temelju samo
		// jednog znaka, generiraj ga, postavi kao trenutni i vrati se
		FCTokenType type = typeMap.get(Character.valueOf(data[position]));
		if (type != null) {
			currentToken = new FCToken(type, null);
			position++;
			return;
		}

		// provjeri imas li znak za string upozorenja ili case insensitive
		// string ispred znaka za sami string
		if (position < data.length - 1) {
			if (data[position] == '@' && data[position + 1] == '\"') {
				currentToken = new FCToken(FCTokenType.WARNING_STRING, null);
				position++;
				return;
			}
			if (data[position] == 'i' && data[position + 1] == '\"') {
				currentToken = new FCToken(FCTokenType.INSENSITIVE_STRING, null);
				position++;
				return;
			}
		}

		// ako si naisao na znak '!' i nisi u stringu, generiraj token
		if (!inString && data[position] == '!') {
			currentToken = new FCToken(FCTokenType.INVERTER, null);
			position++;
			return;
		}

		// ako znak ne generira token, dok je trenutni znak valjan, citaj i vidi
		// sto je
		if (correctCharacter(data[position])) {
			int startIndex = position;
			position++;
			while (position < data.length && correctCharacter(data[position])) {
				position++;
			}
			String value = new String(data, startIndex, position - startIndex);

			// ako je dobiveni identifikator direktno keyword ili micuci prvi
			// znak '!', kreiraj ga
			if (keywords.contains(value)
					|| (value.charAt(0) == '!' && keywords.contains(value
							.substring(1)))) {
				currentToken = new FCToken(FCTokenType.KEYWORD, value);
				return;
			}

			// inace kreiraj obicni identifikator
			currentToken = new FCToken(FCTokenType.IDENT, value);
			return;
		}

		// inace, ne prepoznajem taj tip tokena, baci exception
		throw new FCTokenizerException("Unknown token!");
	}

	/**
	 * Metoda provjerava je li znak valjan za kreiranje identifikatora. Ako je,
	 * vraca true, inace false.
	 * 
	 * @param c
	 *            znak cija se valjanost provjerava
	 * @return true ako je znak valjan, false inace
	 */
	private boolean correctCharacter(char c) {

		// ako je znak neki od nedopustenih za stringove/identifiere, vrati
		// false, inace true
		if (c == '{' || c == '}' || c == '$' || c == '@' || c == '\"'
				|| c == '\n' || c == '\r' || c == '\t' || c == ':' || c == '\\') {
			return false;
		}

		// ako se ne nalazis u stringu, i trenutni znak je ' ', to nije korektan
		// znak
		if (!inString && data[position] == ' ') {
			return false;
		}
		return true;
	}

	/**
	 * Metoda pomice kazaljku pozicije tako da preskace sve prazne znakove.
	 */
	private void skipSpaces() {
		while (position < data.length && !inString) {
			char c = data[position];
			if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				position++;
				continue;
			}
			break;
		}
	}

}
