package test.signatures.levenshtein;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.levenshtein.Levenshtein;
import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.signatures.TraceWriter;
import test.signatures.Utils;


public class BaseLevenshteinSignatures {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;
	
	public BaseLevenshteinSignatures(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][29];
		strings = new Hashtable<String, String>();
		//tw = new TraceWriter("SignaturesLevenshtein.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}
	
	private void transform(){
		for(int i = 1; i <= 10; i++){
			for(int j = 1; j <= 30; j++)	
					//strings.put(""+i+Utils.intToDigit(j)+k, levenshtein.Utils.transform(new RecordParser("records\\"+i+"\\"+Utils.intToDigit(j)+"\\"+Utils.intToDigit(j)+k+".txt").parse()));
					strings.put(""+i+"signature"+j, algorithm.levenshtein.Utils.transform(new RecordParser("records/"+i+"/signature/signature"+j+".txt").parse()));
			System.out.println(i+" transformed");
		}
	}
	
	
	
	public void UD(){
		for(int normalisation = 0; normalisation <= 0; normalisation++){
			tw = new TraceWriter("SignaturesLevenshteinNorm"+normalisation+".txt");
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

					UD(nbUsers,numberTrainingExamples,normalisation);

					for(int knn = 0; knn < confusionMatrixes.size(); knn++){
						tw.println("normalisation="+normalisation+"     nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
						tw.println();
						tw.println(Utils.matrixToString(confusionMatrixes.get(knn)));
						tw.println(Utils.matrixToStringForLatex(confusionMatrixes.get(knn)));
						double[] informations = Utils.informations(confusionMatrixes.get(knn));
						tw.println();
						tw.println("normalisation="+normalisation+"     nbUsers="+nbUsers+"    numberTrainingExamples="+numberTrainingExamples+"    knn="+(knn+1)+" :");
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
			}
			tw.close();
		}
	}

	public void UD(int nbUsers, int numberTrainingExamples, int normalisation){
		List<List<int[][]>> usersConfusionMatrixes = new ArrayList<List<int[][]>>();
		for(int user = 1; user <= nbUsers; user++){
			List<int[][]> userConfusionMatrixes = new ArrayList<int[][]>();
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userConfusionMatrixes.add(new int[10][10]);
			usersConfusionMatrixes.add(userConfusionMatrixes);
		}

		Levenshtein lev = new Levenshtein();
		for(int user = 1; user <= nbUsers; user++)
			for(int teIndex = 1; teIndex <= numberTrainingExamples; teIndex++)
				lev.addTemplate(user+"", strings.get(""+user+"signature"+teIndex));


		for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
			for(int user = 1; user <= nbUsers; user++)
				for(int teIndex = numberTrainingExamples+1; teIndex <= 30; teIndex++){
					String[] foundSigs =  lev.recognizeForAllKnn(strings.get(user+"signature"+teIndex),normalisation,knn);
					for(int i = 0; i < foundSigs.length; i++){
						String foundSig = foundSigs[i];
						confusionMatrixes.get(i)[user-1][Integer.parseInt(foundSig)-1]++;
						usersConfusionMatrixes.get(user-1).get(i)[user-1][Integer.parseInt(foundSig)-1]++;
					}
				}
			System.out.println("norm="+normalisation+"|nbUsers="+nbUsers+"|nte="+numberTrainingExamples+" recognized");
		}

		for(int user = 1; user <= nbUsers; user++)
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userRecognitionRates[user-1][knn] = Utils.recognitionRate(usersConfusionMatrixes.get(user-1).get(knn));
//		tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}
	
	
	
	public static void main(String[] args){
		BaseLevenshteinSignatures bls = new BaseLevenshteinSignatures();
		bls.UD();
		/*System.out.println(Utils.matrixToString(bll.confusionMatrix));
		System.out.println(Utils.matrixToStringForLatex(bll.confusionMatrix));
		System.out.println("Recognition rate = "+Utils.recognitionRate(bll.confusionMatrix));*/
	}
	
	
}
