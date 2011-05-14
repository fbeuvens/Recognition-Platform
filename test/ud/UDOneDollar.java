package test.ud;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;


import test.TraceWriter;
import test.Utils;

public class UDOneDollar {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;
	private int category;
	
	public UDOneDollar(int category){
		this.category = category;
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		points = new Hashtable<String, ArrayList <Point>>();
		switch(category){
		case 1: tw = new TraceWriter("digits/ud/UDDigitsOneDollar.txt");break;
		case 2: tw = new TraceWriter("gestures/ud/UDGesturesOneDollar.txt");break;
		case 3: tw = new TraceWriter("letters/ud/UDLettersOneDollar.txt");break;
		case 4: tw = new TraceWriter("shapes/ud/UDShapesOneDollar.txt");break;
		}
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < Utils.maxGestureIndex(category); j++){
				for(int k = 1; k <= 10; k++){
					ArrayList<Point> p = new ArrayList<Point>();
					List<Dot> dotSet = (new RecordParser("records/"+i+"/"+Utils.intToGesture(j,category)+"/"+Utils.intToGesture(j,category)+k+".txt")).parse();
					Iterator<Dot> it = dotSet.iterator();
					while(it.hasNext()){
						Dot dot = it.next();
						if(dot.valid)
							p.add(new Point(dot.posX, dot.posY));
					}
					points.put(""+i+Utils.intToGesture(j,category)+k, p);
				}
			}
			System.out.println(i+" transformed");
		}
	}
	
	
	
	public void UD(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 5; user++){
					UD(user,numberTrainingExamples);
				}
				for(int knn = 0; knn < confusionMatrixes.size(); knn++){
					tw.println("numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					tw.println(Utils.matrixToString(confusionMatrixes.get(knn),category));
					tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn),category));
					double[] informations = Utils.informations(confusionMatrixes.get(knn),category);
					tw.println();
					tw.println("numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					for(int user = 0; user < 5; user++)
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
		tw.close();
	}
	
	public void UD(int user, int numberTrainingExamples){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[Utils.maxGestureIndex(category)][Utils.maxGestureIndex(category)]);
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Recognizer recognizer = new Recognizer(new File(""));
			for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples; teIndex++){
					recognizer.AddTemplate(Utils.intToGesture(gestureIndex,category)+"", points.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex));
					
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int gestureIndex = 0; gestureIndex < Utils.maxGestureIndex(category); gestureIndex++){
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						ArrayList<Point> toRecognize = points.get(""+user+Utils.intToGesture(gestureIndex,category)+teIndex);
						if(toRecognize.size() > 0){
							String[] foundGestures =  recognizer.RecognizeForAllKnn(toRecognize,knn);
							for(int i = 0; i < foundGestures.length; i++){
								String foundGesture = foundGestures[i];
								confusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
								userConfusionMatrixes.get(i)[gestureIndex][Utils.gestureToInt(foundGesture,category)]++;
							}
						}
					}
				}
			}
			System.out.println("1$: nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn),category);
	}
	
	
	
	public static void main(String[] args){
		int category = Integer.parseInt(args[0]);
		UDOneDollar udod = new UDOneDollar(category);
		udod.UD();
	}
	
	
}
