package hr.fer.zemris.web.aplikacija5.model;

/**
 * Razred predstavlja konkretnu implementaciju za pohranjivanje informacija o
 * samoj formi i o pogreškama u formi za kreiranje/uređivanje zapisa blogova.
 * 
 * @author Ivan Relić
 * 
 */
public class BlogEntryFormErrors extends BlogFormErrors {

	private String title;
	private String text;
	private String id;

	/**
	 * Konstruktor. Inicijalizira mapu za pohranivanje pogrešaka i pohranjuje
	 * naslov i tekst zapisa bloga.
	 * 
	 * @param title
	 *            naslov zapisa bloga
	 * @param text
	 *            tekst zapisa bloga
	 * @param id
	 *            identifikator zapisa bloga
	 */
	public BlogEntryFormErrors(String title, String text, String id) {
		super();
		this.title = getPreparedValue(title);
		this.text = getPreparedValue(text);
		this.id = getPreparedValue(id);
	}

	/**
	 * Dohvaća naslov zapisa bloga.
	 * 
	 * @return naslov zapisa bloga
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Dohvaća tekst zapisa bloga
	 * 
	 * @return tekst zapisa bloga
	 */
	public String getText() {
		return text;
	}

	/**
	 * Postavlja naslov zapisa bloga
	 * 
	 * @param title
	 *            naslov zapisa bloga
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Postavlja tekst zapisa bloga.
	 * 
	 * @param text
	 *            tekst zapisa bloga
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Vraća identifikator zapisa bloga.
	 * 
	 * @return identifikator zapisa bloga
	 */
	public String getId() {
		return id;
	}

	/**
	 * Vraća indentifikator zapisa bloga u Long formatu.
	 * 
	 * @return indetifikator zapisa u Long formatu
	 */
	public Long getLongId() {
		return id.length() == 0 ? null : Long.parseLong(id);
	}

	/**
	 * Postavlja identifikator zapisa bloga.
	 * 
	 * @param id
	 *            identifikator zapisa bloga
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Validira unos forme za zapis bloga - provjerava ima li pogrešaka i
	 * pohranjuje ih ako ih ima.
	 */
	public void validate() {
		if (this.text.isEmpty()) {
			setError("text", "Tekst sadržaja zapisa bloga mora biti definiran!");
		} else if (this.text.length() > 4096) {
			setError("text",
					"Maksimalna duljina teksta za zapis bloga je 4096 znakova!");
		}
		if (this.title.isEmpty()) {
			setError("title", "Naslov zapisa bloga mora biti definiran!");
		} else if (this.title.length() > 200) {
			setError("title",
					"Maksimalna duljina teksta za naslov bloga je 200 znakova!");
		}
	}
}
