package hr.fer.zemris.java.hw13.servleti.glasanje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Obrađuje podatke iz filea s glasovima i prikazuje rezultate.
 * 
 * @author Ivan Relić
 * 
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static int maxVotes;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// pročitaj izvođače
		String artistsFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		String votesFileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		File votes = new File(votesFileName);
		File artistsFile = new File(artistsFileName);
		List<String> artists = GlasanjeRezultatiServlet.readFile(artistsFile);
		// ako je došlo do greške iz čitanja file
		if (artists == null) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
		// ako file ne postoji
		if (!votes.exists()) {
			// dohvati broj izvođača preko definicijske datoteke
			int numberOfArtists = artists.size();
			// stvori file
			try {
				createFile(numberOfArtists, votes);
			} catch (Exception e) {
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
						.forward(req, resp);
				return;
			}
		}
		// nakon kreiranja filea ako ne postoji, kreiraj mapu (izvođač, glasovi)
		// i mapu (izvođač, pjesma) za izvođače s najvećim brojem glasova
		try {
			req.setAttribute("voteMap", createVoteMap(votes, artists));
			req.setAttribute("maxVotes", createMaxVotesMap(votes, artists));
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(
					req, resp);
			return;
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeFileError.jsp")
					.forward(req, resp);
			return;
		}
	}

	/**
	 * Čita primljeni file, ako dođe do greške, vraća null.
	 * 
	 * @param file
	 *            file za čitanje
	 * @param req
	 *            request
	 * @param resp
	 *            response
	 * @return null ako je došlo do greške, linije filea inače
	 */
	public static List<String> readFile(File file) {
		List<String> lines;
		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			return null;
		}
		return lines;
	}

	/**
	 * Kreira mapu s izvođačima i pjesmama koji imaju maksimalan broj glasova.
	 * 
	 * @param votes
	 *            file koji sadrži glasove
	 * @param artists
	 *            lista izvođača
	 * @return mapa izvođača i pjesama s maksimalnim brojem glasova
	 * @throws IOException
	 */
	private Map<String, String> createMaxVotesMap(File votes,
			List<String> artists) throws IOException {
		Map<String, String> maxVotesList = new HashMap<String, String>();
		List<String> votesList = Files.readAllLines(votes.toPath(),
				StandardCharsets.UTF_8);
		for (String s : votesList) {
			Scanner sc = new Scanner(s);
			String id = sc.next();
			Integer numberOfVotes = Integer.parseInt(sc.next());
			sc.close();
			// za svakog izvođača s maksimalnim brojem glasova dohvati ime
			// izvođača i link do pjesme i to pohrani u mapu
			if (numberOfVotes.intValue() == maxVotes) {
				sc = new Scanner(artists.get(Integer.parseInt(id) - 1));
				sc.useDelimiter("\t");
				sc.next();
				String artist = sc.next();
				String song = sc.next();
				maxVotesList.put(artist, song);
				sc.close();
			}
		}
		return maxVotesList;
	}

	/**
	 * Kreira mapu oblika (Izvođač, brojGlasova).
	 * 
	 * @param votes
	 *            file s glasovima
	 * @param artists
	 *            lista izvođača
	 * @return mapa glasova
	 * @throws IOException
	 */
	public static LinkedHashMap<String, String> createVoteMap(File votes,
			List<String> artists) throws IOException {
		maxVotes = 0;
		List<String> sortedList = new ArrayList<String>();
		List<String> fileVotes = Files.readAllLines(votes.toPath(),
				StandardCharsets.UTF_8);
		// u listu spremi stringove oblika (brojGlasova \t izvođač)
		for (int i = 0, length = fileVotes.size(); i < length; i++) {
			// čitaj trenutnu liniju iz filea s rezultatima
			Scanner voteScanner = new Scanner(fileVotes.get(i));
			voteScanner.useDelimiter("\t");
			// pridruži trenutnoj liniji definicijsku liniju (poveži preko ID-a)
			Scanner artistsScanner = new Scanner(artists.get(Integer
					.parseInt(voteScanner.next()) - 1));
			artistsScanner.useDelimiter("\t");
			artistsScanner.next();
			String numberOfVotes = voteScanner.next();
			// provjeri je li trenutni broj glasova najveći, ako je postavi ga
			maxVotes = Math.max(Integer.parseInt(numberOfVotes), maxVotes);
			sortedList.add(numberOfVotes + "\t" + artistsScanner.next());
			voteScanner.close();
			artistsScanner.close();
		}
		// sortiraj listu silazno samo po vrijednostima glasova
		Collections.sort(sortedList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.substring(0, o1.indexOf("\t"));
				o2 = o2.substring(0, o2.indexOf("\t"));
				return -(new Integer(Integer.parseInt(o1))
						.compareTo(new Integer(Integer.parseInt(o2))));
			}
		});
		// u mapu ubaci elemente liste tako da su napisani kao izvođač, broj
		// glasova
		LinkedHashMap<String, String> votesMap = new LinkedHashMap<String, String>();
		for (int i = 0, length = sortedList.size(); i < length; i++) {
			Scanner sc = new Scanner(sortedList.get(i));
			sc.useDelimiter("\t");
			String artistVotes = sc.next();
			String artist = sc.next();
			votesMap.put(artist, artistVotes);
			sc.close();
		}
		return votesMap;
	}

	/**
	 * Kreira file sa svim voteovima postavljenima na 0.
	 * 
	 * @param numberOfArtists
	 *            broj izvođača
	 * @param votes
	 *            file za kreiranje
	 * @throws IOException
	 */
	private void createFile(int numberOfArtists, File votes) throws IOException {
		List<String> voteList = new ArrayList<String>();
		for (int i = 0; i < numberOfArtists; i++) {
			voteList.add(String.valueOf(i + 1) + "\t0");
		}
		FileWriter fw = new FileWriter(votes);
		for (int i = 0, length = voteList.size(); i < length; i++) {
			fw.write(voteList.get(i) + "\r\n");
		}
		fw.close();
	}
}
