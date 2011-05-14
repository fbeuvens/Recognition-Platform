package algorithm.levenshtein;

import data.RecordParser;
import algorithm.stochasticLevenshtein.StochasticLevenshtein;

public class Test {
	
	public static void main(String[] args){
		String a1 = Utils.transform(new RecordParser("records/1/a/a1.txt").parse());
		String a1scaled = Utils.transformWithProcessing(new RecordParser("records/1/a/a1.txt").parse(),false,true,false,false);
		String a2 = Utils.transform(new RecordParser("records/1/a/a2.txt").parse());
		String a3 = Utils.transform(new RecordParser("records/1/a/a3.txt").parse());
		String a4 = Utils.transform(new RecordParser("records/1/a/a4.txt").parse());
		String a5 = Utils.transform(new RecordParser("records/1/a/a5.txt").parse());
		String a6 = Utils.transform(new RecordParser("records/1/a/a6.txt").parse());
		
		String i1 = Utils.transform(new RecordParser("records/1/i/i1.txt").parse());
		String i2 = Utils.transform(new RecordParser("records/1/i/i2.txt").parse());
		String i3 = Utils.transform(new RecordParser("records/1/i/i3.txt").parse());
		String i4 = Utils.transform(new RecordParser("records/1/i/i4.txt").parse());
		String i5 = Utils.transform(new RecordParser("records/1/i/i5.txt").parse());
		
		String s1 = Utils.transform(new RecordParser("records/1/s/s1.txt").parse());
		String s2 = Utils.transform(new RecordParser("records/1/s/s2.txt").parse());
		String s3 = Utils.transform(new RecordParser("records/1/s/s3.txt").parse());
		String s4 = Utils.transform(new RecordParser("records/1/s/s4.txt").parse());
		String s5 = Utils.transform(new RecordParser("records/1/s/s5.txt").parse());
		
		System.out.println(a1);
		System.out.println(a1scaled);
		System.out.println("lev: "+new Levenshtein().distance(a1, a1scaled));
		
		String[] alphabet = {"0","1","2","3","4","5","6","7"};
		
		
		/*System.out.println("a1 - a1: "+sed.backward(a1, a1)[0][0]);
		System.out.println("a1 - a2: "+sed.backward(a1, a2)[0][0]);
		System.out.println("a1 - a3: "+sed.backward(a1, a3)[0][0]);
		System.out.println("a1 - a4: "+sed.backward(a1, a4)[0][0]);
		System.out.println("a1 - a5: "+sed.backward(a1, a5)[0][0]);
		System.out.println("a1 - a6: "+sed.backward(a1, a6)[0][0]);
		System.out.println();
		System.out.println("a1 - i1: "+sed.backward(a1, i1)[0][0]);
		System.out.println("a1 - i2: "+sed.backward(a1, i2)[0][0]);
		System.out.println("a1 - i3: "+sed.backward(a1, i3)[0][0]);
		System.out.println("a1 - i4: "+sed.backward(a1, i4)[0][0]);
		System.out.println("a1 - i5: "+sed.backward(a1, i5)[0][0]);
		System.out.println();
		System.out.println("a1 - s1: "+sed.backward(a1, s1)[0][0]);
		System.out.println("a1 - s2: "+sed.backward(a1, s2)[0][0]);
		System.out.println("a1 - s3: "+sed.backward(a1, s3)[0][0]);
		System.out.println("a1 - s4: "+sed.backward(a1, s4)[0][0]);
		System.out.println("a1 - s5: "+sed.backward(a1, s5)[0][0]);*/
		//sed.costs.init(0.1);
		//System.out.println(a1);
		//System.out.println(i3);
		/*System.out.println(a1.length());
		System.out.println(a2.length());
		System.out.println(a3.length());
		System.out.println(a4.length());
		System.out.println(a5.length());
		
		System.out.println(s1.length());
		System.out.println(s2.length());
		System.out.println(s3.length());
		System.out.println(s4.length());
		System.out.println(s5.length());*/
		
		
		
		StochasticLevenshtein sed = new StochasticLevenshtein(alphabet, a1, a1);
		sed.conditionalExpMax(100);
		
		System.out.println("a1 - a1: "+sed.distance());
		System.out.println(sed.conditionalProbSum());
		sed = new StochasticLevenshtein(alphabet, a2, a1);
		//sed.costs.init(0.1);
		//System.out.println(a1);
		//System.out.println(a2);
		sed.conditionalExpMax(100);
		System.out.println("a1 - a2: "+sed.distance());
		System.out.println(sed.conditionalProbSum());
		sed = new StochasticLevenshtein(alphabet, a3, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - a3: "+sed.distance());
		System.out.println(sed.conditionalProbSum());
		sed = new StochasticLevenshtein(alphabet, a4, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - a4: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, a5, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - a5: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, a6, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - a6: "+sed.distance());
		//System.out.println(sed.probSum());
		
		System.out.println();
		
		sed = new StochasticLevenshtein(alphabet, i1, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - i1: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, i2, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - i2: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, i3, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - i3: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, i4, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - i4: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, i5, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - i5: "+sed.distance());
		//System.out.println(sed.probSum());
		
		System.out.println();
		
		sed = new StochasticLevenshtein(alphabet, s1, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - s1: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, s2, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - s2: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, s3, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - s3: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, s4, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - s4: "+sed.distance());
		//System.out.println(sed.probSum());
		sed = new StochasticLevenshtein(alphabet, s5, a1);
		sed.conditionalExpMax(100);
		System.out.println("a1 - s5: "+sed.distance());
		//System.out.println(sed.probSum());
		
		
	
		
		
		
		
		
		
		/*System.out.println("a1 - a2: "+Levenshtein.distance(a1, a2));
		System.out.println("a1 - a3: "+Levenshtein.distance(a1, a3));
		System.out.println("a1 - a4: "+Levenshtein.distance(a1, a4));
		System.out.println("a1 - a5: "+Levenshtein.distance(a1, a5));
		System.out.println("a1 - a6: "+Levenshtein.distance(a1, a6));
		System.out.println();
		System.out.println("a1 - i1: "+Levenshtein.distance(a1, i1));
		System.out.println("a1 - i2: "+Levenshtein.distance(a1, i2));
		System.out.println("a1 - i3: "+Levenshtein.distance(a1, i3));
		System.out.println("a1 - i4: "+Levenshtein.distance(a1, i4));
		System.out.println("a1 - i5: "+Levenshtein.distance(a1, i5));
		System.out.println();
		System.out.println("a1 - s1: "+Levenshtein.distance(a1, s1));
		System.out.println("a1 - s2: "+Levenshtein.distance(a1, s2));
		System.out.println("a1 - s3: "+Levenshtein.distance(a1, s3));
		System.out.println("a1 - s4: "+Levenshtein.distance(a1, s4));
		System.out.println("a1 - s5: "+Levenshtein.distance(a1, s5));*/
	}
	
}
