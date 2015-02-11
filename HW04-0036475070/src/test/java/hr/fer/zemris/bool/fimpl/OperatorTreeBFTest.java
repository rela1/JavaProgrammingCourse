package hr.fer.zemris.bool.fimpl;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.opimpl.BooleanOperators;

import java.util.Arrays;

public class OperatorTreeBFTest {


    //definicija dvije funkcije, testovi slijede
    private BooleanVariable A = new BooleanVariable("A");
    private BooleanVariable B = new BooleanVariable("B");
    private BooleanVariable C = new BooleanVariable("C");
    
    private BooleanOperator izraz1 = BooleanOperators.or(
	    BooleanOperators.and(A, B, C),
	    C
	    );
    
    private BooleanOperator izraz2 = BooleanOperators.or(
	    BooleanOperators.and(BooleanOperators.not(A), B),
	    BooleanOperators.and(BooleanOperators.not(B), A)
	    );
    
    private BooleanFunction f1 = new OperatorTreeBF(
	    "f1",
	    Arrays.asList(A, B, C),
	    izraz1
	    );
    
    private BooleanFunction f2 = new OperatorTreeBF(
	    "f2",
	    Arrays.asList(A, B, C),
	    izraz2
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
    
    /*test uzima maxterm iterator funkcije f1 i prolazi svim maxtermima i provjerava jesu
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
    
    /*test uzima maxterm iterator funkcije f2 i prolazi svim maxtermima i provjerava jesu
    li rezultati hasMinterm, hasMaxterm, hasDontCare u skladu s ocekivanjem */
    @Test
    public void f2maxtermIteratorAndHasMintermMaxtermDontCareTest() {
	for(Integer index : f2.maxtermIterable()) {
	    assertEquals(f2.hasMinterm(index), false);
	    assertEquals(f2.hasMaxterm(index), true);
	    assertEquals(f2.hasDontCare(index), false);
	}
    }
    
    @Test
    public void f2getValueForCombination0x0Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.DONT_CARE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f2.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test
    public void f2getValueForCombination00xTest() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.FALSE);
	C.setValue(BooleanValue.DONT_CARE);
	assertEquals(f2.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void f2getValueForCombination010Test() {
	A.setValue(BooleanValue.FALSE);
	B.setValue(BooleanValue.TRUE);
	C.setValue(BooleanValue.FALSE);
	assertEquals(f2.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void CreateFunctionWithVariableInOperatorAndNotInDomainTest() {
	new OperatorTreeBF("f3", Arrays.asList(A, B), izraz1);
    }
	    
}
