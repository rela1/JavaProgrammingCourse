package hr.fer.zemris.bool.opimpl;

import static org.junit.Assert.*;

import java.util.Arrays;

import hr.fer.zemris.bool.BooleanConstant;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;

import org.junit.Test;

public class BooleanOperatorANDTest {
    
    @Test
    public void AndWithOnlyOnesTest() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.TRUE);
	BooleanOperator op = new BooleanOperatorAND(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void AndWithOnesAndZeroesTest() {
	BooleanVariable A = new BooleanVariable("A");
	BooleanOperator op = new BooleanOperatorAND(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void AndWithOnesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorAND(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test
    public void AndWithOnesZeroesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorAND(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.FALSE)
	);
	assertEquals(op.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void AndWithZeroesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorAND(Arrays.asList(
		A,
		BooleanConstant.FALSE)
	);
	assertEquals(op.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test (expected = IllegalArgumentException.class) 
    public void AndWithNullArgument() {
	new BooleanOperatorAND(null);
    }
    
    @Test (expected = IllegalArgumentException.class) 
    public void AndOverNullValue() {
	new BooleanOperatorAND(Arrays.asList(
		new BooleanVariable("A"),
		BooleanConstant.FALSE,
		null
		)
	);
    }

}
