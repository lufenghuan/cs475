package cs475;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class LinearThresholdPredictor extends Predictor {

	protected int iterationNum;
	protected double step;
	protected double beta;
	protected double thickness;
	protected double []w;
	protected int featureNum;


	@Override
	public void train(List<Instance> instances) {
		//System.out.println("step: "+step);
		featureNum=Utilities.featureNum(instances);
		w=new double[featureNum];
		int size=instances.size();
		initW();
		for(int k=0;k<iterationNum;k++){
			for(int count=0;count<size;count++){
				Instance i=instances.get(count);
				int label=((ClassificationLabel)i.getLabel()).getLabel();
				if(label==0)
					label-=1;
				//double[]featureArray=getFeatureInVector(i);
				if (label!=calcYHat(i,w)){//y!=yHat
					updateW(i,label);
				}
			}
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		int toLabel=-1;
		int yhat=calcYHat(instance,w);
		if(yhat==1)
			toLabel=1;
		else if(yhat==-1)
			toLabel=0;
		return new ClassificationLabel(toLabel);
	}

	
	protected double[] getFeatureInVector(Instance i){
		double[]features=new double[featureNum];
		Arrays.fill(features, 0);
		Iterator<Entry<Integer, Double>> itr=i.getFeatureVector().itr();
		while(itr.hasNext()){
			Entry<Integer, Double> entry=itr.next();
			int key=entry.getKey()-1;
			if(key>w.length){
				break;
			}
			features[entry.getKey()-1]=entry.getValue();
		}
		
		return features;
	}
//	protected int yHat(double[]features,double[]w){
//		double sum=0;
//		int yhat=0;
//		for(int i=0;i<featureNum;i++){
//			sum+=features[i]*w[i];
//		}
//		if(sum>=beta+thickness)
//			yhat=1;
//		else if(sum<beta+thickness)
//			yhat=-1;
//		return yhat;
//	}
	
	protected int calcYHat(Instance i,double[]w){
		double sum=0;
		int yhat=0;
		Iterator<Entry<Integer, Double>> itr=i.getFeatureVector().itr();
		while(itr.hasNext()){
			Entry<Integer, Double> entry=itr.next();
			int key=entry.getKey()-1;
			if(key<w.length){
				sum+=w[key]*entry.getValue();
			}
			
		}
		if(sum>=beta+thickness)
			yhat=1;
		else if(sum<beta-thickness)
			yhat=-1;
		return yhat;
		
	}
	
	public double[] getW(){
		return w;
	}
	
	abstract protected void initW();
	abstract protected void updateW(Instance instance,int label);
	

}
