package cs475;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public abstract class KernerlLogisticRegression extends Predictor implements Serializable{
	protected int featureNum;
	protected int instanceNum;
	protected int iteration;
	protected double learning_rate;
	protected double alpha[];
	protected double gramMatrix [][];
	protected List<Instance> instances;
	protected boolean trained;
	protected Timer timer;

	@Override
	public void train(List<Instance> instances) {
		
		
		
		double g=0;
		double z=0;
		double alpha_j;
		double matrixTemp;
		double tempAlpha[];
		double gradient;
		ClassificationLabel label;
		/*initialization*/
		int curr=0;
		instanceNum=instances.size();
		this.instances=instances;
		for(Instance i:instances){
			curr=i.getFeatureVector().getMaxIndex();
			if(curr>featureNum)
				featureNum=curr;
		}
		this.alpha=new double [instanceNum];
		Arrays.fill(this.alpha, 0);
		gramMatrix=new double[instanceNum][instanceNum];
		for(int i=0;i<instanceNum;i++){
			for(int j=0;j<instanceNum;j++){
				gramMatrix[i][j]=0;
			}
		}
		gradient=0;
		tempAlpha=new double[instanceNum];
		this.trained=true;
		
		calcGramMatrix(instances);
		System.out.println("finish calcGramM");

		//train
		for(int itr=0;itr<iteration;itr++){
			for(int k=0;k<instanceNum;k++){
				gradient=0;
				for(int i=0;i<instanceNum;i++){
					z=0;
					label=(ClassificationLabel)instances.get(i).getLabel();
					for(int j=0;j<instanceNum;j++ ){
							z+=alpha[j]*gramMatrix[j][i];
						
					}
					matrixTemp=gramMatrix[i][k];
						gradient += label.getLabel()*link(-z)*gramMatrix[i][k] + (1 - label.getLabel())*link(z)*(-gramMatrix[i][k]);
				}//end of i
				
				if(gradient!=0){
					tempAlpha[k]=alpha[k]+learning_rate*gradient;
				}
				
			}//end of k
			for (int count=0;count<instanceNum;count++){
				alpha[count]=tempAlpha[count];
//				System.out.println("["+count+"]:"+ alpha[count]);
			}
			Arrays.fill(tempAlpha, 0);
		}//end of iteration

		
	}


	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		if(trained==false){
			System.err.println("Not trained!");
		}
		double g=0;
		double z=0;
		for(int j=0;j<instanceNum;j++){
			z=z+alpha[j]*kernerl(instances.get(j), instance);
		}
//		g=1.0/(1+Math.exp(-1*z));
		g=link(z);
		if(g>=0.5)
			return new ClassificationLabel(1);
		else
			return new ClassificationLabel(0);
					
	}

	@Override
	abstract public  String toString();

	protected void calcGramMatrix(List<Instance> instances){
		for(int i=0;i<instanceNum;i++){
			for(int j=i;j<instanceNum;j++){
				gramMatrix[i][j]=kernerl(instances.get(i),instances.get(j));
				gramMatrix[j][i]=gramMatrix[i][j];
			}
		}
	}

	private double link(double z) {
		double ez = Math.pow(Math.E, -z);
		return 1/(1 + ez);
	}
	abstract protected double kernerl(Instance i,Instance j);

}
