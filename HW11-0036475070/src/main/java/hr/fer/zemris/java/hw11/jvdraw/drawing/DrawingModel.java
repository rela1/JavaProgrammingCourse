package hr.fer.zemris.java.hw11.jvdraw.drawing;

import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;

/**
 * Sučelje modela koji sadrži podatke o svim grafičkim komponentama koje se
 * iscrtavaju.
 * 
 * @author Ivan Relić
 * 
 */
public interface DrawingModel {

	/**
	 * Vraća veličinu liste modela za crtanje, tj. koliko ima geometrijskih
	 * objekata spremih za iscrtavanje.
	 * 
	 * @return broj objekata
	 */
	public int getSize();

	/**
	 * Dohvaća geometrijski objekt s predanog indeksa.
	 * 
	 * @param index
	 *            indeks željenog objekta
	 * @return objekt s traženog indeksa
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Dodaje geometrijski objekt u crtaći model.
	 * 
	 * @param object
	 */
	public void add(GeometricalObject object);

	/**
	 * Radi update predanog objekta u crtaćem modelu.
	 * 
	 * @param object
	 *            objekt za update
	 */
	public void updateObject(GeometricalObject object);

	/**
	 * Uklanja geometrijski objekt s predanog indeksa.
	 * 
	 * @param index
	 *            indeks objekta kojeg uklanjamo
	 */
	public void removeObject(int index);

	/**
	 * Uklanja sve geometrijske objekte iz crtaćeg modela.
	 */
	public void clear();

	/**
	 * Dodaje promatrača crtaćeg modela.
	 * 
	 * @param l
	 *            promatrač
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Uklanja promatrača crtaćeg modela.
	 * 
	 * @param l
	 *            promatrač za uklanjanje
	 */
	public void removeDrawingModelListener(DrawingModelListener l);

}
