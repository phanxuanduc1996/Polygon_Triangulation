package polygon;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PolygonDraw extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Color backgroundColor = new Color(250, 250, 250);
	public static final int POINTSIZE = 7;
	public static final int POINTSIZE2 = 3;

	private Vector<Point> polypoints = new Vector<Point>(); // vertices of polygon
	private boolean isPolyClosed = false; // finished drawing polygon?

	private int mouseX = -10; // current x-position of cursor on panel
	private int mouseY = -10; // current y-position of cursor on panel
	private boolean isDragging = false; // if user draggs a polygon point
	private Point draggedPoint; // if dragging, this holds the dragged point
	private Vector<Triangle> triangles; // result of Kong algo

	public PolygonDraw(int width, int height) {
		this.setLayout(null);
		this.setBounds(0, 0, width, height);
		this.setBackground(backgroundColor);
		triangles = null;

		// handle mouse actions
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();
				if (IsOverPoint(event.getX(), event.getY()))
					getThisPanel().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				else
					getThisPanel().setCursor(Cursor.getDefaultCursor());

				if (!isPolyClosed)
					repaint();
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				if (isDragging && draggedPoint != null) {
					draggedPoint.x = event.getX() + POINTSIZE2;
					draggedPoint.y = event.getY() + POINTSIZE2;
					// System.out.println("draggin");
					repaint();
				}
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent event) {
				isDragging = false;
				getThisPanel().setCursor(Cursor.getDefaultCursor());
			}

			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1) {
					// first check if we are dragging
					if (IsOverPoint(event.getX(), event.getY())) {
						draggedPoint = getClickedPoint(event.getX(), event.getY());
						isDragging = true;
						getThisPanel().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						// repaint();
					}
					// if we cannot add more points, bye
					if (isPolyClosed)
						return;

					// check if polygon should be closed
					if ((polypoints.size() > 2) && (Math.abs(event.getX() - polypoints.firstElement().x) < 5)
							&& (Math.abs(event.getY() - polypoints.firstElement().y) < 5)) {
						isPolyClosed = true;
						// polypoints.add(polypoints.firstElement());
						System.out.println("Polygon closed");
					} else {
						polypoints.add(new Point(event.getX(), event.getY()));
					}
					repaint();
				} else if (event.getButton() == MouseEvent.BUTTON3) {
					Point p = getClickedPoint(event.getX(), event.getY());
					if (p == null)
						return;
					// here we clicked a point so remove it
					polypoints.remove(p);
					if (polypoints.size() < 3)
						isPolyClosed = false;
					repaint();
				}
			}
		});
	}

	public PolygonDraw getThisPanel() {
		return this;
	}

	public void drawTriangles(Vector<Triangle> tris, Color color) {
		Graphics g = this.getGraphics();
		Color oldColor = g.getColor();
		g.setColor(color);

		g.setColor(Color.BLUE);
		for (int i = 0; i < tris.size(); i++)
			tris.get(i).draw(g);

		g.setColor(oldColor);

	}

	public void drawTriangleFromPoints(Point p1, Point p2, Point p3, Color color) {
		Graphics g = this.getGraphics();
		Color oldColor = g.getColor();
		g.setColor(color);

		g.drawLine(p1.x, p1.y, p2.x, p2.y);
		g.drawLine(p2.x, p2.y, p3.x, p3.y);
		g.drawLine(p3.x, p3.y, p1.x, p1.y);

		g.setColor(oldColor);
	}

	public void drawPolygon(Vector<Point> points) {
		Graphics g = this.getGraphics();

		Color c = g.getColor();
		g.setColor(PolygonDraw.backgroundColor);
		g.fillRect(0, 0, this.getBounds().width, this.getBounds().height);
		g.setColor(c);

		// draw the point rectangles
		Point p;
		for (int i = 0; i < points.size(); i++) {
			p = points.get(i);
			g.fillRect(p.x - POINTSIZE2, p.y - POINTSIZE2, POINTSIZE, POINTSIZE);
			g.drawString(i + "", p.x + 10, p.y + 10);
		}

		// draw the connecting lines
		if (points.size() < 1)
			return;
		Point p2;
		for (int i = 0; i < points.size() - 1; i++) {
			p = points.get(i);
			p2 = points.get(i + 1);
			g.drawLine(p.x, p.y, p2.x, p2.y);
		}

		g.drawLine(points.get(points.size() - 1).x, points.get(points.size() - 1).y, points.get(0).x, points.get(0).y);
	}

	/*
	 * Here goes all the painting
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// draw the point rectangles
		Point p;
		for (int i = 0; i < polypoints.size(); i++) {
			p = polypoints.get(i);
			g.fillRect(p.x - POINTSIZE2, p.y - POINTSIZE2, POINTSIZE, POINTSIZE);
		}

		// draw the connecting lines
		if (polypoints.size() < 1)
			return;
		Point p2;
		for (int i = 0; i < polypoints.size() - 1; i++) {
			p = polypoints.get(i);
			p2 = polypoints.get(i + 1);
			g.drawLine(p.x, p.y, p2.x, p2.y);
		}

		// draw triangles if they are already calculated
		if (triangles != null) {
			g.setColor(Color.BLUE);
			for (int i = 0; i < triangles.size(); i++)
				triangles.get(i).draw(g);
		}

		// if polygon is completed, draw line from last to first point and work is done
		if (this.isPolyClosed) {
			g.drawLine(polypoints.get(polypoints.size() - 1).x, polypoints.get(polypoints.size() - 1).y,
					polypoints.get(0).x, polypoints.get(0).y);
			return;
		}

		// draw current line
		g.drawLine(polypoints.lastElement().x, polypoints.lastElement().y, mouseX, mouseY);

		// draw string to close line
		if ((polypoints.size() > 2) && (Math.abs(mouseX - polypoints.firstElement().x) < 5)
				&& (Math.abs(mouseY - polypoints.firstElement().y) < 5)) {
			g.drawString("Closing polygon", mouseX + 5, mouseY + 5);
		}
	}

	/*
	 * Returns the clicked point; if no point was clicked it returns null
	 */
	public Point getClickedPoint(int x, int y) {
		Point p;
		for (Iterator<Point> iter = polypoints.iterator(); iter.hasNext();) {
			p = iter.next();
			if (x >= p.x - POINTSIZE2 && x <= p.x + POINTSIZE2 && y >= p.y - POINTSIZE2 && y <= p.y + POINTSIZE2)
				return p;
		}
		return null;
	}

	/*
	 * Returns true if given coordinates match a polygon point, otherwise false
	 */
	public boolean IsOverPoint(int x, int y) {
		Point p;
		for (Iterator<Point> iter = polypoints.iterator(); iter.hasNext();) {
			p = iter.next();
			if (x >= p.x - POINTSIZE2 && x <= p.x + POINTSIZE2 && y >= p.y - POINTSIZE2 && y <= p.y + POINTSIZE2)
				return true;
		}
		return false;
	}

	void ApplyKongAlgo(boolean stepbystep, int pausetime) {
		// this algorithmus is only applicable to simple polygons !!!
		for (int i = 0; i < polypoints.size() - 1; i++)
			for (int j = 0; j < polypoints.size() - 1; j++) {
				if (i == j)
					continue;
				if (isIntersect(polypoints.get(i), polypoints.get(i + 1), polypoints.get(j), polypoints.get(j + 1))) {
					JOptionPane.showMessageDialog(null, "Polygon is not simple!");
					return;
				}
			}

		Algorithm ka = new Algorithm(polypoints, this);
		ka.runKong(stepbystep, pausetime); // actual algo call
		triangles = ka.getTriangles();

		// draw original polygon
		Graphics g = this.getGraphics();
		g.setColor(Color.BLACK);
		this.drawPolygon(polypoints);

		System.out.println("DONE");
		repaint();
	}

	boolean isIntersect(Point p1, Point p2, Point p3, Point p4) {
		// abuse Point as mathematical vector here
		Point v = new Point(p2.x - p1.x, p2.y - p1.y);
		Point w = new Point(p4.x - p3.x, p4.y - p3.y);

		double det = v.x * w.y - w.x * v.y;
		if (det != 0) // the lines through the sections intersect - now check this for the actual
						// sections
		{
			Point tmp = new Point(p3.x - p1.x, p3.y - p1.y);
			double lambda = (tmp.x * w.y - tmp.y * w.x) / det;
			double mue = (v.x * tmp.y - tmp.x * v.y) / det;
			mue = -mue;
			return ((0 < lambda && lambda < 1) && (0 < mue && mue < 1));
		} else {
			// to do
			// lines are parallel here, so calculate dist
			return false;
		}
	}

	void ResetPolygon() {
		polypoints.clear();
		isPolyClosed = false;
		isDragging = false;
		draggedPoint = null;
		triangles = null;
		repaint();
	}

}
