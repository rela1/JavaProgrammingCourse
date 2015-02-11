package hr.fer.zemris.java.filechecking.lexical;

/**
 * Enumeracija tipova tokena ulaznog programa.
 * 
 * @author Ivan Relic
 * 
 */
public enum FCTokenType {
	/** Oznacava kraj inputa tokena **/
	EOF,
	/** Identifikator **/
	IDENT,
	/** Kljucna rijec **/
	KEYWORD,
	/** Dvotocka **/
	COLON,
	/** Escaper za varijable **/
	ESCAPER,
	/** Identifikator za string **/
	STRING_IDENT,
	/** Inverter za znacenje naredbe **/
	INVERTER,
	/** Pocetak stringa koji se ispisuje u slucaju pada testa **/
	WARNING_STRING,
	/** Otvorena viticasta zagrada **/
	BLOCK_OPEN,
	/** Zatvorena viticasta zagrada **/
	BLOCK_CLOSE,
	/** Pocetak case insensitive stringa **/
	INSENSITIVE_STRING,
	/** Oznacava token koji sadrzi paket **/
	PACKAGE_STRING
}
