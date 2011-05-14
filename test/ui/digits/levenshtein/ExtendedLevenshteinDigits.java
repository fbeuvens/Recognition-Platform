package test.ui.digits.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.ui.digits.TraceWriter;
import test.ui.digits.Utils;


public class ExtendedLevenshteinDigits {

	private List<int[][]> confusionMatrixes;
	private double[][] usersTrainingRecognitionRate;
	private double[][] usersRecognitionRate;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	private int extension;

	public ExtendedLevenshteinDigits(int extension){
		this.extension = extension;
		confusionMatrixes = new ArrayList<int[][]>();
		strings = new Hashtable<String, String>();
		switch(extension){
		case 0: tw = new TraceWriter("UIDigitsLevenshtein.txt");break;
		case 1: tw = new TraceWriter("UIDigitsAngleLevenshtein.txt");break;
		case 2:	tw = new TraceWriter("UIDigitsOrientationLevenshtein.txt");break;
		case 3: tw = new TraceWriter("UIDigitsPressureLevenshtein.txt");break;
		case 4:	tw = new TraceWriter("UIDigitsAngleDifferenceLevenshtein.txt");break;
		case 5:	tw = new TraceWriter("UIDigitsOrientationDifferenceLevenshtein.txt");break;
		case 6:	tw = new TraceWriter("UIDigitsPressureDifferenceLevenshtein.txt");break;
		case 7:	tw = new TraceWriter("UIDigitsPressureDirectionAndLevenshtein.txt");break;
		case 8:	tw = new TraceWriter("UIDigitsHoleLevenshtein.txt");break;
		case 9:	tw = new TraceWriter("UIDigitsResampleProcessedLevenshtein.txt");break;
		case 10: tw = new TraceWriter("UIDigitsRotateProcessedLevenshtein.txt");break;
		case 11: tw = new TraceWriter("UIDigitsResampleAndHolesLevenshtein.txt");break;
		case 12: tw = new TraceWriter("UIDigitsChangedCostTableLevenshtein.txt");break;
		}

		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 5; i++){
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
		for(int normalisation = 0; normalisation <= 0; normalisation++){
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[10][10]);
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
			userTrainingConfusionMatrixes.add(new int[10][10]);
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int i = 0; i < 5; i++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[10][10]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}

		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Levenshtein lev;
			if(extension == 12)
				lev = new Levenshtein(true);
			else
				lev = new Levenshtein();
			
			for(int digitIndex = 0; digitIndex < 10; digitIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					lev.addTemplate(Utils.intToDigit(digitIndex)+"", strings.get(""+user+Utils.intToDigit(digitIndex)+teIndex));
		
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
						for(int otherUser = 1; otherUser <= 5; otherUser++)
							if(otherUser != user){
								String[] foundDigits =  lev.recognizeForAllKnn(strings.get(""+otherUser+Utils.intToDigit(digitIndex)+teIndex),normalisation,knn);
								for(int i = 0; i < foundDigits.length; i++){
									String foundDigit = foundDigits[i];
									confusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
									userTrainingConfusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
									usersConfusionMatrixes.get(otherUser-1).get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
								}
							}
						/*if(teIndex == tmp)
							firstTeIndexPassage = false;
						i++;*/
					}
					//System.out.println(i);
				}
			}
			System.out.println("digits lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++){
			usersTrainingRecognitionRate[user-1][knn] = Utils.recognitionRate(userTrainingConfusionMatrixes.get(knn));
			for(int i = 0; i < 5; i++)
				if(i != user-1)
					usersRecognitionRate[i][knn] += Utils.recognitionRate(usersConfusionMatrixes.get(i).get(knn))/4;
		}
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
