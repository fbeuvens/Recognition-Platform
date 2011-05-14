package algorithm.stochasticLevenshtein;

import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.levenshtein.Levenshtein;
import algorithm.stochasticLevenshtein.StochasticLevenshtein;



public class Test {
	
	public static void main(String[] args){
		String a1 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a1.txt").parse());
		String a2 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a2.txt").parse());
		String a3 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a3.txt").parse());
		String a4 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a4.txt").parse());
		String a5 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a5.txt").parse());
		String a6 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\a\\a6.txt").parse());
		
		String i1 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\i\\i1.txt").parse());
		String i2 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\i\\i2.txt").parse());
		String i3 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\i\\i3.txt").parse());
		String i4 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\i\\i4.txt").parse());
		String i5 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\i\\i5.txt").parse());
		
		String s1 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\s\\s1.txt").parse());
		String s2 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\s\\s2.txt").parse());
		String s3 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\s\\s3.txt").parse());
		String s4 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\s\\s4.txt").parse());
		String s5 = algorithm.levenshtein.Utils.transform(new RecordParser("records\\14\\s\\s5.txt").parse());
		
		String[] alphabet = {"0","1","2","3","4","5","6","7"};
		String[] trainingSet = {"1232134234141432", "12331234123421", "1422213412432", "1334124323412"};
		
		
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
		
		/*
		String[][] ts1 = {{a1, a1}};
		ConditionalTransducer cd = new ConditionalTransducer(alphabet, ts1);
		cd.expMax(100);
		System.out.println("a1 - a1: "+cd.distance(a1, a1));
		//System.out.println(sed.conditionalProbSum());
		
		String[][] ts2 = {{a2, a1}};
		cd = new ConditionalTransducer(alphabet, ts2);
		cd.expMax(100);
		System.out.println("a2 - a1: "+cd.distance(a2, a1));
		
		String[][] ts3 = {{a3, a1}};
		cd = new ConditionalTransducer(alphabet, ts3);
		cd.expMax(100);
		System.out.println("a3 - a1: "+cd.distance(a3, a1));
		
		String[][] ts4 = {{a4, a1}};
		cd = new ConditionalTransducer(alphabet, ts4);
		cd.expMax(100);
		System.out.println("a4 - a1: "+cd.distance(a4, a1));
		
		String[][] ts5 = {{a5, a1}};
		cd = new ConditionalTransducer(alphabet, ts5);
		cd.expMax(100);
		System.out.println("a4 - a1: "+cd.distance(a5, a1));
		
		String[][] ts6 = {{a6, a1}};
		cd = new ConditionalTransducer(alphabet, ts6);
		cd.expMax(100);
		System.out.println("a5 - a1: "+cd.distance(a6, a1));
		
		System.out.println();
		
		String[][] ts7 = {{i1, a1}};
		cd = new ConditionalTransducer(alphabet, ts7);
		cd.expMax(100);
		System.out.println("i1 - a1: "+cd.distance(i1, a1));
		//System.out.println(sed.conditionalProbSum());
		
		String[][] ts8 = {{i2, a1}};
		cd = new ConditionalTransducer(alphabet, ts8);
		cd.expMax(100);
		System.out.println("i2 - a1: "+cd.distance(i2, a1));
		
		String[][] ts9 = {{i3, a1}};
		cd = new ConditionalTransducer(alphabet, ts9);
		cd.expMax(100);
		System.out.println("i3 - a1: "+cd.distance(i3, a1));
		
		String[][] ts10 = {{i4, a1}};
		cd = new ConditionalTransducer(alphabet, ts10);
		cd.expMax(100);
		System.out.println("i4 - a1: "+cd.distance(i4, a1));
		
		String[][] ts11 = {{i5, a1}};
		cd = new ConditionalTransducer(alphabet, ts11);
		cd.expMax(100);
		System.out.println("i5 - a1: "+cd.distance(i5, a1));
		
		
		System.out.println();
		
		String[][] ts12 = {{s1, a1}};
		cd = new ConditionalTransducer(alphabet, ts12);
		cd.expMax(100);
		System.out.println("s1 - a1: "+cd.distance(s1, a1));
		//System.out.println(sed.conditionalProbSum());
		
		String[][] ts13 = {{s2, a1}};
		cd = new ConditionalTransducer(alphabet, ts13);
		cd.expMax(100);
		System.out.println("s2 - a1: "+cd.distance(s2, a1));
		
		String[][] ts14 = {{s3, a1}};
		cd = new ConditionalTransducer(alphabet, ts14);
		cd.expMax(100);
		System.out.println("s3 - a1: "+cd.distance(s3, a1));
		
		String[][] ts15 = {{s4, a1}};
		cd = new ConditionalTransducer(alphabet, ts15);
		cd.expMax(100);
		System.out.println("s4 - a1: "+cd.distance(s4, a1));
		
		String[][] ts16 = {{s5, a1}};
		cd = new ConditionalTransducer(alphabet, ts16);
		cd.expMax(100);
		System.out.println("s5 - a1: "+cd.distance(s5, a1));
		*/
		
	
		String[][] tset = new String[15][2];
		tset[0][0] = a2;
		tset[1][0] = a3;
		tset[2][0] = a4;
		tset[3][0] = a5;
		tset[4][0] = a6;
		tset[5][0] = i1;
		tset[6][0] = i2;
		tset[7][0] = i3;
		tset[8][0] = i4;
		tset[9][0] = i5;
		tset[10][0] = s1;
		tset[11][0] = s2;
		tset[12][0] = s3;
		tset[13][0] = s4;
		tset[14][0] = s5;
		
		Levenshtein l = new Levenshtein();
		l.addTemplate("a", a2);
		l.addTemplate("a", a3);
		l.addTemplate("a", a4);
		l.addTemplate("a", a5);
		l.addTemplate("a", a6);
		l.preCompute();
		
		l.removeTemplate(a2);
		tset[0][1] = l.closerGesture(a2);
		System.out.println(tset[0][1]);
		l.addTemplate("a", a2);
		
		l = new Levenshtein();
		l.addTemplate("a", a2);
		l.addTemplate("a", a4);
		l.addTemplate("a", a5);
		l.addTemplate("a", a6);
		tset[1][1] = l.closerGesture(a3);
		
		l = new Levenshtein();
		l.addTemplate("a", a2);
		l.addTemplate("a", a3);
		l.addTemplate("a", a5);
		l.addTemplate("a", a6);
		tset[2][1] = l.closerGesture(a4);
		
		l = new Levenshtein();
		l.addTemplate("a", a2);
		l.addTemplate("a", a3);
		l.addTemplate("a", a4);
		l.addTemplate("a", a6);
		tset[3][1] = l.closerGesture(a5);
		
		l = new Levenshtein();
		l.addTemplate("a", a2);
		l.addTemplate("a", a3);
		l.addTemplate("a", a4);
		l.addTemplate("a", a5);
		tset[4][1] = l.closerGesture(a6);
		
		System.out.println("a lev");
		
		l = new Levenshtein();
		l.addTemplate("i", i2);
		l.addTemplate("i", i3);
		l.addTemplate("i", i4);
		l.addTemplate("i", i5);
		tset[5][1] = l.closerGesture(i1);
		
		
		l = new Levenshtein();
		l.addTemplate("i", i1);
		l.addTemplate("i", i3);
		l.addTemplate("i", i4);
		l.addTemplate("i", i5);
		tset[6][1] = l.closerGesture(i2);
		
		l = new Levenshtein();
		l.addTemplate("i", i1);
		l.addTemplate("i", i2);
		l.addTemplate("i", i4);
		l.addTemplate("i", i5);
		tset[7][1] = l.closerGesture(i3);
		
		l = new Levenshtein();
		l.addTemplate("i", i1);
		l.addTemplate("i", i2);
		l.addTemplate("i", i3);
		l.addTemplate("i", i5);
		tset[8][1] = l.closerGesture(i4);
		
		l = new Levenshtein();
		l.addTemplate("i", i1);
		l.addTemplate("i", i2);
		l.addTemplate("i", i3);
		l.addTemplate("i", i4);
		tset[9][1] = l.closerGesture(i5);
		
		System.out.println("i lev");
		
		l = new Levenshtein();
		l.addTemplate("s", s2);
		l.addTemplate("s", s3);
		l.addTemplate("s", s4);
		l.addTemplate("s", s5);
		tset[10][1] = l.closerGesture(s1);
		
		l = new Levenshtein();
		l.addTemplate("s", s1);
		l.addTemplate("s", s3);
		l.addTemplate("s", s4);
		l.addTemplate("s", s5);
		tset[11][1] = l.closerGesture(s2);
		
		l = new Levenshtein();
		l.addTemplate("s", s1);
		l.addTemplate("s", s2);
		l.addTemplate("s", s4);
		l.addTemplate("s", s5);
		tset[12][1] = l.closerGesture(s3);
		
		l = new Levenshtein();
		l.addTemplate("s", s1);
		l.addTemplate("s", s2);
		l.addTemplate("s", s3);
		l.addTemplate("s", s5);
		tset[13][1] = l.closerGesture(s4);
		
		l = new Levenshtein();
		l.addTemplate("s", s1);
		l.addTemplate("s", s2);
		l.addTemplate("s", s3);
		l.addTemplate("s", s4);
		tset[14][1] = l.closerGesture(s5);
		
		System.out.println("s lev");
		
		
		ConditionalTransducer cd2 = new ConditionalTransducer(alphabet, tset);
		cd2.expMax(50);
		
		System.out.println("a2 - a1: "+cd2.distance(a2, a1));
		System.out.println("a3 - a1: "+cd2.distance(a3, a1));
		System.out.println("a4 - a1: "+cd2.distance(a4, a1));
		System.out.println("a5 - a1: "+cd2.distance(a5, a1));
		System.out.println("a6 - a1: "+cd2.distance(a6, a1));
		
		System.out.println("i1 - a1: "+cd2.distance(i1, a1));
		System.out.println("i2 - a1: "+cd2.distance(i2, a1));
		System.out.println("i3 - a1: "+cd2.distance(i3, a1));
		System.out.println("i4 - a1: "+cd2.distance(i4, a1));
		System.out.println("i5 - a1: "+cd2.distance(i5, a1));
		
		System.out.println("s1 - a1: "+cd2.distance(s1, a1));
		System.out.println("s2 - a1: "+cd2.distance(s2, a1));
		System.out.println("s3 - a1: "+cd2.distance(s3, a1));
		System.out.println("s4 - a1: "+cd2.distance(s4, a1));
		System.out.println("s5 - a1: "+cd2.distance(s5, a1));
		
		
		
		
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
