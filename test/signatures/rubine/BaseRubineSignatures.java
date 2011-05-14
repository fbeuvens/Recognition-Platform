package test.signatures.rubine;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;
import algorithm.rubine.Classifier;
import algorithm.rubine.Gesture;
import algorithm.rubine.GestureClass;



import test.signatures.TraceWriter;
import test.signatures.Utils;

public class BaseRubineSignatures {

	private int[][] confusionMatrix;
	private double[] userRecognitionRate;	
	private TraceWriter tw;

	public BaseRubineSignatures(){
		confusionMatrix = new int[10][10];
		userRecognitionRate = new double[10];
		tw = new TraceWriter("SignaturesRubine.txt");
	}


	public void UD(){
		for(int nbUsers = 2; nbUsers <= 10; nbUsers++){
			tw.println("------------------------------------------- nbUsers="+nbUsers+" ---------------------------------------");
			tw.println();
			for(int numberTrainingExamples = 2; numberTrainingExamples <= 29; numberTrainingExamples++){
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				confusionMatrix = new int[10][10];
				userRecognitionRate = new double[10];

				UD(nbUsers,numberTrainingExamples);

				tw.println("nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+" :");
				tw.println();
				tw.println(Utils.matrixToString(confusionMatrix));
				tw.println(Utils.matrixToStringForLatex(confusionMatrix));
				double[] informations = Utils.informations(confusionMatrix);
				tw.println();
				tw.println("nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+" :");
				tw.println();
				for(int user = 0; user < nbUsers; user++)
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
		}
		tw.close();
	}

	public void UD(int nbUsers, int numberTrainingExamples){

		List<int[][]> usersConfusionMatrixes = new ArrayList<int[][]>();
		for(int user = 1; user <= nbUsers; user++)
			usersConfusionMatrixes.add(new int[10][10]);


		Classifier classifier = new Classifier();
		for(int user = 1; user <= nbUsers; user++){
			GestureClass gc = new GestureClass(user+"");
			for(int teIndex = 1; teIndex <= numberTrainingExamples; teIndex++){
				Gesture gest = new Gesture();
				List<Dot> dotSet = (new RecordParser("records/"+user+"/signature/signature"+teIndex+".txt")).parse();
				gest.addGesture(dotSet, false, false, false, false);
				gc.addExample(gest);
			}
			classifier.addClass(gc);
		}
		
		classifier.compile();


		for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
			for(int user = 1; user <= nbUsers; user++)
				for(int teIndex = numberTrainingExamples+1; teIndex <= 30; teIndex++){
					Gesture toRecognize = new Gesture();
					List<Dot> dotSet = (new RecordParser("records/"+user+"/signature/signature"+teIndex+".txt")).parse();
					toRecognize.addGesture(dotSet, false, false, false, false);

					String foundSig = classifier.classify(toRecognize).getName();
					confusionMatrix[user-1][Integer.parseInt(foundSig)-1]++;
					usersConfusionMatrixes.get(user-1)[user-1][Integer.parseInt(foundSig)-1]++;
				}
			System.out.println("nbUsers="+nbUsers+"|nte="+numberTrainingExamples+" recognized");
		}

		for(int user = 1; user <= nbUsers; user++)
				userRecognitionRate[user-1] = Utils.recognitionRate(usersConfusionMatrixes.get(user-1));
	}



	public static void main(String[] args){
		BaseRubineSignatures brs = new BaseRubineSignatures();
		brs.UD();
		//Utils.print(brl.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(brl.confusionMatrix));
	}


}
