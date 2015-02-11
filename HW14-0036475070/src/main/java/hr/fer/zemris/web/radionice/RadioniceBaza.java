package hr.fer.zemris.web.radionice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Razred predstavlja bazu radionica koja podatke čita i zapisuje u datoteku na
 * disku.
 * 
 * @author Ivan Relić
 * 
 */
public class RadioniceBaza {

	private String direktorij;
	private Map<Long, Radionica> radionice;
	private Map<Long, Opcija> oprema;
	private Map<Long, Opcija> publika;
	private Map<Long, Opcija> trajanje;
	private Map<Opcija, Long> trajanjeID;
	private Map<Opcija, Long> publikaID;
	private Map<Opcija, Long> opremaID;
	private Long maxID;

	/**
	 * Privatni konstruktor. Prima path do direktorija i kreira novu praznu
	 * instancu baze.
	 * 
	 * @param direktorij
	 *            path do direktorija za kreiranje baze
	 * @throws IOException
	 *             ako dođe do pogreške prilikom očitavanja podataka o trajanju,
	 *             publici ili opremi
	 */
	private RadioniceBaza(String direktorij) throws IOException {
		this.direktorij = direktorij;
		radionice = new HashMap<Long, Radionica>();
		oprema = new TreeMap<>();
		opremaID = new TreeMap<>();
		publika = new TreeMap<>();
		publikaID = new TreeMap<>();
		trajanje = new TreeMap<>();
		trajanjeID = new TreeMap<>();
		// popuni članske mape za opremu, publiku i trajanje (u oba načina
		// mapiranja) u kreiranoj bazi
		kreirajMapuOpreme(oprema, opremaID, this.direktorij);
		kreirajMapuPublike(publika, publikaID, this.direktorij);
		kreirajMapuTrajanja(trajanje, trajanjeID, this.direktorij);
	}

	/**
	 * Vraća mapu opreme.
	 * 
	 * @return mapa opreme
	 */
	public Map<Long, Opcija> getOprema() {
		return oprema;
	}

	/**
	 * Vraća mapu publike.
	 * 
	 * @return mapa publike
	 */
	public Map<Long, Opcija> getPublika() {
		return publika;
	}

	/**
	 * Vraća mapu trajanja.
	 * 
	 * @return mapa trajanja
	 */
	public Map<Long, Opcija> getTrajanje() {
		return trajanje;
	}

	/**
	 * Puni predane mape za mapiranje trajanja.
	 * 
	 * @param map
	 *            mapa za mapiranje ID->opcija
	 * @param mapID
	 *            mapa za mapiranje opcija->ID
	 * @param direktorij
	 *            direktorij baze podataka
	 * @throws IOException
	 *             u slučaju neuspješnog čitanja filea s trajanjem
	 */
	private static void kreirajMapuTrajanja(Map<Long, Opcija> map,
			Map<Opcija, Long> mapID, String direktorij) throws IOException {
		Path stazaDatoteke = Paths.get(direktorij + "/trajanje.txt");
		if (Files.notExists(stazaDatoteke)) {
			return;
		}
		List<String> linijeDatoteke = Files.readAllLines(stazaDatoteke,
				StandardCharsets.UTF_8);
		popuniMapu(linijeDatoteke, map, mapID);
	}

	/**
	 * Puni predane mape za mapiranje publike.
	 * 
	 * @param map
	 *            mapa za mapiranje ID->opcija
	 * @param mapID
	 *            mapa za mapiranje opcija->ID
	 * @param direktorij
	 *            direktorij baze podataka
	 * @throws IOException
	 *             u slučaju neuspješnog čitanja filea s trajanjem
	 */
	private static void kreirajMapuPublike(Map<Long, Opcija> map,
			Map<Opcija, Long> mapID, String direktorij) throws IOException {
		Path stazaDatoteke = Paths.get(direktorij + "/publika.txt");
		if (Files.notExists(stazaDatoteke)) {
			return;
		}
		List<String> linijeDatoteke = Files.readAllLines(stazaDatoteke,
				StandardCharsets.UTF_8);
		popuniMapu(linijeDatoteke, map, mapID);
	}

