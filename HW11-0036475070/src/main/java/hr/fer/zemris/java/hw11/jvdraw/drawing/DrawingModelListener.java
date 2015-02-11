package hr.fer.zemris.java.hw11.jvdraw.drawing;

public interface DrawingModelListener {

	/**
	 * Definida što promatrač treba napraviti kada se u model dodaju novi
	 * objekti.
	 * 
	 * @param source
	 *            crtaći model
	 * @param index0
	 *            indeks od kojeg su nadodani objekti
	 * @param index1
	 *            indeks do kojeg su nadodani objekti
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Definida što promatrač treba napraviti kada se iz modela uklone objekti.
	 * 
	 * @param source
	 *            crtaći model
	 * @param index0
	 *            indeks od kojeg su uklanjani objekti
	 * @param index1
	 *            indeks do kojeg su uklanjani objekti
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Definida što promatrač treba napraviti kada se u modelu promijene
	 * objekti.
	 * 
	 * @param source
	 *            crtaći model
	 * @param index0
	 *            indeks od kojeg su objekti mijenjani
	 * @param index1
	 *            indeks do kojeg su objekti mijenjani
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
