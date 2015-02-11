package hr.fer.zemris.java.tecaj.hw4.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**Klasa iz datoteke kreira mapu s indeksima JMBAG-a i listu StudentRecorda.
 * 
 * @author Ivan Relic
 *
 */
public class StudentDatabase {

    //lista svih StudentRecorda
    private List<StudentRecord> studentList;
    
    //mapa indeksiranih jmbagova sa StudentRecordima
    private Map<String, StudentRecord> indexMap;
    
    public StudentDatabase(List<String> fileLista) {
	
	//alociraj memoriju za mapu i listu 
	studentList = new ArrayList<StudentRecord>();
	indexMap = new HashMap<String, StudentRecord>();
	
	Scanner scanner = null;
	
	//prodji cijelom listom zapisa o studentima
	for (int i = 0, length = fileLista.size(); i < length; i++) {
	    
	    //dohvati trenutni string, tj. podatke o jednom studentu u scanner
	    scanner = new Scanner(fileLista.get(i));
	    
	    //podaci su razdvojeni tabom, pa koristi taj delimiter
	    scanner.useDelimiter("\t");
	    
	    //procitaj podatke o studentu
	    String jmbag = new String();
	    String lastName = new String();
	    String firstName = new String();
	    int finalGrade = 0;
	    
	    //ako ne uspijes procitati neki od elemenata, znaci da nisu upisani
	    try {
		jmbag = scanner.next();
		lastName = scanner.next();
		firstName = scanner.next();
		finalGrade = scanner.nextInt();
	    } catch (Exception e) {
		System.err.println("To few arguments in file at line: " + (i+1) + "!");
	    }
	    
	    //trenutni pomocni StudentRecord
	    StudentRecord pom = new StudentRecord(jmbag, lastName, firstName, finalGrade);
	    
	    //u listu studenata dodaj novi StudentRecord objekt s ovim podacima
	    studentList.add(pom);
	    
	    //u index map dodaj trenutni jmbag i citavi StudentRecord
	    indexMap.put(jmbag, pom);
	    	    
	}
	
	scanner.close();
    }
    
    
    /**Metoda vraca StudentRecord studenta s trazenim JMBAG-om.
     * 
     * @param jmbag JMBAG studenta kojeg trazimo.
     * @return Vraca StudentRecord
     */
    public StudentRecord forJMBAG(String jmbag) {
	return indexMap.get(jmbag);
    }
    
    
    public List<StudentRecord> filter(IFilter filter) {
	
	//temporary lista za pohranjivanje studenata koji acceptaju filter
	List<StudentRecord> tmp = new ArrayList<StudentRecord>();
	
	for (int i = 0, length = this.studentList.size(); i < length; i++) {
	    
	    //pomocna varijabla radi izbjegavanja dvostrukog ucitavanja iz liste
	    StudentRecord pom = studentList.get(i);
	    
	    //ako student zadovoljava dani filter, dodaj ga u tmp listu
	    if (filter.accepts(pom)) {
		tmp.add(pom);
	    }
	}
	
	return tmp;
    }
}
