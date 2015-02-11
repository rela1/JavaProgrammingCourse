package hr.fer.zemris.bool;

import static org.junit.Assert.*;
import org.junit.Test;

public class BooleanConstantTest {
    
    @Test
    public void ConstantTrueTest() {
	assertEquals(BooleanConstant.TRUE.getValue() == BooleanValue.TRUE, true);
    }

    @Test
    public void ConstantFalseTest() {
	assertEquals(BooleanConstant.FALSE.getValue() == BooleanValue.FALSE, true);
    }

}
