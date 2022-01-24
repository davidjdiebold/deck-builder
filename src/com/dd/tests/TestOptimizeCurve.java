package com.dd.tests;

import com.dd.builder.*;
import com.dd.model.*;
import com.dd.usc.UseCaseOptimizeManaCurve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * User: DD
 * Date: 04/11/13
 * Time: 19:34
 */
public class TestOptimizeCurve
{
    public void printBestScores()
    {
        for(int i = 5 ; i < 15 ; i++)
        {
            test(i);
        }
    }
    
    public void test(int turn)
    {
        Configuration configuration = Configuration.HEARTHSTONE;

        System.out.println("Turn : " + turn);
        CardPowersFixed powers = new CardPowersFixed(new double[]{
                0.6711571793465727,
                0.9333820038347093,
                1.295913862916746,
                1.575412564144247,
                2.230871837172894,
                2.1406264147640983,
                2.94404316462099,
                2.902586612573223});

        Random random = new Random(0x12345);

        CardFactory factory = new CardFactory();
        List<Card> imposedCards = new ArrayList<Card>();
        Card cleric = factory.newMinion("Cleric of Northshire",1,1,3);
        cleric.setCardsDrawn(1);
        cleric.setRawRating(0.9333);
        imposedCards.add(cleric.copy());
        imposedCards.add(cleric.copy());

        Card thoughtsteel = factory.newMinion("Thoughtsteal",3,0,1);
        thoughtsteel.setCardsDrawn(2);
        thoughtsteel.setRawRating(0.);
        imposedCards.add(thoughtsteel.copy());

        Card engineer = factory.newMinion("engineer",4,2,4);
        engineer.setCardsDrawn(1);
        engineer.setRawRating(0.9333);
        imposedCards.add(engineer.copy());
        imposedCards.add(engineer.copy());

        Manacurve minCurve = new Manacurve(Double.NaN, 8);
        for(Card card : imposedCards)
        {
            int cost = card.getCost();
            if(Double.isNaN(minCurve.getCount(cost)))
            {
                minCurve.setCount(cost, 1);
            }
            else
            {
                minCurve.setCount(cost, minCurve.getCount(cost) + 1);
            }
        }
        
        ModelCurveBuilder builder = new ModelCurveBuilder(minCurve, configuration);

        UseCaseOptimizeManaCurve usc = new UseCaseOptimizeManaCurve(
                new Library(),
                imposedCards, turn, builder, random, builder.getModel().buildDomain().size(), null, configuration);

        Manacurve curve = usc.execute();

        List<Record> optima = builder.getScores().listLocalOptima();
        for(Record opt : optima)
        {
            System.out.println(Arrays.toString(opt.getParameters()));
        }

        System.out.println(curve);
    }
    
}
