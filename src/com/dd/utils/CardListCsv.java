package com.dd.utils;


import com.dd.model.Card;
import com.dd.model.CardFactory;

import java.util.Collection;

public class CardListCsv extends CsvIO<Collection<Card>, Card>
{

    private static final String FORMAT_ERROR = "Csv Format was not recognized. You can try to export your library to have an exact idea of the format.";

    private final CardFactory _factory = new CardFactory();

    @Override
    protected boolean hasHeader()
    {
        return true;
    }

    @Override
    protected String[] buildHeader()
    {
        return new String[]{"Name","Cost","Attack","Health","Cards Drawn","Rating"};
    }

    @Override
    protected void preFill(final Collection<Card> container)
    {
    }

    @Override
    protected void fill(final String[] tokens, final Collection<Card> elementToFill)
    {
        if(tokens.length!=6)
        {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }

        String name = tokens[0];
        try
        {
            int cost = (int) Integer.parseInt(tokens[1]);
            int attack = (int) Integer.parseInt(tokens[2]);
            int health = (int) Integer.parseInt(tokens[3]);
            int drawn = (int) Integer.parseInt(tokens[4]);
            double rating = Double.parseDouble(tokens[5]);
            Card card = _factory.newMinion(name, cost, attack, health);
            card.setCardsDrawn(drawn);
            card.setName(name);
            card.setRawRating(rating);
            elementToFill.add(card);
        }
        catch (Throwable t)
        {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }

    @Override
    protected Collection<Card> listRecords(final Collection<Card> container)
    {
        return container;
    }

    @Override
    protected String[] listValues(final Card record)
    {
        return new String[]{
                record.getName().replace(_cvsSplitBy,""),
                ""+record.getCost(),
                ""+record.getPower(),
                ""+record.getRemainingLife(),
                ""+record.getCardsDrawn(),
                ""+record.getRawRating()
        };
    }
}
