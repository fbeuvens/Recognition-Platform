package test.ud;

import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.TraceWriter;
import test.Utils;

public class UDRubine {

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;
	private int category;
	
	public UDRubine(int category){
		this.category = category;
		confusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
		userRecognitionRate = new double[5];
		switch(category){
		case 1: tw = new TraceWriter("digits/ud/UDDigitsRubineTest.txt");break;
		case 2: tw = new TraceWriter("gestures/ud/UDGesturesRubine.txt");break;
		case 3: tw = new TraceWriter("letters/ud/UDLettersRubine.txt");break;
		case 4: tw = new TraceWriter("shapes/ud/UDShapesRubine.txt");break;
		}
	}
	
	
	
	public void UD(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 1; numberTrainingExamples <= 1; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRate = new double[10];
			confusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
			for(int user = 1; user <= 1; user++){
				UD(user,numberTrainingExamples);
			}
		
			tw.println("numberTrainingExamples="+numberTrainingExamples+" :");
			tw.println();
			tw.println(Utils.matrixToString(confusionMatrix,category));
			tw.println(Utils.matrixToStringForLatex(confusionMatrix,category));
			double[] informations = Utils.informations(confusionMatrix,category);
			tw.println();
			tw.println("umberTrainingExamples="+numberTrainingExamples+" :");
			tw.println();
			for(int user = 0; user < 5; user++)
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
	
	public void UD(int user, int numberTrainingExamples){
		int[][] userConfusionMatrix = new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)];
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Classifier classifier = new Classifier();
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				GestureClass gc = new GestureClass(Utils.intToGesture(gestureIndex,category)+"");
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples; teIndex++){
					Gesture gest = new Gesture();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToGesture(gestureIndex,category)+"/"+Utils.intToGesture(gestureIndex,category)+teIndex+".txt")).parse();
					gest.addGesture(dotSet, false, false, false, false);
					gc.addExample(gest);
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
				classifier.addClass(gc);
			}
			classifier.compile();
			
		
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				int tmp = (firstTeIndex+numberTrainingExamples)%10;
				if(tmp == 0) tmp = 10;
				for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
					Gesture toRecognize = new Gesture();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToGesture(gestureIndex,category)+"/"+Utils.intToGesture(gestureIndex,category)+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, false, false, false, false);
					
					String foundGesture = classifier.classify(toRecognize).getName();
					confusionMatrix[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
					userConfusionMatrix[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
				}
			}
			
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		
		userRecognitionRate[user-1] = Utils.recognitionRate(userConfusionMatrix,category);
	}
	
	

	
	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		UDRubine udr = new UDRubine(category);
		udr.UD();
	}
	
	
}
