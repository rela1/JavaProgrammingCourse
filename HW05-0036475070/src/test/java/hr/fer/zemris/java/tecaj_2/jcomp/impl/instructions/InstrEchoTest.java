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

public class InstrEchoTest {
	
	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u instrukciji echo.
	 */
	@Test
	public void InstrEchoRegistersTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu echo
		InstructionArgument arg1 = new InstructionArgumentImpl("r0");
		
		//definiraj sto da mock napravi kad se zatraze naredbe getRegisters i getRegisterValue
		//s odredjenog indeksa
		Mockito.when(c.getRegisters()).thenReturn(r);
		Mockito.when(r.getRegisterValue(0)).thenReturn("TEST STRING");
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrEcho(Arrays.asList(arg1));
		
		//izvedi instrukciju
		instruction.execute(c);	
		
		//provjeri je li se zatrazilo skup registara tocno jednom
		Mockito.verify(c).getRegisters();
		
		//provjeri je li se registru r0 pristupilo tocno jednom za citanje
		Mockito.verify(r).getRegisterValue(0);
	}

}
