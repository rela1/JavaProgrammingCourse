package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * echo.
 *  
 * @author Ivan Relic
 *
 */
public class InstrEcho implements Instruction {
	
	private int registerIndex;
	
	/**
	 * Javni konstruktor koji pohranjuje indeks registra iz liste primljenih argumenata.
	 */
	public InstrEcho(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		
		//dohvati indeks registra
		registerIndex = ((Integer)arguments.get(0).getValue()).intValue();
	}
	
	/**
	 * Metoda izvrsava instrukciju, ispisuje na ekran sadrzaj zadanog registra.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		System.out.print(computer.getRegisters().getRegisterValue(registerIndex));
		return false;
	}
}
