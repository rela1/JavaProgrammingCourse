package hr.fer.zemris.bool.qmc;

import hr.fer.zemris.bool.BooleanFunction;
import hr.fer.zemris.bool.BooleanOperator;
import hr.fer.zemris.bool.BooleanSource;
import hr.fer.zemris.bool.BooleanVariable;
import hr.fer.zemris.bool.Mask;
import hr.fer.zemris.bool.MaskValue;
import hr.fer.zemris.bool.fimpl.IndexedBF;
import hr.fer.zemris.bool.fimpl.MaskBasedBF;
import hr.fer.zemris.bool.fimpl.OperatorTreeBF;
import hr.fer.zemris.bool.opimpl.BooleanOperatorAND;
import hr.fer.zemris.bool.opimpl.BooleanOperatorOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Razred za minimizaciju boolean funkcija Quine-McCluskey metodom.
 * 
 * @author Ivan Relic
 * 
 */
public class QMCMinimizer {

	private static Set<Mask> masksUsedInCombining;
	private static Map<Integer, Set<Mask>> maskMap;

	/**
	 * Prima boolean funkciju i zastavicu hocemo li minimizirati po mintermima
	 * ili makstermima te funkcije. Vraca array svih minimalnih oblika
	 * minimizirane funkcije u MaskBasedBF obliku. Minimalni oblici se određuju
	 * Pyne-McCluskey metodom i nad minimalnim oblicima se vrši dodatna
	 * minimizacija tako da su krajnji minimalni oblici koji se dobiju oni s
	 * najmanjim utroškom logičkih sklopova (oni koji imaju najmanje ulaza).
	 * 
	 * @param function
	 *            funkcija koja se minimizira
	 * @param wantProducts
	 *            zelimo li maksterme ili minterme
	 * @return
	 */
	public static MaskBasedBF[] minimize(BooleanFunction function,
			boolean wantProducts) {

		if (function == null) {
			throw new IllegalArgumentException("Function should not be null!");
		}

		List<Mask> masks = createMasks(function, wantProducts);
		masksUsedInCombining = new HashSet<Mask>();
		masks = createImplicants(masks);
		List<Mask> essentialImplicants = createEssentialImplicants(masks,
				function);
		// ako bitni primarni implikanti pokrivaju sve potrebno, vrati niz
		// funkcija koji se sastoji od samo jedne MaskBasedBF
		if (maskMap.size() == 0) {
			return new MaskBasedBF[] { new MaskBasedBF("f",
					createDomain(function.getDomain().size()), !wantProducts,
					essentialImplicants, new ArrayList<Mask>()) };
		}

		// inace kreiraj niz MaskBased funkcija iz niza listi maski minimalnih
		// oblika funkcija
		List<Mask>[] minimalMasks = createMinimalMasks(essentialImplicants);
		MaskBasedBF[] minimalFunctions = new MaskBasedBF[minimalMasks.length];
		for (int i = 0, length = minimalMasks.length; i < length; i++) {
			minimalFunctions[i] = new MaskBasedBF("f" + String.valueOf(i),
					createDomain(function.getDomain().size()), !wantProducts,
					minimalMasks[i], new ArrayList<Mask>());
		}
		return minimalFunctions;
	}

	/**
	 * Kreira listu varijabli za instanciranje nove funkcije na temelju veličine
	 * maske bitnih primarnih implikanata.
	 * 
	 * @param maskSize
	 *            velicina maski
	 * @return lista varijabli
	 */
	private static List<BooleanVariable> createDomain(int maskSize) {
		BooleanVariable[] variables = new BooleanVariable[maskSize];
		for (int i = 0; i < maskSize; i++) {
			variables[i] = new BooleanVariable("x" + String.valueOf(i));
		}
		return Arrays.asList(variables);
	}

