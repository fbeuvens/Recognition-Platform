package test.ui.shapes.rubine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.ui.shapes.TraceWriter;
import test.ui.shapes.Utils;

public class BaseRubineShapes {

	private int[][] confusionMatrix;
	private double[] usersTrainingRecognitionRate;
	private double[] usersRecognitionRate;	
	private TraceWriter tw;
	
	public BaseRubineShapes(){
		tw = new TraceWriter("UIShapesRubine.txt");
	}
	
	
	
	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			usersTrainingRecognitionRate = new double[5];
			usersRecognitionRate = new double[5];
			confusionMatrix = new int[8][8];
			for(int user = 1; user <= 5; user++){
				UI(user,numberTrainingExamples);
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
				tw.println("Recognition rate for training whith user"+(user+1)+" = "+usersTrainingRecognitionRate[user]);
			tw.println();
			for(int user = 0; user < 5; user++)
				tw.println("Recognition rate for user"+(user+1)+" = "+usersRecognitionRate[user]);
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
		int[][] userTrainingConfusionMatrix = new int[8][8];
		List<int[][]> usersConfusionMatrixes = new ArrayList<int[][]>();
		for(int i = 0; i < 5; i++)
			usersConfusionMatrixes.add(new int[8][8]);
		
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Classifier classifier = new Classifier();
			for(int gestureIndex = 0; gestureIndex < 8; gestureIndex++){
				GestureClass gc = new GestureClass(Utils.intToShape(gestureIndex)+"");
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					Gesture gest = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToShape(characterIndex)+"\\"+Utils.intToShape(characterIndex)+teIndex+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToShape(gestureIndex)+"/"+Utils.intToShape(gestureIndex)+teIndex+".txt")).parse();
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
			
		
			for(int gestureIndex = 0; gestureIndex < 8; gestureIndex++){
				int tmp = (firstTeIndex+numberTrainingExamples)%10;
				if(tmp == 0) tmp = 10;
				for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
					for(int otherUser = 1; otherUser <= 5; otherUser++)
						if(otherUser != user){
							Gesture toRecognize = new Gesture();
							//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToShape(characterIndex)+"\\"+Utils.intToShape(characterIndex)+teIndex+".txt")).parse();
							List<Dot> dotSet = (new RecordParser("records/"+otherUser+"/"+Utils.intToShape(gestureIndex)+"/"+Utils.intToShape(gestureIndex)+teIndex+".txt")).parse();
							toRecognize.addGesture(dotSet, false, false, false, false);
							
							String foundDigit = classifier.classify(toRecognize).getName();
							confusionMatrix[gestureIndex][Utils.shapeToInt(foundDigit)]++;
							userTrainingConfusionMatrix[gestureIndex][Utils.shapeToInt(foundDigit)]++;
							usersConfusionMatrixes.get(otherUser-1)[gestureIndex][Utils.shapeToInt(foundDigit)]++;
						}
				}
			}
			
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		
		usersTrainingRecognitionRate[user-1] = Utils.recognitionRate(userTrainingConfusionMatrix);
		for(int i = 0; i < 5; i++)
			if(i != user-1)
				usersRecognitionRate[i] += Utils.recognitionRate(usersConfusionMatrixes.get(i))/4;
	}
	
	

	
	public static void main(String[] args){
		BaseRubineShapes brd = new BaseRubineShapes();
		brd.UI();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}
	
	
}
