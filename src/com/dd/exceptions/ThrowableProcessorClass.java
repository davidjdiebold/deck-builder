package com.dd.exceptions;


/**
 * User: ddiebold
 * Date: 18/02/13
 * Time: 10:02
 */
public class ThrowableProcessorClass implements ThrowableProcessor
{

    private final Class _class;

    private final boolean _ignore;

    private final boolean _doQuit;

    public ThrowableProcessorClass(Class aClass, boolean ignore, boolean doQuit)
    {
        _class = aClass;
        _ignore = ignore;
        _doQuit = doQuit;
    }

    public boolean matches(Throwable t)
    {
        return t.getClass().equals(_class);
    }

    public boolean doIgnore()
    {
        return _ignore;
    }

    public boolean doQuit()
    {
        return _doQuit;
    }
}