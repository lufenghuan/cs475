package cs475;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Hashtable;
import java.util.Set;
import java.lang.Integer;

public class MajorityClassifier extends Predictor implements Serializable {

	int toLabel=-1;
	
	public int getToLabel(){
		return this.toLabel;
	}
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		Hashtable<Integer,Count> table=new Hashtable<Integer, Count>(); //(index,counts)
		int max=0;
		ArrayList<Integer> majorityFeature=new ArrayList<Integer>();
		
		Iterator<Instance> instanceItr=instances.iterator();
		//int total=instances.size();
		while (instanceItr.hasNext()){
			ClassificationLabel label=(ClassificationLabel)instanceItr.next().getLabel();
			//count+=label.getLabel();
			Integer key=new Integer(label.getLabel());
			Count cou=table.get(key);
			
			if(cou!=null){
				cou.c++;
			}
			else{
				table.put(key, new Count(1));
			}
		}
		 Set<Map.Entry<Integer,Count>>  set=table.entrySet();
		 Iterator<Entry<Integer, Count>> itr=set.iterator();
		 
		 while (itr.hasNext()){
			 Entry<Integer,Count>curr=itr.next();
			 int currCount=curr.getValue().c;
			 if(currCount>max){
				 max=currCount;
				 majorityFeature.clear();
				 majorityFeature.add(curr.getKey());
			 }
			 else if(currCount==max){
				 majorityFeature.add(curr.getKey());
			 }
		 }
		if(majorityFeature.size()==0){
			toLabel=majorityFeature.get(0); 
		}
		else{
			Random rd=new Random();
			toLabel=majorityFeature.get(rd.nextInt(majorityFeature.size()));
		}

	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
//		ClassificationLabel classLabel=(ClassificationLabel)instance.getLabel();
//		if(classLabel.getLabel()!=-1){
//			return classLabel;
//		}
		
		return new ClassificationLabel(this.toLabel);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MajorityClassifier";
	}

}

class Count{
	int c;
	Count(int c){
		this.c=c;
	}
	Count(){
		this.c=0;
	}
}
