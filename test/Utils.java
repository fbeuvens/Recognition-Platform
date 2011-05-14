package test;


public class Utils {
	
	private static int digitToInt(String digit){
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
	
	private static String intToDigit(int i){
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
	
	private static String digitMatrixToString(int[][] confusionMatrix){
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
	
	private static String digitMatrixToStringForLatex(int[][] confusionMatrix){
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
	
	private static double digitRecognitionRate(int[][] confusionMatrix){
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
	
	private static double[] digitInformations(int[][] confusionMatrix){
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
	
	/**************************************************************************************/
	
	private static int gestToInt(String gesture){
		if(gesture.equals("upArrow"))
			return 0;
		else if(gesture.equals("rightUpArrow"))
				return 1;
		else if(gesture.equals("rightArrow"))
			return 2;
		else if(gesture.equals("rightDownArrow"))
			return 3;
		else if(gesture.equals("downArrow"))
			return 4;
		else if(gesture.equals("leftDownArrow"))
			return 5;
		else if(gesture.equals("leftArrow"))
			return 6;
		else if(gesture.equals("leftUpArrow"))
			return 7;
		else if(gesture.equals("upArrowRightArrow"))
			return 8;
		else if(gesture.equals("rightArrowUpArrow"))
			return 9;
		else if(gesture.equals("rightArrowDownArrow"))
			return 10;
		else if(gesture.equals("downArrowRightArrow"))
			return 11;
		else if(gesture.equals("downArrowLeftArrow"))
			return 12;
		else if(gesture.equals("leftArrowDownArrow"))
			return 13;
		else if(gesture.equals("leftArrowUpArrow"))
			return 14;
		else if(gesture.equals("upArrowLeftArrow"))
			return 15;
		else 
			return -1;
	}
	
	private static String intToGest(int i){
		switch(i){
			case(0): return "upArrow";	
			case(1): return "rightUpArrow";
			case(2): return "rightArrow";
			case(3): return "rightDownArrow";
			case(4): return "downArrow";
			case(5): return "leftDownArrow";
			case(6): return "leftArrow";
			case(7): return "leftUpArrow";
			case(8): return "upArrowRightArrow";	
			case(9): return "rightArrowUpArrow";
			case(10): return "rightArrowDownArrow";
			case(11): return "downArrowRightArrow";
			case(12): return "downArrowLeftArrow";
			case(13): return "leftArrowDownArrow";
			case(14): return "leftArrowUpArrow";
			case(15): return "upArrowLeftArrow";
			default: return "";
		}
	}
	
	private static int gestToInt2(String gesture){
		if(gesture.equals("N"))
			return 0;
		else if(gesture.equals("NE"))
				return 1;
		else if(gesture.equals("E"))
			return 2;
		else if(gesture.equals("SE"))
			return 3;
		else if(gesture.equals("S"))
			return 4;
		else if(gesture.equals("SO"))
			return 5;
		else if(gesture.equals("O"))
			return 6;
		else if(gesture.equals("NO"))
			return 7;
		else if(gesture.equals("N-E"))
			return 8;
		else if(gesture.equals("E-N"))
			return 9;
		else if(gesture.equals("E-S"))
			return 10;
		else if(gesture.equals("S-E"))
			return 11;
		else if(gesture.equals("S-O"))
			return 12;
		else if(gesture.equals("O-S"))
			return 13;
		else if(gesture.equals("O-N"))
			return 14;
		else if(gesture.equals("N-O"))
			return 15;
		else 
			return -1;
	}
	
	private static String intToGest2(int i){
		switch(i){
			case(0): return "N";	
			case(1): return "NE";
			case(2): return "E";
			case(3): return "SE";
			case(4): return "S";
			case(5): return "SO";
			case(6): return "O";
			case(7): return "NO";
			case(8): return "N-E";	
			case(9): return "E-N";
			case(10): return "E-S";
			case(11): return "S-E";
			case(12): return "S-O";
			case(13): return "O-S";
			case(14): return "O-N";
			case(15): return "N-O";
			default: return "";
		}
	}
	
	private static String gestureMatrixToString(int[][] confusionMatrix){
		String toReturn = "";
		for(int i = 0; i < 16; i++){
			toReturn += intToGest2(i)+": ";
			for(int j = 0; j < 16; j++){
				toReturn += confusionMatrix[i][j]+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	private static String gestureMatrixToStringForLatex(int[][] confusionMatrix){
		String toReturn = "\\begin{tabular}{c|cccccccccccccccc}\n";
		toReturn += "& N & NE & E & SE & S & SO & O & NO & N-E & E-N & E-S & S-E & S-O & O-S & O-N & N-O\\\\\n";
		toReturn += "\\hline\n";
		for(int i = 0; i < 16; i++){
			toReturn += intToGest2(i);
			for(int j = 0; j < 16; j++){
				toReturn += " & "+confusionMatrix[i][j];
			}
			toReturn += "\\\\\n";
		}
		toReturn += "\\end{tabular}\n";
		return toReturn;
	}
	
	private static double gestureRecognitionRate(int[][] confusionMatrix){
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				if(i == j)
					goodClass += confusionMatrix[i][j];
				else
					badClass += confusionMatrix[i][j];
			}
		}
		return goodClass*100/(goodClass + badClass);
	}
	
	private static double[] gestureInformations(int[][] confusionMatrix){
		double[] toReturn = new double[4];
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
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
	
	/**************************************************************************************/
	
	private static int charToInt(String c){
		return ((int)c.charAt(0))-97;
	}
	
	private static String intToChar(int i){
		return ""+(char)(i+97);
	}
	
	private static String lettersMatrixToString(int[][] confusionMatrix){
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
	
	private static String lettersMatrixToStringForLatex(int[][] confusionMatrix){
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
	
	private static double lettersRecognitionRate(int[][] confusionMatrix){
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
	
	private static double[] lettersInformations(int[][] confusionMatrix){
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
	
	/**************************************************************************************/
	
	private static int shapeToInt(String gesture){
		if(gesture.equals("triangle"))
			return 0;
		else if(gesture.equals("square"))
				return 1;
		else if(gesture.equals("rectangle"))
			return 2;
		else if(gesture.equals("circle"))
			return 3;
		else if(gesture.equals("pentagon"))
			return 4;
		else if(gesture.equals("parallelogram"))
			return 5;
		else if(gesture.equals("doubleSquare"))
			return 6;
		else if(gesture.equals("doubleCircle"))
			return 7;
		else 
			return -1;
	}
	
	private static String intToShape(int i){
		switch(i){
			case(0): return "triangle";	
			case(1): return "square";
			case(2): return "rectangle";
			case(3): return "circle";
			case(4): return "pentagon";
			case(5): return "parallelogram";
			case(6): return "doubleSquare";
			case(7): return "doubleCircle";
			default: return "";
		}
	}
	
	private static String intToShape2(int i){
		switch(i){
			case(0): return "triangle";	
			case(1): return "carré";
			case(2): return "rectangle";
			case(3): return "cercle";
			case(4): return "pentagone";
			case(5): return "parallélogramme";
			case(6): return "double carré";
			case(7): return "double cercle";
			default: return "";
		}
	}
	
	private static String shapeMatrixToString(int[][] confusionMatrix){
		String toReturn = "";
		for(int i = 0; i < 8; i++){
			toReturn += intToShape2(i)+": ";
			for(int j = 0; j < 8; j++){
				toReturn += confusionMatrix[i][j]+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	private static String shapeMatrixToStringForLatex(int[][] confusionMatrix){
		String toReturn = "\\begin{tabular}{c|cccccccccccccccc}\n";
		toReturn += "& N & NE & E & SE & S & SO & O & NO & N-E & E-N & E-S & S-E & S-O & O-S & O-N & N-O\\\\\n";
		toReturn += "\\hline\n";
		for(int i = 0; i < 8; i++){
			toReturn += intToShape2(i);
			for(int j = 0; j < 8; j++){
				toReturn += " & "+confusionMatrix[i][j];
			}
			toReturn += "\\\\\n";
		}
		toReturn += "\\end{tabular}\n";
		return toReturn;
	}
	
	private static double shapeRecognitionRate(int[][] confusionMatrix){
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(i == j)
					goodClass += confusionMatrix[i][j];
				else
					badClass += confusionMatrix[i][j];
			}
		}
		return goodClass*100/(goodClass + badClass);
	}
	
	private static double[] shapeInformations(int[][] confusionMatrix){
		double[] toReturn = new double[4];
		double goodClass = 0;
		double badClass = 0;
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
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
	
	/**************************************************************************************/
	
	public static int gestureToInt(String gesture, int category){
		switch(category){
		case 1: return digitToInt(gesture);
		case 2: return gestToInt(gesture);
		case 3: return charToInt(gesture);
		case 4: return shapeToInt(gesture);
		default: return -1;
		}
	}
	
	public static String intToGesture(int gesture, int category){
		switch(category){
		case 1: return intToDigit(gesture);
		case 2: return intToGest(gesture);
		case 3: return intToChar(gesture);
		case 4: return intToShape(gesture);
		default: return null;
		}
	}
	
	public static String matrixToString(int[][] confusionMatrix, int category){
		switch(category){
		case 1: return digitMatrixToString(confusionMatrix);
		case 2: return gestureMatrixToString(confusionMatrix);
		case 3: return lettersMatrixToString(confusionMatrix);
		case 4: return shapeMatrixToString(confusionMatrix);
		default: return null;
		}
	}
	
	public static String matrixToStringForLatex(int[][] confusionMatrix, int category){
		switch(category){
		case 1: return digitMatrixToStringForLatex(confusionMatrix);
		case 2: return gestureMatrixToStringForLatex(confusionMatrix);
		case 3: return lettersMatrixToStringForLatex(confusionMatrix);
		case 4: return shapeMatrixToStringForLatex(confusionMatrix);
		default: return null;
		}
	}
	
	public static double recognitionRate(int[][] confusionMatrix, int category){
		switch(category){
		case 1: return digitRecognitionRate(confusionMatrix);
		case 2: return gestureRecognitionRate(confusionMatrix);
		case 3: return lettersRecognitionRate(confusionMatrix);
		case 4: return shapeRecognitionRate(confusionMatrix);
		default: return -1.0;
		}
	}
	
	public static double[] informations(int[][] confusionMatrix, int category){
		switch(category){
		case 1: return digitInformations(confusionMatrix);
		case 2: return gestureInformations(confusionMatrix);
		case 3: return lettersInformations(confusionMatrix);
		case 4: return shapeInformations(confusionMatrix);
		default: return null;
		}
	}
	
	public static int maxGestureIndex(int category){
		switch(category){
		case 1: return 10;
		case 2: return 16;
		case 3: return 26;
		case 4: return 8;
		default: return -1;
		}
	}
	

}
