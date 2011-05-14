package algorithm.rubine;
import java.io.Serializable;

// Referenced classes of package rubine:
//            DVector, Matrix, Gesture

public class GestureClass
    implements Serializable
{

    public GestureClass(String n)
    {
        name = n;
        average = new DVector(13, true);
        sumCov = new Matrix(13, 13, true);
        nExamples = 0;
    }

    public String getName()
    {
        return name;
    }

    public void addExample(Gesture g)
    {
    	
    	double nfv[] = new double[MAXGESTURES];
        DVector fv = g.compute();
        nExamples++;
        double nm1on = ((double)nExamples - 1.0D) / (double)nExamples;
        double recipn = 1.0D / (double)nExamples;
        
        for(int i = 0; i < 13; i++){
            nfv[i] = fv.items[i] - average.items[i];
            //System.out.println(average.items[i]);
        }

        for(int i = 0; i < 13; i++)
        {
            for(int j = i; j < 13; j++){
                sumCov.items[i][j] += nm1on * nfv[i] * nfv[j];
            	//System.out.println(nm1on + " " + nfv[i] * nfv[j]);
            }

        }
        
        
        //System.out.println(recipn);
        for(int i = 0; i < 13; i++)
            average.items[i] = nm1on * average.items[i] + recipn * fv.items[i];

    }

    private static final long serialVersionUID = 1L;
    static int MAX_CLASSES = 50;
    static int MAXGESTURES = 32;
    static int MAXCLASSES = 32;
    String name;
    DVector average;
    Matrix sumCov;
    int nExamples;

}