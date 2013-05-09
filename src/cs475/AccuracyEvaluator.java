package cs475;

import java.util.List;

public class AccuracyEvaluator extends Evaluator {

	boolean debug=true;
	@Override
	public double evaluate(List<Instance> instances, Predictor predictor) {
		// TODO Auto-generated method stub
		int matches=0;
		for (Instance i:instances){
			ClassificationLabel label=(ClassificationLabel)i.getLabel();
			if(label.compareTo((ClassificationLabel)predictor.predict(i))==0){
				++matches;
			}
		}
		if(debug){
			System.out.println("AccuracyEvaluator: matches "+matches);
		}
		return (matches+0.0)/instances.size();
	}

}
