package hr.fer.zemris.java.sorters;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QSortParallel2 {

	/**
	 * Prag koji govori koliko elemenata u podpolju minimalno mora biti da bi se
	 * sortiranje nastavilo paralelno; ako elemenata ima manje, algoritam
	 * prelazi na klasično rekurzivno (slijedno) sortiranje.
	 */
	private final static int P_THRESHOLD = 5000;

	/**
	 * Prag za prekid rekurzije. Ako elemenata ima više od ovoga, quicksort
	 * nastavlja rekurzivnu dekompoziciju. U suprotnom ostatak sortira
	 * algoritmom umetanja.
	 */
	private final static int CUT_OFF = 10;

	/**
	 * Sučelje prema klijentu: prima polje i vraća se tek kada je polje
	 * sortirano. Primjenjujući gornje pragove najprije posao paralelizira a
	 * kada posao postane dovoljno mali, rješava ga slijedno.
	 * 
	 * @param array
	 *            polje koje treba sortirati
	 * @return
	 */
	public static void sort(int[] array) {
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(new QSortJob(array, 0, array.length - 1));
		pool.shutdown();
	}

	/**
	 * Model posla sortiranja podpolja čiji su elementi na pozicijama koje su
	 * veće ili jednake <code>startIndex</code> i manje ili jednake
	 * <code>endIndex</code>.
	 */
	private static class QSortJob extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		private int[] array;
		private int startIndex;
		private int endIndex;

		public QSortJob(int[] array, int startIndex, int endIndex) {
			super();
			this.array = array;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		@Override
		public void compute() {
			if (endIndex - startIndex + 1 > CUT_OFF) {
				boolean doInParallel = endIndex - startIndex + 1 > P_THRESHOLD;
				int p = selectPivot();
				swap(array, p, endIndex);
				int pivot = array[endIndex];
				int i = startIndex;
				int j = endIndex - 1;
				while (true) {
					while (i < j && array[i] < pivot) {
						i++;
					}
					while (i < j && array[j] > pivot) {
						j--;
					}
					if (i >= j) {
						break;
					}
					swap(array, i++, j--);
				}
				if (array[i] > pivot) {
					swap(array, i, endIndex);
				}
				// ako je veličina polja veća od granice, pozivom invokeAll
				// rekurzivno dijeli
				if (doInParallel) {
					invokeAll(new QSortJob(array, startIndex, i), new QSortJob(
							array, i + 1, endIndex));
					// inače, direktno pozovi compute nad kreiranim novim
					// poslovima s polovicama polja i obavi posao
				} else {
					new QSortJob(array, startIndex, i).compute();
					new QSortJob(array, i + 1, endIndex).compute();
				}
			} else {
				// Obavi sortiranje umetanjem ako je broj elemenata ispod
				// granice za pokretanje quicksorta
				for (int i = startIndex + 1, length = endIndex; i <= length; i++) {
					for (int j = i; j > startIndex && array[j - 1] > array[j]; j--) {
						swap(array, j, j - 1);
					}
				}
			}
		}

		/**
		 * Odabir pivota metodom medijan-od-tri u dijelu polja
		 * <code>array</code> koje je ograđeno indeksima <code>startIndex</code>
		 * i <code>endIndex</code> (oba uključena).
		 * 
		 * @return vraća indeks na kojem se nalazi odabrani pivot
		 */
		public int selectPivot() {
			int middle = (startIndex + endIndex) / 2;
			if (array[startIndex] > array[middle]) {
				swap(array, startIndex, middle);
			}
			if (array[startIndex] > array[endIndex]) {
				swap(array, startIndex, endIndex);
			}
			if (array[middle] > array[endIndex]) {
				swap(array, middle, endIndex);
			}
			return middle;
		}

		/**
		 * U predanom polju <code>array</code> zamjenjuje elemente na pozicijama
		 * <code>i</code> i <code>j</code>.
		 * 
		 * @param array
		 *            polje u kojem treba zamijeniti elemente
		 * @param i
		 *            prvi indeks
		 * @param j
		 *            drugi indeks
		 */
		public static void swap(int[] array, int i, int j) {
			int pom = array[j];
			array[j] = array[i];
			array[i] = pom;
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li predano polje doista sortirano
	 * uzlazno.
	 * 
	 * @param array
	 *            polje
	 * @return <code>true</code> ako je, <code>false</code> inače
	 */
	public static boolean isSorted(int[] array) {
		for (int i = 0, length = array.length - 1; i < length; i++) {
			if (array[i + 1] < array[i]) {
				return false;
			}
		}
		return true;
	}
}