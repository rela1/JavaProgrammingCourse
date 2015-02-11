package hr.fer.zemris.bool;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class MasksTest {
    
    @Test
    public void MasksFromIndexesTest() {
	List<Mask> maske = Masks.fromIndexes(3, 1, 5, 4);
	assertEquals(maske.contains(Mask.fromIndex(3, 1)), true);
	assertEquals(maske.contains(Mask.fromIndex(3, 4)), true);
	assertEquals(maske.contains(Mask.fromIndex(3, 5)), true);
    }
    
    @Test
    public void MasksFromStringsTest() {
	List<Mask> maske = Masks.fromStrings("01x0", "0101", "0000");
	assertEquals(maske.contains(Mask.parse("0101")), true);
	assertEquals(maske.contains(Mask.parse("01x0")), true);
	assertEquals(maske.contains(Mask.parse("0000")), true);
    }

}
