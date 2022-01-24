package com.dd.usc;


import com.dd.builder.*;
import com.dd.gui.ProgressBar;
import com.dd.ia.CompositeAI;
import com.dd.ia.GreedyCardPlayingAI;
import com.dd.ia.PlayerAI;
import com.dd.model.*;
import com.dd.utils.GameUtils;

import java.util.*;


/**
 * User: DD
 * Date: 04/11/13

 * Time: 06:53
 */
public class UseCaseOptimizeManaCurve
{
    private String _configurationName;
    private boolean _hasBeenRenamed = false;

    private int NB_CURVES;
    private int NB_GAMES = 1000;
    private int NB_MAX_TURNS;

    private String _hero = "Warlock";
    private CurveBuilder _curveBuilder;
    private Random _random;

    private Library _library;
    protected List<Card> _imposedCards;

    private CardPowers _rawPowers;
    private final Manacurve _opponentCurve = ManacurveFactory.buildClassicMtgDraftCurve();

    protected transient ProgressBar _bar;
    protected OptimizeCurveResults _results;

    private boolean _canceled = false;

    protected Configuration _configuration;

    @Override
    public String toString()
    {
        return _configurationName;
    }

    public UseCaseOptimizeManaCurve(
            Library library,
            List<Card> imposedCards,
            int nbTurns,
            CurveBuilder builder,
            Random random,
            int curvesToTest,
            ProgressBar bar, Configuration configuration)
    {
        _configuration = configuration;
        _imposedCards = imposedCards;
        NB_CURVES = curvesToTest;
        NB_MAX_TURNS = nbTurns;
        _random = random;
        _curveBuilder = builder;
        _library = library;
        _results = new OptimizeCurveResults();
        _bar = bar;
        _rawPowers = buildCardPowers();
    }

    public UseCaseOptimizeManaCurve() {
    }

    private CardPowers buildCardPowers()
    {
        double[] array = new double[8];//HACK
        for(int i = 0 ; i < 8 ; i++)
        {
            Card[] cards = _library.listCardsSortedByRawRate(i, _configuration);
            Iterator<Card> it = Arrays.asList(cards).iterator();
            Card old = it.next();
            double lowestRating = old.getRawRating();
            while(it.hasNext())
            {
                Card next = it.next();
                if(
                        next.getRemovalPower()<0 &&
                        next.getCardsDrawn()==0 &&
                        next.getRawRating()<lowestRating &&
                        next.getExtraDefinitiveMana()==0
                        )
                {
                    lowestRating = next.getRawRating();
                    old = next;
                }
            }

            array[i] = old.getRawRating();
        }
        return new CardPowersFixed(array);
    }

    public void setNbGames(final int nbGames)
    {
        NB_GAMES = nbGames;
    }

    public Manacurve execute()
    {
        final double[] bestScore = {Double.NEGATIVE_INFINITY};
        final Manacurve[] bestCurve = {null};
        _curveBuilder.reset();

        int curveIndex = 0;
        while (curveIndex < NB_CURVES && !_canceled)
        {
            final int finalCurveIndex = curveIndex;
            Manacurve curve = _curveBuilder.buildRandomCurve();
            Stat[] scores = evaluate(curve);
            if (!_canceled)
            {
                Stat mean = mean(scores);
                if (mean.getScore() > bestScore[0])
                {
                    System.out.println("Score : " + mean + " +/- " + std(scores));
                    System.out.println(curve);
                    bestCurve[0] = curve;
                    bestScore[0] = mean.getScore();

                    updateScores(curve, mean);
                }
                _curveBuilder.hint(curve, mean.getScore());

                //TODO synchronize
                int percent = finalCurveIndex * 100 / NB_CURVES;
                if (_bar != null)
                {
                    _bar.setAdvancement(percent);
                }
                _results.setAdvancement(percent);
            }
            curveIndex++;
        }
        return bestCurve[0];
    }

    private synchronized void updateScores(final Manacurve curve, final Stat mean)
    {
        Stat oldStats = _results.getResults().get(curve);
        Stat toPut = oldStats != null ? oldStats.merge(mean) : mean;
        _results.getResults().put(curve, toPut);
    }

