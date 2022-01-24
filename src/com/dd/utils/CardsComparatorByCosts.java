package com.dd.utils;

import com.dd.model.Card;

import java.util.Comparator;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 07:03
 */
public class CardsComparatorByCosts implements Comparator<Card>
{
    @Override
    public int compare(Card o1, Card o2)
    {
        int delta = - o1.getCost() + o2.getCost();
        if(delta==0)
        {
            return o1.getName().compareTo(o2.getName());
        }
        else
        {
            return delta;
        }
    }
}
