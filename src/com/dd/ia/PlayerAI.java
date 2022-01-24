package com.dd.ia;

import com.dd.model.Game;
import com.dd.model.Player;
import com.dd.model.PlayerImpl;

/**
 * User: DD
 * Date: 02/11/13
 * Time: 16:05
 */
public interface PlayerAI
{
    public void playOneTurn(Game game, Player player);
}
