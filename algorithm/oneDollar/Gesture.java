package algorithm.oneDollar;

import java.awt.Point;
import java.util.ArrayList;

public class Gesture 
	{
        public String Name;
        public ArrayList <Point>RawPoints; // raw points (for drawing) -- read in from XML
        public ArrayList <Point>Points;    // resampled points (for matching) -- done when loaded

		public Gesture()
		{
			this.Name = null;
            this.RawPoints = null;
            this.Points = null;
		}

        // when a new prototype is made, its raw points are resampled into n equidistantly spaced
        // points, then it is scaled to a preset size and translated to a preset origin. this is
        // the same treatment applied to each candidate stroke, and it allows us to thereafter
        // simply step through each point in each stroke and compare those points' distances.
        // in other words, it removes the challenge of determining corresponding points in each gesture.
        // after resampling, scaling, and translating, we compute the "indicative angle" of the 
        // stroke as defined by the angle between its centroid point and first point.
		public Gesture(String name, ArrayList <Point> points)
		{
			this.Name = name;
            this.RawPoints = new ArrayList(points); // copy (saved for drawing)

            // resample first (influences calculation of centroid)
            Points = Utils.Resample(points, Recognizer.NumResamplePoints);
            
            // rotate so that the centroid-to-1st-point is at zero degrees
            double radians = Utils.AngleInRadians(Utils.Centroid(Points), Points.get(0), false);
            Points = Utils.RotateByRadians(Points, -radians); // undo angle
            
            // scale to a common (square) dimension
            Points = Utils.ScaleTo(Points, Recognizer.ResampleScale);

            // finally, translate to a common origin
            Points = Utils.TranslateCentroidTo(Points, Recognizer.ResampleOrigin);
		}

//        public int Duration ()
//        {
//                if (RawPoints.size() >= 2)
//                {
//                    Point p0 = (Point) RawPoints.get(0);
//                    Point pn = (Point) RawPoints.get(RawPoints.size() - 1);
//                    return pn.T - p0.T;
//                }
//                else
//                {
//                    return 0;
//                }
//
//        }

        // sorts in descending order of Score
        public boolean CompareTo(Object obj)
        {
            if (obj instanceof Gesture)
            {
                Gesture g = (Gesture) obj;
                return Name.equalsIgnoreCase(g.Name);
            }
            return false;
        }

        /// <summary>
        /// Pulls the gesture name from the file name, e.g., "circle03" from "C:\gestures\circles\circle03.xml".
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        public static String ParseName(String filename)
        {
            int start = filename.lastIndexOf('\\');
            int end = filename.lastIndexOf('.');
            return filename.substring(start + 1, end - start - 1);
        }

    }