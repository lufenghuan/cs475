package cs475;


public class SimpleKNNPredictor extends KNNPredictor {

	public SimpleKNNPredictor(int k){
		super.k=k;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SimpleKNNPredictor";
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		double result=0;
		super.calcKNearest(instance);
		for(int i=0;i<k;i++){
			result+=((RegressionLabel)instances.get(distances.get(i).index).getLabel()).getLabel();
		}
		return new RegressionLabel(1.0/k*result);
	}

}
