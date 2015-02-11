package hr.fer.zemris.bool.qmc;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.Masks;
import hr.fer.zemris.bool.fimpl.IndexedBF;
import hr.fer.zemris.bool.fimpl.MaskBasedBF;
import hr.fer.zemris.bool.fimpl.OperatorTreeBF;
import hr.fer.zemris.bool.opimpl.BooleanOperators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class QMCMinimizerTest {

	@Test
	public void MaskBasedFunctionMinimizingTest() {
		MaskBasedBF function = new MaskBasedBF("f", Arrays.asList(
				new BooleanVariable("A"), new BooleanVariable("B"),
				new BooleanVariable("C"), new BooleanVariable("D"),
				new BooleanVariable("E")), true, Masks.fromStrings(false,
				"01xxx"), Masks.fromStrings(true, "10xxx"));
		MaskBasedBF[] minimizedFunctions = QMCMinimizer.minimize(function,
				false);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (int j = 0; j < 32; j++) {
				assertEquals(
						"Rezultat provjere minterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMinterm(j)
								^ minimizedFunctions[i].hasMinterm(j));
			}
		}
	}

	@Test
	public void IndexBasedFunctionMinimizingTest() {
		IndexedBF function = new IndexedBF("f", Arrays.asList(
				new BooleanVariable("A"), new BooleanVariable("B"),
				new BooleanVariable("C"), new BooleanVariable("D"),
				new BooleanVariable("E")), true, Arrays.asList(0, 2, 5, 6, 7,
				11, 12, 15, 18, 22, 28, 30, 31), Arrays.asList(1, 3, 13, 14));
		MaskBasedBF[] minimizedFunctions = QMCMinimizer.minimize(function,
				false);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (Integer index : function.mintermIterable()) {
				assertEquals(
						"Rezultat provjere minterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						true,
						minimizedFunctions[i].hasMinterm(index.intValue()));
			}
		}
	}

	@Test
	public void OperatorTreeFunctionMinimizingTest() {
		BooleanVariable A = new BooleanVariable("A");
		BooleanVariable B = new BooleanVariable("B");
		BooleanVariable C = new BooleanVariable("C");
		BooleanVariable D = new BooleanVariable("D");
		BooleanVariable E = new BooleanVariable("E");
		BooleanOperator operatorTree = BooleanOperators.or(BooleanOperators
				.and(BooleanOperators.not(A), B), BooleanOperators.and(
				BooleanOperators.not(B), A), BooleanOperators.and(C, D,
				BooleanOperators.not(E), BooleanOperators.or(A, C, E)));
		OperatorTreeBF function = new OperatorTreeBF("f", Arrays.asList(A, B,
				C, D, E), operatorTree);
		MaskBasedBF[] minimizedFunctions = QMCMinimizer.minimize(function,
				false);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (int j = 0; j < 32; j++) {
				assertEquals(
						"Rezultat provjere minterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMinterm(j)
								^ minimizedFunctions[i].hasMinterm(j));
				assertEquals(
						"Rezultat provjere maxterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMaxterm(j)
								^ minimizedFunctions[i].hasMaxterm(j));
				assertEquals(
						"Rezultat provjere dont care indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasDontCare(j)
								^ minimizedFunctions[i].hasDontCare(j));
			}
		}
	}

	@Test
	public void MaskBasedFunctionMinimizingMaxtermsTest() {
		MaskBasedBF function = new MaskBasedBF("f", Arrays.asList(
				new BooleanVariable("A"), new BooleanVariable("B"),
				new BooleanVariable("C"), new BooleanVariable("D"),
				new BooleanVariable("E")), false, Masks.fromStrings(false,
				"01xxx"), Masks.fromStrings(true, "10xxx"));
		MaskBasedBF[] minimizedFunctions = QMCMinimizer
				.minimize(function, true);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (int j = 0; j < 32; j++) {
				assertEquals(
						"Rezultat provjere maxterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMaxterm(j)
								^ minimizedFunctions[i].hasMaxterm(j));
			}
		}
	}

	@Test
	public void IndexBasedFunctionMinimizingMaxtermsTest() {
		IndexedBF function = new IndexedBF("f", Arrays.asList(
				new BooleanVariable("A"), new BooleanVariable("B"),
				new BooleanVariable("C"), new BooleanVariable("D"),
				new BooleanVariable("E")), false, Arrays.asList(0, 2, 5, 6, 7,
				11, 12, 15, 18, 22, 28, 30, 31), Arrays.asList(1, 3, 13, 14));
		MaskBasedBF[] minimizedFunctions = QMCMinimizer
				.minimize(function, true);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (Integer index : function.maxtermIterable()) {
				assertEquals(
						"Rezultat provjere maxterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						true,
						minimizedFunctions[i].hasMaxterm(index.intValue()));
			}
		}
	}

	@Test
	public void OperatorTreeFunctionMinimizingMaxtermsTest() {
		BooleanVariable A = new BooleanVariable("A");
		BooleanVariable B = new BooleanVariable("B");
		BooleanVariable C = new BooleanVariable("C");
		BooleanVariable D = new BooleanVariable("D");
		BooleanVariable E = new BooleanVariable("E");
		BooleanOperator operatorTree = BooleanOperators.or(BooleanOperators
				.and(BooleanOperators.not(A), B), BooleanOperators.and(
				BooleanOperators.not(B), A), BooleanOperators.and(C, D,
				BooleanOperators.not(E), BooleanOperators.or(A, C, E)));
		OperatorTreeBF function = new OperatorTreeBF("f", Arrays.asList(A, B,
				C, D, E), operatorTree);
		MaskBasedBF[] minimizedFunctions = QMCMinimizer
				.minimize(function, true);
		for (int i = 0, length = minimizedFunctions.length; i < length; i++) {
			for (int j = 0; j < 32; j++) {
				assertEquals(
						"Rezultat provjere minterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMinterm(j)
								^ minimizedFunctions[i].hasMinterm(j));
				assertEquals(
						"Rezultat provjere maxterma indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasMaxterm(j)
								^ minimizedFunctions[i].hasMaxterm(j));
				assertEquals(
						"Rezultat provjere dont care indexa \"j\" mora biti isti i za funkciju i za pojedinacnu minimiziranu funkciju!",
						false,
						function.hasDontCare(j)
								^ minimizedFunctions[i].hasDontCare(j));
			}
		}
	}

	@Test
	public void DoubleMinimizationByLogicalDeviceInput() {
		IndexedBF function = new IndexedBF("f", Arrays.asList(
				new BooleanVariable("A"), new BooleanVariable("B"),
				new BooleanVariable("C"), new BooleanVariable("D")), true,
				Arrays.asList(4, 5, 6, 7, 8, 9, 11),
				Arrays.asList(2, 3, 12, 15));
		MaskBasedBF[] minimizedFunction = QMCMinimizer
				.minimize(function, false);
		assertEquals("Treba postojati samo jedan minimalni oblik!", true,
				minimizedFunction.length == 1);
		Set<Mask> expectedMasks = new HashSet<Mask>(Masks.fromStrings(false,
				"01xx", "100x", "xx11"));
		assertEquals(
				"Maske dobivene u minimalnoj funkciji trebaju biti iste onima koje se ocekuju!",
				true, expectedMasks.equals(new HashSet<Mask>(
						minimizedFunction[0].getMasks())));
	}
}
