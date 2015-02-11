package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

import java.util.Scanner;


/**Klasa za instanciranje objekta koji sluzi za parsiranje dokumenata zeljene strukture.
 * 
 * @author Ivan Relic
 *
 */
public class SmartScriptParser {

	//stog za pomoc pri kreiranju parse stabla
	private ObjectStack stack = new ObjectStack();
	
	//korijen stabla u koji dodajemo procitane Nodeove
	private DocumentNode root = new DocumentNode();
	
	//za appendanje jer se koristi appendanje svakog pojedinog znaka, optimizacije radi
	private StringBuilder buffer;
	
	
	/**Konstruktor prima string koji zelimo parsirati i prosljeduje ga metodi za parsiranje.
	 * 
	 * @param docBody String koji zelimo parsirati.
	 */
	public SmartScriptParser(String documentBody) {
		parseDocument(documentBody);
	}
	
	
	/**Metoda vraca stablo kreirano parsiranjem.
	 * 
	 * @return DocumentNode, tj. korijen stabla.
	 */
	public DocumentNode getDocumentNode() {
		return root;
	}

	
	/**Parsira dobiveni string i gradi stablo Nodeova koji se sastoje od tokena.
	 * 
	 * @param docBody String koji zelimo parsirati.
	 */
	private void parseDocument(String docBody) {
		
		//gurni root, tj. DocumentNode na stog
		stack.push(root);
		
		//pretvori kompletan string u polje charova
		char[] docBodyArray = docBody.toCharArray(); 
		int duljina = docBodyArray.length;
		
		//instanciraj buffer velicine duljine, to je max sto ces trebati
		buffer = new StringBuilder(duljina);
		
		//boolean varijable za provjeru je li tag escapean ili nije dobro zatvoren
		boolean tagOpened = false;
		boolean tagEscaped = false;
		
		for(int i = 0; i < duljina; i++) {
						
			//ako si dosao do ne-escapeanog pocetka taga
			if ((docBodyArray[i] == '{') && !tagEscaped) {
				
				//ako si dosao do kraja datoteke, a tag je otvoren, baci exception
				if (i == duljina - 1) {
					throw new SmartScriptParserException("Bad tag identifier!");
				}
				
				//inace, ako sljedeci znak nije $, baci exception
				else if (docBodyArray[i+1] != '$') {
					throw new SmartScriptParserException("Bad tag identifier!");
				}
				
				//zapamti index pocetka taga
				int pocetakTaga = i;
				
				//postavi da si otvorio tag {$
				tagOpened = true;
				
				for (; i < duljina; i++) {
										
					//ako tag nije pravilno zavrsen, tag je nepravilno zadan i baci exception

					if ((docBodyArray[i] == '}') && (docBodyArray[i-1] != '$')) {
								throw new SmartScriptParserException("Bad tag identifier!");
							}
					
					//kraj taga, sastavi ga u string, posalji na provjeru i dodavanje u stablo
					if ((docBodyArray[i] == '}') && (docBodyArray[i-1] == '$')) {
						String tag = "";
						for(int j = pocetakTaga; j <= i; j++) {
							tag = tag + docBodyArray[j];
						}
						tagCheck(tag);
						
						/*kad se vratis s provjere taga, postavi da tag nije otvoren i kreni 
						dalje u parsiranje teksta */
						tagOpened = false;
						break;
					}
				}
				
				
			}
						
			//inace, samo nadodaj trenutni znak u buffer
			else {
				buffer.append(docBodyArray[i]);
			}
			
			//ako nisi na kraju dokumenta i trenutni znak je \\, a sljedeci {, escapeaj tag
			if (i < duljina - 1) {
				if (docBodyArray[i] == '\\' && docBodyArray[i+1] == '{') {
					tagEscaped = true;
					
					//ukloni znak escapera iz buffera
					buffer.setLength(buffer.length()-1);
				}
				
				//inace, tag nije escapean
				else {
					tagEscaped = false;
				}
			}
		}
		
		//ako je ostalo teksta nakon sto si iskocio iz petlje, dodaj ga
		addTextNode();
				
		//ako je neki tag ostao otvoren a prosao si cijeli dokument, baci exception
		if (tagOpened) {
			throw new SmartScriptParserException("Bad tag identifier!");
		}
		
		/*nakon zavrsene obrade, provjeri ima li na stogu 1 element, ako nema, znaci
		da postoji visak/manjak FOR/END naredbi, baci exception */
		if (stack.size() != 1) {
			throw new SmartScriptParserException("Too many FOR tags/too few END tags!");
		}
		
	}

	
	/**Metoda koja tekst iz string buildera ubacuje na stog kao dijete trenutnog elementa na
	 * vrhu.
	 * 
	 */
	private void addTextNode() {
		
		String text = buffer.toString();
		
		//ako imas nesto u bufferu
		if (text.length() > 0) {
			
			TextNode pom = new TextNode(text);
			try {
				
				//uzmi element s vrha i njemu za dijete dodaj novi TextNode
				((Node) stack.peek()).addChildNode(pom);
			} catch (EmptyStackException e) {
				throw new SmartScriptParserException("Too many END tags!");
			}
		}
		
		//ocisti buffer
		buffer.setLength(0);
	}


