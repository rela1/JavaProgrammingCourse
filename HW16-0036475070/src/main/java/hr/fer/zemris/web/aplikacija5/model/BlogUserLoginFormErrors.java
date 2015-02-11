package hr.fer.zemris.web.aplikacija5.model;

/**
 * Razred predstavlja konkretnu implementaciju za pohranjivanje informacija o
 * samoj formi i o pogreškama u formi za login.
 * 
 * @author Ivan Relić
 * 
 */
public class BlogUserLoginFormErrors extends BlogFormErrors {

	private String nickname;
	private String password;

	/**
	 * Konstruktor. Inicijalizira mapu za pohranjivanje pogrešaka.
	 */
	public BlogUserLoginFormErrors(String nickname, String password) {
		super();
		this.password = password;
		this.nickname = nickname;
	}

	/**
	 * Vraća lozinku korisnika.
	 * 
	 * @return lozinka korisnika
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Vraća nadimak korisnika
	 * 
	 * @return nadimak korisnika
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Postavlja lozinku korisnika.
	 * 
	 * @param password
	 *            lozinka korisnika
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Postavlja nadimak korisnika.
	 * 
	 * @param nadimak
	 *            korisnika
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
