package algorithm.stochasticLevenshtein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import data.Dot;

import algorithm.levenshtein.Levenshtein;
import algorithm.levenshtein.Result;



/*
 * Classe permettant de classifier des strings represetant des gestes a partir
 * d'un transducteur conditionnel
 */
public class ConditionalRecognizer {
	private Hashtable<String, String> stringsByGesture;
	private Hashtable<String, List<String>> stringsByName;
	private String[] alphabet;
	private String[][] trainingPairs;
	private ConditionalTransducer ct;
	private boolean compiled = false;
	
	public ConditionalRecognizer(String[] alphabet){
		this.alphabet = alphabet;
		stringsByGesture = new Hashtable<String, String>();
		stringsByName = new Hashtable<String, List<String>>();
	}
	
	
	/*
	 * pre: -
	 * post: gesture est transforme pour les directions et est ajoute aux exemples d'entrainement
	 */
	public void addTemplate(String name, List<Dot> gesture){
		stringsByGesture.put(algorithm.levenshtein.Utils.transform(gesture),name);
	}

	/*
	 * pre: -
	 * post: gesture est ajoute aux exemples d'entrainement
	 */
	public void addTemplate(String name, String gesture){
		if(stringsByGesture.get(gesture)!= null)
			return;
		stringsByGesture.put(gesture, name);
		List<String> list = stringsByName.get(name);
		if(list == null){
			List<String> newList = new ArrayList<String>();
			newList.add(gesture);
			stringsByName.put(name, newList);
		}
		else
			list.add(gesture);
	}
	
	public void compile(){
		compile(0);
	}
	
	/*
	 * pre: 0 <= normalisation <= 3
	 * post: le transducteur conditionnel est compile pour tous les exemples qui
	 * 		 ont ete ajoutes et pour la normalisation specifiee
	 */
	public void compile(int normalisation){
		trainingPairs = new String[stringsByGesture.size()][2];
		int i = 0;
		Enumeration<String> e = stringsByName.keys();
		while(e.hasMoreElements()){
			String name = e.nextElement();
			List<String> gestures = stringsByName.get(name);
			Levenshtein l = new Levenshtein();
			for(int j = 0; j < gestures.size(); j++){
				l.addTemplate(name, gestures.get(j));
			}
			
			l.preCompute(normalisation);
			for(int j = 0; j < gestures.size(); j++){
				l.removeTemplate(gestures.get(j));
				trainingPairs[i][0] = gestures.get(j);
				trainingPairs[i][1] = l.closerGesture(gestures.get(j), normalisation);
				l.addTemplate(name, gestures.get(j));
				i++;
			}
		}
		
		
		ct = new ConditionalTransducer(alphabet, trainingPairs);
		ct.expMax(100);
		//System.out.println(ct.costs);
		//System.out.println(ct.probSum());
		compiled = true;
	}
	
	/*
	 * pre: -
	 * post: la classe de gesture est renvoyee
	 */
	public String recognize(String gesture){
		return recognize(gesture, 0);
	}

	/*
	 * pre: 0 <= normalisation <= 3
	 * post: la classe de gesture est renvoyee pour la normalisation specifiee
	 */
	public String recognize(String gesture, int normalisation){
		if(!compiled){
			System.out.println("Conditional recognizer not trained");
			return null;
		}
		double minScore = Integer.MAX_VALUE;
		String toReturn = "";
		Enumeration<String> e = stringsByGesture.keys();
		while(e.hasMoreElements()){
			String key = e.nextElement();
			double dist = ct.distance(key,gesture,normalisation);
			if(dist < minScore){
				minScore = dist;
				toReturn = stringsByGesture.get(key);
			}
		}
		return toReturn;
	}
	
	/*
	 * pre: 0 <= normalisation <= 3
	 * 		knn >= 1
	 * post: la classe de gesture est renvoyee pour la normalisation et le nombre
	 * 		 de plus proches voisins specifies
	 */
	public String recognize(String gesture, int normalisation, int knn){
		if(!compiled){
			System.out.println("Conditional recognizer not trained");
			return null;
		}
		List<Result> results = new ArrayList<Result>();
		double minScore = Integer.MAX_VALUE;
		Enumeration<String> e = stringsByGesture.keys();
		while(e.hasMoreElements()){
			String key = e.nextElement();
			double dist = ct.distance(key,gesture,normalisation);
			results.add(new Result(key,dist));
		}
		Collections.sort(results, new Result());
		Hashtable<String, Integer> numberItemByClass = new Hashtable<String, Integer>();
		for(int i = 0; i < knn; i++){
			String classe = stringsByGesture.get(results.get(i).name);
			if(numberItemByClass.get(classe) == null)
				numberItemByClass.put(classe,1);
			else
				numberItemByClass.put(classe, numberItemByClass.get(classe)+1);
		}
		Enumeration<String> e2 = numberItemByClass.keys();
		String toReturn = e2.nextElement();
		while(e2.hasMoreElements()){
			String key = e2.nextElement();
			if(numberItemByClass.get(key) > numberItemByClass.get(toReturn))
				toReturn = key;
		}
		
		return toReturn;
	}
	
	
	/*
	 * pre: gesture un string de taille non nulle
	 * 		0 <= normalisation <= 3
	 * 		knn >= 1
	 * post: les classes de gesture sont renvoyees en fonction de la normalisation
	 * 		 et pour chacun des 1 -> knn plus proches voisins
	 */
	public String[] recognizeForAllKnn(String gesture, int normalisation, int knn){
		if(!compiled){
			System.out.println("Conditional recognizer not trained");
			return null;
		}
		List<Result> results = new ArrayList<Result>();
		double minScore = Integer.MAX_VALUE;
		Enumeration<String> e = stringsByGesture.keys();
		while(e.hasMoreElements()){
			String key = e.nextElement();
			double dist = ct.distance(key,gesture,normalisation);
			results.add(new Result(key,dist));
		}
		Collections.sort(results, new Result());
		Object[] numberItemByClass = new Object[knn];
		for(int i = 0; i < knn; i++)
			numberItemByClass[i] = new Hashtable<String, Integer>();
		for(int i = 0; i < knn; i++){
			for(int j = 0; j <= i; j++){
				String classe = stringsByGesture.get(results.get(j).name);
				if(((Hashtable)numberItemByClass[i]).get(classe) == null)
					((Hashtable)numberItemByClass[i]).put(classe,1);
				else
					((Hashtable)numberItemByClass[i]).put(classe, ((Hashtable<String, Integer>)numberItemByClass[i]).get(classe)+1);
			}
		}
		String[] toReturn = new String[knn];
		for(int i = 0; i < knn; i++){
			Enumeration<String> e2 = ((Hashtable<String, Integer>)numberItemByClass[i]).keys();
			toReturn[i] = e2.nextElement();
			while(e2.hasMoreElements()){
				String key = e2.nextElement();
				if(((Hashtable<String, Integer>)numberItemByClass[i]).get(key) > ((Hashtable<String, Integer>)numberItemByClass[i]).get(toReturn[i]))
					toReturn[i] = key;
			}
		}
		
		return toReturn;
	}

}
