package hr.fer.zemris.java.hw11.jvdraw.drawing;

import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa predstavlja implementaciju crtaćeg modela JVDraw aplikacije.
 * 
 * @author Ivan Relić
 * 
 */
public class JVDrawDrawingModel implements DrawingModel {

	private List<GeometricalObject> objects;
	private Set<DrawingModelListener> listeners;

	/**
	 * Konstruktor. Kreira liste za pohranu promatrača i objekata koji se
	 * trebaju iscrtavati.
	 */
	public JVDrawDrawingModel() {
		this.objects = new ArrayList<GeometricalObject>();
		this.listeners = new HashSet<DrawingModelListener>();
	}

	/**
	 * Vraća listu svih objekata koji su u modelu.
	 * 
	 * @return lista svih objekata
	 */
	public List<GeometricalObject> getAllObjects() {
		return new ArrayList<GeometricalObject>(objects);
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		int index = objects.size() - 1;
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, index, index);
		}
	}

	@Override
	public void updateObject(GeometricalObject object) {
		// dohvati indeks objekta koji je promijenjen
		int index = objects.indexOf(object);
		// ukloni trenutni objekt iz liste objekata i stavi novi na to mjesto
		objects.remove(index);
		objects.add(index, object);
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}
	}

	@Override
	public void removeObject(int index) {
		objects.remove(index);
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
	}

	@Override
	public void clear() {
		if (objects.size() == 0) {
			return;
		}
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, 0, objects.size() - 1);
		}
		objects.clear();
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners = new HashSet<DrawingModelListener>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners = new HashSet<DrawingModelListener>(listeners);
		listeners.remove(l);
	}
}
