package hr.fer.zemris.web.radionice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Razred modelira radionicu u obliku prikladnom za formu (svi propertyji su
 * stringovi što omogućava lakše praćenje pogrešnih unosa).
 * 
 * @author Ivan Relić
 * 
 */
public class RadionicaForm {

	private String id;
	private String naziv;
	private String datum;
	private Set<String> oprema;
	private String trajanje;
	private Set<String> publika;
	private String maksPolaznika;
	private String email;
	private String dopuna;
	private Map<String, String> greske = new HashMap<>();
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Konstruktor. Sve ostavlja na nedefiniranim vrijednostima.
	 */
	public RadionicaForm() {
	}

	/**
	 * Postavlja parametre forme radionice iz primljenog http zahtjeva.
	 * 
	 * @param req
	 *            http zahtjev
	 */
	public void popuniIzHttpRequesta(HttpServletRequest req) {
		this.id = pripremi(req.getParameter("id"));
		this.naziv = pripremi(req.getParameter("naziv"));
		this.datum = pripremi(req.getParameter("datum"));
		this.oprema = dohvatiOpcije(req.getParameterValues("oprema"));
		this.trajanje = pripremi(req.getParameter("trajanje"));
		this.publika = dohvatiOpcije(req.getParameterValues("publika"));
		this.maksPolaznika = pripremi(req.getParameter("maksPolaznika"));
		this.email = pripremi(req.getParameter("email"));
		this.dopuna = pripremi(req.getParameter("dopuna"));
	}

	/**
	 * Puni objekt forme iz originalnog objekta radionice.
	 * 
	 * @param radionica
	 *            originalni objekt radionice
	 */
	public void popuniIzRadionice(Radionica radionica) {
		if (radionica.getId() == null) {
			this.id = "";
		} else {
			this.id = radionica.getId().toString();
		}
		this.naziv = radionica.getNaziv();
		this.datum = radionica.getDatum();
		this.oprema = dohvatiIdentifikatoreOpcija(radionica.getOprema());
		if (radionica.getTrajanje() == null) {
			this.trajanje = "";
		} else {
			this.trajanje = radionica.getTrajanje().getId();
		}
		this.publika = dohvatiIdentifikatoreOpcija(radionica.getPublika());
		if (radionica.getMaksPolaznika() == null) {
			this.maksPolaznika = "";
		} else {
			this.maksPolaznika = radionica.getMaksPolaznika().toString();
		}
		this.email = radionica.getEmail();
		this.dopuna = radionica.getDopuna();
	}

	/**
	 * Popunjava predanu radionicu tako da odgovara elementima forme radionice.
	 * 
	 * @param radionica
	 *            radionica za popuniti
	 * @param opremaMap
	 *            mapa sa svim opcijama opreme i njenim identifikatorima
	 * @param publikaMap
	 *            mapa sa svim opcijama publike i njenim identifikatorima
	 * @param trajanjeMap
	 *            mapa sa svim opcijama trajanja i njenim identifikatorima
	 */
	public void popuniURadionicu(Radionica radionica,
			Map<Long, Opcija> opremaMap, Map<Long, Opcija> publikaMap,
			Map<Long, Opcija> trajanjeMap) {
		if (this.id.isEmpty()) {
			radionica.setId(null);
		} else {
			radionica.setId(Long.valueOf(this.id));
		}
		radionica.setNaziv(naziv);
		radionica.setDatum(datum);
		radionica.setOprema(dohvatiOpcijeIzIdentifikatora(opremaMap, oprema));
		radionica.setTrajanje(trajanjeMap.get(Long.valueOf(trajanje)));
		radionica
				.setPublika(dohvatiOpcijeIzIdentifikatora(publikaMap, publika));
		radionica.setMaksPolaznika(Integer.valueOf(maksPolaznika));
		radionica.setEmail(email);
		radionica.setDopuna(dopuna);
	}

