package hr.fer.zemris.java.webserver;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RequestContextTest {

	@Test
	public void ContextTest1() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		outputCookies.add(new RCCookie("name", "test", 50, "127.0.0.0", "/"));
		outputCookies
				.add(new RCCookie("state", "test2", 100, "127.0.0.0", "/"));
		RequestContext rc = new RequestContext(os, parameters,
				persistentParameters, outputCookies);
		try {
			rc.write("Proba!");
		} catch (IOException ignorable) {
		}
		String header = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html; charset= UTF-8\r\n"
				+ "Set-Cookie: name=\"test\"; Domain=127.0.0.0; Path=/; Max-Age=50\r\n"
				+ "Set-Cookie: state=\"test2\"; Domain=127.0.0.0; Path=/; Max-Age=100\r\n"
				+ "\r\n" + "Proba!";
		byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
		byte[] contextBytes = os.toByteArray();
		assertEquals("Arrays should be same length!",
				headerBytes.length == contextBytes.length, true);
		for (int i = 0, length = headerBytes.length; i < length; i++) {
			assertEquals("Array elements at index: " + i + " should be same!",
					headerBytes[i] == contextBytes[i], true);
		}
	}

	@Test
	public void ContextTest2() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		outputCookies
				.add(new RCCookie("name", "asd", 275, "127.0.0.0", "/asd"));
		outputCookies.add(new RCCookie("color", "test2", 0, "127.0.0.0",
				"/def", true));
		RequestContext rc = new RequestContext(os, parameters,
				persistentParameters, outputCookies);
		rc.setMimeType("image/jpg");
		rc.setEncoding("ISO_8859_1");
		rc.setStatusCode(547);
		try {
			rc.write("Test!");
		} catch (IOException ignorable) {
		}
		String header = "HTTP/1.1 547 OK\r\n"
				+ "Content-Type: image/jpg\r\n"
				+ "Set-Cookie: name=\"asd\"; Domain=127.0.0.0; Path=/asd; Max-Age=275\r\n"
				+ "Set-Cookie: color=\"test2\"; Domain=127.0.0.0; Path=/def; Max-Age=0; HttpOnly\r\n"
				+ "\r\n" + "Test!";
		byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
		byte[] contextBytes = os.toByteArray();
		assertEquals("Arrays should be same length!",
				headerBytes.length == contextBytes.length, true);
		for (int i = 0, length = headerBytes.length; i < length; i++) {
			assertEquals("Array elements at index: " + i + " should be same!",
					headerBytes[i] == contextBytes[i], true);
		}
	}

	@Test(expected = RuntimeException.class)
	public void ContextSetMimeTypeAfterHeaderGeneratedTest() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		outputCookies
				.add(new RCCookie("set", "asd", 275, "127.0.0.0", "/plooo"));
		outputCookies.add(new RCCookie("poi", "test2", 0, "127.0.0.0", "/548",
				true));
		RequestContext rc = new RequestContext(os, parameters,
				persistentParameters, outputCookies);
		rc.setMimeType("image/jpg");
		rc.setEncoding("ISO_8859_1");
		rc.setStatusCode(547);
		try {
			rc.write("Test!");
		} catch (IOException ignorable) {
		}
		rc.setMimeType("text/plain");
	}

	@Test(expected = RuntimeException.class)
	public void ContextSetCodeAfterHeaderGeneratedTest() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		RequestContext rc = new RequestContext(os, parameters,
				persistentParameters, outputCookies);
		rc.setMimeType("image/jpg");
		rc.setEncoding("ISO_8859_1");
		rc.setStatusCode(547);
		try {
			rc.write("Test!".getBytes());
		} catch (IOException ignorable) {
		}
		rc.setMimeType("text/plain");
		rc.setStatusCode(8);
	}

	@Test(expected = RuntimeException.class)
	public void ContextSetMessageAfterHeaderGeneratedTest() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		outputCookies.add(new RCCookie("testt", "45", 0, "127.0.0.0", "/+897",
				true));
		RequestContext rc = new RequestContext(os, parameters,
				persistentParameters, outputCookies);
		rc.setMimeType("text/html");
		rc.setEncoding("ISO_8859_1");
		rc.setStatusCode(547);
		try {
			rc.write("Test!");
		} catch (IOException ignorable) {
		}
		rc.setStatusText("test");
	}

	@Test(expected = RuntimeException.class)
	public void RemovenvalidPParamTest() {
		RequestContext rc = new RequestContext(new ByteArrayOutputStream(),
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.removePersistentParameter("test");
	}

	@Test(expected = RuntimeException.class)
	public void RemovenvalidTParamTest() {
		RequestContext rc = new RequestContext(new ByteArrayOutputStream(),
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.removeTemporaryParameter("test");
	}

	@Test(expected = RuntimeException.class)
	public void NullOutputStreamTest() {
		new RequestContext(null, null, null, null);
	}

	@Test(expected = RuntimeException.class)
	public void NullCookieTest() {
		RequestContext rc = new RequestContext(System.out, null, null, null);
		rc.addRCCookie(null);
	}

	@Test
	public void parametersGettersAndSettersTest() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test");
		RequestContext rc = new RequestContext(System.out, params, null, null);
		assertEquals("Očekivana vrijednost: test", "test",
				rc.getParameter("test"));
		assertEquals("Očekivana vrijednost: null", null,
				rc.getParameter("test2"));
		rc.setPersistentParameter("test", "test");
		assertEquals("Očekivana vrijednost: test", "test",
				rc.getPersistentParameter("test"));
		rc.setTemporaryParameter("test", "test");
		assertEquals("Očekivana vrijednost: test", "test",
				rc.getTemporaryParameter("test"));
		for (String name : rc.getParameterNames()) {
			assertEquals("Očekivana vrijednost: test", "test", name);
		}
		for (String name : rc.getPersistentParameterNames()) {
			assertEquals("Očekivana vrijednost: test", "test", name);
		}
		for (String name : rc.getTemporaryParameterNames()) {
			assertEquals("Očekivana vrijednost: test", "test", name);
		}
	}
}
