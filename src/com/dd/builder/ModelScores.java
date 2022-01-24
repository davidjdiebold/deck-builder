package com.dd.builder;

import com.dd.maths.Domain;
import com.dd.maths.SimpleDomain;

import java.util.*;

public class ModelScores
{
    private SimpleDomain[] _domain;
    
    private Set<Record> _scores = new LinkedHashSet<Record>();

    //kryo
    public ModelScores()
    {
    }

    public ModelScores(SimpleDomain[] domain)
    {
        _domain = domain;
    }

    public void add(double[] parameters, double score)
    {
        Record record  =new Record(parameters, score, true);
        for(Record other : _scores)
        {
            if(areNear(record, other))
            {
                if(record.getScore()>other.getScore())
                {
                    other.setLocalOptimum(false);
                }
                if(record.getScore()<other.getScore())
                {
                    record.setLocalOptimum(false);
                }
            }
        }
        _scores.add(record);
    }

    private boolean areNear(Record record, Record other)
    {
        int size = record.getParameters().length;
        int index = 0;
        while(index < size)
        {
            if(Math.abs(record.getParameters()[index]-other.getParameters()[index])>_domain[index].getStep())
            {
                return false;
            }
            ++index;
        }
        return true;
    }
    
    public List<Record> listLocalOptima()
    {
        List<Record> ret = new ArrayList<Record>();
        for(Record rec : _scores)
        {
            if(rec.isLocalOptimum())
            {
                ret.add(rec);
            }
        }
        return ret;
    }
    
}