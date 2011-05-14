package algorithm.rubine;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Dot;

import algorithm.oneDollar.Recognizer;



// Referenced classes of package rubine:
//            DVector, Classifier

public class Gesture
{
 
    public Gesture()
    {
        classifier = null;
        nPoints = 0;
        start = NullPoint;
        delta = NullPoint;
        end = NullPoint;
        min = NullPoint;
        max = NullPoint;
        compiledData = new DVector(13);
        reset();
    }

    public Gesture(Classifier c)
    {
        this();
        classifier = c;
    }

    public void reset()
    {
        nPoints = 0;
        initial_sin = initial_cos = 0.0D;
        maxSpeed = 0.0D;
        length = 0.0D;
        rotation = 0.0D;
        abs_th = 0.0D;
        compiledData.clear();
    }

    public Gesture addPoint(double x, double y, long millis)
    {
        addPoint(((Point2D) (new java.awt.geom.Point2D.Double(x, y))), millis);
        return this;
    }
    
    public void addGesture(List<Dot> gesture, boolean resample, boolean rotate, boolean scale, boolean translate){
    	long startTime = -1;
    	long endTime = -1;
    	double maxSpeed = -1;
    	int maxPressure = -1;
    	int minAngle = -1;
    	int maxAngle = -1;
    	int startPressure = -1;
    	int endPressure = -1;
    	ArrayList<Point> alp = new ArrayList<Point>();
		Iterator<Dot> it = gesture.iterator();
		if(it.hasNext()){
			Dot current = it.next();
			startTime = endTime = current.time_millis;
			//startPressure = endPressure = current.pressure;
			minAngle = maxAngle = current.angle;
			if(current.valid){
				alp.add(new Point(current.posX,current.posY));
			}
			while(it.hasNext()){
				Dot last = current;				
				current = it.next();	
				if(current.valid){
					alp.add(new Point(current.posX,current.posY));
					endTime = current.time_millis;
					//endPressure = current.pressure;
					if(current.angle < minAngle)
						minAngle = current.angle;
					if(current.angle > maxAngle)
						maxAngle = current.angle;
					double v = Math.sqrt((current.posX-last.posX)*(current.posX-last.posX) + (current.posY-last.posY)*(current.posY-last.posY))/(double)(current.time_millis - last.time_millis);
					if(current.time_millis > last.time_millis && v > maxSpeed)
						maxSpeed = v;
					//if(current.angle-last.angle > maxPressure)
						//maxPressure = current.angle-last.angle;
				}
			}
		}

		if(resample)
			alp = algorithm.oneDollar.Utils.Resample(alp, algorithm.oneDollar.Recognizer.NumResamplePoints);

		if(rotate){
			double radians = algorithm.oneDollar.Utils.AngleInRadians(algorithm.oneDollar.Utils.Centroid(alp), alp.get(0), false);
			alp = algorithm.oneDollar.Utils.RotateByRadians(alp, -radians);
		}

		if(scale)
			alp = algorithm.oneDollar.Utils.ScaleTo(alp, algorithm.oneDollar.Recognizer.ResampleScale);

		if(translate)
			alp = algorithm.oneDollar.Utils.TranslateCentroidTo(alp, Recognizer.ResampleOrigin);


		Iterator<Point> it2 = alp.iterator();

		Point current2; 

		while(it2.hasNext()){
			current2 = it2.next();
			addPoint(current2,0);
		}
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxSpeed = maxSpeed;
		this.maxPressure = maxAngle - minAngle;
    }

