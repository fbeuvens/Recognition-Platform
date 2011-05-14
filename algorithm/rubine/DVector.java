package algorithm.rubine;
import java.io.PrintStream;
import java.io.Serializable;

// Referenced classes of package rubine:
//            Matrix, BitVector

class DVector
    implements Serializable
{

    public DVector(int nbRows)
    {
        nRows = nbRows;
        items = new double[nbRows];
    }

    public DVector(int nbRows, boolean clear)
    {
        nRows = nbRows;
        items = new double[nbRows];
        if(clear)
            clear();
    }

    public DVector(DVector v)
    {
        nRows = v.items.length;
        items = new double[nRows];
        for(int i = 0; i < nRows; i++)
            items[i] = v.items[i];

    }

    public DVector set(int index, double value)
    {
        items[index] = value;
        return this;
    }

    public void clear()
    {
        for(int i = 0; i < nRows; i++)
            items[i] = 0.0D;

    }

    public void fill(double d)
    {
        for(int i = 0; i < nRows; i++)
            items[i] = d;

    }

    public DVector copy(DVector v)
    {
        if(nRows != v.nRows)
        {
            nRows = v.nRows;
            items = new double[nRows];
        }
        for(int i = 0; i < nRows; i++)
            items[i] = v.items[i];

        return this;
    }

    public void multiplyByScalar(double d)
    {
        for(int i = 0; i < nRows; i++)
            items[i] *= d;

    }

    public double scalarProduct(DVector v)
    {
        if(nRows != v.nRows)
            System.err.println("Scalar product: " + nRows + "x" + v.nRows);
        double res = 0.0D;
        for(int i = 0; i < nRows; i++)
            res += items[i] * v.items[i];

        return res;
    }

    public DVector substract(DVector v)
    {
        int dim = nRows >= v.nRows ? v.nRows : nRows;
        for(int i = 0; i < dim; i++)
            items[i] -= v.items[i];

        return this;
    }

    public DVector minus(DVector v)
    {
        int dim = nRows >= v.nRows ? v.nRows : nRows;
        DVector res = new DVector(dim);
        res.copy(this);
        res.substract(v);
        return res;
    }

    public DVector mult(Matrix m)
    {
        if(nRows != m.nRows)
        {
            System.err.println("Vector times Matrix\n");
            return new DVector(0);
        }
        DVector res = new DVector(m.nCols, true);
        for(int i = 0; i < m.nCols; i++)
        {
            for(int j = 0; j < nRows; j++)
                res.items[i] += items[j] * m.items[i][j];

        }

        return res;
    }

    public double quadraticForm(Matrix m)
    {
        double res = 0.0D;
        if(nRows != m.nRows || nRows != m.nCols)
            System.err.println("QuadraticForm: bad matrix size\n");
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nRows; j++)
                res += m.items[i][j] * items[i] * items[j];

        }

        return res;
    }

    public DVector slice(BitVector rowmask)
    {
        DVector res = new DVector(rowmask.bitCount(nRows));
        int ri;
        for(int i = ri = 0; i < nRows; i++)
            if(rowmask.isSet(i))
                res.items[ri++] = items[i];

        return res;
    }

    public String toString()
    {
        String res = "";
        for(int i = 0; i < nRows; i++)
            res = res + " " + items[i] + "\n";

        return res;
    }

    private static final long serialVersionUID = 1L;
    double items[];
    int nRows;
}