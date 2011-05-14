package test.signatures.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;


import test.signatures.TraceWriter;
import test.signatures.Utils;


public class BaseOneDollarSignatures {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;

	public BaseOneDollarSignatures(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][29];
		points = new Hashtable<String, ArrayList <Point>>();
		tw = new TraceWriter("SignaturesOneDollar.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 10; i++){
				for(int k = 1; k <= 30; k++){
					ArrayList<Point> p = new ArrayList<Point>();
					List<Dot> dotSet = (new RecordParser("records/"+i+"/signature/"+"signature"+k+".txt")).parse();
					Iterator<Dot> it = dotSet.iterator();
					int totalNbrPoints = 0;
					while(it.hasNext()){
						Dot dot = it.next();
						if(dot.valid)
							p.add(new Point(dot.posX, dot.posY));
					}
					points.put(i+"signature"+k, p);
				}
			System.out.println(i+" transformed");
		}
	}

	public void UD(){
		for(int nbUsers = 2; nbUsers <= 10; nbUsers++){
			tw.println("------------------------------------------- nbUsers="+nbUsers+" ---------------------------------------");
			tw.println();
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 29; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[10][10]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][29];

				UD(nbUsers,numberTrainingExamples);

				for(int knn = 0; knn < confusionMatrixes.size(); knn++){
					tw.println("nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					tw.println(Utils.matrixToString(confusionMatrixes.get(knn)));
					tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn)));
					double[] informations = Utils.informations(confusionMatrixes.get(knn));
					tw.println();
					tw.println("nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					for(int user = 0; user < nbUsers; user++)
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

	public void UD(int nbUsers, int numberTrainingExamples){
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int user = 1; user <= nbUsers; user++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[10][10]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}

		Recognizer recognizer = new Recognizer(new File(""));
		for(int user = 1; user <= nbUsers; user++)
			for(int teIndex = 1; teIndex <= numberTrainingExamples; teIndex++)
				recognizer.AddTemplate(user+"", points.get(user+"signature"+teIndex));


		for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
			for(int user = 1; user <= nbUsers; user++)
				for(int teIndex = numberTrainingExamples+1; teIndex <= 30; teIndex++){
					ArrayList<Point> toRecognize = points.get(user+"signature"+teIndex);
					if(toRecognize.size() > 0){
						String[] foundSigs =  recognizer.RecognizeForAllKnn(toRecognize,knn);
						for(int i = 0; i < foundSigs.length; i++){
							String foundSig = foundSigs[i];
							confusionMatrixes.get(i)[user-1][Integer.parseInt(foundSig)-1]++;
							usersConfusionMatrixes.get(user-1).get(i)[user-1][Integer.parseInt(foundSig)-1]++;
						}
					}
				}
			System.out.println("nbUsers="+nbUsers+"|nte="+numberTrainingExamples+" recognized");
		}

		for(int user = 1; user <= nbUsers; user++)
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userRecognitionRates[user-1][knn] = Utils.recognitionRate(usersConfusionMatrixes.get(user-1).get(knn));
//		tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}







	public static void main(String[] args){
		BaseOneDollarSignatures bods = new BaseOneDollarSignatures();
		bods.UD();
	}


}
