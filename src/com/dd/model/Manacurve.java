package com.dd.model;

import java.util.Arrays;

public class Manacurve
{
    private Double[] _curve;

    public Manacurve()
    {
    }

    public Manacurve(double value, int length)
    {
        _curve = new Double[length];
        for(int i = 0 ; i < length ; i++)
        {
            _curve[i] = value;
        }
    }
    
    public Manacurve(Double[] curve)
    {
        _curve = curve;
    }

    public Manacurve(int i)
    {
        _curve = new Double[i];
    }
    
    public double sum()
    {
        double sum = 0.;
        for(int  i = 0 ; i < _curve.length ; i++)
        {
            if(!Double.isNaN(_curve[i]))
            {
                sum += _curve[i];
            }
        }
        return sum;
    }

    public void setCurve(Double[] curve)
    {
        _curve =curve;
    }

    public double getCount(int cost)
    {
        return _curve[cost];
    }

    public void setCount(int cost, double nbCards)
    {
        _curve[cost] = nbCards;
    }

    //Excluded
    public int getMaxCost()
    {
        return 8;
    }

    public Manacurve copy()
    {
        Manacurve ret = new Manacurve(getMaxCost());
        for(int i = 0 ; i < getMaxCost() ; i++)
        {
            ret.setCount(i, getCount(i));
        }
        return ret;
    }

    @Override
    public String toString()
    {
        int[] integers = new int[_curve.length];
        for(int i = 0 ; i < _curve.length ; i++)
        {
            integers[i] = (int)_curve[i].doubleValue();
        }

        return Arrays.toString(integers);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Manacurve manacurve = (Manacurve) o;

        if (!Arrays.equals(_curve, manacurve._curve))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return _curve != null ? Arrays.hashCode(_curve) : 0;
    }
}
