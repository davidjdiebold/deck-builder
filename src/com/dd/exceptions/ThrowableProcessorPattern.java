package com.dd.exceptions;

/**
 * User: ddiebold
 * Date: 18/02/13
 * Time: 10:03
 */
public class ThrowableProcessorPattern implements ThrowableProcessor
{

    private final String _pattern;

    private final int _level;

    private final boolean _doIgnore;

    private final boolean _doExit;

    public ThrowableProcessorPattern(String pattern, int level, boolean doIgnore, boolean doExit)
    {
        _pattern = pattern;
        _level = level;
        _doIgnore = doIgnore;
        _doExit = doExit;
    }

    public boolean matches(Throwable t)
    {
        StackTraceElement[] elements = t.getStackTrace();
        for (int i = 0; i < Math.min(_level, elements.length); i++)
        {
            StackTraceElement element = elements[i];
            if (element.toString().contains(_pattern))
            {
                return true;
            }
        }
        return false;
    }

    public boolean doIgnore()
    {
        return _doIgnore;
    }

    public boolean doQuit()
    {
        return _doExit;
    }
}
