package test.ud.letters;

public class Utils {
	
	public static int charToInt(char c){
		return ((int)c)-97;
	}
	
	public static char intToChar(int i){
		return (char)(i+97);
	}
	
	public static String matrixToString(int[][] confusionMatrix){
		String toReturn = "";
		for(int i = 0; i < 26; i++){
			toReturn += Utils.intToChar(i)+": ";
			for(int j = 0; j < 26; j++){
				toReturn += confusionMatrix[i][j]+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public static String matrixToStringForLatex(int[][] confusionMatrix){
		String toReturn = "\\begin{tabular}{c|cccccccccccccccccccccccccc}\n";
		toReturn += "& a & b & c & d & e & f & g & h & i & j & k & l & m & n & o & p & q & r & s & t & u & v & w & x & y & z\\\\\n";
		toReturn += "\\hline\n";
		for(int i = 0; i < 26; i++){
			toReturn += Utils.intToChar(i);
			for(int j = 0; j < 26; j++){
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
		
		for(int i = 0; i < 26; i++){
			for(int j = 0; j < 26; j++){
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
		
		for(int i = 0; i < 26; i++){
			for(int j = 0; j < 26; j++){
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
