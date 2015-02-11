package hr.fer.zemris.bool;

import static org.junit.Assert.*;
import org.junit.Test;

public class MaskTest {

    @Test
    public void MaskEqualsTest() {
	Mask m = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.ZERO, 
		MaskValue.ONE, MaskValue.ONE);
	Mask m2 = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.ZERO, 
		MaskValue.ONE, MaskValue.ONE);
	assertEquals(m.equals(m2), true);
    }
    
    @Test
    public void MaskNotEqualsTest() {
	Mask m = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.ZERO, 
		MaskValue.ONE, MaskValue.ONE);
	Mask m2 = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.ZERO, 
		MaskValue.ZERO, MaskValue.ONE);
	assertEquals(m.equals(m2), false);
    }
    
    @Test
    public void MaskParseTest() {
	Mask m = Mask.parse("01011");
	Mask m2 = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.ZERO, 
		MaskValue.ONE, MaskValue.ONE);
	assertEquals(m.equals(m2), true);
    }
        
    @Test
    public void MaskParseWithDontCaresTest() {
	Mask m = Mask.parse("01x1x");
	Mask m2 = new Mask(MaskValue.ZERO, MaskValue.ONE, MaskValue.DONT_CARE, 
		MaskValue.ONE, MaskValue.DONT_CARE);
	assertEquals(m.equals(m2), true);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskParseWithBadArgsTest() {
	Mask.parse("01050");
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskParseWithEmptyStringTest() {
	Mask.parse("");
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskParseWithNullStringTest() {
	Mask.parse(null);
    }
    
    @Test
    public void MaskToStringTest() {
	Mask m = Mask.parse("01x1x");
	String pom = m.toString();
	assertEquals(pom.equals("01x1x"), true);
    }
    
    @Test
    public void MaskFromIndexTest() {
	Mask m = Mask.fromIndex(3, 7);
	Mask m2 = Mask.parse("111");
	assertEquals(m.equals(m2), true);
    }
    
    @Test
    public void MaskFromIndex2Test() {
	Mask m = Mask.fromIndex(3, 5);
	Mask m2 = Mask.parse("101");
	assertEquals(m.equals(m2), true);
    }
    
    @Test
    public void MaskFromIndex3Test() {
	Mask m = Mask.fromIndex(4, 0);
	Mask m2 = Mask.parse("0000");
	assertEquals(m.equals(m2), true);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskFromIndexZeroSizeTest() {
	Mask.fromIndex(0, 5);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskFromIndexNegativeSizeTest() {
	Mask.fromIndex(-5, 5);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskFromIndexNegativeIndexTest() {
	Mask.fromIndex(5, -8);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskFromIndexOutOfDomainTest() {
	Mask.fromIndex(4, 25);
    }
    
    @Test
    public void MaskCombineTest() {
	assertEquals(Mask.combine(Mask.parse("0101"), Mask.parse("1101")).
		equals(Mask.parse("x101")), true);
    }
    
    @Test
    public void MaskCombine2Test() {
	assertEquals(Mask.combine(Mask.parse("01xx"), Mask.parse("11xx")).
		equals(Mask.parse("x1xx")), true);
    }
    
    @Test
    public void MaskCombineNotCombineableTest() {
	Mask m = Mask.parse("0101");
	Mask m2 = Mask.parse("1001");
	assertEquals(Mask.combine(m, m2) == null, true);
    }    
    
    @Test
    public void MaskCombineNotCombineable2Test() {
	Mask m = Mask.parse("0101");
	Mask m2 = Mask.parse("010x");
	assertEquals(Mask.combine(m, m2) == null, true);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskCombineMasksNotSameLengthTest() {
	Mask m = Mask.parse("1");
	Mask m2 = Mask.parse("010x");
	Mask.combine(m, m2);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void MaskCombineMasksAreNullTest() {
	Mask m = Mask.parse("1");
	Mask.combine(m, null);
    }
    
    @Test
    public void NumberOfZeroesInMaskTest() {
	Mask m = Mask.parse("0010010");
	assertEquals(m.getNumberOfZeroes() == 5, true);
    }
    
    @Test
    public void NumberOfZeroesInMask2Test() {
	Mask m = Mask.parse("1111");
	assertEquals(m.getNumberOfZeroes() == 0, true);
    }
    
    @Test
    public void NumberOfOnesInMaskTest() {
	Mask m = Mask.parse("11010001");
	assertEquals(m.getNumberOfOnes() == 4, true);
    }
    
    @Test
    public void NumberOfOnesInMask2Test() {
	Mask m = Mask.parse("0");
	assertEquals(m.getNumberOfOnes() == 0, true);
    }
    
    @Test
    public void isMoreGeneralThanTest() {
	Mask m = Mask.parse("010xx010");
	Mask m2 = Mask.parse("01010010");
	assertEquals(m.isMoreGeneralThan(m2), true);
    }
    
    @Test
    public void isMoreGeneralThan2Test() {
	Mask m = Mask.parse("010xx010");
	Mask m2 = Mask.parse("010xx010");
	assertEquals(m.isMoreGeneralThan(m2), false);
    }
    
    @Test
    public void isMoreGeneralThan3Test() {
	Mask m = Mask.parse("xxx");
	Mask m2 = Mask.parse("1x1");
	assertEquals(m.isMoreGeneralThan(m2), true);
    }

    @Test (expected = IllegalArgumentException.class)
    public void isMoreGeneralThanDifferentMaskLengthTest() {
	Mask m = Mask.parse("xxx0");
	Mask m2 = Mask.parse("1x1");
	m.isMoreGeneralThan(m2);
    } 
    
    @Test (expected = IllegalArgumentException.class)
    public void isMoreGeneralThanCompareWithNullMaskTest() {
	Mask m = Mask.parse("xxx0");
	m.isMoreGeneralThan(null);
    }   
}
