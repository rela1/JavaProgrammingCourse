package hr.fer.zemris.bool.opimpl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.bool.BooleanConstant;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;

import java.util.Arrays;

import org.junit.Test;

public class BooleanOperatorORTest {
    
    @Test
    public void OrWithOnlyOnesTest() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.TRUE);
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void OrWithOnesAndZeroesTest() {
	BooleanVariable A = new BooleanVariable("A");
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void OrWithOnesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.TRUE)
	);
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void OrWithOnesZeroesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		BooleanConstant.TRUE,
		A,
		BooleanConstant.FALSE)
	);
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void OrWithZeroesAndDontCare() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		A,
		BooleanConstant.FALSE)
	);
	assertEquals(op.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test
    public void OrWithZeroes() {
	BooleanVariable A = new BooleanVariable("A");
	BooleanOperator op = new BooleanOperatorOR(Arrays.asList(
		BooleanConstant.FALSE,
		A,
		BooleanConstant.FALSE)
	);
	assertEquals(op.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test (expected = IllegalArgumentException.class) 
    public void OrWithNullArgument() {
	new BooleanOperatorOR(null);
    }
    
    @Test (expected = IllegalArgumentException.class) 
    public void OrOverNullValue() {
	new BooleanOperatorOR(Arrays.asList(
		new BooleanVariable("A"),
		BooleanConstant.FALSE,
		null
		)
	);
    }

}