	/**
	 * Puni predane mape za mapiranje opreme.
	 * 
	 * @param map
	 *            mapa za mapiranje ID->opcija
	 * @param mapID
	 *            mapa za mapiranje opcija->ID
	 * @param direktorij
	 *            direktorij baze podataka
	 * @throws IOException
	 *             u slučaju neuspješnog čitanja filea s trajanjem
	 */
	private static void kreirajMapuOpreme(Map<Long, Opcija> map,
			Map<Opcija, Long> mapID, String direktorij) throws IOException {
		Path stazaDatoteke = Paths.get(direktorij + "/oprema.txt");
		if (Files.notExists(stazaDatoteke)) {
			return;
		}
		List<String> linijeDatoteke = Files.readAllLines(stazaDatoteke,
				StandardCharsets.UTF_8);
		popuniMapu(linijeDatoteke, map, mapID);
	}

	/**
	 * Puni predanu mapu elementima iz pročitanih linija iz datoteke. Puni mape
	 * u kojima je mapiranje ID->opcija, ali i mape u kojima je mapiranje
	 * opcija->ID tako da imamo spremne mape i za čitanje i za pisanje baze.
	 * 
	 * @param linijeDatoteke
	 *            linije pročitane iz datoteke
	 * @param mapa
	 *            mapa za punjenje (ID, opcija)
	 * @param mapaID
	 *            mapa za punjenje (opcija, ID)
	 */
	private static void popuniMapu(List<String> linijeDatoteke,
			Map<Long, Opcija> mapa, Map<Opcija, Long> mapaID) {
		boolean prvaLinija = true;
		for (String linija : linijeDatoteke) {
			linija = linija.trim();
			if (linija.isEmpty()) {
				continue;
			}
			// ukloni BOM oznake UTF-8 formata iz prve linije
			if (prvaLinija) {
				prvaLinija = false;
				linija = linija.replace("\uFEFF", "");
			}
			String[] elementi = linija.split("\t");
			// u mapu pohrani trenutnu opciju i namapiraj je na njen ID
			Long ID = Long.parseLong(elementi[0]);
			Opcija opcija = new Opcija(elementi[0], elementi[1]);
			mapa.put(ID, opcija);
			// u mapuID pohrani trenutni ID i namapiraj ga na opciju koja mu je
			// pridružena
			mapaID.put(opcija, ID);
		}
	}

	/**
	 * Metoda za dohvat zapisa preko poznatog identifikatora.
	 * 
	 * @param id
	 *            identifikator
	 * @return zapis ako postoji, null inače
	 */
	public Radionica dohvati(Long id) {
		return radionice.get(id);
	}

	/**
	 * Metoda vraća listu svih trenutno korištenih identifikatora radionica.
	 * 
	 * @return lista identifikatora
	 */
	public Set<Long> dohvatiSveIdentifikatoreRadionica() {
		return Collections.unmodifiableSet(radionice.keySet());
	}

	/**
	 * Metoda vraća listu svih radionica koje su trenutno u bazi.
	 * 
	 * @return lista radionica
	 */
	public List<Radionica> dohvatiSveZapiseRadionica() {
		return Collections.unmodifiableList(new ArrayList<Radionica>(radionice
				.values()));
	}

	/**
	 * Metoda vraća listu svih trenutno korištenih identifikatora za opremu.
	 * 
	 * @return lista identifikatora opreme
	 */
	public Set<Long> dohvatiSveIdentifikatoreOpreme() {
		return Collections.unmodifiableSet(oprema.keySet());
	}

