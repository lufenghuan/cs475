package cs475;

import java.io.Serializable;

public class GaussianKernelLogisticRegression extends KernerlLogisticRegression implements Serializable {
	private double gaussian_kernel_sigma;
	
	public GaussianKernelLogisticRegression(int iteration,double learning_rate,double sigma){
		super.iteration=iteration;
		super.learning_rate=learning_rate;
		super.trained=false;
		this.gaussian_kernel_sigma=sigma;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "GaussianKernelLogisticRegression";
	}

	@Override
	protected double kernerl(Instance i, Instance j) {
		// TODO Auto-generated method stub
		double result=0;
		double f1,f2;
		FeatureVector feature_i=i.getFeatureVector();
		FeatureVector feature_j=j.getFeatureVector();
		for(int k=1;k<featureNum+1;k++){
			if(feature_i.get(k)!=null){
				f1=feature_i.get(k);
			}
			else 
				f1=0;
			if(feature_j.get(k)!=null){
				f2=feature_j.get(k);
			}
			else 
				f2=0;
			if(f1!=f2){
				result+=(f1-f2)*(f1-f2);
			}
		}
		result=Math.exp(-1*result/(2*gaussian_kernel_sigma*gaussian_kernel_sigma));
		return result;
	}

}
