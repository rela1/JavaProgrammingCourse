package hr.fer.zemris.java.tecaj.hw3;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexNumberTest {

	@ Test
	public void ConstructorTest() {
		ComplexNumber c = new ComplexNumber(5, 4);
		double real = c.getReal();
		double imaginary = c.getImaginary();
		assertEquals(imaginary == 4 && real == 5, true);
	}
	
	
	@ Test 
	public void getRealTest() {
		assertEquals((new ComplexNumber(5, 6).getReal() == 5), true);
	}
	
	
	@ Test 
	public void getImaginaryTest() {
		assertEquals((new ComplexNumber(5, 6).getImaginary() == 6), true);
	}
	
	
	@ Test 
	public void getMagnitudeTest() {
		assertEquals(Math.abs(new ComplexNumber(1, 1).getMagnitude() - Math.sqrt(2)) < 1E-6, 
				true);
	}
	
	
	@ Test 
	public void getAngleTest() {
		assertEquals(Math.abs(new ComplexNumber(1, 1).getAngle() - Math.PI/4) < 1E-6, 
				true);
	}
	
	
	@ Test
	public void FromImaginaryTest() {
		ComplexNumber c = ComplexNumber.fromImaginary(5);
		assertEquals(c.getReal() == 0 && c.getImaginary() == 5, true);
		
	}
	
	
	@ Test
	public void FromRealTest() {
		ComplexNumber c = ComplexNumber.fromReal(5);
		assertEquals(c.getReal() == 5 && c.getImaginary() == 0, true);
		
	}
	
	
	@ Test
	public void FromMagnitudeAndAngleTestForPositiveMagnitude() {
		ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2), Math.PI / 4);
		assertEquals(Math.abs(c.getReal() - 1) < 1E-6 
				&& Math.abs(c.getImaginary() - 1) < 1E-6, true);
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void FromMagnitudeAndAngleTestForNegativeMagnitude() {
		ComplexNumber.fromMagnitudeAndAngle(-5, Math.PI / 4);
	}
	
	
	@ Test 
	public void ParseRealAndImaginary1Test() {
		ComplexNumber c = ComplexNumber.parse("5.7+12.87i");
		assertEquals(Math.abs(c.getReal() - 5.7) < 1E-6 
				&& Math.abs(c.getImaginary() - 12.87) < 1E-6, true);
	}
	
	
	@ Test 
	public void ParseRealAndImaginary2Test() {
		ComplexNumber c = ComplexNumber.parse("+5.7+12.87i");
		assertEquals(Math.abs(c.getReal() - 5.7) < 1E-6 
				&& Math.abs(c.getImaginary() - 12.87) < 1E-6, true);
	}
	
	
	@ Test 
	public void ParseRealAndImaginary3Test() {
		ComplexNumber c = ComplexNumber.parse("5.7-12.87i");
		assertEquals(Math.abs(c.getReal() - 5.7) < 1E-6 
				&& Math.abs(c.getImaginary() + 12.87) < 1E-6, true);
	}
	
	
	@ Test 
	public void ParseRealAndImaginary4Test() {
		ComplexNumber c = ComplexNumber.parse("-5.7-12.87i");
		assertEquals(Math.abs(c.getReal() + 5.7) < 1E-6 
				&& Math.abs(c.getImaginary() + 12.87) < 1E-6, true);
	}
	
	
	@ Test
	public void ParseOnlyRealPositiveTest() {
		ComplexNumber c = ComplexNumber.parse("187.478");
		assertEquals(Math.abs(c.getReal() - 187.478) < 1E-6 && c.getImaginary() == 0, true);
	}
	
	
	@ Test
	public void ParseOnlyRealNegativeTest() {
		ComplexNumber c = ComplexNumber.parse("-187.478");
		assertEquals(Math.abs(c.getReal() + 187.478) < 1E-6 && c.getImaginary() == 0, true);
	}
	
	
	@ Test
	public void ParseOnlyImaginaryPositiveTest() {
		ComplexNumber c = ComplexNumber.parse("+187.478i");
		assertEquals(c.getReal() == 0 && Math.abs(c.getImaginary() - 187.478) < 1E-6, true);
	}
	
	
	@ Test
	public void ParseOnlyImaginaryNegativeTest() {
		ComplexNumber c = ComplexNumber.parse("-187.478i");
		assertEquals(c.getReal() == 0 && Math.abs(c.getImaginary() + 187.478) < 1E-6, true);
	}
	
	
	@ Test
	public void ParseOnlyImaginaryWithoutNumbersPositiveTest() {
		ComplexNumber c = ComplexNumber.parse("+i");
		assertEquals(c.getReal() == 0 && c.getImaginary() == 1, true);
	}
	
	
	@ Test
	public void ParseOnlyImaginaryWithoutNumbersPositive2Test() {
		ComplexNumber c = ComplexNumber.parse("i");
		assertEquals(c.getReal() == 0 && c.getImaginary() == 1, true);
	}
	
	
	@ Test
	public void ParseOnlyImaginaryWithoutNumbersNegativeTest() {
		ComplexNumber c = ComplexNumber.parse("-i");
		assertEquals(c.getReal() == 0 && c.getImaginary() == -1, true);
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void ParseWithBadStringTest() {
		ComplexNumber.parse("5+5.78");
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void ParseWithBadString2Test() {
		ComplexNumber.parse("5.78ii");
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void ParseWithBadString3Test() {
		ComplexNumber.parse("i587.5");
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void ParseWithBadString4Test() {
		ComplexNumber.parse("i+8.47");
	}
	
	
	@ Test 
	public void AddRealAndImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(5, -9)).add(new ComplexNumber(-9, 18));
		assertEquals(c.getReal() == -4 && c.getImaginary() == 9, true);
	}
	
	
	@ Test 
	public void AddOnlyImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(0, -9)).add(new ComplexNumber(0, 18.57));
		assertEquals(c.getReal() == 0 && Math.abs(c.getImaginary() - 9.57) < 1E-6, true);
	}
	
	
	@ Test 
	public void AddOnlyRealTest() {
		ComplexNumber c = (new ComplexNumber(5.478, 0)).add(new ComplexNumber(4.522, 0));
		assertEquals(Math.abs(c.getReal() - 10) < 1E-6 && c.getImaginary() == 0, true);
	}
	
	
	@ Test 
	public void SubRealAndImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(5, -9)).sub(new ComplexNumber(-9, 18));
		assertEquals(Math.abs(c.getReal() - 14) < 1E-6  
				&& Math.abs(c.getImaginary() + 27) < 1E-6, true);
	}
	
	
	@ Test 
	public void SubOnlyImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(0, -9)).sub(new ComplexNumber(0, 18.57));
		assertEquals(Math.abs(c.getReal() + 0) < 1E-6 
				&& Math.abs(c.getImaginary() + 27.57) < 1E-6, true);
	}
	
	
	@ Test 
	public void SubOnlyRealTest() {
		ComplexNumber c = (new ComplexNumber(5.478, 0)).sub(new ComplexNumber(4.522, 0));
		assertEquals(Math.abs(c.getReal() - 0.956) < 1E-6 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void MultiplyRealAndImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(1, 1)).mul(new ComplexNumber(1, -1));
		assertEquals(Math.abs(c.getReal() - 2) < 1E-6 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void MultiplyOnlyRealTest() {
		ComplexNumber c = (new ComplexNumber(1, 0)).mul(new ComplexNumber(9.5789, 0));
		assertEquals(Math.abs(c.getReal() - 9.5789) < 1E-6 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void MultiplyOnlyImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(0, -5)).mul(new ComplexNumber(0, 5));
		assertEquals(c.getReal() == 25 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void DivideRealAndImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(4, -2)).div(new ComplexNumber(2, 9));
		assertEquals(Math.abs(c.getReal() + 2.0/17.0) < 1E-6 
				&& Math.abs(c.getImaginary() + 8.0/17.0) < 1E-6, true);
	}
	
	
	@ Test
	public void DivideOnlyRealTest() {
		ComplexNumber c = (new ComplexNumber(4.5, 0)).div(new ComplexNumber(1.5, 0));
		assertEquals(Math.abs(c.getReal() - 3) < 1E-6 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void DivideOnlyImaginaryTest() {
		ComplexNumber c = (new ComplexNumber(0, 72.9)).div(new ComplexNumber(0, 8.1));
		assertEquals(Math.abs(c.getReal() - 9) < 1E-6 
				&& Math.abs(c.getImaginary() - 0) < 1E-6, true);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void PowerOnNegativeExpTest() {
		ComplexNumber c = new ComplexNumber(0, 72.9);
		c.power(-5);
	}
	
	
	@ Test
	public void PowerOnZeroTest() {
		ComplexNumber c = (new ComplexNumber(3, -4)).power(0);
		assertEquals(Math.abs(c.getReal() - 1) < 1E-6 
				&& Math.abs(c.getImaginary() + 0) < 1E-6, true);
	}
	
	
	@ Test
	public void PowerOnOneTest() {
		ComplexNumber c = (new ComplexNumber(3, -4)).power(1);
		assertEquals(Math.abs(c.getReal() - 3) < 1E-6 
				&& Math.abs(c.getImaginary() + 4) < 1E-6, true);
	}
	
	
	@ Test
	public void PowerOnSquareTest() {
		ComplexNumber c = (new ComplexNumber(3, -4)).power(2);
		assertEquals(Math.abs(c.getReal() + 7) < 1E-6 
				&& Math.abs(c.getImaginary() + 24) < 1E-6, true);
	}
	
	
	@ Test
	public void PowerOnSixTest() {
		ComplexNumber c = (new ComplexNumber(3, -4)).power(6);
		assertEquals(Math.abs(c.getReal() - 11753) < 1E-6 
				&& Math.abs(c.getImaginary() - 10296) < 1E-6, true);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void ZeroRootTest() {
		new ComplexNumber(3, -4).root(0);
	}
	
	
	@ Test
	public void FirstRootTest() {
		ComplexNumber[] c = (new ComplexNumber(3, -4)).root(1);
		assertEquals(Math.abs(c[0].getReal() - 3) < 1E-6 
				&& Math.abs(c[0].getImaginary() + 4) < 1E-6, true);
	}
	
	
	@ Test
	public void SecondRootTest() {
		ComplexNumber[] c = (new ComplexNumber(3, 4)).root(2);
		assertEquals(Math.abs(c[0].getReal() - 2) < 1E-6 
				&& Math.abs(c[0].getImaginary() - 1) < 1E-6, true);
		assertEquals(Math.abs(c[1].getReal() + 2) < 1E-6 
				&& Math.abs(c[1].getImaginary() + 1) < 1E-6, true);
	}
	
	
	@ Test
	public void ToStringRealAndImaginaryTest() {
		String s = (new ComplexNumber(5.87, -9.541)).toString();
		assertEquals(s.equals("5.87-9.541i"), true);
	}
	
	
	@ Test
	public void ToStringOnlyRealTest() {
		String s = (new ComplexNumber(5.87, 0)).toString();
		assertEquals(s.equals("5.87"), true);
	}
	
	
	@ Test
	public void ToStringOnlyImaginaryTest() {
		String s = (new ComplexNumber(0, -8.57)).toString();
		assertEquals(s.equals("-8.57i"), true);
	}
}
