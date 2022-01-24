package com.dd.builder;

import com.dd.model.Manacurve;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * User: DD
 * Date: 01/12/13
 * Time: 21:12
 */
public class CurveUtils
{
    public static Manacurve normalize(Manacurve curve, Manacurve minimum, Manacurve maximum, Configuration configuration)
    {
        Manacurve floatCurve = floatNormalize(curve, minimum, maximum, configuration);

        double sumFloor = 0;
        SortedSet<IndexAndDouble> map = new TreeSet<IndexAndDouble>();
        for(int i = 0 ; i < curve.getMaxCost() ; i++)
        {
            map.add(new IndexAndDouble(i, floatCurve.getCount(i)));
            sumFloor += Math.floor(floatCurve.getCount(i));
        }

        double nbCeil = configuration.getDeckSize() - sumFloor;
        Manacurve ret = floatCurve.copy();
        for(IndexAndDouble entry : map)
        {
            double val = entry._double;
            if(nbCeil > 0 && val!=Math.ceil(val))
            {
                ret.setCount(entry._index, Math.ceil(val));
                nbCeil--;
            }
            else
            {
                ret.setCount(entry._index, Math.floor(val));
            }
        }
        
        if(Double.isNaN(ret.getCount(0)))
        {
            int test = 0;
        }

        return ret;
    }
    
    private static Manacurve floatNormalize(Manacurve shape, Manacurve minimum, Manacurve maximum, Configuration configuration)
    {
        Manacurve ret;
        Manacurve imposed = new Manacurve(Double.NaN, shape.getMaxCost());
        double sumImposed;

        do
        {
            sumImposed = imposed.sum();
            ret = toXCards(shape, imposed, configuration);
            for (int i = 0; i < shape.getMaxCost(); i++)
            {
                if (ret.getCount(i) < minimum.getCount(i))
                {
                    imposed.setCount(i, minimum.getCount(i));
                }
                if (ret.getCount(i) > maximum.getCount(i))
                {
                    imposed.setCount(i, maximum.getCount(i));
                }
            }
        }
        while (sumImposed < imposed.sum());

        return ret; 
    }

    private static Manacurve toXCards(Manacurve curve, Manacurve imposed, Configuration configuration)
    {
        Manacurve ret = curve.copy();
        double sum = 0.;
        for(int i = 0 ; i < ret.getMaxCost() ; i++)
        {
            if(Double.isNaN(imposed.getCount(i)))
            {
                sum += curve.getCount(i);
            }
        }
        double imposedSum = imposed.sum(); 
        
        for(int i = 0 ; i < curve.getMaxCost() ; i++)
        {
            if(!Double.isNaN(imposed.getCount(i)))
            {
                ret.setCount(i, imposed.getCount(i));
            }
            else
            {
                ret.setCount(i, curve.getCount(i) * (configuration.getDeckSize() - imposedSum) / sum);
            }
        }
        return ret;
    }

    static class IndexAndDouble implements Comparable<IndexAndDouble>
    {
        private int _index;
        private double _double;

        IndexAndDouble(int index, double aDouble)
        {
            _index = index;
            _double = aDouble;
        }

        @Override
        public int compareTo(IndexAndDouble o)
        {
            double delta = (this._double - Math.floor(this._double)) - (o._double - Math.floor(o._double));
            if(delta<0.)
            {
                return -1;
            }
            if(delta>0.)
            {
                return +1;
            }
            return - this._index + o._index;
        }
    }

}
