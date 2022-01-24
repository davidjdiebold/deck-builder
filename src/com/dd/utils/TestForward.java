package com.dd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestForward
{
    public static void main(String[] args)
    {
        List<String> cards = new ArrayList<String>();
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("1Cost");
        cards.add("2Cost");
        cards.add("2Cost");
        cards.add("2Cost");
        cards.add("2Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("3Cost");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");
        cards.add("Land");

        int remaining = Math.max(0,60 - cards.size());
        for(int i = 0 ; i < remaining ; i++)
        {
            cards.add("*");
        }

        Collections.shuffle(cards);
        
        for(String str : cards)
        {
            System.out.println(str);
        }
    }
}
