package hr.fer.zemris.java.tecaj.hw5;

import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileDateModifyGetter;
import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileHiddenGetter;
import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileInfoGetter;
import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileNameGetter;
import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileSizeGetter;
import hr.fer.zemris.java.tecaj.hw5.fileinfo.FileTypeGetter;
import hr.fer.zemris.java.tecaj.hw5.filesort.CompositionSorter;
import hr.fer.zemris.java.tecaj.hw5.filesort.FileSorts;
import hr.fer.zemris.java.tecaj.hw5.filesort.ReverseFileSort;
import hr.fer.zemris.java.tecaj.hw5.filters.FileExtensionFilter;
import hr.fer.zemris.java.tecaj.hw5.filters.FileFilter;
import hr.fer.zemris.java.tecaj.hw5.filters.FileNameLengthFilter;
import hr.fer.zemris.java.tecaj.hw5.filters.FileSizeFilter;
import hr.fer.zemris.java.tecaj.hw5.filters.FileTypeFilter;
import hr.fer.zemris.java.tecaj.hw5.filters.ReverseFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Klasa je container za main metodu koja obavlja posao.
 * 
 * @author Ivan Relic
 * 
 */
public class Dir {

	// privatne clanske varijable za listu filtera, nacina sortiranja i nacina
	// ispisa
	private static List<Comparator<File>> sorts = new ArrayList<Comparator<File>>();
	private static List<FileInfoGetter> getters = new ArrayList<FileInfoGetter>();
	private static List<FileFilter> filters = new ArrayList<FileFilter>();
	private static int numberOfFiles;

