package test.ui2.digits.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.ui2.digits.TraceWriter;
import test.ui2.digits.Utils;


public class ExtendedLevenshteinDigits {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	private int extension;

	public ExtendedLevenshteinDigits(int extension){
		this.extension = extension;
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		switch(extension){
		case 0: tw = new TraceWriter("UI2DigitsLevenshtein.txt");break;
		case 1: tw = new TraceWriter("UI2DigitsAngleLevenshtein.txt");break;
		case 2:	tw = new TraceWriter("UI2DigitsOrientationLevenshtein.txt");break;
		case 3: tw = new TraceWriter("UI2DigitsPressureLevenshtein.txt");break;
		case 4:	tw = new TraceWriter("UI2DigitsAngleDifferenceLevenshtein.txt");break;
		case 5:	tw = new TraceWriter("UI2DigitsOrientationDifferenceLevenshtein.txt");break;
		case 6:	tw = new TraceWriter("UI2DigitsPressureDifferenceLevenshtein.txt");break;
		case 7:	tw = new TraceWriter("UI2DigitsPressureDirectionAndLevenshtein.txt");break;
		case 8:	tw = new TraceWriter("UI2DigitsHoleLevenshtein.txt");break;
		case 9:	tw = new TraceWriter("UI2DigitsResampleProcessedLevenshteinTest.txt");break;
		case 10: tw = new TraceWriter("UI2DigitsRotateProcessedLevenshteinTest.txt");break;
		case 11: tw = new TraceWriter("UI2DigitsResampleAndHolesLevenshtein.txt");break;
		case 12: tw = new TraceWriter("UI2DigitsChangedCostTableLevenshtein.txt");break;
		}
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < 10; j++)
				for(int k = 1; k <= 10; k++){
					switch(extension){
					case 0: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse()));break;
					case 1: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 2: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 3:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 4:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 5:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 6:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 7:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false));break;
					case 8:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),true));break;
					case 9:	strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),true,false,false,false));break;
					case 10: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse(),false,true,false,false));break;
					case 11: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse()));break;
					case 12: strings.put(""+i+Utils.intToDigit(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt").parse()));break;
					}
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
					confusionMatrixes.add(new int[10][10]);
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
			userConfusionMatrixes.add(new int[10][10]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Levenshtein lev = new Levenshtein();
			for(int digitIndex = 0; digitIndex < 10; digitIndex++){
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
					lev.addTemplate(Utils.intToDigit(digitIndex)+"", strings.get(""+otherUser+Utils.intToDigit(digitIndex)+"1"));

					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int digitIndex = 0; digitIndex < 10; digitIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){
						String[] foundDigits =  lev.recognizeForAllKnn(strings.get(""+user+Utils.intToDigit(digitIndex)+teIndex),normalisation,knn);
						for(int i = 0; i < foundDigits.length; i++){
							String foundDigit = foundDigits[i];
							confusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
							userConfusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;	
						}
					}
				}
				System.out.println("digits lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
	}


	public static void main(String[] args){
		int extension = Integer.parseInt(args[0]);
		ExtendedLevenshteinDigits bll = new ExtendedLevenshteinDigits(extension);
		bll.UI();
		/*System.out.println(Utils.matrixToString(bll.confusionMatrix));
		System.out.println(Utils.matrixToStringForLatex(bll.confusionMatrix));
		System.out.println("Recognition rate = "+Utils.recognitionRate(bll.confusionMatrix));*/
	}


}
