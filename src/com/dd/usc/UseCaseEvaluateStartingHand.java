package com.dd.usc;

import com.dd.builder.BuildOption;
import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;
import com.dd.builder.FixedCurveDeckBuilder;
import com.dd.ia.CompositeAI;
import com.dd.ia.GreedyCardPlayingAI;
import com.dd.ia.PlayerAI;
import com.dd.ia.SimpleAttackAI;
import com.dd.model.*;
import com.dd.utils.MatrixIO;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by DAVID on 26/06/2014.
 */
public class UseCaseEvaluateStartingHand extends UseCaseEvaluatePower
{
    private final int NB_RUNS = 10000;
    private final int MAX_MANA_COST = 8;

    public UseCaseEvaluateStartingHand(Configuration configuration) {
        super(configuration);
    }

    public double[] execute(Library library, CardPowers powers)
    {
        DenseMatrix64F coeffs = new DenseMatrix64F(NB_RUNS, MAX_MANA_COST);
        DenseMatrix64F scores = new DenseMatrix64F(NB_RUNS, 1);

        for(int i = 0 ; i < NB_RUNS ; i++)
        {
            GameImpl game = runGame(library, powers);

            int index = 0;
            Iterator<HistorizedAction> it = game.getHistory().listActions().iterator();
            while (it.hasNext() && index < _configuration.getStartingPlayerInitialCards() * 2 +1)
            {
                ActionDrawCard action = (ActionDrawCard)it.next();
                int sign = game.getWinner().getName().equals(action.getPlayerName()) ? +1 : -1;
                int j = action.getCard().getCost();
                double oldvalue = coeffs.get(i, j);
                coeffs.set(i, j, oldvalue+sign);
                index++;
            }

            for(HistorizedAction action : game.getHistory().listActions())
            {
                if(action instanceof ActionPlayCard)
                {
                    ActionPlayCard playCardAction = (ActionPlayCard)action;
                    double oldValue = scores.get(i,0);
                    int t = action.getTurn();
                    int nbTurns = game.getTurn();

                    int sign = game.getWinner().getName().equals(playCardAction.getPlayerName()) ? +1 : -1;

                    double rating = powers.serve(playCardAction.getCard().getCost(), 1);
                    double newValue = oldValue + (nbTurns-t) * sign * rating;
                    scores.set(i, 0, newValue);
                }
            }
        }

        SimpleMatrix scoresWraper = new SimpleMatrix(scores);
        SimpleMatrix wraper = new SimpleMatrix(coeffs);
        SimpleMatrix result = wraper.transpose().mult(wraper).invert().mult(wraper.transpose()).mult(scoresWraper);

        new MatrixIO().write("y.csv", scoresWraper.transpose());
        new MatrixIO().write("x.csv", wraper.transpose());

        eigenvalues(wraper);

        System.out.println("Result : ");
        double[] ret = new double[MAX_MANA_COST];
        for(int j = 0 ; j < MAX_MANA_COST ; j++)
        {
            ret[j] = result.get(j,0);
        }

        double moyenne = 0.;
        Manacurve curve = getManacurve();
        for (int i = 0 ; i < MAX_MANA_COST ; i++)
        {
            moyenne += ret[i] * curve.getCount(i) / _configuration.getDeckSize();
        }
        System.out.println("Moyenne : " + moyenne);

        return ret;
    }

    public GameImpl runGame(Library library, CardPowers powers)
    {
        Manacurve opponentCurve = ManacurveFactory.buildClassicMtgDraftCurve();
        GameImpl game = new GameImpl(
                buildDeck(library), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(powers, true, false, opponentCurve, _configuration)}),
                buildDeck(library), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(powers, true, false, opponentCurve, _configuration)}),
                new Random(), _configuration);
        game.run();
        return game;
    }


    private Deck buildDeck(Library library)
    {
        Manacurve curve = getManacurve();
        ArrayList<Card> fixed = new ArrayList<Card>();

        FixedCurveDeckBuilder builder = new FixedCurveDeckBuilder(
                library, curve, fixed, new Random(), BuildOption.GOOD_RATE_FIRST, _configuration);
        Deck deck = new Deck();
        builder.fill(deck);
        return deck;
    }

    private Manacurve getManacurve() {
        Manacurve curve = new Manacurve(0., 8);
        curve.setCount(0, 2);
        curve.setCount(1, 9);
        curve.setCount(2, 7);
        curve.setCount(3, 3);
        curve.setCount(4, 4);
        curve.setCount(5, 3);
        curve.setCount(6, 1);
        curve.setCount(7, 2);
        return curve;
    }
}