	/**
	 * Kreira listu minimalnih maski iz kojih se direktno moze instancirati
	 * array gotovih minimiziranih boolean funkcija.
	 * 
	 * @param essentialImplicants
	 *            lista bitnih primarnih implikanata
	 * @return lista minimiziranih masaka, za sve moguce minimalne oblike
	 */
	private static List<Mask>[] createMinimalMasks(
			List<Mask> essentialImplicants) {

		// dohvati varijable koji ce ti trebati (gledaj samo jedinstvene
		// maske - zato set)
		Set<Mask> variables = new HashSet<Mask>();
		for (Entry<Integer, Set<Mask>> entry : maskMap.entrySet()) {
			variables.addAll(entry.getValue());
		}

		// kreiraj i popuni mapu koja povezuje varijablu s maskom (za funkciju
		// pokrivenosti)
		Map<Mask, BooleanVariable> pyneVariables = new HashMap<Mask, BooleanVariable>();
		int iterator = 0;
		for (Mask maska : variables) {
			pyneVariables.put(maska, new BooleanVariable("P" + iterator));
			iterator++;
		}

		BooleanOperator functionTree = createOperatorTree(pyneVariables);
		// kreiraj OperatorTreeBF iz kreiranih operatora i listi
		OperatorTreeBF function = new OperatorTreeBF("f",
				new ArrayList<BooleanVariable>(pyneVariables.values()),
				functionTree);
		// dohvati minterme kreirane funkcije
		List<Integer> pyneMinterms = function.getMintermIndex();
		// prodji kroz minterme i pronadji one koji su sastavljeni od minimalnog
		// broja jedinica
		// i izbroji koliko ima mintermi s istim brojem jedinica
		int maskSize = variables.size();
		int minimalMinterm = maskSize;
		int numberOfMinimalMinterms = 0;
		for (Integer index : pyneMinterms) {
			Mask maska = Mask.fromIndex(false, maskSize, index.intValue());
			int numberOfOnes = maska.getNumberOfOnes();
			if (numberOfOnes < minimalMinterm) {
				minimalMinterm = numberOfOnes;
				numberOfMinimalMinterms = 0;
			}
			if (numberOfOnes == minimalMinterm) {
				numberOfMinimalMinterms++;
			}
		}

		// dodaj sve minimalne oblike u niz
		@SuppressWarnings("unchecked")
		ArrayList<Mask>[] minimalMasks = (ArrayList<Mask>[]) new ArrayList<?>[numberOfMinimalMinterms];
		for (int i = 0, length = minimalMasks.length; i < length; i++) {
			minimalMasks[i] = new ArrayList<Mask>(essentialImplicants);
		}
		iterator = 0;
		for (Integer index : pyneMinterms) {
			if (Mask.fromIndex(false, maskSize, index).getNumberOfOnes() == minimalMinterm) {
				minimalMasks[iterator].addAll(getMasksFromPyne(index,
						pyneVariables, function, maskSize));
				iterator++;
			}
		}

		// provjeri moze li se jos minimizirati po broju ulaza logičkih sklopova
		ArrayList<Mask>[] newMinimalMasks = minimizeByLogicalInput(minimalMasks);
		return newMinimalMasks;
	}

	/**
	 * Prima niz minimalnih oblika funkcije i dodatno minimizira po broju
	 * logickih sklopova potrebnih za ostvarenje funkcije.
	 * 
	 * @param minimalMasks
	 *            niz maski
	 * @return niz lista maski s potpuno minimiziranim oblicima funkcija
	 */
	private static ArrayList<Mask>[] minimizeByLogicalInput(
			ArrayList<Mask>[] minimalMasks) {
		// pronadji funkcijsko ostvarenje s minimalnim brojem potrebnih sklopova
		// (funkcija s najmanjim brojem 0 i 1 u maskama treba najmanje sklopova)
		int minimalNumber = minimalMasks[0].get(0).getSize()
				* minimalMasks[0].size();
		int numberOfMinimalFunctions = 0;
		for (int i = 0, length = minimalMasks.length; i < length; i++) {
			int currentNumber = 0;
			for (Mask maska : minimalMasks[i]) {
				currentNumber += maska.getNumberOfOnes()
						+ maska.getNumberOfZeroes();
			}
			if (currentNumber < minimalNumber) {
				minimalNumber = currentNumber;
				numberOfMinimalFunctions = 0;
			}
			if (currentNumber == minimalNumber) {
				numberOfMinimalFunctions++;
			}
		}
		// ako sve primljene minimalne maske imaju isti utrosak logickih
		// sklopova, vrati njih same
		if (numberOfMinimalFunctions == minimalMasks.length) {
			return minimalMasks;
		}
		// u novi niz minimalnih oblika stavi samo one koje zadovoljavaju
		// minimalan broj
		@SuppressWarnings("unchecked")
		ArrayList<Mask>[] minimized = (ArrayList<Mask>[]) new ArrayList<?>[numberOfMinimalFunctions];
		for (int i = 0, j = 0, length = minimalMasks.length; i < length; i++) {
			int currentNumber = 0;
			for (Mask maska : minimalMasks[i]) {
				currentNumber += maska.getNumberOfOnes()
						+ maska.getNumberOfZeroes();
			}
			if (currentNumber == minimalNumber) {
				minimized[j] = new ArrayList<Mask>(minimalMasks[i]);
				j++;
			}
		}
		return minimized;
	}

