package com.dd.builder;

import com.dd.model.Manacurve;

public interface CurveBuilder
{
    public void reset();

    Manacurve buildRandomCurve();

    public void hint(Manacurve curve, double score);
}
