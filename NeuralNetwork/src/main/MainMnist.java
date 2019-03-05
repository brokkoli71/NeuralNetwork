package main;

import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.Netz2d;
import gui.Visualisation;
import mnist.TrainSet;
import neuronalesNetz.Netz;

public class MainMnist {
	
	/*	zum Zeichnen im Panel muss einmal geklickt werden um zu zeichen und ein weiteres Mal um das Zeichnen zu beenden
	 * 	mit enter wird best�tigt und die Zeichnung zentriert und klassifiziert
	 * 	c -> clear
	 */
	
	public static TrainSet set, testSet;
	public static int setSize = 16000;
	public static int testSetSize = 1000;
	public static Netz netz;
	public static JLabel l;
	public static void main(String[] args) {
		Netz2d n = new Netz2d(28, 28);
		JFrame f = new JFrame();
		setup(f);
		
		
		netz = new Netz(784, 70, 1, 10); 
        netz.LERNRATE = 0.01;

        try {
        	l.setText("preparing trainSet");
    		set = new TrainSet(0,setSize-1);
    		l.setText("preparing testSet");
            testSet = new TrainSet(31000,30999+testSetSize);
		} catch (IOException e) {
			l.setFont(new Font("", 0, 50));
			f.setSize(1700, f.getHeight());
			l.setText("insert \"trainImage.idx3-ubyte\" and \"trainLabel.idx1-ubyte\" into this directory");
			return;
		}
		
        
        int w = 100000;
        String genauigkeit = ""+ run(w) + "/" + testSetSize;
		System.out.println(""+ w +" Wiederholungen Genauigkeit: " + genauigkeit);
		f.setTitle("Genauigkeit: " + genauigkeit);
		l.setText(""+ w +" Wiederholungen Genauigkeit: " + genauigkeit);
	}
	
	public static void determine(double[] d) {
		netz.run(d);
		l.setText(""+netz.getMaxOutputNeuronIndex());
	}
	




	public static int run(int wiederholungen) {
		
        int random;
        for (int i = 0; i < wiederholungen; i++) {
        	if (i%(wiederholungen/100)==0)  
        		l.setText("training:"+i*100/wiederholungen+"/100");
        	random = new Random().nextInt(setSize);
        	netz.train(set.getInput(random), set.getOutput(random));
        	}
        
        //test Netz
        int treffer = 0;
        int maximumGoodOutput = 0;
        for (int i = 0; i < testSetSize; i++) {
        	netz.run(testSet.getInput(i));
        	double[] output = netz.getOutput();
        	double[] goodOutput = testSet.getOutput(i);
        	int maximumOutput = 0;
        	for (int j = 1; j < 10; j++) {
				if (goodOutput[maximumGoodOutput] < goodOutput[j]) {
					maximumGoodOutput = j;
				}
			}
        	for (int j = 1; j < 10; j++) {
				if (output[maximumOutput] < output[j]) {
					maximumOutput = j;
				}
			}
        	if (maximumGoodOutput == maximumOutput) { 
				treffer++;				
        	}
        }
        return treffer;

	}
	
	private static void setup(JFrame f) {
		f.setVisible(true);
		f.setSize(700, 150);
		f.setLocation(0, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l = new JLabel();
		l.setFont(new Font("", 0, 50));
		f.add(l);
	}

	
}
