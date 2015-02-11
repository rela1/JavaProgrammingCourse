package hr.fer.zemris.java.hw11.jvdraw.graphicalcomponents;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class LineTest {

	@Test
	public void LineConstructorTest() {
		Line l = new Line(5, 5, 8, 8, Color.BLUE);
		assertEquals("Koordinata x početka mora biti jednaka 5!", 5,
				l.getStartX());
		assertEquals("Koordinata y početka mora biti jednaka 5!", 5,
				l.getStartY());
		assertEquals("Koordinata x kraja mora biti jednaka 8!", 8, l.getEndX());
		assertEquals("Koordinata y kraja mora biti jednaka 8!", 8, l.getEndY());
		assertEquals("Boja linije mora biti plava!", Color.BLUE, l.getColor());
	}
}
