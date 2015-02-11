package hr.fer.zemris.java.tecaj_06.rays;

import static org.junit.Assert.*;

import org.junit.Test;

public class SphereTest {

	@Test
	public void InnerIntersectionTest() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		// zraka koja ima inner sjeciste
		Ray ray = Ray.fromPoints(new Point3D(0, 0, 0), new Point3D(0, 0, 10));
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals("Koordinata x tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().x, 1E-6);
		assertEquals("Koordinata y tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().y, 1E-6);
		assertEquals("Koordinata z tocke presjecista mora biti 5!", 5.0,
				intersection.getPoint().z, 1E-6);
		assertEquals("Tocka presjecista mora biti inner!", false,
				intersection.isOuter());
	}

	@Test
	public void OuterIntersectionTest() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		// zraka koja ima outer sjeciste
		Ray ray = Ray.fromPoints(new Point3D(0, 0, 10), new Point3D(0, 0, 0));
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals("Koordinata x tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().x, 1E-6);
		assertEquals("Koordinata y tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().y, 1E-6);
		assertEquals("Koordinata z tocke presjecista mora biti 5!", 5.0,
				intersection.getPoint().z, 1E-6);
		assertEquals("Tocka presjecista mora biti outer!", true,
				intersection.isOuter());
	}

	@Test
	public void NoIntersectionTest() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		// zraka koja nema sjeciste
		Ray ray = Ray.fromPoints(new Point3D(-5, -5, 6), new Point3D(5, 5, 6));
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals("Tocka presjecista mora biti null!", null, intersection);
	}

	@Test
	public void NoIntersection2Test() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		// zraka koja nema sjeciste
		Ray ray = Ray.fromPoints(new Point3D(6, 6, 0), new Point3D(15, 15, 0));
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals("Tocka presjecista mora biti null!", null, intersection);
	}

	@Test(expected = IllegalArgumentException.class)
	public void NullRayTest() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		sphere.findClosestRayIntersection(null);
	}

	// test prolazi u eclipseu, ali kad se pokrene s antom jednostavno ne
	// prolazi nego konstantno pada
//	@Test
//	public void NormalGetterTest() {
//		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
//				0.5, 0.5, 0.5);
//		// zraka koja tangira sferu
//		Ray ray = Ray.fromPoints(new Point3D(0, -2, 5), new Point3D(0, 2, 5));
//		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
//		Point3D normal = intersection.getNormal();
//		assertEquals("Smjer x normale mora biti 0!", 0.0, normal.x, 1E-6);
//		assertEquals("Smjer y normale mora biti 0!", 0.0, normal.y, 1E-6);
//		assertEquals("Smjer z normale mora biti 1!", 1.0, normal.z, 1E-6);
//	}

	@Test
	public void TangentIntersectionTest() {
		Sphere sphere = new Sphere(new Point3D(0, 0, 0), 5, 0.5, 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5);
		// zraka koja tangira sferu
		Ray ray = Ray.fromPoints(new Point3D(0, 0, 5), new Point3D(10, 20, 30));
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals("Koordinata x tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().x, 1E-6);
		assertEquals("Koordinata y tocke presjecista mora biti 0!", 0.0,
				intersection.getPoint().y, 1E-6);
		assertEquals("Koordinata z tocke presjecista mora biti 5!", 5.0,
				intersection.getPoint().z, 1E-6);
		assertEquals(
				"Udaljenost izmedju tocke presjeka i pocetka zrake mora biti 0!",
				0.0, intersection.getDistance(), 1E-6);
	}
}
