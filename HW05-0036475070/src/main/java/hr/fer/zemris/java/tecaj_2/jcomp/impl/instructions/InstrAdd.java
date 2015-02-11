package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * add.
 * 
 * @author Ivan Relic
 *
 */
public class InstrAdd implements Instruction {

	private int registerIndexResult;
	private int registerIndexOperand1;
	private int registerIndexOperand2;
	
	/**
	 * Javni konstruktor prima listu arugmenata koji moraju biti registri i pohranjuje
	 * njihove indekse u clanske varijable.
	 * 
	 * @param arguments lista argumenata
	 */
	public InstrAdd(List<InstructionArgument> arguments) {
		
		//provjeri ima li tocno 3 argumenta i jesu li svi registri
		if(arguments.size() != 3) {
			throw new IllegalArgumentException("Expected 3 arguments!");
		}
		if(!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if(!arguments.get(1).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		if(!arguments.get(2).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 2!");
		}
		
		//dohvati indekse registara
		this.registerIndexResult = ((Integer)arguments.get(0).getValue()).intValue();
		this.registerIndexOperand1 = ((Integer)arguments.get(1).getValue()).intValue();
		this.registerIndexOperand2 = ((Integer)arguments.get(2).getValue()).intValue();
	}
	
	/**
	 * Metoda izvrsava instrukciju, zbraja vrijednosti iz 2 registra i postavlja ju u 3.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//dohvati vrijednosti operanada iz registara
		Object operand1 = computer.getRegisters().getRegisterValue(registerIndexOperand1);
		Object operand2 = computer.getRegisters().getRegisterValue(registerIndexOperand2);
		
		//postavi vrijednost u rezultatski registar
		computer.getRegisters().setRegisterValue(
				registerIndexResult, 
				Integer.valueOf(
						((Integer)operand1).intValue() + ((Integer)operand2).intValue()
				)
		);
		return false;
	}

}
