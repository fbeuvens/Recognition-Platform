package algorithm.rubine;
class BitVector
{

    public BitVector()
    {
        direct = 0L;
        size = 32;
    }

    public BitVector(int s)
    {
        direct = 0L;
        size = s;
    }

    public void zero()
    {
        direct = 0L;
    }

    public void fill()
    {
        direct = -1L;
    }

    public void set(int b)
    {
        direct |= 1L << b;
    }

    public void clear(int b)
    {
        direct &= ~(1L << b);
    }

    public int size()
    {
        return size;
    }

    public boolean isSet(int b)
    {
        return (direct >> b & 1L) > 0L;
    }

    public boolean isVoid()
    {
        return direct == 0L;
    }

    BitVector OrEq(BitVector v)
    {
        direct |= v.direct;
        return this;
    }

    BitVector AndEq(BitVector v)
    {
        direct &= v.direct;
        return this;
    }

    public int bitCount(int max)
    {
        int count = 0;
        for(int i = 0; i < max && i < size; i++)
            if(isSet(i))
                count++;

        return count;
    }

    public String toString()
    {
        String res = "";
        for(int i = 0; i < size; i++)
            if(isSet(i))
                res = res + "1";
            else
                res = res + "0";

        return res;
    }

    long direct;
    int size;


    public static void main (String[] args){
    	long test = 0;
    	test |= 1;
    	System.out.println(test);
    }

}
