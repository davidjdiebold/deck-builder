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
    }
}
