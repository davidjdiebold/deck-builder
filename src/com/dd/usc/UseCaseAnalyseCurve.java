package com.dd.usc;

import com.dd.builder.Configuration;
import com.dd.builder.CurveBuilder;
import com.dd.builder.RandomCurveBuilder;
import com.dd.model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * User: DD
 * Date: 06/11/13
 * Time: 22:33
 */
public class UseCaseAnalyseCurve
{
    private final CardPowers powers;
    private final Configuration _configuration;

    public UseCaseAnalyseCurve(final CardPowers powers, Configuration configuration)
    {
        this.powers = powers;
        _configuration = configuration;
    }

    public void execute(Manacurve curve)
    {
        Random random = new Random(0x12345);

        //not used
        CurveBuilder builder = new RandomCurveBuilder(curve.getMaxCost()-1, random, _configuration);

        for(int i = 5 ; i < 15 ; i++)
        {
            UseCaseOptimizeManaCurve usc = new UseCaseOptimizeManaCurve(new Library(),
                    new ArrayList<Card>(), i, builder, random, 400, null, _configuration);
            Stat[] evaluate = usc.evaluate(curve);
            double m = usc.mean(evaluate).getScore();
            System.out.println(m);
        }
    }
}
