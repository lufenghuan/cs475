package cs475;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class PerceptronPredictor extends LinearThresholdPredictor {


	public PerceptronPredictor(int iterationNum,double step,double thickness){
		super.iterationNum=iterationNum;
		super.step=step;
		super.beta=0;
		super.thickness=thickness;
	}
	@Override
	protected void initW() {
		// TODO Auto-generated method stub
		Arrays.fill(super.w, 0);
	}

	@Override
	protected void updateW(Instance instance,int label) {
		// TODO Auto-generated method stub
//		int size=w.length;
//		//double x[]=new double[size];
//		for(int i=0;i<size;i++){
//			xi[i]=xi[i]*label*this.step;
//		}
//		//double toUpdate=xi*this.step*label;
//		//Arrays.fill(x, toUpdate);
//		
//		try {
//			Utilities.arrayAdd(w, xi);
//		} catch (ArrayLeathNotEqualExeption e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Iterator<Entry<Integer, Double>> itr=instance.getFeatureVector().itr();
		while(itr.hasNext()){
			Entry<Integer, Double> entry=itr.next();
			int key=entry.getKey()-1;
			if(key<w.length){
				w[key]=w[key]+entry.getValue()*this.step*label;
			}
			
		}
		

	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PerceptronPredictor";
	}

}
