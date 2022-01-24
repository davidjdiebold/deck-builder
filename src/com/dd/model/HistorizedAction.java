package com.dd.model;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 18:16
 */
public interface HistorizedAction
{
    public int getTurn();
    
    public String getName();
    
    public String getPlayerName();

    public String getDescription();
}