	/**
	 * Prima indeks minterma i mapu s Pyne varijablama povezanim s maskama i
	 * vraca listu maski koje sacinjavaju taj minterm.
	 * 
	 * @param index
	 *            indes minterma
	 * @param pyneVariables
	 *            mapa s Pyne varijablama
	 * @param maskSize
	 *            velicina maski
	 * @return lista maski koje sacinjavaju minterm
	 */
	private static List<Mask> getMasksFromPyne(Integer index,
			Map<Mask, BooleanVariable> pyneVariables, OperatorTreeBF function,
			int maskSize) {

		// pretvori index u masku i iteriraj redom i za svaku jedinicu u maski
		// dodaj masku koja je pridružena toj varijabli
		Mask maska = Mask.fromIndex(false, maskSize, index.intValue());
		List<Mask> listaMaski = new ArrayList<Mask>();
		for (int i = 0; i < maskSize; i++) {
			if (maska.getValue(i) == MaskValue.ONE) {
				BooleanVariable variable = function.getDomain().get(i);
				for (Entry<Mask, BooleanVariable> entry : pyneVariables
						.entrySet()) {
					if (entry.getValue().equals(variable)) {
						listaMaski.add(entry.getKey());
					}
				}
			}
		}
		return listaMaski;
	}

	/**
	 * Prima mapu varijabla i maski za Pyne-McCluskeyev postupak i kreira
	 * operator tree za funkciju pokrivenosti.
	 * 
	 * @param pyneVariables
	 *            mapa varijabli i maski
	 * @return operator tree
	 */
	private static BooleanOperator createOperatorTree(
			Map<Mask, BooleanVariable> pyneVariables) {
		// prodji mapom pokrivenosti i za svaki indeks kreiraj boolean operator
		// or sa svim izrazima
		// koji su uz taj indeks i dodaj kreirani operator u listu operatora or
		List<BooleanSource> operators = new ArrayList<BooleanSource>();
		for (Entry<Integer, Set<Mask>> entry : maskMap.entrySet()) {
			// uzmi sve varijable koje su uz taj index
			List<BooleanSource> indexVariables = new ArrayList<BooleanSource>();
			Set<Mask> indexMasks = entry.getValue();
			for (Mask maska : indexMasks) {
				indexVariables.add(pyneVariables.get(maska));
			}
			// u listu operatora dodaj OR nad svim varijablama za trenutni
			// indeks
			operators.add(new BooleanOperatorOR(indexVariables));
		}
		// kreiraj function tree koji se sastoji od AND-a sa svim operatorima OR
		// za svaki index
		BooleanOperator functionTree = new BooleanOperatorAND(operators);
		return functionTree;
	}

