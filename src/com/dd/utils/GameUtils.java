package com.dd.utils;

import com.dd.builder.Configuration;
import com.dd.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by DAVID on 26/06/2014.
 */
public class GameUtils
{
    public static Stat evaluate(GameImpl game, CardPowers cardPowers, Manacurve opponentCurve, int turnMax, double heroPowerRating, Configuration configuration)
    {
        double score = 0.;
        double powerused = 0.;
        double usedMana = 0.;
        double getMana = 0.;
        Map<String,Integer> alreadyPlayed = new LinkedHashMap<String, Integer>();
        if(!game.isDeckWasEmptied())
        {
            for (HistorizedAction action : game.getHistory().listActions())
            {
                if (action instanceof ActionPlayCard)
                {
                    ActionPlayCard playCard = (ActionPlayCard) action;
                    int turn = playCard.getTurn();
                    int cost = playCard.getCard().getCapedCost();
                    String cardName = playCard.getCard().getName();
                    Integer nbPlayed = alreadyPlayed.get(cardName);
                    nbPlayed = nbPlayed==null ? 0 : nbPlayed;
                    double rating = playCard.getCard().getRating(turn, nbPlayed, cardPowers, opponentCurve, configuration);
                    Player player = game.getPlayer(playCard.getPlayerName());
                    int nbCardsOfCost = (int) player.getDeckCurve().getCount(cost);
                    double power = Double.isNaN(rating) ? null : rating;
                    score += power * (turnMax - turn);
                    alreadyPlayed.put(cardName, nbPlayed);
                    //System.out.println("Turn " + playCard.getTurn() + " : Player " + player.getName() + " Played " + cardName);
                    usedMana += ((ActionPlayCard) action).getCard().getCost() != 7 ? ((ActionPlayCard) action).getCard().getCost() : 0;
                    if(((ActionPlayCard) action).getCard().getExtraDefinitiveMana()>0)
                    {
                        getMana += ((ActionPlayCard) action).getCard().getExtraDefinitiveMana() * (turnMax - action.getTurn());
                    }

                }
                else if (action instanceof ActionPowerUse)
                {
                    score += heroPowerRating;// * (turnMax - action.getTurn());
                    powerused += 1.;
                    //System.out.println("Turn " + action.getTurn() + " : Player " + action.getPlayerName() +" used power");
                }
            }
        }

        //2 players, division by 2
        return new Stat(score / 2., game.isDeckWasEmptied() ? 1. : 0., 1, powerused / 2., (getMana-usedMana) / 2.);
    }
}
