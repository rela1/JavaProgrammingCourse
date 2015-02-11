package hr.fer.zemris.java.tecaj_2.jcomp.impl;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.ExecutionUnit;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;

/**
 * Razred implementira sucelje ExecutionUnit i predstavlja konkretnu implementaciju 
 * jedinice koja generira takt, ucitava program i izvodi ga.
 * 
 * @author Ivan Relic
 *
 */
public class ExecutionUnitImpl implements ExecutionUnit {

	/**
	 * Metoda čiji je zadatak izvoditi program zapisan u memoriji računala.
	 * 
	 * @param computer računalo na kojem se izvodi program
	 * @return true - ako je program izveden, false - ako se desila iznimka
	 */
	public boolean go(Computer computer) {
		try{ 
			Instruction instrukcija;
			do {
				int pc = computer.getRegisters().getProgramCounter();
				
				//dohvati instrukciju s memorijske lokacije na indeksu program countera
				instrukcija = (Instruction) computer.getMemory().getLocation(pc); 
				
				//uvecaj PC
				computer.getRegisters().incrementProgramCounter();
				
				//uvjet za do-while je sve dok instrukcije vracaju false za svoje izvodenje
			} while (!instrukcija.execute(computer));
			
			//vrati true ako je sve proslo bez iznimki
			return true;
			
		} catch (Exception e) {
			
			//ako uhvatis bilo kakvu iznimku, vrati false
			return false;
		}
	}

}
