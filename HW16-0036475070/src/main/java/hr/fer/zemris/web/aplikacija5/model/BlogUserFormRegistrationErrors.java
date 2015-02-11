package hr.fer.zemris.web.aplikacija5.model;

/**
 * Razred predstavlja konkretnu implementaciju za pohranjivanje informacija o
 * samoj formi i o pogreškama u formi za registarciju.
 * 
 * @author Ivan Relić
 * 
 */
public class BlogUserFormRegistrationErrors extends BlogFormErrors {

	private String firstName;
	private String lastName;
	private String email;
	private String nickname;
	private String password;

	/**
	 * Konstruktor. Inicijalizira mapu za pohranjivanje pogrešaka i pohranjuje
	 * potrebne podatke o korisniku.
	 */
	public BlogUserFormRegistrationErrors(String firstName, String lastName,
			String email, String nickname, String password) {
		super();
		this.firstName = getPreparedValue(firstName);
		this.lastName = getPreparedValue(lastName);
		this.email = getPreparedValue(email);
		this.nickname = nickname;
		this.password = password;
	}

	/**
	 * Vraća ime korisnika.
	 * 
	 * @return ime korisnika
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Vraća prezime korisnika
	 * 
	 * @return prezime korisnika
	 */
	public String getLastName() {
		return lastName;
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

	/**
	 * Postavlja ime korisnika.
	 * 
	 * @param ime
	 *            korisnika
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Postavlja prezime korisnika.
	 * 
	 * @param prezime
	 *            korisnika
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Postavlja email korisnika.
	 * 
	 * @param email
	 *            korisnika
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
