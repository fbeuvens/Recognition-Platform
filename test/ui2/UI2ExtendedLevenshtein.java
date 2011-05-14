package test.ui2;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;

import test.TraceWriter;
import test.Utils;


public class UI2ExtendedLevenshtein{

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	private int category;
	private int extension;

	public UI2ExtendedLevenshtein(int category, int extension){
		this.category = category;
		this.extension = extension;
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		String trace = "";
		switch(category){
		case 1: trace = "digits/ui2/UI2Digits";break;
		case 2: trace = "gestures/ui2/UI2Gestures";break;
		case 3: trace = "letters/ui2/UI2Letters";break;
		case 4: trace = "shapes/ui2/UI2Shapes";break;
		}
		switch(extension){
		case 0: trace += "Levenshtein.txt";break;
		case 1: trace += "AngleLevenshtein.txt";break;
		case 2:	trace += "OrientationLevenshtein.txt";break;
		case 3: trace += "PressureLevenshtein.txt";break;
		case 4:	trace += "AngleDifferenceLevenshtein.txt";break;
		case 5:	trace += "OrientationDifferenceLevenshtein.txt";break;
		case 6:	trace += "PressureDifferenceLevenshtein.txt";break;
		case 7:	trace += "PressureDirectionAndLevenshtein.txt";break;
		case 8:	trace += "HoleLevenshtein.txt";break;
		case 9:	trace += "ResampleProcessedLevenshteinTest.txt";break;
		case 10: trace += "RotateProcessedLevenshteinTest.txt";break;
		case 11: trace += "ResampledAndHoleLevenshtein.txt";break;
		case 12: trace += "CostsChangedLevenshtein.txt";break;
		}
		tw = new TraceWriter(trace);
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < Utils.maxGestureIndex(category); j++)
				for(int k = 1; k <= 10; k++){
					switch(extension){
					case 0: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse()));break;
					case 1: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformAngle(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 2: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformOrientation(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 3:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformPressure(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 4:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformAngleDifference(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 5:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformOrientationDifference(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 6:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformPressureDifference(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 7:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformDirectionAndPressure(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false));break;
					case 8:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),true));break;
					case 9:	strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),true,false,false,false));break;
					case 10: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse(),false,true,false,false));break;
					case 11: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transformWithResampleAndHoles(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse()));break;
					case 12: strings.put(""+i+Utils.intToGesture(j,category)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt").parse()));break;
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
			}
		}
		tw.close();
	}

	public void UI(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[10][10]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Levenshtein lev;
			if(extension == 12)
				lev = new Levenshtein(true);
			else
				lev = new Levenshtein();
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
					lev.addTemplate(Utils.intToGesture(gestureIndex,category)+"", strings.get(""+otherUser+Utils.intToGesture(gestureIndex,category)+"1"));

					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){
						String[] foundGestures =  lev.recognizeForAllKnn(strings.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex),normalisation,knn);
						for(int i = 0; i < foundGestures.length; i++){
							String foundGesture = foundGestures[i];
							confusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
							userConfusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;	
						}
					}
				}
				System.out.println("ui2 lev: norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn),category);
	}


	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		int extension = Integer.parseInt(args[1]);
		UI2ExtendedLevenshtein ui2el = new UI2ExtendedLevenshtein(category, extension);
		ui2el.UI();
	}


}
