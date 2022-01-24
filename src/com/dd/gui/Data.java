package com.dd.gui;


import com.dd.builder.Configuration;
import com.dd.model.Card;
import com.dd.model.CardFactory;
import com.dd.usc.UseCaseOptimizeManaCurve;

import java.util.*;


public class Data
{
    private Configuration _configuration;

    private UISettings _settings = new UISettings();

    private List<Card> _cards = new ArrayList<Card>();

    private Set<UseCaseOptimizeManaCurve> _useCases = new LinkedHashSet<UseCaseOptimizeManaCurve>();

    public Data(Configuration configuration)
    {
        _configuration = configuration;
        reset();
    }

    //kryo
    public Data() {
    }

    public int clearUnnamedDecks()
    {
        Set<UseCaseOptimizeManaCurve> toRemove = new LinkedHashSet<UseCaseOptimizeManaCurve>();
        for(UseCaseOptimizeManaCurve useCase : _useCases)
        {
            if(!useCase.isHasBeenRenamed())
            {
                toRemove.add(useCase);
            }
        }

        _useCases.removeAll(toRemove);
        return toRemove.size();
    }

    public void reset()
    {
        _cards.clear();

        CardFactory factory = new CardFactory();

        _cards.add(factory.newMinion(0 + Messages.CARD_LIBRARY_NAME_BY_COST, 0, 1, 1, 0.67));
        _cards.add(factory.newMinion(1 + Messages.CARD_LIBRARY_NAME_BY_COST, 1, 1, 2, 0.93));
        _cards.add(factory.newMinion(2 + Messages.CARD_LIBRARY_NAME_BY_COST, 2, 2, 3, 1.30));
        _cards.add(factory.newMinion(3 + Messages.CARD_LIBRARY_NAME_BY_COST, 3, 3, 3, 1.57));
        _cards.add(factory.newMinion(4 + Messages.CARD_LIBRARY_NAME_BY_COST, 4, 4, 5, 2.23));
        _cards.add(factory.newMinion(5 + Messages.CARD_LIBRARY_NAME_BY_COST, 5, 5, 5, 2.14));
        _cards.add(factory.newMinion(6 + Messages.CARD_LIBRARY_NAME_BY_COST, 6, 6, 7, 2.94));
        _cards.add(factory.newMinion(7 + Messages.CARD_LIBRARY_NAME_BY_COST, 7, 9, 4, 2.90));

        for(Card card : _cards)
        {
            int cost = card.getCapedCost();
            (card).setRawRating(0.44752 + 0.07207 * cost + 0.0032 * cost * cost);
            if(cost==0) {
                card.setRemovalPower(-1);
                card.setRawRating(0.0);
            }
            card.setReadonly(true);
        }
        if(!_configuration.isHearthstone()) {
            _cards.add(factory.newLand("Land"));
        }
    }

    public List<Card> listCards()
    {
        return _cards;
    }

    public Set<UseCaseOptimizeManaCurve> getUseCases()
    {
        return _useCases;
    }

    public void setUseCases(final Set<UseCaseOptimizeManaCurve> useCases)
    {
        _useCases = useCases;
    }

    public void addUseCase(UseCaseOptimizeManaCurve usecase)
    {
        _useCases.add(usecase);
    }

    public UISettings getSettings()
    {
        return _settings;
    }

    public void setSettings(final UISettings settings)
    {
        _settings = settings;
    }

    public Configuration getConfiguration() {
        return _configuration;
    }
}
