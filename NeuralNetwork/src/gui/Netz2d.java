package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.MainMnist;
import neuronalesNetz.Netz;

public class Netz2d extends JFrame{
	public static int COUNTPIXELX; 
	public static int COUNTPIXELY; 
	
	JPanel[][] pixel;
	double[][] pixelData;
	
	public int mouseDown = 0;
	
	public Netz2d() {
		this(25);
	}
	public Netz2d(int countPixelX) {
		this(countPixelX, (int) (countPixelX/0.7));
	}
	public Netz2d(int countPixelX, int countPixelY) {
		COUNTPIXELX = countPixelX;
		COUNTPIXELY = countPixelY;
		setSize(700, 700/COUNTPIXELY*COUNTPIXELX);

		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(COUNTPIXELX, COUNTPIXELY));
		
		pixel = new JPanel[COUNTPIXELX][COUNTPIXELY];
		pixelData = new double[COUNTPIXELX][COUNTPIXELY];
		
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				
				final int x = i;
				final int y = j;
				
				pixelData[x][y] = 0;
				pixel[x][y] = new JPanel(); 
				panel.add(pixel[x][y]);
				pixel[x][y].addMouseMotionListener( new MouseMotionListener() {

					@Override
					public void mouseDragged(MouseEvent arg0) {
					}

					@Override
					public void mouseMoved(MouseEvent arg0) {
						if (mouseDown == 1) {
							addToPixel(x, y, 1);
							
							addToPixel(x+1,y,0.05);					
							addToPixel(x-1,y,0.05);
							addToPixel(x,y+1,0.05);
							addToPixel(x,y-1,0.05);
							
							addToPixel(x+1,y-1,0.005);
							addToPixel(x+1,y+1,0.005);
							addToPixel(x-1,y+1,0.005);
							addToPixel(x-1,y-1,0.005);
							
							addToPixel(x+2,y,0.005);
							addToPixel(x-2,y,0.005);
							addToPixel(x,y+2,0.005);
							addToPixel(x,y-2,0.005);

							updatePixels();
						}else if (mouseDown == 2) {
							pixelData[x][y] = 0;
							updatePixels();
						}
					}
					
				});
				pixel[x][y].addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent arg0) {
					}
					
					@Override
					public void mousePressed(MouseEvent arg0) {
						if (mouseDown == 0) {
							if (arg0.getButton() == 1) 
								mouseDown = 1;
							else 
								mouseDown = 2;
						}else
							mouseDown = 0;
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) {
					}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {
					}
					
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
					}
				});
			}
		}
			
		
		add(panel);
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {

				case 67:
					clear();
					
					break;
				//backspace
				case 8:
					MainMnist.determine(getPixelData());
					break;
				//enter
				case 10:
					center();
					MainMnist.determine(getPixelData());
					break;
					
				case 82:
					for (int i = 0; i < COUNTPIXELX; i++) {
						for (int j = 0; j < COUNTPIXELY; j++) {
							pixelData[i][j] = Math.round(pixelData[i][j]);
							updatePixels();
						}
					}
					break;
				case 88:
					center();
					break;
					
				}
				updatePixels();
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		updatePixels();

	}

	public void center() {
		double xShift = 0;
		double yShift = 0;
		double xCenter = (COUNTPIXELX-1)/2;
		double yCenter = (COUNTPIXELY-1)/2;
		double gesamt = 0;
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				xShift += (i-xCenter) * pixelData[i][j];
				yShift += (j-yCenter) * pixelData[i][j];
				gesamt += pixelData[i][j];
			}
		}
		xShift/=gesamt;
		yShift/=gesamt;

		
		double[][] oldPixelData = new double[COUNTPIXELX][COUNTPIXELY];
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				oldPixelData[i][j] = pixelData[i][j];
			}
		}
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				if (i+xShift>=0&&(i+xShift)<COUNTPIXELX&&j+yShift>=0&&(j+yShift)<COUNTPIXELY) {
						pixelData[i][j] = oldPixelData[(int) (i+xShift)][(int) (j+yShift)];
				}else {
					pixelData[i][j] = 0;
				}
			}
		}
	}
	
	public double[] getPixelData() {
		double[] d = new double[COUNTPIXELX*COUNTPIXELY];
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				//d[j+i*COUNTPIXELY] = Math.round(pixelData[i][j]);
				d[j+i*COUNTPIXELY] = pixelData[i][j];
			}
		}
		return d;
	}

	public void display(double[] d) {
		int counter = 0;
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				double wert =  d[counter++];
				pixelData[i][j] =wert;
				updatePixels();
			}
		}
	} 
	private Color getColor(double wert) {
		return new Color((float)(wert),(float) (wert),(float) (wert));
	}
	private void updatePixels() {
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				if (pixelData[i][j]>1)
					pixelData[i][j]=1;
				getColor(pixelData[i][j]);
				pixel[i][j].setBackground(getColor(pixelData[i][j]));
			}
		}
	}
	
	private void addToPixel(int x, int y, double d) {
		try {
			pixelData[x][y]+= d;
		} catch (Exception e) {}
	}
	private void clear() {
		for (int i = 0; i < COUNTPIXELX; i++) {
			for (int j = 0; j < COUNTPIXELY; j++) {
				pixelData[i][j] = 0;
			}
		}
	}


}
