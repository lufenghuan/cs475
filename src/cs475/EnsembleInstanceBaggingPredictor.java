package cs475;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnsembleInstanceBaggingPredictor extends EnsemblePredictor {


	public EnsembleInstanceBaggingPredictor(int K, int iteration, double learningRate){
//		super.K=K;
//		super.iteration=iteration;
//		super.learningRate=learningRate;
//		weights=new double[K];
//		Arrays.fill(weights, 0);
//		Ws=new ArrayList<double[]>(K);
		super.init(K,iteration,learningRate);
	}
	@Override
	protected void trainPerceptron(List<Instance> instances) {
		// TODO Auto-generated method stub
		ArrayList<PerceptronPredictor> perceptrons=new ArrayList<PerceptronPredictor>(K);
		List<Instance> filtedInstances=new ArrayList<Instance>(instances.size());
		PerceptronPredictor curr=null;
		
		for(int i=0;i<K;i++){
			curr=new PerceptronPredictor(5, 1, 0);
			//filter
			for(int j=0;j<instances.size();j++){
				if(j%K!=i){
					filtedInstances.add(instances.get(j));
				}
			}
			curr.train(filtedInstances);
			filtedInstances.clear();
			Ws.add(curr.getW());
		}
		

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "EnsembleInstanceBaggingPredictor";
	}

}
