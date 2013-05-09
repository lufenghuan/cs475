package cs475;

import java.io.Serializable;


public class LinearKernelLogisticRegression extends KernerlLogisticRegression implements Serializable {

	public LinearKernelLogisticRegression(int iteration,double learning_rate){
		super.iteration=iteration;
		super.learning_rate=learning_rate;
		super.trained=false;
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "LinearKernelLogisticRegression";
	}

	@Override
	protected double kernerl(Instance i, Instance j) {
		// TODO Auto-generated method stub
		double result=0;
		FeatureVector feature_i=i.getFeatureVector();
		FeatureVector feature_j=j.getFeatureVector();
		for(int k=1;k<featureNum+1;k++){
			if(feature_i.get(k)!=null && feature_j.get(k)!=null){
				result+=feature_i.get(k)*feature_j.get(k);
			}
		}
		return result;
	}

}
