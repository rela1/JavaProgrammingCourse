package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * testEquals.
 * 
 * @author Ivan Relic
 *
 */
public class InstrTestEquals implements Instruction {

	private int registerIndex1;
	private int registerIndex2;
	
	/**
	 * Javni konstruktor prima listu arugmenata koji moraju biti registri i pohranjuje
	 * njihove indekse u clanske varijable.
	 * 
	 * @param arguments lista argumenata
	 */
	public InstrTestEquals(List<InstructionArgument> arguments) {
		
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
	 * Metoda izvrsava instrukciju, provjerava sadrze li 2 registra iste vrijednosti.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//dohvati vrijednosti iz registara
		Object value1 = computer.getRegisters().getRegisterValue(registerIndex1);
		Object value2 = computer.getRegisters().getRegisterValue(registerIndex2);
		
		//postavi zastavicu ovisno o rezultatu usporedbe vrijednosti
		computer.getRegisters().setFlag(value1.equals(value2));
		
		return false;
	}

}
