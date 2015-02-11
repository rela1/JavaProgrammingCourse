package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**Osnovna klasa za elemente koji izgraduju stabla parsiranog dokumenta.
 * 
 * @author Ivan Relic
 *
 */
public class Node {

	//clanska varijabla za indikaciju treba li kreirati kolekciju ili ne
	private boolean nijePokrenuta = true;
	
	//clanska varijabla za kolekciju djece
	private ArrayBackedIndexedCollection tree;
	
	
	/**Dodaje zadano dijete u internu kolekciju klase.
	 * 
	 * @param child Dijete koje se dodaje u listu.
	 */
	public void addChildNode(Node child) {
		
		//ako funkcija jos nije pokretana, prvo kreiraj kolekciju
		if(nijePokrenuta) {
			tree = new ArrayBackedIndexedCollection();
			nijePokrenuta = false;
		}
		
		//dodaj dijete u kolekciju
		tree.add(child);
	}
	
	
	/**Vraca broj direktne djece stabla.
	 * 
	 * @return Broj djece stabla.
	 */
	public int numberOfChildren() {
		if (nijePokrenuta) {
			return 0;
		}
		else {
			return tree.size();
		}
	}
	
	
	/**Vraca dijete sa odabranog indeksa.
	 * 
	 * @param index Indeks s kojeg zelimo dohvat, u intervalu [0, size-1]
	 * @return Vraca dijete sa zadanog indeksa.
	 */
	public Node getChild(int index) {
		
		if(!nijePokrenuta) {
		
			//vrati dijete sa zadanog indeksa, exception throw je vec ugradjen u pozivanoj metodi
			return (Node)tree.get(index);
		}
		
		else {
			throw new IllegalArgumentException("Kolekcija nije stvorena!");
		}
	}
}
