package hr.fer.zemris.web.ankete.dao;

import hr.fer.zemris.web.ankete.Poll;
import hr.fer.zemris.web.ankete.PollEntry;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author Ivan Relić
 * 
 */
public interface DAO {

	/**
	 * Dohvaća sve postojeće ankete iz baze.
	 * 
	 * @return lista anketi iz baze
	 * @throws DAOException
	 */
	public List<Poll> getPolls() throws DAOException;

	/**
	 * Dohvaća sve postojeće unose opcija u bazi za traženu anketu.
	 * 
	 * @param pollID
	 *            id ankete čije unose želimo dohvatiti
	 * @return lista unosa za pojedinu anketu
	 * @throws DAOException
	 *             u slučaju pogreške
	 */
	public List<PollEntry> getPollEntries(Long pollID) throws DAOException;

	/**
	 * Updatea jedan unos jedne ankete tako da mu poveća broj glasova za 1.
	 * 
	 * @param pollID
	 *            ID ankete
	 * @param pollEntryID
	 *            ID unosa ankete
	 * @throws DAOException
	 *             u slučaju pogreške
	 */
	public void updatePollEntry(Long pollID, Long pollEntryID)
			throws DAOException;

	/**
	 * Vraća podatke o anketi za traženi ID ankete. 
	 * 
	 * @param pollID
	 *            id ankete čije informacije želimo
	 * @return zapis o anketi
	 * @throws DAOException
	 *             u slučaju pogreške
	 */
	public Poll getPollInfo(Long pollID) throws DAOException;

	/**
	 * Kreira novu anketu s predanim naslovom, porukom ankete i listom PollEntry
	 * unosa.
	 * 
	 * @param pollTitle
	 *            naslov ankete
	 * @param pollMessage
	 *            poruka ankete
	 * @param pollEntries
	 *            mogući odabiri za željenu anketu
	 */
	public void createNewPoll(String pollTitle, String pollMessage,
			List<PollEntry> entries);
}