package com.dd.model;

/**
 * Created by IntelliJ IDEA.
 * User: DD
 * Date: 02/11/13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public interface Game
{
    Player getOpponent(Player player);

    History getHistory();

    int getTurn();

    int getNbMaxTurns();

    Player getPlayer(String name);

    boolean isHearthstone();
}
