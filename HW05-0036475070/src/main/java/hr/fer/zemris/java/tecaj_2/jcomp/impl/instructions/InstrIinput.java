package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;

/**
 * Razred implementira sucelje Instruction i predstavlja konkretnu realizaciju instrukcije
 * iinput.
 * 
 * @author Ivan Relic
 *
 */
public class InstrIinput implements Instruction {
	
	private int memoryLocation;

	/**
	 * Javni konstruktor prima listu arugmenata koja mora sadržavati samo 1 broj - mem lokaciju
	 * 
	 * @param arguments lista arugmenata
	 */
	public InstrIinput(List<InstructionArgument> arguments) {
		
		//provjerava ima li tocno jedan argument koji je broj
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 arguments!");
		}
		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		
		//dohvati zadanu memorijsku lokaciju
		this.memoryLocation = ((Integer)arguments.get(0).getValue()).intValue();	
	}
	
	/**
	 * Metoda izvrsava instrukciju, ucitava broj s tipkovnice i pohranjuje ga na zadanu
	 * memorijsku lokaciju.
	 * 
	 * @return True ako treba zaustaviti procesor, false inace.
	 */
	public boolean execute(Computer computer) {
		
		//procitaj korisnikov unos
		Integer value;
		try {
			Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
			
			//program ce ovdje izazvati exception ako je procitano bilo sto osim integera
			value = Integer.parseInt(scanner.nextLine());
			
			//ako si uspio parsirati u integer, postavi taj procitani broj u memoriju
			computer.getMemory().setLocation(memoryLocation, value);
			
			//postavi flag u true jer si uspjesno procitao i zapisao
			computer.getRegisters().setFlag(true);
			scanner.close();
			
		} catch (Exception e) { 
			
			//ako uhvatis bilo kakav exception, znaci da je doslo do pogreske u parsiranju
			//u cijeli broj pa postavi flag na false
			computer.getRegisters().setFlag(false);
		}
		
		return false;
	}

}
