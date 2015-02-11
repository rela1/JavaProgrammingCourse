package hr.fer.zemris.java.tecaj_2.jcomp.impl;

import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Klasa s pomocnom implementacijom sucelja InstructionArgument za potrebe kreiranja instrukcija
 * kod testiranja.
 * 
 * @author Ivan Relic
 *
 */
public class InstructionArgumentImpl implements InstructionArgument {

	private Object argument;
	
	/**
	 * Javni konstruktor prima argument i pohranjuje ga u svoju clansku varijablu.
	 * 
	 * @param argument argument koji je primljen
	 */
	public InstructionArgumentImpl(Object argument) {
		this.argument = argument;
	}
	
	/**
	 * Metoda provjerava je li primljeni argument oblika r[broj].
	 * 
	 * @return true ako je primljeni argument registar, false ako nije
	 */
	public boolean isRegister() {
		try {
			
			//probaj castati primljeni argument u string i provjeri odgovara li obliku registra
			String pom = (String)this.argument;
			if (pom.matches("r[0-9]+")) {
				return true;
			}
			
		} catch (Exception e) {
			
			//ako ulovis exception objekt se nije uspio castati u string, vrati false
			return false;
		}
		
		return false;
	}

	/**
	 * Metoda provjerava je li primljeni argument string.
	 * 
	 * @return true ako je primljeni argument string, false ako nije
	 */
	public boolean isString() {
		try {
			
			//probaj castati primljeni argument u string, ako uspijes vratit ces true, ako ne, ulovit ce
			//se exception i vratit ces false
			@SuppressWarnings("unused")
			String pom = (String)this.argument;
			return true;
			
		} catch (Exception e) {
			
			//ako ulovis exception objekt se nije uspio castati u string, vrati false
			return false;
		}
	}

	/**
	 * Metoda provjerava je li primljeni argument integer broj.
	 * 
	 * @return true ako je primljeni argument integer broj, false ako nije
	 */
	public boolean isNumber() {
		try {
			
			//probaj castati primljeni argument u integer, ako uspijes vratit ces true, ako ne, 
			//ulovit ce se exception i vratit ces false
			@SuppressWarnings("unused")
			Integer pom = (Integer)this.argument;
			return true;
			
		} catch (Exception e) {
			
			//ako ulovis exception objekt se nije uspio castati u integer, vrati false
			return false;
		}
	}

	/**
	 * Metoda vraca vrijednost argumenta kojeg sadrzava.
	 * 
	 * @return vrijednost argumenta
	 */
	public Object getValue() {
		
		//ako je objekt broj, vrati ga castanog u integer
		if (this.isNumber()) {
			return (Integer) this.argument;
		}
		
		//ako je primljen registar, makni mu slovo r i parsaj ga u integer
		else if (this.isRegister()) {
			String pom = (String) this.argument;
			pom = pom.replaceAll("r", "");
			return Integer.parseInt(pom);
		}
		
		//inace je string, vrati ga castanog u string
		else {
			return (String) this.argument;
		}
	}

}
