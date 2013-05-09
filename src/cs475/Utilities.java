package cs475;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Utilities {
	public static boolean isBinaryFeatures(List<Instance> instances){
		boolean isBinary=true;
		instances.get(0);
		Collection<Double>features=instances.get(0).getFeatureVector().features();
		for(Double d:features){
			double dValue=d.doubleValue();
			if((dValue!=0)&&(dValue!=1)){
				isBinary=false;
				break;
			}
		}
		return isBinary;
	}

	public static List<Label> getLabels(List<Instance> instances){
		ArrayList<Label> labels=new ArrayList<Label>();
		for(Instance i:instances){
			Label l=i.getLabel();
			if(!labels.contains(l)){
				labels.add(l);
			}
		}
		return labels;

	}
	public static int featureNum(List<Instance>instances){
		int featureNum=0;
		for(Instance i:instances){//get max feature number
			int currFeatureNum=i.getFeatureVector().getMaxIndex();
			if(currFeatureNum>featureNum){
				featureNum=currFeatureNum;
			}
		}
		return featureNum;
	}
	public static HashMap <Integer,Integer> lableCountHash(List<Instance> instances){
		HashMap<Integer,Integer> hashmap=new HashMap <Integer,Integer>();
		ArrayList<ClassificationLabel> labels=new ArrayList<ClassificationLabel>();
		for(Instance i:instances){
			labels.add((ClassificationLabel)i.getLabel());
		}
		Collections.sort(labels);
		int i=0;
		int j=1;
		//int count=0;
		ClassificationLabel curr=labels.get(i);
		//ClassificationLabel next;
		
		while(j<labels.size()){
			
			if(labels.get(j).equals(curr)){
				//count++;
				j++;
			}
			else{
				hashmap.put(curr.getLabel(), j-i);
				i=j;
				curr=labels.get(i);
				j++;
			}
		}
		hashmap.put(curr.getLabel(), j-i);
		
		

		return hashmap;
	}
	/**
	 * add ar2 to ar1
	 * @param ar1
	 * @param ar2
	 * @return
	 * @throws ArrayLeathNotEqualExeption
	 */
	public static double[] arrayAdd(double []ar1, double []ar2) throws ArrayLeathNotEqualExeption{
				if(ar1.length!=ar2.length){
					throw new ArrayLeathNotEqualExeption(ar1.length,ar2.length);
				}
				int length=ar1.length;
				
				for(int i=0;i<length;i++){
					ar1[i]=ar1[i]+ar2[i];
				}
		return ar1;
	}
	
	public static double dotProduct( double []ar1, double[]ar2)throws ArrayLeathNotEqualExeption{
		int len1=ar1.length;
		double result=0;
		if(len1!=ar2.length){
			throw new ArrayLeathNotEqualExeption(ar1.length,ar2.length);
		}
		for(int i=0;i<len1;i++){
			result+=ar1[i]*ar2[i];
		}
		return result;
	}
	
	/**
	 * Euclidean distance between two instance
	 * @param ins_i
	 * @param ins_j
	 * @return
	 */
	protected  static double euclideanDistance(Instance ins_i, Instance ins_j){
		// TODO Auto-generated method stub
		double distance=0;
//		int train_featureIndex;
//		double train_feature;
		double temp;
		
		Double f_i;
		Double f_j;
		int f_num_i;
		int f_num_j;
		int f_num;
		FeatureVector fv_i;
		FeatureVector fv_j;
		
		fv_i=ins_i.getFeatureVector();
		fv_j=ins_j.getFeatureVector();
		
		f_num_i=fv_i.getMaxIndex();
		f_num_j=fv_j.getMaxIndex();
		
		if(f_num_i>f_num_j)
			f_num=f_num_i;
		else f_num=f_num_j;
		
		for(int i=0;i<f_num;i++){
			f_i=fv_i.get(i+1);
			f_j=fv_j.get(i+1);
			if(f_i==null)
				f_i=new Double(0);
			if(f_j==null)
				f_j=new Double(0);
			temp=f_i-f_j;
			distance+=temp*temp;
			
		}
		return Math.sqrt(distance);
		
//		Double test_feature;
//		Entry<Integer,Double> tempEntry;
//		FeatureVector testFeatureVetor;
//
//		testFeatureVetor=ins_j.getFeatureVector();
//		Iterator<Entry<Integer, Double>> train_itr=ins_i.getFeatureVector().itr();
//		while(train_itr.hasNext()){
//			tempEntry=train_itr.next();
//			train_featureIndex=tempEntry.getKey();
//			train_feature=tempEntry.getValue();
//			if((test_feature=testFeatureVetor.get(train_featureIndex))!=null){
//				temp=(train_feature-test_feature);
//				distance+=temp*temp;
//			}
//		}
//		return Math.sqrt(distance);
	}
}
