package cs475;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RegressionAccuracyEcaluator extends Evaluator {

	private LinkedList<Double>predictions;
	private LinkedList<Double>true_labels;
	private Scanner prediction_sc;
	//private Scanner test_sc;
	
	public RegressionAccuracyEcaluator(String predictionFile) throws FileNotFoundException {
		this.prediction_sc = new Scanner(new BufferedInputStream(new FileInputStream(predictionFile)));
		//this.test_sc=new Scanner(new BufferedInputStream(new FileInputStream(testFile) ));
		predictions=new LinkedList<Double>();
		true_labels=new LinkedList<Double>();
	}
	
	@Override
	public double evaluate(List<Instance> instances, Predictor predictor) {
		// TODO Auto-generated method stub
		double total_error=0;
		
		for(Instance i:instances){
			true_labels.add(((RegressionLabel)i.getLabel()).getLabel());
		}
		
		//scan predition files
		while(this.prediction_sc.hasNextLine()){
			String line = this.prediction_sc.nextLine();
			String[] split_line = line.split(" ");
			String label_string = split_line[0];
			predictions.add(Double.parseDouble(label_string));
		}
		
		
		if(predictions.size()!=true_labels.size()){
			System.out.println("Number of lines in two files do not match.");
			return 0;
		}
		for (int i=0;i<predictions.size();i++){
			total_error+=Math.abs(predictions.get(i)-true_labels.get(i));
		}

		return total_error/predictions.size();
	}

}
