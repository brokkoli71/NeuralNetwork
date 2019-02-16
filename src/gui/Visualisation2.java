package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import neuronalesNetz.Netz;

public class Visualisation2 extends JFrame{

	public static int COUNTPIXELX = 30;
	public static int COUNTPIXELY = (int) (COUNTPIXELX/0.7);
	JPanel[][] pixel;
	boolean[][] pixelNotAdjustable;
	Netz netz;

	
	public Visualisation2(Netz netz, int COUNTPIXELX) {
		Visualisation2.COUNTPIXELX = COUNTPIXELX;
		COUNTPIXELY = (int) (COUNTPIXELX/0.7);
		this.netz = netz;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(COUNTPIXELX, COUNTPIXELY));
		pixel = new JPanel[COUNTPIXELX][COUNTPIXELY];
		pixelNotAdjustable = new boolean[COUNTPIXELX][COUNTPIXELY];

		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				
				final int x = i;
				final int y = j;
				
				pixel[x][y] = new JPanel();
				pixel[x][y].addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent arg0) {
					}
					
					@Override
					public void mousePressed(MouseEvent arg0) {
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) {
					}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {
					}
					
					@Override
					public void mouseClicked(MouseEvent arg0) {
						arg0.getButton();
					}
				});
				panel.add(pixel[x][y]);
				pixel[x][y].setBackground(Color.white);
			}
		}
		add(panel);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1500, 1050);
		setLocation(1000, 500);
		setAlwaysOnTop(true);
	}

	public void updateGrafik() {
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				netz.run(new double[] {(double)i/COUNTPIXELX, (double)j/COUNTPIXELY});
				float wert = (float) netz.getOutput()[0];
				if (!pixelNotAdjustable[i][j])
				pixel[i][j].setBackground(new Color(wert, wert, wert));
			}
		}
	}

	public void setNetz(Netz netz) {
		this.netz = netz;
	}
	
	public void drawpoint(double[] point, Color c) {
		int x = (int) (point[0]*(COUNTPIXELX-1));
		int y = (int) (point[1]*(COUNTPIXELY-1));
		pixel[x][y].setBackground(c);
		pixelNotAdjustable[x][y] = true;
	}
}
