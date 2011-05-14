/*
 * Created on 20-janv.-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package algorithm.oneDollar;

import java.io.Serializable;
import java.awt.geom.Rectangle2D;

/**
 * @author Adri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DotsSet implements Serializable , Cloneable
{
	private double [][] dotsset;
    private int id;
    private double zoom = 1;
	
	public DotsSet(double [][] dotsset, int id)
	{
		this.dotsset = dotsset;
		this.id = id;
	}
	
	public DotsSet(double [][] dotsset, int id, double zoom)
	{
		this.dotsset = dotsset;
		this.id = id;
		this.zoom = zoom;
	}
	
    public void translate(int x, int y)
    {
        for(int i =0 ; i < dotsset.length; i++) {
            dotsset[i][0] = dotsset[i][0] +x;
            dotsset[i][1] = dotsset[i][1] +y;
        }
    }
    
    public Object clone() {
        try {
            DotsSet ds = (DotsSet) super.clone();
            double [][] res = new double [dotsset.length][dotsset[0].length];
            
            for(int i = 0; i < dotsset.length; i++)
                for(int j = 0; j < dotsset[i].length; j++)
                    res [i][j]= dotsset [i][j];
            
            ds.dotsset = res;
            return ds;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
	public double [][] toArray()
	{
		return dotsset;
	}
	
	public int getId()
	{
		return id;
	}
	
	public double getMinX()
	{
		double min = 10000;
		for(int i = 0; i < dotsset.length; i++)
		{
			if(min > dotsset[i][0])
				min = dotsset[i][0];
		}
		return min;
	}
	
	public double getMinY()
	{
		double min = 10000;
		for(int i = 0; i < dotsset.length; i++)
		{
			if(min > dotsset[i][1])
				min = dotsset[i][1];
		}
		return min;
	}
	
	public double getMaxX()
	{
		double max = -10000;
		for(int i = 0; i < dotsset.length; i++)
		{
			if(max < dotsset[i][0])
				max = dotsset[i][0];
		}
		return max;
	}
	
	public double getMaxY()
	{
		double max = -10000;
		for(int i = 0; i < dotsset.length; i++)
		{
			if(max < dotsset[i][1])
				max = dotsset[i][1];
		}
		return max;
	}
	
	public double getWidth()
	{
		return getMaxX() - getMinX();
	}
	
	public double getZoom()
	{
		return zoom;
	}
	
	public double getHeight()
	{
		return getMaxY() - getMinY();
	}
	
	public Rectangle2D getBounds()
	{
		return new Rectangle2D.Double(getMinX(), getMinY(), Math.max(5, getWidth()), 
                Math.max(5, getHeight()));
	}

    /**
     * @return Returns the dotsset.
     */
    public double[][] getDotsset() {
        return dotsset;
    }

    /**
     * @param dotsset The dotsset to set.
     */
    public void setDotsset(double[][] dotsset) {
        this.dotsset = dotsset;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param zoom The zoom to set.
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
	
	
}
