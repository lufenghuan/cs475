package cs475;

import java.io.Serializable;

public class RegressionLabel extends Label implements Serializable {

	private double label;
	public RegressionLabel(double label) {
		// TODO Auto-generated constructor stub
		this.label=label;
	}
	
	
	public double getLabel(){
		return this.label;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (""+label);
	}

}
