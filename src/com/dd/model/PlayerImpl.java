package com.dd.model;

import com.dd.builder.Configuration;
import com.dd.ia.PlayerAI;
import com.dd.structures.SetOfElements;

import java.util.*;


/**
* User: DD
* Date: 03/11/13
* Time: 06:58
*/
public class PlayerImpl implements Player
{
    private final CardFactory _factory = new CardFactory();

    private final String _id;

    private PlayerAI _ai;

    private Deck _deck;

    private List<Card> _cardsOnBoard = new ArrayList<Card>();
    private List<Card> _cardsInHand = new ArrayList<Card>();

    private int _life;
    private int _currentMana;
    private int _maxMana;
    private int _manaToBeLocked = 0;
    private int _lockedMana = 0;
    private Game _game;

    private boolean _winner;

    @Override
    public void usePower(boolean isWarlock)
    {
        if(_currentMana>=2)
        {
            _game.getHistory().addAction(new ActionPowerUse(_game.getTurn(), this));
            if(isWarlock)
            {
                drawCards(1);
                _currentMana -= 2;
                _life -= 2;
            }
            else
            {
                Card minion = _factory.newMinion("Minion", 2, 1, 1);
                minion.setOwner(this);
                _cardsOnBoard.add(minion);
                _currentMana -= 2;
            }
        }
    }

    @Override
    public Manacurve getDeckCurve()
    {
        return _deck.getCurve();
    }

    public PlayerImpl(String id, Deck deck, PlayerAI ai, int life, int currentMana, int maxMana, Game game)
    {
        _id = id;
        _ai = ai;
        _life = life;
        _currentMana = currentMana;
        _maxMana = maxMana;
        _deck = deck;
        _game = game;
    }

    public Deck getDeck()
    {
        return _deck;
    }

    @Override
    public void drawCards(int cardsToDraw) throws EmptyDeckException
    {
        if (_deck.size() < cardsToDraw)
        {
            throw new EmptyDeckException("Not enough cards in Deck.");
        }
        List<Card> cards = _deck.pull(cardsToDraw);
        for (Card card : cards)
        {
            _game.getHistory().addAction(new ActionDrawCard(_game.getTurn(), _id, card));
        }
        _cardsInHand.addAll(cards);
    }

    public void updateMana()
    {
        increaseMana();
        _currentMana = _maxMana - _manaToBeLocked;
        _lockedMana = _manaToBeLocked;
        _manaToBeLocked = 0;
    }

    private void increaseMana()
    {
        if(_game.isHearthstone())
        {
            ++_maxMana;
        }
        else
        {
            Card c = null;
            Iterator<Card> it = getAllCardsInHand().iterator();
            while(c==null && it.hasNext())
            {
                Card cc = it.next();
                if(cc.getExtraDefinitiveMana()>0&&cc.getCost()==7)
                {
                    _maxMana += cc.getExtraDefinitiveMana();
                    c = cc;
                }
            }
            if(c!=null)
            {
                getAllCardsInHand().remove(c);
                _game.getHistory().addAction(new ActionPlayCard(_game.getTurn(), this, c));
            }
        }
    }

    public void updateCreaturesState()
    {
        for(Card card : _cardsOnBoard)
        {
            if(card.getHealth()>0)
            {
                card.setCanAttack(true);
            }
        }
    }

    public void playOneTurn()
    {
        _ai.playOneTurn(_game, this);
    }

    @Override
    public List<Card> getAllCardsInHand()
    {
        return _cardsInHand;
    }

    @Override
    public void aoe(int aoe) {
        if(aoe>0)
        {
            for(Card m : _game.getOpponent(this).listMinionsOnBoard(true))
            {
                m.dealDamage(aoe);
            }
        }
    }

    @Override
    public int getManaAvailable()
    {
        return _currentMana;
    }

    @Override
    public void playCard(Card card)
    {
        if(!_game.isHearthstone() && card.getExtraDefinitiveMana()>0 && card.getCost()==7)
        {
            return;
        }

        if (card.getCapedCost() <= getManaAvailable() && _cardsInHand.remove(card))
        {
            _currentMana -= card.getCapedCost();
            Card copy = card.copy();
            copy.initializeLife();
            copy.setOwner(this);
            if(copy.getHealth()>0) {
                _cardsOnBoard.add(copy);
            }
            copy.battlecry(this);
            _game.getHistory().addAction(new ActionPlayCard(_game.getTurn(), this, card));
            _manaToBeLocked += copy.getOverload();
            _currentMana += copy.get_extraMana();
            _maxMana += card.getExtraDefinitiveMana();
        }
    }

    public void setWinner(boolean b)
    {
        _winner = b;
    }

    @Override
    public Set<Card> listMinionsOnBoard(boolean canAttack)
    {
        Set<Card> ret = new LinkedHashSet<Card>();
        for(Card card : _cardsOnBoard)
        {
            if((!canAttack || (card).getCanAttack()))
            {
                ret.add(card);
            }
        }
        return ret;
    }

    @Override
    public int getLife()
    {
        return _life;
    }
    
    public void removeFromBoard(Card card)
    {
        _cardsOnBoard.remove(card);
    }

    @Override
    public void attack(Card attacker, Card defender)
    {
        defender.dealDamage(attacker.getPower());
        attacker.dealDamage(defender.getPower());
        System.out.println("+ " + attacker.getName() + " attacks " + defender.getName());
    }

    @Override
    public String getName()
    {
        return _id;
    }

    @Override
    public void dealDammage(int dammages)
    {
        _life -= dammages;
    }

    @Override
    public void attack(Card attacker, Player defender)
    {
        defender.dealDammage(attacker.getPower());
        System.out.println("+ " + attacker.getName() + " attacks " + defender.getName());
    }

    public void discardIfNeeded(Configuration configuration) {
        if(_cardsInHand.size()>configuration.getMaxCardsInHand()) {
            Card toRemove = null;
            for(Card c : _cardsInHand) {
                if(toRemove==null || ((configuration.isHearthstone()||c.getCost()!=7)&&(c.getCost()>toRemove.getCost()||c.getCost()==0)))
                {
                    toRemove = c;
                }
            }
            _cardsInHand.remove(toRemove);
            _game.getHistory().addAction(new ActionDiscardCard(_game.getTurn(), this, toRemove));
        }
    }
}
