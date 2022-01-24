package com.dd.model;


import com.dd.builder.Configuration;

import java.util.*;


/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:09
 */
public class Library
{

    private Map<Card, Integer> _cards = new LinkedHashMap<Card, Integer>();

    public Library()
    {
        this(true);
    }

    public Library(boolean fill)
    {
        if (fill)
        {
            CardFactory factory = new CardFactory();
            add(factory.newMinion("Wisp", 0, 1, 1));
            add(factory.newMinion("Mana Wyrm", 1, 2, 1));
            add(factory.newMinion("Defia Ringleader", 2, 3, 2));
            add(factory.newMinion("Shattered sun cleric", 3, 4, 3));
            add(factory.newMinion("Chillwind Yeti", 4, 4, 5));
            add(factory.newMinion("Venture Co Mercenary", 5, 7, 6));
            add(factory.newMinion("Boulderfist ogre", 6, 6, 5));
            add(factory.newMinion("War golem", 7, 8, 8));
        }
    }

    public void add(Card card)
    {
        add(card, 2);
    }

    public void add(Card card, int quantity)
    {
        Integer current = _cards.get(card);
        int toPut = current == null ? quantity : quantity + current;
        if(toPut<0)
        {
            throw new RuntimeException("Card count cannot be negative.");
        }
        _cards.put(card, toPut);
    }

    public void remove(Card card, int quantity)
    {
        add(card, -quantity);
    }

    public Card get(int index)
    {
        return (Card) _cards.keySet().toArray()[index];
    }

    public int getCount(Card card)
    {
        Integer ret = _cards.get(card);
        return ret == null ? 0 : ret;
    }

    public Card[] listCardsSortedByRawRate(int cost, Configuration configuration)
    {
        SortedSet<Card> set = new TreeSet<Card>(new CardComparatorByRate(0, new CardPowersDummy(), new Manacurve(8), configuration));
        for(Card c : _cards.keySet())
        {
            if(c.getCost()==cost)
            {
                set.add(c);
            }
        }
        return set.toArray(new Card[set.size()]);
    }

    public int getDifferentCardsCount()
    {
        return _cards.size();
    }

    public Library copy()
    {
        Library ret = new Library(false);
        for(Card card : _cards.keySet())
        {
            ret.add(card, getCount(card));
        }
        return ret;
    }
}
