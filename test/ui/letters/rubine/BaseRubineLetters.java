package test.ui.letters.rubine;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;



import test.ui.letters.TraceWriter;
import test.ui.letters.Utils;

public class BaseRubineLetters {

	private int[][] confusionMatrix;
	private double[] usersTrainingRecognitionRate;
	private double[] usersRecognitionRate;
	private TraceWriter tw;
	
	public BaseRubineLetters(){
		confusionMatrix = new int[26][26];
		tw = new TraceWriter("UILettersRubine.txt");
	}
	
	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			confusionMatrix = new int[26][26];
			usersTrainingRecognitionRate = new double[10];
			usersRecognitionRate = new double[10];
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
		int[][] userTrainingConfusionMatrix = new int[26][26];
		List<int[][]> usersConfusionMatrixes = new ArrayList<int[][]>();
		for(int i = 0; i < 5; i++)
			usersConfusionMatrixes.add(new int[26][26]);
		
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Classifier classifier = new Classifier();
			for(int characterIndex = 0; characterIndex < 26; characterIndex++){
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
			
		
			for(int characterIndex = 0; characterIndex < 26; characterIndex++){
				int tmp = (firstTeIndex+numberTrainingExamples)%10;
				if(tmp == 0) tmp = 10;
				for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
					for(int otherUser = 1; otherUser <= 5; otherUser++)
						if(otherUser != user){
							Gesture toRecognize = new Gesture();
							//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(characterIndex)+"\\"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
							List<Dot> dotSet = (new RecordParser("records/"+otherUser+"/"+Utils.intToChar(characterIndex)+"/"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
							toRecognize.addGesture(dotSet, false, false, false, false);
							
							char foundChar = classifier.classify(toRecognize).getName().charAt(0);
							confusionMatrix[characterIndex][Utils.charToInt(foundChar)]++;
							userTrainingConfusionMatrix[characterIndex][Utils.charToInt(foundChar)]++;
							usersConfusionMatrixes.get(otherUser-1)[characterIndex][Utils.charToInt(foundChar)]++;
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
	
	public void UIRubine(){
		for(int i = 1; i <= 15; i+=3){
			Classifier classifier = new Classifier();
			for(int j = 0; j < 26; j++){
				GestureClass gc = new GestureClass(Utils.intToChar(j)+"");
				for(int k = 1; k <= 15; k++){
					if(!(k >= i && k <i+3)){
						for(int l = 1; l <= 1; l++){
						Gesture gest = new Gesture();
						List<Dot> dotSet = (new RecordParser("records\\"+k+"\\"+Utils.intToChar(j)+"\\"+Utils.intToChar(j)+l+".txt")).parse();
						Iterator<Dot> it = dotSet.iterator();
						while(it.hasNext()){
							Dot dot = it.next();
							if(dot.valid)
								gest.addPoint(dot.posX, dot.posY, dot.time_millis);
						}
						gc.addExample(gest);
						}
					}
				}
				classifier.addClass(gc);
				System.out.println(i+gc.getName());
			}
			classifier.compile();
			for(int j = i; j < i+3; j++){
				for(int k = 0; k < 26; k++){
					for(int l = 1; l <= 10; l++){
						Gesture toRecognize = new Gesture();
						List<Dot> dotSet = (new RecordParser("records\\"+j+"\\"+Utils.intToChar(k)+"\\"+Utils.intToChar(k)+l+".txt")).parse();
						Iterator<Dot> it = dotSet.iterator();
						while(it.hasNext()){
							Dot dot = it.next();
							if(dot.valid)
								toRecognize.addPoint(dot.posX, dot.posY, dot.time_millis);
						}
						char foundChar = classifier.classify(toRecognize).getName().charAt(0);
						confusionMatrix[k][Utils.charToInt(foundChar)]++;
					}
				}
			}
			System.out.println("check :"+i);
		}
	}
	
	

	
	public static void main(String[] args){
		BaseRubineLetters brl = new BaseRubineLetters();
		brl.UI();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}
	
	
}
