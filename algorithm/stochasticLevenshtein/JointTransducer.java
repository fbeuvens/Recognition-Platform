package algorithm.stochasticLevenshtein;

import java.util.List;

public class JointTransducer {
	
	CostsTable costs, auxiliaryCosts;
	String[] alphabet1, alphabet2;
	String[][] trainingPairs;
	

	public JointTransducer(String[] alphabet, String[][] trainingPairs){
		this.alphabet1 = alphabet;
		this.alphabet2 = alphabet;
		this.trainingPairs = trainingPairs;
		costs = new CostsTable(alphabet);
		auxiliaryCosts = new CostsTable(alphabet);
		auxiliaryCosts.init(0);
		//costs.init(0.1);
	}

	public JointTransducer(String[] alphabet1, String[] alphabet2, String[][] trainingPairs){
		this.alphabet1 = alphabet1;
		this.alphabet2 = alphabet2;
		this.trainingPairs = trainingPairs;
		costs = new CostsTable(alphabet1,alphabet2);
		auxiliaryCosts = new CostsTable(alphabet1,alphabet2);
		auxiliaryCosts.init(0);
		//costs.init(0.1);
	}
	
	public double[][] forward(String x, String y){
		double[][] forwardValues = new double[x.length()+1][y.length()+1];

		forwardValues[0][0] = 1;

		for (int i = 1; i < forwardValues.length; i++)
			forwardValues[i][0] = forwardValues[i-1][0]*costs.get(x.charAt(i-1)+"", "");

		for (int j = 1; j < forwardValues[0].length; j++)
			forwardValues[0][j] = forwardValues[0][j-1]*costs.get("", y.charAt(j-1)+"");

		for (int i = 1; i < forwardValues.length; i++){
			for (int j = 1; j < forwardValues[0].length; j++){
				forwardValues[i][j] = forwardValues[i-1][j-1]*costs.get(x.charAt(i-1)+"", y.charAt(j-1)+"") +
				forwardValues[i-1][j]*costs.get(x.charAt(i-1)+"", "") +
				forwardValues[i][j-1]*costs.get("", y.charAt(j-1)+"");
			}
		}

		return forwardValues;
	}
	
	public double[][] backward(String x, String y){
		double[][] backwardValues = new double[x.length()+1][y.length()+1];

		backwardValues[x.length()][y.length()] = 1;//costs.get("", "");

		for (int i = backwardValues.length-2; i >=0 ; i--)
			backwardValues[i][y.length()] = backwardValues[i+1][y.length()]*costs.get(x.charAt(i)+"", "");

		for (int j = backwardValues[0].length-2; j >= 0 ; j--)
			backwardValues[x.length()][j] = backwardValues[x.length()][j+1]*costs.get("", y.charAt(j)+"");

		for (int i = backwardValues.length-2; i >= 0; i--){
			for (int j = backwardValues[0].length-2; j >= 0; j--){
				backwardValues[i][j] = backwardValues[i+1][j+1]*costs.get(x.charAt(i)+"", y.charAt(j)+"") +
				backwardValues[i+1][j]*costs.get(x.charAt(i)+"", "") +
				backwardValues[i][j+1]*costs.get("", y.charAt(j)+"");
			}
		}

		return backwardValues;
	}
	
	public double probability(String x, String y){
		return backward(x,y)[0][0]*costs.get("","");
	}

	public double distance(String x, String y){
		return 0-Math.log(probability(x,y))/Math.log(2);
	}
	
	public void expMax(int nbrMaxIterations){
		double[] lastProb = new double[trainingPairs.length];
		for(int i = 0; i < trainingPairs.length; i++)
			lastProb[i] = probability(trainingPairs[i][0], trainingPairs[i][1]);

		for(int i = 0; i < nbrMaxIterations; i++){
		
			expectation();
			
			maximisation();
			
			double[] newProb = new double[trainingPairs.length];
			for(int j = 0; j < trainingPairs.length; j++){
				newProb[j] = probability(trainingPairs[j][0], trainingPairs[j][1]);
			}
			
			boolean converge = true;
			for(int j = 0; j < trainingPairs.length; j++)
				converge = converge && Math.abs(lastProb[j]-newProb[j]) < lastProb[j]/1000;
			
			if(converge){
				//System.out.println("number it: "+(i+1));
				i = nbrMaxIterations;
			}
			for(int j = 0; j < trainingPairs.length; j++)
				lastProb[j] = newProb[j];

		}
	}
	
