package test.ui2.letters.rubine;

import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;


import test.ui2.letters.TraceWriter;
import test.ui2.letters.Utils;

public class BaseRubineLetters {

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;

	public BaseRubineLetters(){
		confusionMatrix = new int[26][26];
		userRecognitionRate = new double[10];
		tw = new TraceWriter("UI2LettersRubine.txt");
	}



	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
		for(int numberTrainingExamples = 2; numberTrainingExamples <= 9; numberTrainingExamples++){
			tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
			tw.println();
			userRecognitionRate = new double[10];
			confusionMatrix = new int[26][26];
			for(int user = 1; user <= 10; user++){
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
		int[][] userConfusionMatrix = new int[26][26];
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Classifier classifier = new Classifier();
			for(int letterIndex = 0; letterIndex < 26; letterIndex++){
				GestureClass gc = new GestureClass(Utils.intToChar(letterIndex)+"");
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
					List<Dot> dotSet = (new RecordParser("records/"+otherUser+"/"+Utils.intToChar(letterIndex)+"/"+Utils.intToChar(letterIndex)+"1.txt")).parse();
					gest.addGesture(dotSet, false, false, false, false);
					gc.addExample(gest);
					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
				classifier.addClass(gc);
			}
			classifier.compile();


			for(int letterIndex = 0; letterIndex < 26; letterIndex++){
				for(int teIndex = 1; teIndex <= 10; teIndex++){
					Gesture toRecognize = new Gesture();
					//List<Dot> dotSet = (new RecordParser("records\\"+user+"\\"+Utils.intToChar(characterIndex)+"\\"+Utils.intToChar(characterIndex)+teIndex+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/"+Utils.intToChar(letterIndex)+"/"+Utils.intToChar(letterIndex)+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, false, false, false, false);

					char foundChar = classifier.classify(toRecognize).getName().charAt(0);
					confusionMatrix[letterIndex][Utils.charToInt(foundChar)]++;
					userConfusionMatrix[letterIndex][Utils.charToInt(foundChar)]++;
				}
			}

			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
		}

		userRecognitionRate[user-1] = Utils.recognitionRate(userConfusionMatrix);
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}




	public static void main(String[] args){
		BaseRubineLetters brd = new BaseRubineLetters();
		brd.UI();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}


}
