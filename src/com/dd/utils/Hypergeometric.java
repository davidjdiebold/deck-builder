package com.dd.utils;

import com.dd.model.Scenario;
import com.dd.model.Success;

import java.util.*;

/**
 * Created by DAVID on 25/12/2014.
 */
public class Hypergeometric
{


    public Map<String,Integer> bestCurve(Set<Scenario> scenarios, int deckSize)
    {
        Map<String, Integer> start = getStart(scenarios);

        Map<String, Integer> ret = start;

        for (int i = 0; i < 1; i++)
        {
            int nbCards = 0;
            //start.put("Land", 21+i);
            for (Integer integer : start.values())
            {
                nbCards += integer;
            }

            ret = start;
            double prob = 0.;
            while (nbCards < deckSize-4)
            {
                double bestProba = 0.;
                Map<String, Integer> nextStart = new LinkedHashMap<String, Integer>();
                for (Scenario scenario : scenarios)
                {
                    for (Success s : scenario.getSuccesses())
                    {
                        Map<String, Integer> v = new LinkedHashMap<String, Integer>(ret);
                        v.put(s.getCardName(), v.get(s.getCardName()) + 1);
                        double p = computeProba(scenarios, v, deckSize);
                        if (p > bestProba)
                        {
                            bestProba = p;
                            nextStart = v;
                            prob = bestProba;
                        }
                    }
                }
                ret = nextStart;
                ++nbCards;
            }
            System.out.println(i + " Land : " + prob);
        }
        return ret;
    }

    private Map<String, Integer> getStart(Set<Scenario> scenarios)
    {
        boolean firstScenario = true;
        Map<String, Integer> start = new LinkedHashMap<String, Integer>();
        for (Scenario scenario : scenarios)
        {
            Map<String, Integer> v = new LinkedHashMap<String, Integer>();
            for (Success s : scenario.getSuccesses())
            {
                Integer old = v.get(s.getCardName());
                old = old == null ? 0 : old;
                int toPut = old + s.getExpected();
                v.put(s.getCardName(), toPut);
            }
            if(firstScenario)
            {
               start = v;
            }
            else
            {
                Set<String> cards = new LinkedHashSet<String>();
                for (String s : v.keySet())
                {
                    cards.add(s);
                }
                for (String s : start.keySet())
                {
                    cards.add(s);
                }
                for (String card : cards)
                {
                    Integer a = start.get(card);
                    Integer b = v.get(card);
                    start.put(card, a!=null && b!=null ? Math.max(a,b) : a!=null ? a : b);
                }
            }

            firstScenario = false;
        }
        return start;
    }

    public double computeProba(Set<Scenario> scenarios, Map<String, Integer> quantities, int deckSize)
    {
        double ret = 0.;
        for (Scenario scenario : scenarios)
        {
            SortedSet<Success> temp = new TreeSet<Success>(scenario.getSuccesses());
            double proba0 = scenario.getProbability();
            double toAdd = computeProba(temp, quantities, 1., deckSize - scenario.getCardsFixed(), scenario.getCardsDrawn());
            ret += toAdd * proba0;
        }
        return ret;
    }

    private double computeProba(SortedSet<Success> successes, Map<String, Integer> quantities, double proba, int ds, int initialCards)
    {
        int nbOldSuccesses = 0;
        int successesAlreadyDrawn = 0;
        Iterator<Success> it = successes.iterator();
        if(!it.hasNext())
        {
            return proba;
        }
        Success success = it.next();
        while (it.hasNext())
        {
            Success s = it.next();
            nbOldSuccesses += s.getExpected();
            if(s.getCardName().equals(success.getCardName()))
            {
                successesAlreadyDrawn += s.getExpected();
            }
        }

        int deckSize  = ds - nbOldSuccesses;
        int available = quantities.get(success.getCardName()) - successesAlreadyDrawn;
        int drawn = initialCards + (success.getTurn() - 1) - nbOldSuccesses;
        double newProba = proba * hypergeometric(deckSize, available, drawn, success.getExpected());
        successes.remove(success);
        return computeProba(successes, quantities, newProba, ds, initialCards);
    }

    public double hypergeometric(int population, int successes, int drawn, int successesToDraw)
    {
        return internalHypergeometric(population, successes, drawn, successesToDraw);
    }

    private double internalHypergeometric(int N, int k, int nn, int x)
    {
        double ret = 0.;
        for (int i = x; i <= Math.min(k,nn); i++)
        {
            double p = cnk(k, i) * cnk(N - k, nn - i) / cnk(N, nn);
            ret += p;
        }
        return ret;
    }

    public double cnk(double n, double k)
    {
        if(n<0||k<0)
        {
            return 0;
        }

        double ret = 1;
        for (double i = 1; i <= k; i++)
        {
            ret *= (n-i+1) / i;
        }
        return ret;
    }
}
