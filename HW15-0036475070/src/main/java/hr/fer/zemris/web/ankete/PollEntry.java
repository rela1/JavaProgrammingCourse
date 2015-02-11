package hr.fer.zemris.web.ankete;

/**
 * Modelira jedan unos ankete određen sa ID-om, naslovom opcije, popratnim
 * linkom uz opciju te brojem glasova za tu opciju.
 * 
 * @author Ivan Relić
 * 
 */
public class PollEntry implements Comparable<PollEntry> {

	private Long ID;
	private String optionTitle;
	private String optionLink;
	private Long votesCount;

	/**
	 * Konstruktor. Prima ID opcije ankete, naslov opcije, link na dodatni
	 * sadržaj opcije i broj trenutnih glasova opcije. ID nije obavezan (može
	 * biti null).
	 * 
	 * @param ID
	 *            long ID
	 * @param optionTitle
	 *            naslov opcije
	 * @param optionLink
	 *            link s dodatnim sadržajem opcije
	 * @param votesCount
	 *            trenutni broj glasova opcije
	 */
	public PollEntry(Long ID, String optionTitle, String optionLink,
			Long votesCount) {
		if (optionTitle == null || optionLink == null || votesCount == null) {
			throw new IllegalArgumentException(
					"Option title, option link and votes count must be specified!");
		}
		this.ID = ID;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.votesCount = votesCount;
	}

	/**
	 * Unosi anketa se moraju sortirati po broju glasova, silazno.
	 */
	public int compareTo(PollEntry o) {
		return -votesCount.compareTo(o.votesCount);
	}

	/**
	 * Vraća ID unosa ankete.
	 * 
	 * @return ID unosa ankete
	 */
	public Long getID() {
		return ID;
	}

	/**
	 * Vraća naslov opcije ankete.
	 * 
	 * @return naslov opcije ankete
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Vraća link na dodatni sadržaj opcije ankete.
	 * 
	 * @return link na dodatni sadržaj opcije ankete
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Vraća trenutni broj glasova opcije ankete.
	 * 
	 * @return broj glasova opcije ankete
	 */
	public Long getVotesCount() {
		return votesCount;
	}
}
