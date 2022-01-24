package com.dd.model;

/**
 * Created by IntelliJ IDEA.
 * User: DD
 * Date: 03/11/13
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class HistorizedActionA implements HistorizedAction 
{
    private final int _turn;
    private final String _name;
    private final String _playerName;

    protected HistorizedActionA(int turn, String name, String playerName)
    {
        _turn = turn;
        _name = name;
        _playerName = playerName;
    }

    @Override
    public int getTurn()
    {
        return _turn;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public String getPlayerName()
    {
        return _playerName;
    }
}
