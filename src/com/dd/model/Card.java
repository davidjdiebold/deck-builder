package com.dd.model;

import com.dd.builder.Configuration;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:01
 */
public interface Card extends Comparable<Card>
{
    int getExtraDefinitiveMana();
    void setExtraDefinitiveMana(int extraDefinitiveMana);

    public String getName();
    public void setName(String name);

    void setRawRating(double rating);
    public double getRawRating();
    public double getRating(int turnPlayed, int nbTimesAlreadyPlayed, CardPowers powers, Manacurve opponentCurve, Configuration configuration);

    public int getCost();
    public void setCost(int cost);
    public int getCapedCost();

    public int getOverload();
    public void setOverload(int overload);

    public int getAoe();
    public void setAoe(int aoe);

    int getPower();
    void setPower(Integer value);

    void setCardsDrawn(Integer value);
    int getCardsDrawn();

    int getHealth();
    void setHealth(Integer value);

    void setCanAttack(boolean canAttack);
    boolean getCanAttack();

    int get_extraMana();
    void set_extraMana(int _extraMana);

    int getRemovalPower();
    void setRemovalPower(int power);

    public String getDescription();

    public boolean isReadonly();
    public void setReadonly(boolean isReadonly);

    int getRemainingLife();

    public Card copy();

    PlayerImpl getOwner();
    void setOwner(PlayerImpl owner);

    public void battlecry(Player player);

    void dealDamage(int aoe);

    void initializeLife();
}
