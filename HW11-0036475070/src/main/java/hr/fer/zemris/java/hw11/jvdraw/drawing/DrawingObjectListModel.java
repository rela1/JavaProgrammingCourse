package hr.fer.zemris.java.hw11.jvdraw.drawing;

import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;

import javax.swing.AbstractListModel;

/**
 * Implementacija liste koja prikazuje sve elemente iz modela.
 * 
 * @author Ivan Relić
 * 
 */
public class DrawingObjectListModel extends
		AbstractListModel<GeometricalObject> {

	private static final long serialVersionUID = 1L;
	private JVDrawDrawingModel model;

	/**
	 * Konstruktor. Prihvaća referencu na model za crtanje koji sadrži podatke o
	 * objektima.
	 * 
	 * @param model
	 *            crtaći model
	 */
	public DrawingObjectListModel(JVDrawDrawingModel model) {
		this.model = model;
		// dodaj listenere da se model liste promijeni kad se promijeni model
		this.model.addDrawingModelListener(new DrawingModelListener() {
			@Override
			public void objectsRemoved(DrawingModel source, int index0,
					int index1) {
				DrawingObjectListModel.this.fireIntervalRemoved(source, index0,
						index1);
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0,
					int index1) {
				DrawingObjectListModel.this.fireContentsChanged(source, index0,
						index1);
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				DrawingObjectListModel.this.fireIntervalAdded(source, index0,
						index1);
			}
		});
	}

	@Override
	public int getSize() {
		return this.model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return this.model.getObject(index);
	}

}
