package com.dd.model;


public class CardPowersDummy implements CardPowers
{

    @Override
    public double serve(final int cost, final int nbCards)
    {
        return cost >= 0 ? cost : 0;
    }
}