	/**
	 * Validira unos radionice u formi i ako ima pogrešaka, postavlja ih u mapu
	 * pogrešaka namapirane na takav ključ koji odgovara propertyju koji je
	 * pogrešan.
	 */
	public void validiraj() {
		greske.clear();
		if (naziv.isEmpty() || naziv.length() > 40) {
			greske.put("naziv",
					"Naziv radionice mora biti definiran i mora biti kraći od 40 znakova.");
		}
		if (!datum.matches("[0-9][0-9][0-9][0-9]-[0-9]?[0-9]-[0-9]?[0-9]")) {
			greske.put("datum", "Datum mora biti oblika GGGG-MM-DD.");
		} else {
			datum = provjeriDatum(datum);
		}
		if (trajanje.isEmpty()) {
			greske.put("trajanje", "Trajanje radionice mora biti zadano.");
		}
		if (publika.size() == 0) {
			greske.put("publika",
					"Potrebno je odabrati barem 1 vrstu ciljane publike za radionicu.");
		}
		if (maksPolaznika.isEmpty()) {
			greske.put("maksPolaznika",
					"Potrebno je zadati maksimalan broj polaznika radionice.");
		} else {
			try {
				int brojPolaznika = Integer.parseInt(maksPolaznika);
				if (brojPolaznika < 10 || brojPolaznika > 50) {
					greske.put("maksPolaznika",
							"Maksimalni broj polaznika mora biti cijeli broj iz intervala [10, 50]");
				}
			} catch (NumberFormatException ex) {
				greske.put("maksPolaznika",
						"Maksimalni broj polaznika mora biti cijeli broj iz intervala [10, 50].");
			}
		}
		if (email.isEmpty()) {
			greske.put("email",
					"E-mail voditelja radionice mora biti definiran.");
		} else {
			if (!email.matches(EMAIL_REGEX)) {
				greske.put("email", "E-mail voditelja radionice nije validan.");
			}
		}
	}

	/**
	 * Predani string priprema tako da se može odmah pohraniti u property ovog
	 * razreda. Ako je predani parametar null, metoda vraća prazni string, inače
	 * ga trima i vraća ga trimanog.
	 * 
	 * @param parameter
	 *            string koji je potrebno pripremiti
	 * @return pripremljeni string
	 */
	private String pripremi(String parameter) {
		if (parameter == null) {
			return "";
		} else
			return parameter.trim();
	}

	/**
	 * Dohvaća opcije za popunjavanje forme.
	 * 
	 * @param parameters
	 * @return
	 */
	private Set<String> dohvatiOpcije(String[] parameters) {
		Set<String> opcije = new LinkedHashSet<String>();
		if (parameters == null) {
			return opcije;
		}
		for (int i = 0, length = parameters.length; i < length; i++) {
			opcije.add(pripremi(parameters[i]));
		}
		return opcije;
	}

	/**
	 * Popravlja strogo validan datum (mora proći kroz metodu provjeriDatum())
	 * tako da ako je na nekom od mjesta mjesec ili dan napisan kao
	 * jednoznamenkasti broj to pretvori u dvoznamenkasti.
	 * 
	 * @param datumElements
	 *            elementi datuma razdvojeni po -
	 * @return popravljeni datum
	 */
	private String popraviDatum(String[] datumElements) {
		StringBuilder datum = new StringBuilder();
		datum.append(datumElements[0] + "-");
		if (datumElements[1].length() == 1) {
			datum.append("0");
		}
		datum.append(datumElements[1] + "-");
		if (datumElements[2].length() == 1) {
			datum.append("0");
		}
		datum.append(datumElements[2]);
		return datum.toString();
	}

