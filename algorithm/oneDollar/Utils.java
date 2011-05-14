package algorithm.oneDollar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Utils {

	private static Random random= new Random(); 
	public static Rectangle FindBox(ArrayList <Point>points)
	{
			int minX = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
		
			for(int i = 0; i < points.size(); i++)
			{
				if (points.get(i).x < minX)
					minX = points.get(i).x;
				if (points.get(i).x > maxX)
					maxX = points.get(i).x;
			
				if (points.get(i).y < minY)
					minY = points.get(i).y;
				if (points.get(i).y > maxY)
					maxY = points.get(i).y;
			}
		
			return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	public static double Distance(Point p1, Point p2)
	{
			double dx = p2.x - p1.x;
			double dy = p2.y - p1.y;
			return Math.sqrt(dx * dx + dy * dy);
     }

     // compute the centroid of the points given
     public static Point Centroid(ArrayList <Point>points)
     {
         double xsum = 0.0;
         double ysum = 0.0;

         for(int i = 0; i < points.size(); i++)
         {
             xsum += points.get(i).x;
             ysum += points.get(i).y;
         }
         return new Point((int)(xsum / points.size()), (int) (ysum / points.size()));
     }

     public static double PathLength(ArrayList <Point>points)
     {
         double length = 0;
         for(int i = 1; i < points.size(); i++)
         {
             length += Distance(points.get(i - 1), points.get(i));
         }
         return length;
     }



     // determines the angle, in degrees, between two points. the angle is defined 
     // by the circle centered on the start point with a radius to the end point, 
     // where 0 degrees is straight right from start (+x-axis) and 90 degrees is
     // straight down (+y-axis).
     public static double AngleInDegrees(Point start, Point end, boolean positiveOnly)
     {
         double radians = AngleInRadians(start, end, positiveOnly);
         return Rad2Deg(radians);
     }

     // determines the angle, in radians, between two points. the angle is defined 
     // by the circle centered on the start point with a radius to the end point, 
     // where 0 radians is straight right from start (+x-axis) and PI/2 radians is
     // straight down (+y-axis).
		public static double AngleInRadians(Point start, Point end, boolean positiveOnly)
		{
         double radians = 0.0;
         if (start.x != end.x)
         {
             radians = Math.atan2(end.y - start.y, end.x - start.x);
         }
         else // pure vertical movement
         {
             if (end.y < start.y)
                 radians = -Math.PI / 2.0; // -90 degrees is straight up
             else if (end.y > start.y)
                 radians = Math.PI / 2.0; // 90 degrees is straight down
         }
         if (positiveOnly && radians < 0.0)
         {
             radians += Math.PI * 2.0;
         }
         return radians;
		}

     public static double Rad2Deg(double rad)
		{
			return (rad * 180d / Math.PI);
		}

		public static double Deg2Rad(double deg)
		{
			return (deg * Math.PI / 180d);
     }

     // rotate the points by the given degrees about their centroid
     public static ArrayList RotateByDegrees(ArrayList <Point>points, double degrees)
     {
         double radians = Deg2Rad(degrees);
         return RotateByRadians(points, radians);
     }

     // rotate the points by the given radians about their centroid
     public static ArrayList <Point>RotateByRadians(ArrayList <Point>points, double radians)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         Point c = Centroid(points);

         double cos = Math.cos(radians);
         double sin = Math.sin(radians);

         double cx = c.x;
         double cy = c.y;

         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);

             double dx = p.x - cx;
             double dy = p.y - cy;

             Point q = new Point();
             q.x = (int) (dx * cos - dy * sin + cx);
             q.y = (int) (dx * sin + dy * cos + cy);
             
             newPoints.add(q);
         }
         return newPoints;
     }

     // Rotate a point 'p' around a point 'c' by the given radians.
     // Rotation (around the origin) amounts to a 2x2 matrix of the form:
     //
     //		[ cos A		-sin A	] [ p.x ]
     //		[ sin A		cos A	] [ p.y ]
     //
     // Note that the C# Math coordinate system has +x-axis stright right and
     // +y-axis straight down. Rotation is clockwise such that from +x-axis to
     // +y-axis is +90 degrees, from +x-axis to -x-axis is +180 degrees, and 
     // from +x-axis to -y-axis is -90 degrees.
     public static Point RotatePoint(Point p, Point c, double radians)
     {
         Point q = new Point();
         q.x = (int) ((p.x - c.x) * Math.cos(radians) - (p.y - c.y) * Math.sin(radians) + c.x);
         q.y = (int) ((p.x - c.x) * Math.sin(radians) + (p.y - c.y) * Math.cos(radians) + c.y);
         return q;
     }

     // translates the points so that the upper-left corner of their bounding box lies at 'toPt'
     public static ArrayList <Point>TranslateBBoxTo(ArrayList <Point>points, Point toPt)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         Rectangle r = Utils.FindBox(points);
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x += (toPt.x - r.x);
             p.y += (toPt.y - r.y);
             newPoints.add(p);
         }
         return newPoints;
     }

     // translates the points so that their centroid lies at 'toPt'
     public static ArrayList <Point>TranslateCentroidTo(ArrayList <Point>points, Point toPt)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         Point centroid = Centroid(points);
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x += (toPt.x - centroid.x);
             p.y += (toPt.y - centroid.y);
             newPoints.add(p);
         }
         return newPoints;
     }

     // translates the points by the given delta amounts
     public static ArrayList <Point>TranslateBy(ArrayList <Point>points, Dimension sz)
     {
         ArrayList<Point> newPoints = new ArrayList<Point>();
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x += sz.width;
             p.y += sz.height;
             newPoints.add(p);
         }
         return newPoints;
     }


     // scales the points so that they form the size given. does not restore the 
     // origin of the box.
     public static ArrayList<Point> ScaleTo(ArrayList <Point>points, Dimension sz)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         Rectangle r = FindBox(points);
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             if (r.width != 0d)
                 p.x *= ((double)sz.width / r.width);
           
             if (r.height != 0d)
                 p.y *= ((double)sz.height / r.height);
             newPoints.add(p);
         }
         return newPoints;
     }

     // scales by the percentages contained in the 'sz' parameter. values of 1.0 would result in the
     // identity scale (that is, no change).
     public static ArrayList <Point>ScaleBy(ArrayList <Point>points, Dimension sz)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x *= sz.width;
             p.y *= sz.height;
             newPoints.add(p);
         }
         return newPoints;
     }

     // scales the points so that the length of their longer side
     // matches the length of the longer side of the given box.
     // thus, both dimensions are warped proportionally, rather than
     // independently, like in the function ScaleTo.
     public static ArrayList<Point> ScaleToMax(ArrayList <Point>points, Rectangle box)
     {
         ArrayList<Point> newPoints = new ArrayList<Point>();
         Rectangle r = FindBox(points);
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x *= Math.max(box.width, box.height) / Math.max(r.width, r.height);
             p.y *= Math.max(box.width, box.height) / Math.max(r.width, r.height);
             newPoints.add(p);
         }
         return newPoints;
     }

     // scales the points so that the length of their shorter side
     // matches the length of the shorter side of the given box.
     // thus, both dimensions are warped proportionally, rather than
     // independently, like in the function ScaleTo.
     public static ArrayList <Point>ScaleToMin(ArrayList <Point>points, Rectangle box)
     {
         ArrayList <Point>newPoints = new ArrayList<Point>();
         Rectangle r = FindBox(points);
         for (int i = 0; i < points.size(); i++)
         {
             Point p = points.get(i);
             p.x *= Math.min(box.width, box.height) / Math.min(r.width, r.height);
             p.y *= Math.min(box.width, box.height) / Math.min(r.width, r.height);
             newPoints.add(p);
         }
         return newPoints;
     }

     
    /* public static ArrayList<Point> Resample2(ArrayList<ArrayList<Point>> pointsArrays, int n){
    	 int nbrPoints = 0;
    	 ArrayList <Point>dstPts = new ArrayList<Point>();
    	 
    	 for(int i = 0; i < pointsArrays.size(); i++)
    		 nbrPoints += pointsArrays.get(0).size();
    	 
    	 for(int i = 0; i < pointsArrays.size(); i++){
    		 ArrayList<Point> points = pointsArrays.get(i);
    		 ArrayList<Point> resampledPoints = Resample(points,n*(points.size()/nbrPoints));
    		 for(int j = 0; j < resampledPoints.size(); j++)
    			 dstPts.add(resampledPoints.get(j));
    	 }
    	 return dstPts;
    		 
     }*/

     public static ArrayList<Point> Resample(ArrayList <Point>points, int n)
     {
         double I = PathLength(points) / (n - 1); // interval length
         double D = 0.0;
         ArrayList <Point>srcPts = points;
         ArrayList <Point>dstPts = new ArrayList<Point>(n);
         dstPts.add(srcPts.get(0));
         for (int i = 1; i < srcPts.size(); i++)
         {
             Point pt1 = srcPts.get(i - 1);
             Point pt2 = srcPts.get(i);

             double d = Distance(pt1, pt2);
             if ((D + d) >= I)
             {
                 int qx = (int) (pt1.x + ((I - D) / d) * (pt2.x - pt1.x));
                 int qy = (int) (pt1.y + ((I - D) / d) * (pt2.y - pt1.y));
                 Point q = new Point(qx, qy);
                 dstPts.add(q); // append new point 'q'
                 
                 srcPts.add(i, q); // insert 'q' at position i in points s.t. 'q' will be the next i
                 D = 0.0;
             }
             else
             {
                 D += d;
             }
         }
         // somtimes we fall a rounding-error short of adding the last point, so add it if so
         if (dstPts.size() == n - 1)
         {
             dstPts.add(srcPts.get(srcPts.size() - 1));
         }
         
         return dstPts;
     }

     // computes the 'distance' between two point paths by summing their corresponding point distances.
     // assumes that each path has been resampled to the same number of points at the same distance apart.
     public static double PathDistance(ArrayList <Point>path1, ArrayList <Point>path2)
     {            
         double distance = 0;
         for (int i = 0; i < Math.min(path1.size(),path2.size()); i++)
         {
             distance += Distance((Point) path1.get(i), (Point) path2.get(i));
         }
         return distance / path1.size();
     }

     /// <summary>
     /// Gets a random number between low and high, inclusive.
     /// </summary>
     /// <param name="low"></param>
     /// <param name="high"></param>
     /// <returns></returns>
     public static int Random(int low, int high)
     {
         return low + random.nextInt(high + 1);
     }

     /// <summary>
     /// Gets multiple random numbers between low and high, inclusive. The
     /// numbers are guaranteed to be distinct.
     /// </summary>
     /// <param name="low"></param>
     /// <param name="high"></param>
     /// <param name="num"></param>
     /// <returns></returns>
     public static int[] Random(int low, int high, int num)
     {
         int[] array = new int[num];
         for (int i = 0; i < num; i++)
         {
             array[i] = low + random.nextInt(high + 1);
             for (int j = 0; j < i; j++)
             {
                 if (array[i] == array[j])
                 {
                     i--; // redo i
                     break;
                 }
             }
         }
         return array;
     }

}
