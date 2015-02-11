package hr.fer.zemris.web.aplikacija5.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import hr.fer.zemris.web.aplikacija5.dao.DAO;
import hr.fer.zemris.web.aplikacija5.dao.DAOException;
import hr.fer.zemris.web.aplikacija5.model.BlogComment;
import hr.fer.zemris.web.aplikacija5.model.BlogEntry;
import hr.fer.zemris.web.aplikacija5.model.BlogUser;

/**
 * Implementacija DAO sučelja kroz pristup perzistencijskom paketu preko Java
 * Persistance API-ja i EntityManagera.
 * 
 * @author Ivan Relić
 * 
 */
public class JPADAOImpl implements DAO {

	@Override
	public List<BlogUser> getRegisteredUsers() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		Query query = em.createQuery("SELECT u FROM BlogUser u");
		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) query.getResultList();
		if (users != null && users.size() == 0) {
			return null;
		}
		return users;
	}

	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		Query query = em.createQuery(
				"SELECT u FROM BlogUser u WHERE u.nick=:userNick")
				.setParameter("userNick", nick);
		// getSingleResult baca exception u slučaju da ne pronađe
		// zadovoljavajući objekt
		BlogUser user;
		try {
			user = (BlogUser) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return user;
	}

	@Override
	public void addBlogUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
	}

	@Override
	public void addBlogEntry(BlogEntry entry) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(entry);
		BlogUser user = em.find(BlogUser.class, entry.getCreator().getId());
		user.getBlogEntries().add(entry);
	}

	@Override
	public List<BlogEntry> getUserBlogEntries(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		Query query = em.createQuery(
				"SELECT e FROM BlogEntry e WHERE e.creator.nick=:creatorNick")
				.setParameter("creatorNick", nick);
		@SuppressWarnings("unchecked")
		List<BlogEntry> entries = (List<BlogEntry>) query.getResultList();
		if (entries != null && entries.size() == 0) {
			return null;
		}
		return entries;
	}

	@Override
	public BlogEntry getUserBlogEntry(String nick, Long blogEntryId)
			throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		Query query = em
				.createQuery(
						"SELECT e FROM BlogEntry e WHERE e.creator.nick=:userNick AND e.id=:blogEntryID")
				.setParameter("userNick", nick)
				.setParameter("blogEntryID", blogEntryId);
		// getSingleResult baca exception u slučaju da ne pronađe
		// zadovoljavajući objekt
		BlogEntry entry;
		try {
			entry = (BlogEntry) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return entry;
	}

	@Override
	public BlogEntry updateBlogEntry(Long blogEntryId, String title, String text) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryId);
		if (blogEntry == null) {
			return null;
		}
		blogEntry.setLastModifiedAt(new Date());
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		return blogEntry;
	}

	@Override
	public BlogEntry addComment(Long blogEntryId, String email, String message) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryId);
		if (blogEntry == null) {
			return null;
		}
		BlogComment comment = new BlogComment();
		comment.setBlogEntry(blogEntry);
		comment.setMessage(message);
		comment.setPostedOn(new Date());
		comment.setUsersEMail(email);
		em.persist(comment);
		blogEntry.getComments().add(comment);
		return blogEntry;
	}

	@Override
	public BlogEntry getBlogEntry(Long blogEntryId) {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class,
				blogEntryId);
	}
}