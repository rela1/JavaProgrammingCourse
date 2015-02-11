package hr.fer.zemris.linearna;

import static org.junit.Assert.*;

import org.junit.Test;

public class VectorTest {

	@Test
	public void constructorCopyElementsArrayOnMutableFlag() {
		double[] el = new double[] {
				5, 6.58, 8.74
		};
		IVector v = new Vector(false, false, el);
		el[2] = 5;
		assertEquals("Broj na poziciji 2 bi i dalje trebao biti 8.74", Math.abs(v.get(2) - 8.74) < 1E-6, true);
	}
	
	@Test(expected=UnmodifiableObjectException.class)
	public void vectorReadOnlyTest() {
		IVector v = new Vector(true, true, new double[5]);
		v.set(0, 5);
	}
	
	@Test
	public void parsedVectorEqualsManuallyCreatedTest() {
		IVector v1 = Vector.parseSimple("0 8 4 1 7");
		IVector v2 = new Vector(0, 8, 4, 1, 7);
		assertEquals("Isparsirani vektor odgovara manualno kreiranome.", v1.equals(v2), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void parseStringContainingNotOnlyNumbersTest() {
		Vector.parseSimple("  5    8   p");
	}
	
	@Test
	public void getFromCorrectIndexTest() {
		IVector v1 = Vector.parseSimple("0 8 4.358 1 7");
		assertEquals("Broj s pozicije 2 treba biti 4.358.", Math.abs(v1.get(2) - 4.358) < 1E-6, true);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getFromBadIndexTest() {
		IVector v1 = new Vector(5, 8, 9.5870);
		v1.get(3);
	}
	
	@Test
	public void setToCorrectIndexTest() {
		IVector v1 = new Vector(1, 2, 3.48);
		v1.set(2, 3.144);
		assertEquals("Broj s pozicije 2 treba biti 3.144", Math.abs(v1.get(2) - 3.144) < 1E-6, true); 
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void setToBadIndexTest() {
		IVector v1 = new Vector(5, 8, 9.5870);
		v1.set(3, 258);
	}
	
	@Test
	public void copyIsEqualToOriginalTest() {
		IVector v1 = Vector.parseSimple("5.58 -4.878 5.58 -98");
		assertEquals("Kopija vektora treba biti identicna originalu.", v1.equals(v1.copy()), true);
	}
	
	@Test
	public void changeInCopyIsNotChangeInOriginalTest() {
		IVector v1 = Vector.parseSimple("-5 -4 -3 -7");
		IVector v2 = v1.copy();
		v2.set(2, -50);
		assertEquals("Broj na poziciji 2 v1 treba biti -3.", 
				Math.abs(v1.get(2) + 3) < 1E-6, true);
		assertEquals("Broj na poziciji 2 v2 treba biti -50.", 
				Math.abs(v2.get(2) + 50) < 1E-6, true);
	}
	
	@Test
	public void newInstanceEqualsZeroVectorTest() {
		IVector v1 = Vector.parseSimple("0 0 0 0 0 0");
		IVector v2 = v1.newInstance(6);
		assertEquals("Nova instanca vektora v1 iste velicine kao on, treba biti identicna njemu.", 
				v1.equals(v2), true);
	}
	
	@Test
	public void copyPartWithPartSmallerThanOriginalTest() {
		IVector v1 = Vector.parseSimple("5 8 7 1.147");
		assertEquals("Copy part 2 vektora v1 odgovara vektoru (5, 8)", 
				v1.copyPart(2).equals(Vector.parseSimple("5 8")), true);
	}
	
	@Test
	public void copyPartWithPartBiggerThanOriginalTest() {
		IVector v1 = Vector.parseSimple("5 8 7 1.147");
		assertEquals("Copy part 7 vektora v1 odgovara vektoru (5, 8, 7, 1.147, 0, 0, 0)", 
				v1.copyPart(7).equals(Vector.parseSimple("5 8 7 1.147 0 0 0")), true);
	}
	
	@Test
	public void addWithCorrectParametersTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Zbroj v1 s vektorom  je (10, 10, 10)", v1.add(Vector.parseSimple(
				"5.85  1.25   7.75")).equals(Vector.parseSimple("10 10 10")), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void addWithBadParametersTest() {
		IVector v1 = Vector.parseSimple("1.859");
		v1.add(Vector.parseSimple("5 8"));
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void addWithNullTest() {
		Vector.parseSimple("5 2 8").add(null);
	}
	
	@Test
	public void nAddWithCorrectParametersTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Zbroj v1 s vektorom  je (10, 10, 10)", v1.nAdd(Vector.parseSimple(
				"5.85  1.25   7.75")).equals(Vector.parseSimple("10 10 10")), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nAddWithBadParametersTest() {
		IVector v1 = Vector.parseSimple("1.859");
		v1.nAdd(Vector.parseSimple("5 8"));
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nAddWithNullTest() {
		Vector.parseSimple("5 2 8").nAdd(null);
	}
	
	@Test
	public void subWithCorrectParametersTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Razlika v1 s vektorom  je (4, 8, 2)", v1.sub(Vector.parseSimple(
				"0.15  0.75   0.25")).equals(Vector.parseSimple("4 8 2")), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void subWithBadParametersTest() {
		IVector v1 = Vector.parseSimple("1.859");
		v1.add(Vector.parseSimple("5 8"));
	}
	
