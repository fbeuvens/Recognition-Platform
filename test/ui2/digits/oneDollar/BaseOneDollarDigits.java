package test.ui2.digits.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.levenshtein.Levenshtein;
import algorithm.oneDollar.Recognizer;



import test.ui2.digits.TraceWriter;
import test.ui2.digits.Utils;

public class BaseOneDollarDigits {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;
	
	public BaseOneDollarDigits(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		points = new Hashtable<String, ArrayList <Point>>();
		tw = new TraceWriter("UI2DigitsOneDollar.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 0; j < 10; j++){
				for(int k = 1; k <= 10; k++){
					ArrayList<Point> p = new ArrayList<Point>();
					//ArrayList<Point> tmp = new ArrayList<Point>();
					//ArrayList<Point> resampledPoints;
					//List<Dot> dotSet = (new RecordParser("records\\"+i+"\\"+Utils.intToDigit(j)+"\\"+Utils.intToDigit(j)+k+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+i+"/"+Utils.intToDigit(j)+"/"+Utils.intToDigit(j)+k+".txt")).parse();
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
							//System.out.println(""+i+Utils.intToDigit(j)+k+": "+nbrInt);
							resampledPoints = oneDollar.Utils.Resample(tmp, nbrInt);
							//System.out.println(""+i+Utils.intToDigit(j)+k+" resampled size = "+resampledPoints.size());
							for(int l = 0; l < resampledPoints.size(); l++)
								p.add(resampledPoints.get(l));
							tmp.clear();
							nbrPoints = 0;
						}
					}*/
					//System.out.println(tmp);
					//System.out.println(""+i+Utils.intToDigit(j)+k+" size = "+p.size());
					points.put(""+i+Utils.intToDigit(j)+k, p);
				}
			}
			System.out.println(i+" transformed");
		}
	}
	
	
	
	public void UI(){
		tw.println("-------------------------------------------------- User Independent -------------------------------------------------");
		tw.println();
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[10][10]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 10; user++){
					UI(user,numberTrainingExamples);
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
					for(int user = 0; user < 10; user++)
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
	
	public void UI(int user, int numberTrainingExamples){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[10][10]);
		for(int firstUserIndex = 1; firstUserIndex <= 10; firstUserIndex++){
			Recognizer recognizer = new Recognizer(new File(""));
			for(int digitIndex = 0; digitIndex < 10; digitIndex++){
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
					recognizer.AddTemplate(Utils.intToDigit(digitIndex)+"", points.get(""+otherUser+Utils.intToDigit(digitIndex)+"1"));

					if(otherUser == 10){
						otherUser = 0;
						mod = 1;
					}
				}
			}

			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int digitIndex = 0; digitIndex < 10; digitIndex++){
					for(int teIndex = 1; teIndex <= 10; teIndex++){
						String[] foundDigits =  recognizer.RecognizeForAllKnn(points.get(""+user+Utils.intToDigit(digitIndex)+teIndex),knn);
						for(int i = 0; i < foundDigits.length; i++){
							String foundDigit = foundDigits[i];
							confusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;
							userConfusionMatrixes.get(i)[digitIndex][Utils.digitToInt(foundDigit)]++;	
						}
					}
				}
				System.out.println("UI2oneDollar: nte="+numberTrainingExamples+"|user="+user+"|fui="+firstUserIndex+" recognized");
			}
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
	}
	
	
	
	
	
	public static void main(String[] args){
		BaseOneDollarDigits bodl = new BaseOneDollarDigits();
		bodl.UI();
	}
	
	
}
