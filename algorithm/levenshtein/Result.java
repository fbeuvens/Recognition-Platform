package algorithm.levenshtein;

import java.util.Comparator;

	/*
	 * Classe definissant un resultat comprenant un nom associe a un score 
	 */
	public class Result implements Comparator {
	
		public String name;
		public double score;
		public  Result(String name, double d) 
		{
			this.name = name;
			this.score = d;
		}
	
		public Result(){
	
		}
	
		public String toString(){
			return(name +"\t" + score);
		}
	
	
		public int compare(Object o1, Object o2) {
			if(((Result)o1).score > ((Result)o2).score)
				return 1;
			if(((Result)o1).score < ((Result)o2).score)
				return -1;
	
			return 0;
		}
	}