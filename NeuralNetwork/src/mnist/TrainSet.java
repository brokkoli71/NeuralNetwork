package mnist;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TrainSet {

    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet (int start, int end) throws IOException{
        this.INPUT_SIZE = 28 * 28;
        this.OUTPUT_SIZE = 10; 
        
        MnistImageFile m;
        MnistLabelFile l;
        	try {
        		 m = new MnistImageFile("res/trainImage.idx3-ubyte", "rw");
                 l = new MnistLabelFile("res/trainLabel.idx1-ubyte", "rw");
        	}catch (IOException e) {
        		m = new MnistImageFile("trainImage.idx3-ubyte", "rw");
        		l = new MnistLabelFile("trainLabel.idx1-ubyte", "rw");
			}
           

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[28 * 28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for(int j = 0; j < 28*28; j++){
                	input[j] = (double)m.read() / (double)256;
                }

                this.addData(input, output);
                m.next();
                l.next();
            }
        
    }

    public void addData(double[] in, double[] expected) {
        if(in.length != INPUT_SIZE || expected.length != OUTPUT_SIZE) return;
        data.add(new double[][]{in, expected});
    }



    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }
    
    

}
