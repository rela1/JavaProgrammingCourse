package hr.fer.zemris.java.tecaj.hw6.problem1a;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;


/**
 * Konkretna implementacija sucelja IntegerStorageObserver, za svaku promjenu vrijednosti subjekta
 * kreira novi red koji sadrzi trenutnu vrijednost u fileu predanome konstruktoru.
 * 
 * @author Ivan Relic
 *
 */
public class LogValue implements IntegerStorageObserver {
	
	private Path filePath;
	Writer pisac = null;
	
	//ID za potrebe metoda equals i hashCode (za valjano funkcioniranje u listama)
	private final int ID = 4;
	
	/**
	 * Javni konstruktor preuzima string koji predstavlja path i pohranjuje ga u svoju varijablu.
	 * 
	 * @param path
	 */
	public LogValue(Path path) {
		filePath = path;
	}
	
	/**
	 * Metoda za svaku promjenu stanja subjekta otvara file i nadodaje novi red s trenutnom 
	 * vrijednosti subjekta.
	 * 
	 * @param istorage subjekt nad kojim se obavlja observing
	 */
	public void valueChanged(IntegerStorage istorage) {
		
		//otvori output stream za pisanje
		try {
			
			//ako se prvi put pise u file, overwriteaj sve sto je u njemu, inace, appendaj
			boolean appending = (pisac == null) ? false : true;
			pisac = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					filePath.toFile(), appending), "UTF-8"));
			pisac.write((new Integer(istorage.getValue())).toString() 
					+ System.getProperty("line.separator"));
			pisac.flush();
			pisac.close();
		} catch (FileNotFoundException e1) {
			System.err.println("Bad filepath given for LogValue observer!");
		} catch (IOException e) {
			System.err.println("Error writing to file!");
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LogValue))
			return false;
		LogValue other = (LogValue) obj;
		if (ID != other.ID)
			return false;
		return true;
	}	
}
