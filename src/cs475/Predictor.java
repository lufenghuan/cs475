package cs475;

import java.io.Serializable;
import java.util.List;

/**
 * Predictor- This is an abstract class that will be the parent class for all learning
 * algorithms. Learning algorithms must implement the train and predict methods.
 * Predictors must be serializable so that they can be saved after training.
 */
public abstract class Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract void train(List<Instance> instances);
	
	public abstract Label predict(Instance instance);
	
	public abstract String toString();
}
