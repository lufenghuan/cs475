package cs475;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class LambdaMeanPredictor extends Predictor {
	double lambda;
	int K;
	int iterations;
	int numOfFeatures;
	ArrayList<double[]> clusterCentroids;

	public LambdaMeanPredictor(double lambda, int iterations){
		this.iterations=iterations;
		this.lambda=lambda;
	}

	@Override
	public void train(List<Instance> instances) {
		int indicator[]=new int [instances.size()];//indicate[i] indicate the cluster that instances[i] belongs to

		init(instances);
		for(int itr=0;itr<iterations;itr++){
			//E-step
			for(int i=0;i<instances.size();i++){
				indicator[i]=closestClust(instances.get(i), clusterCentroids);
			}
			//M-step
			List<ArrayList<Instance>> clusters=new ArrayList<ArrayList<Instance>>(clusterCentroids.size());
			for(int i=0;i<clusterCentroids.size();i++){
				clusters.add(new ArrayList<Instance>());
			}
			for(int i=0;i<indicator.length;i++){
				clusters.get(indicator[i]).add(instances.get(i));
			}

			for(int i=0;i<clusterCentroids.size();i++){
				clusterCentroids.set(i,calcMean(clusters.get(i)));
			}

		}
		System.out.println("number of cluster: "+clusterCentroids.size());
	}

	@Override
	public Label predict(Instance instance) {

		return new ClassificationLabel(predictCloestCluster(instance, clusterCentroids));
	}

	@Override
	public String toString() {
		return null;
	}

	protected void init(List<Instance> instances){
		this.K=1;
		this.clusterCentroids=new ArrayList<double[]>();
		this.numOfFeatures=Utilities.featureNum(instances);
		clusterCentroids.add(calcMean(instances));
		if(this.lambda==0){
			for (Instance ins:instances){
				lambda+=distance(ins, clusterCentroids.get(0));
			}
			lambda=lambda/instances.size();
			System.out.println("lambda"+lambda);
		}
	}

	/**
	 * calcuate the centriod of given list of instancs
	 * @param instances
	 * @return mean[], centriod of given instances
	 */
	protected double[]calcMean(List<Instance> instances){
		double length=instances.size();
		double []mean=new double[this.numOfFeatures];
		Arrays.fill(mean, 0);
		if(length!=0){
			FeatureVector fv;
			Iterator<Entry<Integer,Double>>itr;
			Entry<Integer,Double> entry;
			for(Instance i:instances){
				fv=i.getFeatureVector();
				itr=fv.itr();
				while(itr.hasNext()){
					entry=itr.next();
					mean[entry.getKey()-1]+=entry.getValue();
				}
			}
			for(int i=0;i<mean.length;i++){
				mean[i]/=length;
			}
		}
		return mean;
	}

	/**
	 * Euclidean distance of given instance to a centriod 
	 * @param ins
	 * @param centriod
	 * @return
	 */
	protected double distance(Instance ins, double[] centriod){
		Double temp;
		double temp2;
		double result=0;
		FeatureVector fv=ins.getFeatureVector();
		for(int i=0;i<centriod.length;i++){
			if((temp=fv.get(i+1))!=null){
				temp2=centriod[i]-temp;
				result+=temp2*temp2;
			}
			else 
				result+=(centriod[i]*centriod[i]);
		}
		return Math.sqrt(result);
	}
	/**
	 * calculate the nesest cluset for given instance
	 * @param instances
	 * @param clusterCentroids
	 * @return the index of the nearest clust
	 */
	protected int closestClust(Instance instance, ArrayList<double[]>clusterCentroids){
		double minDistance=Double.MAX_VALUE;
		double tempDistance=0;
		int closestClust=-1;
		for(int i=0;i<clusterCentroids.size();i++){
			tempDistance=distance(instance, clusterCentroids.get(i));
			if(tempDistance<minDistance){
				minDistance=tempDistance;
				closestClust=i;
			}
		}
//		System.out.println(minDistance)

		//bigger than lambda
		if(minDistance>this.lambda){
			double features[]=new double[this.numOfFeatures];
			Arrays.fill(features, 0);
			Iterator<Entry<Integer,Double>>itr;
			Entry<Integer,Double> entry;
			itr=instance.getFeatureVector().itr();
			while(itr.hasNext()){
				entry=itr.next();
				features[entry.getKey()-1]=entry.getValue();
			}
			closestClust=clusterCentroids.size();
			clusterCentroids.add(features);

		}
		return closestClust;
	}
	
	/**
	 * Same with CloestCluster but with out create new cluster in testing time
	 * @param instance
	 * @param clusterCentroids
	 * @return
	 */
	protected int predictCloestCluster(Instance instance, ArrayList<double[]>clusterCentroids){
		double minDistance=Double.MAX_VALUE;
		double tempDistance=0;
		int closestClust=-1;
		for(int i=0;i<clusterCentroids.size();i++){
			tempDistance=distance(instance, clusterCentroids.get(i));
			if(tempDistance<minDistance){
				minDistance=tempDistance;
				closestClust=i;
			}
		}
		return closestClust;
	}

}
