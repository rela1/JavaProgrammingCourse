package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestContext {

	/**
	 * Implementira entitet cookie-a.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	public static class RCCookie {

		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		private boolean httpOnly;

		/**
		 * Konstruktor. Postavlja vrijednosti za ime, vrijednost, domenu, path i
		 * maxAge.
		 * 
		 * @param name
		 *            ime
		 * @param value
		 *            vrijednost
		 * @param domain
		 *            domena
		 * @param path
		 *            path
		 * @param maxAge
		 *            maxAge
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			this(name, value, maxAge, domain, path, false);
		}

		/**
		 * Konstruktor. Postavlja vrijednosti za ime, vrijednost, domenu, path i
		 * maxAge.
		 * 
		 * @param name
		 *            ime
		 * @param value
		 *            vrijednost
		 * @param domain
		 *            domena
		 * @param path
		 *            path
		 * @param maxAge
		 *            maxAge
		 * @param httpOnly
		 *            je li cookie http only ili ne
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path, boolean httpOnly) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.httpOnly = httpOnly;
		}

		/**
		 * Dohvaća ime.
		 * 
		 * @return ime
		 */
		public String getName() {
			return name;
		}

		/**
		 * Dohvaća vrijednost.
		 * 
		 * @return vrijednost
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Dohvaća domenu.
		 * 
		 * @return domena
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Dohvaća put.
		 * 
		 * @return put
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Dohvaća maxAge.
		 * 
		 * @return maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

	private OutputStream outputStream;
	private Charset charset;
	private String encoding;
	private int statusCode;
	private String statusText;
	private String mimeType;
	private Map<String, String> parameters;
	private Map<String, String> temporaryParameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated;

	/**
	 * Konstruktor. Kreira kontekst zahtjeva.
	 * 
	 * @param outputStream
	 *            output stream
	 * @param parameters
	 *            parametri
	 * @param persistentParameters
	 *            persistent parametri
	 * @param outputCookies
	 *            output cookieji
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {
		if (outputStream == null) {
			throw new IllegalArgumentException(
					"Output stream should not be null!");
		}
		this.outputStream = outputStream;
		if (parameters == null) {
			this.parameters = new HashMap<>();
		} else {
			this.parameters = parameters;
		}
		if (persistentParameters == null) {
			this.persistentParameters = new HashMap<>();
		} else {
			this.persistentParameters = persistentParameters;
		}
		if (outputCookies == null) {
			this.outputCookies = new ArrayList<>();
		} else {
			this.outputCookies = new ArrayList<>(outputCookies);
		}
		this.encoding = "UTF-8";
		this.temporaryParameters = new HashMap<String, String>();
		this.statusCode = 200;
		this.statusText = "OK";
		this.mimeType = "text/html";
		this.headerGenerated = false;
	}

	/**
	 * Postavlja encoding.
	 * 
	 * @param encoding
	 *            encoding za postaviti
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Can't change encoding if header generated!");
		}
		this.encoding = encoding;
	}

	/**
	 * Dodaje cookie u listu outputCookiea.
	 * 
	 * @param cookie
	 *            cookie za dodati u listu
	 */
	public void addRCCookie(RCCookie cookie) {
		if (cookie == null) {
			throw new IllegalArgumentException("RCCookie should not be null!");
		}
		this.outputCookies.add(cookie);
	}

	/**
	 * Postavlja status code.
	 * 
	 * @param statusCode
	 *            status code za postaviti
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Can't change status code if header generated!");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Postavlja tekst statusa.
	 * 
	 * @param statusText
	 *            tekst statusa za postaviti
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Can't change status text if header generated!");
		}
		this.statusText = statusText;
	}

	/**
	 * Postavlja mime tip.
	 * 
	 * @param mimeType
	 *            mime tip za postaviti
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Can't change mime type if header generated!");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Vraća mapu parametara.
	 * 
	 * @return mapa parametara
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	/**
	 * Vraća mapu temporary parametara.
	 * 
	 * @return temporary parametri
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}

	/**
	 * Postavlja temporary parametre.
	 * 
	 * @param temporaryParameters
	 *            temporary parametri za postaviti
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Vraća mapu persistent parametara.
	 * 
	 * @return persistent parametri
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}

	/**
	 * Postavlja persistent parametre.
	 * 
	 * @param temporaryParameters
	 *            persistent parametri za postaviti
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}

	/**
	 * Vraća parametar pridružen imenu.
	 * 
	 * @param name
	 *            ime parametra koji želimo
	 * @return vrijednost parametra ili null ako ne postoji parametar pod
	 *         traženim imenom
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Vraća set imena svih parametara.
	 * 
	 * @return set parametara
	 */
	public Set<String> getParameterNames() {
		Set<String> parameterValues = parameters.keySet();
		return Collections.unmodifiableSet(parameterValues);
	}

	/**
	 * Vraća persistent parametar pridružen imenu.
	 * 
	 * @param name
	 *            ime parametra koji želimo
	 * @return vrijednost parametra ili null ako ne postoji parametar pod
	 *         traženim imenom
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Vraća set imena svih persistent parametara.
	 * 
	 * @return set parametara
	 */
	public Set<String> getPersistentParameterNames() {
		return persistentParameters.keySet();
	}

	/**
	 * Postavlja persistent parametar s navedenim imenom, ako postoji, na
	 * navedenu vrijednost.
	 * 
	 * @param name
	 *            ime parametra
	 * @param value
	 *            vrijednost parametra koja se postavlja
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Uklanja persistent parametar s predanim imenom, ako postoji.
	 * 
	 * @param name
	 *            ime persistent parametra
	 */
	public void removePersistentParameter(String name) {
		if (persistentParameters.get(name) == null) {
			throw new IllegalArgumentException(
					"Invalid persistent parameter name!");
		}
		persistentParameters.remove(name);
	}

	/**
	 * Vraća temporary parametar pridružen imenu.
	 * 
	 * @param name
	 *            ime parametra koji želimo
	 * @return vrijednost parametra ili null ako ne postoji parametar pod
	 *         traženim imenom
	 */
	public String getTemporaryParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Vraća set imena svih temporary parametara.
	 * 
	 * @return set parametara
	 */
	public Set<String> getTemporaryParameterNames() {
		return persistentParameters.keySet();
	}

	/**
	 * Postavlja temporary parametar s navedenim imenom, ako postoji, na
	 * navedenu vrijednost.
	 * 
	 * @param name
	 *            ime parametra
	 * @param value
	 *            vrijednost parametra koja se postavlja
	 */
	public void setTemporaryParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Uklanja temporary parametar s predanim imenom, ako postoji.
	 * 
	 * @param name
	 *            ime persistent parametra
	 */
	public void removeTemporaryParameter(String name) {
		if (persistentParameters.get(name) == null) {
			throw new IllegalArgumentException(
					"Invalid persistent parameter name!");
		}
		persistentParameters.remove(name);
	}

	/**
	 * Zapisuje niz byteova na izlazni stream i pritom, ako već nije stvoren,
	 * kreira header.
	 * 
	 * @param data
	 *            byteovi za zapisati
	 * @return vraća referencu na samoga sebe
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			createHeader(outputStream);
			headerGenerated = true;
		}
		outputStream.write(data);
		return this;
	}

	/**
	 * Zapisuje string enkodiran u byteove koristeći zadani encoding na izlazni
	 * stream i pritom, ako već nije stvoren, kreira header.
	 * 
	 * @param text
	 *            string za zapisati
	 * @return vraća referencu na samoga sebe
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			createHeader(outputStream);
			headerGenerated = true;
		}
		outputStream.write(text.getBytes(charset));
		return this;
	}

	/**
	 * Kreira header i šalje ga na output stream.
	 * 
	 * @throws IOException
	 */
	private void createHeader(OutputStream outputStream) throws IOException {
		charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		sb.append("Content-Type: " + mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append("; charset= " + encoding);
		}
		sb.append("\r\n");
		if (outputCookies.size() > 0) {
			addCookies(sb);
		}
		sb.append("\r\n");
		byte[] data = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
		outputStream.write(data);
	}

	/**
	 * Dodaje elemente iz liste cookiesa u header ako postoje.
	 * 
	 * @param sb
	 */
	private void addCookies(StringBuilder sb) {
		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: " + cookie.getName() + "=\""
					+ cookie.getValue() + "\"");
			String domain = cookie.domain;
			if (domain != null) {
				sb.append("; Domain=" + domain);
			}
			String path = cookie.path;
			if (path != null) {
				sb.append("; Path=" + path);
			}
			Integer maxAge = cookie.maxAge;
			if (maxAge != null) {
				sb.append("; Max-Age=" + maxAge.toString());
			}
			if (cookie.httpOnly) {
				sb.append("; HttpOnly");
			}
			sb.append("\r\n");
		}
	}
}
