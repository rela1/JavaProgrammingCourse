package hr.fer.zemris.java.tecaj.hw3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**Klasa za instanciranje objekata koji predstavljaju zapis kompleksnog broja, klasa pruza
 * i metode za izvrsavanje raznih operacija nad kompleksnim brojevima, ali koje ako vrse
 * promjene nad varijablama, vracaju novi objekt, objekti su immutable.
 * 
 * @author Ivan Relic
 *
 */
public class ComplexNumber {

	//clanski read only property
	private double real;
	private double imaginary;
	
	
	/**Konstruktor koji prima realni i imaginarni dio broja kao argumente i pohranjuje ih
	 * u clanske varijable.
	 * 
	 * @param real Realni dio broja.	
	 * @param imaginary Imaginarni dio broja.
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	
	/**Metoda prima realni broj koji predstavlja realni dio kompleksnog broja i stvara novi 
	 * objekt tipa ComplexNumber.
	 *  
	 * @param real Realni broj koji predstavlja realni dio kompleksnog broja.
	 * @return Vraca novi objekt tipa ComplexNumber.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	
	/**Metoda prima realni broj koji predstavlja imaginarni dio kompleksnog broja i 
	 * stvara novi objekt tipa ComplexNumber.
	 * 
	 * @param imaginary Realni broj koji predstavlja imaginarni dio kompleksnog broja.
	 * @return Vraca novi objekt tipa ComplexNumber.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	
	/**Metoda prima dva realna broja, radijus i kut trigonometrijskog zapisa kompleksnog broja
	 * i iz toga stvara novi objekt tipa ComplexNumber.
	 * 
	 * @param magnitude Radijus, korijen iz kvadrata realnog i kvadrata imaginarnog dijela.
	 * @param angle Kut phi pod kojim je vektor kompleksnog broja u odnosu na os x.
	 * @return Vraca novi objekt tipa ComplexNumber.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		
		//magnitude mora biti nenegativan broj
		if (magnitude < 0) {
			throw new IllegalArgumentException("Magnitude can't be negative!");
		}
		
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	
	/**Metoda prima string koji je valjani prikaz kompleksnog broja i parsira ga i vraca
	 * novi objekt tipa ComplexNumber s danim vrijednostima.
	 * 
	 * @param number String koji predstavlja kompleksni broj.
	 * @return Vraca novi objekt tipa ComplexNumber.
	 */
	public static ComplexNumber parse(String number) {
		
		//varijable za cuvanje vrijednosti dijelova kompleksnog broja
		double real = 0, imaginary = 0;
		
		//ako broj ne odgovara string prikazu imaginarnog broja, baci exception
		if (!number.matches("([\\+-]?[0-9][0-9]*(\\.[0-9]*)?"
				+ "[\\+-]([0-9][0-9]*)?(\\.[0-9]*)?i)|([\\+-]?([0-9][0-9]*(\\.[0-9]*)?)?i?)")) {
			throw new IllegalArgumentException("Bad string for number parse!");
		}
		
		//inace, isparsiraj string
		else {
			
			//kreiraj pattern koji trazi po uzorku broja
			Pattern pattern = Pattern.compile("[\\+-]?([0-9][0-9]*(\\.[0-9]*)?)?i?");
			
			//upari matcher s kreiranim uzorkom
			Matcher matcher = pattern.matcher(number);
			
			//dok nalazis uzorke patterna
			while (matcher.find()) {
				
				//pospremi element koji je uzorak patterna u pomocni string
				String broj = matcher.group(0);
				
				//ako si procitao prazan string, breakaj
				if (broj.equals("")) {
					break;
				}
				
				//ako je procitani element dio imaginarnog broja
				if (broj.matches("[\\+-]?([0-9][0-9]*(\\.[0-9]*)?)?i")) {
					
					//ako se imaginarni dio sastoji samo od (+)i
					if (broj.matches("[\\+]?i")) {
						imaginary = 1;
					}
					
					//ako se imaginarni dio sastoji samo od -i
					else if (broj.matches("[-]i")) {
						imaginary = -1;
					}
					
					//za sve ostale vrijednosti imaginarnog dijela
					else {
						
						//ukloni slovo i parsiraj u double
						broj = broj.replace("i", "");
						imaginary = Double.parseDouble(broj);
					}
				}
				
				//inace je broj realni dio
				else {
					real = Double.parseDouble(broj);
				}
			}
		}
		
		return new ComplexNumber(real, imaginary);
	}
	
	
	/**Vraca vrijednost realnog dijela kompleksnog broja.
	 * 
	 * @return Double broj, realni dio broja.
	 */
	public double getReal() {
		return real;
	}
	
	
	/**Vraca vrijednost imaginarnog dijela kompleksnog broja.
	 * 
	 * @return Double broj, kompleksni dio broja.
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	
	/**Vraca vrijednost radijusa, tj. argument kompleksnog broja iz trigonometrijskog oblika.
	 * 
	 * @return Double broj, radijus.
	 */
	public double getMagnitude() {
		return Math.hypot(real, imaginary);
	}
	
	
	/**Metoda vraca kut phi, kut iz trigonometrijskog oblika kompleksnog broja.
	 * 
	 * @return Double broj, kut.
	 */
	public double getAngle() {
		return Math.atan2(imaginary, real);
	}
	
	
	/**Metoda zbraja objekt nad kojim se poziva funkcija s argumentom i vraca novi objekt
	 * koji je jednak rezultatu zbroja.
	 * 
	 * @param c Kompleksni broj s kojim zelimo zbrojiti.
	 * @return Novi objekt tipa ComplexNumber.
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
	}
	
	
	/**Metoda oduzima objekt nad kojim se poziva funkcija s argumentom i vraca novi objekt
	 * koji je jednak rezultatu oduzimanja.
	 * 
	 * @param c Kompleksni broj kojeg zelimo oduzeti.
	 * @return Novi objekt tipa ComplexNumber.
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	
	/**Metoda mnozi objekt nad kojim se poziva funkcija s argumentom i vraca novi objekt koji
	 * je jednak rezultatu mnozenja.
	 * 
	 * @param c Kompleksni broj s kojim mnozimo.
	 * @return Novi objekt tipa ComplexNumber
	 */
	public ComplexNumber mul(ComplexNumber c) {
		
		//umnozak radijusa 
		double radius = this.getMagnitude() * c.getMagnitude();
		
		//zbroj kuteva phi
		double phi = this.getAngle() + c.getAngle();
		
		//vrati novi kompleksni broj
		return ComplexNumber.fromMagnitudeAndAngle(radius, phi);
	}
	
	
	/**Metoda dijeli objekt nad kojim se poziva funkcija s predanim i vraca novi objekt koji
	 * je jednak rezultatu dijeljenja.
	 * 
	 * @param c Broj s kojim zelimo podijeliti.
	 * @return Novi objekt tipa ComplexNumber.
	 */
	public ComplexNumber div(ComplexNumber c) {
		
		//kvocijent radijusa
		double radius = this.getMagnitude() / c.getMagnitude();
		
		//razlika kuteva phi
		double phi = this.getAngle() - c.getAngle();
		
		//vrati novi kompleksni broj
		return ComplexNumber.fromMagnitudeAndAngle(radius, phi);
	}
	
	
	/**Metoda potencira objekt nad kojim se poziva funkcija na potenciju koja je predana kao
	 * argument i vraca novi objekt koji je jednak rezultatu potenciranja.
	 * 
	 * @param n Potencija na koju dizemo kompleksni broj, veca ili jednaka nuli.
	 * @return Novi objekt tipa ComplexNumber.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Potencija ne smije biti negativan broj!");
		}
		
		//ako je trazena potencija nula, vrati novi kompleksni broj s realnim dijelom 1
		else if (n == 0) {
			return new ComplexNumber(1, 0);
		}
		
		else {
			return ComplexNumber.fromMagnitudeAndAngle(
					Math.pow(this.getMagnitude(), n), this.getAngle()*n);
		}
	}
	
	
	/**Vraca niz novih objekata koji predstavljaju sve korijene objekta nad kojim se metoda
	 * izvrsila.
	 * 
	 * @param n Koji korijen trazimo.
	 * @return Niz novih objekata tipa ComplexNumber.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Broj korijena mora biti pozitivan broj!");
		}
		
		//ako je zatrazen 1. korijen, vrati novi objekt identican sebi
		else if (n == 1) {
			return new ComplexNumber[] {
					new ComplexNumber(this.real, this.imaginary)
			};
		}
		
		else {
			
			//stvori novo polje kompleksnih brojeva velicine n (toliko korijena ima)
			ComplexNumber[] korijen = new ComplexNumber[n];
			
			//izracunaj n-ti korijen radiusa 
			double radius = Math.pow(this.getMagnitude(), (double) 1/n);
			
			//izracunaj kut phi
			double phi = this.getAngle();
			
			//prodji u for petlji i obavi korjenovanje i pospremaj nove objekte u niz
			for(int i = 0; i < n; i++) {
				korijen[i] = ComplexNumber.fromMagnitudeAndAngle(
						radius, (2*i*Math.PI + phi) / n);
			}
			
			//vrati polje korijena
			return korijen;
		}
	}
	
	
	/**Override metode toString, za ispis kompleksnih brojeva.
	 * 
	 * @return Vraca kompleksni broj u obliku stringa.
	 */
	public String toString() {
		String pom = "";
		
		if (this.real != 0) {
			pom = pom + Double.toString(this.real);
		}
		
		if (this.imaginary != 0) {
			if (this.imaginary > 0) {
				pom = pom + "+" + Double.toString(imaginary) + "i";
			}
			
			else {
				pom = pom + Double.toString(imaginary) + "i";
			}
		}
		
		return pom;
	}
}
