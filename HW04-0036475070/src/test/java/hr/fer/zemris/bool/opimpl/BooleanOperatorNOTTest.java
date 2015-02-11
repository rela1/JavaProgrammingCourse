package hr.fer.zemris.bool.opimpl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.bool.BooleanConstant;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanValue;
import hr.fer.zemris.bool.BooleanVariable;
import org.junit.Test;

public class BooleanOperatorNOTTest {
    
    @Test
    public void NotOfTrueTest() {
	BooleanOperator op = new BooleanOperatorNOT(BooleanConstant.TRUE);
	assertEquals(op.getValue() == BooleanValue.FALSE, true);
    }
    
    @Test
    public void NotOfFalseTest() {
	BooleanOperator op = new BooleanOperatorNOT(new BooleanVariable("D"));
	assertEquals(op.getValue() == BooleanValue.TRUE, true);
    }
    
    @Test
    public void NotOfDontCareTest() {
	BooleanVariable A = new BooleanVariable("A");
	A.setValue(BooleanValue.DONT_CARE);
	BooleanOperator op = new BooleanOperatorNOT(A);
	assertEquals(op.getValue() == BooleanValue.DONT_CARE, true);
    }
    
    @Test (expected=IllegalArgumentException.class) 
    public void NotOfNullTest() {
	new BooleanOperatorNOT(null);
    }

}
