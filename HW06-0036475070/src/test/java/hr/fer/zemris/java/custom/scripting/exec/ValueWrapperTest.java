package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ValueWrapperTest {
	
	@Test
	public void TwoIntegerIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new Integer(60);
		a.increment(b);
		assertEquals("Rezultat zbroja 50 i 60 je 110", a.getValue().equals(new Integer(110)), true);
	}
	
	@Test
	public void IntegerAndStringIntegerIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new String("47");
		a.increment(b);
		assertEquals("Rezultat zbroja 50 i 47 je 97", a.getValue().equals(new Integer(97)), true);
	}
	
	@Test
	public void IntegerAndNullIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = null;
		a.increment(b);
		assertEquals("Rezultat zbroja 50 i 0 je 50", a.getValue().equals(new Integer(50)), true);
	}
	
	@Test
	public void NullAndNullIncrementTest() {
		ValueWrapper a = new ValueWrapper(null);
		Object b = null;
		a.increment(b);
		assertEquals("Rezultat zbroja 0 i 0 je 0", a.getValue().equals(new Integer(0)), true);
	}
	
	@Test
	public void TwoDoubleIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(21.12));
		Object b = new Double(60.8);
		a.increment(b);
		assertEquals(81.92, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithDecimalDotIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(15.39));
		Object b = new String("-18.47");
		a.increment(b);
		assertEquals(-3.08, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithExponentIncrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(18.47E-3));
		Object b = new String("-18.47E-3");
		a.increment(b);
		assertEquals(0.0, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoStringDoubleWithExponentAndDecimalDotIncrementTest() {
		ValueWrapper a = new ValueWrapper(new String("58.12"));
		Object b = new String("-5.812E1");
		a.increment(b);
		assertEquals(0.0, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDoubleIncrementTest() {
		ValueWrapper a = new ValueWrapper(new String("58"));
		Object b = new String("12.5");
		a.increment(b);
		assertEquals(70.5, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble2IncrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(14));
		Object b = new String("22.1");
		a.increment(b);
		assertEquals(36.1, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble3IncrementTest() {
		ValueWrapper a = new ValueWrapper(new String("-85"));
		Object b = new Double(1E2);
		a.increment(b);
		assertEquals(15.0, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoIntegerDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new Integer(60);
		a.decrement(b);
		assertEquals("Rezultat oduzimanja 50 i 60 je -10", a.getValue().equals(new Integer(-10)),
				true);
	}
	
	@Test
	public void IntegerAndStringIntegerDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new String("47");
		a.decrement(b);
		assertEquals("Rezultat oduzimanja 50 i 47 je 3", a.getValue().equals(new Integer(3)), 
				true);
	}
	
	@Test
	public void TwoDoubleDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(21.12));
		Object b = new Double(60.8);
		a.decrement(b);
		assertEquals(-39.68, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithDecimalDotDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(15.39));
		Object b = new String("-18.47");
		a.decrement(b);
		assertEquals(33.86, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithExponentDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Double(18.47E-3));
		Object b = new String("-18.47E-3");
		a.decrement(b);
		assertEquals(0.03694, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoStringDoubleWithExponentAndDecimalDotDecrementTest() {
		ValueWrapper a = new ValueWrapper(new String("58.12"));
		Object b = new String("-5.812E1");
		a.decrement(b);
		assertEquals(116.24, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDoubleDecrementTest() {
		ValueWrapper a = new ValueWrapper(new String("58"));
		Object b = new String("12.5");
		a.decrement(b);
		assertEquals(45.5, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble2DecrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(14));
		Object b = new String("22.1");
		a.decrement(b);
		assertEquals(-8.1, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble3DecrementTest() {
		ValueWrapper a = new ValueWrapper(new String("-85"));
		Object b = new Double(1E2);
		a.decrement(b);
		assertEquals(-185, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndNullDecrementTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = null;
		a.decrement(b);
		assertEquals("Rezultat oduzimanja 50 i 0 je 50", a.getValue().equals(new Integer(50)), true);
	}
	
	@Test
	public void NullAndNullDecrementTest() {
		ValueWrapper a = new ValueWrapper(null);
		Object b = null;
		a.decrement(b);
		assertEquals("Rezultat oduzimanja 0 i 0 je 0", a.getValue().equals(new Integer(0)), true);
	}
	
	@Test
	public void TwoIntegerMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new Integer(60);
		a.multiply(b);
		assertEquals("Rezultat mnozenja 50 i 60 je 3000", a.getValue().equals(new Integer(3000)),
				true);
	}
	
	@Test
	public void IntegerAndStringIntegerMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = new String("47");
		a.multiply(b);
		assertEquals("Rezultat mnozenja 50 i 47 je 2350", a.getValue().equals(new Integer(2350)), 
				true);
	}
	
	@Test
	public void TwoDoubleMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Double(21.12));
		Object b = new Double(60.8);
		a.multiply(b);
		assertEquals(1284.096, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithDecimalDotMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Double(15.39));
		Object b = new String("-18.47");
		a.multiply(b);
		assertEquals(-284.2533, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithExponentMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Double(18.47E-3));
		Object b = new String("-18.47E-3");
		a.multiply(b);
		assertEquals(-0.0003411409, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoStringDoubleWithExponentAndDecimalDotMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new String("58.12"));
		Object b = new String("-5.812E1");
		a.multiply(b);
		assertEquals(-3377.9344, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDoubleMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new String("58"));
		Object b = new String("12.5");
		a.multiply(b);
		assertEquals(725, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble2MultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Integer(14));
		Object b = new String("22.1");
		a.multiply(b);
		assertEquals(309.4, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndNullMultiplyTest() {
		ValueWrapper a = new ValueWrapper(new Integer(50));
		Object b = null;
		a.multiply(b);
		assertEquals("Rezultat umnoska 50 i 0 je 0", a.getValue().equals(new Integer(0)), true);
	}
	
	@Test
	public void NullAndNullMultiplyTest() {
		ValueWrapper a = new ValueWrapper(null);
		Object b = null;
		a.multiply(b);
		assertEquals("Rezultat umnoska 0 i 0 je 0", a.getValue().equals(new Integer(0)), true);
	}
	
	@Test
	public void IntegerAndDouble3MultiplyTest() {
		ValueWrapper a = new ValueWrapper(new String("-85"));
		Object b = new Double(1E2);
		a.multiply(b);
		assertEquals(-8500, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoIntegerDivideTest() {
		ValueWrapper a = new ValueWrapper(new Integer(60));
		Object b = new Integer(50);
		a.divide(b);
		assertEquals("Rezultat dijeljenja 60 i 50 je 1", a.getValue().equals(new Integer(1)),
				true);
	}
	
	@Test
	public void IntegerAndStringIntegerDivideTest() {
		ValueWrapper a = new ValueWrapper(new Integer(100));
		Object b = new String("47");
		a.divide(b);
		assertEquals("Rezultat dijeljenja 100 i 47 je 2", a.getValue().equals(new Integer(2)), 
				true);
	}
	
	@Test
	public void TwoDoubleDivideTest() {
		ValueWrapper a = new ValueWrapper(new Double(21.12));
		Object b = new Double(60.8);
		a.divide(b);
		assertEquals(0.347368, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithDecimalDotDivideTest() {
		ValueWrapper a = new ValueWrapper(new Double(15.39));
		Object b = new String("-18.47");
		a.divide(b);
		assertEquals(-0.833243, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void DoubleAndStringDoubleWithExponentDivideTest() {
		ValueWrapper a = new ValueWrapper(new Double(18.47E-3));
		Object b = new String("-18.47E-3");
		a.divide(b);
		assertEquals(-1.0, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void TwoStringDoubleWithExponentAndDecimalDotDivideTest() {
		ValueWrapper a = new ValueWrapper(new String("58.12"));
		Object b = new String("-5.812E1");
		a.divide(b);
		assertEquals(-1.0, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDoubleDivideTest() {
		ValueWrapper a = new ValueWrapper(new String("58"));
		Object b = new String("12.5");
		a.divide(b);
		assertEquals(4.64, (Double) a.getValue(), 1E-6);
	}
	
	@Test
	public void IntegerAndDouble2DivideTest() {
		ValueWrapper a = new ValueWrapper(new Integer(14));
		Object b = new String("22.1");
		a.divide(b);
		assertEquals(0.633484, (Double) a.getValue(), 1E-6);
	}
		
	@Test
	public void IntegerAndDouble3DivideTest() {
		ValueWrapper a = new ValueWrapper(new String("-85"));
		Object b = new Double(1E2);
		a.divide(b);
		assertEquals(-0.85, (Double) a.getValue(), 1E-6);
	}
	
	@Test (expected=RuntimeException.class)
	public void IncrementUnknowObjectTest() {
		ValueWrapper a = new ValueWrapper(new String("-85a"));
		Object b = new Double(18);
		a.increment(b);
	}
	
	@Test (expected=RuntimeException.class)
	public void IncrementUnknowObject2Test() {
		ValueWrapper a = new ValueWrapper(new ArrayList<String>());
		Object b = new Double(18);
		a.increment(b);
	}
	
	@Test (expected=RuntimeException.class)
	public void IncrementUnknowObject3Test() {
		ValueWrapper a = new ValueWrapper(new String("absr"));
		Object b = new Double(18);
		a.increment(b);
	}
	
	@Test
	public void CompareToSmallerTest() {
		ValueWrapper a = new ValueWrapper(new Integer(60));
		Object b = new Integer(50);
		int val = a.numCompare(b);
		assertEquals("Rezultat usporedbe 60 i 50 je 1", val == 1,
				true);
	}
	
	@Test
	public void CompareToBiggerTest() {
		ValueWrapper a = new ValueWrapper(new Integer(60));
		Object b = new Integer(150);
		int val = a.numCompare(b);
		assertEquals("Rezultat usporedbe 60 i 150 je -1", val == -1,
				true);
	}
	
	@Test
	public void CompareToEqualTest() {
		ValueWrapper a = new ValueWrapper(new Integer(186));
		Object b = new Integer(186);
		int val = a.numCompare(b);
		assertEquals("Rezultat usporedbe 186 i 186 je 0", val == 0,
				true);
	}
	
	@Test
	public void CompareNullWithNullTest() {
		ValueWrapper a = new ValueWrapper(null);
		Object b = null;
		int val = a.numCompare(b);
		assertEquals("Rezultat usporedbe 0 i 0 je 0", val == 0,
				true);
	}
}
