package hr.fer.zemris.web.aplikacija5.model;

import hr.fer.zemris.web.aplikacija5.servlets.SaveUser;

/**
 * Razred predstavlja konkretnu implementaciju za pohranjivanje informacija o
 * samoj formi i o pogreškama u formi za kreiranje komentara zapisa blogova.
 * 
 * @author Ivan Relić
 * 
 */
public class BlogCommentFormErrors extends BlogFormErrors {

	private String email;
	private String message;

	/**
	 * Inicijalizira mapu za pogreške i preuzima podatke o komentaru.
	 * 
	 * @param email
	 *            email korisnika koji ostavlja komentar
	 * @param message
	 *            poruka komentara korisnika
	 */
	public BlogCommentFormErrors(String email, String message) {
		super();
		this.email = getPreparedValue(email);
		this.message = getPreparedValue(message);
	}

	/**
	 * Vraća email korisnika.
	 * 
	 * @return email korisnika
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Vraća poruku komentara.
	 * 
	 * @return poruka komentara
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Postavlja email korisnika.
	 * 
	 * @param email
	 *            email korisnika
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Postavlja poruku komentara.
	 * 
	 * @param message
	 *            poruka komentara
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Validira unos forme za komentar zapisa bloga - provjerava ima li
	 * pogrešaka i pohranjuje ih ako ih ima.
	 */
	public void validate() {
		if (email.isEmpty()) {
			setError("email", "E-Mail mora biti definiran!");
		} else if (email.length() > 100) {
			setError("email", "Duljina E-Maila mora biti manja od 100!");
		} else if (!email.matches(SaveUser.EMAIL_REGEX)) {
			setError("email", "Neispravan E-Mail!");
		}
		if (message.isEmpty()) {
			setError("message", "Poruka komentara mora biti definirana!");
		} else if (message.length() > 4096) {
			setError("message", "Poruka ne smije biti dulja od 4096 znakova!");
		}
	}
}
