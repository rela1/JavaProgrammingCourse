package hr.fer.zemris.java.tecaj.hw6.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Klasa je container za main metodu, sadrzi i metode za kriptiranje/dekriptiranje/digesting.
 * 
 * @author Ivan Relic
 *
 */
public class Crypto {

	private static final int BUFFER_SIZE = 4096;

	/**
	 * Main metoda korisniku nudi na izbor kriptiranje, dekriptiranje i digesting po zadanom
	 * kljucu.
	 * 
	 * @param args argumenti komandne linije
	 * 
	 */
	public static void main(String[] args) {
		
		if (args.length<2 || args.length>3) {
			System.out.println("You should provide either \"cheksha\", \"encrypt\" or"
					+ " \"decrypt\" and files for operating as command line argument to program.");
			System.exit(-1);
		}
		
		Scanner scanner = new Scanner(System.in, "UTF-8");
		
		//ako je zatrazen digesting
		if (args[0].equals("checksha")) {
			String keyText = getKeyText(
					"Please provide expected sha signature for " +args[1]+ ": ", scanner);
			
			boolean correctDigest = false;
			
			//pokusaj digestirati, ulovi exceptione i ispisi korisniku pogreske
			try {
				
				correctDigest = digester(args[1], keyText);
				
			} catch (FileNotFoundException e) {
				
				System.out.println("Bad file path!");
				System.exit(-1);
				
			} catch (Exception e) {
				
				System.out.println("Digesting went wrong!");
				System.exit(-1);
			}
			
			System.out.println(correctDigest ?
					"Digesting completed. Digest of " +args[1]+ " matches expected digest."
					: "Digesting completed. Digest of " +args[1]+ " doesnt match expected digest.");
		}
		
		//ako je zatrazeno kriptiranje/dekriptiranje
		else if (args[0].equals("encrypt") || args[0].equals("decrypt")) {
			
			if (args.length != 3) {
				System.out.println("You should provide input and output file!");
			}
			
			//dohvati kljuc i inicijalizacijski vektor
			String keyText = getKeyText(
					"Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ",
					scanner);
			
			String ivText = getKeyText(
					"Please provide initialization vector as hex-encoded text (32 hex-digits): ",
					scanner);
			
			//ako je trazeno kriptiranje
			if (args[0].equals("encrypt")) {
				
				try {
					
					crypt(args[1], args[2], keyText, ivText, true);
					System.out.println("File crypted into file: " +args[2]);
					
				} catch (FileNotFoundException e) {
					
					System.out.println("Bad filepath!");
					System.exit(-1);
					
				} catch (Exception e) {
					
					System.out.println("Encrypting went wrong!");
					System.exit(-1);
				}
			}
			
			//inace, zatrazeno je dekriptiranje
			else {
				
				try {
					
					crypt(args[1], args[2], keyText, ivText, false);
					System.out.println("File decrypted into file: " +args[2]);
					
				} catch (FileNotFoundException e) {
					
					System.out.println("Bad filepath!");
					System.exit(-1);
					
				} catch (Exception e) {
					
					System.out.println("Decrypting went wrong!");
					System.exit(-1);
				}
			}
		}
		
		else {
			System.out.println("Unsupported operation!");
			System.exit(-1);
		}
		
		scanner.close();
		
	}

	/**
	 * Metoda uzima pathove do fileova i dekriptira file.
	 * 
	 * @param input ulazni file
	 * @param output izlazni file
	 * @param keyText string s kljucem
	 * @param ivText string s inicijalizacijskim vektorom
	 * @throws Exception
	 */
	private static void crypt(String input, String output, String keyText,
			String ivText, boolean isEncrypt) throws Exception {
		
		InputStream inputStream = new BufferedInputStream(
				new FileInputStream(input), BUFFER_SIZE);
		
		OutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(output, true), BUFFER_SIZE);
		
		//kreiraj cipher iz primljenih argumenata
		Cipher cipher = createCipher(keyText, ivText, isEncrypt);
		
		//kreiraj byte nizove velicine BUFFERA
		byte[] inputBytes = new byte[BUFFER_SIZE];
		byte[] outputBytes = new byte[BUFFER_SIZE];
		
		//dok imas sto za citati, updateaj podatke s cipherom i zapisi ih u file
		while (inputStream.available() != 0) {
			int size = inputStream.read(inputBytes);
			outputBytes = cipher.update(inputBytes, 0, size);
			outputStream.write(outputBytes);
			outputStream.flush();
		}
		
		inputStream.close();
		outputStream.close();
	}

	/**
	 * Metoda prima kljuc, inicijalizacijski vektor i kreira novi Cipher.
	 * 
	 * @param keyText kljuc
	 * @param ivText inicijalizacijski vektor
	 * @param isEncrypter je li cipher za enkripciju ili dekripciju
	 * @return
	 * @throws Exception
	 */
	private static Cipher createCipher(String keyText, String ivText, boolean isEncrypter) 
			throws Exception {
		
		//kreiraj cipher s primljenim argumentima i vrati ga
		SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(isEncrypter ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		
		return cipher;
	}

	/**
	 * Metoda primljeni string pretvara u niz byteova i vraca ga,
	 * 
	 * @param keyText string
	 * @return niz byteova
	 */
	private static byte[] hexToByte(String keyText) {
		
		//kreiraj novo polje dvostruko manje velicine nego je kljuc (2 hex znaka idu u 1 bajt)
		int length = keyText.length() / 2;
		byte[] newArray = new byte[length];
		
		//popuni sve bajtove
		for (int i = 0; i < length; i++) {
			
			//uzmi po dva hex znaka
			String hexPom = keyText.substring(2*i, 2*i + 2);
			
			//pretvori ih u integer
			Integer integerPom = Integer.parseInt(hexPom, 16);
			
			//integer zapisi kao bajt u polje bajtova
			newArray[i] = integerPom.byteValue();
		}
		
		return newArray;
	}

	/**
	 * Metoda provjerava zadovoljava li file predani SHA kljuc.
	 * 
	 * @param string filepath za ulaznu datoteku
	 * @param keyText kljuc
	 * @return true ili false
	 */
	private static boolean digester(String input, String keyText) throws Exception {
		
		//kreiraj message digest objekt
		MessageDigest shaDigester = MessageDigest.getInstance("SHA-1");
		
		InputStream inputStream = new BufferedInputStream(
				new FileInputStream(input), BUFFER_SIZE);	
		
		byte[] inputBytes = new byte[BUFFER_SIZE];
			
		//dok imas sto za citati, updateaj digester s tocno onoliko byteova koliko si procitao
		while (inputStream.available() != 0) {
			int size = inputStream.read(inputBytes);
			shaDigester.update(inputBytes, 0, size);
		}
		
		inputStream.close();
		
		//kreiraj originalni digest i digest filea koji citas
		byte[] originalDigest = hexToByte(keyText);
		byte[] fileDigest = shaDigester.digest();
		
		return MessageDigest.isEqual(originalDigest, fileDigest);
	}

	/**
	 * Metoda iz scannera cita kljuc za digestiranje/kriptiranje/dekriptiranje
	 * 
	 * @param poruka poruka koju treba ispisivati prilikom citanja
	 * @return kljuc u obliku stringa
	 */
	private static String getKeyText(String poruka, Scanner scanner) {
		System.out.println(poruka);
		String keyText = scanner.next();
		return keyText;
	}
}
