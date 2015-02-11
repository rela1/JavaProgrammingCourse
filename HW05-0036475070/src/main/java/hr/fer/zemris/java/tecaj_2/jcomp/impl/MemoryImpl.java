package hr.fer.zemris.java.tecaj_2.jcomp.impl;

import hr.fer.zemris.java.tecaj_2.jcomp.Memory;

/**
 * Klasa implementira sucelje Memory sa svojim pripadnim metodama.
 * 
 * @author Ivan Relic
 *
 */
public class MemoryImpl implements Memory {

    private Object[] memoryLocation;
    
    /**
     * Javni konstruktor koji kreira memoriju zadane velicine.
     * 
     * @param size Zeljena velicina memorije.
     */
    public MemoryImpl(int size) {
    	this.memoryLocation = new Object[size];
    }
    
    /**
     * Metoda za postavljanje vrijednosti na zadanu memorijsku lokaciju.
     * 
     * @param location Memorijska lokacija na koju zelimo postaviti vrijednost.
     * @param value Vrijednost koju postavljamo na memorijsku lokaciju.
     */
    public void setLocation(int location, Object value) {
    	
    	//ako je nedozvoljeni indeks memorije, baci exception
    	if (location < 0 || location >= this.memoryLocation.length) {
    		throw new IllegalArgumentException("Bad index for memory location!");
    	}
    	this.memoryLocation[location] = value;
    }

    /**
     * Metoda za dohvat vrijednosti sa zadane lokacije.
     * 
     * @param location Memorijska adresa.
     * @return Objekt sa zadane adrese.
     */
    public Object getLocation(int location) {
    	
    	//ako je nedozvoljeni indeks memorije, baci exception
    	if (location < 0 || location >= this.memoryLocation.length) {
    		throw new IllegalArgumentException("Bad index for memory location!");
    	}
    	return this.memoryLocation[location];
    }

}
