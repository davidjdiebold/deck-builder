package com.dd.model;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 18:18
 */
public class ActionPlayCard extends HistorizedActionA implements HistorizedAction
{
    private final Card _card;
    
    protected ActionPlayCard(int turn, PlayerImpl player, Card card)
    {
        super(turn, "Play Card", player.getName());
        _card = card;
    }

    public Card getCard()
    {
        return _card;
    }

    @Override
    public String getDescription()
    {
        return "  " + getPlayerName() + " has played " + _card.getName();
    }
}
