package hr.fer.zemris.web.radionice;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelira korisnika web-aplikacije Radionice.
 * 
 * @author Ivan Relić
 * 
 */
public class User {

	private String login;
	private String zaporka;
	private String ime;
	private String prezime;
	Map<String, String> greske;

	/**
	 * Konstruktor.
	 * 
	 * @param login
	 *            korisničko ime korisnika
	 * @param zaporka
	 *            zaporka korisnika
	 * @param ime
	 *            ime korisnika
	 * @param prezime
	 *            prezime korisnika
	 */
	public User(String login, String zaporka, String ime, String prezime) {
		super();
		this.login = login;
		this.zaporka = zaporka;
		this.ime = ime;
		this.prezime = prezime;
		greske = new HashMap<String, String>();
	}

	/**
	 * @return vraća vrijednost korisničkog imena
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return vraća vrijednost zaporke
	 */
	public String getZaporka() {
		return zaporka;
	}

	/**
	 * @return vraća vrijednost imena
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * @return vraća vrijednost prezimena
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * @param login
	 *            korisničko ime koje se želi postaviti
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @param zaporka
	 *            zaporka koja se želi postaviti
	 */
	public void setZaporka(String zaporka) {
		this.zaporka = zaporka;
	}

	/**
	 * @param ime
	 *            ime koje se želi postaviti
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * @param prezime
	 *            prezime koje se želi postaviti
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
}
