package com.dd.model;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by DAVID on 29/12/2014.
 */
public class Scenario
{
    private final SortedSet<Success> _successes = new TreeSet<Success>();

    private final double _probability;

    private final int _cardsFixed;

    private final int _cardsDrawn;

    public Scenario(double probability, int cardsFixed, int cardsDrawn)
    {
        _probability = probability;
        _cardsFixed = cardsFixed;
        _cardsDrawn = cardsDrawn;
    }

    public SortedSet<Success> getSuccesses()
    {
        return _successes;
    }

    public double getProbability()
    {
        return _probability;
    }

    public int getCardsFixed()
    {
        return _cardsFixed;
    }

    public int getCardsDrawn()
    {
        return _cardsDrawn;
    }
}
