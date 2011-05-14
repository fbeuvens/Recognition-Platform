package test.ud;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.TraceWriter;
import test.Utils;

public class UDStochasticLevenshtein {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	private int category;
	private int numberTrainingExamples;

	public UDStochasticLevenshtein(int category, int numberTrainingExamples){
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
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < Utils.maxGestureIndex(category); j++)
				for(int k = 1; k <= 10; k++){
					strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse()));
				}
			System.out.println(i+" transformed");
		}
	}



	public void UD(){
		for(int normalisation = 0; normalisation <= 3; normalisation++){
			switch(category){
			case 1: tw = new TraceWriter("digits/ud/UDDigitsStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 2: tw = new TraceWriter("gestures/ud/UDGesturesStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 3: tw = new TraceWriter("letters/ud/UDLettersStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			case 4: tw = new TraceWriter("shapes/ud/UDShapesStochasticLevenshtein"+numberTrainingExamples+"Norm"+normalisation+".txt");break;
			}
			tw = new TraceWriter("UDGesturesStochasticLevenshtein2Norm"+normalisation+".txt");
			tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
			tw.println();

			confusionMatrixes.clear();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				confusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRates = new double[10][9];
			for(int user = 1; user <= 5; user++){
				UD(user,numberTrainingExamples,normalisation);
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

	public void UD(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			String[] alphabet = {"0","1","2","3","4","5","6","7"};
			ConditionalRecognizer cr = new ConditionalRecognizer(alphabet);
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples; teIndex++){
					cr.addTemplate(Utils.intToGesture(gestureIndex,category)+"", strings.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex));
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			cr.compile(normalisation);

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						String[] foundGestures =  cr.recognizeForAllKnn(strings.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex),normalisation,knn);
						for(int i = 0; i < foundGestures.length; i++){
							String foundGesture = foundGestures[i];
							confusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
							userConfusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
						}
					}
					System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+"|char="+Utils.intToGesture(gestureIndex,category)+" recognized");
				}
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn),category);
	}


	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		int numberTrainingExamples = Integer.parseInt(args[1]);
		UDStochasticLevenshtein udsl = new UDStochasticLevenshtein(category, numberTrainingExamples);
		udsl.UD();
	}
}
