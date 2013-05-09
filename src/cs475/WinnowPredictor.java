package cs475;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;


public class WinnowPredictor extends LinearThresholdPredictor {

	
	public WinnowPredictor(int iterationNum,double step,double thickness,double beta){
		super.iterationNum=iterationNum;
		super.step=step;
		super.beta=beta;
		super.thickness=thickness;
	}
	@Override
	protected void initW() {
		// TODO Auto-generated method stub
		Arrays.fill(super.w, 1);
	}

	@Override
	protected void updateW(Instance instance, int label) {
		// TODO Auto-generated method stub
		Iterator<Entry<Integer, Double>> itr=instance.getFeatureVector().itr();
		int si;
		while(itr.hasNext()){
			Entry<Integer, Double> entry=itr.next();
			int key=entry.getKey()-1;
			if(key>w.length){
				break;
			}
			if(entry.getValue()>=0)
				si=1;
			else
				si=-1;
			w[key]=w[key]*Math.pow(step, si*label);
			//System.out.println(w[key]);
			if(w[key]>1000000)
				w[key]=1000000;
		}
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WinnowPredictor";
	}


}
