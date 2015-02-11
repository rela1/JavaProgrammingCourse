package hr.fer.zemris.java.tecaj_2.jcomp;

import hr.fer.zemris.java.tecaj_2.jcomp.impl.ComputerImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.ExecutionUnitImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.parser.InstructionCreatorImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.parser.ProgramParser;

/**
 * Razred je samo container za main metodu.
 * 
 * @author Ivan Relic
 *
 */
public class Simulator {

	/**
	 * Main metoda za testiranje rada mikroračunala.
	 * 
	 * @param args argumenti komandne linije, prima se path do filea s assembler kodom
	 * @throws Exception ako ne uspije citanje filea
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			throw new IllegalArgumentException("You should provide path to file!");
		}
			
		// Stvori računalo s 256 memorijskih lokacija i 16 registara
		Computer comp = new ComputerImpl(256, 16);
		
		// Stvori objekt koji zna stvarati primjerke instrukcija
		InstructionCreator creator = new InstructionCreatorImpl(
				"hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions"
		);
		
		// Napuni memoriju računala programom iz datoteke; instrukcije stvaraj
		// uporabom predanog objekta za stvaranje instrukcija
		ProgramParser.parse(
				args[0],
				comp,
				creator
		);
		
		// Stvori izvršnu jedinicu
		ExecutionUnit exec = new ExecutionUnitImpl();
		
		// Izvedi program
		exec.go(comp);
	}

}
