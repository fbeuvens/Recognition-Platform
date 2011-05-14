package algorithm.stochasticLevenshtein;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import data.Dot;

import algorithm.levenshtein.Levenshtein;



public class JointRecognizer {
	private Hashtable<String, String> strings;
	private String[] alphabet;
	
	public JointRecognizer(String[] alphabet){
		this.alphabet = alphabet;
		strings = new Hashtable<String, String>();
	}
	
	
	public void addTemplate(String name, List<Dot> gesture){
		strings.put(algorithm.levenshtein.Utils.transform(gesture),name);
	}

	public void addTemplate(String name, String gesture){
		strings.put(gesture, name);
	}

	public String recognize(String gesture){
		double minScore = Integer.MAX_VALUE;
		String toReturn = "";
		Enumeration<String> e = strings.keys();
		while(e.hasMoreElements()){
			String key = e.nextElement();
			StochasticLevenshtein sl = new StochasticLevenshtein(alphabet, key, gesture);
			sl.jointExpMax(50);
			double dist = sl.distance();
			if(dist < minScore){
				minScore = dist;
				toReturn = strings.get(key);
			}
		}
		return toReturn;
	}

}
