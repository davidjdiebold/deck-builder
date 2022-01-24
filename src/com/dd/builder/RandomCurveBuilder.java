package com.dd.builder;

import com.dd.model.Manacurve;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class RandomCurveBuilder implements CurveBuilder
{
    private final Configuration _configuration;

    private final int _maxCost;
    private final Random _random;
    private final Map<Integer,Slot> _slots = new LinkedHashMap<Integer, Slot>();

    public RandomCurveBuilder(int maxCost, Random random, Configuration configuration)
    {
        _configuration = configuration;
        _maxCost = maxCost;
        _random = random;
        addConstraint(0, _maxCost+1, configuration.getDeckSize());
    }

    @Override
    public void reset()
    {
    }

    @Override
    public Manacurve buildRandomCurve()
    {
        Manacurve ret = new Manacurve(_maxCost+1);
        for(int i = 0 ; i<ret.getMaxCost();i++)
        {
            ret.setCount(i, 0.);
        }

        int cardsBySlot = 2;
        for(int i = 0 ; i < _configuration.getDeckSize() / cardsBySlot ; i++)
        {
            int cost = (int) (_random.nextDouble() * (_maxCost+1));
            Slot slot = findSlot(cost);
            while(getCardsInSlots(slot, ret)>=slot.getMaxCards())
            {
                
                int c = slot.getMinIncluded()-1;
                if(c==-1)
                {
                    c=_maxCost;
                }
                slot = findSlot(c);
                cost = slot.getMinIncluded() + ((int)(Math.random() * (slot.getMaxExcluded()-slot.getMinIncluded())));
            }
            
            ret.setCount(cost, ret.getCount(cost) + cardsBySlot);
        }
        return ret;
    }
    
    public void addConstraint(int includedMinCost, int excludedMaxCost, double nbMaxCards)
    {
        Slot slot = new Slot(includedMinCost, excludedMaxCost, nbMaxCards);
        for(int i = includedMinCost ; i < excludedMaxCost ; i++)
        {
            _slots.put(i, slot);
        }
    }
    
    private Slot findSlot(int cost)
    {
        return _slots.get(cost);
    }
    
    private double getCardsInSlots(Slot slot, Manacurve curve)
    {
        double ret = 0.;
        for(int i = slot.getMinIncluded() ; i < slot.getMaxExcluded() ; i++)
        {
            ret += curve.getCount(i);
        }
        return ret;
    }

    @Override
    public void hint(Manacurve curve, double score)
    {
    }

    class Slot
    {
        private final int _minIncluded;
        private final int _maxExcluded;
        private final double _maxCards;

        Slot(int minIncluded, int maxExcluded, double maxCards)
        {
            _minIncluded = minIncluded;
            _maxExcluded = maxExcluded;
            _maxCards = maxCards;
        }

        public int getMinIncluded()
        {
            return _minIncluded;
        }

        public int getMaxExcluded()
        {
            return _maxExcluded;
        }

        public double getMaxCards()
        {
            return _maxCards;
        }
    }
}