	public void expectation(){
		Object[] forwardTables = new Object[trainingPairs.length];
		Object[] backwardTables = new Object[trainingPairs.length];
		for(int i = 0; i < trainingPairs.length; i++){
			forwardTables[i] = forward(trainingPairs[i][0], trainingPairs[i][1]);
			backwardTables[i] = backward(trainingPairs[i][0], trainingPairs[i][1]);
		}
		
		auxiliaryCosts.init(0);
		auxiliaryCosts.put("", "", trainingPairs.length);

		
		for(int a = 0; a < trainingPairs.length; a++){
			String x = trainingPairs[a][0];
			String y = trainingPairs[a][1];
			for(int i = 1; i < x.length()+1; i++){
				for(int j = 1; j < y.length()+1; j++){
					double tmp = ((double[][])forwardTables[a])[x.length()][y.length()]*costs.get("", "");
					auxiliaryCosts.put(x.charAt(i-1)+"","",auxiliaryCosts.get(x.charAt(i-1)+"","")+((double[][])forwardTables[a])[i-1][j]*costs.get(x.charAt(i-1)+"","")*((double[][])backwardTables[a])[i][j]/tmp);
					auxiliaryCosts.put("",y.charAt(j-1)+"",auxiliaryCosts.get("",y.charAt(j-1)+"")+((double[][])forwardTables[a])[i][j-1]*costs.get("",y.charAt(j-1)+"")*((double[][])backwardTables[a])[i][j]/tmp);
					auxiliaryCosts.put(x.charAt(i-1)+"", y.charAt(j-1)+"",auxiliaryCosts.get(x.charAt(i-1)+"", y.charAt(j-1)+"")+((double[][])forwardTables[a])[i-1][j-1]*costs.get(x.charAt(i-1)+"", y.charAt(j-1)+"")*((double[][])backwardTables[a])[i][j]/tmp);
				}
			}
		}
	}


	public void maximisation(){
		double N = 0.0;

		for(int i = -1; i < alphabet1.length; i++){
			String str1 = "";
			if(i > -1)
				str1 = alphabet1[i];
			for(int j = -1; j < alphabet2.length; j++){
				String str2 = "";
				if(j > -1)
					str2 = alphabet2[j];
				N += auxiliaryCosts.get(str1, str2);
			}
		}
		for(int i = -1; i < alphabet1.length; i++){
			String str1 = "";
			if(i > -1)
				str1 = alphabet1[i];
			for(int j = -1; j < alphabet2.length; j++){
				String str2 = "";
				if(j > -1)
					str2 = alphabet2[j];
				costs.put(str1, str2, auxiliaryCosts.get(str1, str2)/N);
			}
		}
	}
	
	public double probSum(){
		double toReturn = 0.0;
		for(int i = -1; i < alphabet1.length; i++){
			String str1 = "";
			if(i > -1)
				str1 = alphabet1[i];
			for(int j = -1; j < alphabet2.length; j++){
				String str2 = "";
				if(j > -1)
					str2 = alphabet2[j];
				toReturn += costs.get(str1, str2);
			}
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		String[] alphabet = {"0","1","2","3","4","5","6","7"};
		String[] alphabet1 = {"a","b"};
		String[] alphabet2 = {"c"};
		String s1 = "564644422221111111111111111111111010100010444444443444343333333333323221111111111111111111111000077777777777777766665555555555555544443332512244344434343433433333333333333333332221111111111111111111115";
		String s2 = "564644422221111111111111111111111010100010444444443444343333333333323221111111111111111111111000077777777777777766665555555555555544443332512244344434343433433333333333333333332221111111111111111111115";
		String[][] trainingSet = {{"abb","cc"}};
		JointTransducer jt = new JointTransducer(alphabet1, alphabet2, trainingSet);
		jt.expMax(100);
		System.out.println(jt.costs);

	}

}
