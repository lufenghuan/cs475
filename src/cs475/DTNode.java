package cs475;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for Decision Tree Node
 * @author Fenghuan Lu
 *
 */
public class DTNode implements Serializable {
	public DTNode left;
	public DTNode right;
	private int splitFeatureNum;
	private int label;
	private double split;  //0 for binary
	public DTNode(){
		this.left=null;
		this.right=null;
		this.splitFeatureNum=-1;
		this.label=-1;
		this.split=0;
	}
	public DTNode(DTNode left, DTNode right){
		this.left=left;
		this.right=right;
		this.splitFeatureNum=-1;
		this.label=-1;
		this.split=0;
	}
	
	public DTNode(int label){
		this.left=null;
		this.right=null;
		this.splitFeatureNum=-1;
		this.label=label;
		this.split=0;
	}
	
	public DTNode(int splitFeatureNum,double split){
		this.left=null;
		this.right=null;
		this.splitFeatureNum=splitFeatureNum;
		this.label=-1;
		this.split=split;
		
	}

	
	public int getSplitFeartureNum(){
		return this.splitFeatureNum;
	}
	
	public int getLabel(){
		return this.label;
	}
	public double getSplit(){
		return this.split;
	}
	
	
	
	public void setsplitFeatureNum(int splitFeatureNum){
		this.splitFeatureNum=splitFeatureNum;
	}
	
	public void setLable(int label){
		this.label=label;
	}
	public void setSplit(double split){
		this.split=split;
	}


}
