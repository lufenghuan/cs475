package cs475;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DecisionTree implements Serializable {

	private int majorityLabel;
	private int maxFeatureIndex;
	private boolean isBinary;//if the feature is binary
	private int maxLevel;
	public DecisionTree(int majorityLabel,int maxFeatureIndex,boolean isBinary,int maxLevel){
		this.majorityLabel=majorityLabel;
		this.maxFeatureIndex=maxFeatureIndex;
		this.isBinary=isBinary;
		this.maxLevel=maxLevel;
	}
	/**
	 * Recursively build decision tree
	 * @param curr current node
	 * @param instances training instances
	 * @param labels label set
	 * @return
	 */
	public DTNode buildDecisionTree(List<Instance> instances,List<ClassificationLabel> labels,
			List<Integer> visitedFeatures,int level){

		if(labels.size()==1){
			//all label agree
			return new DTNode(labels.get(0).getLabel());//leaf node
		}
		if (instances.size()==0){
			return new DTNode(majorityLabel);
		}
		if(level==maxLevel||visitedFeatures.size()==this.maxFeatureIndex){//max level ||all features used
			MajorityClassifier mc=new MajorityClassifier();
			mc.train(instances);
			System.out.println(mc.getToLabel()+"\n");
			return new DTNode(mc.getToLabel());
		}

		List<Instance> leftIns=new ArrayList<Instance>();
		List<Instance> rightIns=new ArrayList<Instance>();
		List<ClassificationLabel> leftLabels=new ArrayList<ClassificationLabel>();
		List<ClassificationLabel> rightLabels=new ArrayList<ClassificationLabel>();
		
		
		
		List<Integer> vFeatures=new ArrayList<Integer>();
		for(Integer i:visitedFeatures){//get a deep copy
			vFeatures.add(i);
		}
		int nLevel=level+1;
		
		DTNode root=creatNode(visitedFeatures,this.isBinary,instances,labels,
				leftIns,leftLabels,rightIns,rightLabels);
		//System.out.println("size of left:"+leftIns.size()+". Size of right�� "+rightIns.size());
		
		root.left=buildDecisionTree(leftIns,leftLabels,visitedFeatures,nLevel);
		root.right=buildDecisionTree(rightIns,rightLabels,visitedFeatures,nLevel);
		
		return root;
		
	}

	
	private DTNode creatNode(List<Integer> visitedFeature,boolean isBinary,
			List<Instance> instances, List<ClassificationLabel>labels,
			List<Instance> leftIn, List<ClassificationLabel>leftLabels,
			List<Instance> rightIn, List<ClassificationLabel>rightLabels){
		int splitFeature=-1;
		double split=0;
		double p=999999999.0;
		int sum=instances.size();
		
		for(int i=1;i<=this.maxFeatureIndex;i++){//traverse each feature
			int size=labels.size();
			int []lCounts=new int [size];
			int []rCounts=new int [size];
			int leftIns=0;
			int rightIns=0;
			double splitP=0;
			
			if(!visitedFeature.contains(i)){
				if(!isBinary){
					//System.out.println("Is not binary");
					splitP=splitPoint(instances,i);
					//System.out.println("split:"+splitP);
				}
				
				for(Instance j:instances){//traverse each instances
					Double d=j.getFeatureVector().get(i);
					double featureTmp=0;
					if(d==null)
						featureTmp=0;//treat as 0 if not exit in the feature vector
					else featureTmp=d;
					
					if(featureTmp<=splitP){//left
						leftIns++;
						for(int k=0;k<labels.size();k++){
							if(((ClassificationLabel)j.getLabel()).compareTo(labels.get(k))==0){
								++lCounts[k];
							}
						}
					}
					else{//right
						rightIns++;
						for(int k=0;k<labels.size();k++){
							if(((ClassificationLabel)j.getLabel()).compareTo(labels.get(k))==0){
								++rCounts[k];
							}
						}
					}
				}//end of traverse each instances by given feature i
				//System.out.println("leftIns:"+leftIns+". rightIns:"+rightIns);
				double leftPart=0;
				double rightPart=0;
				for(int k=0;k<size;k++){
					double probL=lCounts[k]/(leftIns+0.0);
					if(probL!=0)
						leftPart+=-1*probL*Math.log(probL);
					
					double probR=rCounts[k]/(rightIns+0.0);
					if(probR!=0)
						rightPart+=-1*probR*Math.log(probR);
				}
				//System.out.println("leftPart:"+leftPart+". rightPart:"+rightPart);
				double p2=(leftIns+0.0)/sum*leftPart+(rightIns+0.0)/sum*rightPart;
				//System.out.println("p2:"+p2+". p:"+p);
				if(p2<p){
					p=p2;
					splitFeature=i;
					split=splitP;
					//System.out.println("split:"+split+". SplitFeature: "+splitFeature);
				}
			}
		}
		
		//get sub set
		for(Instance j:instances){
			Double d=j.getFeatureVector().get(splitFeature);
			double featureTmp=0;
			if(d==null)
				featureTmp=0;//treat as 0 if not exit in the feature vector
			else featureTmp=d;
			
			ClassificationLabel currLabel=(ClassificationLabel)j.getLabel();
			if(featureTmp<=split){//left
				leftIn.add(j);
				
				if(!leftLabels.contains(currLabel)){//if leftLabels does not contain the label
					leftLabels.add(currLabel);
				}
			}
			else{//right
				rightIn.add(j);
				if(!rightLabels.contains(currLabel)){
					rightLabels.add(currLabel);
				}
			}
		}
		//System.out.println(splitFeature);
		System.out.println(splitFeature);
		System.out.println(split);
		return new DTNode(splitFeature,split);
	}
	/**
	 * calc split point for feature-th feature in given continuous feature values in instances
	 * @param instances
	 * @param feature
	 * @return
	 */
	private double splitPoint(List<Instance> instances,int feature){
		double sum=0;
		for (Instance i:instances){
			Double d=i.getFeatureVector().get(feature);
			if(d!=null){
				sum+=d;
			}
		}
		return sum/maxFeatureIndex;
	}
}
	
	
	