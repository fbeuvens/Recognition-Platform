package algorithm.oneDollar;

import java.util.Comparator;

//
	// Result class
	//
	public class Result implements Comparator {
		
		public String name;
		public double score;
	public  Result(String name, double d) // constructor
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
			return -1;
		if(((Result)o1).score < ((Result)o2).score)
		return 1;
		
		return 0;
	}
}