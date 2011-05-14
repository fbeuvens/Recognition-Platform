package algorithm.levenshtein;

import java.util.Enumeration;
import java.util.Hashtable;

/*
 * Classe definissant une table de distances a double entree de strings
 */
public class DistancesTable {

	private Hashtable<String, Hashtable<String, Double>> ht;
	
	public DistancesTable(){

		ht = new Hashtable<String, Hashtable<String, Double>>();
		

	}
	
	/*
	 * pre: -
	 * post: la distance entre a et b est renvoyee si celle-ci existe dans la table des distances, -1 sinon
	 */
	public double get(String a, String b){
		if(ht.get(a) == null)
			return -1.0;
		else
			if((ht.get(a)).get(b) == null)
				return -1.0;
			else
				return (ht.get(a)).get(b);
	}
	
	/*
	 * pre: -
	 * post: la distance distance est inseree comme distance entre a et b
	 */
	public void put(String a, String b, double distance){
		if(ht.get(a)==null)
			ht.put(a, new Hashtable<String, Double>());
		(ht.get(a)).put(b,distance);
	}
	
	
	
	public String toString(){
		String toReturn = "";
		Enumeration<String> e1 = ht.keys();
		Enumeration<String> e2 = ht.keys();
		
		while(e1.hasMoreElements()){
			String key1 = e1.nextElement();
			while(e2.hasMoreElements()){
				String key2 = e2.nextElement();
				toReturn += get(key1,key2)+" ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		
		String[] alphabet = {"1", "2", "3"};
	}

}
