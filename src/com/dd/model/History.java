package com.dd.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 18:15
 */
public class History
{
    private final boolean _debug;
    private List<HistorizedAction> _actions = new ArrayList<HistorizedAction>();

    public History(boolean debug)
    {
        _debug = debug;
    }

    public void addAction(HistorizedAction action)
    {
        _actions.add(action);
    }

    public List<HistorizedAction> listActions()
    {
        return _actions;
    }
}
