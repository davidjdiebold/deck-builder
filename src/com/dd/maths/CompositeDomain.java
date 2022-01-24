package com.dd.maths;

import java.util.Iterator;

/**
 * User: DD
 * Date: 02/12/13
 * Time: 20:11
 */
public class CompositeDomain implements Domain
{
    private final Domain[] _domains;

    public CompositeDomain(Domain[] domains)
    {
        _domains = domains;
    }


    @Override
    public ExtendedIterator<double[]> buildIterator()
    {
        ExtendedIterator[] iterators = new ExtendedIterator[_domains.length];
        for(int i = 0 ; i < _domains.length ; i++)
        {
            iterators[i] = _domains[i].buildIterator();
            if(i>0)
            {
                iterators[i].next();
            }
        }

        return new CompositeIterator(iterators);
    }

    @Override
    public int size()
    {
        int size = 1;
        for(Domain domain : _domains)
        {
            size *= domain.size();
        }
        return size;
    }
    
    class CompositeIterator implements ExtendedIterator<double[]>
    {
        private ExtendedIterator<double[]>[] _iterators;

        CompositeIterator(ExtendedIterator<double[]>[] iterators)
        {
            _iterators = iterators;
        }

        @Override
        public boolean hasNext()
        {
            int index = 0;
            while (index < _iterators.length && !_iterators[index].hasNext())
            {
                ++index;
            }
            return index!=_iterators.length;
        }

        @Override
        public double[] next()
        {
            double[] ret = new double[_iterators.length];
            int index = 0;
            while (!_iterators[index].hasNext())
            {
                ++index;
            }
            _iterators[index].next();

            for (int i = 0; i < index; i++)
            {
                _iterators[i] = _domains[i].buildIterator();
               // if(i>0)
                //{
                    _iterators[i].next();
                //}
            }
            
            for(int i = 0 ; i < ret.length ; i++)
            {
                ret[i] = _iterators[i].current()[0];
            }
            return ret;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] current()
        {
            double[] ret = new double[_iterators.length];

            for(int i = 0 ; i < ret.length ; i++)
            {
                ret[i] = _iterators[i].current()[0];
            }
            return ret;        
        }
    }
}