	/**
	 * Metoda vraća listu svih opcija opreme koje su trenutno u bazi.
	 * 
	 * @return lista opcija opreme
	 */
	public List<Opcija> dohvatiSveZapiseOpreme() {
		return Collections.unmodifiableList(new ArrayList<Opcija>(oprema
				.values()));
	}

	/**
	 * Metoda vraća listu svih trenutno korištenih identifikatora za publiku.
	 * 
	 * @return lista identifikatora publike
	 */
	public Set<Long> dohvatiSveIdentifikatorePublike() {
		return Collections.unmodifiableSet(publika.keySet());
	}

	/**
	 * Metoda vraća listu svih opcija publike koje su trenutno u bazi.
	 * 
	 * @return lista opcija publike
	 */
	public List<Opcija> dohvatiSveZapisePublike() {
		return Collections.unmodifiableList(new ArrayList<Opcija>(publika
				.values()));
	}

	/**
	 * Metoda vraća listu svih trenutno korištenih identifikatora za trajanje.
	 * 
	 * @return lista identifikatora trajanja
	 */
	public Set<Long> dohvatiSveIdentifikatoreTrajanja() {
		return Collections.unmodifiableSet(trajanje.keySet());
	}

	/**
	 * Metoda vraća listu svih opcija trajanja koje su trenutno u bazi.
	 * 
	 * @return lista opcija trajanja
	 */
	public List<Opcija> dohvatiSveZapiseTrajanja() {
		return Collections.unmodifiableList(new ArrayList<Opcija>(trajanje
				.values()));
	}

	/**
	 * Metoda briše zapis sa predanim identifikatorom.
	 * 
	 * @param id
	 *            identifikator zapisa
	 */
	public void obrisi(Long id) {
		radionice.remove(id);
	}

	/**
	 * Metoda s diska čita podatke i vraća bazu radionica koja je
	 * inicijalizirana tim podatcima. Ako datoteka ne postoji, metoda vraća
	 * prazanu bazu radionica. Ako datoteka postoji ali je nije moguće
	 * pročitati, generira se iznimka.
	 * 
	 * @param direktorij
	 *            direktorij baze na disku
	 * @return adresar
	 * @throws IOException
	 *             u slučaju pogreške prilikom čitanja datoteke
	 */
	public static RadioniceBaza ucitaj(String direktorij) throws IOException {

		// ako ne postoji file radionice, kreiraj novu praznu bazu radionica
		Path path = Paths.get(direktorij + "/radionice.txt");
		if (!Files.exists(path)) {
			return new RadioniceBaza(direktorij);
		}

		RadioniceBaza baza = new RadioniceBaza(direktorij);

		// pročitaj sve linije iz datoteke i napuni bazu prema pročitanome
		List<String> linijeDatoteke = Files.readAllLines(path,
				StandardCharsets.UTF_8);
		boolean prvaLinija = true;
		for (String linija : linijeDatoteke) {
			linija = linija.trim();
			if (linija.isEmpty()) {
				continue;
			}
			// ukloni BOM oznake UTF-8 formata iz prve linije
			if (prvaLinija) {
				prvaLinija = false;
				linija = linija.replace("\uFEFF", "");
			}
			String[] elementi = linija.split("\t");
			Long radionicaID = Long.parseLong(elementi[0].trim());
			// dohvati opremu, publiku i trajanje radionice i kreiraj novu
			// radionicu te je stavi u mapu radionica baze
			Set<Opcija> opremaRadionice = new LinkedHashSet<Opcija>(
					dohvatiOpremu(radionicaID, baza.oprema, baza.direktorij));
			Set<Opcija> publikaRadionice = new LinkedHashSet<Opcija>(
					dohvatiPubliku(radionicaID, baza.publika, baza.direktorij));
			Opcija trajanjeRadionice = baza.trajanje.get(Long
					.parseLong(elementi[4].trim()));
			String dopuna = (elementi.length == 6) ? ""
					: dekodirajDopunu(elementi[6].trim());
			baza.snimi(new Radionica(Long.valueOf(elementi[0]), elementi[1],
					elementi[2], opremaRadionice, trajanjeRadionice,
					publikaRadionice, Integer.valueOf(elementi[3]),
					elementi[5], dopuna));
		}
		return baza;
	}

