package hr.fer.zemris.java.gui.layouts;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.Test;

public class CalcLayoutTest {

	@Test(expected = IllegalArgumentException.class)
	public void AddInFirstRowOnColumn2Test() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(1, 2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddInFirstRowOnColumn5Test() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(1, 5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddInRowOutOfBoundsTest() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(0, 5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddInRowOutOfBounds2Test() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(6, 5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddInColumnOutOfBoundsTest() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(2, -5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddInColumnOutOfBounds2Test() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), "3, 8");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void AddWithBadConstraintsObjectTest() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new JButton("1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void AddOnAlreadyTakenPositionTest() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		testPanel.add(new JButton("a"), new RCPosition(3, 5));
		testPanel.add(new JButton("a"), new RCPosition(3, 5));
	}

	@Test
	public void AddOnPositionRemoveAndAddOnTheSameTest() {
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new CalcLayout());
		JButton a = new JButton("a");
		testPanel.add(a, "3,  5");
		testPanel.remove(a);
		JButton c = new JButton("c");
		testPanel.add(c, new RCPosition(3, 5));
		Component[] components = testPanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			assertEquals("Jedina komponenta u layoutu bi trebao biti gumb c!",
					components[i].equals(c), true);
		}
	}
}
