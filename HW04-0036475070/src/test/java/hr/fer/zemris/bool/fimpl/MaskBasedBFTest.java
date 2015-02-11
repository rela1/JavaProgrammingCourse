package hr.fer.zemris.bool.fimpl;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.Masks;

public class MaskBasedBFTest {

    //definicija dvije funkcije, testovi slijede 
    private BooleanVariable A = new BooleanVariable("A");
    private BooleanVariable B = new BooleanVariable("B");
    private BooleanVariable C = new BooleanVariable("C");
    
    private BooleanFunction f1 = new MaskBasedBF(
	    "f1",
	    Arrays.asList(A, B, C),
	    true,
	    Masks.fromIndexes(3, 1, 3, 5, 7),
	    new ArrayList<Mask>()
	    );
    
    private BooleanFunction f2 = new MaskBasedBF(
	    "f2",
	    Arrays.asList(A, B, C),
	    false,
	    Masks.fromIndexes(3, 4, 5, 6),
	    Masks.fromIndexes(3, 2)
	    );
    
    private BooleanFunction f3 = new MaskBasedBF(
	    "f3",
	    Arrays.asList(A, B, C),
	    true,
	    Masks.fromIndexes(3, 2, 3, 4, 5),
	    new ArrayList<Mask>()
	    );
    
    
    /*test uzima minterm iterator funkcije f1 i prolazi svim mintermima i provjerava jesu 
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f1mintermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f1.mintermIterable()) {
	    assertEquals(f1.hasMinterm(index), true);
	    assertEquals(f1.hasMaxterm(index), false);
	    assertEquals(f1.hasDontCare(index), false);
	}
    }
    
    /*test uzima minterm iterator funkcije f1 i prolazi svim makstermima i provjerava jesu 
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f1maxtermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f1.maxtermIterable()) {
	    assertEquals(f1.hasMinterm(index), false);
	    assertEquals(f1.hasMaxterm(index), true);
	    assertEquals(f1.hasDontCare(index), false);
	}
    }
    
    @Test
    public void f1getValueForCombination0x0Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.DONT_CARE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f1.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void f1getValueForCombination00xTest() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.FALSE);
	C.setValue(BooleanValue.DONT_CARE);
	assertEquals(f1.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test
    public void f1getValueForCombination101Test() {
	A.setValue(BooleanValue.TRUE);
	B.setValue(BooleanValue.FALSE);
	C.setValue(BooleanValue.TRUE);
	assertEquals(f1.getValue() == BooleanValue.TRUE, true);
    }
    
    /*test uzima minterm iterator funkcije f2 i prolazi svim mintermima i provjerava jesu 
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f2mintermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f2.mintermIterable()) {
	    assertEquals(f2.hasMinterm(index), true);
	    assertEquals(f2.hasMaxterm(index), false);
	    assertEquals(f2.hasDontCare(index), false);
	}
    }
    
    /*test uzima minterm iterator funkcije f2 i prolazi svim makstermima i provjerava jesu
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f2maxtermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f2.maxtermIterable()) {
	    assertEquals(f2.hasMinterm(index), false);
	    assertEquals(f2.hasMaxterm(index), true);
	    assertEquals(f2.hasDontCare(index), false);
	}
    }
    
    /*test uzima dont care iterator funkcije f2 i prolazi svim dont careima i provjerava jesu
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f2dontCareIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f2.dontCareIterable()) {
	    assertEquals(f2.hasMinterm(index), false);
	    assertEquals(f2.hasMaxterm(index), false);
	    assertEquals(f2.hasDontCare(index), true);
	}
    }
    
    @Test
    public void f2getValueForCombination0x0Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.DONT_CARE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f2.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void f2getValueForCombination00xTest() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.FALSE);
	C.setValue(BooleanValue.DONT_CARE);
	assertEquals(f2.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void f2getValueForCombination010Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.TRUE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f2.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void FunctionCreationWithIndexOutOfDomainBoundsTest() {
	new MaskBasedBF("f3", Arrays.asList(A), true, Masks.fromIndexes(2, 1), 
		new ArrayList<Mask>());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void FunctionCreationWithTooSmallIndexTest() {
	new MaskBasedBF("f3", Arrays.asList(A, B), true, Masks.fromIndexes(1, 1, 0), 
		Masks.fromIndexes(2, 0, 5));
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void FunctionCreationWithDontCareOutOfDomainBoundsTest() {
	new MaskBasedBF("f3", Arrays.asList(A, B), true, Masks.fromIndexes(2, 1, 3, 2), 
		Masks.fromIndexes(2, 0, 8));
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void FunctionCreationWithIndexesAndDontCaresInCommonTest() {
	new MaskBasedBF("f3", Arrays.asList(A), true, Masks.fromIndexes(1, 1, 3, 4), 
		Masks.fromIndexes(1, 0, 2, 4));
    }
    
    /*test uzima minterm iterator funkcije f3 i prolazi svim mintermima i provjerava jesu 
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f3mintermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f3.mintermIterable()) {
	    assertEquals(f3.hasMinterm(index), true);
	    assertEquals(f3.hasMaxterm(index), false);
	    assertEquals(f3.hasDontCare(index), false);
	}
    }
    
    /*test uzima minterm iterator funkcije f3 i prolazi svim makstermima i provjerava jesu 
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f3maxtermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f3.maxtermIterable()) {
	    assertEquals(f3.hasMinterm(index), false);
	    assertEquals(f3.hasMaxterm(index), true);
	    assertEquals(f3.hasDontCare(index), false);
	}
    }
    
    @Test
    public void f3getValueForCombination0x0Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.DONT_CARE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f3.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test
    public void f3getValueForCombination00xTest() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.FALSE);
	C.setValue(BooleanValue.DONT_CARE);
	assertEquals(f3.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void f3getValueForCombination010Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.TRUE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f3.getValue() == BooleanValue.TRUE, true);
    }
    

}