	/**Dobiva moguceg reprezenta taga i provjerava je li on uistinu to i obavlja funkcije
	 * koje je potrebno za svaki pojedini.
	 * 
	 * @param tag Prima string.
	 */
	private void tagCheck(String tag) {
		
		//prvo ubaci tekst koji si do sada procitao, ako ga ima
		addTextNode();
		
		/*ako je tag valjana FOR naredba, oblika varijabla token token (token), zadnji ne 
		 * treba biti naveden */
		if(tag.matches("\\{\\$(\\s)*[fF][oO][rR](\\s)*.*(\\s)*.*(\\s)*.*(\\s)*\\$\\}")) {
			
			//ocisti tag od prva dva, zadnja dva znaka ({$, $}) i kljucne rijeci
			tag = tag.substring(2, tag.length());
			tag = tag.replaceFirst("[fF][oO][rR]", "");
			tag = tag.substring(0, tag.length()-2);
			
			//kolekcija u koju ces spremati tokene iz for taga
			ArrayBackedIndexedCollection kolekcija = new ArrayBackedIndexedCollection();
			
			parseTag(tag, kolekcija);
			
			
			ForLoopNode forNaredba;
			
			//ako je u kolekciji samo 3 tokena, pozovi takav konstruktor za ForLoopNode
			if (kolekcija.size() == 3) {
				forNaredba = new ForLoopNode((TokenVariable)kolekcija.get(0), 
						(Token)kolekcija.get(1), (Token)kolekcija.get(2));
			}
			
			//ako je zadan i step, pozovi takav konstruktor za ForLoopNode
			else if (kolekcija.size() == 4) {
				forNaredba = new ForLoopNode((TokenVariable)kolekcija.get(0), 
						(Token)kolekcija.get(1), (Token)kolekcija.get(2), (Token)kolekcija.get(3));
			}
			
			//inace, ako nema ni 3 ni 4 argumenta, baci exception
			else {
				throw new SmartScriptParserException("Bad FOR tag!");
			}
			
			//postavi ForLoopNode kao dijete trenutnog objekta na vrhu stoga
			try {
				((Node) stack.peek()).addChildNode(forNaredba);
			} catch (EmptyStackException e) {
				throw new SmartScriptParserException("Too many END tags!");
			}
			
			//gurni ForLoopNode na stog
			stack.push(forNaredba);			
		}
		
		//ako je tag valjana END naredba
		else if (tag.matches("\\{\\$(\\s)*[eE][nN][dD](\\s)*\\$\\}")) {
			try {
				stack.pop();
			} catch (EmptyStackException e) {
				
				//ako je stog prazan, postoji END naredbi viska, baci exception
				throw new SmartScriptParserException("Too many END tags/too few FOR tags!");
			}
		}
		
		//ako je tag = naredba
		else if (tag.matches("\\{\\$(\\s)*=.*(\\s)*\\$\\}")) {
			
			//pomocna kolekcija za pohranjivanje Tokena
			ArrayBackedIndexedCollection kolekcija = new ArrayBackedIndexedCollection();
			
			//ocisti tag od prva dva, zadnja dva znaka ({$, $}) i kljucne rijeci
			tag = tag.substring(2, tag.length());
			tag = tag.replaceFirst("=", "");
			tag = tag.substring(0, tag.length()-2);
			
			parseTag(tag, kolekcija);
			
			/*kad si zavrsio s obradom, spakiraj tokene u niz Token[], instanciraj EchoNode
			i posalji na stablo*/
			int duljina = kolekcija.size();
			Token[] nizTokena = new Token[duljina];
			
			for(int i = 0; i < duljina; i++) {
				nizTokena[i] = (Token) kolekcija.get(i);
			}
			
			EchoNode echoNode = new EchoNode(nizTokena);
			
			try {
				((Node) stack.peek()).addChildNode(echoNode);
			} catch (EmptyStackException e) {
				throw new SmartScriptParserException("Too many END tags!");
			}
		}
		
		//ako tag nije nijedna od navedenih naredbi (FOR END =) baci exception
		else {
			throw new SmartScriptParserException("Wrong tag command!");
		}
	}

	
	/**Uzima string koji predstavlja tag i sprema sve tokene u kolekciju koja je predana kao
	 * argument.
	 * 
	 * @param tag String koji predstavlja "unutrasnjost" taga, bez {$ i $}
	 * @param kolekcija Kolekcija za spremanje tokena.
	 */
	private void parseTag(String tag, ArrayBackedIndexedCollection kolekcija) {
		Scanner scanner = new Scanner(tag);
		
		//pomocni string u koji ucitavamo argumente naredbe {$= redom i provjeravamo
		String argument;
		
		//dok ima sto za citati
		while(scanner.hasNext()) {
			argument = scanner.next();
		
			//ako je procitan string varijabla, pohrani je u TokenVariable pa kolekciju
			if (argument.matches("[a-zA-Z][0-9_a-zA-Z]*")) {
				TokenVariable variable = new TokenVariable(argument);
				kolekcija.add(variable);
			}
		
			/*ako je procitani string prikaz stringa, pohrani je 
			u TokenString pa kolekciju*/
			else if (isString(argument)) {
				
				//citaj string
				argument = readString(argument, scanner);
				
				//makni escapere i zamijeni ih znakovima kojima trebas
				argument = argument.replace("\\\\", "\\");
				argument = argument.replace("\\\"", "\"");
								
				//u token ga pohrani bez pocetnih i zavrsnih navodnika
				TokenString string = new TokenString(argument.substring(1, argument.length()-1));
				kolekcija.add(string);
			}
			
			//ako je procitani string operator, pohrani ga u TokenOperator pa kolekciju
			else if (isOperator(argument)) {
				TokenOperator operator = new TokenOperator(argument);
				kolekcija.add(operator);
			}
			
			/*ako je procitani string double broj, pohrani ga u TokenConstantDouble
			pa kolekciju */
			else if (isDouble(argument)) {
				TokenConstantDouble brojDouble = 
						new TokenConstantDouble(Double.parseDouble(argument));
				kolekcija.add(brojDouble);
			}
			
			/*ako je procitani string integer broj, pohrani ga u TokenConstantInteger
			pa kolekciju */
			else if (isInteger(argument)) {
				TokenConstantInteger brojInteger =
						new TokenConstantInteger(Integer.parseInt(argument));
				kolekcija.add(brojInteger);
			}
			
			//ako je procitani string funkcija, pohrani je u TokenFunction pa kolekciju
			else if (isFunction(argument)) {
				TokenFunction funkcija = new TokenFunction(argument.substring(1));
				kolekcija.add(funkcija);
			}
			
			//ako nije nista od navedenoga, baci exception
			else {
				scanner.close();
				throw new SmartScriptParserException("Tag not defined correctly!");
			}
		
		}
		
		scanner.close();
		
	}


