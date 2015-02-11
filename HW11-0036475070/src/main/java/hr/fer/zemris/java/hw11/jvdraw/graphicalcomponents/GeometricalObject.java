package hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents;

import hr.fer.zemris.java.hw11.jvdraw.drawing.DrawingModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;

/**
 * Vršna klasa za sve grafičke objekte koje aplikacija nudi za crtanje.
 * 
 * @author Ivan Relić
 * 
 */
public abstract class GeometricalObject {

	/**
	 * Za svaki pojedini lik, ova metoda će vraćati file zapis za njega.
	 * 
	 * @return file zapis objekta
	 */
	public abstract String toFileString();

	/**
	 * Iscrtava lik predanom grafikom.
	 * 
	 * @param g2
	 *            grafika za crtanje
	 */
	public abstract void paint(Graphics2D g2);

	/**
	 * Metoda nad objektom obavlja potrebne operacije koje su potrebne kada se
	 * on počne iscrtavati u canvasu.
	 * 
	 * @param startingPoint
	 *            početna točka objekta
	 * @param foregroundColor
	 *            foreground boja
	 * @param backgroundColor
	 *            background boja
	 */
	public abstract void paintingStarted(Point startingPoint,
			Color foregroundColor, Color backgroundColor);

	/**
	 * Metoda nad objektom obavlja potrebne operacije koje su potrebne kada on
	 * završi s iscrtavanjem u canvasu.
	 * 
	 * @param endingPoint
	 *            završna točka objekta
	 */
	public abstract void paintingEnded(Point endingPoint);

	/**
	 * Ubacuje novi objekt u model koji je istog tipa kao objekt nad kojim je
	 * metoda pozvana.
	 * 
	 * @param model
	 *            crtaći model
	 */
	public abstract void addToModel(DrawingModel model);

	/**
	 * Kreira JOptionPane koji omogućuje promjenu parametara pojedinog objekta
	 * ili njegovo brisanje.
	 * 
	 * @param model
	 *            crtaći model
	 * @param parent
	 *            roditeljski frame nad kojim se poziva JOptionPane
	 * @param index
	 *            indeks objekta u modelu
	 */
	public abstract void getModifyPane(DrawingModel model, JFrame parent,
			int index);

	/**
	 * Vraća gornju lijevu točku koju objekt pokriva.
	 * 
	 * @return gornja lijeva točka
	 */
	public abstract Dimension getLeftUpperPoint();

	/**
	 * Vraća donju desnu točku koju objekt pokriva.
	 * 
	 * @return donja desna točka
	 */
	public abstract Dimension getRightLowerPoint();

	/**
	 * Pomiče točke objekta za primljenu dimenziju.
	 * 
	 * @param dimension
	 *            dimenzija za pomak
	 */
	public abstract void shiftPoint(Dimension dimension);
}