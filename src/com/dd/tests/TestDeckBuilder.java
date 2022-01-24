package com.dd.tests;

import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;
import com.dd.builder.RandomDeckBuilder;
import com.dd.model.Deck;
import com.dd.model.Library;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:26
 */
public class TestDeckBuilder
{
    public void test()
    {
        Library library = new Library();
        DeckBuilder builder = new RandomDeckBuilder(library, Configuration.HEARTHSTONE);
        Deck deck = new Deck();
        builder.fill(deck);
        deck.print();
    }
}
