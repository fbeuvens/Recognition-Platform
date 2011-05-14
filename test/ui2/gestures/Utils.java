package test.ui2.gestures;

public class Utils {
	
	public static int gestToInt(String gesture){
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
	
	public static String intToGest(int i){
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
	
	public static int gestToInt2(String gesture){
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
	
	public static String intToGest2(int i){
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
	
	public static String matrixToString(int[][] confusionMatrix){
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
	
	public static String matrixToStringForLatex(int[][] confusionMatrix){
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
	
	public static double recognitionRate(int[][] confusionMatrix){
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
	
	public static double[] informations(int[][] confusionMatrix){
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

}