	/**
	 * Prima listu maski (primarnih implikanata) i kreira listu s bitnim
	 * primarnim implikantima.
	 * 
	 * @param masks
	 *            lista primarnih
	 * @param function
	 *            funkcija za koju se kreiraju bitni primarni implikanti
	 * @return lista bitnih primarnih implikanata
	 */
	private static List<Mask> createEssentialImplicants(List<Mask> masks,
			BooleanFunction function) {

		// popuni mapu i stavi svakom indeksu da je pokriven s 0 maski (ne
		// uključuj dont care indexe)
		maskMap = new HashMap<Integer, Set<Mask>>();
		int maskSize = masks.get(0).getSize();
		for (int i = 0, length = (int) Math.pow(2, maskSize); i < length; i++) {
			if (!function.hasDontCare(new Integer(i))) {
				maskMap.put(new Integer(i), new HashSet<Mask>());
			}
		}

		// prodji svim zapisima mape i svim maskama iz liste i postavi
		// uz svaki
		// index listu maski koje ga pokrivaju
		for (Entry<Integer, Set<Mask>> entry : maskMap.entrySet()) {
			Mask maska = Mask.fromIndex(false, maskSize, entry.getKey()
					.intValue());
			for (int i = 0, length = masks.size(); i < length; i++) {
				Mask pomMaska = masks.get(i);
				if (pomMaska.isMoreGeneralThan(maska) || pomMaska.equals(maska)) {
					Set<Mask> listaMaski = entry.getValue();
					listaMaski.add(pomMaska);
					entry.setValue(listaMaski);
				}
			}
		}

		// prodji svim zapisima mape i u set bitnih primarnih implikanata stavi
		// sve one maske koje jedine pokrivaju neki index
		Set<Mask> essentialImplicants = new HashSet<Mask>();
		for (Entry<Integer, Set<Mask>> entry : maskMap.entrySet()) {
			Set<Mask> listaMaski = entry.getValue();
			if (listaMaski.size() == 1) {
				essentialImplicants.addAll(listaMaski);
			}
		}

		// prodji svim zapisima mape i ukloni indexe koji su pokriveni bitnim
		// primarnim implikantima
		// ili imaju praznu listu kao vrijednost uz kljuc (ne trebaju biti
		// pokriveni)
		Map<Integer, Set<Mask>> maskMapCopy = new HashMap<Integer, Set<Mask>>(
				maskMap);
		for (Entry<Integer, Set<Mask>> entry : maskMapCopy.entrySet()) {
			Set<Mask> listaMaski = entry.getValue();
			if (listaMaski.size() == 0
					|| !Collections.disjoint(listaMaski, essentialImplicants)) {
				maskMap.remove(entry.getKey());
			}
		}

		return new ArrayList<Mask>(essentialImplicants);
	}

	/**
	 * Prima listu maski dobivenih iz funkcije i kreira listu svih implikanata
	 * bez ponavljanja onih maski koje su vec obuhvacene nekom generalnijom.
	 * 
	 * @param masks
	 *            lista maski
	 * @return lista maski
	 */
	private static List<Mask> createImplicants(List<Mask> masks) {
		// iskombiniraj sve maske, ukloni one koje su korištene u kombiniranju i
		// ukloni sve DONT CARE maske
		masks.addAll(combineAllMasks(new HashSet<Mask>(masks)));
		masks.removeAll(masksUsedInCombining);
		List<Mask> masksCopy = new ArrayList<Mask>(masks);
		for (int i = 0, length = masks.size(); i < length; i++) {
			Mask maska = masks.get(i);
			if (maska.isDontCare()) {
				masksCopy.remove(maska);
			}
		}
		return masksCopy;
	}

	/**
	 * Prima listu maski i vraca listu svih implikanata funkcije (i kombinirane
	 * i pocetne).
	 * 
	 * @param masks
	 *            lista maski
	 * @return lista maski
	 */
	private static Set<Mask> combineAllMasks(Set<Mask> masks) {

		Set<Mask> combinedMasks = new HashSet<Mask>();

		// provjeri za sve moguce parove mozes li ih kombinirati, ako je valjana
		// kombinacija, dodaj je u
		// listu kombiniranih maski
		for (Mask maska1 : masks) {
			for (Mask maska2 : masks) {
				Mask kombiniranaMaska = Mask.combine(maska1, maska2);
				if (kombiniranaMaska != null) {
					combinedMasks.add(kombiniranaMaska);
					masksUsedInCombining.add(maska2);
					masksUsedInCombining.add(maska1);
				}
			}
		}

		// ako je kombinirana vise od 1 maske, rekurzivno dodaj i kombinacije
		// tih maski
		if (combinedMasks.size() > 1) {
			combinedMasks.addAll(combineAllMasks(combinedMasks));
		}
		return combinedMasks;
	}

