package com.dd.maths;

/**
 * User: DD
 * Date: 01/12/13
 * Time: 21:07
 */
public class Maths
{
    public static double gauss(double mean, double std, double val)
    {
        return 1 / Math.sqrt(2*Math.PI) / std * Math.exp(-0.5 * (mean-val) * (mean - val) / std / std);
    }
}
