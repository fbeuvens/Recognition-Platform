package algorithm.oneDollar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import algorithm.rubine.GestureClass;


public class Recognizer {
	//
	// Recognizer class constants
	//
	final int NumTemplates = 16;
	final int NumPoints = 64;
	final double SquareSize = 250.0;
	final double HalfDiagonal = 0.5 * Math.sqrt(250.0 * 250.0 + 250.0 * 250.0);
	final double AngleRange = 45.0;
	final double AnglePrecision = 2.0;
	final double Phi = 0.5 * (-1.0 + Math.sqrt(5.0)); // Golden Ratio
	ArrayList<Gesture> Templates = new ArrayList<Gesture>();
	private File directory;
	private static int DX = 250;
	public static Dimension ResampleScale = new Dimension(DX, DX);
	public static Point ResampleOrigin = new Point(0, 0);
	public static int NumResamplePoints = 64;

	//
	// Recognizer class
	//
	public Recognizer(File file) // constructor
	{

		directory = file;
		loadDotSets();
	}

	public Result Recognize(ArrayList <Point> points) {
	
		points = Utils.Resample(points, NumPoints);
		double radians = Utils.AngleInRadians(Utils.Centroid(points), points
				.get(0), false);
		points = Utils.RotateByRadians(points, -radians);
		
		points = Utils.ScaleTo(points, ResampleScale);
		/*for(int i = 0; i < points.size(); i++){
			System.out.println("newPoints : "+points.get(i));
		}*/
		points = Utils.TranslateCentroidTo(points, ResampleOrigin);
	
		ArrayList<Result> result = new ArrayList<Result>();
		
		
		
		for (int i = 0; i < Templates.size(); i++) {
			double[] best = GoldenSectionSearch(points,
					Templates.get(i).Points, Utils.Deg2Rad(-45.0), Utils
							.Deg2Rad(+45.0), Utils.Deg2Rad(2.0));
			
			result.add(new Result(Templates.get(i).Name, 1d - best[0] / HalfDiagonal));
		}
		Collections.sort(result, new Result());

		System.out.println(result);

		
		if(!result.isEmpty())
		return result.get(0);
		else
			return null;
	}
	
	public String RecognizeKnn(ArrayList <Point> points, int knn) {
	
		points = Utils.Resample(points, NumPoints);
		double radians = Utils.AngleInRadians(Utils.Centroid(points), points
				.get(0), false);
		points = Utils.RotateByRadians(points, -radians);
		
		points = Utils.ScaleTo(points, ResampleScale);
		/*for(int i = 0; i < points.size(); i++){
			System.out.println("newPoints : "+points.get(i));
		}*/
		points = Utils.TranslateCentroidTo(points, ResampleOrigin);
	
		ArrayList<Result> result = new ArrayList<Result>();
		
		
		
		for (int i = 0; i < Templates.size(); i++) {
			double[] best = GoldenSectionSearch(points,
					Templates.get(i).Points, Utils.Deg2Rad(-45.0), Utils
							.Deg2Rad(+45.0), Utils.Deg2Rad(2.0));
			
			result.add(new Result(Templates.get(i).Name, 1d - best[0] / HalfDiagonal));
		}
		Collections.sort(result, new Result());
		Hashtable<String, Integer> numberItemByClass = new Hashtable<String, Integer>();
		for(int i = 0; i < knn; i++){
			String classe = result.get(i).name;
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
	
	public String[] RecognizeForAllKnn(ArrayList <Point> points, int knn) {
				
		points = Utils.Resample(points, NumPoints);
		double radians = Utils.AngleInRadians(Utils.Centroid(points), points
				.get(0), false);
		points = Utils.RotateByRadians(points, -radians);
		
		points = Utils.ScaleTo(points, ResampleScale);
		
		points = Utils.TranslateCentroidTo(points, ResampleOrigin);
	
		ArrayList<Result> result = new ArrayList<Result>();
		
		
		
		for (int i = 0; i < Templates.size(); i++) {
			double[] best = GoldenSectionSearch(points,
					Templates.get(i).Points, Utils.Deg2Rad(-45.0), Utils
							.Deg2Rad(+45.0), Utils.Deg2Rad(2.0));
			
			result.add(new Result(Templates.get(i).Name, 1d - best[0] / HalfDiagonal));
		}
		Collections.sort(result, new Result());
		Object[] numberItemByClass = new Object[knn];
		for(int i = 0; i < knn; i++)
			numberItemByClass[i] = new Hashtable<String, Integer>();
		for(int i = 0; i < knn; i++){
			for(int j = 0; j <= i; j++){
				String classe = result.get(j).name;
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

	private double[] GoldenSectionSearch(ArrayList<Point> pts1, ArrayList<Point> pts2,
			double a, double b, double threshold) {
		double x1 = Phi * a + (1 - Phi) * b;
		ArrayList<Point> newPoints = Utils.RotateByRadians(pts1, x1);
		double fx1 = Utils.PathDistance(newPoints, pts2);
		
		double x2 = (1 - Phi) * a + Phi * b;
		newPoints = Utils.RotateByRadians(pts1, x2);
		double fx2 = Utils.PathDistance(newPoints, pts2);

		

		double i = 2.0; // calls
		while (Math.abs(b - a) > threshold) {
			if (fx1 < fx2) {
				b = x2;
				x2 = x1;
				fx2 = fx1;
				x1 = Phi * a + (1 - Phi) * b;
				newPoints = Utils.RotateByRadians(pts1, x1);
				fx1 = Utils.PathDistance(newPoints, pts2);
			} else {
				a = x1;
				x1 = x2;
				fx1 = fx2;
				x2 = (1 - Phi) * a + Phi * b;
				newPoints = Utils.RotateByRadians(pts1, x2);
				fx2 = Utils.PathDistance(newPoints, pts2);
			}
			i++;
		}
		
		double[] res = { Math.min(fx1, fx2), Utils.Rad2Deg((b + a) / 2.0), i };
		
		return res; // distance, angle, calls to pathdist
	}

	//
	// add/delete new templates
	//
	public void AddTemplate(String name,  ArrayList <Point> points) {
		if(points.size() > 0){
		Gesture t = new Gesture(name, points); // append new template
		Templates.add(t);
		}
		
	}

	private void loadDotSets() {
		File[] files = directory.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(".dat");
			}
		});
		if (files == null)
			return;
		Arrays.sort(files);
		String lastWidget = null;
		for (int i = 0, id = 0; i < files.length; ++i, ++id) {
			String name = files[i].getName();
			name = name.substring(0, name.indexOf('-'));
			if (lastWidget == null || !lastWidget.equals(name)) {
				lastWidget = name;
				id = 0;
			}

			try {
				double[][] _dotSet = loadDotSet(files[i]);
				DotsSet dotSet = new DotsSet(_dotSet, 0, 0);
				double[][] d = dotSet.toArray();
				ArrayList<Point> points = new ArrayList<Point>();
				for (int j = 0; j < _dotSet.length; j++) {
					points.add(new Point((int) d[j][0], (int) d[j][1]));
				}
				Templates.add(new Gesture(name, points));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private double[][] loadDotSet(File file) throws IOException {
		Vector<double[]> result = new Vector<double[]>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			double[] dValues = new double[values.length];
			for (int i = 0; i < values.length; ++i)
				dValues[i] = Double.parseDouble(values[i]);
			result.add(dValues);
		}
		return result.toArray(new double[0][0]);
		
	}

}