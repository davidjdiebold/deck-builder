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
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.SingularValueDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DAVID on 27/06/2014.
 */
public class UseCaseEvaluatePowerTurn {

        private final Configuration _configuration;

        private final int NB_RUNS = 10000;
        private final int MAX_MANA_COST = 8;

    public UseCaseEvaluatePowerTurn(Configuration configuration) {
        _configuration = configuration;
    }

    public double[] execute(Library library)
        {
            int nbWinnerA = 0;
            DenseMatrix64F coeffs = new DenseMatrix64F(NB_RUNS, MAX_MANA_COST * (MAX_MANA_COST-1)/2);
            DenseMatrix64F scores = new DenseMatrix64F(NB_RUNS, 1);
            Manacurve opponentCurve = ManacurveFactory.buildClassicMtgDraftCurve();
            for(int i = 0 ; i < NB_RUNS ; i++)
            {
                GameImpl game = runGame(library, opponentCurve);

                for(HistorizedAction action : game.getHistory().listActions())
                {
                    if(action instanceof ActionPlayCard)
                    {
                        ActionPlayCard playCardAction = (ActionPlayCard)action;

                        int cost = playCardAction.getCard().getCost();
                        int t = action.getTurn();

                        int j = Math.min(t,MAX_MANA_COST-1)-cost + (cost-1) * (2 * MAX_MANA_COST-1-cost+1) / 2;

                        double oldValue = 0;
                        try {
                            oldValue = coeffs.get(i, j);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        int sign = game.getWinner().getName().equals(playCardAction.getPlayerName()) ? +1 : -1;

                        double newValue = oldValue + sign;
                        if(playCardAction.getCard().getAoe()==0) {
                            coeffs.set(i, j, newValue);
                        }
                    }
                }
                int life = game.getWinner().getLife();
                nbWinnerA += game.getWinner().getName().equals("Player A") ? +1 : 0;
                scores.set(i, 0, 1.);
            }

            SimpleMatrix scoresWraper = new SimpleMatrix(scores);
            SimpleMatrix wraper = new SimpleMatrix(coeffs);
            SimpleMatrix result = wraper.transpose().mult(wraper).invert().mult(wraper.transpose()).mult(scoresWraper);

            new MatrixIO().write("y.csv", scoresWraper.transpose());
            new MatrixIO().write("x.csv", wraper.transpose());

            eigenvalues(wraper);

            System.out.println("Result : ");
            double[] ret = new double[MAX_MANA_COST * (MAX_MANA_COST-1)/2];
            for(int j = 0 ; j < MAX_MANA_COST * (MAX_MANA_COST-1)/2 ; j++)
            {
                ret[j] = result.get(j,0);
            }

            return ret;
        }

        public GameImpl runGame(Library library, Manacurve opponentCurve)
        {
            GameImpl game = new GameImpl(
                    buildDeck(library, getManacurve2()), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(new CardPowersDummy(), true, false, opponentCurve, _configuration)}),
                    buildDeck(library, getManacurve2()), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(new CardPowersDummy(), true, false, opponentCurve, _configuration)}),
                    new Random(), _configuration);
            game.run();
            return game;
        }

        private Deck buildDeck(Library library, Manacurve curve)
        {
            ArrayList<Card> fixed = new ArrayList<Card>();
            double rnd = Math.random() * 100.;
            if(rnd <13.)
            {
                Card card = new CardFactory().newAoe("Holy Nova", 5, 2);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<31.)
            {
                Card card = new CardFactory().newAoe("Arcane Explosion", 2, 1);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<48.)
            {
                Card card = new CardFactory().newAoe("Explosive Trap", 2, 2);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<57.)
            {
                Card card = new CardFactory().newAoe("Thunder Storm", 3, 3);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<65.)
            {
                Card card = new CardFactory().newAoe("Consecration", 4, 2);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<72.)
            {
                Card card = new CardFactory().newAoe("Whirlwind", 1, 1);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<79.)
            {
                Card card = new CardFactory().newAoe("Druid AOE", 4, 2);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            else if(rnd<86.)
            {
                Card card = new CardFactory().newAoe("Fan of knives", 3, 1);
                fixed.add(card);
                fixed.add(card);
                library.add(card, 2);
            }
            FixedCurveDeckBuilder builder = new FixedCurveDeckBuilder(
                    library, curve, fixed, new Random(), BuildOption.RANDOM, _configuration);
            Deck deck = new Deck();
            builder.fill(deck);
            return deck;
        }

    private Manacurve getManacurve() {
        Manacurve curve = new Manacurve(0., 8);
        curve.setCount(0, 0);
        curve.setCount(1, _configuration.getDeckSize());
        return curve;
    }

    private Manacurve getManacurve2() {
        Manacurve curve = new Manacurve(0., 8);
        curve.setCount(0, 0);
        curve.setCount(1, 7);
        curve.setCount(2, 7);
        curve.setCount(3, 5);
        curve.setCount(4, 4);
        curve.setCount(5, 4);
        curve.setCount(6, 2);
        curve.setCount(7, 1);
        return curve;
    }

    protected void eigenvalues(SimpleMatrix wraper)
        {
            DenseMatrix64F all = wraper.transpose().mult(wraper).getMatrix();
            SingularValueDecomposition<DenseMatrix64F> svd =
                    DecompositionFactory.svd(all.numRows, all.numCols, false, true, false);
            if( !svd.decompose(all) )
                throw new RuntimeException("SVD failed");

            DenseMatrix64F W = svd.getW(null);
            DenseMatrix64F diag = new DenseMatrix64F(W.getNumCols(),1);
            CommonOps.extractDiag(W, diag);
            diag.print();
        }
    }
