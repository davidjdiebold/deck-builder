package com.dd.maths;

import java.util.Iterator;

/**
 * User: DD
 * Date: 02/12/13
 * Time: 20:02
 */
public interface Domain
{
    public ExtendedIterator<double[]> buildIterator();

    public int size();
}
