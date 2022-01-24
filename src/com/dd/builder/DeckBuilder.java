package com.dd.builder;

import com.dd.model.Deck;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:07
 */
public interface DeckBuilder
{
    public static final int DECK_SIZE = 30;

    public void fill(Deck deck);
}
