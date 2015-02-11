package hr.fer.zemris.java.tecaj_2.jcomp.impl;

import hr.fer.zemris.java.tecaj_2.jcomp.Computer;
import hr.fer.zemris.java.tecaj_2.jcomp.Memory;
import hr.fer.zemris.java.tecaj_2.jcomp.Registers;

/**
 * Razred implementira sucelje Computer, predstavlja konkretnu realizaciju, tj. instanciranje
 * racunala sa zeljenom velicinom memorije i zeljenim brojem registara.
 * 
 * @author Ivan Relic
 *
 */
public class ComputerImpl implements Computer {
	
	private RegistersImpl registers;
	private MemoryImpl memory;
	
	/**
	 * Javni konstruktor koji kreira racunalo sa zadanom velicinom memorije i brojem registara.
	 * 
	 * @param memorySize zeljena velicina memorije
	 * @param registerCount zeljeni broj registara
	 */
	public ComputerImpl(int memorySize, int registerCount) {
		this.memory = new MemoryImpl(memorySize);
		this.registers = new RegistersImpl(registerCount);
	}

	/**
	 * Metoda vraca skup registara racunala.
	 * 
	 * @return registri racunala
	 */
	public Registers getRegisters() {
		return this.registers;
	}

	/**
	 * Metoda vraca memoriju racunala.
	 * 
	 * @return memorija racunala
	 */
	public Memory getMemory() {
		return this.memory;
	}

}
