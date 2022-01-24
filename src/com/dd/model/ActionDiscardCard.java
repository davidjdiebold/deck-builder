package com.dd.model;

public class ActionDiscardCard extends HistorizedActionA implements HistorizedAction
{
    private final Card _card;

    protected ActionDiscardCard(int turn, PlayerImpl player, Card card)
    {
        super(turn, "Discard Card", player.getName());
        _card = card;
    }

    public Card getCard()
    {
        return _card;
    }

    @Override
    public String getDescription()
    {
        return "  " + getPlayerName() + " has discarded " + _card.getName();
    }
}