	/**Provjerava je li predan argument valjani prikaz funkcije.
	 * 
	 * @param argument Argument za koji provjeravamo je li valjani prikaz funkcije.
	 * @return True ili false, ovisno o provjeri.
	 */
	private boolean isFunction(String argument) {
		if (argument.matches("@[a-z+A-Z][0-9+_+a-z+A-Z]*")) {
			return true;
		}
		else {
			return false;
		}
	}


	/**Provjerava je li predan argument valjani prikaz integera.
	 * 
	 * @param argument Argument za koji provjeravamo je li valjani integer.
	 * @return True ili false, ovisno o provjeri.
	 */
	private boolean isInteger(String argument) {
		if (argument.matches("[-\\+]?[0-9][0-9]*")) {
			return true;
		}
		else {
			return false;
		}
	}


	/**Provjerava je li predan argument valjani prikaz doublea.
	 * 
	 * @param argument Argument za koji provjeravamo je li valjani double.
	 * @return True ili false, ovisno o provjeri.
	 */
	private boolean isDouble(String argument) {
		if (argument.matches("[-\\+]?[0-9][0-9]*\\.[0-9]*")) {
			return true;
		}
		else {
			return false;
		}		
	}


	/**Provjerava je li predan argument valjani prikaz stringa.
	 * 
	 * @param argument Argument za koji provjeravamo je li valjani operator.
	 * @return True ili false, ovisno o provjeri.
	 */
	private boolean isString(String argument) {
		if (argument.matches("(\".*\")|(\".*)")) {
			return true;
		}
		else {
			return false;
		}
	}


