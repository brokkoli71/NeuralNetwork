package guiComponents;

import java.awt.Point;
import java.util.Random;

public class Neuron {

	private double wert, bias;
	private int x1, y1, x2, y2, id;
	static int counter = 0;
	
	public Neuron() {
		this.id = counter++;
		this.wert = new Random().nextDouble();
	}
	
	public Point getFromPoint() {
		return new Point(x1+x2, y2/2 + y1);
	} 
	
	public Point getToPoint() {
		return new Point(x1, y2/2 + y1);
	} 
	
	public Point getTextPoint() {
		return new Point(x2/4 + x1, y2/2 + y1);
	}
	
	public int getId() {
		return id;
	}


	public int getX1() {
		return x1;
	}


	public void setX1(int x1) {
		this.x1 = x1;
	}


	public int getY1() {
		return y1;
	}


	public void setY1(int y1) {
		this.y1 = y1;
	}


	public int getX2() {
		return x2;
	}


	public void setX2(int x2) {
		this.x2 = x2;
	}


	public int getY2() {
		return y2;
	}


	public void setY2(int y2) {
		this.y2 = y2;
	}


	public double getWert() {
		return wert;
	}


	public void setWert(double wert) {
		this.wert = wert;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}


}
