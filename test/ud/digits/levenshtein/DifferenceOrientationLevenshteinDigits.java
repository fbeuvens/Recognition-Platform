package test.ud.digits.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.ud.digits.TraceWriter;
import test.ud.digits.Utils;


public class DifferenceOrientationLevenshteinDigits {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	
	public DifferenceOrientationLevenshteinDigits(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		tw = new TraceWriter("UDDigitsOrientationDifferenceLevenshtein.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < 10; j++)
				for(int k = 1; k <= 10; k++){
					//strings.put(""+i+Utils.intToDigit(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToDigit(j)+"\\"+Utils.intToDigit(j)+k+".txt").parse()));
					strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));
				}
			System.out.println(i+" transformed");
		}
	}
	
	
	
	public void UD(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
		for(int normalisation = 0; normalisation <= 3; normalisation++){
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[10][10]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 5; user++){
					UD(user,numberTrainingExamples,normalisation);
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
	
	public void UD(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[10][10]);
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Levenshtein lev = new Levenshtein();
			for(int digitIndex = 0; digitIndex < 10; digitIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					lev.addTemplate(Utils.intToDigit(digitIndex)+"", strings.get(""+user+Utils.intToDigit(digitIndex)+teIndex));
					/*if(teIndex == firstTeIndex)
						firstTeIndexPassage = false;*/
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int digitIndex = 0; digitIndex < 10; digitIndex++){
					//boolean firstTeIndexPassage = true;
					//int i = 0;
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						//System.out.println("teIndex = "+teIndex);
						/*String foundDigit = lev.recognize(strings.get(""+user+Utils.intToDigit(digitIndex)+teIndex),normalisation,knn);
						confusionMatrixes.get(knn-1)[digitIndex][Utils.digitToInt(foundDigit)]++;
						userConfusionMatrixes.get(knn-1)[digitIndex][Utils.digitToInt(foundDigit)]++;
						if(teIndex == tmp)
							firstTeIndexPassage = false;
						i++;*/
						String[] foundDigits =  lev.recognizeForAllKnn(strings.get(""+user+Utils.intToDigit(digitIndex)+teIndex),normalisation,knn);
						for(int i = 0; i < foundDigits.length; i++){
							String foundDigit = foundDigits[i];
							confusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
							userConfusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
						}
					}
					//System.out.println(i);
				}
			}
			System.out.println("digits lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}

	
	public static void main(String[] args){
		DifferenceOrientationLevenshteinDigits bll = new DifferenceOrientationLevenshteinDigits();
		bll.UD();
		/*System.out.println(Utils.matrixToString(bll.confusionMatrix));
		System.out.println(Utils.matrixToStringForLatex(bll.confusionMatrix));
		System.out.println("Recognition rate = "+Utils.recognitionRate(bll.confusionMatrix));*/
	}
	
	
}
