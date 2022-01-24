package com.dd.model;

/**
 * Created by DAVID on 24/06/2014.
 */
public class CardFactory
{
    public Card newAoe(String name, int cost, int aoeDmg)
    {
        Card spell = new CardImpl();
        spell.setName(name);
        spell.setRawRating(0.);
        spell.setCost(cost);
        spell.setAoe(aoeDmg);
        return spell;
    }

    public Card newMinion(String name, int cost, int attack, int life)
    {
        Card minion = new CardImpl();
        minion.setCost(cost);
        minion.setName(name);
        minion.setPower(attack);
        minion.setHealth(life);
        minion.setRawRating(0.);
        return minion;
    }

    public Card newLand(String name)
    {
        Card land = new CardImpl();
        land.setName(name);
        land.setRawRating(0.0);
        land.setCost(7);
        land.setExtraDefinitiveMana(1);
        return land;
    }

    public Card newMinion(String name, int cost, int attack, int life, double rating)
    {
        Card minion = new CardImpl();
        minion.setCost(cost);
        minion.setName(name);
        minion.setPower(attack);
        minion.setHealth(life);
        minion.setRawRating(rating);
        return minion;
    }

    public Card newSpell(String name, int cost, int extraMana, int aoeDmg)
    {
        Card card = new CardImpl();
        card.setName(name);
        card.setCost(cost);
        card.set_extraMana(extraMana);
        card.setAoe(aoeDmg);
        card.setRawRating(0.);
        return card;
    }
}
