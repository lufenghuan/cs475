package cs475;

import java.util.List;
/**
 * Evaluator- This is an abstract parent class for evaluation methods. 
 * For classif-cation you will need an AccuracyEvaluator (not implemented.) These should be
 * used to evaluate your model on the train and development data. You will need
 * to create AccuracyEvaluator in this assignment to evaluate the classifers you are
 * implementing (see below.)
 *
 */
public abstract class Evaluator {

	public abstract double evaluate(List<Instance> instances, Predictor predictor);
}
