package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * decrement.
 * 
 * @author Ivan Relic
 *
 */
public class InstrDecrement implements Instruction {

	private int registerIndex;
	
	/**
	 * Javni konstruktor prima listu arugmenata koji mora biti registar i pohranjuje
	 * njegov indeks u clansku varijablu.
	 * 
	 * @param arguments lista arugmenata
	 */
	public InstrDecrement(List<InstructionArgument> arguments) {
		
		//provjeri ima li tocno 1 argument i je li registar
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		
		registerIndex = ((Integer)arguments.get(0).getValue()).intValue();

	}
	
	/**
	 * Metoda izvrsava instrukciju, smanjuje vrijednost registra za 1.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//dohvati staru vrijednost registra
		Object value = computer.getRegisters().getRegisterValue(registerIndex);
		
		value = new Integer(((Integer)value).intValue() - 1);
		
		//postavi novu vrijednost u registar
		computer.getRegisters().setRegisterValue(registerIndex, value);
		
		return false;
	}

}
