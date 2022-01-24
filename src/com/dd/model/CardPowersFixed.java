package com.dd.model;

public class CardPowersFixed implements CardPowers
{
    private double[] _powers;

    public CardPowersFixed(final double[] powers)
    {
        _powers = powers;
    }

    public CardPowersFixed()
    {}

    @Override
    public double serve(final int cost, final int nbCards)
    {
        return cost >= 0 ? _powers[cost] : 0.;
    }
}
