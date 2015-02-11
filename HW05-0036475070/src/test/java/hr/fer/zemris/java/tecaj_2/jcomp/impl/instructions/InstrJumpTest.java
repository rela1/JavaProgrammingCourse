package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;
import hr.fer.zemris.java.tecaj_2.jcomp.Registers;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.ComputerImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.InstructionArgumentImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.RegistersImpl;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

public class InstrJumpTest {
	
	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u instrukciji jump.
	 */
	@Test
	public void InstrJumpRegistersTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu jump
		InstructionArgument arg1 = new InstructionArgumentImpl(new Integer(55));
		
		//definiraj sto da mock napravi kad se zatrazi naredba getRegisters
		Mockito.when(c.getRegisters()).thenReturn(r);
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrJump(Arrays.asList(arg1));
		
		//izvedi instrukciju
		instruction.execute(c);	
		
		//provjeri je li se zatrazilo skup registara tocno jednom
		Mockito.verify(c).getRegisters();
		
		//provjeri je li se registru PC pristupilo tocno jednom za pisanje broja 55
		Mockito.verify(r).setProgramCounter(55);
	}

}
