package hr.fer.zemris.java.tecaj.hw4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**Klasa za operaciju s listom.
 * 
 * @author Ivan Relic
 *
 */
public class AboveAverage {

    
    /**Main metoda koja prima niz decimalnih brojeva sa stdin i vraca samo one koji su za barem
     * 20% veci od prosjeka dosad ucitanih.
     * 
     * @param args Argumenti komandne linije, ne koriste se.
     */
    public static void main(String[] args) {
	
	Scanner scanner = new Scanner(System.in);
	List<Double> listaBrojeva = new ArrayList<Double>();
	System.out.println("Valid inputs are numbers and string quit. Input your numbers: ");
	while(scanner.hasNextLine()) {
	    
	    //procitaj ulaz
	    String element = scanner.next();
	    
	    //ako je procitano "quit", zavrsi s ucitavanjem
	    if (element.equals("quit")) {
		break;
	    }
	    
	    //ako procitano nije decimalni broj, baci exception
	    if (!element.matches("[-\\+]?[0-9][0-9]*(.[0-9]*)?")) {
		scanner.close();
		throw new RuntimeException("Valid inputs are decimal numbers or \"quit\"!");
	    }
	    
	    //inace, parsiraj procitano u double i dodaj u listu brojeva
	    listaBrojeva.add(new Double(Double.parseDouble(element)));
	    
	} 
	
	scanner.close();
	
	//ako nema nista u kolekciji, upozori korisnika da nista nije unio
	if (listaBrojeva.size() == 0) {
	    System.err.println("List is empty, can't extract numbers!");
	}
	
	//inace, prodji kroz listu i izracunaj prosjek brojeva
	else {
	    double prosjek = 0;
	    int length = listaBrojeva.size();
		    
	    for (int i = 0; i < length; i++) {
		prosjek += listaBrojeva.get(i);
	    }
	    
	    prosjek = prosjek / length;
	    Double prosjekPlus20 = new Double(prosjek * 1.2);
	    
	    //ponovno prodji kroz listu i izbaci one koji su veci 20 % od prosjeka
	    Iterator<Double> iterator = listaBrojeva.iterator();
	    while(iterator.hasNext()) {
		if (iterator.next() < prosjekPlus20) {
		    iterator.remove();
		}
	    }
	    
	    //ispisi listu sortirano uzlazno
	    Collections.sort(listaBrojeva);
	    for(double d : listaBrojeva) {
		System.out.print(d + " ");
	    }
	}
    }
}