    public OptimizeCurveResults getResults()
    {
        return _results;
    }

    public Stat[] evaluate(final Manacurve curve)
    {
        final Stat[] scores = new Stat[NB_GAMES];
        Deck deckA = buildDeck(curve);
        Deck deckB = buildDeck(curve);
        for(int i = 0 ; i < NB_GAMES ; i++)
        {
            GameImpl game = runGame(deckA, deckB, i);
            deckA.regatherAllCards();
            deckB.regatherAllCards();
            scores[i] = evaluate(game, i);
        }
        return scores;
    }

    public Stat mean(Stat[] values)
    {
        Stat merged = values[0];
        for (int i = 1; i < values.length; i++)
        {
            merged = merged.merge(values[i]);
        }
        return merged;
    }

    public double std(Stat[] values)
    {
        double m = mean(values).getScore();
        double ret = 0.;
        for (int i = 0; i < values.length; i++)
        {
            double delta = values[i].getScore() - m;
            ret += delta * delta;
        }
        return Math.sqrt(ret / values.length);
    }

    private Stat evaluate(GameImpl game, int index)
    {
        double heroPowerRating = computeHeroPowerRating();
        Stat rowScore = GameUtils.evaluate(game, _rawPowers, _opponentCurve, getTurn(index, getNbGames()), heroPowerRating, _configuration);
        double maxScore = 100.;
        return new Stat(rowScore.getScore()/maxScore*100., rowScore.getTooMuchCardsDrawn(), rowScore.getCount(), rowScore.getPowerUsed(),
                rowScore.getManaNotUSed());
    }

    private double computeHeroPowerRating() {
        if(_configuration.isHearthstone()) {
            return _hero.equals("Warlock") ? 0. : _rawPowers.serve(0, 1);
        } else {
            return 0.0;
        }
    }

    private double getMaxScore(int index)
    {
        int turn = getTurn(index, getNbGames());
        double ret = 0.;
        double maxPower = 0.;
        for (int i = 0; i < turn; i++)
        {
            int cost = Math.min(i,7);
            maxPower = Math.max(maxPower, _rawPowers.serve(cost, 1));
            ret += maxPower;
        }
        return ret;
    }

    public GameImpl runGame(Deck deckA, Deck deckB, int index)
    {
        boolean useWarlock  = _hero.equals("Warlock") && _configuration.isHearthstone();
        GameImpl game = new GameImpl(
                deckA, new CompositeAI(new PlayerAI[]{new GreedyCardPlayingAI(_rawPowers, true, useWarlock, _opponentCurve, _configuration)}),
                deckB, new CompositeAI(new PlayerAI[]{new GreedyCardPlayingAI(_rawPowers, true, useWarlock, _opponentCurve, _configuration)}),
                _random, _configuration);
        game.setNbMaxTurns(getTurn(index,NB_GAMES));
        game.run();
        return game;
    }

    protected int getTurn(int index, int maxindex)
    {
        return NB_MAX_TURNS;
    }

    private Deck buildDeck(Manacurve curve)
    {
        FixedCurveDeckBuilder builder = new FixedCurveDeckBuilder(_library, curve, _imposedCards, _random, BuildOption.GOOD_RATE_FIRST, _configuration);
        Deck deck = new Deck();
        builder.fill(deck);
        return deck;
    }

    public String getHero()
    {
        return _hero;
    }

    public void setHero(final String hero)
    {
        _hero = hero;
    }

    public void cancel()
    {
        _canceled = true;
    }

    public boolean isCanceled()
    {
        return _canceled;
    }

    public int getNbGames()
    {
        return NB_GAMES;
    }

    public int getNB_MAX_TURNS()
    {
        return NB_MAX_TURNS;
    }

    public List<Card> getImposedCards()
    {
        return _imposedCards;
    }

    public String getConfigurationName()
    {
        return _configurationName;
    }

    public void setConfigurationName(final String configurationName)
    {
        _configurationName = configurationName;
    }

    public boolean isHasBeenRenamed()
    {
        return _hasBeenRenamed;
    }

    public void setHasBeenRenamed(final boolean hasBeenRenamed)
    {
        _hasBeenRenamed = hasBeenRenamed;
    }
}
