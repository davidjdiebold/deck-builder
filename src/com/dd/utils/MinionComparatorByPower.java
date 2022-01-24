package com.dd.utils;

import com.dd.model.Card;

import java.util.Comparator;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 11:55
 */
public class MinionComparatorByPower implements Comparator<Card>
{
    private int sign = 1;

    public MinionComparatorByPower(boolean greaterToLower)
    {
        sign = greaterToLower ? -1 : +1;
    }

    @Override
    public int compare(Card o1, Card o2)
    {
        int delta = sign * o1.getPower() - sign * o2.getPower();
        if (delta != 0)
        {
            return delta;
        } else
        {
            delta = sign * o1.getRemainingLife() - sign * o2.getRemainingLife();
            if (delta != 0)
            {
                return delta;
            } else
            {
                return o1.getName().compareTo(o2.getName());
            }
        }
    }
}
