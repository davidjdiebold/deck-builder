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
//    = new double[]{
//            0.6711571793465727,
//            0.9333820038347093,
//            1.295913862916746,
//            1.575412564144247,
//            2.230871837172894,
//            2.1406264147640983,
//            2.94404316462099,
//            2.902586612573223};

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
//            System.out.println("Turn : " + i);
            UseCaseOptimizeManaCurve usc = new UseCaseOptimizeManaCurve(new Library(),
                    new ArrayList<Card>(), i, builder, random, 400, null, _configuration);
            Stat[] evaluate = usc.evaluate(curve);
            double m = usc.mean(evaluate).getScore();
            System.out.println(m);
        }
    }
}
