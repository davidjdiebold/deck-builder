package com.dd.builder;

import com.dd.model.Card;
import com.dd.model.Deck;
import com.dd.model.Library;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:08
 */
public class RandomDeckBuilder implements DeckBuilder
{
    private final Library _library;
    private final Configuration _configuration;

    public RandomDeckBuilder(Library library, Configuration configuration)
    {
        _library = library;
        _configuration = configuration;
    }

    @Override
    public void fill(Deck deck)
    {
        while(deck.size()< _configuration.getDeckSize())
        {
            int index = (int)(Math.random() * _library.getDifferentCardsCount());
            Card card = _library.get(index).copy();
            deck.add(card);
        }
    }
}
