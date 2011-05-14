package algorithm.rubine;
import java.io.*;
import java.net.URL;

// Referenced classes of package rubine:
//            GestureClass, DVector, Gesture, BitVector, 
//            Matrix, ClassifierNotTrainedException

public class Classifier
    implements Serializable
{

    public Classifier()
    {
        classes = new GestureClass[GestureClass.MAX_CLASSES];
        cnst = new double[GestureClass.MAX_CLASSES];
        weights = new DVector[GestureClass.MAX_CLASSES];
        probabilityThreshold = 1.7976931348623157E+308D;
        mahalanobisThreshold = 1.7976931348623157E+308D;
        currentProbability = 0.0D;
        currentDistance = 0.0D;
    }

  

 
    private double MahalanobisDistance(DVector v, DVector u, Matrix sigma)
    {
        DVector tmp = v.minus(u);
        return tmp.quadraticForm(sigma);
    }

    private void fix(Matrix avgcov)
    {
        double det[] = new double[1];
        BitVector bv = new BitVector();
        bv.zero();
        for(int i = 0; i < 13; i++)
        {
            bv.set(i);
            avgcov.slice(bv, bv).invert(det);
            if(Math.abs(det[0]) <= epsilon)
                bv.clear(i);
        }

        Matrix m = avgcov.slice(bv, bv).invert(det);
        if(Math.abs(det[0]) <= epsilon)
            System.err.println("Can't fix classifier!\n");
        invAvgCov = m.deSlice(0.0D, 13, 13, bv, bv);
    }

    public GestureClass createClass(String name)
    {
        return new GestureClass(name);
    }



    public void save(String filename)
    {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try
        {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void reset()
    {
        if(nClasses == 0)
            return;
        for(int i = 0; i < nClasses; i++)
        {
            classes[i] = null;
            weights[i] = null;
        }

        nClasses = 0;
        compiled = false;
    }

    public void addClass(GestureClass d)
    {
        if(nClasses >= GestureClass.MAX_CLASSES)
        {
            return;
        } else
        {
            classes[nClasses] = d;
            weights[nClasses] = new DVector(13);
            cnst[nClasses] = 0.0D;
            nClasses++;
            compiled = false;
            return;
        }
    }

    public void removeClass(GestureClass d)
    {
        if(nClasses == 0)
            System.err.println("no class in the classifier");
        int i;
        for(i = 0; i < nClasses; i++)
            if(classes[i] == d)
                break;

        for(nClasses--; i < nClasses; i++)
        {
            classes[i] = classes[i + 1];
            weights[i] = weights[i + 1];
            cnst[i] = cnst[i + 1];
        }

        compiled = false;
    }

    public GestureClass findClass(String name)
    {
        GestureClass c[] = classes;
        for(int i = 0; i < nClasses; i++)
            if(name.compareTo(c[i].getName()) == 0)
                return c[i];

        return null;
    }

    void compute()
    {
        if(nClasses == 0)
            return;
        Matrix avgcov = new Matrix(13, 13, true);
        int ne = 0;
        GestureClass d[] = classes;
        for(int nc = 0; nc < nClasses; nc++)
        {
            ne += d[nc].nExamples;
            for(int i = 0; i < 13; i++)
            {
                for(int j = i; j < 13; j++)
                    avgcov.items[i][j] += d[nc].sumCov.items[i][j];
                	//System.out.println(d[nc].sumCov);
            }
        }
          
        int denom = ne - nClasses;
        if(denom <= 0)
        {
            System.out.println("no examples, denom=" + denom + "\n");
            return;
        }
        double oneoverdenom = 1.0D / (double)denom;
        for(int i = 0; i < 13; i++)
        {
            for(int j = i; j < 13; j++)
                avgcov.items[j][i] = avgcov.items[i][j] *= oneoverdenom;

        }

        double det[] = new double[1];
        invAvgCov = avgcov.invert(det);
        if(Math.abs(det[0]) <= epsilon)
            fix(avgcov);
        d = classes;
        DVector w[] = weights;
        double cst[] = cnst;
        for(int nc = 0; nc < nClasses; nc++)
        {
            w[nc] = new DVector(d[nc].average.mult(invAvgCov));
            cst[nc] = -0.5D * w[nc].scalarProduct(d[nc].average);
        }
        compiled = true;
    }

    public void compile()
    {
        if(!compiled)
            compute();
    }

    public GestureClass classify(Gesture g)      
    {
       if(!compiled){
    	System.out.println("Classifier is not trained !");
    	return null;
       }
        DVector fv = g.compute();
        DVector wghts[] = weights;
        double cst[] = cnst;
        GestureClass d[] = classes;
        double values[] = new double[GestureClass.MAX_CLASSES];
        double pv[] = values;
        GestureClass maxclass = null;
        double maxvalue = -1.7976931348623157E+308D;
        for(int nc = 0; nc < nClasses; nc++)
        {
            double value = pv[nc] = wghts[nc].scalarProduct(fv) + cst[nc];
            if(value > maxvalue)
            {
            	maxclass = d[nc];
                maxvalue = value;
            }
        }
        
        
        double denom = 0.0D;
        for(int i = 0; i < nClasses; i++)
        {
            double delta = pv[i] - maxvalue;
            if(delta > -7D)
                denom += Math.exp(delta);
        }

        currentProbability = 1.0D / denom;
        currentDistance = MahalanobisDistance(fv, maxclass.average, invAvgCov);
        //System.out.println(maxclass.name + "  " +currentDistance + "  "+ denom+ " "+currentProbability);
        if(currentProbability < probabilityThreshold && currentDistance < mahalanobisThreshold)
            return maxclass;
        else
            return null;
    }

    private static final long serialVersionUID = 1L;
    int nClasses;
    GestureClass classes[];
    double cnst[];
    DVector weights[];
    Matrix invAvgCov;
    boolean compiled;
    double probabilityThreshold;
    double mahalanobisThreshold;
    static double epsilon = 9.9999999999999995E-007D;
    transient double currentProbability;
    transient double currentDistance;

}