	/**
	 * Provjerava jesu li svi parametri datuma dobro zadani, ako nisu, dodaje
	 * grešku u mapu.
	 * 
	 * @param datum
	 */
	private String provjeriDatum(String datum) {
		Calendar kalendar = new GregorianCalendar();
		String[] elementiDatuma = datum.split("-");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String trenutniDatum = dateFormat.format(kalendar.getTime());
		int trenutnaGodina = kalendar.get(Calendar.YEAR);
		int trenutniMjesec = kalendar.get(Calendar.MONTH) + 1;
		int trenutniDan = kalendar.get(Calendar.DAY_OF_MONTH);
		int godina = Integer.parseInt(elementiDatuma[0]);
		if (godina < trenutnaGodina) {
			greske.put("datum",
					"Datum mora biti veći ili jednak od trenutnog: "
							+ trenutniDatum);
			return datum;
		}
		int mjesec = Integer.parseInt(elementiDatuma[1]);
		if (godina == trenutnaGodina && mjesec < trenutniMjesec) {
			greske.put("datum",
					"Datum mora biti veći ili jednak od trenutnog: "
							+ trenutniDatum);
			return datum;
		}
		if (mjesec < 1 || mjesec > 12) {
			greske.put("datum", "Mjesec mora biti unutar intervala [1,12].");
			return datum;
		}
		int dan = Integer.parseInt(elementiDatuma[2]);
		if (godina == trenutnaGodina && mjesec == trenutniMjesec
				&& dan < trenutniDan) {
			greske.put("datum",
					"Datum mora biti veći ili jednak od trenutnog: "
							+ trenutniDatum);
			return datum;
		}
		if (mjesec == 2 && (dan < 0 || dan > 28)) {
			greske.put("datum",
					"Ako je mjesec 2., dan mora biti unutar intervala [0-28]");
			return datum;
		}
		if (mjesec < 8) {
			if (mjesec % 2 == 0 & (dan < 0 || dan > 30)) {
				greske.put("datum", "Ako je mjesec " + mjesec
						+ "., dan mora biti unutar intervala [0-30]");
				return datum;
			} else if (mjesec % 2 == 1 && (dan < 0 || dan > 31)) {
				greske.put("datum", "Ako je mjesec " + mjesec
						+ "., dan mora biti unutar intervala [0-31]");
				return datum;
			}
		} else {
			if (mjesec % 2 == 0 & (dan < 0 || dan > 31)) {
				greske.put("datum", "Ako je mjesec " + mjesec
						+ "., dan mora biti unutar intervala [0-31]");
				return datum;
			} else if (mjesec % 2 == 1 && (dan < 0 || dan > 30)) {
				greske.put("datum", "Ako je mjesec " + mjesec
						+ "., dan mora biti unutar intervala [0-30]");
				return datum;
			}
		}
		// ako su sve provjere prošle u redu, vrati popravljeni datum
		return popraviDatum(elementiDatuma);
	}

	/**
	 * Kreira set opcija za radionicu iz primljene mape koja mapira opcije po
	 * njenim identifikatorima i iz mape identifikatora opcija koje forma
	 * sadrži.
	 * 
	 * @param mapa
	 *            mapa (ID->opcija)
	 * @param identifikatori
	 *            set identifikatora opcija koje forma sadrži
	 * @return
	 */
	private Set<Opcija> dohvatiOpcijeIzIdentifikatora(Map<Long, Opcija> mapa,
			Set<String> identifikatori) {
		Set<Opcija> opcije = new LinkedHashSet<Opcija>();
		for (String ID : identifikatori) {
			opcije.add(mapa.get(Long.valueOf(ID)));
		}
		return opcije;
	}

	/**
	 * Iz seta opcija radionice dohvaća samo vrijednosti identifikatora.
	 * 
	 * @param opcije
	 *            set opcija radionice
	 * @return set identifikatora opcija
	 */
	private Set<String> dohvatiIdentifikatoreOpcija(Set<Opcija> opcije) {
		Set<String> identifikatori = new LinkedHashSet<String>();
		for (Opcija opcija : opcije) {
			identifikatori.add(opcija.getId());
		}
		return identifikatori;
	}

	/**
	 * Dohvaća pogrešku mapiranu uz predani property.
	 * 
	 * @param ime
	 *            ime propertya
	 * @return pogreška uz property
	 */
	public String dohvatiPogresku(String ime) {
		return greske.get(ime);
	}

	/**
	 * Vraća informaciju ima li pogrešaka u ispunjenoj formi.
	 * 
	 * @return true ako ima pogrešaka, false inače
	 */
	public boolean imaPogresaka() {
		return !greske.isEmpty();
	}

