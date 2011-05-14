package test.signatures.stochasticLevenshtein;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import data.RecordParser;

import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import test.signatures.TraceWriter;
import test.signatures.Utils;

public class BaseStochasticLevenshteinSignatures {

	private List<int[][]> confusionMatrixes;
	private double[][] userRecognitionRates;
	private Hashtable<String, String> strings;
	private TraceWriter tw;

	public BaseStochasticLevenshteinSignatures(){
		confusionMatrixes = new ArrayList<int[][]>();
		userRecognitionRates = new double[10][29];
		strings = new Hashtable<String, String>();
		//tw = new TraceWriter("StochasticLevenshteinDigits.txt");
		System.out.println("transform begin");
		transform();
		System.out.println("transform end");
	}

	private void transform(){
		for(int i = 1; i <= 5; i++){
			for(int j = 1; j <= 30; j++)	
				strings.put(""+i+"signature"+j, algorithm.levenshtein.Utils.transformWithProcessing(new RecordParser("records/"+i+"/signature/signature"+j+".txt").parse(),false,false,false,false));
			System.out.println(i+" transformed");
			}
		System.out.println(strings.get("1signature1"));
	}




	public void UD(){
		for(int normalisation = 0; normalisation <= 0; normalisation++){
			for(int nbUsers = 2; nbUsers <= 2; nbUsers++){
				for(int numberTrainingExamples = 4; numberTrainingExamples <= 4; numberTrainingExamples++){
					tw = new TraceWriter("SignaturesStochasticLevenshteinNorm"+normalisation+"Nte"+numberTrainingExamples+".txt");
					confusionMatrixes.clear();
					for(int knn = 0; knn < numberTrainingExamples; knn++)
						confusionMatrixes.add(new int[10][10]);
					tw.println("------------------------------------------- numberTrainingExamples="+numberTrainingExamples+" ---------------------------------------");
					tw.println();
					userRecognitionRates = new double[10][29];

					UD(nbUsers,numberTrainingExamples,normalisation);

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
					tw.close();
				}
			}
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

		String[] alphabet = {"0","1","2","3","4","5","6","7"};
		ConditionalRecognizer cr = new ConditionalRecognizer(alphabet);
		for(int user = 1; user <= nbUsers; user++)
			for(int teIndex = 1; teIndex <= numberTrainingExamples; teIndex++)
				cr.addTemplate(user+"", strings.get(""+user+"signature"+teIndex));



		cr.compile(normalisation);

		for(int knn = numberTrainingExamples; knn <= numberTrainingExamples; knn++){
			for(int user = 1; user <= nbUsers; user++)
				for(int teIndex = numberTrainingExamples+1; teIndex <= 30; teIndex++){
					String[] foundSigs =  cr.recognizeForAllKnn(strings.get(user+"signature"+teIndex),normalisation,knn);
					for(int i = 0; i < foundSigs.length; i++){
						String foundSig = foundSigs[i];
						confusionMatrixes.get(i)[user-1][Integer.parseInt(foundSig)-1]++;
						usersConfusionMatrixes.get(user-1).get(i)[user-1][Integer.parseInt(foundSig)-1]++;
					}
				}
			System.out.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|nbUsers="+nbUsers+" recognized");
		}

		for(int user = 1; user <= nbUsers; user++)
			for(int knn = 0; knn < numberTrainingExamples; knn++)
				userRecognitionRates[user-1][knn] = Utils.recognitionRate(usersConfusionMatrixes.get(user-1).get(knn));
//		tw.println("norm="+normalisation+"|nte="+numberTrainingExamples+"|user="+user+": recognition rate = "+Utils.recognitionRate(userConfusionMatrix));
	}


	public static void main(String[] args){
		BaseStochasticLevenshteinSignatures bsll = new BaseStochasticLevenshteinSignatures();
		bsll.UD();
		//Utils.print(bsll.confusionMatrix);
		//System.out.println("Recognition rate = "+Utils.recognitionRate(bsll.confusionMatrix));
	}
}
