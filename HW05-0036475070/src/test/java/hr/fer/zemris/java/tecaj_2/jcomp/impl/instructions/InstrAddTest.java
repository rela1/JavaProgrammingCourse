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

public class InstrAddTest {

	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u metodi add.
	 */
	@Test
	public void InstrAdd20Plus20Test() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu add
		InstructionArgument arg1 = new InstructionArgumentImpl("r0");
		InstructionArgument arg2 = new InstructionArgumentImpl("r1");
		InstructionArgument arg3 = new InstructionArgumentImpl("r2");
		
		//definiraj sto da mock napravi kad se zatraze naredbe getRegisters i getRegisterValue
		//s odredjenog indeksa
		Mockito.when(c.getRegisters()).thenReturn(r);
		Mockito.when(r.getRegisterValue(1)).thenReturn(new Integer(20));
		Mockito.when(r.getRegisterValue(2)).thenReturn(new Integer(20));
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrAdd(Arrays.asList(arg1, arg2, arg3));
		
		//izvedi instrukciju
		instruction.execute(c);
		
		//provjeri je li se zatrazilo skup registara triput - r0, r1, r2
		Mockito.verify(c, Mockito.times(3)).getRegisters();
		
		//provjeri je li se registru r1 pristupilo tocno jednom
		Mockito.verify(r).getRegisterValue(1);
		
		//provjeri je li se registru r2 pristupilo tocno jendom
		Mockito.verify(r).getRegisterValue(2);
		
		//provjeri je li se registru r0 pristupilo tocno jednom s upisom vrijednosti 40 (20+20)
		Mockito.verify(r).setRegisterValue(0, new Integer(40));
	}	
}
