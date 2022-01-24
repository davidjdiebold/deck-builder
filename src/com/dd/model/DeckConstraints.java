package com.dd.model;


import java.util.List;

public class DeckConstraints
{
    private final Manacurve _curve;

    private final List<Card> _fixedCards;

    public DeckConstraints(Manacurve curve, List<Card> fixedCards)
    {
        _curve = curve;
        _fixedCards = fixedCards;
    }

    public Manacurve getCurve()
    {
        return _curve;
    }

    public List<Card> getFixedCards()
    {
        return _fixedCards;
    }
}
