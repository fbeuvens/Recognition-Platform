package test.ud.letters.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;


import test.ud.letters.TraceWriter;
import test.ud.letters.Utils;

public class BaseOneDollarLetters {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;
	
	public BaseOneDollarLetters(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		points = new Hashtable<String, ArrayList <Point>>();
		tw = new TraceWriter("UDLettersOneDollar.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < 26; j++){
				for(int k = 1; k <= 10; k++){
					ArrayList<Point> p = new ArrayList<Point>();
					//ArrayList<Point> tmp = new ArrayList<Point>();
					//ArrayList<Point> resampledPoints;
					//List<Dot> dotSet = (new RecordParser("records\\"+i+"\\"+Utils.intToChar(j)+"\\"+Utils.intToChar(j)+k+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+i+"/"+Utils.intToChar(j)+"/"+Utils.intToChar(j)+k+".txt")).parse();
					Iterator<Dot> it = dotSet.iterator();
					int totalNbrPoints = 0;
					while(it.hasNext()){
						Dot dot = it.next();
						if(dot.valid)
							p.add(new Point(dot.posX, dot.posY));
							//totalNbrPoints++;
					}
					
					/*it = dotSet.iterator();
					int nbrPoints = 0;
					while(it.hasNext()){
						Dot dot = it.next();
						if(dot.valid){
							tmp.add(new Point(dot.posX, dot.posY));
							nbrPoints++;
						}
						else{
							int nbrInt = (int)(oneDollar.Recognizer.NumResamplePoints*((double)nbrPoints/totalNbrPoints));
							if(nbrInt == 0)
								nbrInt = 1;
							//System.out.println(""+i+Utils.intToChar(j)+k+": "+nbrInt);
							resampledPoints = oneDollar.Utils.Resample(tmp, nbrInt);
							//System.out.println(""+i+Utils.intToChar(j)+k+" resampled size = "+resampledPoints.size());
							for(int l = 0; l < resampledPoints.size(); l++)
								p.add(resampledPoints.get(l));
							tmp.clear();
							nbrPoints = 0;
						}
					}*/
					//System.out.println(tmp);
					//System.out.println(""+i+Utils.intToChar(j)+k+" size = "+p.size());
					points.put(""+i+Utils.intToChar(j)+k, p);
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
					confusionMatrixes.add(new int[26][26]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 5; user++){
					UD(user,numberTrainingExamples);
				}
				for(int knn = 0; knn < confusionMatrixes.size(); knn++){
					tw.println("numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					tw.println(Utils.matrixToString(confusionMatrixes.get(knn)));
					tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn)));
					double[] informations = Utils.informations(confusionMatrixes.get(knn));
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
			userConfusionMatrixes.add(new int[26][26]);
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Recognizer recognizer = new Recognizer(new File(""));
			for(int characterIndex = 0; characterIndex < 26; characterIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					recognizer.AddTemplate(Utils.intToChar(characterIndex)+"", points.get(""+user+Utils.intToChar(characterIndex)+teIndex));
					/*if(teIndex == firstTeIndex)
						firstTeIndexPassage = false;*/
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int characterIndex = 0; characterIndex < 26; characterIndex++){
					//boolean firstTeIndexPassage = true;
					//int i = 0;
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						//System.out.println("teIndex = "+teIndex);
						ArrayList<Point> toRecognize = points.get(""+user+Utils.intToChar(characterIndex)+teIndex);
						if(toRecognize.size() > 0){
							String[] foundChars =  recognizer.RecognizeForAllKnn(toRecognize,knn);
							for(int i = 0; i < foundChars.length; i++){
								char foundChar = foundChars[i].charAt(0);
								//char foundChar = recognizer.RecognizeKnn(toRecognize,knn).charAt(0);
								confusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
								userConfusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
							}
						}
						/*if(teIndex == tmp)
							firstTeIndexPassage = false;
						i++;*/
					}
					//System.out.println(i);
				}
			}
			System.out.println("nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}
	
	public void UD(int user){
		Recognizer recognizer = new Recognizer(new File(""));
		for(int i = 0; i < 10; i++)
			for(int j = 1; j <= 3; j++)
				recognizer.AddTemplate(Utils.intToChar(i)+"", points.get(""+user+Utils.intToChar(i)+j));
		
		for(int i = 0; i < 10; i++)
			for(int j = 4; j <= 10; j++){
				ArrayList<Point> toRecognize = points.get(""+user+Utils.intToChar(i)+j);
				if(toRecognize.size() > 0){
					char foundChar = recognizer.RecognizeKnn(toRecognize,1).charAt(0);
					//System.out.println(Utils.intToChar(i)+" => "+foundChar);
					confusionMatrixes.get(0)[i][Utils.charToInt(foundChar)]++;
					System.out.println(""+user+Utils.intToChar(i)+j);
				}
			}
	}
	
	
	public static void main(String[] args){
		BaseOneDollarLetters bodl = new BaseOneDollarLetters();
		bodl.UD();
	}
	
	
}
