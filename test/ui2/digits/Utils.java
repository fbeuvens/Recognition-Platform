package test.ui2.digits;

public class Utils {
	
	public static int digitToInt(String digit){
		if(digit.equals("zero"))
			return 0;
		else if(digit.equals("one"))
				return 1;
		else if(digit.equals("two"))
			return 2;
		else if(digit.equals("three"))
			return 3;
		else if(digit.equals("four"))
			return 4;
		else if(digit.equals("five"))
			return 5;
		else if(digit.equals("six"))
			return 6;
		else if(digit.equals("seven"))
			return 7;
		else if(digit.equals("eight"))
			return 8;
		else if(digit.equals("nine"))
			return 9;
		else 
			return -1;
	}
	
	public static String intToDigit(int i){
		switch(i){
			case(0): return "zero";	
			case(1): return "one";
			case(2): return "two";
			case(3): return "three";
			case(4): return "four";
			case(5): return "five";
			case(6): return "six";
			case(7): return "seven";
			case(8): return "eight";
			case(9): return "nine";
			default: return "";
		}
	}
	
	public static String matrixToString(int[][] confusionMatrix){
		String toReturn = "";
		for(int i = 0; i < 10; i++){
			toReturn += i+": ";
			for(int j = 0; j < 10; j++){
				toReturn += confusionMatrix[i][j]+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public static String matrixToStringForLatex(int[][] confusionMatrix){
		String toReturn = "\\begin{tabular}{c|cccccccccc}\n";
		toReturn += "& 0 & 1 & 2 & 3 & 4 & 5 & 6 & 7 & 8 & 9\\\\\n";
		toReturn += "\\hline\n";
		for(int i = 0; i < 10; i++){
			toReturn += i;
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
