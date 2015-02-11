package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectMultistackTest {
	
	@Test 
	public void peekFromStackTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper visina = new ValueWrapper(new Integer(257));
		multiStack.push("visina", visina);
		assertEquals("Objekt na vrhu stoga je 257", 257, multiStack.peek("visina").getValue());
	}
	
	@Test 
	public void pushOnStackTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper visina = new ValueWrapper(new Integer(175));
		ValueWrapper visina2 = new ValueWrapper(new Integer(185));
		ValueWrapper visina3 = new ValueWrapper(new Integer(182));
		multiStack.push("visina", visina);
		assertEquals("Objekt na vrhu stoga je 175", 175, multiStack.peek("visina").getValue());
		multiStack.push("visina", visina3);
		assertEquals("Objekt na vrhu stoga je 182", 182, multiStack.peek("visina").getValue());
		multiStack.push("visina", visina2);
		assertEquals("Objekt na vrhu stoga je 185", 185, multiStack.peek("visina").getValue());
	}
	
	@Test 
	public void popFromStackTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper visina = new ValueWrapper(new Integer(175));
		ValueWrapper visina2 = new ValueWrapper(new Integer(185));
		multiStack.push("visina", visina);
		multiStack.push("visina", visina2);
		multiStack.pop("visina");
		assertEquals("Objekt na vrhu stoga je 175", 175, multiStack.peek("visina").getValue());
	}
	
	@Test 
	public void stackIsEmptyTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper visina = new ValueWrapper(new Integer(175));
		multiStack.push("visina", visina);
		multiStack.pop("visina");
		assertEquals("Stog je prazan jer su poppani svi elementi.", 
				multiStack.isEmpty("visina"), true);
	}
	
	public void stackIsEmptyIllegalKeyTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		assertEquals("Stog je prazan jer jos nije ni kreiran.", 
				multiStack.isEmpty("test"), true);
	}
	
	@Test (expected=RuntimeException.class)
	public void popForIllegalKeyTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.pop("test");
	}
	
	@Test (expected=RuntimeException.class)
	public void popForEmptyStackTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("test", new ValueWrapper("proba"));
		multiStack.pop("test");
		multiStack.pop("test");
	}
	
	@Test (expected=RuntimeException.class)
	public void peekForIllegalKeyTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.peek("test");
	}
	
	@Test (expected=RuntimeException.class)
	public void peekForEmptyStackTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("test", new ValueWrapper("proba"));
		multiStack.pop("test");
		multiStack.peek("test");
	}
	
}
