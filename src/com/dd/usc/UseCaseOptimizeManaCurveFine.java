package com.dd.usc;

import com.dd.builder.Configuration;
import com.dd.builder.CurveBuilder;
import com.dd.builder.DeckBuilder;
import com.dd.gui.ProgressBar;
import com.dd.model.*;

import java.util.*;

/**
 * Created by DAVID on 25/06/2014.
 */
public class UseCaseOptimizeManaCurveFine extends UseCaseOptimizeManaCurve
{
    private final int NB_MAX_ELEMENTS_IN_GENERATION = 8 * 8;

    public UseCaseOptimizeManaCurveFine(Library library, List<Card> imposedCards, int nbTurns, CurveBuilder builder,
                                        Random random,
                                        int curvesToTest,
                                        ProgressBar bar,
                                        Configuration configuration)
    {
        super(library, imposedCards, nbTurns, builder, random, curvesToTest, bar, configuration);
    }

    public UseCaseOptimizeManaCurveFine() {
        super();
    }

    @Override
    public Manacurve execute()
    {
        SortedSet<CurveWithScore> sortedCurves = new TreeSet<CurveWithScore>();
        int nbFixedCards = 0;
        Manacurve initial = new Manacurve(0., 8);
        if(!_configuration.isHearthstone()) {
            int nbLands = 16;
            initial.setCount(7, nbLands);//HACK
        }
        for (Card c : _imposedCards)
        {
            initial.setCount(c.getCost(), initial.getCount(c.getCost()) + 1);
            ++nbFixedCards;
        }

        sortedCurves.add(new CurveWithScore(initial, new Stat(0., 0., 0., 0., 0.)));
        for (int i = 0; i < Math.max(0, _configuration.getDeckSize() - nbFixedCards - (_configuration.isHearthstone()?0:16)); i++)
        {
            Set<Manacurve> newGeneration = createNewGeneration(sortedCurves);
            int has0=0;
            for(Manacurve c : newGeneration) {
                has0 += c.getCount(0)>0 ?1:0;
            }
            if(has0>8)
            {
                System.out.println("has zero !");
            }
            sortedCurves.clear();
            for (Manacurve curve : newGeneration)
            {
                Manacurve workingcopy = curve.copy();
                complete(workingcopy);
                Stat[] evaluate = evaluate(workingcopy);
                sortedCurves.add(new CurveWithScore(curve, mean(evaluate)));
            }

            int k =0;
            boolean hasaero = false;
            for(CurveWithScore c : sortedCurves){
                if(k>7)
                {
                    break;
                }
                if(c._curve.getCount(0)>0)
                    hasaero=true;
                ++k;
            }
            if(hasaero)
                System.out.printf("Has zero");

            //TODO synchronize
            int percent = i * 100 / _configuration.getDeckSize();
            if (_bar != null)
            {
                _bar.setAdvancement(percent);
            }
            _results.setAdvancement(percent);
        }

        int index = 0;
        List<CurveWithScore> inversedSet = new ArrayList<CurveWithScore>();
        for (CurveWithScore cws : sortedCurves)
        {
            if (index < 8)
            {
                inversedSet.add(0, cws);
                ++index;
            }
        }

        for (CurveWithScore cws : inversedSet)
        {
            _results.getResults().put(cws.get_curve(), cws.get_score());
        }

        return sortedCurves.iterator().next().get_curve();
    }

    private void complete(Manacurve curve)
    {
        int ct = 0;
        for (int i = 0; i < 8; i++)
        {
            ct += curve.getCount(i);
        }
        curve.setCount(0, _configuration.getDeckSize() - ct + curve.getCount(0));
    }

    private Set<Manacurve> createNewGeneration(SortedSet<CurveWithScore> oldGeneration)
    {
        Set<Manacurve> newGeneration = new LinkedHashSet<Manacurve>();
        Iterator<CurveWithScore> it = oldGeneration.iterator();
        while (it.hasNext() && newGeneration.size() < NB_MAX_ELEMENTS_IN_GENERATION)
        {
            CurveWithScore parent = it.next();
            createNewCurves(parent.get_curve(), newGeneration);
        }
        return newGeneration;
    }

    private void createNewCurves(Manacurve curve, Set<Manacurve> toFill)
    {
        //hack
        for (int i = 1; i < 8; i++)
        {
            Manacurve newCurve = curve.copy();
            newCurve.setCount(i, newCurve.getCount(i) + 1);
            toFill.add(newCurve);
        }
    }

    class CurveWithScore implements Comparable<CurveWithScore>
    {
        private final Manacurve _curve;
        private final Stat _score;

        CurveWithScore(Manacurve _curve, Stat _score)
        {
            this._curve = _curve;
            this._score = _score;
        }

        public Manacurve get_curve()
        {
            return _curve;
        }

        public Stat get_score()
        {
            return _score;
        }

        @Override
        public int compareTo(CurveWithScore o)
        {
            if (this.get_score().getScore() == o.get_score().getScore())
            {
                for (int i = 7; i < 0; i--)
                {
                    if (this.get_curve().getCount(i) > o.get_curve().getCount(i))
                    {
                        return -1;
                    }
                    if (this.get_curve().getCount(i) < o.get_curve().getCount(i))
                    {
                        return +1;
                    }
                }
            }
            if (this.get_score().getScore() > o.get_score().getScore())
            {
                return -1;
            } else
            {
                return 1;
            }
        }
    }
}
