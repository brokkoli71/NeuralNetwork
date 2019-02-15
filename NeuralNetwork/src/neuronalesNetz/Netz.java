package neuronalesNetz;

import java.util.ArrayList;
import java.util.Random;


public class Netz {
	
	public double LERNRATE = 0.01;
	private final int HIDDENLAYERCOUNT;
	private final int INPUTNEURONCOUNT;
	private final int HIDDENNEURONCOUNT;
	private final int OUTPUTNEURONCOUNT;
	private final int MAXNEURONCOUNT;

	private double[][] neurons;
	private boolean[][] neuronsIsAlive;
	private double[][] neuronsCost;
	private double[][] deltas;

	
	private double[][][] weights;
	private boolean[][][] weightsIsAlive;
	

	private double[][] bias;
	private boolean[][] biasIsAlive;
	
	private double[] outputError;
	
	public Netz(int inputNeurons, int hiddenNeurons, int hiddenLayers, int outputNeurons) {
		HIDDENLAYERCOUNT = hiddenLayers;
		INPUTNEURONCOUNT = inputNeurons;
		HIDDENNEURONCOUNT = hiddenNeurons;
		OUTPUTNEURONCOUNT = outputNeurons;
		MAXNEURONCOUNT = (HIDDENNEURONCOUNT>INPUTNEURONCOUNT?HIDDENNEURONCOUNT:INPUTNEURONCOUNT);
		
		neurons = new double[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 								//[Layer][Neuron]
		neuronsIsAlive = new boolean[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 						//[Layer][Neuron]
		neuronsCost = new double[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 	
		deltas = new double[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 	


		weights = new double[HIDDENLAYERCOUNT+1][MAXNEURONCOUNT][MAXNEURONCOUNT];				//[FromHiddenLayer][FromHiddenNeuron][ToHiddenNeuron]
		weightsIsAlive = new boolean[HIDDENLAYERCOUNT+1][MAXNEURONCOUNT][MAXNEURONCOUNT];		//[FromHiddenLayer][FromHiddenNeuron][ToHiddenNeuron]
		
		bias = new double[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 									//[Layer][Neuron]
		biasIsAlive = new boolean[HIDDENLAYERCOUNT+2][MAXNEURONCOUNT]; 							//[Neuron]
		
		for (int i = 0; i < INPUTNEURONCOUNT; i++) {
			for (int j = 0; j < HIDDENNEURONCOUNT; j++) {
				weights[0][i][j] = (new Random().nextDouble()*2)-1;
				weightsIsAlive[0][i][j] = true;
			}
		}
		
		for (int i = 1; i < HIDDENLAYERCOUNT; i++) {
			for (int j = 0; j < HIDDENNEURONCOUNT; j++) {
				for (int k = 0; k < HIDDENNEURONCOUNT; k++) {
					weights[i][j][k] = (new Random().nextDouble()*2)-1;
					weightsIsAlive[i][j][k] = true;
				}
			}
		}
		
		for (int i = 0; i < HIDDENNEURONCOUNT; i++) {
			for (int j = 0; j < OUTPUTNEURONCOUNT; j++) {
				weights[HIDDENLAYERCOUNT][i][j] = (new Random().nextDouble()*2)-1;
				weightsIsAlive[HIDDENLAYERCOUNT][i][j] = true;			
				}
		}
		
		for (int i = 1; i < HIDDENLAYERCOUNT+1; i++) {
			for (int j = 0; j < HIDDENNEURONCOUNT; j++) {
				bias[i][j] = (new Random().nextDouble()*2)-1;
				biasIsAlive[i][j] = true;
			}
		}
		
		for (int i = 0; i < OUTPUTNEURONCOUNT; i++) {
			bias[HIDDENLAYERCOUNT+1][i] = (new Random().nextDouble()*2)-1;
			biasIsAlive[HIDDENLAYERCOUNT+1][i] = true;
		}
		
		for (int i = 0; i < INPUTNEURONCOUNT; i++) {
			neuronsIsAlive[0][i] = true;
		}
		
		for (int i = 0; i < HIDDENLAYERCOUNT; i++) {
			for (int j = 0; j < HIDDENNEURONCOUNT; j++) {
				neuronsIsAlive[i+1][j] = true;
			}
		}
		for (int i = 0; i < OUTPUTNEURONCOUNT; i++) {
			neuronsIsAlive[HIDDENLAYERCOUNT+1][i] = true;
		}
	}
	
	
	private void addInput(double[] input) {
		neurons[0] = input;
	}
	

	public void run(double[] input) {
		addInput(input);
		for (int i = 1; i < HIDDENLAYERCOUNT+2; i++) {
			for (int j = 0; j < HIDDENNEURONCOUNT; j++) {
				double sum = 0;
				
				for (int k = 0; k < (i==1?INPUTNEURONCOUNT:HIDDENNEURONCOUNT); k++) {
					sum += neurons[i-1][k] * weights[i-1][k][j];
				}
				
				neurons[i][j] = sigmoidValue((double) sum + bias[i][j]);
			}
		}
	}
	

	public double[] getOutput() {

		double[] output = new double[OUTPUTNEURONCOUNT];
		for (int i = 0; i < output.length; i++) {
			if (neuronsIsAlive[HIDDENLAYERCOUNT+1][i]) {
				output[i] = neurons[HIDDENLAYERCOUNT+1][i];
			}
		} 
		return output;
	}
	
	 public double[] getOutput(double[] input) {
         run(input);
         return getOutput();
     }
	

	
	private void calculateError(double[] goodOutput) {
		
		for (int i = 0; i < goodOutput.length; i++) {
			neuronsCost[HIDDENLAYERCOUNT+1][i] = goodOutput[i] - neurons[HIDDENLAYERCOUNT+1][i];
		}
		for (int i = HIDDENLAYERCOUNT; i > 0; i--) {
			for (int j = 0; j < MAXNEURONCOUNT; j++) {
				if (neuronsIsAlive[i][j]) {
					neuronsCost[i][j] = 0;
					for (int k = 0; k < MAXNEURONCOUNT; k++) {
						neuronsCost[i][j] += neuronsCost[i+1][k] * weights[i][j][k];
					}
					neuronsCost[i][j] = Math.pow(neuronsCost[i][j], 2);
				}
			}
		}
	}	
	
	private void adjustWeightsAndBias() {
		double neuronGradient;
		for (int i = 0; i < HIDDENLAYERCOUNT+1; i++) { 									//fromLayer
			for (int k = 0; k < HIDDENNEURONCOUNT; k++) { 								//toNeuron
				
				neuronGradient = (neurons[i+1][k]*(1-neurons[i+1][k])); 				//dsigmoid of toNeurons
				neuronGradient *= neuronsCost[i+1][k]; 									//errors of toNeurons
				neuronGradient *= LERNRATE;
				
				for (int j = 0; j < ((i==0?INPUTNEURONCOUNT:HIDDENNEURONCOUNT)); j++) { //fromNeuron
					
					if (weightsIsAlive[i][j][k]) {
						weights[i][j][k] +=  neuronGradient * neurons[i][j]; 			// neuronGradient * neurons of 1st layer
					}
				}
				
				if (biasIsAlive[i+1][k]) {												//toNeuron
					bias[i+1][k] += neuronGradient;
				}
			}
		}
	}
	
	private void calculateError2(double[] goodOutput) {
		
		for (int i = 0; i < goodOutput.length; i++) {
			deltas[HIDDENLAYERCOUNT+1][i] = (neurons[HIDDENLAYERCOUNT+1][i] * (1 - neurons[HIDDENLAYERCOUNT+1][i]));
			deltas[HIDDENLAYERCOUNT+1][i] *=  neurons[HIDDENLAYERCOUNT+1][i]-goodOutput[i] ;
			deltas[HIDDENLAYERCOUNT+1][i] *= -1;
			}
		
		for (int i = HIDDENLAYERCOUNT; i > 0; i--) {
			for (int j = 0; j < MAXNEURONCOUNT; j++) {
				
				if (neuronsIsAlive[i][j]) {
					deltas[i][j] = 0;
					for (int k = 0; k < MAXNEURONCOUNT; k++) {
						deltas[i][j] += deltas[i+1][k] * weights[i][j][k];
					}
					deltas[i][j] *= (neurons[i][j] * (1 - neurons[i][j]));
				}
			}
		}
	}
	
	private void adjustWeightsAndBias2() {
		for (int i = 0; i < HIDDENLAYERCOUNT+1; i++) { 									//fromLayer
			for (int k = 0; k < HIDDENNEURONCOUNT; k++) {								//toNeuron
				for (int j = 0; j < ((i==0?INPUTNEURONCOUNT:HIDDENNEURONCOUNT)); j++) { //fromNeuron
					if (weightsIsAlive[i][j][k]) {
						weights[i][j][k] += LERNRATE * deltas[i+1][k] * neurons[i][j];
					}
				}
				if (biasIsAlive[i+1][k]) {//toNeuron
					bias[i+1][k] += LERNRATE * deltas[i+1][k];
				}
			}
		}
	}
	
	public void train(double[] input, double[] goodOutput) {
		run(input);
		calculateError2(goodOutput);
		adjustWeightsAndBias2();
	}
		
	public double getTotalError() {
		double error = 0;
		for (int i = 0; i < OUTPUTNEURONCOUNT; i++) { 
			error += deltas[HIDDENLAYERCOUNT+1][i];
		}
		return error/OUTPUTNEURONCOUNT;
	}
	
	private double sigmoidValue(Double d) {
        return (1 / (1 + Math.exp(-d))); 
    }
	
	public ArrayList<Double> getWeights() {
		ArrayList<Double> allWeights = new ArrayList<Double>();

		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					if (weightsIsAlive[i][j][k]) allWeights.add(2 * sigmoidValue(weights[i][j][k])-1);
				}
			}
		}
		
		return allWeights;
	}
	public ArrayList<Double> getNeurons() {
		ArrayList<Double> allNeurons = new ArrayList<Double>();
		
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				if (neuronsIsAlive[i][j]) allNeurons.add(neurons[i][j]);
			}
		}
		
		return allNeurons;
	}
	
	public ArrayList<Double> getBias() {
		ArrayList<Double> allBias = new ArrayList<Double>();
		
		for (int i = 0; i < bias.length; i++) { 
			for (int j = 0; j < bias[i].length; j++) {
				if (biasIsAlive[i][j]) allBias.add(bias[i][j]);
			}
		}
		
		return allBias;
	}


	public int getHiddenLayerCount() {
		return HIDDENLAYERCOUNT;
	}


	public int getInputNeuronCount() {
		return INPUTNEURONCOUNT;
	}


	public int getHiddenNeuronCount() {
		return HIDDENNEURONCOUNT;
	}


	public int getOutputNeuronCount() {
		return OUTPUTNEURONCOUNT;
	}
	public int getMaxOutputNeuronIndex() {
		double[] output = neurons[HIDDENLAYERCOUNT+1];
		int maximumOutput = 0;
		for (int i = 1; i < OUTPUTNEURONCOUNT; i++) {
			if (output[maximumOutput] < output[i]) {
				maximumOutput = i;
			}
		}
		return maximumOutput;
	}
}
