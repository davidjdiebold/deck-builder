package com.dd.model;

import com.dd.builder.Configuration;
import com.dd.ia.PlayerAI;

import java.util.Random;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:03
 */
public class GameImpl implements Game
{
    //Static config
    public static final int INITIAL_PLAYER_LIFE = 30;
    public static final int INITIAL_PLAYER_MANA = 0;
    public static final int INITIAL_PLAYER_MAX_MANA = 0;
    private final static boolean DEBUG = false;
    
    //User config
    private int _nbMaxTurns = 30;
    
    //Data
    private PlayerImpl _playerA;
    private PlayerImpl _playerB;
    private Random _random;

    //States
    private int _turn = 0;
    private boolean _endOfGame = false;
    private boolean _deckWasEmptied = false;
    private History _history = new History(DEBUG);

    private final Configuration _configuration;

    public GameImpl(Deck deckA, PlayerAI aiA, Deck deckB, PlayerAI aiB, Random random, Configuration configuration)
    {
        _random = random;
        _playerA = new PlayerImpl("Player A", deckA, aiA, INITIAL_PLAYER_LIFE, INITIAL_PLAYER_MANA, INITIAL_PLAYER_MAX_MANA, this);
        _playerB = new PlayerImpl("Player B", deckB, aiB, INITIAL_PLAYER_LIFE, INITIAL_PLAYER_MANA, INITIAL_PLAYER_MAX_MANA, this);
        _configuration = configuration;
    }

    public void run()
    {
        _playerA.getDeck().shuffle(_random);
        _playerA.drawCards(_configuration.getStartingPlayerInitialCards());

        _playerB.getDeck().shuffle(_random);
        _playerB.drawCards(_configuration.getStartingPlayerInitialCards()+1);
        //HACK PROBA ?
        //DESACTIVE CAR CA MARCHE PAS
        if(Math.random()<0.0 && _configuration.isHearthstone())
        {
            Card card = new CardFactory().newSpell("The Coin", 0, 1, 0);
            _playerB.getAllCardsInHand().add(card);
        }

        while(!_endOfGame && _turn <= _nbMaxTurns)
        {
            ++_turn;

            if(DEBUG)
            {
                System.out.println("Turn " + _turn);
            }
            runPlayer(_playerA, _playerB);
            updateEndOfGame();
            if(!_endOfGame)
            {
                runPlayer(_playerB, _playerA);
                updateEndOfGame();
            }
            if(DEBUG)
            {
                printBoard();
            }
        }

        if(_endOfGame && DEBUG)
        {
            Player winner = getWinner();
            System.out.println("Winner : " + winner.getName() + " (" + winner.getLife() + " life left)");
        }
    }

    public Player getWinner()
    {
        if(!_endOfGame)
        {
            throw new RuntimeException("Game is not ended !");
        }
        return _playerA.getLife()>0 && _playerA.getDeck().size()>0 ? _playerA : _playerB;
    }
    
    private void updateEndOfGame()
    {
        _endOfGame |= _playerA.getLife()<=0 || _playerB.getLife()<=0;
    }

    private void runPlayer(PlayerImpl player, PlayerImpl opponent)
    {
        try
        {
            player.drawCards(1);
            player.updateMana();
            player.updateCreaturesState();
            player.playOneTurn();
            player.discardIfNeeded(_configuration);

        } catch (EmptyDeckException e)
        {
            _deckWasEmptied = true;
            opponent.setWinner(true);
            player.setWinner(false);
            _endOfGame = true;
        }
    }

    @Override
    public boolean isHearthstone() {
        return _configuration.isHearthstone();
    }

    @Override
    public Player getPlayer(String name)
    {
        if(_playerA.getName().equals(name))
        {
            return _playerA;
        }
        else
        {
            return _playerB;
        }
    }

    @Override
    public Player getOpponent(Player player)
    {
        return _playerA.equals(player) ? _playerB : _playerA;
    }

    @Override
    public int getNbMaxTurns()
    {
        return _nbMaxTurns;
    }

    @Override
    public int getTurn()
    {
        return _turn;
    }

    @Override
    public History getHistory()
    {
        return _history;
    }

    public void printBoard()
    {
        System.out.println("");
        printPlayer(_playerA);
        System.out.println("========================");
        printPlayer(_playerB);
    }

    private void printPlayer(Player player)
    {
        if(DEBUG)
        {
        System.out.println("  " + player.getName());
        System.out.println("  " + player.getLife() + " Life");
        StringBuilder hand = new StringBuilder();
        for(Card card : player.getAllCardsInHand())
        {
            hand.append(card.getDescription());
            hand.append(" - ");
        }
        StringBuilder board = new StringBuilder();
        for(Card card : player.listMinionsOnBoard(false))
        {
            board.append(card.getDescription());
            board.append(" - ");
        }
        System.out.println("  Hand : " + hand.toString());
        System.out.println("  Board : " + board.toString());
        }
    }

    public void setNbMaxTurns(int nb_max_turns)
    {
        _nbMaxTurns = nb_max_turns;
    }

    public boolean isDeckWasEmptied()
    {
        return _deckWasEmptied;
    }
}
