package cs475;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class LambdaMeanPredictor2 extends Predictor {
	double lambda;
	int K;
	int iterations;
	int numOfFeatures;
	ArrayList<FeatureVector> clusterCentroids;

	public LambdaMeanPredictor2(double lambda, int iterations){
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
		// TODO Auto-generated method stub

		return new ClassificationLabel(closestClust(instance, clusterCentroids));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void init(List<Instance> instances){
		this.K=1;
		this.clusterCentroids=new ArrayList<FeatureVector>();
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
	protected FeatureVector calcMean(List<Instance> instances){
		double length=instances.size();
		FeatureVector centriod=new FeatureVector();

		FeatureVector fv;
		Iterator<Entry<Integer,Double>>itr;
		Entry<Integer,Double> entry;
		for(Instance i:instances){
			fv=i.getFeatureVector();
			itr=fv.itr();
			while(itr.hasNext()){
				entry=itr.next();
				if(centriod.get(entry.getKey())==null)
					centriod.add(entry.getKey(), entry.getValue());
				else {
					double temp=centriod.get(entry.getKey());
					centriod.add(entry.getKey(), entry.getValue()+temp);
				}
			}
		}
		itr=centriod.itr();
		while(itr.hasNext()){
			entry=itr.next();
			entry.setValue(entry.getValue()/length);
		}
		

		return centriod;
	}

	/**
	 * Euclidean distance of given instance to a centriod 
	 * @param ins
	 * @param centriod
	 * @return
	 */
	protected double distance(Instance ins, FeatureVector centriod){
		Instance ins_temp=new Instance(centriod, null);
		return Utilities.euclideanDistance(ins, ins_temp);
		
	}
	/**
	 * calculate the nesest cluset for given instance
	 * @param instances
	 * @param clusterCentroids
	 * @return the index of the nearest clust
	 */
	protected int closestClust(Instance instance, ArrayList<FeatureVector>clusterCentroids){
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

		//bigger than lambda
		if(minDistance>this.lambda){
			FeatureVector fvCopy=new FeatureVector();
			Iterator<Entry<Integer,Double>>itr;
			Entry<Integer,Double> entry;
			itr=instance.getFeatureVector().itr();
			while(itr.hasNext()){
				entry=itr.next();
				fvCopy.add(entry.getKey(), entry.getValue());
			}
			closestClust=clusterCentroids.size();
			clusterCentroids.add(fvCopy);

		}
		return closestClust;
	}

}
