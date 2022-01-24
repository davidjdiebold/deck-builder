package com.dd.builder;

import com.dd.maths.ExtendedIterator;
import com.dd.model.Manacurve;

/**
 * User: DD
 * Date: 01/12/13
 * Time: 21:01
 */
public class ModelCurveBuilder implements CurveBuilder
{
    private Model _model;
    private transient ExtendedIterator<double[]> _domain;

    private double[] _lastParameters;
    private ModelScores _scores;

    public ModelCurveBuilder()
    {
    }

    public ModelCurveBuilder(Manacurve minCards, Configuration configuration)
    {
        _model = new Model(minCards, configuration);
        _scores = new ModelScores(_model.getDomain());
    }

    public Model getModel()
    {
        return _model;
    }

    @Override
    public void reset()
    {
        _domain = _model.buildDomain().buildIterator();
    }

    @Override
    public synchronized Manacurve buildRandomCurve()
    {
        if(!_domain.hasNext())
        {
            reset();
        }

        double[] parameters = _domain.next();
        _lastParameters = parameters;
        return _model.buildCurve(parameters);
    }

    @Override
    public synchronized void hint(Manacurve curve, double score)
    {
        _scores.add(_lastParameters, score);
    }

    public ModelScores getScores()
    {
        return _scores;
    }
}
