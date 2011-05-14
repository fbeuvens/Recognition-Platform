package test.ui.shapes;

public class Utils {
	
	public static int shapeToInt(String gesture){
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
	
	public static String intToShape(int i){
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
	
	public static String intToShape2(int i){
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
	
	public static String matrixToString(int[][] confusionMatrix){
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
	
	public static String matrixToStringForLatex(int[][] confusionMatrix){
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
	
	public static double recognitionRate(int[][] confusionMatrix){
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
	
	public static double[] informations(int[][] confusionMatrix){
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

}