	/**
	 * Metoda prima BooleanFunction i vraca listu maski koje sacinjavaju
	 * funkciju,
	 * 
	 * @param function
	 *            boolean funkcija
	 * @param wantProducts
	 *            zelimo li minterme ili maksterme iz predane funkcije
	 * @return lista maski koja cini funkciju
	 */
	private static List<Mask> createMasks(BooleanFunction function,
			boolean wantProducts) {
		List<Mask> masks = new ArrayList<Mask>();
		masks.addAll(wantProducts ? returnMaxterms(function)
				: returnMinterms(function));
		masks.addAll(returnDontCares(function));
		return masks;
	}

	/**
	 * Prima boolean funkciju i vraca listu dont care maski.
	 * 
	 * @param function
	 *            funkcija
	 * @return lista maski
	 */
	private static List<Mask> returnDontCares(BooleanFunction function) {
		if (function instanceof MaskBasedBF) {
			return ((MaskBasedBF) function).getDontCareMasks();
		} else if (function instanceof IndexedBF) {
			return getMasks(((IndexedBF) function).getDontCares(), function
					.getDomain().size(), true);
		} else {
			return getMasks(((OperatorTreeBF) function).getDontCareIndex(),
					function.getDomain().size(), true);
		}
	}

	/**
	 * Vraca listu minterma neke funkcije.
	 * 
	 * @param function
	 *            funkcija
	 * @return lista maski
	 */
	private static List<Mask> returnMinterms(BooleanFunction function) {

		// ako je funkcija instanca MaskBased fje, provjeri sto su joj indeksi i
		// vrati zahtijevanu stvar
		if (function instanceof MaskBasedBF) {
			if (((MaskBasedBF) function).areMasksProducts()) {
				return getMasks(((MaskBasedBF) function).getOtherIndexList(),
						function.getDomain().size(), false);
			} else {
				return ((MaskBasedBF) function).getMasks();
			}
		}
		// ako je funkcija instanca Indexed fje, provjeri sto su joj indeksi i
		// vrati zahtijevanu stvar
		if (function instanceof IndexedBF) {
			if (((IndexedBF) function).isIndexesAreMinterms()) {
				return getMasks(((IndexedBF) function).getIndexes(), function
						.getDomain().size(), false);
			} else {
				return getMasks(((IndexedBF) function).getOtherIndexes(),
						function.getDomain().size(), false);
			}
		}
		// ako je instanca OperatorTree funkcije
		else {
			return getMasks(((OperatorTreeBF) function).getMintermIndex(),
					function.getDomain().size(), false);
		}
	}

	/**
	 * Iz liste indeksa kreira listu maski i vraca kreiranu listu.
	 * 
	 * @param otherIndexList
	 *            lista indeksa
	 * @return lista maski
	 */
	private static List<Mask> getMasks(List<Integer> indexList, int maskSize,
			boolean dontCare) {
		List<Mask> masks = new ArrayList<Mask>();
		for (int i = 0, length = indexList.size(); i < length; i++) {
			masks.add(Mask.fromIndex(dontCare, maskSize, indexList.get(i)));
		}
		return masks;
	}

	/**
	 * Vraca listu maksterma neke funkcije.
	 * 
	 * @param function
	 *            funkcija
	 * @return lista maski
	 */
	private static List<Mask> returnMaxterms(BooleanFunction function) {
		// ako je funkcija instanca MaskBased fje, provjeri sto su joj indeksi i
		// vrati zahtijevanu stvar
		if (function instanceof MaskBasedBF) {
			if (!((MaskBasedBF) function).areMasksProducts()) {
				return getMasks(((MaskBasedBF) function).getOtherIndexList(),
						function.getDomain().size(), false);
			} else {
				return ((MaskBasedBF) function).getMasks();
			}
		}
		// ako je funkcija instanca Indexed fje, provjeri sto su joj indeksi i
		// vrati zahtijevanu stvar
		if (function instanceof IndexedBF) {
			if (!((IndexedBF) function).isIndexesAreMinterms()) {
				return getMasks(((IndexedBF) function).getIndexes(), function
						.getDomain().size(), false);
			} else {
				return getMasks(((IndexedBF) function).getOtherIndexes(),
						function.getDomain().size(), false);
			}
		}
		// ako je instanca OperatorTree funkcije
		else {
			return getMasks(((OperatorTreeBF) function).getMaxtermIndex(),
					function.getDomain().size(), false);
		}
	}
}
