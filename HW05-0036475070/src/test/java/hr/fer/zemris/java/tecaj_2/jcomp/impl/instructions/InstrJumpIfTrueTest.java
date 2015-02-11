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

public class InstrJumpIfTrueTest {

	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u instrukciji jumpIfTrue.
	 */
	@Test
	public void InstrJumpIfTrueRegistersTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu jumpIfTrue
		InstructionArgument arg1 = new InstructionArgumentImpl(new Integer(128));
		
		//definiraj sto da mock napravi kad se zatrazi naredba getRegisters
		Mockito.when(c.getRegisters()).thenReturn(r);
		
		//definiraj sto da mock napravi kad se zatrazi naredba getFlag
		Mockito.when(r.getFlag()).thenReturn(true);
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrJumpIfTrue(Arrays.asList(arg1));
		
		//izvedi instrukciju
		instruction.execute(c);	
		
		//provjeri je li se zatrazilo skup registara dvaput (pristup PC i flag)
		Mockito.verify(c, Mockito.times(2)).getRegisters();
		
		//provjeri je li se pristupilo registru zastavice tocno jednom
		Mockito.verify(r).getFlag();
		
		//provjeri je li se registru PC pristupilo tocno jednom za pisanje broja 128
		Mockito.verify(r).setProgramCounter(128);
	}
	
	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima u instrukciji jumpIfTrue.
	 */
	@Test
	public void InstrJumpIfFalseRegistersTest() {
		
		//kreiraj mock objekte racunala i registara
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		
		//stvori nove argumente za naredbu increment
		InstructionArgument arg1 = new InstructionArgumentImpl(new Integer(15));
		
		//definiraj sto da mock napravi kad se zatrazi naredba getRegisters
		Mockito.when(c.getRegisters()).thenReturn(r);
		
		//definiraj sto da mock napravi kad se zatrazi naredba getFlag
		Mockito.when(r.getFlag()).thenReturn(false);
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrJumpIfTrue(Arrays.asList(arg1));
		
		//izvedi instrukciju
		instruction.execute(c);	
		
		//provjeri je li se pristupilo registrima jednom (flag)
		Mockito.verify(c).getRegisters();
		
		//provjeri je li se pristupilo registru zastavice tocno jednom
		Mockito.verify(r).getFlag();
		
		//provjeri da nije bilo pristupanja registru program counter s vrijednosti 15
		Mockito.verify(r, Mockito.times(0)).setProgramCounter(15);
	}
}
