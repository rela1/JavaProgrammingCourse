package hr.fer.zemris.java.hw06.part2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import hr.fer.zemris.java.tecaj_06.rays.GraphicalObject;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.LightSource;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayIntersection;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

/**
 * Klasa sadrzi main program koji iscrtava scenu koristeći više dretvi.
 * 
 * @author Ivan Relić
 * 
 */
public class RayCasterParallel {

	/**
	 * granica do koje se "ekran" rekurzivno dijeli, ispod nje se slijedno
	 * računa
	 */
	private static final int TRESHOLD = 1;

	/**
	 * Prikazuje scenu u grafičkom sučelju.
	 * 
	 * @param args
	 *            ne koriste se
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Vraća novi producer u kojem je metoda produce implementirana na potreban
	 * način za bacanje zrake.
	 * 
	 * @return producer slike
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {
				long time1 = System.currentTimeMillis();
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				Point3D vectorOG = view.sub(eye).modifyNormalize();
				Point3D yAxis = viewUp
						.modifyNormalize()
						.sub(vectorOG.scalarMultiply(viewUp.modifyNormalize()
								.scalarProduct(vectorOG))).modifyNormalize();
				Point3D xAxis = vectorOG.vectorProduct(yAxis).modifyNormalize();
				Point3D screenCorner = view.sub(
						xAxis.scalarMultiply(horizontal / 2)).add(
						yAxis.scalarMultiply(vertical / 2));
				Scene scene = RayTracerViewer.createPredefinedScene();
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new ExecutorJob(horizontal, vertical, width,
						height, 0, height - 1, screenCorner, xAxis, yAxis, eye,
						red, blue, green, scene));
				pool.shutdown();
				long time2 = System.currentTimeMillis();
				System.out.println("Izračuni gotovi (" + (time2 - time1)
						+ "ms)...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Posao izračuna boje piksela.
	 * 
	 * @author Ivan Relić
	 * 
	 */
	public static class ExecutorJob extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		private double horizontal;
		private double vertical;
		private int width;
		private int height;
		private int heightMin;
		private int heightMax;
		private Point3D screenCorner;
		private Point3D xAxis;
		private Point3D yAxis;
		private Point3D eye;
		private short[] red;
		private short[] blue;
		private short[] green;
		private short[] rgb;
		private Scene scene;
		private int offset;

		/**
		 * Konstruktor. Prima podatke o ekranu i izračunatim vektorima
		 * potrebnima za računanje.
		 * 
		 * @param horizontal
		 *            horizontal
		 * @param vertical
		 *            vertical
		 * @param width
		 *            širina ekrana
		 * @param height
		 *            visina ekrana
		 * @param heightMin
		 *            visina od koje počinje izračun
		 * @param heightMax
		 *            visina do koje s računa
		 * @param screenCorner
		 *            gornji lijevi ugao
		 * @param xAxis
		 *            jedinični vektor i
		 * @param yAxis
		 *            jedinični vektor j
		 * @param eye
		 *            položaj promatrača
		 * @param red
		 *            polje za vrijednosti doprinosa crvene komponente na
		 *            pojedini piksel
		 * @param blue
		 *            polje za vrijednosti doprinosa plave komponente na
		 *            pojedini piksel
		 * @param green
		 *            polje za vrijednosti doprinosa zelene komponente na
		 *            pojedini piksel
		 * @param scene
		 *            scena
		 */
		public ExecutorJob(double horizontal, double vertical, int width,
				int height, int heightMin, int heightMax, Point3D screenCorner,
				Point3D xAxis, Point3D yAxis, Point3D eye, short[] red,
				short[] blue, short[] green, Scene scene) {
			super();
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.heightMin = heightMin;
			this.heightMax = heightMax;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.eye = eye;
			this.red = red;
			this.blue = blue;
			this.green = green;
			this.rgb = new short[3];
			this.scene = scene;
			this.offset = width * heightMin;
		}

