package com.dd.maths;

import java.util.Iterator;

/**
 * User: DD
 * Date: 02/12/13
 * Time: 20:25
 */
public interface ExtendedIterator<T> extends Iterator<T>
{
    public T current();
}
