package com.dd.model;


import com.dd.builder.Configuration;

import java.util.Comparator;
import java.util.Map;


public class CardComparatorByRate implements Comparator<Card>
{
    private final int _turn;
    private final CardPowers _powers;
    private final Manacurve _opponentCurve;
    private final Configuration _configuration;

    public CardComparatorByRate(int turn, CardPowers powers, Manacurve opponentCurve, Configuration configuration) {
        _turn = turn;
        _powers = powers;
        _opponentCurve = opponentCurve;
        _configuration = configuration;
    }

    @Override
    public int compare(final Card o1, final Card o2)
    {
        //HACK
        if(o1.getExtraDefinitiveMana()>0&&o1.getCost()==7)
        {
            return -1;
        }
        if(o2.getExtraDefinitiveMana()>0&&o2.getCost()==7)
        {
            return +1;
        }

        int deltaC = -(o1).getCardsDrawn() - (o2).getCardsDrawn();
        if(deltaC!=0)
        {
            return deltaC;
        }
        double delta =
                - o1.getRating(_turn, 0, _powers, _opponentCurve, _configuration) * 1000
                + o2.getRating(_turn, 0, _powers, _opponentCurve, _configuration) * 1000;
        if(delta!=0)
        {
            return (int) delta;
        }
        else
        {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
