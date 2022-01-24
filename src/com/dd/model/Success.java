package com.dd.model;

/**
 * Created by DAVID on 25/12/2014.
 */
public class Success implements Comparable<Success>
{
    private final String _cardName;

    private final int _turn;

    private final int _expected;

    public Success(String cardName, int turn, int expected)
    {
        _cardName = cardName;
        _turn = turn;
        _expected = expected;
    }

    public String getCardName()
    {
        return _cardName;
    }

    public int getTurn()
    {
        return _turn;
    }

    public int getExpected()
    {
        return _expected;
    }

    @Override
    public int compareTo(Success o)
    {
        int diff = this._turn - o._turn;
        return diff != 0 ? -diff : this._cardName.compareTo(o._cardName);
    }
}
