package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred modelira jednostavni server.
 * 
 * @author Ivan Relić
 * 
 */
public class SmartHttpServer {

	private String address;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String, String> mimeTypes;
	private Map<String, IWebWorker> workersMap;
	private ServerThread serverThread;
	private GarbageCollectorThread garbageCollectorThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	private Path mimeTypeConfigFilePath;
	private Path workersConfigFilePath;
	private Map<String, SessionMapEntry> sessions;
	private Random sessionRandom;
	private AtomicBoolean started;

	/**
	 * Konstruktor. Kreira filepath parsirajući predani server.properties file.
	 * 
	 * @param configFileName
	 *            path properties filea
	 */
	public SmartHttpServer(String configFileName) {
		Path configFilePath = Paths.get(configFileName);
		checkFilePath(configFilePath);
		String configFile = null;
		try {
			configFile = new String(Files.readAllBytes(configFilePath),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Error reading file: "
					+ configFilePath.toString());
			SmartHttpServer.this.stop();
			System.exit(0);
		}
		sessions = new HashMap<String, SessionMapEntry>();
		sessionRandom = new Random();
		mimeTypes = new HashMap<String, String>();
		workersMap = new HashMap<String, IWebWorker>();
		parseConfigFile(configFile);
		serverThread = new ServerThread();
		garbageCollectorThread = new GarbageCollectorThread();
		started = new AtomicBoolean(false);
	}

	/**
	 * Provjerava postoji li predani path i može li se čitati.
	 * 
	 * @param filePath
	 *            path za provjeru
	 */
	private void checkFilePath(Path filePath) {
		if (!filePath.toFile().exists()) {
			System.out.println("Given filepath doesn't exist!");
			System.exit(0);
		}
		if (!filePath.toFile().canRead()) {
			System.out.println("Cannot read from given filepath!");
			System.exit(0);
		}
	}

