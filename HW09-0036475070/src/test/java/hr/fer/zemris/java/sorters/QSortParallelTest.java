package hr.fer.zemris.java.sorters;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class QSortParallelTest {

	@Test
	public void SortTest() {
		final int SIZE = 150_000;
		Random rand = new Random();
		int[] data = new int[SIZE];
		for (int i = 0; i <= 50; i++) {
			for (int j = 0; j < data.length; j++) {
				data[j] = rand.nextInt();
			}
			QSortParallel.sort(data);
			assertEquals("Polje bi trebalo biti sortirano!", true,
					QSortParallel.isSorted(data));
		}
	}
}
