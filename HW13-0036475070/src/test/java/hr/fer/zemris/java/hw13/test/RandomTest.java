package hr.fer.zemris.java.hw13.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomTest {

	@Test
	public void StringReplaceAllTest() {
		String testString = "test";
		testString = testString.replace("t", "T");
		assertEquals("Testni string mora biti jednak TesT!", "TesT", testString);
	}

}
