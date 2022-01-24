package com.dd.model;

public interface CardPowers
{

    /**
     * @param cost
     * @param nbCards
     * @return The average card power of a given slot, when we ask for a given amount of cards.
     * Average power should be decreasing as we ask for more cards.
     */
    public double serve(int cost, int nbCards);
}