	/**
	 * Metoda prima argumente komandne linije - path do foldera i razni
	 * specifikatori za filtriranje, sortiranje i ispis datoteka predanog
	 * direktorija.
	 * 
	 * @param args argumentni komandne linije
	 */
	public static void main(String[] args) {

		// ako nije predan nijedan arugment, dojaviti to korisniku
		if (args.length < 1) {
			System.out.println("You should provide path do directory!");
			System.exit(1);
		}

		// pokusaj kreirati file s predanim pathom, ako ne uspijes ili nije predan file koji
		// je direktorij, dojavi to korisniku
		File dir = null;
		try {
			dir = new File(args[0]);
			if (!dir.isDirectory()) {
				System.out.println("Given path is not a directory!");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("You should provide correct filepath!");
			System.exit(1);
		}

		// ako ima vise od 1 argumenata, pozovi metodu za parsiranje primljenih
		// specifikatora
		if (args.length > 1) {
			if (!parseArgs(args)) {
				System.exit(1);
			}
		}

		// inace, ako nema nijednog specificatora za file info, u file info listu stavi
		// file name getter, jer po defaultu se ispisuju samo imena fileova
		if (getters.size() == 0) {
			getters.add(new FileNameGetter());
		}
				
		//kreiraj listu fileova iz primljenog patha
		List<File> fileList = new ArrayList<File>(Arrays.asList(dir.listFiles()));
		
		//ako je fileList duljine 0, ispisi korisniku da je predan prazan direktorij i terminiraj prog
		if (fileList.size() == 0) {
			System.out.println("Directory you provided is empty!");
			System.exit(1);
		}
		
		//ako lista primljenih sortova nije prazna, kreiraj CompositionSorter i sortiraj
		//listu fajlova iz zadanog direktorija po predanim specifikatorima
		if (sorts.size() > 0) {
			CompositionSorter compositedSort = new CompositionSorter(sorts);
			Collections.sort(fileList, compositedSort);
		}
		
		//dohvati maksimalnu sirinu za svaki pojedini stupac
		int[] maxWidth = getMaxWidths(fileList);
		
		//ispisi trazene informacije za svaki file
		printFileInfo(fileList, maxWidth);
	}

	/**
	 * Metoda ispisuje trazene informacije za svaki file.
	 * 
	 * @param fileList lista fileova iz predanog direktorija
	 * @param maxWidth maksimalna sirina za svaki pojedini stupac
	 */
	private static void printFileInfo(List<File> fileList, int[] maxWidth) {
		
		//ako je 0 pronadjenih fileova (metoda koja trazi maksimalnu sirinu stupca to postavlja jer 
		//iovako iterira kroz sve fileove, tamo je dodano radi iskoristivosti), ispisi to korisniku 
		//i prekini ispis
		if (numberOfFiles == 0) {
			System.out.println("0 files meet the requirements.");
			return;
		}
		
		//ispisi gornji rub tablice
		printTableEdge(maxWidth);
		
		//prodji kroz listu fileova
		for (int i = 0, filesLength = fileList.size(); i < filesLength; i++) {
			
			//dohvati trenutni file
			File pomFile = fileList.get(i);
			
			//ako trenutni file zadovoljava filtere, trazi maxWidth za njega, inace ne
			if (acceptsAll(pomFile)) {
			
				//za trenutni file prodji kroz sve gettere
				for (int j = 0, gettersLength = getters.size(); j < gettersLength; j++) {
				
					//dohvati trenutni getter
					FileInfoGetter pomGetter = getters.get(j);
				
					//ako je trenutni getter FileNameGetter ispisi uz lijevo poravnanje, inace
					//ispisujes sve normalno (default je desno poravnanje)
					if (pomGetter instanceof FileNameGetter) {
						System.out.printf("| %-"+maxWidth[j]+"s ", 
								pomGetter.getInfo(pomFile));
					}
				
					else {
						System.out.printf("| %"+maxWidth[j]+"s ", 
								pomGetter.getInfo(pomFile));
					}
				}
				
				//dodaj znak | na kraj reda
				System.out.print("|");
				
				//prijedi u novi red kad si ispisao sve iz trenutnog reda
				System.out.println();
			}
		}
		
		//kad si ispisao sve redove, ispisi donji rub tablice
		printTableEdge(maxWidth);
		
		//ispisi broj fileova koji odgovaraju svim zahtjevima
		System.out.println(numberOfFiles + " files meet all the requirements.");
	}

	/**
	 * Metoda ispisuje rub tablice koristeci maksimalne sirine svakog stupca.
	 * 
	 * @param maxWidth niz maksimalnih sirina za svaki pojedini stupac
	 */
	private static void printTableEdge(int[] maxWidth) {
		
		//za svaku maksimalnu sirinu prodji i ispisi potrebno
		for (int i = 0; i < maxWidth.length; i++) {
			System.out.print("+");
			
			//ispisi - onoliko koliko je maxWidth za taj stupac + 2 radi estetike
			for (int j = 0, length = maxWidth[i] + 2; j < length; j++) {
				System.out.print("-");
			}			
		}
		System.out.print("+");
		
		//predji u novi red
		System.out.println();
	}

	/**
	 * Metoda iz liste fileova i gettera dohvaca maksimalnu sirinu za svaki pojedini stupac.
	 * 
	 * @param fileList lista fileova iz predanog direktorija
	 * @return niz maksimalnih sirina za svaki stupac po onom redoslijedu kojim su predani
	 */
	private static int[] getMaxWidths(List<File> fileList) {
		
		//kreiraj niz intova velicine liste gettera, specifikacija garantira da ce pocetne
		//vrijednosti biti postavljene na 0
		int[] maxWidth = new int[getters.size()];
		
		
		//prodji kroz listu fileova
		for (int i = 0, filesLength = fileList.size(); i < filesLength; i++) {
			
			//dohvati trenutni file
			File pomFile = fileList.get(i);
			
			//ako trenutni file zadovoljava filtere, trazi maxWidth za njega i povecaj brojac
			//pronadjenih fileova, inace nista ne radi
			if (acceptsAll(pomFile)) {
			
				numberOfFiles++;
				
				//za trenutni file prodji kroz sve primljene gettere
				for (int j = 0, gettersLength = getters.size(); j < gettersLength; j++) {
				
					//dohvati trenutni getter
					FileInfoGetter pomGetter = getters.get(j);
				
					//ako je sirina trenutnog stupca, tj. trenutne trazene informacije za 
					//trenutni file veca od max, max postavi na tu vrijednost
					int length = pomGetter.getInfo(pomFile).length();
					maxWidth[j] = (length > maxWidth[j]) ? length : maxWidth[j];
				}
			}
		}
		
		//prodji kroz niz maxWidthova i ako je bilo koji na 0, postavi ga na 1 (za stupce s jednim
		//slovom, za tip filea i za informaciju o skrivenosti)
		for (int i = 0; i < maxWidth.length; i++) {
			maxWidth[i] = (maxWidth[i] == 0) ? 1 : maxWidth[i];
		}
		
		//vrati niz maksimalnih sirina
		return maxWidth;
		
	}

	/**
	 * Metoda parsira primljene argumente i dodaje potrebne elemente u listu.
	 * 
	 * @param args argumenti komandne linije
	 * @return true ako je parsiranje svih argumenata uspjelo, false ako nije
	 */
	private static boolean parseArgs(String[] args) {

		// prodji kroz sve primljene specifikatore
		for (int i = 1, length = args.length; i < length; i++) {

			//ako argument odgovara specificatoru za sort, pozovi tu metodu i provjeri je li 
			//valjani specifikator, ako nije ispisi to korisniku
			if (args[i].matches("-s:.*")) {
				if (!isSortSpecificator(args[i])) {
					System.out.println("Bad sort specificator on " +i+ ". argument!");
					return false;
				}
			}
			
			//ako argument odgovara specificatoru za getter, pozovi tu metodu i provjeri je li 
			//valjani specifikator, ako nije ispisi to korisniku
			else if (args[i].matches("-w:.*")) {
				if (!isInfoSpecificator(args[i])) {
					System.out.println("Bad getter specificator on " +i+ ". argument!");
					return false;
				}
			}
			
			//ako argument odgovara specificatoru za filter, pozovi tu metodu i provjeri je li 
			//valjani specifikator, ako nije ispisi to korisniku
			else if (args[i].matches("-f:.*")) {
				if (!isFilterSpecificator(args[i])) {
					System.out.println("Bad filter specificator on " +i+ ". argument!");
					return false;
				}
			}
			
			//inace, nepoznat specifikator
			else {
				System.out.println("Unknown specificator on " +i+ ". argument!");
				return false;
			}
		}
		
		//ako svaki specifikator odgovara 
		return true;
	}

	/**
	 * Metoda provjerava je li predani string valjani info specifikator, ako je,
	 * kreira novi prikladni info getter i stavlja ga u listu gettera.
	 * 
	 * @param arg string s kojim provjeravamo
	 * @return true ako je predani string valjani specificator, false ako nije
	 */
	private static boolean isInfoSpecificator(String arg) {
		
		//ako je file name getter, dodaj ga u listu
		if (arg.matches("-w:n")) {
			getters.add(new FileNameGetter());
		}
		
		//ako je file type getter, dodaj ga u listu
		else if (arg.matches("-w:t")) {
			getters.add(new FileTypeGetter());
		}
		
		//ako je file size getter, dodaj ga u listu
		else if (arg.matches("-w:s")) {
			getters.add(new FileSizeGetter());
		}
		
		//ako je file date modify getter, dodaj ga u listu
		else if (arg.matches("-w:m")) {
			getters.add(new FileDateModifyGetter());
		}
		
		//ako je file hidden getter, dodaj ga u listu
		else if (arg.matches("-w:h")) {
			getters.add(new FileHiddenGetter());
		}
		
		//inace, vrati false jer nije valjani info specifikator
		else {
			return false;
		}
		
		//ako nisi vratio false, znaci da si pronasao match
		return true;
		
		
	}

	/**
	 * Metoda provjerava je li predani string valjani filter specifikator, ako
	 * je, kreira novi prikladni filter i stavlja ga u listu filtera.
	 * 
	 * @param arg string s kojim provjeravamo
	 * @return true ako je predani string valjani specificator, false ako nije
	 */
	private static boolean isFilterSpecificator(String arg) {
		
		//ako je size filter
		if (arg.matches("-f:s[0-9]+")) {
			
			//extractaj broj iz argumenta
			long size = Long.parseLong(arg.substring(arg.lastIndexOf('s') + 1));
			filters.add(new FileSizeFilter(size));
		}
		
		//ako je reverse size filter, tj. oni fileovi koji imaju velicinu vecu od zadane
		else if (arg.matches("-f:-s[0-9]+")) {
			
			//extractaj broj iz argumenta
			long size = Long.parseLong(arg.substring(arg.lastIndexOf('s') + 1));
			filters.add(new ReverseFileFilter(new FileSizeFilter(size)));
		}
		
		//ako je name length filter
		else if (arg.matches("-f:l[0-9]+")) {
			
			//extractaj broj iz argumenta
			long size = Long.parseLong(arg.substring(arg.lastIndexOf('l') + 1));
			filters.add(new FileNameLengthFilter(size));
		}
		
		//ako je reverse name length filter, tj. oni fileovi koji imaju duljinu imena vecu
		//od zadane
		else if (arg.matches("-f:-l[0-9]+")) {
			
			//extractaj broj iz argumenta
			long size = Long.parseLong(arg.substring(arg.lastIndexOf('l') + 1));
			filters.add(new ReverseFileFilter(new FileNameLengthFilter(size)));
		}
		
		//ako je file filter
		else if (arg.matches("-f:f")) {
			filters.add(new FileTypeFilter());
		}
		
		//ako je reverse file filter, tj. directory filter
		else if (arg.matches("-f:-f")) {
			filters.add(new ReverseFileFilter(new FileTypeFilter()));
		}
		
		//ako je extension filter
		else if (arg.matches("-f:e")) {
			filters.add(new FileExtensionFilter());
		}
		
		//ako je reverse extension filter, tj. fileovi koji nemaju ekstenziju
		else if (arg.matches("-f:-e")) {
			filters.add(new ReverseFileFilter(new FileExtensionFilter()));
		}
		
		//inace, ne odgovara ni jednom specifikatoru pa vrati false
		else {
			return false;
		}
		
		//ako nisi vratio false, znaci da je valjani specifikator, vrati true
		return true;
	}

	/**
	 * Metoda provjerava je li predani string valjani sort specifikator, ako je,
	 * kreira novi prikladni komparator i stavlja ga u listu komparatora.
	 * 
	 * @param arg string s kojim provjeravamo
	 * @return true ako je predani string valjani specificator, false ako nije
	 */
	private static boolean isSortSpecificator(String arg) {
		
		// ako odgovara specifikatoru za sort po velicini, dodaj ga u listu
		if (arg.matches("-s:s")) {
			sorts.add(FileSorts.BY_SIZE);
		}

		// ako odgovara specifikatoru za reverzni sort po velicini, dodaj ga u
		// listu
		else if (arg.matches("-s:-s")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_SIZE));
		}

