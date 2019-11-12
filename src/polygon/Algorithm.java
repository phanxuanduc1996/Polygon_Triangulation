package polygon;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

public class Algorithm {

	private Vector<Point> points;
	private Vector<Point> nonconvexPoints;
	private Vector<Triangle> triangles;

	// orientation of polygon - true = clockwise, false = counterclockwise
	private boolean isCw;
	private PolygonDraw drawPanel;

	public Algorithm(Vector<Point> points, PolygonDraw panel) {
		// we have to copy the point vector as we modify it
		this.points = new Vector<Point>();
		for (int i = 0; i < points.size(); i++)
			this.points.add(new Point(points.get(i)));

		nonconvexPoints = new Vector<Point>();
		triangles = new Vector<Triangle>();

		calcPolyOrientation();
		calcNonConvexPoints();
		drawPanel = panel;
	}

	/*
	 * This determines all concave vertices of the polygon.
	 */
	private void calcNonConvexPoints() {
		// safety check, with less than 4 points we have to do nothing
		if (points.size() <= 3)
			return;

		// actual three points
		Point p;
		Point v;
		Point u;
		// result value of test function
		int res = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			p = points.get(i);
			Point tmp = points.get(i + 1);
			v = new Point(); // interpret v as vector from i to i+1
			v.x = tmp.x - p.x;
			v.y = tmp.y - p.y;

			// ugly - last polygon segment goes from last point to first point
			if (i == points.size() - 2)
				u = points.get(0);
			else
				u = points.get(i + 2);

			res = u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x;
			// note: cw means res/newres is <= 0
			if ((res > 0 && isCw) || (res <= 0 && !isCw)) {
				nonconvexPoints.add(tmp);
				System.out.println("Concave point #" + (i + 1) + "  Coordinates: " + tmp.x + "/" + tmp.y);
			}

		}
	}

	/*
	 * Get the orientation of the polygon - clockwise (cw) or counter-clockwise
	 * (ccw)
	 */
	private void calcPolyOrientation() {
		if (points.size() < 3)
			return;

		// first find point with minimum x-coord - if there are several ones take
		// the one with maximal y-coord
		int index = 0; // index of point in vector to find
		Point pointOfIndex = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < pointOfIndex.x) {
				pointOfIndex = points.get(i);
				index = i;
			} else if (points.get(i).x == pointOfIndex.x && points.get(i).y > pointOfIndex.y) {
				pointOfIndex = points.get(i);
				index = i;
			}
		}

		// get vector from index-1 to index
		Point prevPointOfIndex;
		if (index == 0)
			prevPointOfIndex = points.get(points.size() - 1);
		else
			prevPointOfIndex = points.get(index - 1);
		Point v1 = new Point(pointOfIndex.x - prevPointOfIndex.x, pointOfIndex.y - prevPointOfIndex.y);
		// get next point
		Point succPointOfIndex;
		if (index == points.size() - 1)
			succPointOfIndex = points.get(0);
		else
			succPointOfIndex = points.get(index + 1);

		// get orientation
		int res = succPointOfIndex.x * v1.y - succPointOfIndex.y * v1.x + v1.x * prevPointOfIndex.y
				- v1.y * prevPointOfIndex.x;

		isCw = (res <= 0 ? true : false);
		System.out.println("Clockwise : " + isCw);
	}

	/*
	 * Returns true if the triangle formed by the three given points is an ear
	 * considering the polygon - thus if no other point is inside and it is convex.
	 * Otherwise false.
	 */
	private boolean isEar(Point p1, Point p2, Point p3) {
		// not convex, bye
		if (!(isConvex(p1, p2, p3)))
			return false;

		// iterate over all Concave points and check if one of them lies inside the given
		// triangle
		for (int i = 0; i < nonconvexPoints.size(); i++) {
			if (Triangle.isInside(p1, p2, p3, nonconvexPoints.get(i)))
				return false;
		}
		return true;
	}

	/*
	 * Returns true if the point p2 is convex considered the actual polygon. p1, p2
	 * and p3 are three consecutive points of the polygon.
	 */
	private boolean isConvex(Point p1, Point p2, Point p3) {
		Point v = new Point(p2.x - p1.x, p2.y - p1.y);
		int res = p3.x * v.y - p3.y * v.x + v.x * p1.y - v.y * p1.x;
		return !((res > 0 && isCw) || (res <= 0 && !isCw));
	}

	/*
	 * This is a helper function for accessing consecutive points of the polygon
	 * vector. It ensures that no IndexOutofBoundsException occurs.
	 * 
	 * @param index is the base index of the point to be accessed
	 * 
	 * @param offset to be added/subtracted to the index value
	 */
	private int getIndex(int index, int offset) {
		int newindex;
		System.out.println("size " + points.size() + " index:" + index + " offset:" + offset);
		if (index + offset >= points.size())
			newindex = points.size() - (index + offset);
		else {
			if (index + offset < 0)
				newindex = points.size() + (index + offset);
			else
				newindex = index + offset;
		}
		System.out.println("new index = " + newindex);
		return newindex;
	}

	/*
	 * The actual Kong's Triangulation Algorithm
	 */
	public void runKong(boolean stepbystep, int pausetime) {
		if (points.size() <= 3)
			return;

		triangles.clear();
		int index = 1;

		while (points.size() > 3) {

			// step by step mode - then pause
			if (stepbystep) {
				// draw at first the current polygon that is left
				drawPanel.drawPolygon(points);
				// draw the already calcluated part of the triangulation
				drawPanel.drawTriangles(triangles, Color.BLUE);
				// draw the triangle that is currently under observation
				drawPanel.drawTriangleFromPoints(points.get(getIndex(index, -1)), points.get(index),
						points.get(getIndex(index, 1)), Color.RED);
				try {
					Thread.sleep(pausetime);
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			if (isEar(points.get(getIndex(index, -1)), points.get(index), points.get(getIndex(index, 1)))) {
				// cut ear
				triangles.add(new Triangle(points.get(getIndex(index, -1)), points.get(index),
						points.get(getIndex(index, 1))));
				/*
				 * if (nonconvexPoints.contains(points.get(getIndex(index, -1))) &&
				 * isConvex(points.get(getIndex(index, -2)), points.get(getIndex(index, -1)),
				 * points.get(getIndex(index, 1))) )
				 * nonconvexPoints.remove(points.get(getIndex(index, -1)));
				 * 
				 * if (nonconvexPoints.contains(points.get(getIndex(index, 1))) &&
				 * isConvex(points.get(getIndex(index, -1)), points.get(getIndex(index, 1)),
				 * points.get(getIndex(index, 2))) )
				 * nonconvexPoints.remove(points.get(getIndex(index, 1)));
				 */
				points.remove(points.get(index));

				index = getIndex(index, -1);

			} else {
				index = getIndex(index, 1);
			}
		}
		// add last triangle
		triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));

	}

	public Vector<Triangle> getTriangles() {
		return triangles;
	}

}
