package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * jump.
 * 
 * @author Ivan Relic
 *
 */
public class InstrJump implements Instruction {

	private int jumpIndex;

	/**
	 * Javni konstruktor postavlja clansku varijablu na onaj indeks koji dobije u listi
	 * argumenata.
	 * 
	 * @param arguments lista argumenata
	 */
	public InstrJump(List<InstructionArgument> arguments) {
		
		//provjeri ima li tocno 1 argument i je li broj
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		
		jumpIndex = ((Integer)arguments.get(0).getValue()).intValue();
	}
	
	/**
	 * Metoda izvrsava instrukciju, u PC postavlja predanu vrijednost.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		computer.getRegisters().setProgramCounter(jumpIndex);
		return false;
	}

}
