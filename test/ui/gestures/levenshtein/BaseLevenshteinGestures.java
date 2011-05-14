package test.ui.gestures.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.ui.gestures.TraceWriter;
import test.ui.gestures.Utils;


public class BaseLevenshteinGestures {

	private List<int[][]> confusionMatrixes;
	private double[][] usersTrainingRecognitionRate;
	private double[][] usersRecognitionRate;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	
	public BaseLevenshteinGestures(){
		confusionMatrixes = new ArrayList<int[][]>();
		strings = new Hashtable<String, String>();
		tw = new TraceWriter("UIGesturesLevenshtein.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < 16; j++)
				for(int k = 1; k <= 10; k++){
					//strings.put(""+i+Utils.intToGest(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToGest(j)+"\\"+Utils.intToGest(j)+k+".txt").parse()));
					strings.put(""+i+Utils.intToGest(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGest(j)+"/"+Utils.intToGest(j)+k+".txt").parse()));
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
					confusionMatrixes.add(new int[16][16]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				usersTrainingRecognitionRate = new double[10][9];
				usersRecognitionRate = new double[10][9];
				for(int user = 1; user <= 5; user++){
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
					for(int user = 0; user < 5; user++)
						tw.println("Recognition rate for training whith user"+(user+1)+" = "+usersTrainingRecognitionRate[user][knn]);
					tw.println();
					for(int user = 0; user < 5; user++)
						tw.println("Recognition rate for user"+(user+1)+" = "+usersRecognitionRate[user][knn]);
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
		List<int[][]> userTrainingConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userTrainingConfusionMatrixes.add(new int[16][16]);
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int i = 0; i < 5; i++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[16][16]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}
		
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Levenshtein lev = new Levenshtein();
			for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					lev.addTemplate(Utils.intToGest(gestureIndex)+"", strings.get(""+user+Utils.intToGest(gestureIndex)+teIndex));
					/*if(teIndex == firstTeIndex)
						firstTeIndexPassage = false;*/
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
					//boolean firstTeIndexPassage = true;
					//int i = 0;
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						//System.out.println("teIndex = "+teIndex);
						for(int otherUser = 1; otherUser <= 5; otherUser++)
							if(otherUser != user){
								String[] foundGestures =  lev.recognizeForAllKnn(strings.get(""+otherUser+Utils.intToGest(gestureIndex)+teIndex),normalisation,knn);
								for(int i = 0; i < foundGestures.length; i++){
									String foundGesture = foundGestures[i];
									confusionMatrixes.get(i)[gestureIndex][Utils.gestToInt(foundGesture)]++;
									userTrainingConfusionMatrixes.get(i)[gestureIndex][Utils.gestToInt(foundGesture)]++;
									usersConfusionMatrixes.get(otherUser-1).get(i)[gestureIndex][Utils.gestToInt(foundGesture)]++;
								}
							}
						/*if(teIndex == tmp)
							firstTeIndexPassage = false;
						i++;*/
					}
					//System.out.println(i);
				}
			}
			System.out.println("gestures lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++){
			usersTrainingRecognitionRate[user-1][knn] = Utils.recognitionRate(userTrainingConfusionMatrixes.get(knn));
			for(int i = 0; i < 5; i++)
				if(i != user-1)
					usersRecognitionRate[i][knn] += Utils.recognitionRate(usersConfusionMatrixes.get(i).get(knn))/4;
		}
	}

	
	public static void main(String[] args){
		BaseLevenshteinGestures bll = new BaseLevenshteinGestures();
		bll.UI();
		/*System.out.println(Utils.matrixToString(bll.confusionMatrix));
		System.out.println(Utils.matrixToStringForLatex(bll.confusionMatrix));
		System.out.println("Recognition rate = "+Utils.recognitionRate(bll.confusionMatrix));*/
	}
	
	
}
