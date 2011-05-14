package test.ud.gestures.rubine;

import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.ud.gestures.TraceWriter;
import test.ud.gestures.Utils;

public class BaseRubineGestures {

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;
	
	public BaseRubineGestures(){
		tw = new TraceWriter("UDGesturesRubine.txt");
	}
	
	
	
	public void UD(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRate = new double[10];
			confusionMatrix = new int[16][16];
			for(int user = 1; user <= 5; user++){
				UD(user,numberTrainingExamples);
			}
		
			tw.println("numberTrainingExamples="+numberTrainingExamples+" :");
			tw.println();
			tw.println(Utils.matrixToString(confusionMatrix));
			tw.println(Utils.matrixToStringForLatex(confusionMatrix));
			double[] informations = Utils.informations(confusionMatrix);
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
		int[][] userConfusionMatrix = new int[16][16];
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Classifier classifier = new Classifier();
			for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
				GestureClass gc = new GestureClass(Utils.intToGest(gestureIndex)+"");
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					Gesture gest = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToGest(characterIndex)+"\\"+Utils.intToGest(characterIndex)+teIndex+".txt")).parse();
					//System.out.println("records/"+user+"/"+Utils.intToGest(gestureIndex)+"/"+Utils.intToGest(gestureIndex)+teIndex+".txt");
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToGest(gestureIndex)+"/"+Utils.intToGest(gestureIndex)+teIndex+".txt")).parse();
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
			
		
			for(int gestureIndex = 0; gestureIndex < 16; gestureIndex++){
				int tmp = (firstTeIndex+numberTrainingExamples)%10;
				if(tmp == 0) tmp = 10;
				for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
					Gesture toRecognize = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToGest(characterIndex)+"\\"+Utils.intToGest(characterIndex)+teIndex+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToGest(gestureIndex)+"/"+Utils.intToGest(gestureIndex)+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, false, false, false, false);
					
					String foundDigit = classifier.classify(toRecognize).getName();
					confusionMatrix[gestureIndex][Utils.gestToInt(foundDigit)]++;
					userConfusionMatrix[gestureIndex][Utils.gestToInt(foundDigit)]++;
				}
			}
			
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		
		userRecognitionRate[user-1] = Utils.recognitionRate(userConfusionMatrix);
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}
	
	

	
	public static void main(String[] args){
		BaseRubineGestures brd = new BaseRubineGestures();
		brd.UD();
		//List<Dot> dotSet = (new RecordParser("records/3/leftArrowUpArrow/leftArrowUpArrow8.txt")).parse();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}
	
	
}
