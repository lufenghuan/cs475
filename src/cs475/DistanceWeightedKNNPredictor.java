package cs475;

public class DistanceWeightedKNNPredictor extends KNNPredictor {

	public DistanceWeightedKNNPredictor(int k){
		super.k=k;
	}
	
	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		double result=0;
		double sumOfSim=0;
		double []sims=new double[this.k];
		super.calcKNearest(instance);
		for(int i=0;i<k;i++){
			sims[i]=1.0/(1+super.distances.get(i).dist);
			sumOfSim+=sims[i];
			
		}
		for(int i=0;i<k;i++){
			result+=(((RegressionLabel)instances.get(distances.get(i).index).getLabel()).getLabel())*(sims[i]/sumOfSim);
		}
		return new RegressionLabel(result);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DistanceWeightedKNNPredictor";
	}

}
