package com.dd.model;

/**
 * User: DD
 * Date: 05/11/13
 * Time: 06:14
 */
public class ActionDrawCard extends HistorizedActionA
{
    private Card _card;

    public ActionDrawCard(int turn, String playerName, Card card)
    {
        super(turn, "Draw Card", playerName);
        _card = card;
    }

    public Card getCard()
    {
        return _card;
    }

    @Override
    public String getDescription()
    {
        return "  " + getPlayerName() + " has drawn " + _card.getDescription();
    }
}
