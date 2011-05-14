package test.ud.letters.rubine;

import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.ud.letters.TraceWriter;
import test.ud.letters.Utils;

public class BaseRubineLetters {

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;
	
	public BaseRubineLetters(){
		confusionMatrix = new int[26][26];
		userRecognitionRate = new double[5];
		tw = new TraceWriter("UDLettersRubine.txt");
	}
	
	
	
	public void UD(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			confusionMatrix = new int[26][26];
			userRecognitionRate = new double[10];
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
		int[][] userConfusionMatrix = new int[26][26];
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Classifier classifier = new Classifier();
			for(int characterIndex = 0; characterIndex < 10; characterIndex++){
				GestureClass gc = new GestureClass(Utils.intToChar(characterIndex)+"");
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					Gesture gest = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(characterIndex)+"\\"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToChar(characterIndex)+"/"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
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
			
		
			for(int characterIndex = 0; characterIndex < 10; characterIndex++){
				int tmp = (firstTeIndex+numberTrainingExamples)%10;
				if(tmp == 0) tmp = 10;
				for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
					Gesture toRecognize = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(characterIndex)+"\\"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToChar(characterIndex)+"/"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, false, false, false, false);
					
					char foundChar = classifier.classify(toRecognize).getName().charAt(0);
					confusionMatrix[characterIndex][Utils.charToInt(foundChar)]++;
					userConfusionMatrix[characterIndex][Utils.charToInt(foundChar)]++;
				}
			}
			
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		
		userRecognitionRate[user-1] = Utils.recognitionRate(userConfusionMatrix);
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}
	
	public void UD(int user){
		Classifier classifier = new Classifier();
		for(int i = 0; i < 26; i++){
			GestureClass gc = new GestureClass(Utils.intToChar(i)+"");
			for(int j = 1; j <= 9; j++){
				Gesture gest = new Gesture();
				List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(i)+"\\"+Utils.intToChar(i)+j+".txt")).parse();
				Iterator<Dot> it = dotSet.iterator();
				/*while(it.hasNext()){
					Dot dot = it.next();
					if(dot.valid)
						gest.addPoint(dot.posX, dot.posY, dot.time_millis);
				}*/
				gest.addGesture(dotSet, false, false, false, false);
				gc.addExample(gest);
				
			}
			classifier.addClass(gc);
		}
		
		classifier.compile();
		
		for(int i = 0; i < 26; i++){
			for(int j = 10; j <= 10; j++){
				Gesture toRecognize = new Gesture();
				List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(i)+"\\"+Utils.intToChar(i)+j+".txt")).parse();
				Iterator<Dot> it = dotSet.iterator();
				while(it.hasNext()){
					Dot dot = it.next();
					if(dot.valid)
						toRecognize.addPoint(dot.posX, dot.posY, dot.time_millis);
				}
				char foundChar = classifier.classify(toRecognize).getName().charAt(0);
				confusionMatrix[i][Utils.charToInt(foundChar)]++;
			}
		}
	}
	

	
	public static void main(String[] args){
		BaseRubineLetters brl = new BaseRubineLetters();
		brl.UD();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}
	
	
}
