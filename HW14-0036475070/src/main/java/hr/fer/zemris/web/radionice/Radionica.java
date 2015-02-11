package hr.fer.zemris.web.radionice;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Razred modelira zapis o jednoj radionici.
 * 
 * @author Ivan Relić
 * 
 */
public class Radionica implements Comparable<Radionica> {

	private Long id;
	private String naziv;
	private String datum;
	private Set<Opcija> oprema;
	private Opcija trajanje;
	private Set<Opcija> publika;
	private Integer maksPolaznika;
	private String email;
	private String dopuna;

	/**
	 * Konstruktor. Kreira novi zapis o radionici iz primljenih parametara.
	 * 
	 * @param id
	 *            id radionice
	 * @param naziv
	 *            naziv radionice
	 * @param datum
	 *            datum održavanja radionice
	 * @param oprema
	 *            oprema potrebna za održavanje radionice
	 * @param trajanje
	 *            trajanje radionice
	 * @param publika
	 *            prihvatljiva publika za radionicu
	 * @param maksPolaznika
	 *            broj maksimalnih polaznika radionice
	 * @param email
	 *            email voditelja radionice
	 * @param dopuna
	 *            dopuna opisa radionice
	 */
	public Radionica(Long id, String naziv, String datum, Set<Opcija> oprema,
			Opcija trajanje, Set<Opcija> publika, Integer maksPolaznika,
			String email, String dopuna) {
		this.id = id;
		this.naziv = naziv.trim();
		this.datum = datum.trim();
		this.oprema = new LinkedHashSet<Opcija>(oprema);
		this.trajanje = trajanje;
		this.publika = new LinkedHashSet<Opcija>(publika);
		this.maksPolaznika = maksPolaznika;
		this.email = email.trim();
		this.dopuna = dopuna.trim();
	}

	/**
	 * Konstruktor. Kreira praznu radionicu sa svim atributima nedefiniranima.
	 */
	public Radionica() {
		id = null;
		naziv = "";
		datum = "";
		oprema = new LinkedHashSet<Opcija>();
		trajanje = null;
		publika = new LinkedHashSet<Opcija>();
		maksPolaznika = null;
		email = "";
		dopuna = "";
	}

	/**
	 * Dohvaća vrijednost id-a radionice.
	 * 
	 * @return id radionice
	 */
	public Long getId() {
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
	public Set<Opcija> getOprema() {
		return oprema;
	}

	/**
	 * Dohvaća vrijednost trajanja radionice.
	 * 
	 * @return trajanje radionice
	 */
	public Opcija getTrajanje() {
		return trajanje;
	}

	/**
	 * Dohvaća vrijednosti tipova publike koja može sudjelovati u radionici.
	 * 
	 * @return tipovi publike koji mogu sudjelovati u radionici
	 */
	public Set<Opcija> getPublika() {
		return publika;
	}

	/**
	 * Dohvaća vrijednost maksimalnog broja polaznika koji mogu sudjelovati
	 * radionici.
	 * 
	 * @return maksimalni broj polaznika koji mogu sudjelovati u radionici
	 */
	public Integer getMaksPolaznika() {
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
	public void setId(Long id) {
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
	public void setOprema(Set<Opcija> oprema) {
		this.oprema = new LinkedHashSet<Opcija>(oprema);
	}

	/**
	 * Postavlja vrijednost trajanja radionice.
	 * 
	 * @param trajanje
	 *            trajanje radionice
	 */
	public void setTrajanje(Opcija trajanje) {
		this.trajanje = trajanje;
	}

	/**
	 * Postavlja vrijednosti tipova publike koja može sudjelovati u radionici.
	 * 
	 * @param publika
	 *            tipovi publike koji mogu sudjelovati u radionici
	 */
	public void setPublika(Set<Opcija> publika) {
		this.publika = publika;
	}

	/**
	 * Postavlja vrijednost maksimalnog broja polaznika koji mogu sudjelovati
	 * radionici.
	 * 
	 * @param maksPolaznika
	 *            maksimalni broj polaznika koji mogu sudjelovati u radionici
	 */
	public void setMaksPolaznika(Integer maksPolaznika) {
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

	@Override
	public int compareTo(Radionica o) {
		if (this.id == null) {
			if (o.id == null) {
				return 0;
			}
			return -1;
		} else if (o.id == null) {
			return 1;
		}
		return this.id.compareTo(o.id);
	}
}
