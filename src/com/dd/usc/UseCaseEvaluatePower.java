package com.dd.usc;

import com.dd.builder.*;
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
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 18:12
 */
public class UseCaseEvaluatePower
{
    private final int NB_RUNS = 10000;
    private final int MAX_MANA_COST = 8;

    protected final Configuration _configuration;

    public UseCaseEvaluatePower(Configuration configuration) {
        _configuration = configuration;
    }

    public double[] execute(Library library)
    {
        DenseMatrix64F coeffs = new DenseMatrix64F(NB_RUNS, MAX_MANA_COST * 2);
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

                            int j = playCardAction.getCard().getCost() + MAX_MANA_COST *
                                                                         (playCardAction.getPlayerName().equals("Player A") ? 0 : 1);
                            double oldValue = coeffs.get(i,j);
                            int t = action.getTurn();
                            int nbTurns = game.getTurn();

                            int sign = game.getWinner().getName().equals(playCardAction.getPlayerName()) ? +1 : -1;
                            
                            double newValue = oldValue + (nbTurns-t) * sign;
                            coeffs.set(i, j, newValue);
                    }
            }
            int life = game.getWinner().getLife();
            scores.set(i, 0, life);
        }

        SimpleMatrix scoresWraper = new SimpleMatrix(scores);
        SimpleMatrix wraper = new SimpleMatrix(coeffs);
        SimpleMatrix result = wraper.transpose().mult(wraper).invert().mult(wraper.transpose()).mult(scoresWraper);

        new MatrixIO().write("y.csv", scoresWraper.transpose());
        new MatrixIO().write("x.csv", wraper.transpose());

        eigenvalues(wraper);

        System.out.println("Result : ");
        double[] ret = new double[2*MAX_MANA_COST];
        for(int j = 0 ; j < 2*MAX_MANA_COST ; j++)
        {
            ret[j] = result.get(j,0);
        }

        return ret;
    }

    public GameImpl runGame(Library library, Manacurve opponentCurve)
    {
        GameImpl game = new GameImpl(
                buildDeck(library), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(new CardPowersDummy(), true, false, opponentCurve, _configuration)}),
                buildDeck(library), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(new CardPowersDummy(), true, false, opponentCurve, _configuration)}),
                new Random(), _configuration);

        game.run();
        return game;
    }

    private Deck buildDeck(Library library)
    {
        Manacurve curve = new Manacurve(0., 8);
        curve.setCount(0, 1);
        curve.setCount(1, 6);
        curve.setCount(2, 7);
        curve.setCount(3, 5);
        curve.setCount(4, 4);
        curve.setCount(5, 4);
        curve.setCount(6, 2);
        curve.setCount(7, 1);
        ArrayList<Card> fixed = new ArrayList<Card>();
        if(Math.random()*100.<100.)
        {
            Card card = new CardFactory().newAoe("Holy Nova", 5, 2);
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
