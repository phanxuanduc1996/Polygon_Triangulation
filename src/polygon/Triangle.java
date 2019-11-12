package polygon;

import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author THANHTV A class to represent a triangle Note that all three points
 *         should be different in order to work properly
 */
public class Triangle {

	// coordinates
	private Point a;
	private Point b;
	private Point c;

	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public void draw(Graphics g) {
		g.drawLine(a.x, a.y, b.x, b.y);
		g.drawLine(b.x, b.y, c.x, c.y);
		g.drawLine(c.x, c.y, a.x, a.y);
	}

	public boolean isInside(Point p) {
		// interpret v1 and v2 as vectors
		Point v1 = new Point(b.x - a.x, b.y - a.y);
		Point v2 = new Point(c.x - a.x, c.y - a.y);

		double det = v1.x * v2.y - v2.x * v1.y;
		Point tmp = new Point(p.x - a.x, p.y - a.y);
		double lambda = (tmp.x * v2.y - v2.x * tmp.y) / det;
		double mue = (v1.x * tmp.y - tmp.x * v1.y) / det;

		return (lambda >= 0 && mue >= 0 && (lambda + mue) <= 1);
	}

	public static boolean isInside(Point x, Point y, Point z, Point p) {
		Point v1 = new Point(y.x - x.x, y.y - x.y);
		Point v2 = new Point(z.x - x.x, z.y - x.y);

		double det = v1.x * v2.y - v2.x * v1.y;
		Point tmp = new Point(p.x - x.x, p.y - x.y);
		double lambda = (tmp.x * v2.y - v2.x * tmp.y) / det;
		double mue = (v1.x * tmp.y - tmp.x * v1.y) / det;

		return (lambda > 0 && mue > 0 && (lambda + mue) < 1);
	}

}
