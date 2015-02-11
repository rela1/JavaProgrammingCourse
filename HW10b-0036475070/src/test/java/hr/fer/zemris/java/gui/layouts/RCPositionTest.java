package hr.fer.zemris.java.gui.layouts;

import static org.junit.Assert.*;

import org.junit.Test;

public class RCPositionTest {

	@Test
	public void RCPositionParseTwoIntegersTest() {
		RCPosition pos = RCPosition.parse("  2   ,   6");
		assertEquals(
				"Objekt kreiran parsiranjem i čistim konstruktorom bi trebali biti isti!",
				new RCPosition(2, 6), pos);
	}

	@Test(expected = IllegalArgumentException.class)
	public void RCPositionParseOneInteger() {
		RCPosition.parse("   5   ,   ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void RCPositionParseTwoDoubles() {
		RCPosition.parse("   5.58   , 1.18  ");
	}
}
