package hr.fer.zemris.java.tecaj_2.jcomp.impl.instructions;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Instruction;
import hr.fer.zemris.java.tecaj_2.jcomp.InstructionArgument;
import hr.fer.zemris.java.tecaj_2.jcomp.Memory;
import hr.fer.zemris.java.tecaj_2.jcomp.Registers;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.ComputerImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.InstructionArgumentImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.MemoryImpl;
import hr.fer.zemris.java.tecaj_2.jcomp.impl.RegistersImpl;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

public class InstrLoadTest {
	
	/**
	 * Metoda provjerava broj pozivanja i pristupanja registrima i memoriji u instrukciji load.
	 */
	@Test
	public void InstrLoadRegistersAndMemoryTest() {
		
		//kreiraj mock objekte racunala, registara i memorije
		Computer c = Mockito.mock(ComputerImpl.class);
		Registers r = Mockito.mock(RegistersImpl.class);
		Memory m = Mockito.mock(MemoryImpl.class);
		
		//stvori nove argumente za naredbu load
		InstructionArgument arg1 = new InstructionArgumentImpl("r0");
		InstructionArgument arg2 = new InstructionArgumentImpl(new Integer(20));
		
		//definiraj sto da mock napravi kad se zatrazi naredba getRegisters
		Mockito.when(c.getRegisters()).thenReturn(r);
		
		//definiraj sto da mock napravi kad se zatrazi naredba getMemory
		Mockito.when(c.getMemory()).thenReturn(m);
		
		//definiraj sto da mock napravi kad se zatrazi nesto s odredjene memorijske lokacije
		Mockito.when(m.getLocation(20)).thenReturn("TEST LOAD INSTR");
		
		//kreiraj novu instrukciju s definiranim argumentima
		Instruction instruction = new InstrLoad(Arrays.asList(arg1, arg2));
		
		//izvedi instrukciju
		instruction.execute(c);	
		
		//provjeri je li se zatrazilo skup registara tocno jednom
		Mockito.verify(c).getRegisters();
		
		//provjeri je li se zatrazilo memoriju tocno jednom
		Mockito.verify(c).getMemory();
		
		//provjeri je li se pristupilo memorijskoj lokaciji 20 tocno jednom
		Mockito.verify(m).getLocation(20);
		
		//provjeri je li se pristupilo registru r0 tocno jednom kod upisa vrijednosti
		Mockito.verify(r).setRegisterValue(0, "TEST LOAD INSTR");
	}

}
