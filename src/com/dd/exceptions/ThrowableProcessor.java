package com.dd.exceptions;

/**
 * User: ddiebold
 * Date: 18/02/13
 * Time: 10:02
 */
public interface ThrowableProcessor
{

    public boolean matches(Throwable t);

    public boolean doIgnore();

    public boolean doQuit();
}