		/**
		 * Središnja metoda višedretvenog izračuna - ako je veličina posla veća
		 * od granične, podijelit će ga na manje dijelove, inače će računati
		 * slijedno.
		 */
		protected void compute() {
			if (heightMax - heightMin > TRESHOLD) {
				invokeAll(new ExecutorJob(horizontal, vertical, width, height,
						heightMin, heightMin + (heightMax - heightMin) / 2,
						screenCorner, xAxis, yAxis, eye, red, blue, green,
						scene),

				new ExecutorJob(horizontal, vertical, width, height, heightMin
						+ (heightMax - heightMin) / 2 + 1, heightMax,
						screenCorner, xAxis, yAxis, eye, red, blue, green,
						scene));
			} else {
				for (int y = heightMin; y <= heightMax; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(
								xAxis.scalarMultiply(horizontal * x
										/ (width - 1))).sub(
								yAxis.scalarMultiply(vertical * y
										/ (height - 1)));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						caster(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
			}
		}

		/**
		 * Za predanu zraku u sceni pronalazi točku najbližeg sjecišta s nekim
		 * od objekata.
		 * 
		 * @param scene
		 *            scena
		 * @param ray
		 *            zraka
		 * @return točka najbližeg sjecišta
		 */
		private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			// kao udaljenost za usporedbu stavi infinity
			double minimalDistance = Double.POSITIVE_INFINITY;
			RayIntersection closestIntersection = null;
			// prođi kroz sve objekte scene i za one koje zraka sječe, nađi
			// ono koje ima najmanju udaljenost od početka zrake
			for (GraphicalObject object : scene.getObjects()) {
				RayIntersection intersection = object
						.findClosestRayIntersection(ray);
				// ako sjecište postoji, provjeri je li njegova udaljenost
				// manja od trenutno najbližeg sjecišta i postavi ga za
				// najbližeg ako je
				if (intersection != null) {
					double intersectionDistance = intersection.getDistance();
					if (intersectionDistance < minimalDistance) {
						closestIntersection = intersection;
						minimalDistance = intersectionDistance;
					}
				}
			}
			return closestIntersection;
		}

		/**
		 * Za trenutni piksel baca zraku od položaja promatrača kroz trenutnu
		 * točku za koju se računaju rgb boje tako da pronađe najbliže sjecište
		 * zrake s objektima na sceni i potom komponentama ambijenta nadodaje
		 * difuznu i reflektiranu.
		 * 
		 * @param scene
		 *            scena
		 * @param ray
		 *            zraka
		 * @param rgb
		 *            polje komponenata boje
		 */
		private void caster(Scene scene, Ray ray, short[] rgb) {
			RayIntersection closestIntersection = findClosestIntersection(
					scene, ray);
			// ako sjecište ne postoji, za komponente postavi 0, 0, 0
			if (closestIntersection == null) {
				rgb[0] = rgb[1] = rgb[2] = 0;
			} else {
				determineColor(scene, closestIntersection, ray.start, rgb);
			}
		}

		/**
		 * Određuje boju u presjecištu zrake i objekta postavljajući je na
		 * ambijentnu ako je u sjeni, a ako nije, dodaje joj difuznu i
		 * reflektiranu komponentu.
		 * 
		 * @param scene
		 *            scena
		 * @param closestIntersection
		 *            najbliža točka presjecišta za trenutnu zraku
		 * @param eyePosition
		 *            položaj promatrača
		 * @param rgb
		 *            polje komponenata boja
		 */
		private void determineColor(Scene scene,
				RayIntersection closestIntersection, Point3D eyePosition,
				short[] rgb) {
			// postavi komponente boja na ambijentne
			rgb[0] = rgb[1] = rgb[2] = 15;
			// prodji po svim svjetlosnim izvorima scene
			for (LightSource source : scene.getLights()) {
				// napravi zraku koja je definirana tako da ide od
				// svjetlosnog izvora prema točki presjecišta
				Ray lightRay = Ray.fromPoints(source.getPoint(),
						closestIntersection.getPoint());
				RayIntersection closestLightRayIntersection = findClosestIntersection(
						scene, lightRay);
				// ako postoji sjecište sa svjetlosnom zrakom izračunaj
				// udaljenost od svjetlosnog izvora do tog presjecišta
				double minimumDistance = 0;
				if (closestLightRayIntersection != null) {
					minimumDistance = source.getPoint()
							.sub(closestLightRayIntersection.getPoint()).norm();
				}
				// ako je izračunata udaljenost manja od udaljenosti
				// trenutnog svjetlosnog izvora do točke presjecišta koja je
				// predana, znači da postoji neki objekt koji ga zaklanja,
				// pa se komponente boja postavljaju na ambijentne, inače se
				// računa difuzna i reflektirana komponenta i nadodaje se u
				// polje komponenata boja
				if (!(Math.abs(minimumDistance
						- source.getPoint().sub(closestIntersection.getPoint())
								.norm()) > 1E-9)) {
					addDiffuseAndReflectiveComponent(source,
							closestIntersection, eyePosition, rgb);
				}
			}
		}

		/**
		 * Računa difuznu i refleksiranu komponentu za sve 3 komponente boja za
		 * predan svjetlosni izvor i presjecište.
		 * 
		 * @param source
		 *            svjetlosni izvor
		 * @param closestIntersection
		 *            presjecište za koje se računaju komponente
		 * @param eyePosition
		 *            položaj promatrača
		 * @param rgb
		 *            polje komponenata boja u koje se nadodaju izračunate
		 *            komponente
		 */
		private void addDiffuseAndReflectiveComponent(LightSource source,
				RayIntersection closestIntersection, Point3D eyePosition,
				short[] rgb) {

			// izračunaj difuzne komponente za svaku boju
			Point3D intersectionPoint = closestIntersection.getPoint();
			Point3D l = intersectionPoint.difference(intersectionPoint,
					source.getPoint());
			Point3D n = closestIntersection.getNormal();
			double cosPhi = l.normalize().scalarProduct(n.normalize());
			double sourceR = source.getR();
			double sourceG = source.getG();
			double sourceB = source.getB();
			double diffuseRed = Math.max(0, sourceR * cosPhi
					* closestIntersection.getKdr() * cosPhi);
			double diffuseGreen = Math.max(0, sourceG * cosPhi
					* closestIntersection.getKdg() * cosPhi);
			double diffuseBlue = Math.max(0, sourceB * cosPhi
					* closestIntersection.getKdb() * cosPhi);
			rgb[0] += diffuseRed;
			rgb[1] += diffuseGreen;
			rgb[2] += diffuseBlue;

			// izračunaj reflektirane komponente za svaku boju
			Point3D v = intersectionPoint.difference(intersectionPoint,
					eyePosition);
			cosPhi = Math.max(0,
					reflectedVector(l, n).scalarProduct(v.normalize()));
			cosPhi = Math.pow(cosPhi, closestIntersection.getKrn());
			double reflectiveRed = Math.max(0, sourceR * cosPhi
					* closestIntersection.getKrr() * cosPhi);
			double reflectiveGreen = Math.max(0, sourceG * cosPhi
					* closestIntersection.getKrg() * cosPhi);
			double reflectiveBlue = Math.max(0, sourceB * cosPhi
					* closestIntersection.getKrb() * cosPhi);
			rgb[0] += reflectiveRed;
			rgb[1] += reflectiveGreen;
			rgb[2] += reflectiveBlue;
		}

		/**
		 * Računa reflektirani vektor koji je refleksija vektora v1 oko vektora
		 * v2.
		 * 
		 * @param v1
		 *            vektor koji se reflektira
		 * @param v2
		 *            vektor oko kojeg se reflektira
		 * @return reflektirani vektor
		 */
		private Point3D reflectedVector(Point3D v1, Point3D v2) {
			double scalar = v1.scalarProduct(v2) * 2 / Math.pow(v2.norm(), 2);
			return v2.scalarMultiply(scalar).sub(v1).modifyNormalize();
		}

	}
}
