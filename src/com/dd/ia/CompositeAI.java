package com.dd.ia;

import com.dd.model.Game;
import com.dd.model.Player;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 12:34
 */
public class CompositeAI implements PlayerAI
{
    private final PlayerAI[] _ais;

    public CompositeAI(PlayerAI[] ais)
    {
        _ais = ais;
    }

    @Override
    public void playOneTurn(Game game, Player player)
    {
        for(PlayerAI ai : _ais)
        {
            ai.playOneTurn(game, player);
        }
    }
}
