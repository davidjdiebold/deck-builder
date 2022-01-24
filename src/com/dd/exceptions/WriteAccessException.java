package com.dd.exceptions;

public class WriteAccessException extends RuntimeException
{
    public WriteAccessException(String path)
    {
        super("No write permissions in folder '" + path + "'.");
    }
}
