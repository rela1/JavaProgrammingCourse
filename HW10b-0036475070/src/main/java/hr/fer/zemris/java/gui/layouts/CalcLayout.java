package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementacija Layouta koji sadrži fiksni broj redaka i stupaca. Layout
 * sadrži 5 redaka i 7 stupaca, s tim da se prvi element Layouta u prvom retku
 * provlači kroz 5 stupaca. Sve ostale ćelije su dimenzija 1x1.
 * 
 * @author Ivan Relić
 * 
 */
public class CalcLayout implements LayoutManager2 {

	/** Označava koliki je razmak među retcima/stupcima objekata layouta. */
	private int rowColumnSpace;

	/** Mapa s objektima pridruženima određenim pozicijama layouta. */
	private Map<Component, RCPosition> componentMap;

	/** broj redaka layouta */
	private static final int ROWS = 5;

	/** broj stupaca layouta */
	private static final int COLS = 7;

	/** broj stupaca koje zauzima prva ćelija */
	private static final int FIRST_CELL = 5;

	/**
	 * Konstruktor. Prima int vrijednost koja predstavlja razmak između redaka i
	 * stupaca layouta.
	 * 
	 * @param rowColumnSpace
	 */
	public CalcLayout(int rowColumnSpace) {
		this.rowColumnSpace = rowColumnSpace;
		this.componentMap = new HashMap<Component, RCPosition>();
	}

