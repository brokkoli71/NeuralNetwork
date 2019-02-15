package trainingDataVisualisierungsNetz;

import java.util.Random;

public class TrainingData {
	double input[][], goodOutput[][];
	
	public TrainingData(int inputCount, int goodOutputCount, int pointsCount) {
		input = new double[pointsCount][inputCount];
		goodOutput = new double[pointsCount][goodOutputCount];

		for (int j = 0; j < pointsCount; j++) {
			for (int i = 0; i < inputCount; i++) {
				input[j][i] = new Random().nextDouble();
			}
			
			for (int i = 0; i < goodOutputCount; i++) {
				goodOutput[j][i] = new Random().nextInt(2);
			}
		}
	}
	public TrainingData(double input[][], double goodOutput[][]) {
		this.input = input;
		this.goodOutput = goodOutput;
	}

	public double[] getInput(int i) {
		return input[i];
	}
	public double[] getGoodOutput(int i) {
		return goodOutput[i];
	}
	public int getDataAmount() {
		return input.length;
	}
	public static TrainingData getTestData(int i) {
		switch (i) {
		case 0:
			return new TrainingData(	new double[][]{	{0, 1},
														{0, 0},
														{1, 1},
														{1, 0}	}, 
										new double[][]{	{1},
														{0},
														{0},
														{1} 	});
		case 1:return new TrainingData(	new double[][]{	{0.4, 1},
														{0.2, 0},
														{0.8, 1},
														{0, 0.5},
														{1, 0}	}, 
											new double[][]{	{1},
														{0},
														{0},
														{0},
														{1} 	});
		default:
			return null;
		}
	}
}

