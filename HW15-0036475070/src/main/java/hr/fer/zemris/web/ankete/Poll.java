package hr.fer.zemris.web.ankete;

/**
 * Modelira jednu anketu sa svim informacijama vezanima uz nju:ID, naslov ankete
 * i opis ankete.
 * 
 * @author Ivan Relić
 * 
 */
public class Poll implements Comparable<Poll> {

	private Long ID;
	private String title;
	private String message;

	/**
	 * Konstruktor. Prima ID ankete, naslov ankete i poruku ankete i postavlja
	 * ih. ID nije obavezan (može biti null).
	 * 
	 * @param ID
	 * @param title
	 * @param message
	 */
	public Poll(Long ID, String title, String message) {
		if (title == null || message == null) {
			throw new IllegalArgumentException(
					"Title and message must be specified!");
		}
		this.ID = ID;
		this.title = title;
		this.message = message;
	}

	/**
	 * Dohvaća ID ankete.
	 * 
	 * @return ID ankete
	 */
	public Long getID() {
		return ID;
	}

	/**
	 * Dohvaća poruku opisa ankete.
	 * 
	 * @return poruka opisa ankete
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Dohvaća naslov ankete.
	 * 
	 * @return naslov ankete
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public int compareTo(Poll o) {
		return title.compareTo(o.title);
	}
}
