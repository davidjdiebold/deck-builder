package com.dd.tests;

import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;
import com.dd.builder.RandomDeckBuilder;
import com.dd.ia.CompositeAI;
import com.dd.ia.GreedyCardPlayingAI;
import com.dd.ia.PlayerAI;
import com.dd.ia.SimpleAttackAI;
import com.dd.model.*;

import java.util.Random;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 07:11
 */
public class TestRunGame
{
    private final CardPowersFixed _powers = new CardPowersFixed(new double[]{
            0.6711571793465727,
            0.9333820038347093,
            1.295913862916746,
            1.575412564144247,
            2.230871837172894,
            2.1406264147640983,
            2.94404316462099,
            2.902586612573223}
    );

    public void testCardPlaying()
    {
        Configuration configuration = Configuration.HEARTHSTONE;
        Manacurve opponentCurve = ManacurveFactory.buildClassicMtgDraftCurve();
        GameImpl game = new GameImpl(
                buildDeck(configuration), new GreedyCardPlayingAI(_powers, false, false, opponentCurve, configuration),
                buildDeck(configuration), new GreedyCardPlayingAI(_powers, false, false, opponentCurve, configuration),
                new Random(), configuration);
        
        game.run();
    }

    public void testSimpleAI()
    {
        Configuration configuration = Configuration.HEARTHSTONE;
        Manacurve opponentCurve = ManacurveFactory.buildClassicMtgDraftCurve();
        GameImpl game = new GameImpl(
                buildDeck(configuration), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(_powers, false, false, opponentCurve, configuration)}),
                buildDeck(configuration), new CompositeAI(new PlayerAI[]{new SimpleAttackAI(), new GreedyCardPlayingAI(_powers, false, false, opponentCurve, configuration)}),
                new Random(), configuration);

        game.run();
    }

    private Deck buildDeck(Configuration configuration)
    {

        Library library = new Library();
        DeckBuilder builder = new RandomDeckBuilder(library, configuration);
        Deck deck = new Deck();
        builder.fill(deck);
        return deck;
    }
}
