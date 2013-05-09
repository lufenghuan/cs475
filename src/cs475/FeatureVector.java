package cs475;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


public class FeatureVector implements Serializable {
	
	protected HashMap<Integer,Double> hashmap=new HashMap<Integer,Double> (400);
	protected int maxIndex=-1;
	

	public void add(int index, double value) {
		// TODO Auto-generated method stub
		if(index>maxIndex)
			maxIndex=index;
		hashmap.put(index, value);
		
	}
	
	public Double get(int index) {
		// TODO Auto-generated method stub
		return hashmap.get(index);
	}
	
	public Iterator<Entry<Integer, Double>> itr(){
		
		return hashmap.entrySet().iterator();
	}
	
	public Collection<Double> features(){
		return hashmap.values();
	}
	
	public int getMaxIndex(){
		return maxIndex;
	}
	public Set<Entry<Integer, Double>> getEntrySet(){
		return hashmap.entrySet();
	}
	
	

}
