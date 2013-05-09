package cs475;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class EnsemblePredictor extends Predictor {
	
	protected int K;
	protected double learningRate;
	protected int iteration;
	protected double weights[];
	protected ArrayList<double []> Ws;

	protected void init(int K, int iteration, double learningRate){
		this.K=K;
		this.iteration=iteration;
		this.learningRate=learningRate;
		weights=new double[K];
		Arrays.fill(weights, 0);
		Ws=new ArrayList<double[]>(K);
	}
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		double tempW[];
		double z=0; //wk*x, g(z)
		double h=0;//h(z)
		int yhat=0;
		int yi=0;
		int updateSign=0;//update sign
		double gWkXi[]=new double[K];
		Iterator<Entry<Integer,Double>> itr;
		Entry<Integer,Double>curr;
		trainPerceptron(instances);
		for(int i=0;i<iteration;i++){
			for(Instance example:instances){
				yi=((ClassificationLabel)example.getLabel()).getLabel();
				
				h=0;
				for(int j=0;j<this.K;j++){
					tempW=Ws.get(j);
					z=0;
					itr=example.getFeatureVector().itr();
					while(itr.hasNext()){
						curr=itr.next();
						if(curr.getKey()<=tempW.length&&tempW[curr.getKey()-1]!=0)
							z+=curr.getValue()*tempW[curr.getKey()-1];
					}
					gWkXi[j]=g(z);
					h+=weights[j]*gWkXi[j];
				}
				//predict
				if(h>=0) 
					yhat=1;
				else 
					yhat=0;
				//update
				if(yi!=yhat){
					if(yi==1){
						updateSign=1;
					}
					else if(yi==0){
						updateSign=-1;
					}
					for(int j=0;j<K;j++){
						weights[j]+=updateSign*this.learningRate*gWkXi[j];
					}
				}//end of update
			}//end of traverse all examples
		}//end of all iterations
		
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		double tempW[];
		double z=0; //wk*x, g(z)
		double h=0;//h(z)
		int yhat=0;
		double gWkXi[]=new double[K];
		Iterator<Entry<Integer,Double>> itr;
		Entry<Integer,Double>curr;
		
		
		for(int j=0;j<this.K;j++){
			tempW=Ws.get(j);
			z=0;
			itr=instance.getFeatureVector().itr();
			while(itr.hasNext()){
				curr=itr.next();
				if(curr.getKey()<=tempW.length&&tempW[curr.getKey()-1]!=0)
					z+=curr.getValue()*tempW[curr.getKey()-1];
			}
			gWkXi[j]=g(z);
			h+=weights[j]*gWkXi[j];
		}
		//predict
		if(h>=0) 
			yhat=1;
		else 
			yhat=0;
		return new ClassificationLabel(yhat);
	}
	
	protected double g(double z){
		return z/(Math.sqrt(1+z*z));
	}

	/**
	 * Train k sub-perceptrons
	 */
	abstract protected void trainPerceptron(List<Instance> instances);

}
