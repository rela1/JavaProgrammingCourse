package hr.fer.zemris.java.tecaj_06.rays;

/**
 * Klasa implementira sferu kao grafički objekt.
 * 
 * @author Ivan Relić
 * 
 */
public class Sphere extends GraphicalObject {

	private Point3D center;
	private double radius;
	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;

	/**
	 * Konstruktor. Prima centar te parametre reflektirane komponente i difuzne
	 * komponente.
	 * 
	 * @param center
	 *            centar sfere
	 * @param radius
	 *            radijus sfere
	 * @param kdr
	 *            crvena komponenta difuzne
	 * @param kdg
	 *            zelena komponenta difuzne
	 * @param kdb
	 *            plava komponenta difuzne
	 * @param krr
	 *            crvena komponenta reflektirane
	 * @param krg
	 *            zelena komponenta reflektirane
	 * @param krb
	 *            plava komponenta reflektirane
	 * @param krn
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Računa presjecište sfere s primljenom zrakom. Ako ne postoji presjecište,
	 * vraća null, ako postoje 2, vraća ono bliže promatraču. Računa se
	 * koristeći algoritam sa stranice
	 * http://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection.
	 * 
	 * @param ray
	 *            zraka s kojom se traže presjecišta
	 * @return presjecište najbliže promatraču ili null ako nema presjecišta
	 */
	public RayIntersection findClosestRayIntersection(Ray ray) {
		if (ray == null) {
			throw new IllegalArgumentException("Ray should not be null!");
		}
		// izračunaj koeficijente a, b, c kvadratne jednadžbe
		// a je jednak 1
		Point3D vector = ray.start.sub(center);
		double b = ray.direction.scalarMultiply(2).scalarProduct(vector);
		double c = vector.scalarProduct(vector) - radius * radius;
		// ako je diskriminanta manja od 0
		double discriminant = b * b - 4 * c;
		if ((discriminant) < 0) {
			return null;
		}
		discriminant = Math.sqrt(discriminant);
		// izračunaj lambda1 i lambda2 preko formule za kvadratnu jednadbžu
		double lambda1 = ((-1) * b - discriminant) / 2;
		double lambda2 = ((-1) * b + discriminant) / 2;
		double lambda;
		boolean outer;
		// ako su oba rješenja pozitivna, uzmi manju lambdu i outer postavi na
		// true
		if (lambda1 > 0 && lambda2 > 0) {
			lambda = Math.min(lambda1, lambda2);
			outer = true;
			// ako su oba negativna vrati null
		} else if (lambda1 < 0 && lambda2 < 0) {
			return null;
			// inace, uzmi pozitivnu lambdu i outer postavi na false
		} else {
			lambda = Math.max(lambda1, lambda2);
			outer = false;
		}
		// točka presjecišta će biti točka koja je udaljena od početne točke
		// zrake po njenom vektoru smjera za onoliko koliko je izračunata
		// lambda
		Point3D intersection = new Point3D(ray.start.x + ray.direction.x
				* lambda, ray.start.y + ray.direction.y * lambda, ray.start.z
				+ ray.direction.z * lambda);
		return new RayIntersection(intersection, lambda, outer) {

			@Override
			public Point3D getNormal() {
				// smjer vektora normale određen je točkama središta sfere i
				// točkom presjecišta
				return this.getPoint().sub(center).normalize();
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
}
