package hr.fer.zemris.web.aplikacija5.dao;

import java.util.List;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;

/**
 * Sučelje za pristup podacima iz perzistencijskog sloja.
 * 
 * @author Ivan Relić
 * 
 */
public interface DAO {

	/**
	 * Dohvaća listu svih registriranih korisnika. Ako nema nijednog
	 * registriranog korisnika, vraća null.
	 * 
	 * @return lista svih registriranih korisnika; null ako nema nijednog
	 *         registriranog korisnika
	 */
	public List<BlogUser> getRegisteredUsers() throws DAOException;

	/**
	 * Dohvaća podatke o korisniku pod traženim nickom. Ako korisnik ne postoji,
	 * vraća null.
	 * 
	 * @param nick
	 *            nick korisnika čije podatke želimo
	 * @return podaci o korisniku čiji nick je predan; null ako korisnik ne
	 *         postoji
	 */
	public BlogUser getBlogUser(String nick) throws DAOException;

	/**
	 * Dodaje zapis bloga u bazu podataka.
	 * 
	 * @param entry
	 *            zapis bloga koji se dodaje
	 */
	public void addBlogEntry(BlogEntry entry);

	/**
	 * Dodaje novog korisnika bloga u bazu.
	 * 
	 * @param user
	 *            korisnik koji se dodaje
	 */
	public void addBlogUser(BlogUser user) throws DAOException;

	/**
	 * Dohvaća listu svih blog zapisa za traženog korisnika. Ako korisnik nema
	 * nijedan blog zapis, vraća null.
	 * 
	 * @param creatorNick
	 *            nick korisnika čije blogove tražimo
	 * @return lista svih blog zapisa za traženog korisnika; null ako korisnik
	 *         nema napisan nijedan blog zapis
	 */
	public List<BlogEntry> getUserBlogEntries(String nick) throws DAOException;

	/**
	 * Dohvaća određeni blog entry po ID-u korisnika i ID-u zapisa bloga.
	 * 
	 * @param nick
	 *            nick korisnika čiji zapis bloga želimo
	 * @param blogEntryId
	 *            ID zapisa bloga koji želimo
	 * @return traženi zapis bloga; null ako traženi zapis ne postoji
	 */
	public BlogEntry getUserBlogEntry(String nick, Long blogEntryId)
			throws DAOException;

	/**
	 * Entryju s predanim id-om updatea naslov i sadržaj na predani, te također
	 * i timestamp zadnje modifikacije na trenutni.
	 * 
	 * @param blogEntryId
	 *            id entryja koji se modificira
	 * @param title
	 *            novi naslov entryja
	 * @param text
	 *            novi tekst entryja
	 * @return blog entry koji je updatean; null ako ništa nije updateano
	 */
	public BlogEntry updateBlogEntry(Long blogEntryId, String title, String text);

	/**
	 * Entryju s predanim id-om dodaje komentar s predanim emailom i predanim
	 * sadržajem.
	 * 
	 * @param blogEntryId
	 *            id entryja kojem se dodaje komentar
	 * @param email
	 *            email vlasnika komentara
	 * @param message
	 *            sadržaj komentara
	 * @return vraća entry kojem je komentar dodan ili null ako dodavanje nije
	 *         uspjelo
	 */
	public BlogEntry addComment(Long blogEntryId, String email, String message);

	/**
	 * Dohvaća zapis bloga s predanim id-om.
	 * 
	 * @param blogEntryId
	 *            id traženog entryja
	 * @return vraća traženi entry ili null ako traženi entry ne postoji
	 */
	public BlogEntry getBlogEntry(Long blogEntryId);
}