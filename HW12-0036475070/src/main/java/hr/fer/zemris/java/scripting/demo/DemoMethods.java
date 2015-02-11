package hr.fer.zemris.java.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Razred s pomoćnim metodama za korištenje u demo programima.
 * 
 * @author Ivan Relić
 * 
 */
public class DemoMethods {

	/**
	 * Čita file s diska i vraća string sa sadržajem filea.
	 * 
	 * @param filePath
	 *            path filea
	 * @return string sadržaj filea
	 */
	public static String readFromDisk(Path filePath) {
		byte[] data = null;
		try {
			data = Files.readAllBytes(filePath);
		} catch (IOException e) {
			System.out.println("Error reading from file!");
			System.exit(0);
		}
		return new String(data, StandardCharsets.UTF_8);
	}

}