	/**Provjerava je li predan argument valjani prikaz operatora.
	 * 
	 * @param argument Argument za koji provjeravamo je li valjani operator.
	 * @return True ili false, ovisno o provjeri.
	 */
	private boolean isOperator(String argument) {
		if (argument.matches("[(\\*)(\\+)(-)(/)]")) {
			return true;
		}
		
		else return false;
	}
	
	
	/**Cita string u tagovima i vraca ga.
	 * 
	 * @param argument Pocetak stringa iz taga.
	 * @param scanner Scanner kojim citamo u trenutnom tagu.
	 * @return Vraca procitani string iz taga.
	 */
	private String readString(String argument, Scanner scanner) {
		
		//ako postoje razmaci u stringu
		if (!argument.matches("(\".*\")")) {
			
			//pomocni string za citanje cijelog stringa u tagu
			String pom;
			boolean stringZavrsen = false;
			
			//dok imas sto za citati u tagu
			while (scanner.hasNext()) {
				pom = scanner.next();
				
				//ako procitana vrijednost predstavlja kraj stringa, iskoci 
				if (pom.matches(".*(?<!\\\\)\"")) {
					argument = argument + " " + pom;
					stringZavrsen = true;
					break;
				}
			
				//inace, samo nadodaj to sto si procitao jer nisi dosao do kraja
				else {
					argument = argument + " " + pom;
				}
			}
			
			//ako string nigdje nije terminiran s ", baci exception
			if (!stringZavrsen) {
				scanner.close();
				throw new SmartScriptParserException("Tag not defined correctly!");
			}
			
			//vrati string nakon citanja svih komponenti
			return argument;
			
		}
		
		//ako nema razmaka u stringu, vrati ono sto si dobio
		else {
			return argument;
		}
	}
	
}