	/**
	 * Vraća informaciju ima li pogrešaka u ispunjenoj formi za predani
	 * property.
	 * 
	 * @param ime
	 *            property za koji se provjerava ima li pogrešaka
	 * @return true ako ima pogrešaka, false inače
	 */
	public boolean imaPogresku(String ime) {
		return greske.containsKey(ime);
	}

	/**
	 * Dohvaća vrijednost id-a radionice.
	 * 
	 * @return id radionice
	 */
	public String getId() {
		return id;
	}

	/**
	 * Dohvaća vrijednost naziva radionice.
	 * 
	 * @return naziv radionice
	 */
	public String getNaziv() {
		return naziv;
	}

	/**
	 * Dohvaća vrijednost datuma održavanja radionice.
	 * 
	 * @return datum održavanja radionice
	 */
	public String getDatum() {
		return datum;
	}

	/**
	 * Dohvaća vrijednosti opreme potrebne za održavanje radionice.
	 * 
	 * @return oprema potrebna za održavanje radionice
	 */
	public Set<String> getOprema() {
		return oprema;
	}

	/**
	 * Dohvaća vrijednost trajanja radionice.
	 * 
	 * @return trajanje radionice
	 */
	public String getTrajanje() {
		return trajanje;
	}

	/**
	 * Dohvaća vrijednosti tipova publike koja može sudjelovati u radionici.
	 * 
	 * @return tipovi publike koji mogu sudjelovati u radionici
	 */
	public Set<String> getPublika() {
		return publika;
	}

	/**
	 * Dohvaća vrijednost maksimalnog broja polaznika koji mogu sudjelovati
	 * radionici.
	 * 
	 * @return maksimalni broj polaznika koji mogu sudjelovati u radionici
	 */
	public String getMaksPolaznika() {
		return maksPolaznika;
	}

	/**
	 * Dohvaća vrijednost emaila voditelja radionice.
	 * 
	 * @return email voditelja radionice
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Dohvaća vrijednost dodatnih uputa o radionici.
	 * 
	 * @return dodatne upute o radionici
	 */
	public String getDopuna() {
		return dopuna;
	}

	/**
	 * Postavlja vrijednost id-a radionice.
	 * 
	 * @param id
	 *            id radionice
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Postavlja vrijednost naziva radionice.
	 * 
	 * @param naziv
	 *            naziv radionice
	 */
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	/**
	 * Postavlja vrijednost datuma održavanja radionice.
	 * 
	 * @param datum
	 *            datum održavanja radionice
	 */
	public void setDatum(String datum) {
		this.datum = datum;
	}

	/**
	 * Postavlja vrijednosti opreme potrebne za održavanje radionice.
	 * 
	 * param oprema oprema potrebna za održavanje radionice
	 */
	public void setOprema(Set<String> oprema) {
		this.oprema = new LinkedHashSet<String>(oprema);
	}

	/**
	 * Postavlja vrijednost trajanja radionice.
	 * 
	 * @param trajanje
	 *            trajanje radionice
	 */
	public void setTrajanje(String trajanje) {
		this.trajanje = trajanje;
	}

	/**
	 * Postavlja vrijednosti tipova publike koja može sudjelovati u radionici.
	 * 
	 * @param publika
	 *            tipovi publike koji mogu sudjelovati u radionici
	 */
	public void setPublika(Set<String> publika) {
		this.publika = publika;
	}

	/**
	 * Postavlja vrijednost maksimalnog broja polaznika koji mogu sudjelovati
	 * radionici.
	 * 
	 * @param maksPolaznika
	 *            maksimalni broj polaznika koji mogu sudjelovati u radionici
	 */
	public void setMaksPolaznika(String maksPolaznika) {
		this.maksPolaznika = maksPolaznika;
	}

	/**
	 * Postavlja vrijednost emaila voditelja radionice.
	 * 
	 * @param email
	 *            email voditelja radionice
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Postavlja vrijednost dodatnih uputa o radionici.
	 * 
	 * @param dopuna
	 *            dodatne upute o radionici
	 */
	public void setDopuna(String dopuna) {
		this.dopuna = dopuna;
	}
}
