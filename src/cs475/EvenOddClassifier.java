package cs475;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class EvenOddClassifier extends Predictor implements Serializable {
	//int toLabel;
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub


	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		
//		ClassificationLabel classLabel=(ClassificationLabel)instance.getLabel();
//		if(classLabel.getLabel()!=-1){
//			return classLabel;
//		}
		
		int odd=0;
		int even=0;
		Iterator<Entry<Integer, Double>> itr=instance.getFeatureVector().itr();
		while(itr.hasNext()){
			Entry<Integer, Double> currEntry=itr.next();
			if(currEntry.getKey()%2==0){
				even+=currEntry.getValue();
			}
			else
				odd+=currEntry.getValue();;
		}

		if(even>=odd){
			return new ClassificationLabel(1);
		}
		else 
			return new ClassificationLabel(0);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "EvenOddClassifer";
	}

}
