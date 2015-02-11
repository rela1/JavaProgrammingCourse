package hr.fer.zemris.web.aplikacija5.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "blog_users")
/**
 * Razred predstavlja jednog korisnika na blogu.
 * 
 * @author Ivan Relić
 *
 */
public class BlogUser {

	private Long id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String passwordHash;
	private List<BlogEntry> blogEntries = new ArrayList<>();

	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	/**
	 * Vraća sve blog zapise ovog korisnika.
	 * 
	 * @return blog zapisi ovog korisnika
	 */
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	/**
	 * Postavlja blog zapise ovom korisniku.
	 * 
	 * @param blogEntries
	 *            blog zapisi za postavljanje korisniku
	 */
	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	@Id
	@GeneratedValue
	/**
	 * Vraća ID korisnika bloga
	 * 
	 * @return ID korisnika bloga
	 */
	public Long getId() {
		return id;
	}

	@Column(length = 30, nullable = false)
	/**
	 * Vraća ime korisnika bloga.
	 * 
	 * @return ime korisnika bloga
	 */
	public String getFirstName() {
		return firstName;
	}

	@Column(length = 40, nullable = false)
	/**
	 * Vraća prezime korisnika bloga.
	 * 
	 * @return prezime korisnika bloga
	 */
	public String getLastName() {
		return lastName;
	}

	@Column(length = 30, unique = true)
	/**
	 * Vraća nick korisnika bloga.
	 * 
	 * @return nick korisnika bloga
	 */
	public String getNick() {
		return nick;
	}

	@Column(length = 100, nullable = false)
	/**
	 * Vraća email korisnika bloga.
	 * 
	 * @return email korisnika bloga
	 */
	public String getEmail() {
		return email;
	}

	@Column(length = 40, nullable = false)
	/**
	 * Vraća password hash korisnika bloga.
	 * 
	 * @return password hash korisnika bloga
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Postavlja ID korisnika bloga.
	 * 
	 * @param id
	 *            id korisnika bloga
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Postavlja ime korisnika bloga.
	 * 
	 * @param firstName
	 *            ime korisnika bloga
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Postavlja prezime korisnika bloga
	 * 
	 * @param lastName
	 *            prezime korisnika bloga
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Postavlja nick korisnika bloga.
	 * 
	 * @param nick
	 *            nick korisnika bloga
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Postavlja email korisnika bloga.
	 * 
	 * @param email
	 *            email korisnika bloga
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Postavlja password hash korisnika bloga.
	 * 
	 * @param passwordHash
	 *            password hash korisnika bloga
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
