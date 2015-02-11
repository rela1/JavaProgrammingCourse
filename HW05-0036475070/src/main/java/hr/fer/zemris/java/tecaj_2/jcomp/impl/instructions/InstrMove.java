package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * move.
 * 
 * @author Ivan Relic
 *
 */
public class InstrMove implements Instruction {

	private int registerIndex1;
	private int registerIndex2;
	
	/**
	 * Javni konstruktor prima listu arugmenata koji moraju biti registri i pohranjuje
	 * njihove indekse u clanske varijable.
	 * 
	 * @param arguments lista argumenata
	 */
	public InstrMove(List<InstructionArgument> arguments) {
		
		//provjeri ima li tocno 2 argumenta i jesu li svi registri
		if(arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if(!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if(!arguments.get(1).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		
		//pohrani indekse registara
		registerIndex1 = ((Integer)arguments.get(0).getValue()).intValue();
		registerIndex2 = ((Integer)arguments.get(1).getValue()).intValue();
	}
	
	/**
	 * Metoda izvrsava instrukciju, u registar stavlja vrijednost iz nekog drugog registra.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//dohvati vrijednost iz drugog registra
		Object value = computer.getRegisters().getRegisterValue(registerIndex2);
		
		//postavi tu vrijednost u prvi registar
		computer.getRegisters().setRegisterValue(registerIndex1, value);
		
		return false;
	}

}
