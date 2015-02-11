package hr.fer.zemris.java.tecaj.hw6.problem1a;

import java.nio.file.Paths;

/**
 * Klasa je container za main metodu.
 * 
 * @author Ivan Relic
 *
 */
public class ObserverExample {
	
	/**
	 * Metoda pokazuje rad subjekt - promatrac uzorka.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		istorage.addObserver(observer);
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		
		istorage.removeObserver(observer);
		
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue());
		istorage.addObserver(new LogValue(Paths.get("./log.txt")));
		
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
}
}