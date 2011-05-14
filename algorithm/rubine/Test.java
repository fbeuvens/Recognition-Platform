package algorithm.rubine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.Dot;
import data.RecordParser;


public class Test {
	
	public static void main(String[] args){
		
		GestureClass vertical = new GestureClass("vertical");
		GestureClass horizontal = new GestureClass("horizontal");
		GestureClass diagonal = new GestureClass("diagonal");
		GestureClass brokenLine = new GestureClass("brokenLine");
		GestureClass octogon = new GestureClass("octogon");
		
		GestureClass a = new GestureClass("a");
		GestureClass i = new GestureClass("i");
		GestureClass s = new GestureClass("s");
		
		for(int j = 1; j < 0; j++){
			Gesture gest = new Gesture();
			List<Dot> dotSet = (new RecordParser("records\\1\\a\\a"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					gest.addPoint(dot.posX, dot.posY, dot.time_millis);
			}
			a.addExample(gest);
		}
		
		for(int j = 1; j < 0; j++){
			Gesture gest = new Gesture();
			List<Dot> dotSet = (new RecordParser("records\\1\\i\\i"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					gest.addPoint(dot.posX, dot.posY, dot.time_millis);
			}
			i.addExample(gest);
		}
		
		for(int j = 1; j < 5; j++){
			Gesture gest = new Gesture();
			List<Dot> dotSet = (new RecordParser("records\\1\\s\\s"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					gest.addPoint(dot.posX, dot.posY, dot.time_millis);
			}
			s.addExample(gest);
		}
		
		
		Gesture vert1 = new Gesture();
		vert1.addPoint(1, 1, 1);
		vert1.addPoint(1, 2, 2);
		vert1.addPoint(1, 3, 3);
		vert1.addPoint(1, 4, 4);
		vert1.addPoint(1, 5, 5);
		vert1.addPoint(1, 6, 6);
		vert1.addPoint(1, 7, 7);
		vert1.addPoint(1, 8, 8);
		vert1.addPoint(1, 9, 9);
		vert1.addPoint(1, 10, 10);
		vert1.addPoint(1, 11, 11);
		vert1.addPoint(1, 12, 12);
		Gesture vert2 = new Gesture();
		vert2.addPoint(2, 1, 1);
		vert2.addPoint(2, 2, 2);
		vert2.addPoint(2, 3, 3);
		vert2.addPoint(2, 4, 4);
		vert2.addPoint(2, 5, 5);
		vert2.addPoint(2, 6, 6);
		vert2.addPoint(2, 7, 7);
		vert2.addPoint(2, 8, 8);
		vert2.addPoint(2, 9, 9);
		vert2.addPoint(2, 10, 10);
		vert2.addPoint(2, 11, 11);
		vert2.addPoint(2, 12, 12);
		Gesture vert3 = new Gesture();
		vert3.addPoint(3, 1, 1);
		vert3.addPoint(3, 2, 2);
		vert3.addPoint(3, 3, 3);
		vert3.addPoint(3, 4, 4);
		vert3.addPoint(3, 5, 5);
		vert3.addPoint(3, 6, 6);
		vert3.addPoint(3, 7, 7);
		vert3.addPoint(3, 8, 8);
		vert3.addPoint(3, 9, 9);
		vert3.addPoint(3, 10, 10);
		vert3.addPoint(3, 11, 11);
		vert3.addPoint(3, 12, 12);
		Gesture vert4 = new Gesture();
		vert4.addPoint(4, 1, 1);
		vert4.addPoint(4, 2, 2);
		vert4.addPoint(4, 3, 3);
		vert4.addPoint(4, 4, 4);
		vert4.addPoint(4, 5, 5);
		vert4.addPoint(4, 6, 6);
		vert4.addPoint(4, 7, 7);
		vert4.addPoint(4, 8, 8);
		vert4.addPoint(4, 9, 9);
		vert4.addPoint(4, 10, 10);
		vert4.addPoint(4, 11, 11);
		vert4.addPoint(4, 12, 12);
		Gesture vert5 = new Gesture();
		vert5.addPoint(5, 1, 1);
		vert5.addPoint(5, 2, 2);
		vert5.addPoint(5, 3, 3);
		vert5.addPoint(5, 4, 4);
		vert5.addPoint(5, 5, 5);
		vert5.addPoint(5, 6, 6);
		vert5.addPoint(5, 7, 7);
		vert5.addPoint(5, 8, 8);
		vert5.addPoint(5, 9, 9);
		vert5.addPoint(5, 10, 10);
		vert5.addPoint(5, 11, 11);
		vert5.addPoint(5, 12, 12);
		Gesture vert6 = new Gesture();
		vert6.addPoint(6, 1, 1);
		vert6.addPoint(6, 2, 2);
		vert6.addPoint(6, 3, 3);
		vert6.addPoint(6, 4, 4);
		vert6.addPoint(6, 5, 5);
		vert6.addPoint(6, 6, 6);
		vert6.addPoint(6, 7, 7);
		vert6.addPoint(6, 8, 8);
		vert6.addPoint(6, 9, 9);
		vert6.addPoint(6, 10, 10);
		vert6.addPoint(6, 11, 11);
		vert6.addPoint(6, 12, 12);
		vertical.addExample(vert1);
		vertical.addExample(vert2);
		vertical.addExample(vert3);
		vertical.addExample(vert4);
		vertical.addExample(vert5);
		vertical.addExample(vert6);
		
		
		Gesture hor1 = new Gesture();
		hor1.addPoint(1, 1, 1);
		hor1.addPoint(2, 1, 2);
		hor1.addPoint(3, 1, 3);
		hor1.addPoint(4, 1, 4);
		hor1.addPoint(5, 1, 5);
		hor1.addPoint(6, 1, 6);
		hor1.addPoint(7, 1, 7);
		hor1.addPoint(8, 1, 8);
		hor1.addPoint(9, 1, 9);
		hor1.addPoint(10, 1, 10);
		hor1.addPoint(11, 1, 11);
		hor1.addPoint(12, 1, 12);
		Gesture hor2 = new Gesture();
		hor2.addPoint(1, 2, 1);
		hor2.addPoint(2, 2, 2);
		hor2.addPoint(3, 2, 3);
		hor2.addPoint(4, 2, 4);
		hor2.addPoint(5, 2, 5);
		hor2.addPoint(6, 2, 6);
		hor2.addPoint(7, 2, 7);
		hor2.addPoint(8, 2, 8);
		hor2.addPoint(9, 2, 9);
		hor2.addPoint(10, 2, 10);
		hor2.addPoint(11, 2, 11);
		hor2.addPoint(12, 2, 12);
		Gesture hor3 = new Gesture();
		hor3.addPoint(1, 3, 1);
		hor3.addPoint(2, 3, 2);
		hor3.addPoint(3, 3, 3);
		hor3.addPoint(4, 3, 4);
		hor3.addPoint(5, 3, 5);
		hor3.addPoint(6, 3, 6);
		hor3.addPoint(7, 3, 7);
		hor3.addPoint(8, 3, 8);
		hor3.addPoint(9, 3, 9);
		hor3.addPoint(10, 3, 10);
		hor3.addPoint(11, 3, 11);
		hor3.addPoint(12, 3, 12);
		horizontal.addExample(hor1);
		horizontal.addExample(hor2);
		horizontal.addExample(hor3);
		
		Gesture diag1 = new Gesture();
		diag1.addPoint(1, 1, 1);
		diag1.addPoint(2, 2, 2);
		diag1.addPoint(3, 3, 3);
		diag1.addPoint(4, 4, 4);
		diag1.addPoint(5, 5, 5);
		diag1.addPoint(6, 6, 6);
		Gesture diag2 = new Gesture();
		diag2.addPoint(7, 7, 1);
		diag2.addPoint(8, 8, 2);
		diag2.addPoint(9, 9, 3);
		diag2.addPoint(10, 10, 4);
		diag2.addPoint(11, 11, 5);
		diag2.addPoint(12, 12, 6);
		Gesture diag3 = new Gesture();
		diag3.addPoint(13, 13, 1);
		diag3.addPoint(14, 14, 2);
		diag3.addPoint(15, 15, 3);
		diag3.addPoint(16, 16, 4);
		diag3.addPoint(17, 17, 5);
		diag3.addPoint(18, 18, 6);
		diagonal.addExample(diag1);
		diagonal.addExample(diag2);
		diagonal.addExample(diag3);
		
		Gesture bl1 = new Gesture();
		bl1.addPoint(1, 1, 1);
		bl1.addPoint(2, 2, 2);
		bl1.addPoint(3, 1, 3);
		bl1.addPoint(4, 2, 4);
		bl1.addPoint(5, 1, 5);
		bl1.addPoint(6, 2, 6);
		Gesture bl2 = new Gesture();
		bl2.addPoint(7, 1, 1);
		bl2.addPoint(8, 2, 2);
		bl2.addPoint(9, 1, 3);
		bl2.addPoint(10, 2, 4);
		bl2.addPoint(11, 1, 5);
		bl2.addPoint(12, 2, 6);
		Gesture bl3 = new Gesture();
		bl3.addPoint(13, 1, 1);
		bl3.addPoint(14, 2, 2);
		bl3.addPoint(15, 1, 3);
		bl3.addPoint(16, 2, 4);
		bl3.addPoint(17, 1, 5);
		bl3.addPoint(18, 2, 6);
		brokenLine.addExample(bl1);
		brokenLine.addExample(bl2);
		brokenLine.addExample(bl3);
		
		
		Gesture oct1 = new Gesture();
		oct1.addPoint(1, 1, 1);
		oct1.addPoint(1, 2, 2);
		oct1.addPoint(2, 3, 3);
		oct1.addPoint(3, 3, 4);
		oct1.addPoint(4, 2, 5);
		oct1.addPoint(4, 1, 6);
		oct1.addPoint(3, 0, 7);
		oct1.addPoint(2, 0, 8);
		oct1.addPoint(1, 1, 9);
		Gesture oct2 = new Gesture();
		oct2.addPoint(11, 11, 1);
		oct2.addPoint(11, 12, 2);
		oct2.addPoint(12, 13, 3);
		oct2.addPoint(13, 13, 4);
		oct2.addPoint(14, 12, 5);
		oct2.addPoint(14, 11, 6);
		oct2.addPoint(13, 10, 7);
		oct2.addPoint(12, 10, 8);
		oct2.addPoint(11, 11, 9);
		Gesture oct3 = new Gesture();
		oct3.addPoint(21, 21, 1);
		oct3.addPoint(21, 22, 2);
		oct3.addPoint(22, 23, 3);
		oct3.addPoint(23, 23, 4);
		oct3.addPoint(24, 22, 5);
		oct3.addPoint(24, 21, 6);
		oct3.addPoint(23, 20, 7);
		oct3.addPoint(22, 20, 8);
		oct3.addPoint(21, 21, 9);
		brokenLine.addExample(oct1);
		brokenLine.addExample(oct2);
		brokenLine.addExample(oct3);
		
		Gesture toRecognize = new Gesture();
		/*toRecognize.addPoint(1, 1, 1);
		toRecognize.addPoint(1, 2, 2);
		toRecognize.addPoint(1, 3, 3);
		toRecognize.addPoint(1, 4, 4);
		toRecognize.addPoint(1, 5, 5);
		toRecognize.addPoint(1, 6, 6);
		toRecognize.addPoint(1, 7, 7);
		toRecognize.addPoint(1, 8, 8);
		toRecognize.addPoint(1, 9, 9);
		toRecognize.addPoint(1, 10, 10);
		toRecognize.addPoint(1, 11, 11);
		toRecognize.addPoint(1, 12, 12);*/
		
		List<Dot> dotSet = (new RecordParser("records\\1\\a\\a10.txt")).parse();
		Iterator<Dot> it = dotSet.iterator();
		while(it.hasNext()){
			Dot dot = it.next();
			if(dot.valid)
				toRecognize.addPoint(dot.posX, dot.posY, dot.time_millis);
		}
		
		Classifier classif = new Classifier();
		//classif.addClass(horizontal);
		//classif.addClass(vertical);
		//classif.addClass(diagonal);
		//classif.addClass(brokenLine);
		//classif.addClass(octogon);
		classif.addClass(a);
		classif.addClass(i);
		classif.addClass(s);
	
		classif.compile();
		System.out.println(classif.classify(toRecognize).name);
	}
}
