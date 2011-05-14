package test.ui.letters.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;



import test.ui.letters.TraceWriter;
import test.ui.letters.Utils;

public class BaseOneDollarLetters {

	private List<int[][]> confusionMatrixes;
	private double[][] usersTrainingRecognitionRate;
	private double[][] usersRecognitionRate;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;
	
	public BaseOneDollarLetters(){
		confusionMatrixes = new ArrayList<int[][]>();
		points = new Hashtable<String, ArrayList <Point>>();
		tw = new TraceWriter("UILettersOneDollar.txt");
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
	
	public void UI(){
		tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
		tw.println();
			for(int numberTrainingExamples = 1; numberTrainingExamples <= 9; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[26][26]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				usersTrainingRecognitionRate = new double[10][9];
				usersRecognitionRate = new double[10][9];
				for(int user = 1; user <= 5; user++){
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
					for(int user = 0; user < 5; user++)
						tw.println("Recognition rate for training whith user"+(user+1)+" = "+usersTrainingRecognitionRate[user][knn]);
					tw.println();
					for(int user = 0; user < 5; user++)
						tw.println("Recognition rate for user"+(user+1)+" = "+usersRecognitionRate[user][knn]);
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
		List<int[][]> userTrainingConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userTrainingConfusionMatrixes.add(new int[26][26]);
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int i = 0; i < 5; i++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[26][26]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}
		
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
						for(int otherUser = 1; otherUser <= 5; otherUser++)
							if(otherUser != user){
								ArrayList<Point> toRecognize = points.get(""+otherUser+Utils.intToChar(characterIndex)+teIndex);
								if(toRecognize.size() > 0){
									String[] foundChars =  recognizer.RecognizeForAllKnn(toRecognize,knn);
									for(int i = 0; i < foundChars.length; i++){
										char foundChar = foundChars[i].charAt(0);
										confusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
										userTrainingConfusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
										usersConfusionMatrixes.get(otherUser-1).get(i)[characterIndex][Utils.charToInt(foundChar)]++;
									}
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
		for(int knn = 0; knn < numberTrainingExamples; knn++){
			usersTrainingRecognitionRate[user-1][knn] = Utils.recognitionRate(userTrainingConfusionMatrixes.get(knn));
			for(int i = 0; i < 5; i++)
				if(i != user-1)
					usersRecognitionRate[i][knn] += Utils.recognitionRate(usersConfusionMatrixes.get(i).get(knn))/4;
		}
	}
	
	public void oldUI(){
		for(int i = 1; i <= 15; i+=3){
			Recognizer recognizer = new Recognizer(new File(""));
			for(int j = 0; j < 26; j++){
				for(int k = 1; k <= 15; k++){
					if(!(k >= i && k <i+3)){
						for(int l = 1; l <= 1; l++){
						/*ArrayList<Point> points = new ArrayList<Point>();
						List<Dot> dotSet = (new RecordParser("records\\"+k+"\\"+Utils.intToChar(j)+"\\"+Utils.intToChar(j)+l+".txt")).parse();
						Iterator<Dot> it = dotSet.iterator();
						while(it.hasNext()){
							Dot dot = it.next();
							if(dot.valid)
								points.add(new Point(dot.posX, dot.posY));
						}
						System.out.println(""+k+Utils.intToChar(j)+l);
						recognizer.AddTemplate(""+Utils.intToChar(j), points);*/
						recognizer.AddTemplate(Utils.intToChar(j)+"", points.get(""+k+Utils.intToChar(j)+l));
						}
					}
				}
				System.out.println(""+i+Utils.intToChar(j));
			}
			
			for(int j = i; j < i+3; j++){
				for(int k = 0; k < 26; k++){
					for(int l = 1; l <= 10; l++){
						/*ArrayList<Point> toRecognize = new ArrayList<Point>();
						List<Dot> dotSet = (new RecordParser("records\\"+j+"\\"+Utils.intToChar(k)+"\\"+Utils.intToChar(k)+l+".txt")).parse();
						Iterator<Dot> it = dotSet.iterator();
						while(it.hasNext()){
							Dot dot = it.next();
							if(dot.valid)
								toRecognize.add(new Point(dot.posX, dot.posY));
						}*/
						ArrayList<Point> toRecognize = points.get(""+j+Utils.intToChar(k)+l);
						if(toRecognize.size() > 0){
							char foundChar = recognizer.Recognize(toRecognize/*toRecognize*/).name.charAt(0);
							confusionMatrixes.get(0)[k][Utils.charToInt(foundChar)]++;
							System.out.println(""+j+Utils.intToChar(k)+l);
						}
						
					}
				}
			}
			System.out.println("check :"+i);
		}
	}
	
	
	
	
	public static void main(String[] args){
		BaseOneDollarLetters bodl = new BaseOneDollarLetters();
		bodl.UI();
	}
	
	
}
