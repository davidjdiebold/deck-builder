package com.dd.tests;

import com.dd.builder.Configuration;
import com.dd.builder.DeckBuilder;
import com.dd.builder.RandomCurveBuilder;
import com.dd.gui.EmptyProgressBar;
import com.dd.model.*;
import com.dd.usc.*;
import com.dd.utils.Hypergeometric;

import java.util.*;

public class Sandbox
{
    public static void main(String[] args)
    {
        double[] ret = new UseCaseEvaluateCardRatings().execute();
        for (int i = 0; i < ret.length; i++) {
            System.out.println(ret[i]);
        }

        System.exit(0);

        //opponent
        //sol ring
        //excavations
        //blast

        Set<Scenario> scenarios = new LinkedHashSet<Scenario>();
        Scenario vsAggroGoingFirst = new Scenario(1., 0, 7);
//        Scenario vsAggroGoingFirst = new Scenario(0.25*46/60, 0, 7);
        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 1, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 2, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 7, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Little", 1, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("3Cost", 7, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Removal", 7, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Finisher", 7, 1));
        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 7, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 1, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 2, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 3, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Little", 1, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("3Cost", 3, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Removal", 4, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Finisher", 5, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Land", 5, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Ramp", 2, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("FatGuy", 3, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Mana3", 3, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Mana3", 4, 1));
//        vsAggroGoingFirst.getSuccesses().add(new Success("Mana5", 5, 1));
        scenarios.add(vsAggroGoingFirst);

        Set<Scenario> ss = new LinkedHashSet<Scenario>();
        Scenario scen = new Scenario(1., 2, 8);
        scen.getSuccesses().add(new Success("Artifact", 2, 1));
        ss.add(scen);
        Map<String,Integer> map = new LinkedHashMap<String, Integer>();
        map.put("Artifact", 8);
        double proba = new Hypergeometric().computeProba(ss, map, 60);

        Map<String, Integer> bestCurve = new Hypergeometric().bestCurve(scenarios, 60);

        //Abusive Sergent > Boulderfist ogre (<10)

        CardFactory factory = new CardFactory();

        Deck used = new Deck();
        used.add(factory.newMinion("...", 4, 1, 1, 17.), 1);
        Card inventor = factory.newMinion("...", 4, 1, 1, 12.5);
        inventor.setCardsDrawn(1);
        used.add(inventor, 1);
        used.add(factory.newMinion("...", 1, 1, 1, 9.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 14.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 9.), 1);
        used.add(factory.newMinion("...", 3, 1, 1, 11.), 1);
        Card hoarder = factory.newMinion("...", 2, 1, 1, 5.);
        hoarder.setCardsDrawn(1);
        used.add(hoarder, 1);
        used.add(factory.newMinion("...", 2, 1, 1, 11.), 1);
        used.add(factory.newMinion("...", 4, 1, 1, 11.), 1);
        used.add(factory.newMinion("...", 1, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 4, 1, 1, 9.), 1);
        Card starfire = factory.newMinion("...", 6, 1, 1, 17.);
        starfire.setCardsDrawn(1);
        used.add(starfire, 1);
        used.add(factory.newMinion("...", 2, 1, 1, 9.), 1);
        used.add(factory.newMinion("...", 3, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 14.), 1);
        used.add(factory.newMinion("...", 3, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 3, 1, 1, 14.), 1);
        used.add(factory.newMinion("...", 5, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 5, 1, 1, 17.), 1);
        Card nourrish = factory.newMinion("...", 2, 1, 1, 6.);
        used.add(nourrish, 1);
        used.add(factory.newMinion("...", 4, 1, 1, 20.), 1);
        used.add(factory.newMinion("...", 4, 1, 1, 11.), 1);
        used.add(factory.newMinion("...", 3, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 2, 1, 1, 14.), 1);
        used.add(factory.newMinion("...", 1, 1, 1, 17.), 1);
        used.add(factory.newMinion("...", 1, 1, 1, 5.), 1);
        used.add(factory.newMinion("...", 4, 1, 1, 11.), 1);


        //Durée : 10 tours
        //9
        //9
        //15

        Configuration configuration = Configuration.HEARTHSTONE;
        int deckSize = configuration.getDeckSize();

        Deck pool = new Deck();
        pool.add(factory.newMinion("0 Cost", 0, 1, 1, 7.), 0);
        pool.add(factory.newMinion("1 Cost", 1, 1, 1, 7.2), 3);
        pool.add(factory.newMinion("2 Cost", 2, 1, 1, 11.33), 4);
        pool.add(factory.newMinion("3 Cost", 3, 1, 1, 10.01), 3);
        pool.add(factory.newMinion("4 Cost", 4, 1, 1, 13.57), 0);
        pool.add(factory.newMinion("5 Cost", 5, 1, 1, 12.0), 4);
        pool.add(factory.newMinion("6 Cost", 6, 1, 1, 10.0), 0);
        pool.add(factory.newMinion("7+ Cost",7, 1, 1, 12.25), 4);
        pool.add(factory.newMinion("0 Cost Trash", 0, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("1 Cost Trash", 1, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("2 Cost Trash", 2, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("3 Cost Trash", 3, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("4 Cost Trash", 4, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("5 Cost Trash", 5, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("6 Cost Trash", 6, 1, 1, 0), deckSize);
        pool.add(factory.newMinion("7+ Cost Trash", 7, 1, 1, 0),deckSize);

        Library library = new Library();
        for(Card c : pool.toList())
        {
            library.add(c);
        }
        for(Card c :used.toList())
        {
            library.add(c);
        }

        UseCaseOptimizeManaCurveFine usc = new UseCaseOptimizeManaCurveFine(
                    library,
                    used.toList(),
                    9,
                    new RandomCurveBuilder(7, new Random(), configuration),
                    new Random(0x1234),
                    3,
                    new EmptyProgressBar(), configuration);
        usc.setHero("Paladin");
        usc.execute();

        OptimizeCurveResults results = usc.getResults();
        for(Stat s : results.getResults().values())
        {
            System.out.println(""+s.getScore());
        }
        int test = 3;

        //20
        //17
        //14
        //11
        //9
        //5

        //DRUIDE
//        pool.add(factory.newMinion("0 Cost", 0, 1, 1, 7.), 2);
//        pool.add(factory.newMinion("1 Cost", 1, 1, 1, 7.2), 13);
//        pool.add(factory.newMinion("2 Cost", 2, 1, 1, 11.33), 23);
//        pool.add(factory.newMinion("3 Cost", 3, 1, 1, 10.01), 23);
//        pool.add(factory.newMinion("4 Cost", 4, 1, 1, 13.57), 15);
//        pool.add(factory.newMinion("5 Cost", 5, 1, 1, 12.0), 13);
//        pool.add(factory.newMinion("6 Cost", 6, 1, 1, 10.0), 7);
//        pool.add(factory.newMinion("7+ Cost",7, 1, 1, 12.25), 4);

        //Library lib = new Library(true);
        //double[] ret = new UseCaseEvaluatePowerTurn().execute(lib);
        //double[] ret = new UseCaseEvaluateStartingHand().execute(lib, new CardPowersPhilosophyFire());
        //System.out.println(Arrays.toString(ret));
    }
}
