package com.dd.model;

/**
 * User: DD
 * Date: 05/11/13
 * Time: 20:39
 */
public class ActionPowerUse extends HistorizedActionA implements HistorizedAction
{
    protected ActionPowerUse(int turn, PlayerImpl player)
    {
        super(turn, "Warlock Power", player.getName());
    }

    @Override
    public String getDescription()
    {
        return "  " + getPlayerName() + " has used Warlock Power";
    }
}
