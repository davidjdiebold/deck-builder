package com.dd.model;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/7/14
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Stat
{
    private double _count;
    private double _score;
    private double _powerUsed;
    private double _tooMuchCardsDrawn;
    private double _manaNotUSed;

    public Stat()
    {
    }

    @Override
    public String toString() {
        return "" + _score ;
    }

    public Stat(final double score, final double tooMuchCardsDrawn, final double count, final double powerUsed, double manaNotUSed)
    {
        _score = score;
        _tooMuchCardsDrawn = tooMuchCardsDrawn;
        _count = count;
        _powerUsed = powerUsed;
        _manaNotUSed = manaNotUSed;
    }

    public double getScore()
    {
        return _score;
    }

    public double getTooMuchCardsDrawn()
    {
        return _tooMuchCardsDrawn;
    }

    public Stat merge(Stat stat)
    {
        double newCount = this.getCount() + stat.getCount();
        return new Stat(
                (this.getScore() * this.getCount() + stat.getScore() * stat.getCount()) / newCount,
                (this.getTooMuchCardsDrawn() * this.getCount() + stat.getTooMuchCardsDrawn() * stat.getCount()) / newCount,
                newCount,
                (this._powerUsed * this._count + stat._powerUsed * stat._count) / (this._count + stat._count),
                (this._manaNotUSed * this._count + stat._manaNotUSed * stat._count) / (this._count + stat._count)
                );
    }

    public double getManaNotUSed()
    {
        return _manaNotUSed;
    }

    public double getCount()
    {
        return _count;
    }

    public double getPowerUsed()
    {
        return _powerUsed;
    }
}
