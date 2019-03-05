package gui;


import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import gui.Grafik;
import neuronalesNetz.Netz;

public class Visualisation extends JFrame{
	Netz netz;
	ArrayList<Double> allWeights;
	ArrayList<Double> allNeurons;
	ArrayList<Double> allBias;
	Grafik g;

	public Visualisation(Netz netz) {
		this.netz = netz;
		
		int spaltenHidden = netz.getHiddenLayerCount();
		int anzahlInput = netz.getInputNeuronCount();
		int anzahlOutput = netz.getOutputNeuronCount();
		int anzahlHidden = netz.getHiddenNeuronCount();
		
		g = new Grafik(anzahlInput, anzahlHidden, spaltenHidden, anzahlOutput);
		this.add(g);

		this.setSize(1500, 1050);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

	public void updateGrafik() {
		allWeights = netz.getWeights();
		allNeurons = netz.getNeurons();
		allBias = netz.getBias();
		
		
		for (int i = 0; i < allWeights.size(); i++) {
			g.lines.get(i).setWert(allWeights.get(i));
		}
		for (int i = 0; i < allBias.size(); i++) {
			Double w = allBias.get(i);
			if (w>1) 
				w = 1d;
			if (w<0)
				w = 0d;
			g.neurons.get(i).setWert(w);
		}
		
//		for (int i = 0; i < allNeurons.size(); i++) {
//			g.neurons.get(i).setWert(allNeurons.get(i));
//		}
		for (int i = 0; i < allBias.size(); i++) {
			g.neurons.get(i).setBias(allBias.get(i));
		}
		
		g.repaint();
		
	}
	

	public void setNetz(Netz netz) {
		this.netz = netz;
	}
}
