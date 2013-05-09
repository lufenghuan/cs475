package cs475;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTClassifier extends Predictor implements Serializable{

	private DTNode root;
	private int maxLevel;

	public DTClassifier(int maxLevel){
		this.maxLevel=maxLevel;
	}
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		int majorityLabel=majority(instances);
		int maxFeatureIndex=0;
		List<ClassificationLabel> labels=new ArrayList<ClassificationLabel>();

		for(Instance i:instances){
			int curr=i.getFeatureVector().getMaxIndex();
			ClassificationLabel label=(ClassificationLabel) i.getLabel();
			if(!labels.contains(label))
				labels.add(label);
			if(curr>maxFeatureIndex)
				maxFeatureIndex=curr;
		}
		//System.out.println("size:"+labels.size());

		boolean isBinary;
		if(labels.size()==2)
			isBinary=true;
		else 
			isBinary=false;

		DecisionTree dt=new DecisionTree(majorityLabel,maxFeatureIndex,isBinary,maxLevel);
		ArrayList<Integer>visitedFeatures=new ArrayList<Integer>();
		root=dt.buildDecisionTree(instances, labels, visitedFeatures, 0);
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		DTNode curr=root;
		int level=0;
		while(curr.left!=null||curr.right!=null){//while not reach leaf
			level++;
			int f=curr.getSplitFeartureNum();
			double s=curr.getSplit();
			Double d=instance.getFeatureVector().get(f);
			double featureTmp=0;
			if(d==null)
				featureTmp=0;//treat as 0 if not exit in the feature vector
			else featureTmp=d;
			if(featureTmp<=s){
				curr=curr.left;
			}
			else
				curr=curr.right;
		}
		//System.out.println("level is "+level+"\n");
		
		return new ClassificationLabel(curr.getLabel());
	}

	@Override
	public String toString() {
		return "DTClassifier";
	}

	private int majority(List<Instance> instances){
		MajorityClassifier mc=new MajorityClassifier();
		mc.train(instances);

		return mc.getToLabel();
	}

}
