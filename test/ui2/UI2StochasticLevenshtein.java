package test.ui2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.TraceWriter;
import test.Utils;

public class UI2StochasticLevenshtein{

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	private int category;
	private int numberTrainingExamples;

	public UI2StochasticLevenshtein(int category, int numberTrainingExamples){
		this.category = category;
		this.numberTrainingExamples = numberTrainingExamples;
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < Utils.maxGestureIndex(category); j++)
				for(int k = 1; k <= 10; k++){
					strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse()));
				}
			System.out.println(i+" transformed");
		}
	}



	public void UI(){
		for(int normalisation = 0; normalisation <= 3; normalisation++){
			switch(category){
			case 1: tw = new TraceWriter("digits/ui2/UI2DigitsStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 2: tw = new TraceWriter("gestures/ui2/UI2GesturesStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 3: tw = new TraceWriter("letters/ui2/UI2LettersStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 4: tw = new TraceWriter("shapes/ui2/UI2ShapesStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			}
			tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
			tw.println();
			confusionMatrixes.clear();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				confusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRates = new double[10][9];
			for(int user = 1; user <= 10; user++){
				UI(user,numberTrainingExamples,normalisation);
			}
			for(int knn = 0; knn < confusionMatrixes.size(); knn++){
				tw.println("normalisation="+normalisation+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
				tw.println();
				tw.println(Utils.matrixToString(confusionMatrixes.get(knn),category));
				tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn),category));
				double[] informations = Utils.informations(confusionMatrixes.get(knn),category);
				tw.println();
				tw.println("normalisation="+normalisation+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
				tw.println();
				for(int user = 0; user < 10; user++)
					tw.println("Recognition rate for user"+(user+1)+" = "+userRecognitionRates[user][knn]);
				tw.println();
				tw.println("Goodclass examples = "+informations[0]);
				tw.println("Badclass examples = "+informations[1]);
				tw.println("total examples = "+informations[2]);
				tw.println("Recognition rate = "+informations[3]);
				tw.println("----------------------------------------------------------------------------------------------------------------");
				tw.println();
				tw.println();
				tw.println();
			}
			tw.println("================================================================================================================");
			tw.println();

			tw.close();
		}

	}

	public void UI(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			String[] alphabet = {"0","1","2","3","4","5","6","7"};
			ConditionalRecognizer cr = new ConditionalRecognizer(alphabet);
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				int mod = 0;
				int threshold = firstUserIndex+numberTrainingExamples;
				for(int otherUser = firstUserIndex; mod*10+otherUser < threshold  ; otherUser++){
					if(otherUser == user){
						if(otherUser == 10){
							otherUser = 1;
							mod = 1;
						}
						else
							otherUser++;
						threshold++;
					}
					cr.addTemplate(Utils.intToGesture(gestureIndex,category)+"", strings.get(""+otherUser+Utils.intToGesture(gestureIndex,category)+"1"));

					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			cr.compile(normalisation);

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){

						String[] foundGestures =  cr.recognizeForAllKnn(strings.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex),normalisation,knn);
						for(int i = 0; i < foundGestures.length; i++){
							String foundGesture = foundGestures[i];
							confusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
							userConfusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
						}
						System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+"|char="+Utils.intToGesture(gestureIndex,category)+" recognized");
					}
				}
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn),category);
	}



	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		int numberTrainingExamples = Integer.parseInt(args[1]);
		UI2StochasticLevenshtein ui2sl = new UI2StochasticLevenshtein(category, numberTrainingExamples);
		ui2sl.UI();
	}
}
