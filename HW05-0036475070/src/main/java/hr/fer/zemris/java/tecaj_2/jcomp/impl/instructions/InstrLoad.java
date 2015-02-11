package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * load.
 * 
 * @author Ivan Relic
 *
 */
public class InstrLoad implements Instruction {
	
	private int registerIndex;
	private int memoryLocation;
	
	/**
	 * Javni konstruktor pohranjuje indeks registra i broj memorijske lokacije iz liste
	 * primljenih argumenata.
	 */
	public InstrLoad(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if (!arguments.get(1).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		
		//dohvati indeks registra i indeks memorijske lokacije
		this.registerIndex = ((Integer)arguments.get(0).getValue()).intValue();
		this.memoryLocation = ((Integer)arguments.get(1).getValue()).intValue();		
	}
	
	/**
	 * Metoda izvrsava instrukciju, pohranjuje u zeljeni registar vrijednost sa zeljene
	 * memorijske lokacije.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//u registar koji je na registerIndex indeksu pohrani vrijednost s memorijske lokacije
		//koja je na memoryLocation lokaciji
		computer.getRegisters().setRegisterValue(
				registerIndex, 
				computer.getMemory().getLocation(memoryLocation)
		);
		return false;
	}
}