		// ako odgovara specifikatoru za sort po imenu, dodaj ga u listu
		else if (arg.matches("-s:n")) {
			sorts.add(FileSorts.BY_NAME);
		}

		// ako odgovara specifikatoru za reverzni sort po imenu, dodaj ga u
		// listu
		else if (arg.matches("-s:-n")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_NAME));
		}

		// ako odgovara specifikatoru za sort po modifikaciji, dodaj ga u listu
		else if (arg.matches("-s:m")) {
			sorts.add(FileSorts.BY_LAST_MODIFIED);
		}

		// ako odgovara specifikatoru za reverzni sort po modifikaciji, dodaj ga
		// u listu
		else if (arg.matches("-s:-m")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_LAST_MODIFIED));
		}

		// ako odgovara specifikatoru za sort po tipu, dodaj ga u listu
		else if (arg.matches("-s:t")) {
			sorts.add(FileSorts.BY_TYPE);
		}

		// ako odgovara specifikatoru za reverzni sort po tipu, dodaj ga u listu
		else if (arg.matches("-s:-t")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_TYPE));
		}

		// ako odgovara specifikatoru za sort po duljini imena, dodaj ga u listu
		else if (arg.matches("-s:l")) {
			sorts.add(FileSorts.BY_NAME_LENGTH);
		}

		// ako odgovara specifikatoru za reverzni sort po duljini imena, dodaj
		// ga u listu
		else if (arg.matches("-s:-l")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_NAME_LENGTH));
		}

		// ako odgovara specifikatoru za sort po izvrsivosti, dodaj ga u listu
		else if (arg.matches("-s:e")) {
			sorts.add(FileSorts.BY_EXEC);
		}

		// ako odgovara specifikatoru za reverzni sort po izvrsivosti, dodaj ga
		// u listu
		else if (arg.matches("-s:-e")) {
			sorts.add(new ReverseFileSort(FileSorts.BY_EXEC));
		}

		// ako ne odgovara nijednom zadanom specifikatoru
		else {
			return false;
		}

		return true;
	}
	
	/**
	 * Metoda provjerava zadovoljava li file sve filtere iz liste.
	 * 
	 * @param f file za koji provjeravamo filtere
	 * @return true ako file zadovoljava sve filtere, false inace
	 */
	private static boolean acceptsAll(File f) {
		
		//ako je predana prazna lista, nema filtera pa je svakako rezultat uvijek true
		if (filters.size() == 0) {
			return true;
		}
		
		//inace, prodji kroz sve filtere i ako na barem jednom dobijes false, vrati false
		for (int i = 0, length = filters.size(); i < length; i++) {
			if (!filters.get(i).accepts(f)) {
				return false;
			}
		}
		
		//ako si prosao kroz cijelu listu filtera i nijedan nije vratio false, vrati true
		return true;
	}

}
