package com.dd.ia;

import com.dd.model.Game;
import com.dd.model.Card;
import com.dd.model.Player;
import com.dd.utils.MinionComparatorByPower;

import java.util.*;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 10:58
 */
public class SimpleAttackAI implements PlayerAI
{
    private Set<Card> _available = new LinkedHashSet<Card>();
    private Set<Card> _notDealtWith = new LinkedHashSet<Card>();
    private Set<Card> _willBeRemaining= new LinkedHashSet<Card>();
    private Map<Card, Card> _engagedMinions = new LinkedHashMap<Card, Card>();

    @Override
    public void playOneTurn(Game game, Player player)
    {
        Player opponent = game.getOpponent(player);

        //int timeToPlayAll = computeTimeToPlayAllCards()

        if (getArmyPower(player.listMinionsOnBoard(false)) >= opponent.getLife())
        {
            for (Card minion : player.listMinionsOnBoard(true))
            {
                player.attack(minion, opponent);
            }
        }
        else
        {
            _available.clear();
            _notDealtWith.clear();
            _engagedMinions.clear();
            _willBeRemaining.clear();

            _available.addAll(player.listMinionsOnBoard(true));
            _notDealtWith.addAll(opponent.listMinionsOnBoard(false));
            _willBeRemaining.addAll(_available);

            int turnsToPlayHand = computeTurnsToPlayHand(game, player);

            boolean nothingToDo = false;
            while (getLifePowersProduct(player, _willBeRemaining) < getLifePowersProduct(opponent, _notDealtWith) && !nothingToDo)
            {
                SortedSet<Card> owners = new TreeSet<Card>(new MinionComparatorByPower(false));
                owners.addAll(findOwningCreatures(_available, _notDealtWith));
                boolean riskToDieSoon = getArmyPower(_notDealtWith) * turnsToPlayHand >= player.getLife();
                Set<Card> tradeableCreatures = findTradeableCreatures(_available, _notDealtWith, riskToDieSoon);
                //On ne cherche que � avoir du card advantage si on a assez de points de vie
                if (!owners.isEmpty() && (!riskToDieSoon))
                {
                    Card owner = owners.iterator().next();
                    SortedSet<Card> owned = new TreeSet<Card>(new MinionComparatorByPower(true));
                    owned.addAll(findCreaturesOwnedBy(owner, _notDealtWith));
                    Card selected = owned.iterator().next();
                    _available.remove(owner);
                    _notDealtWith.remove(selected);
                    _engagedMinions.put(owner, selected);
                }
                else if (!tradeableCreatures.isEmpty())
                {
                    SortedSet<Card> tradeable = new TreeSet<Card>(new MinionComparatorByPower(false));
                    tradeable.addAll(tradeableCreatures);
                    Card a = tradeable.iterator().next();
                    Card other = findCreatureTradeableWith(a, _notDealtWith, riskToDieSoon);
                    _available.remove(a);
                    _notDealtWith.remove(other);
                    _willBeRemaining.remove(a);
                    _engagedMinions.put(a, other);
                }
                else
                {
                    nothingToDo = true;
                }
            
            }
            
            //Si une cr�ature peut se faire owner au tour suivant, lui faire faire le plus de degats possibles,
            //quitte � �changer plusieurs cartes
            for(Card minion : new LinkedHashSet<Card>(_available))
            {
                Set<Card> owningCreatures = findOwningCreatures(_notDealtWith, Collections.singleton(minion));
                Set<Card> tradableCreatures= findTradeableCreatures(_notDealtWith, Collections.singleton(minion), false);
                if(!owningCreatures.isEmpty() || !tradableCreatures.isEmpty())
                {
                    Card toKill = null;
                    for(Card m : _notDealtWith)
                    {
                        if(m.getRemainingLife() <= minion.getPower() && (toKill==null || toKill.getPower()<m.getPower()))
                        {
                            if(!owningCreatures.isEmpty() || minion.getPower() < m.getPower())
                            {
                                toKill = m;
                            }
                        }
                    }
                    if(toKill!=null)
                    {
                        _available.remove(minion);
                        _notDealtWith.remove(toKill);
                        _engagedMinions.put(minion, toKill);
                        _willBeRemaining.remove(minion);
                    }
                }
            }
            //TODO Echange avec plusieurs cartes

            for (Map.Entry<Card, Card> entry : _engagedMinions.entrySet())
            {
                player.attack(entry.getKey(), entry.getValue());
            }
            for (Card m : _available)
            {
                player.attack(m, opponent);
            }
        }
    }

    private int computeTurnsToPlayHand(Game game, Player player) {
        int turn = game.getTurn();
        double sumCosts = 0;
        for(Card c : player.getAllCardsInHand())
        {
            sumCosts += c.getCost();
        }
        double delta = (2*turn+1) * (2*turn+1) - 4 * 2 * (turn - sumCosts);
        return (int) Math.ceil((Math.sqrt(delta) - (2*turn+1)) / 2.);
    }

    private Set<Card> findOwningCreatures(Set<Card> minions, Set<Card> otherMinions)
    {
        Set<Card> ret = new LinkedHashSet<Card>();
        for (Card m : minions)
        {
            if (!findCreaturesOwnedBy(m, otherMinions).isEmpty())
            {
                ret.add(m);
            }
        }

        return ret;
    }

    private Set<Card> findCreaturesOwnedBy(Card minion, Set<Card> minions)
    {
        Set<Card> ret = new LinkedHashSet<Card>();
        for (Card m : minions)
        {
            int turnsToKill = (int) Math.ceil(((double) m.getRemainingLife()) / ((double)minion.getPower()));
            if (minion.getRemainingLife() > turnsToKill * m.getPower())
            {
                ret.add(m);
            }
        }
        return ret;
    }
    
    private Set<Card> findTradeableCreatures(Set<Card> minions, Set<Card> otherMinions, boolean acceptAnyTrade)
    {
        Set<Card> ret = new LinkedHashSet<Card>();
        for (Card m : minions)
        {
            Card other = findCreatureTradeableWith(m, otherMinions, acceptAnyTrade);
            if(other!=null)
            {
                ret.add(m);
            }
        }

        return ret;
    }
    
    private Card findCreatureTradeableWith(Card minion, Set<Card> minions, boolean acceptAnyTrade)
    {
        Card bestChoice = null;
        for(Card m : minions)
        {
            int turnsToKill = (int) Math.ceil(((double)m.getRemainingLife()) / ((double)minion.getPower()));

            if(minion.getRemainingLife() > m.getPower() * (turnsToKill-1) && (acceptAnyTrade || minion.getPower() < m.getPower()))
            {
                if(bestChoice==null || bestChoice.getPower() < m.getPower())
                {
                    bestChoice = m;
                }
            }
        }
        return bestChoice;
    }

    private int getLifePowersProduct(Player player, Set<Card> minions)
    {
        int myLife = player.getLife();
        int powers = getArmyPower(minions);
        return myLife * powers;
    }

    private int getArmyPower(Set<Card> minions)
    {

        int powers = 0;
        for (Card minion : minions)
        {
            powers += minion.getPower();
        }
        return powers;
    }
}
