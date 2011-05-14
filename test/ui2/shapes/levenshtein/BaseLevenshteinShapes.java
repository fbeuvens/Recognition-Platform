package test.ui2.shapes.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.ui2.shapes.TraceWriter;
import test.ui2.shapes.Utils;


public class BaseLevenshteinShapes {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;

	public BaseLevenshteinShapes(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		tw = new TraceWriter("UI2ShapesLevenshtein.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < 8; j++)
				for(int k = 1; k <= 10; k++){
					//strings.put(""+i+Utils.intToShape(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToShape(j)+"\\"+Utils.intToShape(j)+k+".txt").parse()));
					strings.put(""+i+Utils.intToShape(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToShape(j)+"/"+Utils.intToShape(j)+k+".txt").parse()));
				}
			System.out.println(i+" transformed");
		}
	}



	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
		for(int normalisation = 0; normalisation <= 3; normalisation++){
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[8][8]);
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
		}
		tw.close();
	}

	public void UI(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[8][8]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Levenshtein lev = new Levenshtein();
			for(int shapeIndex = 0; shapeIndex < 8; shapeIndex++){
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
					lev.addTemplate(Utils.intToShape(shapeIndex)+"", strings.get(""+otherUser+Utils.intToShape(shapeIndex)+"1"));

					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int shapeIndex = 0; shapeIndex < 8; shapeIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){
						String[] foundShapes =  lev.recognizeForAllKnn(strings.get(""+user+Utils.intToShape(shapeIndex)+teIndex),normalisation,knn);
						for(int i = 0; i < foundShapes.length; i++){
							String foundShape = foundShapes[i];
							confusionMatrixes.get(i)[shapeIndex][Utils.shapeToInt(foundShape)]++;
							userConfusionMatrixes.get(i)[shapeIndex][Utils.shapeToInt(foundShape)]++;	
						}
					}
				}
				System.out.println("shapes lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
	}


	public static void main(String[] args){
		BaseLevenshteinShapes bll = new BaseLevenshteinShapes();
		bll.UI();
		/*System.out.println(Utils.matrixToString(bll.confusionMatrix));
		System.out.println(Utils.matrixToStringForLatex(bll.confusionMatrix));
		System.out.println("Recognition rate = "+Utils.recognitionRate(bll.confusionMatrix));*/
	}


}
