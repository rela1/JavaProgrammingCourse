package hr.fer.zemris.java.tecaj.hw4.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Klasa za rad sa simulacijom baze podataka.
 * 
 * @author Ivan Relic
 *
 */
public class StudentDB {

    /**Main metoda kreira bazu podataka studenata i cita zahtjeve sa stdin i ispisuje n-torke
     * koje zadovoljavaju upit.
     * 
     * @param args Argumenti komandne linije, ne koriste se.
     * @throws IOException Exception ako se ne uspije procitati.
     */
    public static void main(String[] args) throws IOException {
	
	//procitaj listu stringova, string za svaku liniju iz datoteke
	List<String> lines = Files.readAllLines(
		Paths.get("./database.txt"), StandardCharsets.UTF_8);
	
	//kreiraj bazu StudentRecordsa iz liste stringova
	StudentDatabase database = new StudentDatabase(lines);
	
	//ispisi informacije korisniku
	System.out.println("This is simple database. Supported commands are:\n"
		+ "query jmbag = \"jmbag\" and query lastName = \"lastName\". \n");
	
	//kreni citati sa stdin
	Scanner scanner = new Scanner(System.in);
	
	System.out.print(">");
	
	    while (scanner.hasNextLine()) {
		
		//procitaj liniju 
		String line = scanner.nextLine();
		
		//ako odgovara upitu za JMBAG
		if (line.matches("([qQ][uU][eE][rR][yY]\\s+[jJ][mM][bB][aA][gG]\\s*"
			+ "=\\s*\"[0-9]+\"\\s*)")) {
		    String jmbag = extractFromLiterals(line);
		    List<StudentRecord> lista = new ArrayList<StudentRecord>();
		    lista.add(database.forJMBAG(jmbag));
		    
		    //ako si nasao nekoga, ispisi
		    if (lista.get(0) != null) {
			System.out.println(printQuery(lista));
			System.out.println("Records selected: " + lista.size());
		    }
		    
		    //inace, reci da nitko ne zadovoljava taj upit
		    else {
			System.out.println("Records selected: 0");
		    }
		}
		
		//ako odgovara upitu za lastName
		else if (line.matches("([qQ][uU][eE][rR][yY]\\s+[lL][aA][sS][tT]"
			+ "[nN][aA][mM][eE]\\s*=\\s*\".*\"\\s*)")) {
		    String lastName = extractFromLiterals(line);
		    List<StudentRecord> lista;
		    IFilter lastNameMask = new LastNameFilter(lastName);
		    lista = database.filter(lastNameMask);
		    
		    //ako si nasao nekoga, ispisi
		    if (lista.size() > 0) {
			System.out.println(printQuery(lista));
			System.out.println("Records selected: "+ lista.size());
		    }
		    
		    //inace, reci da nitko ne zadovoljava taj upit
		    else {
			System.out.println("Records selected: 0");
		    }
		}
		
		//ako korisnik upise quit, zavrsi ucitavanje upita
		else if (line.matches("quit")) {
		    break;
		}
		
		//ako nije ni jmbag ni lastName upit, onda je nepoznat
		else {
		    System.out.println("Nepoznat upit!");
		}
		
		System.out.print(">");
	    }
	    
	    scanner.close();

    }
    
    
    /**Metoda prima listu studenata i kreira string s formatiranom tablicom.
     * 
     * @param lista Lista StudentRecorda.
     * @return String koji predstavlja trazenu relaciju.
     */
    private static String printQuery(List<StudentRecord> lista) {
		
	//pronadji najdulje ime i prezime
	int[] max = findMaxNames(lista);
	
	StringBuilder builder = new StringBuilder();
	
	//appendaj gornji rub tablice
	builder.append(rubTablice(max));
	
	//dodaj prijelaz u novi red
	builder.append("\n");
	
	//prodji kroz cijelu listu i appendaj sve sto treba
	for (int i = 0, length = lista.size(); i < length; i++) {
	    
	    //pomocne varijable za izbjegavanje visestrukih poziva listi
	    StudentRecord pom = lista.get(i);
	    String lastName = pom.getLastName();
	    String firstName = pom.getFirstName();
	    
	    //dodaj jmbag i prezime
	    builder.append("| ").append(pom.getJmbag()).append(" | ").append(lastName);
	    
	    //ispuni s prazninama ako je potrebno, za ocuvanje formata, dodaj i ime
	    builder.append(empty(lastName, max[0])).append(" | ").append(firstName);
	    
	    //ispuni s prazninama ako je potrebno, za ocuvanje formata
	    builder.append(empty(firstName, max[1])).append(" | ");
	    
	    //dodaj zavrsnu ocjenu
	    builder.append(pom.getFinalGrade()).append(" |");
	    
	    //dodaj prijelaz u novi red
	    builder.append("\n");
	}
	
	//appendaj donji rub tablice
	builder.append(rubTablice(max));
	
	return builder.toString();
    }


