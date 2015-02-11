package hr.fer.zemris.java.tecaj.hw3;

import static org.junit.Assert.*;
import org.junit.Test;

public class CStringTest {

	//privatni niz charactera za testiranje
	private char[] c = new char[] {'T', 'E','S', 'T', ' ', 'S', 'T', 'R', 'I', 'N', 'G'};
	
	
	@ Test 
	public void CStringToStringTest() {
		String s = (new CString(c).toString());
		assertEquals(s.equals("TEST STRING"), true);
	}
	
	
	@ Test
	public void SubstringTest() {
		CString s = new CString(c);
		assertEquals(s.substring(2, 7).toString().equals("ST ST"), true);
	}
	
	
	@ Test
	public void SubstringOfSubstringTest() {
		CString s = new CString(c);
		assertEquals(s.substring(2, 7).substring(1, 3).toString().equals("T "), true);
	}
	
	
	@ Test 
	public void CStringConstructorFromCharArrayTest() {
		CString s = new CString(c);
		assertEquals(s.toString().equals("TEST STRING"), true);
	}
	
	
	@ Test 
	public void CStringConstructorFromCharArrayWithOffsetAndLengthTest() {
		CString s = new CString(c, 2, 6);
		assertEquals(s.toString().equals("ST STR"), true);
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void CStringConstructorFromCharArrayWithBadOffsetAndLengthTest() {
		new CString(c, 11, 6);
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void CStringConstructorFromCharArrayWithBadOffsetAndBadLengthTest() {
		new CString(c, 11, 15);
	}
	
	
	@ Test (expected=IllegalArgumentException.class)
	public void CStringConstructorFromCharArrayWithOffsetAndBadLengthTest() {
		new CString(c, 2, 10);
	}		
	
	
	@ Test
	public void CStringConstructorFromCString() {
		CString s = new CString(c);
		CString s2 = new CString(s);
		assertEquals(s2.toString().equals("TEST STRING"), true);		
	}
	
	
	@ Test
	public void CStringConstructorFromString() {
		CString s = new CString("TEST STRING");
		assertEquals(s.toString().equals("TEST STRING"), true);
	}
	
	
	@ Test
	public void LengthTest() {
		CString s = new CString(c);
		assertEquals(s.length() == 11, true);
	}
	
	
	@ Test
	public void SubstringLength() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 11).substring(4, 6).length() == 2, true);
	}
	
	
	@ Test
	public void CharAtTest() {
		CString s = new CString(c);
		assertEquals(s.charAt(7) == 'R', true);
	}
	
	
	@ Test
	public void CharAtSubstringTest() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 11).substring(4, 9).charAt(2) == 'R', true);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void CharAtLessThanZeroTest() {
		(new CString(c)).charAt(-5);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void CharAtMoreThanLengthMinusOne() {
		CString s = new CString(c);
		s.charAt(s.length());
	}
	
	
	@ Test
	public void IndexOfTest() {
		CString s = new CString(c);
		assertEquals(s.indexOf('I') == 8, true);
	}
	
	
	@ Test
	public void SubstringIndexOfCharExistsTest() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 11).substring(4, 9).indexOf('I') == 3, true);
	}
	
	
	@ Test
	public void SubstringIndexOfCharNotExistTest() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 11).substring(4, 9).indexOf('G') == -1, true);
	}
	
	
	@ Test
	public void StartsWithTest() {
		CString s = new CString(c);
		assertEquals(s.startsWith(new CString("TEST S")), true);
	}
	
	
	@ Test
	public void SubstringStartsWithTest() {
		CString s = new CString(c);
		assertEquals(s.substring(3, 11).startsWith(new CString("T STR")), true);
	}
	
	
	@ Test
	public void StartsWithStringLargerThanStringItselfTest() {
		CString s = new CString(c);
		assertEquals(s.startsWith(new CString("TEST STRING!")), false);
	}
	
	
	@ Test
	public void StartsWithEmptyStringTest() {
		CString s = new CString(c);
		assertEquals(s.startsWith(new CString("")), true);
	}
	
	
	@ Test
	public void EndsWithTest() {
		CString s = new CString(c);
		assertEquals(s.endsWith(new CString("ING")), true);
	}
	
	
	@ Test
	public void SubstringEndsWithTest() {
		CString s = new CString(c);
		assertEquals(s.substring(3, 7).endsWith(new CString(" ST")), true);
	}
	
	
	@ Test
	public void EndsWithStringLargerThanStringItselfTest() {
		CString s = new CString(c);
		assertEquals(s.endsWith(new CString("TEST STRING!")), false);
	}
	
	
	@ Test
	public void EndsWithEmptyStringTest() {
		CString s = new CString(c);
		assertEquals(s.endsWith(new CString("")), true);
	}
	
	
	@ Test
	public void ContainsTest() {
		CString s = new CString(c);
		assertEquals(s.contains(new CString("T S")), true);
	}
	
	
	@ Test
	public void SubstringContainsTest() {
		CString s = new CString(c);
		assertEquals(s.substring(3, 11).contains(new CString("T S")), true);
	}
	
	
	@ Test
	public void ContainsStringLargerThanStringItselfTest() {
		CString s = new CString(c);
		assertEquals(s.contains(new CString("TEST STRING!")), false);
	}
	
	
	@ Test
	public void ContainsEmptyStringTest() {
		CString s = new CString(c);
		assertEquals(s.contains(new CString("")), true);
	}
	
	
	@ Test 
	public void LeftOfStringTest() {
		CString s = new CString(c);
		assertEquals(s.left(5).toString().equals("TEST "), true);
	}
	
	@ Test 
	public void LeftOfSubstringTest() {
		CString s = new CString(c);
		assertEquals(s.substring(2, 11).substring(1, 8).left(4).toString().equals("T ST"),
				true);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void LeftNegativeElementsWantedTest() {
		(new CString(c)).left(-5);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void LeftMoreThanLengthElementsWantedTest() {
		CString s = new CString(c);
		s.left(s.length() + 1);
	}
	
	
	@ Test 
	public void RightOfStringTest() {
		CString s = new CString(c);
		assertEquals(s.right(5).toString().equals("TRING"), true);
	}
	
	@ Test 
	public void RightOfSubstringTest() {
		CString s = new CString(c);
		assertEquals(s.substring(2, 11).substring(1, 8).right(4).toString().equals("TRIN"),
				true);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void RightNegativeElementsWantedTest() {
		(new CString(c)).right(-5);
	}
	
	
	@ Test(expected=IllegalArgumentException.class)
	public void RightMoreThanLengthElementsWantedTest() {
		CString s = new CString(c);
		s.right(s.length() + 1);
	}
	
	
	@ Test
	public void AddStringsTest() {
		CString s = new CString(c);
		CString s2 = new CString(" AND ADD METHOD!");
		assertEquals(s.add(s2).toString().equals("TEST STRING AND ADD METHOD!"), true);
	}
	
	
	@ Test
	public void AddSubstringsTest() {
		CString s = new CString(c);
		CString s2 = new CString(" AND ADD METHOD!");
		assertEquals((s.substring(0, 4)).add(s2.substring(0, 8)).toString().equals(
				"TEST AND ADD"), true);
	}
	
	
	@ Test 
	public void ReplaceAllCharVersionTest() {
		CString s = new CString(c);
		assertEquals(s.replaceAll('T', 'X').toString().equals("XESX SXRING"), true);
	}
	
	
	@ Test 
	public void SubstringReplaceAllCharVersionTest() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 10).substring(1, 8).replaceAll('S', 'a').toString().
				equals("aT aTRI"), true);
	}
	
	
	@ Test 
	public void ReplaceAllStringVersionEmptyOldStringTest() {
		CString s = new CString(c);
		assertEquals(s.replaceAll(new CString(""), s).toString().equals("TEST STRING"), true);
	}
	
	
	@ Test 
	public void SubstringReplaceAllStringVersionTest() {
		CString s = new CString(c);
		assertEquals(s.substring(1, 10).substring(1, 8).replaceAll(new CString("T "), 
				new CString("NEW")).toString().equals("SNEWSTRI"), true);
	}
	
	
	@ Test 
	public void ReplaceAllStringVersionEmptyNewStringTest() {
		CString s = new CString(c);
		assertEquals(s.replaceAll(new CString("T S"), new CString("")).toString().
				equals("TESTRING"), true);
	}
	
	
	@ Test
	public void ReplaceAllWithNothingToReplaceTest() {
		CString s = new CString(c);
		assertEquals(s.replaceAll(new CString("STRUNG"), new CString("NEW")).toString().
				equals("TEST STRING"), true);
	}
		
}
