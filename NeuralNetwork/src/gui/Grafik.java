package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import guiComponents.Line;
import guiComponents.Neuron;

public class Grafik extends JPanel{

	int spalten, anzahlInput, anzahlOutput, anzahlHidden;
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    ArrayList<Neuron> from = new ArrayList<Neuron>();

	
	public Grafik(int anzahlInput,int anzahlHidden,int spaltenHidden,int anzahlOutput) {
		super();
		this.spalten = spaltenHidden+2;
		this.anzahlInput = anzahlInput;
		this.anzahlOutput = anzahlOutput;
		this.anzahlHidden = anzahlHidden;
		for (int i = 0; i < spalten; i++) {
			int anzahlNeuronen = i==0?anzahlInput:i==spalten-1?anzahlOutput:anzahlHidden;
			int anzahlFromNeuronen = i==0?0:i==1?anzahlInput:anzahlHidden;
			for (int j = 0; j < anzahlNeuronen; j++) {
				neurons.add(new Neuron());
				for (int k = 0; k < anzahlFromNeuronen; k++) {
					lines.add(new Line());
				}
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {  
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int counter = 0;
		int counter2 = 0;

        int abstandX = this.getWidth()/(2 * spalten - 1); // == durchmesser
        
        for (int i = 0; i < spalten; i++) {
        	
        	
        	int anzahlNeuronen = i==0?anzahlInput:i==spalten-1?anzahlOutput:anzahlHidden;
        	
        	int merkeCounter = counter;
        	for (int j = 0; j < anzahlNeuronen; j++) {
        		Neuron n = neurons.get(counter++);
        		n.setX1((2*i) * abstandX);
        		n.setX2(abstandX);
            	if (this.getHeight()<abstandX*anzahlNeuronen) {
        			int sizeY = this.getHeight()/anzahlNeuronen;
        			n.setY1(j * sizeY);
        			n.setY2(sizeY);
            	}else {
    				int abstandY = (this.getHeight() - (anzahlNeuronen * abstandX))/(anzahlNeuronen+1);
    				n.setY1(j * abstandX + (j+1) * abstandY);
    				n.setY2(abstandX);
            	}

            	for (Neuron f : from) {
            		Line l = lines.get(counter2++);
            		l.setFromPoint(f.getFromPoint());
            		l.setToPoint(n.getToPoint());
				}
        	}
        	from.clear();
        	if (i!=spalten-1) {
        		for (int j = merkeCounter; j < counter; j++) {
            		Neuron n = neurons.get(j);
            		from.add(n);
    			}
			}
  		}
        
        for (Neuron n : neurons) {
        	g.setColor(new Color(1-(float) n.getWert(), 1-(float) n.getWert(), 1-(float) n.getWert())); 
			g.fillOval(n.getX1(), n.getY1(), n.getX2(), n.getY2());
			g.setColor(Color.red);
			g.drawString(Double.toString(n.getWert()), n.getTextPoint().x, n.getTextPoint().y);
			g.drawString((n.getBias()>=0?"  +":"  ") + Double.toString(n.getBias()), n.getTextPoint().x, n.getTextPoint().y + 10);
			
		}
        
        for (Line l : lines) {
        	if (l.getWert()<0) {
        		g.setColor(new Color( 1,  (float)(1+l.getWert()), (float) (1+l.getWert())));
			}else {
				g.setColor(new Color( (float)(1-l.getWert()),  (float)(1-l.getWert()), 1));
			}

        	Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
			g.drawLine(l.getFromPoint().x, l.getFromPoint().y, l.getToPoint().x, l.getToPoint().y);
			
			g.setColor(Color.blue);
			g.fillOval(l.getTextPoint().x, l.getTextPoint().y, 10, 10);
			g.drawString(Double.toString(l.getWert()), l.getTextPoint().x, l.getTextPoint().y);
		}
	}


}
