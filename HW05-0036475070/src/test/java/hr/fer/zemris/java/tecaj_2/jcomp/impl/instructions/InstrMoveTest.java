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

public class InstrMoveTest {

	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u metodi move.
	 */
	@Test
	public void InstrMoveRegistersTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu move
		InstructionArgument arg1 = new InstructionArgumentImpl("r0");
		InstructionArgument arg2 = new InstructionArgumentImpl("r1");
		
		//definiraj sto da mock napravi kad se zatraze naredbe getRegisters i getRegisterValue
		//s odredjenog indeksa
		Mockito.when(c.getRegisters()).thenReturn(r);
		Mockito.when(r.getRegisterValue(1)).thenReturn("test move");
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrMove(Arrays.asList(arg1, arg2));
		
		//izvedi instrukciju
		instruction.execute(c);
		
		//provjeri je li se zatrazilo skup registara dvaput - r0, r1
		Mockito.verify(c, Mockito.times(2)).getRegisters();
		
		//provjeri je li se registru r1 pristupilo tocno jednom
		Mockito.verify(r).getRegisterValue(1);
		
		//provjeri je li se registru r0 pristupilo tocno jednom s upisom vrijednosti
		Mockito.verify(r).setRegisterValue(0, "test move");
	}	
}
