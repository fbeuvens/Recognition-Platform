package test.ud.letters.stochasticLevenshtein;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.ud.letters.TraceWriter;
import test.ud.letters.Utils;

public class BaseStochasticLevenshteinLetters3 {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	
	public BaseStochasticLevenshteinLetters3(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][9];
		strings = new Hashtable<String, String>();
		//tw = new TraceWriter("StochasticLevenshteinLetters.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 0; j < 26; j++)
				for(int k = 1; k <= 10; k++){
					//strings.put(""+i+Utils.intToChar(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToChar(j)+"\\"+Utils.intToChar(j)+k+".txt").parse()));
					strings.put(""+i+Utils.intToChar(j)+k, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/"+Utils.intToChar(j)+"/"+Utils.intToChar(j)+k+".txt").parse()));
				}
			System.out.println(i+" transformed");
		}
	}
	
	
	
	public void UD(){
		for(int normalisation = 0; normalisation <= 0; normalisation++){
			tw = new TraceWriter("UDLettersStochasticLevenshtein3Norm"+normalisation+".txt");
			tw.println("-------------------------------------------------- User Dependent -------------------------------------------------");
			tw.println();
			for(int numberTrainingExamples = 3; numberTrainingExamples <= 3; numberTrainingExamples++){
				confusionMatrixes.clear();
				for(int knn = 0; knn < numberTrainingExamples; knn++)
					confusionMatrixes.add(new int[26][26]);
				tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
				tw.println();
				userRecognitionRates = new double[10][9];
				for(int user = 1; user <= 5; user++){
					UD(user,numberTrainingExamples,normalisation);
				}
				for(int knn = 0; knn < confusionMatrixes.size(); knn++){
					tw.println("normalisation="+normalisation+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
					tw.println();
					tw.println(Utils.matrixToString(confusionMatrixes.get(knn)));
					tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn)));
					double[] informations = Utils.informations(confusionMatrixes.get(knn));
					tw.println();
					tw.println("normalisation="+normalisation+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
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
		
	}
	
	public void UD(int user, int numberTrainingExamples, int normalisation){
		List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userConfusionMatrixes.add(new int[26][26]);
		for(int firstTeIndex = 1; firstTeIndex <= 10; firstTeIndex++){
			String[] alphabet = {"0","1","2","3","4","5","6","7"};
			ConditionalRecognizer cr = new ConditionalRecognizer(alphabet);
			for(int characterIndex = 0; characterIndex < 26; characterIndex++){
				int mod = 0;
				for(int teIndex = firstTeIndex; mod*10+teIndex < firstTeIndex+numberTrainingExamples /*&& (teIndex != firstTeIndex || firstTeIndexPassage)*/; teIndex++){
					cr.addTemplate(Utils.intToChar(characterIndex)+"", strings.get(""+user+Utils.intToChar(characterIndex)+teIndex));
					/*if(teIndex == firstTeIndex)
						firstTeIndexPassage = false;*/
					if(teIndex == 10){
						teIndex = 0;
						mod = 1;
					}
				}
			}

			cr.compile(normalisation);
			
			for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
				for(int characterIndex = 0; characterIndex < 26; characterIndex++){
					//boolean firstTeIndexPassage = true;
					//int i = 0;
					int tmp = (firstTeIndex+numberTrainingExamples)%10;
					if(tmp == 0) tmp = 10;
					for(int teIndex = tmp; teIndex != firstTeIndex ; teIndex = (teIndex%10)+1){
						//System.out.println("teIndex = "+teIndex);
						String[] foundChars =  cr.recognizeForAllKnn(strings.get(""+user+Utils.intToChar(characterIndex)+teIndex),normalisation,knn);
						for(int i = 0; i < foundChars.length; i++){
							char foundChar = foundChars[i].charAt(0);
							confusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
							userConfusionMatrixes.get(i)[characterIndex][Utils.charToInt(foundChar)]++;
						}
						/*if(teIndex == tmp)
							firstTeIndexPassage = false;
						i++;*/
						//System.out.println(""+firstTeIndex+Utils.intToChar(characterIndex)+teIndex+" recognized");
					}
					//System.out.println(i);
					System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+"|char="+Utils.intToChar(characterIndex)+" recognized");
				}
			}
			//System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+"|ftei="+firstTeIndex+" recognized");
		}
		for(int knn = 0; knn < numberTrainingExamples; knn++)
			userRecognitionRates[user-1][knn] = Utils.recognitionRate(userConfusionMatrixes.get(knn));
		//tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}
	
	
	public static void main(String[] args){
		BaseStochasticLevenshteinLetters3 bsll = new BaseStochasticLevenshteinLetters3();
		bsll.UD();
		//Utils.print(bsll.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(bsll.confusionMatrix));
	}
	
}
