package com.dd.exceptions;

public class ThrowableProcessorAll implements ThrowableProcessor
{

    @Override
    public boolean matches(final Throwable t)
    {
        return true;
    }

    @Override
    public boolean doIgnore()
    {
        return false;
    }

    @Override
    public boolean doQuit()
    {
        return true;
    }
}
