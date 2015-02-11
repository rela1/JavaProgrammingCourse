package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import java.util.Arrays;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;
import hr.fer.zemris.java.tecaj_2.jcomp.Registers;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.ComputerImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.InstructionArgumentImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.RegistersImpl;

import org.junit.Test;
import org.mockito.Mockito;

public class InstrDecrementTest {
	
	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u instrukciji decrement.
	 */
	@Test
	public void InstrDecrementRegisterTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu decrement
		InstructionArgument arg1 = new InstructionArgumentImpl("r0");
		
		//definiraj sto da mock napravi kad se zatraze naredbe getRegisters i getRegisterValue
		//s odredjenog indeksa
		Mockito.when(c.getRegisters()).thenReturn(r);
		Mockito.when(r.getRegisterValue(0)).thenReturn(new Integer(49));
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrDecrement(Arrays.asList(arg1));
		
		//izvedi instrukciju
		instruction.execute(c);
		
		//provjeri je li se zatrazilo skup registara - citanje i upis umanjene vrijednosti
		Mockito.verify(c, Mockito.times(2)).getRegisters();
		
		//provjeri je li se registru r0 pristupilo tocno jednom za citanje
		Mockito.verify(r).getRegisterValue(0);
		
		//provjeri je li se registru r0 pristupilo tocno jednom s upisom vrijednosti 48
		Mockito.verify(r).setRegisterValue(0, new Integer(48));
	}

}
