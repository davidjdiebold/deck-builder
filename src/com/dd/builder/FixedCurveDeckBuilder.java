package com.dd.builder;

import com.dd.model.Card;
import com.dd.model.Deck;
import com.dd.model.Library;
import com.dd.model.Manacurve;

import java.util.*;

/**
 * User: DD
 * Date: 04/11/13
 * Time: 06:53
 */
public class FixedCurveDeckBuilder implements DeckBuilder
{
    private final Manacurve _curve;
    private Library _library;
    private final Random _random;
    private final Map<Integer, List<Card>> _imposedCardsByCost;
    private final BuildOption _option;
    private final Configuration _configuration;
    
    public FixedCurveDeckBuilder(Library library, Manacurve curve, List<Card> imposedCards, Random random, BuildOption option,
                                 Configuration configuration)
    {
        _configuration = configuration;
        _library = library;
        _curve = curve;
        _random = random;
        _option = option;

        _imposedCardsByCost = new HashMap<Integer, List<Card>>();
        for(Card card : imposedCards)
        {
            if(_imposedCardsByCost.get(card.getCapedCost())==null)
            {
                _imposedCardsByCost.put(card.getCapedCost(), new ArrayList<Card>());
            }
            _imposedCardsByCost.get(card.getCapedCost()).add(card);
        }
    }

    @Override
    public void fill(Deck deck)
    {
        Library library = _library.copy();
        for(int cost = 0 ; cost < _curve.getMaxCost() ; cost++)
        {
            //Ajouter les cartes imposées
            int nbImposedCards = 0;
            if(_imposedCardsByCost.get(cost)!=null)
            {
                List<Card> cards = _imposedCardsByCost.get(cost);
                nbImposedCards = cards.size();
                for(Card card : cards)
                {
                    deck.add(card);
                    library.remove(card, 1);
                }
            }

            //Cards are added by rating order.
            if(_option==BuildOption.GOOD_RATE_FIRST)
            {
                int nbCards = (int) (_curve.getCount(cost) - nbImposedCards);
                Card[] cardsOfThisCost = library.listCardsSortedByRawRate(cost, _configuration);
                cardsOfThisCost = removeRemoval(cardsOfThisCost);
                int index = 0;
                while (nbCards>0)
                {
                    if(cardsOfThisCost.length==0)
                    {
                        throw new BuilderException("Library does not contain card of cost", cost);
                    }
                    Card card = cardsOfThisCost[index];
                    int cardsToAdd = Math.min(nbCards, library.getCount(card));
                    deck.add(card, cardsToAdd);
                    library.remove(card, cardsToAdd);
                    nbCards -= cardsToAdd;

                    if(index==cardsOfThisCost.length-1)
                    {
                        deck.add(card, nbCards);
                        nbCards = 0;
                    }

                    ++index;
                }
            }
            //TODO Genericiser
            else if(_option==BuildOption.RANDOM)
            {
                int nbCards = (int) (_curve.getCount(cost) - nbImposedCards);
                Card[] cardsOfThisCost = library.listCardsSortedByRawRate(cost, _configuration);
                for (int i = 0 ; i < nbCards ; i++)
                {
                    deck.add(_option.select(cardsOfThisCost, _configuration), 1);
                }
            }
        }
    }

    private Card[] removeRemoval(Card[] cardsOfThisCost) {
        List<Card> ret = new ArrayList<Card>();
        for(Card card : cardsOfThisCost)
        {
            if(card.getRemovalPower()<0 && card.getCardsDrawn()==0 && (card.getExtraDefinitiveMana()==0||card.getCost()==7))
            {
                ret.add(card);
            }
        }
        return ret.toArray(new Card[ret.size()]);
    }
}
