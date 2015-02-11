package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * halt.
 *  
 * @author Ivan Relic
 *
 */
public class InstrHalt implements Instruction {
	
	/**
	 * Javni konstruktor koji provjerava ispravnost zadane naredbe halt 
	 * (mora biti bez argumenata).
	 * 
	 * @param arguments lista argumenata
	 */
	public InstrHalt(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Expected 0 arguments!");
		}
	}
	
	/**
	 * Metoda izvrsava instrukciju, zaustavlja rad procesora.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		return true;
	}
}
