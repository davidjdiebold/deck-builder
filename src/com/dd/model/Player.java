package com.dd.model;

import com.dd.structures.SetOfElements;

import java.util.List;
import java.util.Set;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 07:00
 */
public interface Player
{
    public int getManaAvailable();

    public int getLife();

    public String getName();


    /**
     * Nothing happens if card cannot be played.
     * @param card
     */
    public void playCard(Card card);

    public Set<Card> listMinionsOnBoard(boolean canAttack);

    public void attack(Card attacker, Card defender);

    public void attack(Card attacker, Player defender);

    public void dealDammage(int dammages);

    public Manacurve getDeckCurve();

    public void usePower(boolean isWarlock);

    void drawCards(int cardsToDraw) throws EmptyDeckException;

    List<Card> getAllCardsInHand();

    void aoe(int aoe);
}