	/**
	 * Konstruktor. Postavlja razmak između redaka i stupaca na 0.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Metoda ne radi ništa jer ovaj layout zahtijeva argument constraint za
	 * stavljanje u container.
	 */
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("This method is not supported!");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		componentMap.remove(comp);
	}

	/**
	 * Metoda vraca preferirano dimenzioniranje containera s obzirom na layout,
	 * tj. postavlja takve dimenzije layouta da se veličine svih ćelija postave
	 * na preferiranu veličinu najveće ćelije.
	 * 
	 * @return dimenzije preferirane veličine containera
	 */
	public Dimension preferredLayoutSize(Container parent) {
		Dimension largestComponentSize = getLargestComponentSize(parent, false);
		Insets insets = parent.getInsets();
		largestComponentSize = getFullDimensions(largestComponentSize, insets);
		return largestComponentSize;
	}

	/**
	 * Metoda vraca preferirano dimenzioniranje containera s obzirom na layout,
	 * tj. postavlja takve dimenzije layouta da se veličine svih ćelija postave
	 * na minimalnu veličinu najveće ćelije.
	 * 
	 * @return dimenzije minimalne veličine containera
	 */
	public Dimension minimumLayoutSize(Container parent) {
		Dimension largestComponentSize = getLargestComponentSize(parent, true);
		Insets insets = parent.getInsets();
		largestComponentSize = getFullDimensions(largestComponentSize, insets);
		return largestComponentSize;
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();

			// iz ukupne dimenzije parent containera izračunaj veličinu ćelije
			Dimension containerSize = parent.getSize();
			int cellHeight = (containerSize.height - insets.top - insets.bottom - (ROWS + 1)
					* rowColumnSpace)
					/ ROWS;
			int cellWidth = (containerSize.width - insets.left - insets.right - (COLS + 1)
					* rowColumnSpace)
					/ COLS;

			// prodji kroz sve komponente koje su trenutno u containeru i svakoj
			// postavi poziciju i veličinu
			Component[] components = parent.getComponents();
			for (int i = 0, length = components.length; i < length; i++) {
				RCPosition position = componentMap.get(components[i]);
				if (position != null) {
					// pozicija po x-osi je umnožak pozicije umanjene za 1 sa
					// širinom ćelije zbrojena s umnoškom pozicije sa razmakom
					// među ćelijama i lijevim insetsom
					int x = insets.left + cellWidth
							* (position.getColumn() - 1) + rowColumnSpace
							* position.getColumn();
					// pozicija po y-osi je umnožak pozicije umanjene za 1 sa
					// visinom ćelije zbrojena s umnoškom pozicije sa razmakom
					// među ćelijama i gornjim insetsom
					int y = insets.top + cellHeight * (position.getRow() - 1)
							+ rowColumnSpace * position.getRow();
					// ako je pozicija ćelije (1,1), šrina te ćelije se određuje
					// posebno
					if (position.equals(new RCPosition(1, 1))) {
						int width = FIRST_CELL * cellWidth + (FIRST_CELL - 1)
								* rowColumnSpace;
						components[i].setBounds(x, y, width, cellHeight);
						// za sve ostale ćelije je širina označena jednoznačno
					} else {
						components[i].setBounds(x, y, cellWidth, cellHeight);
					}
				}
			}
		}
	}

	/**
	 * Dodaje predanu komponentu u layout container, ali uz predani constraint,
	 * tj. u ovom slučaju na predanu poziciju layouta.
	 * 
	 * @param comp
	 *            komponenta za dodati u layout
	 * @param constraints
	 *            gdje se komponenta dodaje
	 */
	public void addLayoutComponent(Component comp, Object constraints) {
		// ako je constraint predan kao string pokušaj ga parsirati u RCPosition
		if (constraints instanceof String) {
			constraints = RCPosition.parse((String) constraints);
		}
		// ako je constraint čisti RCPosition objekt ili je uspješno parsiran iz
		// stringa obradi ga
		if (constraints instanceof RCPosition) {
			checkPosition((RCPosition) constraints);
		} else {
			throw new IllegalArgumentException(
					"Unknown object for constraints! It should either be string in format: \"intNumber, intNumber\" or RCPosition object!");
		}
		componentMap.put(comp, (RCPosition) constraints);
	}

	/**
	 * Vraca maksimalne dimenzije layouta uz komponente koje trenutno sadrži.
	 */
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return (float) 0.5;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return (float) 0.5;
	}

	@Override
	public void invalidateLayout(Container target) {
		// metoda se ne koristi
	}

	/**
	 * Primljenoj maksimalnoj veličini ćelije nadodaje sve što treba za veličinu
	 * cijelog layout containera.
	 * 
	 * @param largestComponentSize
	 *            maksimalna veličina ćelije
	 * @param insets
	 *            insetsi parent containera
	 * @return novokreirana dimenzija
	 */
	private Dimension getFullDimensions(Dimension largestComponentSize,
			Insets insets) {
		largestComponentSize.height = (largestComponentSize.height * ROWS)
				+ ((ROWS + 1) * rowColumnSpace) + insets.top + insets.bottom;
		largestComponentSize.width = (largestComponentSize.width * COLS)
				+ ((COLS + 1) * rowColumnSpace) + insets.left + insets.right;
		return largestComponentSize;
	}

	/**
	 * Prolazi kroz sve komponente containera i traži najveće dimenzije pojedine
	 * komponente uzimajući u obzir traži li se minimalna ili preferirana
	 * veličina.
	 * 
	 * @param parent
	 *            container s komponentama
	 * @param isMinimumSize
	 *            traži li se minimalna ili preferirana veličina - true za
	 *            minimalnu, false za preferiranu
	 * @return najveća dimenzija ćelije s obzirom na komponente
	 */
	private Dimension getLargestComponentSize(Container parent,
			boolean isMinimumSize) {
		Dimension largestSize = new Dimension(0, 0);
		Component[] components = parent.getComponents();
		for (int i = 0, length = components.length; i < length; i++) {
			Dimension currentComponentDimension = (isMinimumSize ? components[i]
					.getMinimumSize() : components[i].getPreferredSize());
			if (currentComponentDimension != null) {
				// ako je to komponenta na poziciji (1,1)
				if (componentMap.get(components[i])
						.equals(new RCPosition(1, 1))) {
					// širina prve ćelije za usporedbu je njena širina umanjena
					// za vrijednost koliko stupaca ona zauzima minus jedan
					// pomnožena sa širinom razmaka među stupcima, i na kraju
					// podijeljena sa brojem stupaca koje zauzima
					int firstCellWidth = ((currentComponentDimension.width - (FIRST_CELL - 1)
							* rowColumnSpace) / FIRST_CELL);
					// uzmi veće vrijednosti od visine i širine trenutne
					// komponente i trenutne maksimalne
					largestSize.width = Math.max(largestSize.width,
							firstCellWidth);
				} else {
					largestSize.width = Math.max(largestSize.width,
							currentComponentDimension.width);
				}
				largestSize.height = Math.max(currentComponentDimension.height,
						largestSize.height);
			}
		}
		return largestSize;
	}

	/**
	 * Provjerava je li primljeni RCPosition valjana pozicija za CalcLayout.
	 * 
	 * @param constraints
	 *            RCPosition objekt
	 */
	private void checkPosition(RCPosition constraints) {
		int row = constraints.getRow();
		int column = constraints.getColumn();
		if (row < 1 || row > ROWS) {
			throw new IllegalArgumentException(
					"Row position should be in interval: [1,5]!");
		}
		if (column < 1 || column > COLS) {
			throw new IllegalArgumentException(
					"Row position should be in interval: [1,7]!");
		}
		if (row == 1 && (column > 1 && column < (FIRST_CELL + 1))) {
			throw new IllegalArgumentException(
					"If row position is 1, column position should be 1 or in interval: [6,7]!");
		}
		if (componentMap.containsValue(constraints)) {
			throw new IllegalArgumentException(
					"Only one component per constraint is possible!");
		}
	}

}