    void addPoint(Point2D p, long t)
    {
        if(++nPoints == 1)
        {
            startTime = endTime = t;
            start = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
            end = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
            min = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
            max = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
            delta = new java.awt.geom.Point2D.Double(0.0D, 0.0D);
            return;
        }
        Point2D del = new java.awt.geom.Point2D.Double(p.getX() - end.getX(), p.getY() - end.getY());
        magsq = del.getX() * del.getX() + del.getY() * del.getY();
        if(magsq <= dist_sq_threshold)
        {
            nPoints--;
            return;
        }
        if(p.getX() < min.getX())
            min.setLocation(p.getX(), min.getY());
        if(p.getX() > max.getX())
            max.setLocation(p.getX(), max.getY());
        if(p.getY() < min.getY())
            min.setLocation(min.getX(), p.getY());
        if(p.getY() > max.getY())
            max.setLocation(max.getX(), p.getY());
        long lasttime = endTime;
        endTime = t;
        double dist = Math.sqrt(magsq);
        length += dist;
        if(nPoints == 3)
        {
            double dx = p.getX() - start.getX();
            double dy = p.getY() - start.getY();
            double dist2 = dx * dx + dy * dy;
            if(dist2 > dist_sq_threshold)
            {
                double d = Math.sqrt(dist2);
                initial_cos = dx / d;
                initial_sin = dy / d;
            }
        }
        if(nPoints >= 3)
        {
            double t1 = del.getX() * delta.getY() - delta.getX() * del.getY();
            double t2 = del.getX() * delta.getX() + del.getY() * delta.getY();
            double th = Math.atan2(t1, t2);
            double absth = th >= 0.0D ? th : -th;
            rotation += th;
            abs_th += absth;
            sharpness += th * th;
            double v = dist / (double)(endTime - lasttime);
            if(endTime > lasttime && v > maxSpeed)
                maxSpeed = v;
        }
        end.setLocation(p.getX(), p.getY());
        delta.setLocation(del.getX(), del.getY());
    }

    DVector compute()
    {
        if(nPoints <= 1)
            return compiledData;
        compiledData.set(PF_INIT_COS, initial_cos);
        compiledData.set(PF_INIT_SIN, initial_sin);
        double t1 = max.getX() - min.getX();
        double t2 = max.getY() - min.getY();
        double bblen = Math.sqrt(t1 * t1 + t2 * t2);
        compiledData.set(PF_BB_LEN, bblen);
        if(bblen * bblen > dist_sq_threshold)
        {
            double tmp = Math.atan2(t2, t1);
            compiledData.set(PF_BB_TH, tmp);
        }
        t1 = end.getX() - start.getX();
        t2 = end.getY() - start.getY();
        double selen = Math.sqrt(t1 * t1 + t2 * t2);
        compiledData.set(PF_SE_LEN, selen);
        double factor = (selen * selen) / se_th_rolloff;
        if(factor > 1.0D)
            factor = 1.0D;
        factor = selen <= 0.0001D ? 0.0D : factor / selen;
        compiledData.set(PF_SE_COS, (end.getX() - start.getX()) * factor);
        compiledData.set(PF_SE_SIN, (end.getY() - start.getY()) * factor);
        compiledData.set(PF_LEN, length);
        compiledData.set(PF_TH, rotation);
        compiledData.set(PF_ATH, abs_th);
        compiledData.set(PF_SQTH, sharpness);
        compiledData.set(PF_DUR, (double)(endTime - startTime) * 0.01D);
        compiledData.set(12, maxSpeed * 10000D);
        //compiledData.set(13, maxPressure);
        return compiledData;
    }

    long getDuration()
    {
        return endTime - startTime;
    }

    Point2D GetMin()
    {
        return min;
    }

    Point2D GetMax()
    {
        return max;
    }

    Point2D GetStart()
    {
        return start;
    }

    Point2D GetEnd()
    {
        return end;
    }

    double GetLength()
    {
        return length;
    }

    double GetMaxSpeed()
    {
        return maxSpeed;
    }

    static int PF_INIT_COS = 0;
    static int PF_INIT_SIN = 1;
    static int PF_BB_LEN = 2;
    static int PF_BB_TH = 3;
    static int PF_SE_LEN = 4;
    static int PF_SE_COS = 5;
    static int PF_SE_SIN = 6;
    static int PF_LEN = 7;
    static int PF_TH = 8;
    static int PF_ATH = 9;
    static int PF_SQTH = 10;
    static int PF_DUR = 11;
    static final int PF_MAXV = 12;
    static final int NFEATURES = 13;
    int nPoints;
    Point2D start;
    long startTime;
    Point2D delta;
    double magsq;
    double initial_sin;
    double initial_cos;
    Point2D end;
    long endTime;
    Point2D min;
    Point2D max;
    double length;
    double rotation;
    double abs_th;
    double sharpness;
    double maxSpeed;
    int maxPressure;
    DVector compiledData;
    static double dist_sq_threshold = 9D;
    static double se_th_rolloff = 16D;
    static Point NullPoint = new Point(0, 0);
    static final double epsilon = 0.0001D;
    transient Classifier classifier;
    
    public static void main(String[] args){
    	System.out.println(Math.atan2(0,0));
    }
}
