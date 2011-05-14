package test.ui.shapes.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.oneDollar.Recognizer;


import test.ui.shapes.TraceWriter;
import test.ui.shapes.Utils;

public class BaseOneDollarShapes {

	private List<int[][]> confusionMatrixes;
	private double[][] usersTrainingRecognitionRate;
	private double[][] usersRecognitionRate;
	private Hashtable<String,  ArrayList <Point>> points;
	private TraceWriter tw;
	
	public BaseOneDollarShapes(){
		confusionMatrixes = new ArrayList<int[][]>();
		points = new Hashtable<String, ArrayList <Point>>();
		tw = new TraceWriter("UIShapesOneDollar.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < 8; j++){
				for(int k = 1; k <= 10; k++){
					ArrayList<Point> p = new ArrayList<Point>();
					//ArrayList<Point> tmp = new ArrayList<Point>();
					//ArrayList<Point> resampledPoints;
					//List<Dot> dotSet = (new RecordParser("records\\"+i+"\\"+Utils.intToShape(j)+"\\"+Utils.intToShape(j)+k+".txt")).parse();
					List<Dot> dotSet = (new RecordParser("records/"+i+"/"+Utils.intToShape(j)+"/"+Utils.intToShape(j)+k+".txt")).parse();
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
							//System.out.println(""+i+Utils.intToShape(j)+k+": "+nbrInt);
							resampledPoints = oneDollar.Utils.Resample(tmp, nbrInt);
							//System.out.println(""+i+Utils.intToShape(j)+k+" resampled size = "+resampledPoints.size());
							for(int l = 0; l < resampledPoints.size(); l++)
								p.add(resampledPoints.get(l));
							tmp.clear();
							nbrPoints = 0;
						}
					}*/
					//System.out.println(tmp);
					//System.out.println(""+i+Utils.intToShape(j)+k+" size = "+p.size());
					points.put(""+i+Utils.intToShape(j)+k, p);
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
					confusionMatrixes.add(new int[8][8]);
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
			userTrainingConfusionMatrixes.add(new int[8][8]);
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int i = 0; i < 5; i++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[8][8]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}
		
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			Recognizer recognizer = new Recognizer(new File(""));
			for(int shapeIndex = 0; shapeIndex < 8; shapeIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					recognizer.AddTemplate(Utils.intToShape(shapeIndex)+"", points.get(""+user+Utils.intToShape(shapeIndex)+teIndex));
					/*if(teIndex == firstTeIndex)
						firstTeIndexPassage = false;*/
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			for(int knn = 1; knn <= numberTrainingExamples; knn++){
				for(int shapeIndex = 0; shapeIndex < 8; shapeIndex++){
					//boolean firstTeIndexPassage = true;
					//int i = 0;
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						//System.out.println("teIndex = "+teIndex);
						for(int otherUser = 1; otherUser <= 5; otherUser++)
							if(otherUser != user){
								ArrayList<Point> toRecognize = points.get(""+otherUser+Utils.intToShape(shapeIndex)+teIndex);
								if(toRecognize.size() > 0){
									if(toRecognize.size() > 0){
										String[] foundShapes =  recognizer.RecognizeForAllKnn(toRecognize,knn);
										for(int i = 0; i < foundShapes.length; i++){
											String foundShape = foundShapes[i];
											confusionMatrixes.get(i)[shapeIndex][Utils.shapeToInt(foundShape)]++;
											userTrainingConfusionMatrixes.get(i)[shapeIndex][Utils.shapeToInt(foundShape)]++;
											usersConfusionMatrixes.get(otherUser-1).get(i)[shapeIndex][Utils.shapeToInt(foundShape)]++;
										}
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
			System.out.println("shapes 1$: nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++){
			usersTrainingRecognitionRate[user-1][knn] = Utils.recognitionRate(userTrainingConfusionMatrixes.get(knn));
			for(int i = 0; i < 5; i++)
				if(i != user-1)
					usersRecognitionRate[i][knn] += Utils.recognitionRate(usersConfusionMatrixes.get(i).get(knn))/4;
		}
	}
	
	
	
	public static void main(String[] args){
		BaseOneDollarShapes bodl = new BaseOneDollarShapes();
		bodl.UI();
	}
	
	
}
