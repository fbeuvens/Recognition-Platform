package test.signatures;

public class Utils {
	
	
	public static String matrixToString(int[][] confusionMatrix){
		String toReturn = "";
		for(int i = 0; i < 10; i++){
			toReturn += (i+1)+": ";
			for(int j = 0; j < 10; j++){
				toReturn += confusionMatrix[i][j]+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public static String matrixToStringForLatex(int[][] confusionMatrix){
		String toReturn = "\\begin{tabular}{c|cccccccccc}\n";
		toReturn += "& 1 & 2 & 3 & 4 & 5 & 6 & 7 & 8 & 9 & 10\\\\\n";
		toReturn += "\\hline\n";
		for(int i = 0; i < 10; i++){
			toReturn += (i+1);
			for(int j = 0; j < 10; j++){
				toReturn += " & "+confusionMatrix[i][j];
			}
			toReturn += "\\\\\n";
		}
		toReturn += "\\end{tabular}\n";
		return toReturn;
	}
	
	public static double recognitionRate(int[][] confusionMatrix){
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(i == j)
					goodClass += confusionMatrix[i][j];
				else
					badClass += confusionMatrix[i][j];
			}
		}
		return goodClass*100/(goodClass + badClass);
	}
	
	public static double[] informations(int[][] confusionMatrix){
		double[] toReturn = new double[4];
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(i == j)
					goodClass += confusionMatrix[i][j];
				else
					badClass += confusionMatrix[i][j];
			}
		}
		toReturn[0] = goodClass;
		toReturn[1] = badClass;
		toReturn[2] = goodClass+badClass;
		toReturn[3] = goodClass*100/(goodClass + badClass);
		return toReturn;
	}

}
