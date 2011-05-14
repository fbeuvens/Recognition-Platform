package test.ui2.gestures.stochasticLevenshtein;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.ui2.gestures.TraceWriter;
import test.ui2.gestures.Utils;

public class BaseStochasticLevenshteinGestures7 {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;

	public BaseStochasticLevenshteinGestures7(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		//tw = new TraceWriter("StochasticLevenshteinGestures.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < 16; j++)
				for(int k = 1; k <= 10; k++){
					//strings.put(""+i+Utils.intToGest(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToGest(j)+"\\"+Utils.intToGest(j)+k+".txt").parse()));
					strings.put(""+i+Utils.intToGest(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGest(j)+"/"+Utils.intToGest(j)+k+".txt").parse()));
				}
			System.out.println(i+" transformed");
		}
	}



	public void UI(){
		for(int normalisation = 0; normalisation <= 0; normalisation++){
			tw = new TraceWriter("UI2GesturesStochasticLevenshtein7Norm"+normalisation+".txt");
			tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
			tw.println();
			for(int numberTrainingExamples = 7; numberTrainingExamples <= 7; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[16][16]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 10; user++){
					UI(user,numberTrainingExamples,normalisation);
				}
				for(int knn = 0; knn < confusionMatrixes.size(); knn++){
					tw.println("normalisation="+normalisation+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					tw.println(Utils.matrixToString(confusionMatrixes.get(knn)));
					tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn)));
					double[] informations = Utils.informations(confusionMatrixes.get(knn));
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
			}
			tw.close();
		}

	}

	public void UI(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[16][16]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			String[] alphabet = {"0","1","2","3","4","5","6","7"};
			ConditionalRecognizer cr = new ConditionalRecognizer(alphabet);
			for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
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
					cr.addTemplate(Utils.intToGest(gestureIndex)+"", strings.get(""+otherUser+Utils.intToGest(gestureIndex)+"1"));
					
					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			cr.compile(normalisation);

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){

						String[] foundGestures =  cr.recognizeForAllKnn(strings.get(""+user+Utils.intToGest(gestureIndex)+teIndex),normalisation,knn);
						for(int i = 0; i < foundGestures.length; i++){
							String foundGest = foundGestures[i];
							confusionMatrixes.get(i)[gestureIndex][Utils.gestToInt(foundGest)]++;
							userConfusionMatrixes.get(i)[gestureIndex][Utils.gestToInt(foundGest)]++;
						}
						System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+"|char="+Utils.intToGest(gestureIndex)+" recognized");
					}
				}
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
	}



	public static void main(String[] args){
		BaseStochasticLevenshteinGestures7 bsll = new BaseStochasticLevenshteinGestures7();
		bsll.UI();
		//Utils.print(bsll.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(bsll.confusionMatrix));
	}
}
