package com.dd.gui;

import java.util.Observable;

public class NoArgObservable extends Observable
{
    public void trigger()
    {
        setChanged();
        notifyObservers();
        clearChanged();
    }

}
