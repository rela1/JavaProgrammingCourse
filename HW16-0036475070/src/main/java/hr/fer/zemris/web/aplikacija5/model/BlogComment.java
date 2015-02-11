package hr.fer.zemris.web.aplikacija5.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "blog_comments")
/**
 * Predstavlja jedan komentar jednog zapisa na blogu.
 * 
 * @author Ivan Relić
 *
 */
public class BlogComment {

	private Long id;
	private BlogEntry blogEntry;
	private String usersEMail;
	private String message;
	private Date postedOn;

	@Id
	@GeneratedValue
	/**
	 * Dohvaća id komentara zapisa bloga.
	 * 
	 * @return id komentara zapisa bloga
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Postavlja id komentara zapisa bloga
	 * 
	 * @param id
	 *            id koji želimo postaviti komentaru zapisa bloga
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	/**
	 * Vraća blog entry kojem ovaj komentar pripada.
	 * 
	 * @return blog entry vezan uz ovaj komentar
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Postavlja blog entry kojem ovaj komentar pripad.
	 * 
	 * @param blogEntry
	 *            blog entry kojem ovaj komentar pripada
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	@Column(length = 100, nullable = false)
	/**
	 * Vraća email usera koji je kreirao ovaj komentar.
	 * 
	 * @return email usera koji je kreirao komentar
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Postavlja email usera koji je kreirao ovaj komentar.
	 * 
	 * @param usersEMail
	 *            email usera koji je kreirao ovaj komentar
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	@Column(length = 4096, nullable = false)
	/**
	 * Dohvaća poruku komentara.
	 * 
	 * @return poruka komentara
	 */
	public String getMessage() {
		return message;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	/**
	 * Vraća datum i vrijeme kada je komentar kreiran.
	 * 
	 * @return timestamp kreiranja komentara
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Postavlja datum i vrijeme kada je komentar kreiran.
	 * 
	 * @param postedOn
	 *            timestamp kreiranja komentara
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}