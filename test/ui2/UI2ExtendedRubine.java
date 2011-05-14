package test.ui2;

import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.TraceWriter;
import test.Utils;

public class UI2ExtendedRubine{

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;
	private int category;
	private int extension;

	public UI2ExtendedRubine(int category, int extension){
		this.category = category;
		this.extension = extension;
		confusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
		userRecognitionRate = new double[10];
		String trace = "";
		switch(category){
		case 1: trace = "digits/ui2/UI2Digits";break;
		case 2: trace = "gestures/ui2/UI2Gestures";break;
		case 3: trace = "letters/ui2/UI2Letters";break;
		case 4: trace = "shapes/ui2/UI2Shapes";break;
		}
		switch(extension){
		case 0: trace += "Rubine.txt";break;
		case 1: trace += "ResampleProcessedRubine.txt";break;
		case 2:	trace += "RotateProcessedRubine.txt";break;
		case 3: trace += "RescaleProcessedRubine.txt";break;
		case 4:	trace += "TranslateProcessedRubine.txt";break;
		}
		tw = new TraceWriter(trace);
	}



	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRate = new double[10];
			confusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
			for(int user = 1; user <= 10; user++){
				UI(user,numberTrainingExamples);
			}

			tw.println("numberTrainingExamples="+numberTrainingExamples+" :");
			tw.println();
			tw.println(Utils.matrixToString(confusionMatrix,category));
			tw.println(Utils.matrixToStringForLatex(confusionMatrix,category));
			double[] informations = Utils.informations(confusionMatrix,category);
			tw.println();
			tw.println("umberTrainingExamples="+numberTrainingExamples+" :");
			tw.println();
			for(int user = 0; user < 10; user++)
				tw.println("Recognition rate for user"+(user+1)+" = "+userRecognitionRate[user]);
			tw.println();
			tw.println("Goodclass examples = "+informations[0]);
			tw.println("Badclass examples = "+informations[1]);
			tw.println("total examples = "+informations[2]);
			tw.println("Recognition rate = "+informations[3]);
			tw.println("----------------------------------------------------------------------------------------------------------------");
			tw.println();
			tw.println();
			tw.println();

			tw.println("================================================================================================================");
			tw.println();
		}
		tw.close();
	}

	public void UI(int user, int numberTrainingExamples){
		int[][] userConfusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Classifier classifier = new Classifier();
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				GestureClass gc = new GestureClass(Utils.intToGesture(gestureIndex,category)+"");
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
					Gesture gest = new Gesture();
					List<Dot> dotSet = (new RecordParser("records/"+otherUser+"/"+Utils.intToGesture(gestureIndex,category)+"/"+Utils.intToGesture(gestureIndex,category)+"1.txt")).parse();
					gest.addGesture(dotSet, extension==1, extension==2, extension==3, extension==4);
					gc.addExample(gest);
					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
				classifier.addClass(gc);
			}
			classifier.compile();

			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				for(int teIndex = 1; teIndex <=10 ; teIndex++){
					Gesture toRecognize = new Gesture();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToGesture(gestureIndex,category)+"/"+Utils.intToGesture(gestureIndex,category)+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, extension==1, extension==2, extension==3, extension==4);

					String foundGesture = classifier.classify(toRecognize).getName();
					confusionMatrix[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
					userConfusionMatrix[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
				}
			}
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
		}
		userRecognitionRate[user-1] = Utils.recognitionRate(userConfusionMatrix,category);
	}




	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		int extension = Integer.parseInt(args[1]);
		UI2ExtendedRubine ui2er = new UI2ExtendedRubine(category, extension);
		ui2er.UI();
	}


}
