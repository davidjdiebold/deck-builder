package com.dd.usc;


import com.dd.model.Manacurve;
import com.dd.model.Stat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class OptimizeCurveResults
{
    private Map<Manacurve, Stat> _results = new LinkedHashMap<Manacurve, com.dd.model.Stat>();

    private int _advancement;

    public Map<Manacurve, Stat> getResults()
    {
        return _results;
    }

    public int getAdvancement()
    {
        return _advancement;
    }

    public void setAdvancement(final int advancement)
    {
        _advancement = advancement;
    }
}
