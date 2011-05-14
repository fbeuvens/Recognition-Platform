package algorithm.oneDollar;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Dot;
import data.RecordParser;

import algorithm.rubine.Gesture;


public class Test {
	
	public static void main(String[] args){
		
		Recognizer reco = new Recognizer(new File(""));
		
		for(int j = 1; j < 8; j++){
			ArrayList<Point> points = new ArrayList<Point>();
			List<Dot> dotSet = (new RecordParser("records\\1\\four\\four"+j+".txt")).parse();
			//System.out.println("dotset : "+dotSet.size());
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					points.add(new Point(dot.posX, dot.posY));
			}
			//System.out.println("points : "+points.size());
			reco.AddTemplate("four", points);
		}
		
		for(int j = 1; j < 5; j++){
			ArrayList<Point> points = new ArrayList<Point>();
			List<Dot> dotSet = (new RecordParser("records\\1\\a\\a"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					points.add(new Point(dot.posX, dot.posY));
			}
		//	reco.AddTemplate("a", points);
		}
		
		
		
		for(int j = 1; j < 5; j++){
			ArrayList<Point> points = new ArrayList<Point>();
			List<Dot> dotSet = (new RecordParser("records\\1\\c\\c"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					points.add(new Point(dot.posX, dot.posY));
			}
			//reco.AddTemplate("c", points);
		}
		
		for(int j = 1; j < 5; j++){
			ArrayList<Point> points = new ArrayList<Point>();
			List<Dot> dotSet = (new RecordParser("records\\1\\d\\d"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					points.add(new Point(dot.posX, dot.posY));
			}
			//reco.AddTemplate("d", points);
		}
		
		for(int j = 1; j < 8; j++){
			ArrayList<Point> points = new ArrayList<Point>();
			List<Dot> dotSet = (new RecordParser("records\\1\\three\\three"+j+".txt")).parse();
			Iterator<Dot> it = dotSet.iterator();
			while(it.hasNext()){
				Dot dot = it.next();
				if(dot.valid)
					points.add(new Point(dot.posX, dot.posY));
			}
			reco.AddTemplate("three", points);
		}
		
		ArrayList<Point> toRecognize = new ArrayList<Point>();
		List<Dot> dotSet = (new RecordParser("records\\1\\four\\four1.txt")).parse();
		Iterator<Dot> it = dotSet.iterator();
		while(it.hasNext()){
			Dot dot = it.next();
			if(dot.valid)
				toRecognize.add(new Point(dot.posX, dot.posY));
		}
		//System.out.println(toRecognize.size());
		System.out.println(reco.Recognize(toRecognize));
		
		
	}

}
