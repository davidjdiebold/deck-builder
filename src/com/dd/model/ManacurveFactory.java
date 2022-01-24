package com.dd.model;

/**
 * Created by DAVID on 14/07/2014.
 */
public class ManacurveFactory
{
    public static Manacurve buildClassicMtgDraftCurve()
    {
        Double[] array = new Double[]{0.,6.,6.,5.,5.,4.,3.,1.};
        return new Manacurve(array);
    }
}
