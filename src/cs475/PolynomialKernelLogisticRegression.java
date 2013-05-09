package cs475;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

public class PolynomialKernelLogisticRegression extends
		KernerlLogisticRegression implements Serializable{
private double polynomial_kernel_exponent;
	
	public PolynomialKernelLogisticRegression(int iteration,double learning_rate,double exponent){
		super.iteration=iteration;
		super.learning_rate=learning_rate;
		super.trained=false;
		this.polynomial_kernel_exponent=exponent;
		System.out.println("exp:"+polynomial_kernel_exponent+",rate:"+learning_rate+",itr:"+iteration);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PolynomialKernelLogisticRegression";
	}

	@Override
	protected double kernerl(Instance i, Instance j) {
		// TODO Auto-generated method stub
		double result=0;
		Entry<Integer,Double> curr=null;
		int index=0;
		FeatureVector feature_i=i.getFeatureVector();
		FeatureVector feature_j=j.getFeatureVector();
		Iterator<Entry<Integer,Double>>itr_i=feature_i.itr();
		while(itr_i.hasNext()){
			curr=itr_i.next();
			index=curr.getKey();
			if(feature_j.get(index)!=null){
				result+=curr.getValue()*feature_j.get(index);
			}
		}
		result=Math.pow(result+1, polynomial_kernel_exponent);
//		for(int k=1;k<featureNum+1;k++){
//			if(feature_i.get(k)!=null && feature_j.get(k)!=null){
//				result+=feature_i.get(k)*feature_j.get(k);
//			}
//		}
//		result=Math.pow(result+1, polynomial_kernel_exponent);
		return result;
		
	}

}
