package hr.fer.zemris.web.aplikacija5.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "blog_entries")
@Cacheable(true)
/**
 * Razred predstavlja jedan zapis na blogu.
 * 
 * @author Ivan Relić
 *
 */
public class BlogEntry {

	private Long id;
	private List<BlogComment> comments = new ArrayList<>();
	private Date createdAt;
	private Date lastModifiedAt;
	private String title;
	private String text;
	private BlogUser creator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	/**
	 * Vraća korisnika koji je kreirao zapis bloga.
	 * 
	 * @return korisnik koji je kreirao zapis bloga
	 */
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Postavlja korisnika koji je kreirao ovaj zapis bloga.
	 * 
	 * @param creator
	 *            korisnik koji je kreirao ovaj zapis bloga
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	@Id
	@GeneratedValue
	/**
	 * Dohvaća id zapisa bloga.
	 * 
	 * @return id zapisa bloga
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Postavlja id zapisa bloga
	 * 
	 * @param id
	 *            id koji želimo postaviti zapisu
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	/**
	 * Vraća listu komentara koji su pridruženi ovom blog zapisu.
	 * 
	 * @return lista komentara pridruženih ovom blog zapisu
	 */
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Postavlja listu komentara za ovaj blog zapis.
	 * 
	 * @param comments
	 *            lista komentara koje želimo postaviti uz ovaj blog zapis
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	/**
	 * Dohvaća datum i vrijeme kada je ovaj blog zapis kreiran.
	 * 
	 * @return timestamp kreiranja ovog zapisa bloga
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Postavlja datum i vrijeme kada je ovaj blog zapis kreiran.
	 * 
	 * @param createdAt
	 *            timestamp kreiranja ovog blog zapisa
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	/**
	 * Vraća datum i vrijeme zadnje modifikacije ovog blog zapisa.
	 * 
	 * @return timestamp zadnje modifikacije ovog blog zapisa
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Postavlja datum i vrijeme zadnje modifikacije ovog blog zapisa.
	 * 
	 * @param lastModifiedAt
	 *            timestamp zadnje modifikacije ovog blog zapisa
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	@Column(length = 200, nullable = false)
	/**
	 * Vraća naslov ovog blog zapisa.
	 * 
	 * @return naslov ovog blog zapisa
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Postavlja naslov ovog blog zapisa
	 * 
	 * @param title
	 *            naslov ovog blog zapisa
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length = 4096, nullable = false)
	/**
	 * Vraća tekst ovog blog zapisa.
	 * 
	 * @return tekst ovog blog zapisa
	 */
	public String getText() {
		return text;
	}

	/**
	 * Postavlja tekst ovog blog zapisa.
	 * 
	 * @param text
	 *            tekst ovog blog zapisa
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}