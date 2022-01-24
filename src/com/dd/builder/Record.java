package com.dd.builder;

/**
 * Created by IntelliJ IDEA.
 * User: DD
 * Date: 21/12/13
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class Record
{
    double[] parameters;
    double score;
    boolean localOptimum;

    public Record()
    {
    }

    Record(double[] parameters, double score, boolean localOptimum)
    {
        this.parameters = parameters;
        this.score = score;
        this.localOptimum = localOptimum;
    }

    public void setLocalOptimum(boolean localOptimum)
    {
        this.localOptimum = localOptimum;
    }

    public double[] getParameters()
    {
        return parameters;
    }

    public double getScore()
    {
        return score;
    }

    public boolean isLocalOptimum()
    {
        return localOptimum;
    }
}
