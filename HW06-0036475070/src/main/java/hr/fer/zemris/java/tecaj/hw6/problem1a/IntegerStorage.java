package hr.fer.zemris.java.tecaj.hw6.problem1a;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa "subjekta" koji provodi promjene nad svojim sadrzajem i obavjestava "observere" da je
 * doslo do promjene.
 * 
 * @author Ivan Relic
 *
 */
public class IntegerStorage {
	
	private int value;
	private List<IntegerStorageObserver> observers;
	
	/**
	 * Javni konstruktor koji stvara instancu objekta IntegerStorage s predanom int vrijednosti.
	 * 
	 * @param initialValue pocetna vrijednost koju zelimo u objektu
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<IntegerStorageObserver>();
	}
	
	/**
	 * Metoda dodaje promatraca u listu promatraca subjekta ako takav tamo vec ne postoji.
	 * 
	 * @param observer promatrac kojeg zelimo dodati, ne smije biti null
	 */
	public void addObserver(IntegerStorageObserver observer) {
		
		//ako je predana null vrijednost, baci iznimku
		if (observer == null) {
			throw new IllegalArgumentException("Observer should not be null!");
		}
		
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}
	
	/**
	 * Metoda uklanja promatraca iz liste promatraca subjekta ako on postoji.
	 * 
	 * @param observer promatrac kojeg zelimo ukloniti, ne smije biti null
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		
		//ako je predana null vrijednost, baci iznimku
		if (observer == null) {
			throw new IllegalArgumentException("Observer should not be null!");
		}
		
		observers.remove(observer);
	}
	
	/**
	 * Metoda uklanja sve promatrace subjekta.
	 */
	public void clearObservers() {
		if (observers != null) {
			observers.clear();
		}
	}
	
	/**
	 * Metoda vraca trenutnu vrijednost pohranjenu u subjektu.
	 * 
	 * @return int vrijednost subjekta
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Metoda postavlja novu vrijednost subjekta samo ako je ona razlicita od trenutne i 
	 * obavijestava sve trenutne promatrace, ako takvi postoje.
	 * 
	 * @param value vrijednost koju zelimo postaviti subjektu
	 */
	public void setValue(int value) {
		if (value != this.value) {
			this.value = value;		
			
			//ako lista promatraca nije null, obavijesti ih
			if (observers != null) {
				
				//iteracija kopijom liste zbog moguceg brisanja observera za double value i 
				//unistavanja slijednosti liste
				for (IntegerStorageObserver observer : 
					new ArrayList<IntegerStorageObserver>(observers)) {				
					observer.valueChanged(this);
				}
			}
		}
	}
}
