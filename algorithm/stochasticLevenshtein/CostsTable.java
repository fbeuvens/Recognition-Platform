package algorithm.stochasticLevenshtein;

import java.util.Enumeration;
import java.util.Hashtable;

/*
 * Classe definissant une table de couts a double entree de strings
 * definis sur deux alphabets
 */
public class CostsTable {

	private Hashtable<String, Hashtable<String, Double>> ht;
	private String[] alphabet1, alphabet2;
	
	public CostsTable(String[] alphabet){
		this.alphabet1 = alphabet;
		this.alphabet2 = alphabet;
		ht = new Hashtable<String, Hashtable<String, Double>>();
		
		init(1.0/((alphabet1.length+1)*(alphabet2.length+1)));
	}
	
	public CostsTable(String[] alphabet1, String[] alphabet2){
		
		this.alphabet1 = alphabet1;
		this.alphabet2 = alphabet2;
		ht = new Hashtable<String, Hashtable<String, Double>>();
		
		init(1.0/((alphabet1.length+1)*(alphabet2.length+1)));
	}
	
	/*
	 * pre: -
	 * post: tous les couts de la table valent initialValue
	 */
	public void init(double initialValue){
		for (int i = -1; i < alphabet1.length; i++){
			Hashtable<String, Double> current;
			if  (i==-1){
				current = new Hashtable<String, Double>();
				ht.put("", current);
			}
			else{
				current = new Hashtable<String, Double>();
				ht.put(alphabet1[i], current);
			}
						
			for (int j = -1; j < alphabet2.length; j++){
				if(j==-1)
					current.put("",initialValue);
				else
					current.put(alphabet2[j],initialValue);
			}	
		}
	}
	
	/*
	 * pre: -
	 * post: le cout entre a et b est renvoye si celui-ci existe dans la table des distances, -1 sinon
	 */
	public double get(String a, String b){
		return (ht.get(a)).get(b);
	}
	
	/*
	 * pre: -
	 * post: le cout cost est insere comme cout entre a et b
	 */
	public void put(String a, String b, double cost){
		(ht.get(a)).put(b,cost);
	}
	
	
	
	public String toString(){
		String toReturn = "";
		for(int i = -1; i < alphabet1.length; i++){
			if(i == -1)
				toReturn += " : ";
			else
				toReturn += alphabet1[i]+": ";
			String curChar;
			if(i==-1)
				curChar = "";
			else 
				curChar = alphabet1[i];
			
			for(int j = -1; j < alphabet2.length; j++)
				if(j==-1)
					toReturn += get(curChar,"") + " ";
				else 
					toReturn += get(curChar, alphabet2[j]) + " ";
			toReturn += "\n";
		}
		
		return toReturn;
	}
	
	public static void main(String[] args){
		
		String[] alphabet = {"1", "2", "3"};
		CostsTable ct = new CostsTable(alphabet, alphabet);
		System.out.println(ct);
	}

}
