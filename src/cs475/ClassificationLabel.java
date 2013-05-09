package cs475;

import java.io.Serializable;

/**
 * ClassificationLabel, which contains an int to
 * indicate a class (binary prediction will be 0 or 1)
 *
 */
public class ClassificationLabel extends Label implements Serializable, Comparable<ClassificationLabel> {
	
	private int label;

	public ClassificationLabel(int label) {
		// TODO Auto-generated constructor stub
		this.label=label;
	}
	
	public int getLabel(){
		return label;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (""+label);
	}

	@Override
	public int compareTo(ClassificationLabel cmp) 
	{
		// TODO Auto-generated method stub
		if(this.label==cmp.getLabel()){
			return 0;
		}
		else if(this.label>cmp.getLabel()){
			return 1;
		}
		else
			return -1;
		
	}
	@Override
	public boolean equals(Object cmp){
		if(this.label==((ClassificationLabel)cmp).getLabel()){
			return true;
		}
		else return false;
		
	}

}
