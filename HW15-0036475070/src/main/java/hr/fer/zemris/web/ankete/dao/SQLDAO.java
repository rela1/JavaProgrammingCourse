package hr.fer.zemris.web.ankete.dao;

import hr.fer.zemris.web.ankete.Poll;
import hr.fer.zemris.web.ankete.PollEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija DAO sustava za perzistenciju podataka preko SQL baze podataka.
 * 
 * @author Ivan Relić
 * 
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollEntry> getPollEntries(Long pollID) throws DAOException {
		List<PollEntry> entries = new ArrayList<PollEntry>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			// napravljeno je lijepljenje jer nam korisnik sigurno ništa ne može
			// podvaliti kroz long objekt, može predati samo i jedino broj
			pst = con
					.prepareStatement("SELECT id, optionTitle, optionLink, votesCount FROM PollOptions WHERE pollID="
							+ pollID.toString());
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollEntry entry = new PollEntry(rs.getLong(1),
								rs.getString(2), rs.getString(3), rs.getLong(4));
						entries.add(entry);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException(
					"Pogreška prilikom dohvata liste unosa ankete!", ex);
		}
		return entries;
	}

	@Override
	public void updatePollEntry(Long pollID, Long pollEntryID)
			throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		Statement sta = null;
		try {
			// kreiraj statement koji može updateati retke
			sta = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			try {
				// query se izvodi također lijepljenjem jer nam korisnik ne može
				// podvaliti ništa preko long objekta
				ResultSet rs = sta
						.executeQuery("SELECT votesCount FROM PollOptions WHERE pollID="
								+ pollID.toString()
								+ "AND id="
								+ pollEntryID.toString());
				if (rs != null && rs.next()) {
					try {
						long votesCount = rs.getLong(1);
						rs.updateLong("votesCount", votesCount + 1);
						rs.updateRow();
					} finally {
						try {
							rs.close();
						} catch (Exception ignorable) {
						}
					}
				}
			} finally {
				try {
					sta.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException(
					"Pogreška prilikom povećavanja broja glasova!", ex);
		}
	}

	@Override
	public Poll getPollInfo(Long pollID) throws DAOException {
		Poll poll = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			// napravljeno je lijepljenje jer nam korisnik sigurno ništa ne može
			// podvaliti kroz long objekt, može predati samo i jedino broj
			pst = con
					.prepareStatement("SELECT title, message FROM Polls WHERE id="
							+ pollID.toString());
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						poll = new Poll(pollID, rs.getString(1),
								rs.getString(2));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException(
					"Pogreška prilikom dohvata informacija o anketi!", ex);
		}
		return poll;
	}

	@Override
	public void createNewPoll(String pollTitle, String pollMessage,
			List<PollEntry> entries) {
		Connection con = SQLConnectionProvider.getConnection();
		// prva naredba će biti za dodavanje opisa ankete u tablici Polls, a
		// drugom će se dodati svi zapisi u PollOptions tablicu
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		try {
			pst1 = con.prepareStatement(
					"INSERT INTO Polls (title, message) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst1.setString(1, pollTitle);
			pst1.setString(2, pollMessage);
			pst1.executeUpdate();
			long generatedPollID = 0;
			try {
				// dohvati ID koji je dodijeljen ubačenoj anketi
				ResultSet rs = pst1.getGeneratedKeys();
				try {
					if (rs != null && rs.next()) {
						generatedPollID = rs.getLong(1);
					} else {
						// ako nema rezultata, ID ne smije ostati nula, unošenje
						// opcija ankete se ne smije nastaviti
						throw new DAOException(
								"Pogreška prilikom kreiranja ankete!");
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
				// pripremi insert naredbu za PollOptions tablicu i za svaki od
				// primljenih zapisa ubaci ih referencirajući se na dobiveni
				// pollID prilikom ubacivanja u Polls tablicu
				pst2 = con
						.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)");
				for (PollEntry entry : entries) {
					pst2.setString(1, entry.getOptionTitle());
					pst2.setString(2, entry.getOptionLink());
					pst2.setLong(3, generatedPollID);
					pst2.setLong(4, entry.getVotesCount().longValue());
					pst2.executeUpdate();
				}
			} finally {
				try {
					pst1.close();
					pst2.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom kreiranja ankete!", ex);
		}
	}

	@Override
	public List<Poll> getPolls() throws DAOException {
		List<Poll> polls = new ArrayList<Poll>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, title, message FROM Polls");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						Poll poll = new Poll(rs.getLong(1), rs.getString(2),
								rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste anketa!",
					ex);
		}
		return polls;
	}
}
