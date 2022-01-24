package com.dd.model;

/**
 * Created by DAVID on 27/06/2014.
 */
public class CardPowersPhilosophyFire implements CardPowers
{

    @Override
    public double serve(final int cost, final int nbCards)
    {
        return cost >= 0 ? 7 + cost * 2 : 0.;
    }
}
