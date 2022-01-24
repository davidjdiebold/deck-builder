package com.dd.ia;

import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;
import com.dd.model.*;
import com.dd.utils.CardsComparatorByCosts;

import java.util.*;

public class GreedyCardPlayingAI implements PlayerAI
{
    private final boolean _usePower;
    private final boolean _isWarlock;

    private final CardPowers _powers;
    private final Manacurve _opponentCurve;

    private final Configuration _configuration;

    private final List<Card> _manaGivers = new ArrayList<Card>();
    private final List<Card> _candidatesWithoutPower = new ArrayList<Card>();
    private final List<Card> _candidatesWithPower = new ArrayList<Card>();
    private final List<Card> _candidatesWithExtraMana = new ArrayList<Card>();


    public GreedyCardPlayingAI(CardPowers powers, boolean usePower, boolean isWarlock, Manacurve opponentCurve, Configuration configuration)
    {
        _configuration = configuration;
        _powers = powers;
        _opponentCurve = opponentCurve;
        _usePower = usePower;
        _isWarlock = isWarlock;
    }

    @Override
    public void playOneTurn(Game game, Player player)
    {
        int extraMana = 0;
        _manaGivers.clear();
        for(Card c : player.getAllCardsInHand())
        {
            if(c.get_extraMana()>0)
            {
                _manaGivers.add(c);
                extraMana += c.get_extraMana() - c.getCost();
            }
        }

        chooseCardsToPlay(player, game, player.getManaAvailable(), _candidatesWithoutPower);
        chooseCardsToPlay(player, game, player.getManaAvailable()-2, _candidatesWithPower);
        _candidatesWithExtraMana.clear();
        int manaLeftWhenInnervate = 1;
        if(extraMana>0)
        {
            manaLeftWhenInnervate = chooseCardsToPlay(player, game, player.getManaAvailable() + extraMana, _candidatesWithExtraMana);
        }
        if(manaLeftWhenInnervate==0)
        {
            for(Card c : _manaGivers)
            {
                player.playCard(c);
            }
        }

        if(!_usePower || (_isWarlock && player.getLife()<=2))
        {
            for(Card card : manaLeftWhenInnervate==0 ? _candidatesWithExtraMana : _candidatesWithoutPower)
            {
                player.playCard(card);
            }
        }
        else
        {
            double score = 0.;
            int turn = game.getTurn();
            for (Card card : _candidatesWithoutPower)
            {
                int nbCardsSameCost = (int) player.getDeckCurve().getCount(card.getCapedCost());
                score += _powers.serve(card.getCapedCost(), nbCardsSameCost) * (game.getNbMaxTurns() - turn);
            }

            double scoreWithPower = 0.;
            for (Card card : _candidatesWithPower)
            {
                int nbCardsSameCost = (int) player.getDeckCurve().getCount(card.getCapedCost());
                scoreWithPower += _powers.serve(card.getCapedCost(), nbCardsSameCost) * (game.getNbMaxTurns() - turn);
            }
            double averagePower = 0.;
            double averageCost = 0.;
            for (int i = 0; i < player.getDeckCurve().getMaxCost(); i++)
            {
                //HACK SIZE
                int nbCardsOfCost = (int) player.getDeckCurve().getCount(i);
                averagePower += _powers.serve(i, nbCardsOfCost) * nbCardsOfCost / _configuration.getDeckSize();
                averageCost += nbCardsOfCost / _configuration.getDeckSize();
            }
            double nbTurnsBeforePlay = averageCost;
            for(Card card : player.getAllCardsInHand())
            {
                nbTurnsBeforePlay += card.getCapedCost();
            }
            nbTurnsBeforePlay /= turn +1;
            int nbCardsCost0 = (int) player.getDeckCurve().getCount(0);
            scoreWithPower += _isWarlock
                    ? averagePower * Math.max(0., game.getNbMaxTurns() - turn - nbTurnsBeforePlay)
                    : _powers.serve(0, nbCardsCost0) * (game.getNbMaxTurns() - turn);

            if (score > scoreWithPower)
            {
                for (Card card : manaLeftWhenInnervate>0 ? _candidatesWithoutPower : _candidatesWithExtraMana)
                {
                    player.playCard(card);
                }
            }
            else
            {
                player.usePower(_isWarlock);
                for (Card card : _candidatesWithPower)
                {
                    player.playCard(card);
                }
            }
        }
    }

    private int chooseCardsToPlay(Player player, Game game, int mana, List<Card> toFill)
    {
        toFill.clear();
        int manaAvailable = mana;
        //approximation, on ne compte pas les cartes
        Collections.sort(player.getAllCardsInHand(), new CardComparatorByRate(game.getTurn(), _powers, _opponentCurve, _configuration));
        for (final Card next : player.getAllCardsInHand())
        {
            if (next.getCapedCost() <= manaAvailable && (next.get_extraMana()==0|| next.getCost()<7))
            {
                toFill.add(next);
                manaAvailable -= next.getCapedCost();
            }
        }
        return manaAvailable;
    }
}