	/**
	 * Snima trenutnu instancu baze radionica na path koji joj je predan preko
	 * konstruktora.
	 * 
	 * @throws IOException
	 *             ako snimanje ne uspije
	 */
	public void snimi() throws IOException {
		snimi(this.direktorij);
	}

	/**
	 * Snima trenutnu instancu baze radionica na predani path.
	 * 
	 * @param direktorij
	 *            direktorij za snimanje baze
	 * @throws IOException
	 *             ako snimanje ne uspije
	 */
	public void snimi(String direktorij) throws IOException {
		provjeriIspravnostOpcija();
		File dir = new File(direktorij);
		if (!dir.exists()) {
			dir.mkdir();
		}
		List<Radionica> listaRadionica = new ArrayList<Radionica>(
				radionice.values());
		Collections.sort(listaRadionica);

		// provjeri postoje li fileovi za radionice, opremu radionica, publiku
		// radionica, ako ne postoje kreiraj ih
		File stazaDatotekeRadionica = new File(direktorij + "/radionice.txt");
		kreirajDatotekuAkoNePostoji(stazaDatotekeRadionica);
		File stazaDatotekeOpreme = new File(direktorij
				+ "/radionice_oprema.txt");
		kreirajDatotekuAkoNePostoji(stazaDatotekeOpreme);
		File stazaDatotekePublike = new File(direktorij
				+ "/radionice_publika.txt");
		kreirajDatotekuAkoNePostoji(stazaDatotekePublike);

		// kreiraj writere za pisanje u svaku od kreiranih fileova
		BufferedWriter radionicaWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
						stazaDatotekeRadionica), StandardCharsets.UTF_8));
		BufferedWriter opremaWriter = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(stazaDatotekeOpreme),
						StandardCharsets.UTF_8));
		BufferedWriter publikaWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
						stazaDatotekePublike), StandardCharsets.UTF_8));

		// za svaku radionicu zapiši sve o njoj u fileove
		for (Radionica radionica : listaRadionica) {
			Long ID = radionica.getId();
			zapisiRadionicu(radionicaWriter, radionica, ID);
			zapisiOpcijeRadionice(opremaWriter, ID, opremaID,
					radionica.getOprema());
			zapisiOpcijeRadionice(publikaWriter, ID, publikaID,
					radionica.getPublika());
		}
		radionicaWriter.flush();
		radionicaWriter.close();
		opremaWriter.flush();
		opremaWriter.close();
		publikaWriter.flush();
		publikaWriter.close();
	}

	/**
	 * Metoda predanu radionicu dodaje u bazu. Ako radionica nema jedinstveni
	 * identifikator, automatski će ga dobiti.
	 * 
	 * @param r
	 *            radionica
	 */
	public void snimi(Radionica r) {
		if (r.getId() == null) {
			Long noviId = maxID == null ? 1 : maxID + 1;
			r.setId(noviId);
		}

		radionice.put(r.getId(), r);
		if (maxID == null || r.getId().compareTo(maxID) > 0) {
			maxID = r.getId();
		}
	}

	/**
	 * Zapisuje opcije radionice na predani writer.
	 * 
	 * @param opremaWriter
	 *            writer na koji ispisuje podatke
	 * @param ID
	 *            ID radionice
	 * @param mapID
	 *            mapa po kojoj se obavlja mapiranje opcije na ID
	 * @param listaOpcija
	 *            lista opcija koje se moraju zapisati na writer
	 * @throws IOException
	 *             ako zapisivanje ne uspije
	 */
	private void zapisiOpcijeRadionice(BufferedWriter writer, Long ID,
			Map<Opcija, Long> mapID, Set<Opcija> listaOpcija)
			throws IOException {
		List<Opcija> opcije = new ArrayList<Opcija>(listaOpcija);
		Collections.sort(opcije);
		for (Opcija opcija : opcije) {
			writer.write(ID.toString() + "\t" + mapID.get(opcija).toString());
			writer.write("\r\n");
		}
	}

	/**
	 * Zapisuje elemente radionice na predani writer.
	 * 
	 * @param radionicaWriter
	 *            writer na koji ispisuje podatke
	 * @param radionica
	 *            radionica
	 * @param ID
	 *            ID radionice
	 * @throws IOException
	 *             ako zapisivanje ne uspije
	 */
	private void zapisiRadionicu(BufferedWriter radionicaWriter,
			Radionica radionica, Long ID) throws IOException {
		radionicaWriter.write(ID.toString() + "\t");
		radionicaWriter.write(radionica.getNaziv() + "\t");
		radionicaWriter.write(radionica.getDatum() + "\t");
		radionicaWriter.write(radionica.getMaksPolaznika().toString() + "\t");
		radionicaWriter.write(trajanjeID.get(radionica.getTrajanje())
				.toString() + "\t");
		radionicaWriter.write(radionica.getEmail() + "\t");
		radionicaWriter.write(kodirajDopunu(radionica.getDopuna()));
		radionicaWriter.write("\r\n");
	}

	/**
	 * Kreira datoteku ako ne postoji.
	 * 
	 * @param datoteka
	 *            datoteka za kreiranje
	 * @throws IOException
	 */
	private void kreirajDatotekuAkoNePostoji(File datoteka) throws IOException {
		if (!datoteka.exists()) {
			datoteka.createNewFile();
		}
	}

	/**
	 * Provjerava konzistentnost baze podataka, tj. postoje li sve navedene
	 * opcije u radionicama interno u bazi podataka, tj. je li održan integritet
	 * stranog ključa.
	 */
	private void provjeriIspravnostOpcija() {
		// prođi svim radionicama
		for (Radionica radionica : radionice.values()) {
			// prođi cijelom opremom
			for (Opcija opcija : radionica.getOprema()) {
				if (!opremaID.containsKey(opcija)) {
					throw new InconsistentDatabaseException(
							"Nepoznata vrijednost za opciju opreme: "
									+ opcija.getVrijednost());
				}
			}
			// prođi svim opcijama publike
			for (Opcija opcija : radionica.getPublika()) {
				if (!publikaID.containsKey(opcija)) {
					throw new InconsistentDatabaseException(
							"Nepoznata vrijednost za opciju publike: "
									+ opcija.getVrijednost());
				}
			}
			// provjeri opciju trajanja
			if (!trajanjeID.containsKey(radionica.getTrajanje())) {
				throw new InconsistentDatabaseException(
						"Nepoznata vrijednost za opciju trajanja: "
								+ radionica.getTrajanje().getVrijednost());
			}
		}
	}

	/**
	 * Kodira dopunu za upis u file baze podataka.
	 * 
	 * @param dopuna
	 *            dopuna koja se treba kodirati
	 * @return kodirana dopuna
	 */
	private static String kodirajDopunu(String dopuna) {
		return dopuna.replace("\\", "\\\\").replace("\r", "")
				.replace("\n", "\\n").replace("\t", "\\t");
	}

	/**
	 * Obavlja dekodiranje znakova \n, \t i \ u dopuni iz filea.
	 * 
	 * @param dopuna
	 *            dopuna radionice
	 * @return dekodirana dopuna radionice
	 */
	private static String dekodirajDopunu(String dopuna) {
		return dopuna.replace("\\n", "\n").replace("\\t", "\t")
				.replace("\\\\", "\\");
	}

	/**
	 * Za primljeni ID radionice vraća set sa svim opcijama za publiku koji su
	 * pridjeljeni toj radionici.
	 * 
	 * @param radionicaID
	 *            ID radionice za koju tražimo publiku
	 * @param publika
	 *            mapa s opcijama za publiku
	 * @param direktorij
	 *            direktorij baze
	 * @return set publike radionice
	 * @throws IOException
	 *             ako dođe do pogreške prilikom čitanja filea s publikom
	 */
	private static List<Opcija> dohvatiPubliku(Long radionicaID,
			Map<Long, Opcija> publika, String direktorij) throws IOException {
		Path pathDatoteke = Paths.get(direktorij + "/radionice_publika.txt");
		// ako datoteka ne postoji, vrati prazan set
		if (Files.notExists(pathDatoteke)) {
			return new ArrayList<Opcija>();
		}
		// pročitaj sadržaj filea radionice_publika.txt i pronađi sve retke koji
		// sadrže informaciju za primljeni ID radionice
		List<String> linijeDatoteke = Files.readAllLines(pathDatoteke,
				StandardCharsets.UTF_8);
		List<Opcija> opcije = dohvatiOpcije(radionicaID, linijeDatoteke,
				publika);
		return opcije;
	}

	/**
	 * Za primljeni ID radionice vraća set sa svim opcijama za opremu koja je
	 * pridjeljena toj radionici.
	 * 
	 * @param radionicaID
	 *            ID radionice za koju tražimo opremu
	 * @param publika
	 *            mapa s opcijama za publiku
	 * @param direktorij
	 *            direktorij baze
	 * @return set opreme radionice
	 * @throws IOException
	 *             ako dođe do pogreške prilikom čitanja filea s publikom
	 */
	private static List<Opcija> dohvatiOpremu(Long radionicaID,
			Map<Long, Opcija> oprema, String direktorij) throws IOException {
		Path pathDatoteke = Paths.get(direktorij + "/radionice_oprema.txt");
		// ako datoteka ne postoji, vrati prazan set
		if (Files.notExists(pathDatoteke)) {
			return new ArrayList<Opcija>();
		}
		// pročitaj sadržaj filea radionice_oprema.txt i pronađi sve retke koji
		// sadrže informaciju za primljeni ID radionice
		List<String> linijeDatoteke = Files.readAllLines(pathDatoteke,
				StandardCharsets.UTF_8);
		List<Opcija> opcije = dohvatiOpcije(radionicaID, linijeDatoteke, oprema);
		return opcije;
	}

	/**
	 * Dohvaća opcije iz predane mape koje u pročitanim linijama datoteke
	 * odgovaraju predanom ID-u radionice.
	 * 
	 * @param radionicaID
	 *            ID radionice
	 * @param linijeDatoteke
	 *            linije iz pročitane datoteke
	 * @param mapa
	 *            mapa iz koje se dohvaćaju opcije po ID-u
	 * @return set opcija
	 */
	private static List<Opcija> dohvatiOpcije(Long radionicaID,
			List<String> linijeDatoteke, Map<Long, Opcija> mapa) {
		String ID = radionicaID.toString();
		List<Long> opcijeID = new ArrayList<Long>();
		boolean prvaLinija = true;
		for (String linija : linijeDatoteke) {
			linija = linija.trim();
			if (linija.isEmpty()) {
				continue;
			}
			// ukloni BOM oznake UTF-8 formata iz prve linije
			if (prvaLinija) {
				prvaLinija = false;
				linija = linija.replace("\uFEFF", "");
			}
			String[] elementi = linija.split("\t");
			// ako prvi element linije odgovara predanom id-u, u listu koja
			// označava sve opcije dodaj i drugi element linije
			if (elementi[0].equals(ID)) {
				opcijeID.add(Long.parseLong(elementi[1]));
			}
		}
		List<Opcija> opcije = new ArrayList<Opcija>();
		for (Long opcijaID : opcijeID) {
			opcije.add(mapa.get(opcijaID));
		}
		Collections.sort(opcije);
		return opcije;
	}
}