    /**Metoda vraca string koji se sastoji od broja praznina potrebnih da se ocuva 
     * format tablice.
     * 
     * @param name Ime ili prezime studenta.
     * @param max Maksimalne sirine prezimena i imena iz liste studenata.
     * @param prezimeJe True ili false, ako je prezime ili ime.
     * @return Potreban broj praznina.
     */
    private static String empty(String name, int max) {
	String emptyString = "";
	
	for (int i = 0, length = max - name.length(); i < length; i++) {
	    emptyString += " ";
	}
	
	return emptyString;
    }


    /**Metoda u string pohranjuje varijabilni formatirani ispis koji ovisi o sirini prezimena
     * i imena.
     * 
     * @param max Sirina najsireg prezimena i imena.
     * @return String koji predstavlja gornji i donji rub tablice.
     */
    private static String rubTablice(int[] max) {
	
	StringBuilder builder = new StringBuilder();
	
	//appendaj sirinu jmbaga - konstantna je
	builder.append("+============+=");
	
	//appendaj onoliko koliko je siroko najsire prezime
	for (int i = 0; i < max[0]; i++) {
	    builder.append("=");
	}
	
	builder.append("=+=");
	
	//appendaj onoliko koliko je siroko najsire ime
	for (int i = 0; i < max[1]; i++) {
	    builder.append("=");
	}
	
	//appendaj sirinu zavrsne ocjene - konstantna je
	builder.append("=+===+");
	
	return builder.toString();
    }


    private static int[] findMaxNames(List<StudentRecord> lista) {
	
	//dvije vrijednosti, prva je za prezime, druga za ime
	int[] max = new int[2];
	max[0] = max[1] = 0;
	
	StudentRecord pomStudent;
	//prodji kroz listu i nadji najvece prezime i ime
	for (int i = 0, length = lista.size(); i < length; i++) {
	    
	    //spremi potrebne podatke u pomocne varijable da ne dohvacas vise puta
	    pomStudent = lista.get(i);
	    int sirinaPrezime = pomStudent.getLastName().length();
	    int sirinaIme = pomStudent.getFirstName().length();
	    
	    //ako prezime/ime sire od trenutnog max, postavi max na tu vrijednost
	    if (sirinaPrezime > max[0]) {
		max[0] = sirinaPrezime;
	    }
	    if (sirinaIme > max[1]) {
		max[1] = sirinaIme;
	    }
	}
	
	return max;
    }


    /**Metoda iz upita extracta samo vrijednost koja je pod navodnicima.
     * 
     * @param query Kompletan string s upitom.
     * @return Extractana vrijednost iz navodnika.
     */
    private static String extractFromLiterals(String query) {
	
	//kreiraj novu patternu po kojoj trazis - " xxx "
	Pattern p = Pattern.compile(".*\"(.*)\".*");
	
	//kreiraj novi matcher i pronadji prvo pojavljivanje trazenog patterna i vrati ga
	Matcher m = p.matcher(query);
	m.find();
	return m.group(1);
    }

}