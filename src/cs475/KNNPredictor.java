package cs475;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class KNNPredictor extends Predictor {

	protected int k;
	protected List<Instance> instances;
	protected List<InstanceWithDist> distances;
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		this.instances=instances;
		this.distances=new ArrayList<InstanceWithDist>(this.instances.size());
	}

	@Override
	abstract public Label predict(Instance instance);	
	
	/**
	 * caculate the k nearest neighbors for intsnace
	 * @param instance
	 * @return
	 */
	protected void calcKNearest(Instance instance){
		int index=0;
		distances.clear();
		for(Instance i:this.instances){
			distances.add(index, new InstanceWithDist(index,distance(i, instance)));
			index++;
		}
		Collections.sort(distances);
	}
	
	
	/**
	 * Euclidean distance
	 * @param train
	 * @param test
	 * @return
	 */
	protected  double distance(Instance train, Instance test){
		// TODO Auto-generated method stub
		double distance=0;
		int train_featureIndex;
		double train_feature;
		double temp;
		Double test_feature;
		Entry<Integer,Double> tempEntry;
		FeatureVector testFeatureVetor;

		testFeatureVetor=test.getFeatureVector();
		Iterator<Entry<Integer, Double>> train_itr=train.getFeatureVector().itr();
		while(train_itr.hasNext()){
			tempEntry=train_itr.next();
			train_featureIndex=tempEntry.getKey();
			train_feature=tempEntry.getValue();
			if((test_feature=testFeatureVetor.get(train_featureIndex))!=null){
				temp=(train_feature-test_feature);
				distance+=temp*temp;
			}
		}
		return Math.sqrt(distance);
	}
}

class InstanceWithDist implements Comparable<InstanceWithDist>, Serializable{
	int index;
	double dist;
	public InstanceWithDist(int index,double dist){
		this.index=index;
		this.dist=dist;
	}
	@Override
	public int compareTo(InstanceWithDist o) {
		// TODO Auto-generated method stub
		if(this.dist>o.dist)
			return 1;
		else if(this.dist==o.dist)
			return 0;
		else return -1;
	}
}
