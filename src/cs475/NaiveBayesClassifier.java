package cs475;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class NaiveBayesClassifier extends Predictor implements Serializable {
	double[] means;
	//ArrayList<ProbY> probYList=new ArrayList<ProbY>();
	int featureNum=0;
	boolean isBinaryFeature;
	HashSet<Integer> featureIds;//record the features appears in traning set
	
	double[]y0Zeros;
	double[]y1Zeros;
	double[]y0Ones;
	double[]y1Ones;
	
	double y0;
	double y1;
	
	double probY[];
	double lambda;

	public NaiveBayesClassifier(double lambda){
		featureNum=0;
		isBinaryFeature=true;
		means=null;
		this.lambda=lambda;

	}
	@Override
	public void train(List<Instance> instances) {
		
		for(Instance i:instances){//get max feature number
			int currFeatureNum=i.getFeatureVector().getMaxIndex();
			if(currFeatureNum>featureNum){
				featureNum=currFeatureNum;
			}
		}
		featureIds=new HashSet<Integer>((int) (featureNum/0.75));
		isBinaryFeature =Utilities.isBinaryFeatures(instances);
		y0Zeros=new double[featureNum];
		y1Zeros=new double[featureNum];
		y0Ones=new double[featureNum];
		y1Ones=new double[featureNum];
		
		if(!isBinaryFeature){//if is not binary features, calc means
			means=calcMeans(instances,featureNum); 
		}
		else{
			means=new double[instances.size()];
			Arrays.fill(means, 0);//if is binary,fill zeros, 
		}
		
		for(Instance i:instances){
			int labelId=((ClassificationLabel)i.getLabel()).getLabel();
			if(labelId==0) y0+=1;
			else y1+=1;
			
			
			Iterator<Entry<Integer, Double>> itr=i.getFeatureVector().itr();
			
			while(itr.hasNext()){
				Entry<Integer, Double> entry=itr.next();
				int featureIndex=entry.getKey();
				
				if(!featureIds.contains(featureIndex)){//record the features appears in traning set
					featureIds.add(featureIndex);
				}
				
				if(entry.getValue()<=means[featureIndex-1]){
					if(labelId==0)y0Zeros[featureIndex-1]+=1;
					else y1Zeros[featureIndex-1]+=1;
				}
				else{
					if(labelId==0) y0Ones[featureIndex-1]+=1;
					else y1Ones[featureIndex-1]+=1;
				}
			}
		}
		
		for(int i=0;i<featureNum;i++){
			y0Zeros[i]=(y0Zeros[i]+this.lambda)/(y0+2.0*this.lambda);
			y1Zeros[i]=(y1Zeros[i]+this.lambda)/(y1+2.0*this.lambda);
			y0Ones[i]=(y0Ones[i]+this.lambda)/(y0+2.0*this.lambda);
			y1Ones[i]=(y1Ones[i]+this.lambda)/(y1+2.0*this.lambda);
		}
		
		int total=instances.size();
		y0=(y0+this.lambda)/(total+2.0*this.lambda);
		y1=(y1+this.lambda)/((total+2.0*this.lambda));
	
		
	}







	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		double probY0=0;
		double probY1=0;
		Iterator<Entry<Integer, Double>> itr=instance.getFeatureVector().itr();
		while(itr.hasNext()){
			Entry<Integer, Double> entry=itr.next();
			int featureIndex=entry.getKey();
			if(this.featureIds.contains(featureIndex)){//the feature id appears in training set
				if(entry.getValue()<=means[featureIndex-1]){//<=mean, appears in zeros
					probY0+=Math.log(y0Zeros[featureIndex-1]);
					probY1+=Math.log(y1Zeros[featureIndex-1]);
				}
				else{//>mean, appears in ones
					probY0+=Math.log(y0Ones[featureIndex-1]);
					probY1+=Math.log(y1Ones[featureIndex-1]);
				}
			}
		}
		probY0+=Math.log(y0);
		probY1+=Math.log(y1);
		if (probY1>=probY0)
			return new ClassificationLabel(1);
		else return new ClassificationLabel(0);
	}

	@Override
	public String toString() {
		return "NaiveBayesClassifier";
	}

	private double[] calcMeans(List<Instance> instances,int featureNum){

		double[] means=new double[featureNum];
		Arrays.fill(means, 0.0);

		for(Instance i: instances){
			Iterator<Entry<Integer, Double>> itr=i.getFeatureVector().itr();
			while(itr.hasNext()){
				Entry<Integer, Double> entry=itr.next();
				means[entry.getKey()-1]=entry.getValue();
			}
		}

		for(int i=0;i<means.length;i++){
			means[i]=means[i]/(instances.size()+0.0);
		}

		return means;
	}

}

