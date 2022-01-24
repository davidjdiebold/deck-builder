package com.dd.model;

import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:38
 */
public class CardImpl implements Card
{
    protected String _name;
    protected int _cardsDrawn;
    //Card Definition
    protected int _power;
    protected int _maxLife;

    protected String _class;
    protected String _rarity;
    protected int _cost;
    private int _overload = 0;
    private int _aoe;
    protected double _rating = Double.NaN;
    private int _manaCostRemoved = -1;
    protected boolean _isReadonly = false;
    private int _extraMana = 0;

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public int getExtraDefinitiveMana() {
        return _extraDefinitiveMana;
    }

    @Override
    public void setExtraDefinitiveMana(int extraDefinitiveMana) {
        _extraDefinitiveMana = extraDefinitiveMana;
    }

    private int _extraDefinitiveMana = 0;



    //State in Game
    protected int _remainingLife;
    private boolean _canAttack = false;
    //State in Game
    private PlayerImpl _owner;

    @Override
    public int compareTo(final Card o)
    {
        int delta = this.getCost() - o.getCost();
        if(delta!=0)
        {
            return delta;
        }
        return getName().compareTo(o.getName());
    }


    public CardImpl()
    {
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public int getCost()
    {
        return _cost;
    }

    @Override
    public void setCost(int cost) {
        _cost = cost;
    }

    @Override
    public int getCapedCost()
    {
        return Math.min(_cost, 7);
    }

    @Override
    public PlayerImpl getOwner()
    {
        return _owner;
    }

    @Override
    public void setOwner(PlayerImpl owner)
    {
        _owner = owner;
    }

    @Override
    public String getDescription()
    {
        return _name + "(" + _cost + ")";
    }

    @Override
    public void battlecry(Player player)
    {

        player.drawCards(_cardsDrawn);
        player.aoe(getAoe());
    }

    @Override
    public double getRawRating() {
        return _rating;
    }

    @Override
    public boolean isReadonly()
    {
        return _isReadonly;
    }

    @Override
    public void setReadonly(final boolean isReadonly)
    {
        _isReadonly = isReadonly;
    }

    public double getRating(int turnPlayed, int nbTimesAlreadyPlayed, CardPowers powers, Manacurve opponentCurve, Configuration configuration)
    {
        int maxCostRemoved = Math.min(7, Math.min(_manaCostRemoved, turnPlayed));

        int cardsDrawnByOpponent = turnPlayed + configuration.getStartingPlayerInitialCards();
        int costIndex = maxCostRemoved;
        double cumpProb = 0.;
        while(costIndex>0 && cumpProb<(1.+nbTimesAlreadyPlayed))
        {
            cumpProb += cardsDrawnByOpponent * opponentCurve.getCount(costIndex) / configuration.getDeckSize();
            --costIndex;
        }

        double removalPower = costIndex >= 0 ? powers.serve(costIndex,1) : 0.;
        //HACK
        return removalPower + getRawRating();
//        return getCost()>=6. ? 0. : removalPower + getRawRating() * (1. - (turnPlayed-getCost())/(6.-getCost()));
    }

    @Override
    public void setRawRating(double rating)
    {
        _rating = rating;
    }


    public void setName(final String nameText)
    {
        _name = nameText;
    }

    @Override
    public int getOverload() {
        return _overload;
    }

    @Override
    public void setOverload(int overload) {
        _overload = overload;
    }

    @Override
    public int getAoe() {
        return _aoe;
    }

    @Override
    public void setAoe(int aoe) {
        _aoe = aoe;
    }

    @Override
    public void setCardsDrawn(final Integer value)
    {
        _cardsDrawn = value;
    }


    @Override
    public int getCardsDrawn()
    {
        return _cardsDrawn;
    }

    public int getPower()
    {
        return _power;
    }

    public int getRemainingLife()
    {
        return _remainingLife;
    }

    public void dealDamage(int dammage)
    {
        _remainingLife -= dammage;

        if(_remainingLife<=0)
        {
            getOwner().removeFromBoard(this);
        }
    }

    public boolean getCanAttack()
    {
        return _canAttack;
    }

    public void setCanAttack(boolean canAttack)
    {
        _canAttack = canAttack;
    }

    @Override
    public void setPower(final Integer value)
    {
        _power = value;
    }

    @Override
    public void setHealth(final Integer value)
    {
        _maxLife = value;
    }

    @Override
    public int getHealth()
    {
        return _maxLife;
    }

    @Override
    public Card copy() {
        CardImpl ret = new CardImpl();
        ret._aoe = this._aoe;
        ret._cardsDrawn = this._cardsDrawn;
        ret._class = this._class;
        ret._cost = this._cost;
        ret._isReadonly = this._isReadonly;
        ret._maxLife = this._maxLife;
        ret._name = this._name;
        ret._overload = this._overload;
        ret._power = this._power;
        ret._rarity = this._rarity;

        ret._remainingLife = this._remainingLife;
        ret._owner = this._owner;
        ret._canAttack = this._canAttack;
        return ret;
    }

    @Override
    public int get_extraMana() {
        return _extraMana;
    }

    @Override
    public void initializeLife() {
        _remainingLife = _maxLife;
    }

    @Override
    public void set_extraMana(int _extraMana) {
        this._extraMana = _extraMana;
    }

    @Override
    public int getRemovalPower() {
        return _manaCostRemoved;
    }

    @Override
    public void setRemovalPower(int power) {
        _manaCostRemoved = power;
    }
}
