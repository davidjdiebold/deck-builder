package com.dd.builder;

import com.dd.maths.CompositeDomain;
import com.dd.maths.Domain;
import com.dd.maths.Maths;
import com.dd.maths.SimpleDomain;
import com.dd.model.Manacurve;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 * User: DD
 * Date: 01/12/13
 * Time: 21:05
 */
public class Model
{
    private final Configuration _configuration;
    private Manacurve _minimum;
    private Manacurve _maximum;
    private SimpleDomain[] _domain;

    public Model(Manacurve minimum, Configuration configuration)
    {
        _configuration = configuration;
        _minimum = minimum;

        _maximum = new Manacurve(8);
        _maximum.setCurve(new Double[]{2., 40., 40., 40., 40., 40., 40., 40.});
        _domain = new SimpleDomain[]{
                new SimpleDomain(0., 7., 4),
                new SimpleDomain(1., 7., 4),
                new SimpleDomain(0., 7., 4),
                new SimpleDomain(1., 7., 4),
                new SimpleDomain(0., 1., 4)};
    }

    public SimpleDomain[] getDomain()
    {
        return _domain;
    }

    public Domain buildDomain()
    {

        return new CompositeDomain(_domain);
    }
    private Set<Manacurve> _created = new HashSet<Manacurve>();

    public Manacurve buildCurve(double[] parameters)
    {
        //HACK 8 slots
        double sum = 0.;
        Manacurve ret = new Manacurve(8);
        for(int i = 0 ; i < ret.getMaxCost() ; i++)
        {
            ret.setCount(i, Maths.gauss(parameters[0], parameters[1], i));
            ret.setCount(i, ret.getCount(i) + parameters[4] * Maths.gauss(parameters[2], parameters[3], i));
            sum += ret.getCount(i);
        }

        Manacurve rr = CurveUtils.normalize(ret, _minimum, _maximum, _configuration);
        _created.add(rr);

        return rr;
    }
}
