package com.dd.usc;

import com.dd.builder.Configuration;
import com.dd.builder.ModelCurveBuilder;
import com.dd.model.Card;
import com.dd.model.CardFactory;
import com.dd.model.Library;
import com.dd.model.Manacurve;

import java.util.*;

public class UseCaseEvaluateCardRatings {

    private final int nb_games = 20;

    public double[] execute() {

        Manacurve[] existingCurves = loadExistingCurves();

        double bestError = Double.MAX_VALUE;
        double[] bestRawRatings = null;
        List<double[]> ratingstoCheck = generateRawRatings();
        for(double[] ratings : ratingstoCheck) {
            Map<Integer, Manacurve> optimalCurves = computeOptimalCurves(ratings);
            double error = 0.0;
            for(Manacurve existingCurve : existingCurves) {
                error += findMinimumError(existingCurve, optimalCurves);
            }
            if(error<bestError) {
                bestError = error;
                bestRawRatings = ratings;
            }
        }

        return bestRawRatings;
    }

    private Manacurve[] loadExistingCurves() {
        return new Manacurve[]{
                new Manacurve(new Double[]{0.0,3.0,9.0,8.0,13.0,2.0,1.0,24.0}),
                new Manacurve(new Double[]{0.0,3.0,10.0,7.0,10.0,3.0,1.0,25.0}),
                new Manacurve(new Double[]{0.0,6.0,9.0,6.0,9.0,4.0,3.0,23.0}),
                new Manacurve(new Double[]{0.0,2.0,11.0,7.0,10.0,2.0,3.0,25.0}),
                new Manacurve(new Double[]{0.0,3.0,11.0,5.0,11.0,0.0,4.0,26.0}),
                new Manacurve(new Double[]{0.0,3.0,9.0,11.0,6.0,1.0,1.0,24.0})
        };
    }

    private List<double[]> generateRawRatings() {
        double[] alphas = new double[]{20.0};
        //double[] alphas = new double[]{5.0,7.5,10.0,15.0,20.0};
        double[] betas = new double[]{0.01};
        //double[] betas = new double[]{-1.0,-0.5,0.1,0.5,1.0,2.0};
        List<double[]> ret = new ArrayList<double[]>();
        for(int i = 0 ; i < alphas.length ; i++)
        {
            for(int j = 0 ; j < alphas.length ; j++)
            {
                double[] weights = new double[7];
                weights[0] = 0.0;
                for(int c = 1 ; c<7 ; c++) {
                    weights[c] = 1.0 + alphas[i] * c + betas[j] / 5.0 * c * c;
                }
                ret.add(weights);
            }
        }
        return ret;
    }

    private Map<Integer, Manacurve> computeOptimalCurves(double[] ratings) {
        Configuration conf = Configuration.MAGIC;
        Map<Integer, Manacurve> bestCurves = new HashMap<Integer, Manacurve>();
        ModelCurveBuilder builder = new ModelCurveBuilder(new Manacurve(Double.NaN, 8), conf);
        for(int turn = 5 ; turn < 35 ; turn++) {
            Library library = buildLibrary(ratings);
            UseCaseOptimizeManaCurveFine optimizer =
                    new UseCaseOptimizeManaCurveFine(library, new ArrayList<Card>(), turn, builder, new Random(), builder.getModel().buildDomain().size(), null, conf);
            optimizer.setNbGames(nb_games);
            Manacurve bestCurve = optimizer.execute();
            bestCurves.put(turn, bestCurve);
        }
        return bestCurves;
    }

    private Library buildLibrary(double[] ratings) {
        Library ret = new Library(false);
        CardFactory factory = new CardFactory();
        for(int cost = 0 ; cost < 7 ; cost++) {
            ret.add(factory.newMinion(cost+" cost", cost, cost, cost, ratings[cost]));
        }
        ret.add(factory.newLand("Land"));
        return ret;
    }

    private double findMinimumError(Manacurve curve, Map<Integer, Manacurve> optimalCurves) {
        double best_error = Double.MAX_VALUE;
        int best_t=0;
        for(Integer t : optimalCurves.keySet()) {
            Manacurve c = optimalCurves.get(t);
            double error = distance(curve, c);
            if(error<best_error) {
                best_t=t;
                best_error = error;
            }
        }
        System.out.println(best_t);
        return best_error;
    }

    private double distance(Manacurve curve, Manacurve c) {
        double ret = 0.0;
        for(int i = 0 ; i < curve.getMaxCost() ; i++) {
            ret += Math.abs(curve.getCount(i) - c.getCount(i));
        }
        return ret;
    }

}