	/**
	 * Iz stringa koji predstavlja sadržaj properties filea parsira i dohvaća
	 * parametre servera.
	 * 
	 * @param configFile
	 *            sadržaj config filea
	 */
	private void parseConfigFile(String configFile) {
		Scanner sc = new Scanner(configFile);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			parseServerPropertiesLine(line);
		}
		// ako nije procitan path za mimeType config file, obavijesti korisnika
		// i završi izvođenje
		if (mimeTypeConfigFilePath == null) {
			System.out
					.println("Bad config file! Expected line with server.mimeConfig attribute!");
			System.exit(0);
		}
		checkFilePath(mimeTypeConfigFilePath);
		String mimeTypeConfig = new String(
				readFileBytes(mimeTypeConfigFilePath));
		parseMimeConfigFile(mimeTypeConfig);
		sc.close();
		// ako nije procitan path za worker config file, obavijesti korisnika
		// i završi izvođenje
		if (workersConfigFilePath == null) {
			System.out
					.println("Bad config file! Expected line with server.workers attribute!");
			System.exit(0);
		}
		String workersConfigFile = new String(
				readFileBytes(workersConfigFilePath));
		parseWorkersConfigFile(workersConfigFile);
	}

	/**
	 * Parsira config file za workere.
	 * 
	 * @param configFile
	 *            config file s workerima
	 */
	private void parseWorkersConfigFile(String configFile) {

		Scanner sc = new Scanner(configFile);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			String[] params = line.split("=");
			if (params.length != 2) {
				System.out.println("Bad config file line: " + line);
				System.exit(0);
			}
			params[0] = params[0].trim();
			params[1] = params[1].trim();
			if (workersMap.get(params[0]) != null) {
				System.out.println("There should not be duplicated paths!");
				System.exit(0);
			}
			try {
				workersMap.put(params[0], (IWebWorker) Class.forName(params[1])
						.getConstructor().newInstance());
			} catch (Exception e) {
				System.out.println("Cannot create instance of: " + params[1]);
				System.exit(0);
			}
		}
		sc.close();
	}

	/**
	 * Parsira mimeTypeConfig file.
	 * 
	 * @param mimeTypeConfig
	 *            sadržaj mimeTypeConfig filea
	 */
	private void parseMimeConfigFile(String configFile) {
		Scanner sc = new Scanner(configFile);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			String[] params = line.split("=");
			if (params.length != 2) {
				System.out.println("Bad config file line: " + line);
				System.exit(0);
			}
			params[0] = params[0].trim();
			params[1] = params[1].trim();
			mimeTypes.put(params[0], params[1]);
		}
		sc.close();
	}

	/**
	 * Parsira liniju config filea server propertiesa.
	 * 
	 * @param line
	 *            linija config filea
	 */
	private void parseServerPropertiesLine(String line) {
		String[] params = line.split("=");
		if (params.length != 2) {
			System.out.println("Bad config file line: " + line);
			System.exit(0);
		}
		params[0] = params[0].trim();
		params[1] = params[1].trim();
		if (params[0].equals("server.address")) {
			this.address = params[1];
		} else if (params[0].equals("server.port")) {
			try {
				this.port = Integer.parseInt(params[1]);
			} catch (Exception e) {
				System.out
						.println("Bad config file, unknown port number at line: "
								+ line);
				System.exit(0);
			}
		} else if (params[0].equals("server.workerThreads")) {
			try {
				this.workerThreads = Integer.parseInt(params[1]);
			} catch (Exception e) {
				System.out.println("Bad config file line: " + line);
				System.exit(0);
			}
		} else if (params[0].equals("server.documentRoot")) {
			this.documentRoot = Paths.get(params[1]);
		} else if (params[0].equals("server.mimeConfig")) {
			this.mimeTypeConfigFilePath = Paths.get(params[1]);
		} else if (params[0].equals("session.timeout")) {
			try {
				this.sessionTimeout = Integer.parseInt(params[1]) * 1000;
			} catch (Exception e) {
				System.out
						.println("Bad config file, unknown sessionTimeout at line: "
								+ line);
				System.exit(0);
			}
		} else if (params[0].equals("server.workers")) {
			this.workersConfigFilePath = Paths.get(params[1]);
		} else {
			System.out.println("Bad config file, unknown line: " + line);
			System.exit(0);
		}
	}

	/**
	 * Pokreće server ako već nije pokrenut.
	 */
	protected synchronized void start() {
		if (started.get()) {
			return;
		}
		started.set(true);
		threadPool = Executors.newFixedThreadPool(workerThreads);
		serverThread.start();
		garbageCollectorThread.start();

	}

	/**
	 * Zaustavlja server ako već nije zaustavljen.
	 */
	protected synchronized void stop() {
		if (!started.get()) {
			return;
		}
		started.set(false);
		threadPool.shutdown();
	}

	/**
	 * Vraća niz byteova sadržaja filea.
	 * 
	 * @param filePath
	 *            path filea
	 * @return niz byteova sadržaja filea
	 */
	private byte[] readFileBytes(Path filePath) {
		byte[] data = null;
		try {
			data = Files.readAllBytes(filePath);
		} catch (IOException e) {
			System.out.println("Error reading from file!");
			System.exit(0);
		}
		return data;
	}

	/**
	 * Razred enkapsulira entry za mapu sesija.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	private static class SessionMapEntry {
		@SuppressWarnings("unused")
		String sid;
		long validUntil;
		Map<String, String> map;
	}

	/**
	 * Klasa enkapsulira posao serverske dretve.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket ssocket = null;
			try {
				ssocket = new ServerSocket(port);
			} catch (IOException e) {
				closeSocket(ssocket);
				System.out.println("Cannot create socket on port: " + port);
				System.exit(0);
			}
			while (true) {
				if (!started.get()) {
					break;
				}
				Socket client = null;
				try {
					client = ssocket.accept();
				} catch (IOException e) {
					System.out.println("Error accepting client!");
					SmartHttpServer.this.stop();
					System.exit(0);
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
		}

		/**
		 * Zatvara predani socket.
		 * 
		 * @param ssocket
		 *            socket koji treba zatvoriti
		 */
		private void closeSocket(ServerSocket ssocket) {
			try {
				ssocket.close();
			} catch (IOException e1) {
				System.out.println("Error closing socket!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
		}
	}

	/**
	 * Dretva koja svakih 5 min čisti sesije kojima je isteklo validUntil
	 * vrijeme.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	private class GarbageCollectorThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (!started.get()) {
					break;
				}
				// iteriraj kopijom mape, briši iz originalne
				sessions = new HashMap<String, SessionMapEntry>(sessions);
				for (Entry<String, SessionMapEntry> entry : new HashMap<String, SessionMapEntry>(
						sessions).entrySet()) {
					if (entry.getValue().validUntil < System
							.currentTimeMillis()) {
						sessions.remove(entry.getKey());
					}
				}
				try {
					sleep(300000);
				} catch (InterruptedException ignorable) {
				}
			}
		}
	}

	/**
	 * Klasa enkapsulira klijentski posao.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	private class ClientWorker implements Runnable {
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		@SuppressWarnings("unused")
		private String version;
		@SuppressWarnings("unused")
		private String method;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> permParams = null;
		private List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		@SuppressWarnings("unused")
		private String SID;

		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
			} catch (IOException e) {
				System.out.println("Error getting input stream!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
			// obtain input stream from socket and wrap it to pushback input
			// stream
			try {
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				System.out.println("Error getting output stream!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
			// procitaj klijentski zahtjev
			List<String> request = readRequest();
			if (request.size() < 1) {
				returnErrorResponse(400, "Bad header!");
				closeSocket(csocket);
				return;
			}
			checkSession(request);
			// izdvoji prvu liniju
			String firstLine = request.get(0);
			if (firstLine == null) {
				closeSocket(csocket);
				return;
			}
			// provjeri je li prva linija zahtjeva u redu i izdvoji traženi path
			String requestedPath = checkFirstLine(firstLine);
			if (requestedPath == null) {
				closeSocket(csocket);
				return;
			}
			// iz traženog patha izdvoji path i parametre
			String[] pathParams = getPathParams(requestedPath);
			if (pathParams == null) {
				closeSocket(csocket);
				return;
			}
			String path = pathParams[0];
			if (pathParams.length > 1) {
				String paramString = pathParams[1];
				parseParameters(paramString);
			}
			// provjeri je li path ext zahtjev, ako je obradi ga
			if (isExtRequest(path)) {
				sendExtRequestData(path);
				closeSocket(csocket);
				return;
			}
			// ako path postoji u workers mapi preskoči stvari vezane uz
			// ekstenziju
			if (workersMap.get(path) != null) {
				sendWorkerData(path);
				closeSocket(csocket);
				return;
			}
			// kreiraj full path u odnosu na documentRoot
			Path filePath = getRequestedPath(Paths.get(path));
			if (filePath == null) {
				returnErrorResponse(403, "Forbidden!");
				closeSocket(csocket);
				return;
			}
			// ako file ne postoji ili se ne može čitati dojavi to korisniku
			if (!filePath.toFile().exists() && !filePath.toFile().isFile()
					&& !filePath.toFile().canRead()) {
				returnErrorResponse(404,
						"Bad file path: " + filePath.toString());
				closeSocket(csocket);
				return;
			}
			// dohvati ekstenziju filea i pošalji je klijentu s headerom
			String extension = getExtension(filePath.getFileName().toString());
			sendData(filePath, extension);
			closeSocket(csocket);
		}

		/**
		 * Provjerava sesiju za trenutni zahtjev.
		 * 
		 * @param request
		 *            zahtjev
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for (int i = 0, length = request.size(); i < length; i++) {
				String line = request.get(i);
				if (!line.startsWith("Cookie:")) {
					continue;
				}
				line = line.replace("Cookie: ", "");
				String[] params = line.split(";");
				for (int j = 0, cookieLength = params.length; j < cookieLength; j++) {
					String[] cookieValues = params[j].split("=");
					if (cookieValues[0].equals("sid")) {
						sidCandidate = cookieValues[1].replace("\"", "");
					}
				}
			}
			// ako nije poslan sid, kreiraj ga i kreiraj novu sesiju
			if (sidCandidate == null) {
				sidCandidate = createSid(sessionRandom);
				SessionMapEntry sessionEntry = createSession(sidCandidate);
				sessions.put(sidCandidate, sessionEntry);
			}
			// inače, provjeri postoji li entry za njega, ako ne postoji kreiraj
			// ga, ako postoji provjeri je li istekao
			else if (sessions.get(sidCandidate) == null
					|| sessions.get(sidCandidate).validUntil < System
							.currentTimeMillis()) {
				if (sessions.get(sidCandidate) != null) {
					sessions.remove(sidCandidate);
				}
				sidCandidate = createSid(sessionRandom);
				SessionMapEntry sessionEntry = createSession(sidCandidate);
				sessions.put(sidCandidate, sessionEntry);
			} else {
				sessions.get(sidCandidate).validUntil += sessionTimeout;
			}
			permParams = sessions.get(sidCandidate).map;
		}

		/**
		 * Kreira sesiju za predani sid.
		 * 
		 * @param sid
		 *            sid sesije
		 * @return
		 */
		private SessionMapEntry createSession(String sid) {
			SessionMapEntry sessionEntry = new SessionMapEntry();
			sessionEntry.sid = sid;
			sessionEntry.validUntil = System.currentTimeMillis()
					+ sessionTimeout;
			sessionEntry.map = new ConcurrentHashMap<String, String>();
			outputCookies
					.add(new RCCookie("sid", sid, null, address, "/", true));
			return sessionEntry;
		}

		/**
		 * Kreira sid za trenutnu sesiju (20 random uppercase znakova).
		 * 
		 * @param sessionRandom
		 *            randomizer za kreiranje
		 * @return string prikaz sid-a
		 */
		private String createSid(Random sessionRandom) {
			char[] data = new char[20];
			for (int i = 0; i < 20; i++) {
				data[i] = (char) (sessionRandom.nextInt(25) + 65);
			}
			return new String(data);
		}

		/**
		 * Obrađuje ext zahtjev i šalje podatke klijentu.
		 * 
		 * @param request
		 */
		private void sendExtRequestData(String request) {
			request = request.substring(request.lastIndexOf('/') + 1);
			RequestContext requestContext = new RequestContext(ostream, params,
					permParams, outputCookies);
			requestContext.setStatusCode(200);
			try {
				IWebWorker worker = (IWebWorker) Class
						.forName(
								"hr.fer.zemris.java.webserver.workers."
										+ request).getConstructor()
						.newInstance();
				worker.processRequest(requestContext);
			} catch (Exception e) {
				System.out.println("Error creating worker!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
		}

		/**
		 * Provjerava je li primljen ext zahtjev.
		 * 
		 * @param request
		 *            zahtjev
		 * @return true ako je valjani ext zahtjev, false inače
		 */
		private boolean isExtRequest(String request) {
			if (request.equals("/ext/CircleWorker")
					|| request.equals("/ext/HelloWorker")
					|| request.equals("/ext/EchoWorker")) {
				return true;
			}
			return false;
		}

		/**
		 * Služi za pokretanje obrade zahtjeva radnikom i slanje podataka
		 * klijentu.
		 * 
		 * @param key
		 *            ključ radnika koji se koristi
		 */
		private void sendWorkerData(String key) {
			RequestContext requestContext = new RequestContext(ostream, params,
					permParams, outputCookies);
			requestContext.setStatusCode(200);
			workersMap.get(key).processRequest(requestContext);
		}

		/**
		 * Kreira header i šalje podatke klijentu.
		 * 
		 * @param filePath
		 *            path filea koji treba poslati
		 * @param extension
		 *            ekstenzija filea
		 */
		private void sendData(Path filePath, String extension) {
			// kreiraj request context i preko njega pošalji podatke filea
			RequestContext requestContext = new RequestContext(ostream, params,
					permParams, outputCookies);
			if (extension == null) {
				requestContext.setMimeType("application/octet-stream");
			} else {
				if (extension.toUpperCase().equals("SMSCR")) {
					requestContext.setMimeType("text/plain");
					requestContext.setStatusCode(200);
					executeSmartScript(filePath, requestContext);
					return;
				} else {
					if (mimeTypes.get(extension) != null) {
						requestContext.setMimeType(mimeTypes.get(extension));
					} else {
						requestContext.setMimeType("application/octet-stream");
					}
				}
			}
			requestContext.setStatusCode(200);
			byte[] data = readFileBytes(filePath);
			try {
				requestContext.write(data);
			} catch (IOException e) {
				System.out.println("Error writing on output stream!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
		}

		/**
		 * Izvršava skriptu traženu u http zahtijevu.
		 * 
		 * @param filePath
		 *            path do skripte
		 * @param requestContext
		 *            request context za kreiranje zaglavlja i slanje podataka
		 */
		private void executeSmartScript(Path filePath,
				RequestContext requestContext) {
			String scriptText = new String(readFileBytes(filePath),
					StandardCharsets.UTF_8);
			try {
				new SmartScriptEngine(
						new SmartScriptParser(scriptText).getDocumentNode(),
						requestContext).execute();
			} catch (RuntimeException e) {
				try {
					requestContext.write(e.getMessage());
				} catch (IOException e1) {
					System.out.println("Error writing on output stream!");
					SmartHttpServer.this.stop();
					System.exit(0);
				}
			}

		}

		/**
		 * Zatvara predani socket.
		 * 
		 * @param ssocket
		 *            socket koji treba zatvoriti
		 */
		private void closeSocket(Socket socket) {
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println("Error closing socket!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
		}

		/**
		 * Vraća ekstenziju filea.
		 * 
		 * @param fileName
		 *            ime filea
		 * @return ekstenzija filea
		 */
		private String getExtension(String fileName) {
			int position = fileName.lastIndexOf('.');
			if (position == -1) {
				return null;
			}
			return fileName.substring(position + 1);
		}

		/**
		 * Dohvaća traženi path kombinirajući documentRoot i path
		 * 
		 * @param path
		 *            path za kombiniranje
		 * @return
		 */
		private Path getRequestedPath(Path path) {
			Path requestedPath = Paths.get(documentRoot.toString()
					+ path.toString());
			if (!requestedPath.startsWith(documentRoot)) {
				return null;
			}
			return Paths.get(documentRoot.toString() + path.toString());
		}

		/**
		 * Puni mapu parametara parsirajući paramString.
		 * 
		 * @param paramString
		 *            paramString za parsiranje
		 */
		private void parseParameters(String paramString) {
			String[] params = paramString.split("&");
			for (int i = 0, length = params.length; i < length; i++) {
				String[] currentParam = params[i].split("=");
				this.params.put(currentParam[0], currentParam[1]);
			}
		}

		/**
		 * Dohvaća path i paramString iz requestedPatha.
		 * 
		 * @param requestedPath
		 *            requestedPath
		 * @return path i paramString
		 */
		private String[] getPathParams(String requestedPath) {
			if (requestedPath.indexOf('?') == -1) {
				return new String[] { requestedPath };
			}
			String[] pathParams = requestedPath.split("\\?");
			if (pathParams.length != 2) {
				returnErrorResponse(400, "Bad header!");
				return null;
			}
			return pathParams;
		}

		/**
		 * Provjerava je li prva linija zahtjeva valjana i vraća requested path
		 * ako je sve u redu.
		 * 
		 * @param firstLine
		 *            prva linija zahtjeva
		 * @return requested path
		 */
		private String checkFirstLine(String firstLine) {
			String[] params = firstLine.split(" ");
			if (params.length != 3) {
				returnErrorResponse(400, "Bad header!");
				return null;
			}
			if (!params[0].toUpperCase().equals("GET")) {
				returnErrorResponse(400, "Bad header!");
				return null;
			}
			method = params[0].toUpperCase();
			if (!params[2].toUpperCase().equals("HTTP/1.0")
					&& !params[2].toUpperCase().equals("HTTP/1.1")) {
				returnErrorResponse(400, "Bad header!");
				return null;
			}
			version = params[2].toUpperCase();
			return params[1];
		}

		/**
		 * Vraća odgovor korisniku preko output streama.
		 * 
		 * @param messageCode
		 *            kod poruke
		 * @param messageText
		 *            tekst poruke
		 */
		private void returnErrorResponse(int messageCode, String messageText) {
			RequestContext requestContext = new RequestContext(ostream, params,
					permParams, outputCookies);
			requestContext.setStatusCode(messageCode);
			requestContext.setStatusText(messageText);
			try {
				requestContext.write(messageText);
			} catch (IOException e) {
				System.out.println("Error writing to output stream!");
				SmartHttpServer.this.stop();
				System.exit(0);
			}
		}

		/**
		 * Čita header s input streama socketa.
		 * 
		 * @return lista stringova koji predstavljaju header
		 */
		private List<String> readRequest() {
			List<String> header = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					istream, StandardCharsets.ISO_8859_1));
			// čitaj sve dok ne naiđeš na prazan red
			while (true) {
				String line = null;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					System.out
							.println("Error reading from socket input stream!");
					SmartHttpServer.this.stop();
					System.exit(0);
				}
				if (line.trim().isEmpty()) {
					break;
				}
				header.add(line.trim());
			}
			return header;
		}
	}

	/**
	 * Učitava properties fileove i pokreće server.
	 * 
	 * @param args
	 *            put do server properties filea
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out
					.println("You should provide filepath to server properties file!");
			System.exit(0);
		}
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
		System.out
				.println("Enter stop for stopping the server and exit the program!");
		Scanner s = new Scanner(System.in);
		while (s.hasNextLine()) {
			String input = s.nextLine().toUpperCase();
			// stopiraj server
			if (input.equals("STOP")) {
				s.close();
				server.stop();
				System.exit(0);
			}

		}
	}

}
