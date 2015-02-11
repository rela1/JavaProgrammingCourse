package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Kolekcija koja kao kljuc sadrzava string, a svakom kljucu se moze pridruziti
 * vise objekata kroz pratecu strukturu u obliku stoga.
 * 
 * @author Ivan Relic
 * 
 */
public class ObjectMultistack {

	/**
	 * Static klasa predstavlja strukturu podataka stog u obliku jednostruko
	 * povezane liste.
	 * 
	 * @author Ivan Relic
	 * 
	 */
	static class MultiStackEntry {
		ValueWrapper value;
		MultiStackEntry next;
	}

	private Map<String, MultiStackEntry> mapa = null;

	/**
	 * Javni konstruktor zauzima memoriju za mapu.
	 */
	public ObjectMultistack() {
		mapa = new HashMap<String, MultiStackEntry>();
	}

	/**
	 * Metoda provjerava je li stog s navedenim kljucem prazan.
	 */
	public boolean isEmpty(String name) {

		MultiStackEntry stack = mapa.get(name);

		// ako navedeni kljuc uopce ne postoji unutra, vrati true
		if (stack == null) {
			return true;
		}

		// ako je prazan, vrati true
		if (stack.next == null) {
			return true;
		}

		return false;
	}

	/**
	 * Metoda dodaje novi cvor u stog na predani kljuc.
	 * 
	 * @param name
	 *            kljuc stoga
	 * @param valueWrapper
	 *            vrijednost koju dodajemo na stog
	 */
	public void push(String name, ValueWrapper valueWrapper) {

		// ako navedeno ime stoga ne postoji u mapi dodaj ga i kreiraj stog za
		// njega
		if (mapa.get(name) == null) {
			MultiStackEntry stack = new MultiStackEntry();
			stack.next = null;
			mapa.put(name, stack);
		}

		// inace, dohvati value, tj. stack s te vrijednosti i ubaci value unutra
		MultiStackEntry stack = mapa.get(name);
		MultiStackEntry noviCvor = new MultiStackEntry();
		noviCvor.value = valueWrapper;
		noviCvor.next = stack.next;
		stack.next = noviCvor;
	}

	/**
	 * Metoda skida element s vrha stoga pod navedenim kljucem.
	 * 
	 * @param name
	 *            kljuc stoga
	 * @return vrijednost s vrha stoga
	 */
	public ValueWrapper pop(String name) {

		// ako navedeni kljuc uopce ne postoji unutra, baci exception
		if (mapa.get(name) == null) {
			throw new RuntimeException("Stack does not exist!");
		}

		MultiStackEntry stack = mapa.get(name);

		// ako je prazan, baci exception
		if (stack.next == null) {
			throw new IllegalArgumentException("Stack is empty!");
		}

		// inace, uzmi vrijednost s vrha stoga, ukloni je sa stoga i vrati je
		ValueWrapper value = stack.next.value;
		stack.next = stack.next.next;
		return value;
	}

	/**
	 * Metoda samo dohvaca element s vrha stoga ne uklanjajuci ga.
	 * 
	 * @param name
	 *            kljuc stuga
	 * @return vrijednost s vrha stoga
	 */
	public ValueWrapper peek(String name) {

		MultiStackEntry stack = mapa.get(name);

		// ako navedeni kljuc uopce ne postoji unutra, baci exception
		if (stack == null) {
			throw new RuntimeException("Stack does not exist!");
		}

		// ako je prazan, baci exception
		if (stack.next == null) {
			throw new IllegalArgumentException("Stack is empty!");
		}

		// inace, uzmi vrijednost s vrha stoga i vrati je
		return stack.next.value;
	}

	/**
	 * Uklanja vrijednost pod navedenim imenom.
	 * 
	 * @param name
	 *            ime vrijednosti
	 */
	public void remove(String name) {
		if (mapa.get(name) == null) {
			throw new IllegalArgumentException(
					"Value with that name doesn't exist!");
		}
		mapa.remove(name);
	}

}
