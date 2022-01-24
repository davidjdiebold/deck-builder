package com.dd.model;

import java.util.*;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:02
 */
public class Deck
{
    private boolean _curveDeprecated = true;
    private Manacurve _curve;
    private Queue<Card> _cards = new LinkedList<Card>();
    private List<Card> _removed = new ArrayList<Card>();

    public void add(Card card)
    {
        add(card, 1);
    }

    public void add(Card card, int quantity)
    {
        _curveDeprecated = true;
        for(int i = 0 ; i < quantity; i++)
        {
            _cards.add(card);
        }
    }

    public int size()
    {
        return _cards.size();
    }

    public void print()
    {
        for(Card card : _cards)
        {
            System.out.println(card.getDescription());
        }
    }

    public void regatherAllCards()
    {
        _cards.addAll(_removed);
        _removed.clear();
    }

    public void shuffle(Random random)
    {
        ArrayList<Card> list = new ArrayList<Card>(_cards);
        Collections.shuffle(list, random);
        _cards=new LinkedList<Card>(list);
    }

    public List<Card> pull(int cardsToDraw)
    {
        List<Card> ret = new ArrayList<Card>();
        int i = cardsToDraw;
        while(i > 0)
        {
            Card card = _cards.poll();
            _removed.add(card);
            ret.add(card);
            --i;
        }
        return ret;
    }
    
    public Manacurve getCurve()
    {
        if(_curveDeprecated)
        {
            computeCurve();
            _curveDeprecated = false;
        }
        return _curve;
    }

    private void computeCurve()
    {
        _curve = new Manacurve(0., 8);
        for(Card card : _cards)
        {
            int cost = card.getCapedCost();
            _curve.setCount(cost, _curve.getCount(cost)+1);
        }
    }

    public List<Card> toList()
    {
        return new ArrayList<Card>(_cards);
    }
}
