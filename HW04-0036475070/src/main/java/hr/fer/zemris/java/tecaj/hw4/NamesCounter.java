package hr.fer.zemris.java.tecaj.hw4;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**Klasa za operaciju s mapom.
 * 
 * @author Ivan Relic
 *
 */
public class NamesCounter {

    /**Main metoda koja ucitava imena dok se ne ucita quit i ispisuje koliko je puta svako ime
     * ucitano.
     * 
     * @param args Argumenti komandne linije, ne koriste se.
     */
    public static void main(String[] args) {
	
	Scanner scanner = new Scanner(System.in);
	Map<String, Integer> listaImena = new HashMap<String, Integer>();
	System.out.println("Input your names. Command for ending input is quit.");
	while(scanner.hasNextLine()) {
	    
	    //procitaj ulaz
	    String ime = scanner.next();
	    
	    //ako je procitano "quit", zavrsi s ucitavanjem
	    if (ime.equals("quit")) {
		break;
	    }
	    
	    //ako nije procitano ime, baci exception
	    if (!ime.matches("[a-zA-z]+")) {
		scanner.close();
		throw new RuntimeException("Only names with letters should be provided!");
	    }
	    
	    //ako mapa ne sadrzi ime, ubaci ga u nju
	    if (!listaImena.containsKey(ime)) {
		listaImena.put(ime, 1);
	    }
	    
	    //inace, samo uvecaj brojac trenutnog imena
	    else {
		Integer brojac = listaImena.get(ime) + 1;
		listaImena.put(ime, brojac);
	    }
	}
	
	scanner.close();
	
	//ako je mapa prazna, ispisi upozorenje korisniku
	if (listaImena.size() == 0) {
	    System.err.println("Map is empty, you didn't provide any name!");
	}
	
	//inace, samo ispisi sve parove vrijednosti mape
	else {
	    for (String ime : listaImena.keySet()) {
		System.out.println(ime + " " + listaImena.get(ime));
	    }
	}
    }

}
