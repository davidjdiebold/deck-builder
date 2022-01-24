package com.dd.builder;

import com.dd.model.Deck;
import com.dd.model.Manacurve;

import java.util.*;

public class MetropolisHastingsCurveBuilder implements CurveBuilder
{
    private final static int INITIAL_POPULATION_SIZE = 20;
    private final static double TARGET_ACCEPTANCE_RATIO = 0.25;

    private Random _random;
    private RandomCurveBuilder _builder;
    
    private Map<Manacurve, Double> _scores = new HashMap<Manacurve, Double>();
    private double _selectionScore = 0.;
    private double _sumScores = 0.;
    private double _accepted = 0;
    private double _nbTests = 0;
    private double _deviation = 1.0;

    private final Configuration _configuration;

    public MetropolisHastingsCurveBuilder(int maxCost, Random random, Configuration configuration)
    {
        _configuration = configuration;
        _random = random;
        _builder = new RandomCurveBuilder(maxCost, random, configuration);

        _constraints = new Manacurve(8);
        _constraints.setCurve(new Double[]{2.,40.,40.,40.,40.,40.,40.,40.});
    }

    @Override
    public void reset()
    {
        _builder.reset();
        _scores.clear();
        _sumScores = 0.;
        _selectionScore = 0.;
        _accepted = 0;
        _nbTests = 0;
        _deviation = 1.;
    }

    @Override
    public Manacurve buildRandomCurve()
    {
        if(_scores.size()<INITIAL_POPULATION_SIZE)
        {
            return _builder.buildRandomCurve();
        }
        else
        {
            Manacurve baseCurve = selectCurve();
            Manacurve modifiedCurve = modify(baseCurve);
            return modifiedCurve;
        }
    }

    @Override
    public void hint(Manacurve curve, double score)
    {
        if(_scores.size() < INITIAL_POPULATION_SIZE || score > _selectionScore)
        {
            _builder.hint(curve, score);
            double exp = Math.exp(score);
            _scores.put(curve, exp);
            _sumScores += exp;
            if(_scores.size() >= INITIAL_POPULATION_SIZE)
            {
                _accepted += 1.;
            }
        }
        if(_scores.size()>=INITIAL_POPULATION_SIZE)
        {
            _nbTests +=1;
            updateDeviation();
        }
    }

    private void updateDeviation()
    {
        if(_accepted/_nbTests < TARGET_ACCEPTANCE_RATIO)
        {
            _deviation *= 0.9;
        }
        else
        {
            _deviation *= 1.1;
        }
    }

    private Manacurve selectCurve()
    {
        double currentSum = 0.;
        double index = _random.nextDouble() * _sumScores;
        for(Map.Entry<Manacurve,Double> entry : _scores.entrySet())
        {
            currentSum += entry.getValue();
            if(index <= currentSum)
            {
                _selectionScore = Math.log(entry.getValue());
                return entry.getKey();
            }
        }
        return _builder.buildRandomCurve();
    }

    private Manacurve modify(Manacurve baseCurve)
    {
        double sum = 0;
        Manacurve intermediate = baseCurve.copy();
        for(int i = 0 ; i < intermediate.getMaxCost() ; i++)
        {
            intermediate.setCount(i, baseCurve.getCount(i) + Math.abs(gauss(0, _deviation)));
            sum += intermediate.getCount(i);
        }

        Manacurve ret = CurveUtils.normalize(intermediate, new Manacurve(Double.NaN, intermediate.getMaxCost()), _constraints, _configuration);
        return ret;
    }
    
    private Manacurve _constraints;

    private double gauss(double mean, double std)
    {
        double u = _random.nextDouble();
        double theta = -Math.PI + 2 * _random.nextDouble() * Math.PI;

        double R = std * Math.sqrt(-2 * Math.log(u));
        double ret = mean + R * Math.cos(theta);
        return ret;
    }
}