	@Test
	public void nSubWithCorrectParametersTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Razlika v1 s vektorom  je (4, 8, 2)", v1.nSub(Vector.parseSimple(
				"0.15  0.75   0.25")).equals(Vector.parseSimple("4 8 2")), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nSubWithBadParametersTest() {
		IVector v1 = Vector.parseSimple("1.859");
		v1.nSub(Vector.parseSimple("5 8"));
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nASubWithNullTest() {
		Vector.parseSimple("5 2 8").nSub(null);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void subWithNullTest() {
		Vector.parseSimple("5 2 8").sub(null);
	}
	
	@Test
	public void scalarMultiplyTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Umnozak v1 s 2  je (8.3, 17.5, 4.5)", 
				v1.scalarMultiply(2).equals(Vector.parseSimple("8.3 17.5 4.5")), true);
	}
	
	@Test
	public void nScalarMultiplyTest() {
		IVector v1 = Vector.parseSimple("4.15 8.75 2.25");
		assertEquals("Umnozak v1 s 2  je (8.3, 17.5, 4.5)", 
				v1.nScalarMultiply(2).equals(Vector.parseSimple("8.3 17.5 4.5")), true);
	}
	
	@Test
	public void normTest() {
		IVector v1 = Vector.parseSimple("3 4");
		assertEquals("Norma vektora s elementima 3, 4 je 5.", Math.abs(v1.norm() - 5) < 1E-6, true);
	}
	
	@Test
	public void normalizeTestNotNullVector() {
		IVector v1 = Vector.parseSimple("1 1 1 1");
		assertEquals("Normalizirani vektor v1 odgovara vektoru (0.5, 0.5, 0.5, 0.5)", 
				Vector.parseSimple("0.5 0.5 0.5 0.5").equals(v1.normalize()), true);
	}
	
	@Test
	public void nNormalizeTestNotNullVector() {
		IVector v1 = Vector.parseSimple("1 1 1 1");
		assertEquals("Normalizirani vektor v1 odgovara vektoru (0.5, 0.5, 0.5, 0.5)", 
				Vector.parseSimple("0.5 0.5 0.5 0.5").equals(v1.nNormalize()), true);
	}
	
	@Test 
	public void cosineVectorTest() {
		IVector v1 = Vector.parseSimple("1 0 0");
		IVector v2 = Vector.parseSimple("0 0 1");
		assertEquals("Kosinus kuta izmedju ova dva vektora bi trebao biti 0.",
				Math.abs(v1.cosine(v2)) < 1E-6, true);
	}
	
	@Test (expected=IncompatibleOperandException.class)
	public void cosineBadDimensionVectorTest() {
		IVector v1 = Vector.parseSimple("1  0");
		IVector v2 = Vector.parseSimple("0 0 1");
		v1.cosine(v2);
	}
	
	@Test
	public void scalarProductCorrectDimensionsTest() {
		IVector v1 = Vector.parseSimple("5 2 4");
		IVector v2 = Vector.parseSimple("2 5 2.5");
		assertEquals("Skalarni produkt ova dva vektora bi trebao biti 30.",
				Math.abs(v1.scalarProduct(v2) - 30) < 1E-6, true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void scalarProductIncorrectDimensionsTest() {
		IVector v1 = Vector.parseSimple("5 ");
		IVector v2 = Vector.parseSimple("2 5 2.5");
		v1.scalarProduct(v2);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void scalarProductWithNullTest() {
		IVector v1 = Vector.parseSimple("5 ");
		v1.scalarProduct(null);
	}
	
	@Test
	public void nVectorProductCorrectDimensionsTest() {
		IVector v1 = Vector.parseSimple("5 2 4");
		IVector v2 = Vector.parseSimple("3.5 8.75 9.16");
		assertEquals("Vektorski produkt ova dva vektora bi trebao biti (-16.68, -31.8, 36.75)",
				v1.nVectorProduct(v2).equals(Vector.parseSimple("-16.68  -31.8   36.75")), true);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nVectorProductIncorrectDimensionsTest() {
		IVector v1 = Vector.parseSimple("5 ");
		IVector v2 = Vector.parseSimple("2 5 2.5");
		v1.nVectorProduct(v2);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nVectorProductWithNullTest() {
		IVector v1 = Vector.parseSimple("5 ");
		v1.nVectorProduct(null);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nFromHomogeneusWith1DimensionVectorTest() {
		Vector.parseSimple("2").nFromHomogeneus();
	}
	
	@Test
	public void nFromHomogeneusWithCorrectDimension() {
		assertEquals("Vektor iz radnog prostora bi trebao biti jednak (1, 2, 3, 4)",
				Vector.parseSimple("2 4 6 8 2").nFromHomogeneus().equals(Vector.parseSimple("1 2 3 4")), true);
	}
	
	@Test
	public void toArrayReturnCopyOfOwnElements() {
		IVector v1 = Vector.parseSimple("5 6.8 7.258");
		double[] elements = v1.toArray();
		elements[2] = 10;
		assertEquals("Broj na poziciji 2 polja elements treba biti 10.", 
				Math.abs(elements[2] - 10) < 1E-6, true);
		assertEquals("Broj na poziciji 2 v2 treba biti 7.258.", 
				Math.abs(v1.get(2) - 7.258) < 1E-6, true);
	}
}
