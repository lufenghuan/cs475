package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	static public LinkedList<Option> options = new LinkedList<Option>();
	static double thickness;
	static int max_decision_tree_depth;
	static double lambda;
	static double online_learning_rate;
	static int online_training_iterations;

	//HW4
	static double gradient_ascent_learning_rate;
	static int gradient_ascent_training_iterations;
	static double polynomial_kernel_exponent;
	static double gaussian_kernel_sigma ;

	//HW5
	static int k_nn;
	static boolean classification;
	static int k_ensemble ;
	static int ensemble_training_iterations;
	static double ensemble_learning_rate ;
	
	//HW6
	static double cluster_lambda;
	static int clustering_training_iterations;
	

	public static void main(String[] args) throws IOException {
		DataReader data_reader;

		String[]regressionAlgorithms={"knn","knn_distance"};

		// Parse the command line.
		max_decision_tree_depth = 4;
		lambda=1;
		thickness = 0.0;
		online_training_iterations = 1;

		//HW4
		gradient_ascent_learning_rate = 0.01;
		gradient_ascent_training_iterations = 5;
		polynomial_kernel_exponent = 2;
		gaussian_kernel_sigma = 1;

		//HW5
		k_nn = 5;
		classification=true;
		
		k_ensemble = 5;
		ensemble_training_iterations = 5;
		ensemble_learning_rate = 0.1;
		//HW6
		cluster_lambda = 0.0;
		clustering_training_iterations = 10;

		
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);

		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");

		//is classification?
		for(String str:regressionAlgorithms){
			if(algorithm!=null&&algorithm.equalsIgnoreCase(str)){
				classification=false;
				break;
			}
		}


		if (CommandLineUtilities.hasArg("max_decision_tree_depth"))
			max_decision_tree_depth = CommandLineUtilities.getOptionValueAsInt("max_decision_tree_depth");

		if (CommandLineUtilities.hasArg("lambda"))
			lambda = CommandLineUtilities.getOptionValueAsFloat("lambda");


		if (CommandLineUtilities.hasArg("thickness"))
			thickness = CommandLineUtilities.getOptionValueAsFloat("thickness");





		if (CommandLineUtilities.hasArg("online_training_iterations"))
			online_training_iterations = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");

		//HW4
		if (CommandLineUtilities.hasArg("gradient_ascent_learning_rate"))
			gradient_ascent_learning_rate =
			CommandLineUtilities.getOptionValueAsFloat("gradient_ascent_learning_rate");

		if (CommandLineUtilities.hasArg("gradient_ascent_training_iterations"))
			gradient_ascent_training_iterations =
			CommandLineUtilities.getOptionValueAsInt("gradient_ascent_training_iterations");


		if (CommandLineUtilities.hasArg("polynomial_kernel_exponent"))
			polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsFloat("polynomial_kernel_exponent");

		if (CommandLineUtilities.hasArg("gaussian_kernel_sigma"))
			gaussian_kernel_sigma = CommandLineUtilities.getOptionValueAsFloat("gaussian_kernel_sigma");

		//HW5
		if (CommandLineUtilities.hasArg("k_nn"))
			k_nn = CommandLineUtilities.getOptionValueAsInt("k_nn");
		if (CommandLineUtilities.hasArg("k_ensemble"))
			k_ensemble = CommandLineUtilities.getOptionValueAsInt("k_ensemble");
		if (CommandLineUtilities.hasArg("ensemble_training_iterations"))
			ensemble_training_iterations = CommandLineUtilities.getOptionValueAsInt("ensemble_training_iterations");
		
		if (CommandLineUtilities.hasArg("ensemble_learning_rate"))
			ensemble_learning_rate = CommandLineUtilities.getOptionValueAsFloat("ensemble_learning_rate");

		//HW6
		if (CommandLineUtilities.hasArg("cluster_lambda"))
			cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");
		if (CommandLineUtilities.hasArg("clustering_training_iterations"))
			clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
		
		
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}

			online_learning_rate = algorithm.equalsIgnoreCase("winnow") ? 2.0 : 1.0;

			if (CommandLineUtilities.hasArg("online_learning_rate"))
				online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");

			// Load the training data.
			if(!classification)
				data_reader = new DataReader(data, false);//regression
			else
				data_reader=new DataReader(data, true);

			List<Instance> instances = data_reader.readData();
			data_reader.close();

			// Train the model.
			Predictor predictor = train(instances, algorithm);
			saveObject(predictor, model_file);		

		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}

			// Load the test data.
			if(!classification)
				data_reader = new DataReader(data, false);//regression
			else
				data_reader=new DataReader(data, true);

			List<Instance> instances = data_reader.readData();
			data_reader.close();

			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}


	private static Predictor train(List<Instance> instances, String algorithm) {
		// TODO Train the model using "algorithm" on "data"
		// TODO Evaluate the model
		Predictor predictor=null;
		String[]algorithms={"even_odd","majority","decision_tree","naive_bayes","Perceptron","Winnow",
				"logistic_regression_linear_kernel","logistic_regression_polynomial_kernel","logistic_regression_gaussian_kernel",
				"knn","knn_distance","instance_bagging","feature_bagging","lambda_means"};
		boolean contain=false;
		for(String str:algorithms){
			if (str.equalsIgnoreCase(algorithm)){
				contain=true;
				break;
			}
		}

		while (!contain){
			System.out.print("Train only support:");
			for(String str:algorithms){
				System.out.print(str+", ");
			}
			System.out.print("right now. Enter commend again");
			Scanner sc=new Scanner (System.in);
			algorithm=sc.nextLine();
		}

		if (algorithm.equalsIgnoreCase("even_odd")){
			predictor=new EvenOddClassifier();
		}
		else if(algorithm.equalsIgnoreCase("majority")){
			predictor=new MajorityClassifier();
		}
		else if(algorithm.equalsIgnoreCase("decision_tree")){
			predictor=new DTClassifier(max_decision_tree_depth);

		}
		else if(algorithm.equalsIgnoreCase("naive_bayes")){
			predictor=new NaiveBayesClassifier(lambda);
		}
		else if(algorithm.equalsIgnoreCase("Perceptron")){
			predictor=new PerceptronPredictor(online_training_iterations,online_learning_rate,thickness);
		}
		else if(algorithm.equalsIgnoreCase("Winnow")){
			predictor=new WinnowPredictor(online_training_iterations,online_learning_rate,thickness,instances.size()/2);
		}
		else if(algorithm.equalsIgnoreCase("logistic_regression_linear_kernel")){
			predictor=new LinearKernelLogisticRegression(gradient_ascent_training_iterations,gradient_ascent_learning_rate);
		}
		else if(algorithm.equalsIgnoreCase("logistic_regression_polynomial_kernel")){
			predictor=new PolynomialKernelLogisticRegression(gradient_ascent_training_iterations, 
					gradient_ascent_learning_rate, polynomial_kernel_exponent);
		}
		else if(algorithm.equalsIgnoreCase("logistic_regression_gaussian_kernel")){
			predictor=new GaussianKernelLogisticRegression(gradient_ascent_training_iterations, 
					gradient_ascent_learning_rate, gaussian_kernel_sigma);
		}
		else if(algorithm.equalsIgnoreCase("knn")){
			predictor=new SimpleKNNPredictor(k_nn);
		}
		else if(algorithm.equalsIgnoreCase("knn_distance")){
			predictor=new DistanceWeightedKNNPredictor(k_nn);
		}
		else if(algorithm.equalsIgnoreCase("instance_bagging")){
			predictor=new EnsembleInstanceBaggingPredictor(k_ensemble, ensemble_training_iterations, ensemble_learning_rate);
		}
		else if(algorithm.equalsIgnoreCase("feature_bagging")){
			predictor=new EnsembleFeatureBaggingPredictor(k_ensemble, ensemble_training_iterations, ensemble_learning_rate);
		}
		else if(algorithm.equalsIgnoreCase("lambda_means")){
			predictor=new LambdaMeanPredictor(cluster_lambda, clustering_training_iterations);
		}
		predictor.train(instances);

		//print out evaluation result
		if(classification)
			System.out.println("Accuracy of "+predictor.toString()+
					" on traning data set: "+new AccuracyEvaluator().evaluate(instances, predictor));

		return predictor;


	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available. 
		boolean labelsAvaliable=true;
		for (Instance instance : instances) {
			if(instance.getLabel()==null){
				labelsAvaliable=false;
				break;
			}
			//			if(((ClassificationLabel)instance.getLabel())==null){
			//				labelsAvaliable=false;
			//				break;
			//			}
		}
		if(labelsAvaliable&&classification)
			System.out.println("Classification: Accuracy of "+predictor.toString()+
					" on test data set: "+new AccuracyEvaluator().evaluate(instances, predictor));
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}

		writer.close();
		if(!classification){
			System.out.println("!classfication Accuracy of "+predictor.toString()+
					" on test data set: "+new RegressionAccuracyEcaluator(predictions_file).evaluate(instances, predictor));
		}
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
					new ObjectOutputStream(new BufferedOutputStream(
							new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}

	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);

		Classify.options.add(option);		
	}

	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		registerOption("max_decision_tree_depth", "int", true, "The maximum depth of the decision tree.");
		registerOption("lambda", "double", true, "The level of smoothing for Naive Bayes.");
		registerOption("thickness", "double", true, "The value of the linear separator thickness.");
		registerOption("online_learning_rate", "double", true, "The LTU learning rate.");
		registerOption("online_training_iterations", "int", true, "The number of training iterations for LTU.");

		//HW4
		registerOption("gradient_ascent_learning_rate", "double", true, "The learning rate for logistic regression.");
		registerOption("gradient_ascent_training_iterations", "int", true, "The number of training iterations.");
		registerOption("polynomial_kernel_exponent", "double", true, "The exponent of the polynomial kernel.");
		registerOption("gaussian_kernel_sigma", "double", true, "The sigma of the Gaussian kernel.");

		//HW5
		registerOption("k_nn", "int", true, "The value of K for KNN regression.");
		
		registerOption("k_ensemble", "double", true, "The number of classifiers in the ensemble.");
		registerOption("ensemble_training_iterations", "int", true, "The number of ensemble training iterations.");
		registerOption("ensemble_learning_rate", "double", true, "The ensemble learning rate.");
		
		//HW6
		registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
		registerOption("clustering_training_iterations", "int", true, "The number of lambda-means EM iterations.");
		// Other options will be added here.
	}

}
