package com.dd.maths;

import java.util.Iterator;

/**
 * User: DD
 * Date: 02/12/13
 * Time: 20:03
 */
public class SimpleDomain implements Domain
{
    private double _min;
    private double _max;
    private int _nbSteps;

    public SimpleDomain()
    {
    }

    public SimpleDomain(double min, double max, int nbSteps)
    {
        _min = min;
        _max = max;
        _nbSteps = nbSteps;
    }

    @Override
    public ExtendedIterator<double[]> buildIterator()
    {
        double step = getStep();
        return new SimpleDomainIterator(step);
    }

    public double getStep()
    {
        return (_max - _min) / (_nbSteps - 1);
    }

    @Override
    public int size()
    {
        return _nbSteps;
    }
    
    class SimpleDomainIterator implements ExtendedIterator<double[]>
    {
        private int _index = 0;
        private final double _step;

        SimpleDomainIterator(double step)
        {
            _step = step;
        }

        @Override
        public boolean hasNext()
        {
            return _index < _nbSteps;
        }

        @Override
        public double[] next()
        {
            double[] ret = new double[]{_min + _step * _index};
            ++_index;
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
            return new double[]{_min + _step * (_index - 1)};
        }
    }
}
