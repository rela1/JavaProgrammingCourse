package hr.fer.zemris.java.tecaj_2.jcomp.impl;

import hr.fer.zemris.java.tecaj_2.jcomp.Registers;

/**
 * Klasa implementira sucelje Registers sa svojim pripadnim metodama.
 * 
 * @author Ivan Relic
 *
 */
public class RegistersImpl implements Registers {

    private Object[] register;
    private int programCounter = 0;
    private boolean flag;
    
    /**
     * Javni konstruktor koji kreira zeljeni broj registara.
     * 
     * @param registerCount zeljeni broj registara
     */
    public RegistersImpl(int registerCount) {
    	this.register = new Object[registerCount];
    }
    
    /**
     * Metoda vraca vrijednost trazenog registra.
     * 
     * @return Vrijednost registra.
     */
    public Object getRegisterValue(int index) {
    	
    	//baci exception za nedozvoljene indekse
    	if (index < 0 || index >= this.register.length) {
    		throw new IllegalArgumentException("Bad register index!");
    	}
    	return this.register[index];
    }

    /**
     * Metoda postavlja zeljeni registar na zeljenu vrijednost.
     * 
     * @param index zeljeni registar
     * @param value zeljena vrijednost
     */
    public void setRegisterValue(int index, Object value) {
    	
    	//baci exception za nedozvoljene indekse
    	if (index < 0 || index >= this.register.length) {
    		throw new IllegalArgumentException("Bad register index!");
    	}
    	this.register[index] = value;
    }

    /**
     * Metoda vraca trenutno stanje program counter registra.
     * 
     * @return vrijednost program countera
     */
    public int getProgramCounter() {
    	return this.programCounter;
    }

    /**
     * Metoda postavlja program counter na zeljenu vrijednost.
     * 
     * @param value nova vrijednost program countera
     */
    public void setProgramCounter(int value) {
    	
    	//value ne bi smio biti manji od 0, gornju granicu ne mozemo znati jer ne znamo
    	//s koliko memorije cemo inicijalizirati racunalo
    	if (value < 0) {
    		throw new IllegalArgumentException("PC value should be greater than 0!");
    	}
    	this.programCounter = value;
    }

    /**
     * Metoda uvecava vrijednost program countera za 1.
     */
    public void incrementProgramCounter() {
    	this.programCounter++;
    }

    /**
     * Metoda dohvaca vrijednost zastavice.
     * 
     * @return vrijednost zastavice
     */
    public boolean getFlag() {
    	return this.flag;
    }

    /**
     * Metoda postavlja vrijednost zastavice.
     * 
     * @param value nova vrijednost zastavice
     */
    public void setFlag(boolean value) {
    	this.flag = value;
    }

}
