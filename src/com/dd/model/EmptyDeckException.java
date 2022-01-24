package com.dd.model;

/**
 * User: DD
 * Date: 03/11/13
 * Time: 09:25
 */
public class EmptyDeckException extends RuntimeException
{
    public EmptyDeckException(String message)
    {
        super(message);
    }
}
