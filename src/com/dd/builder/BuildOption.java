package com.dd.builder;


import com.dd.model.Card;
import com.dd.model.CardComparatorByRate;
import com.dd.model.CardPowersDummy;
import com.dd.model.Manacurve;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public enum BuildOption
{
    RANDOM
            {
                @Override
                public Card select(final Card[] cards, Configuration configuration)
                {
                    int index = (int) (Math.random() * cards.length);
                    return cards[index];
                }
            },
    GOOD_RATE_FIRST
            {
                @Override
                public Card select(final Card[] cards, Configuration configuration)
                {
                    TreeSet<Card> set = new TreeSet<Card>(new CardComparatorByRate(0, new CardPowersDummy(), new Manacurve(8), configuration));
                    set.addAll(Arrays.asList(cards));
                    Iterator<Card> iterator = set.iterator();
                    Card ret;
                    do {
                        ret = iterator.next();
                    }
                    while(ret.getRemovalPower()>0 || ret.getCardsDrawn() > 0 || (ret.getExtraDefinitiveMana()>0&&ret.getCost()<6));
                    return ret;
                }
            };

    public abstract Card select(Card[] cards, Configuration configuration);
}
