package hr.fer.zemris.java.hw11.jvdraw.drawing;

import hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents.GeometricalObject;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JList;

/**
 * Klasa implementira listener za listu koji nudi korisniku modificiranje
 * elemenata liste na dvoklik.
 * 
 * @author Ivan Relić
 * 
 */
public class MouseListModifyListener implements MouseListener {

	private DrawingModel model;
	private JFrame parent;
	private JList<GeometricalObject> list;

	/**
	 * Konstruktor. Prima referencu na crtaći model, parent frame i listu nad
	 * kojom se vrši promatranje.
	 * 
	 * @param model
	 *            crtaći model
	 * @param parent
	 *            parent frame
	 * @param list
	 *            lista geometrijskih objekata
	 */
	public MouseListModifyListener(DrawingModel model, JFrame parent,
			JList<GeometricalObject> list) {
		this.model = model;
		this.parent = parent;
		this.list = list;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int index = list.getSelectedIndex();
			if (index == -1) {
				return;
			}
			model.getObject(index).getModifyPane(model, parent, index);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
