package guiComponents;

import java.awt.Point;

public class Line {
	private double wert;
	private Point fromPoint, toPoint;
	private int id;
	static int counter = 0;
	
	public Line() {
		this.id = counter++;
	}
	
	public int getId() {
		return id;
	}

	public double getWert() {
		return wert;
	}
	
	public Point getTextPoint() {
		return new Point(fromPoint.x + (toPoint.x - fromPoint.x)/3, fromPoint.y + (toPoint.y - fromPoint.y)/3);
	}

	public void setWert(double wert) {
		this.wert = wert;
	}

	public Point getFromPoint() {
		return fromPoint;
	}

	public void setFromPoint(Point fromPoint) {
		this.fromPoint = fromPoint;
	}

	

	public Point getToPoint() {
		return toPoint;
	}

	public void setToPoint(Point toPoint) {
		this.toPoint = toPoint;
	}

	
	
	
}
