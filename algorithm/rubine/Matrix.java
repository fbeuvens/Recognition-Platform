package algorithm.rubine;
import java.io.PrintStream;
import java.io.Serializable;

// Referenced classes of package rubine:
//            BitVector

class Matrix
    implements Serializable
{

    public Matrix(int r, int c, boolean clear)
    {
        debugInvertMatrix = false;
        nRows = r;
        nCols = c;
        items = new double[r][c];
        if(clear)
            clear();
    }

    public Matrix(int r, int c)
    {
        this(r, c, false);
    }

    public Matrix(Matrix m)
    {
        debugInvertMatrix = false;
        nRows = m.nRows;
        nCols = m.nCols;
        items = new double[nRows][nCols];
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                items[i][j] = m.items[i][j];

        }

    }

    public void clear()
    {
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                items[i][j] = 0.0D;

        }

    }

    public void fill(double d)
    {
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                items[i][j] = d;

        }

    }

    public Matrix copy(Matrix m)
    {
        if(nRows != m.nRows || nCols != m.nCols)
        {
            nRows = m.nRows;
            nCols = m.nCols;
            items = new double[nRows][nCols];
        }
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                items[i][j] = m.items[i][j];

        }

        return this;
    }

    public Matrix multiplyByScalar(double d)
    {
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                items[i][j] *= d;

        }

        return this;
    }

    public Matrix add(Matrix m)
    {
        int r = nRows >= m.nRows ? m.nRows : nRows;
        int c = nCols >= m.nCols ? m.nCols : nCols;
        for(int i = 0; i < r; i++)
        {
            for(int j = 0; j < c; j++)
                items[i][j] += items[i][j];

        }

        return this;
    }

    public Matrix mult(Matrix m)
    {
        if(nCols != m.nRows)
            System.err.println("Matrix Multiply: Different sizes\n");
        Matrix res = new Matrix(nRows, m.nCols);
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < m.nCols; j++)
            {
                double sum = 0.0D;
                for(int k = 0; k < nCols; k++)
                    sum += items[i][k] * m.items[k][j];

                res.items[i][j] = sum;
            }

        }

        return res;
    }

    public String toString()
    {
        String res = "M " + nRows + " " + nCols + "\n";
        for(int i = 0; i < nRows; i++)
        {
            for(int j = 0; j < nCols; j++)
                res = res + " " + items[i][j];

            res = res + "\n";
        }

        res = res + "\n";
        return res;
    }

    public void print()
    {
        System.out.println(toString());
    }

    public Matrix invert(double rdet[])
    {
        int l[] = new int[PERMBUFSIZE];
        int m[] = new int[PERMBUFSIZE];
        if(nRows != nCols)
            System.err.println("InvertMatrix: not square");
        Matrix rm = this;
        if(debugInvertMatrix)
            print();
        if(nRows >= PERMBUFSIZE)
            System.err.println("InvertMatrix: PERMBUFSIZE");
        double det = 1.0D;
        for(int k = 0; k < nRows; k++)
        {
            l[k] = k;
            m[k] = k;
            double biga = rm.items[k][k];
            int i;
            int j;
            for(i = k; i < nRows; i++)
                for(j = k; j < nRows; j++)
                    if(Math.abs(rm.items[i][j]) > Math.abs(biga))
                    {
                        biga = rm.items[i][j];
                        l[k] = i;
                        m[k] = j;
                    }


            if(debugInvertMatrix && biga == 0.0D)
                print();
            i = l[k];
            if(i > k)
                for(j = 0; j < nRows; j++)
                {
                    double hold = -rm.items[k][j];
                    rm.items[k][j] = rm.items[i][j];
                    rm.items[i][j] = hold;
                }

            j = m[k];
            if(j > k)
                for(i = 0; i < nRows; i++)
                {
                    double hold = -rm.items[i][k];
                    rm.items[i][k] = rm.items[i][j];
                    rm.items[i][j] = hold;
                }

            if(biga == 0.0D)
            {
                rdet[0] = 0.0D;
                return rm;
            }
            if(debugInvertMatrix)
                System.out.println("biga = " + biga + "\n");
            double recip_biga = 1.0D / biga;
            for(i = 0; i < nRows; i++)
                if(i != k)
                    rm.items[i][k] *= -recip_biga;

            for(i = 0; i < nRows; i++)
                if(i != k)
                {
                    double hold = rm.items[i][k];
                    for(j = 0; j < nRows; j++)
                        if(j != k)
                            rm.items[i][j] += hold * rm.items[k][j];

                }

            for(j = 0; j < nRows; j++)
                if(j != k)
                    rm.items[k][j] *= recip_biga;

            det *= biga;
            if(debugInvertMatrix)
                System.out.println("det = " + det + "\n");
            rm.items[k][k] = recip_biga;
        }

        for(int k = nRows - 1; k >= 0; k--)
        {
            int i = l[k];
            int j;
            if(i > k)
                for(j = 0; j < nRows; j++)
                {
                    double hold = rm.items[j][k];
                    rm.items[j][k] = -rm.items[j][i];
                    rm.items[j][i] = hold;
                }

            j = m[k];
            if(j > k)
                for(i = 0; i < nRows; i++)
                {
                    double hold = rm.items[k][i];
                    rm.items[k][i] = -rm.items[j][i];
                    rm.items[j][i] = hold;
                }

        }

        if(debugInvertMatrix)
            System.out.println("returning, det = " + det + "\n");
        rdet[0] = det;
        return rm;
    }

    public Matrix slice(BitVector rowmask, BitVector colmask)
    {
        Matrix r = new Matrix(rowmask.bitCount(nRows), colmask.bitCount(nCols));
        int ri;
        for(int i = ri = 0; i < nRows; i++)
            if(rowmask.isSet(i))
            {
                int rj;
                for(int j = rj = 0; j < nCols; j++)
                    if(colmask.isSet(j))
                        r.items[ri][rj++] = items[i][j];

                ri++;
            }

        return r;
    }

    public Matrix deSlice(double fill, int nrow, int ncol, BitVector rowmask, BitVector colmask)
    {
        Matrix r = new Matrix(nrow, ncol);
        r.fill(fill);
        int ri;
        for(int i = ri = 0; i < nrow; i++)
            if(rowmask.isSet(i))
            {
                int rj;
                for(int j = rj = 0; j < ncol; j++)
                    if(colmask.isSet(j))
                        r.items[i][j] = items[ri][rj++];

                ri++;
            }

        return r;
    }

    private static final long serialVersionUID = 1L;
    double items[][];
    int nRows;
    int nCols;
    public static int PERMBUFSIZE = 200;
    boolean debugInvertMatrix;

}