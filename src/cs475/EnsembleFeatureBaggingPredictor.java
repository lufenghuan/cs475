package cs475;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class EnsembleFeatureBaggingPredictor extends EnsemblePredictor {

	public EnsembleFeatureBaggingPredictor(int K, int iteration, double learningRate){
		super.init(K, iteration, learningRate);
	}
	@Override
	public String toString() {
		return "EnsembleFeatureBaggingPredictor";
	}

	@Override
	protected void trainPerceptron(List<Instance> instances) {
		
		List<Instance> filtedInstances=new ArrayList<Instance>(instances.size());
		PerceptronPredictor curr=null;
		Instance filtedIns=null;
		FeatureVector filtedFV=null;
		Iterator<Entry<Integer,Double>> itr;
		Entry<Integer,Double> currEntry;
		for(int i=0;i<K;i++){
			curr=new PerceptronPredictor(5, 1, 0);
			//filter
			for(Instance ins:instances){
				filtedFV=new FeatureVector();
				itr=ins.getFeatureVector().itr();
				while(itr.hasNext()){
					currEntry=itr.next();
					if(currEntry.getKey()%K==i){
						filtedFV.add(currEntry.getKey(), currEntry.getValue());
					}
				}
				ClassificationLabel label=(ClassificationLabel)ins.getLabel();
				filtedIns=new Instance(filtedFV, new ClassificationLabel( label.getLabel()));
				filtedInstances.add(filtedIns);
			}
			curr.train(filtedInstances);
			filtedInstances.clear();
			Ws.add(curr.getW());
		}
	}

}
