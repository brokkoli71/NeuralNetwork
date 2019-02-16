package main;

/*
 * es werden zweidimensionale Elemente mit unterschiedlichen Klassifizierungen 
 * (blau bzw. rot) auf ein Koordinatensystem aufgetragen und das Netz wird auf 
 * die Klassifizierungen in Abhaengigkeit zu den Koordinaten trainiert 
 * 
 * anschliessend wird die verbleibende Flaeche nach dem selbsterlernten Muster klassifiziert
 * 
 * die eine Visualle Ausgabe stellt den aktuellen Zustand des Netzes dar, die Andere
 * das zweidimensionale Koordinatensystem
 */

import java.awt.Color;
import java.util.Random;

import gui.Visualisation;
import gui.Visualisation2;
import neuronalesNetz.Netz;
import trainingDataVisualisierungsNetz.TrainingData;

public class MainVisualisierungNetz {
	static Visualisation v;
	static Visualisation2 v2;
	
	//veraenderbare Groessen:
	
	//neuronales Netz
	static int anzahlHiddenLayer = 1;
	static int anzahlHiddenNeuron = 2;
	static double lernrate = 0.001;
	
	//gui
	static double updateRateV1 = 0.0001;
	static double updateRateV2 = 0.0001;
	static int aufloesungV2 = 30;


	/*Zufaellige Daten
	
	//training Data
	static int anzahlPunkte = 10;
		
	static TrainingData trainingData = new TrainingData(2, 1, anzahlPunkte);
	*/
	
	
	//Beispieldaten
	static TrainingData trainingData = TrainingData.getTestData(0);

	static boolean training = true;
	


	
	public static void main(String[] args) throws InterruptedException {
		Netz n = new Netz(trainingData.getInput(0).length, anzahlHiddenNeuron, anzahlHiddenLayer, trainingData.getGoodOutput(0).length);
		n.LERNRATE = lernrate;
		v = new Visualisation(n);
		v2 = new Visualisation2(n, aufloesungV2);

		int counter = 0;

		for (int i = 0; i < trainingData.getDataAmount(); i++) 
			v2.drawpoint(trainingData.getInput(i), trainingData.getGoodOutput(i)[0]<0.5?Color.BLUE:Color.RED);

		
		while (training) {
		counter++;
		if (counter%(1/updateRateV1) == 0) {
			v.updateGrafik();
			v.setTitle("total Error: " + String.format("%.12f", n.getTotalError()));
			}
		if (counter%(1/updateRateV2) == 0) {
			v2.updateGrafik();
		}
		int random = new Random().nextInt(trainingData.getDataAmount());
		n.train(trainingData.getInput(random), trainingData.getGoodOutput(random));
		}	
	}	
}
