package algorithm.stochasticLevenshtein;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import data.Dot;


public class StochasticLevenshtein {

	CostsTable costs, auxiliaryCosts;
	String[] alphabet1, alphabet2;
	String x, y;
	

	public StochasticLevenshtein(String[] alphabet, String x, String y){
		this.alphabet1 = alphabet;
		this.alphabet2 = alphabet;
		this.x = x;
		this.y = y;
		costs = new CostsTable(alphabet);
		auxiliaryCosts = new CostsTable(alphabet);
		auxiliaryCosts.init(0);
		costs.init(0.1);
	}

	public StochasticLevenshtein(String[] alphabet1, String[] alphabet2, String x, String y){
		this.alphabet1 = alphabet1;
		this.alphabet2 = alphabet2;
		this.x = x;
		this.y = y;
		costs = new CostsTable(alphabet1,alphabet2);
		auxiliaryCosts = new CostsTable(alphabet1,alphabet2);
		auxiliaryCosts.init(0);
		costs.init(0.1);
	}

	public double[][] forward(){
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

	public double[][] backward(){
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

	public double jointProbability(){
		return backward()[0][0]*costs.get("","");
	}

	public double distance(){
		return 0-Math.log(jointProbability())/Math.log(2);
	}

	public double normalizedDistance1(String s, String t){
		return distance()/Math.max(s.length(),t.length());
	}

	public double normalizedDistance2(String s, String t){
		return distance()/(s.length() + t.length());
	}

	public double normalizedDistance3(String s, String t){
		double dist = distance();
		return dist*2/(s.length() + t.length() + dist);
	}



	
	public void jointExpMax(int nbrMaxIterations){
	
		double lastProb = backward()[0][0];
		double lastDist = distance();
		
		double threshold = lastProb/1000;
		for(int i = 0; i < nbrMaxIterations && lastProb > 0 && jointProbability() > 0; i++){


			expectation();
			
			jointMaximisation();
			double newProb = backward()[0][0];
			double newDist = distance();
			
			if(newDist > lastDist && Math.abs(jointProbSum()-1) < 0.001){
				System.out.println("number it: "+(i+1));
				i = nbrMaxIterations;
			}
			lastProb = newProb;
			lastDist = newDist;

			threshold = lastProb/1000;
		}
	}

	


	public void expectation(){
		double[][] forwardValues = forward();
		double[][] backwardValues = backward();

		if(forwardValues[x.length()][y.length()]==0*costs.get("", "")) return;

		auxiliaryCosts.init(0);
		auxiliaryCosts.put("", "", auxiliaryCosts.get("", "")+1);

		
		for(int i = 1; i < x.length()+1; i++){
			for(int j = 1; j < y.length()+1; j++){
				double tmp = forwardValues[x.length()][y.length()]*costs.get("", "");
				auxiliaryCosts.put(x.charAt(i-1)+"","",auxiliaryCosts.get(x.charAt(i-1)+"","")+forwardValues[i-1][j]*costs.get(x.charAt(i-1)+"","")*backwardValues[i][j]/tmp);
				auxiliaryCosts.put("",y.charAt(j-1)+"",auxiliaryCosts.get("",y.charAt(j-1)+"")+forwardValues[i][j-1]*costs.get("",y.charAt(j-1)+"")*backwardValues[i][j]/tmp);
				auxiliaryCosts.put(x.charAt(i-1)+"", y.charAt(j-1)+"",auxiliaryCosts.get(x.charAt(i-1)+"", y.charAt(j-1)+"")+forwardValues[i-1][j-1]*costs.get(x.charAt(i-1)+"", y.charAt(j-1)+"")*backwardValues[i][j]/tmp);
			}
		}

		
	}


	public void jointMaximisation(){
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

	public void conditionalMaximisation(){
		double N = 0.0;
		double Nempty = 0.0;
		Hashtable<String, Double> Nmarg = new Hashtable<String, Double>();

		for(int i = -1; i < alphabet1.length; i++){
			String str1 = "";
			if(i > -1){
				str1 = alphabet1[i];
				Nmarg.put(str1,0.0);
			}
			for(int j = -1; j < alphabet2.length; j++){
				String str2 = "";
				if(j > -1){
					str2 = alphabet2[j];
				}
				N += auxiliaryCosts.get(str1, str2);
				if(!str1.equals(""))
					Nmarg.put(str1, Nmarg.get(str1)+auxiliaryCosts.get(str1, str2));
				if(str1.equals("") && !str2.equals(""))
					Nempty += auxiliaryCosts.get(str1, str2);
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
				if(str1.equals("") && str2.equals(""))
					costs.put(str1, str2, (N - Nempty)/N);
				else
					if(str1.equals(""))
						costs.put(str1, str2, auxiliaryCosts.get(str1, str2)/N);
					else
						if(auxiliaryCosts.get(str1, str2)==0)
							costs.put(str1, str2, 0.0);
						else
							costs.put(str1, str2, (auxiliaryCosts.get(str1, str2)/Nmarg.get(str1)*((N - Nempty)/N)));

			}
		}
	
	}



	public double jointProbSum(){
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

	public Hashtable conditionalProbSum(){
		Hashtable<String, Double> toReturn = new Hashtable<String, Double>();
		for(int i = -1; i < alphabet1.length; i++)
			if(i == -1)
				toReturn.put("", costs.get("",""));
			else
				toReturn.put(alphabet1[i], costs.get(alphabet1[i],""));

		for(int i = -1; i < alphabet1.length; i++){
			String str1 = "";
			if(i > -1)
				str1 = alphabet1[i];
			for(int j = -1; j < alphabet2.length; j++){
				String str2 = "";
				if(j > -1)
					str2 = alphabet2[j];
				if(!str2.equals("")){
					if(!str1.equals(""))
						toReturn.put(str1, toReturn.get(str1)+costs.get(str1,str2));
					toReturn.put(str1, toReturn.get(str1)+costs.get("",str2));
				}
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
		//s1 = "000";
		//s2 = "0000";
		StochasticLevenshtein sl = new StochasticLevenshtein(alphabet, s1, s2);
		sl = new StochasticLevenshtein(alphabet1, alphabet2, "ab","c");
		//sed = new StochasticLevenshtein(alphabet1,alphabet2, s1, s2);
		//sed.costs.init(0.9);

		//double[][] forward = sed.forward();
		//System.out.println(forward[forward.length-1][forward[0].length-1]);
		System.out.println(sl.distance());
		System.out.println(sl.jointProbSum());
		//177777777777777777655555555555555433333321111111111111111100065555555555543333333332211111111115
		//10777777777777775555555555555333333111111111111111111055555555555433333333332222222121115
		//321111111111111111111111001000700700044343444344343333333333333333222111111111151454644444243235

		//564644422221111111111111111111111010100010444444443444343333333333323221111111111111111111111000077777777777777766665555555555555544443332512244344434343433433333333333333333332221111111111111111111115
		sl.jointExpMax(100);

		//forward = sed.forward();

		//System.out.println(forward[forward.length-1][forward[0].length-1]);
		System.out.println(sl.distance());
		System.out.println(sl.jointProbSum());
		System.out.println(sl.costs);
		/*double tmp = sl.costs.get("5", "");
		for(int i = 0; i < sl.alphabet2.length; i++){
			tmp += sl.costs.get("5",sl.alphabet2[i]);
			tmp += sl.costs.get("",sl.alphabet2[i]);
		}
		System.out.println(tmp);
		//System.out.println(sed.costs);
		tmp = 0.0;
		for(int i = -1; i < sl.alphabet1.length; i++){
			String str1 = "";
			if(i > -1)
				str1 = sl.alphabet1[i];
			for(int j = -1; j < sl.alphabet2.length; j++){
				String str2 = "";
				if(j > -1)
					str2 = sl.alphabet2[j];
				tmp += sl.costs.get(str1, str2);
			}
		}*/
		//System.out.println(tmp);
		//System.out.println(sed.costs);4.340518516658259E-71


	}

}
