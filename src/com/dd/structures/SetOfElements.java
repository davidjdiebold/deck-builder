package com.dd.structures;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class SetOfElements<T>  implements Iterable<T>
{
    private final Map<T,Integer> _elements = new LinkedHashMap<T, Integer>();

    public void add(T element, int quantity)
    {
        Integer ini = _elements.get(element);
        Integer newQ = Integer.valueOf(ini == null ? quantity : ini + quantity);
        if(newQ==0)
        {
            _elements.remove(element);
        }
        else
        {
            _elements.put(element, newQ);
        }
    }

    public boolean remove(T element)
    {
        Integer ini = _elements.get(element);
        if(ini!=null && ini>0)
        {
            Integer newQ = Integer.valueOf(ini - 1);
            _elements.put(element, newQ);
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new MyIterator();
    }

    class MyIterator implements Iterator<T>
    {
        private Map.Entry<T,Integer> _current;

        private int _indexElement = 0;
        private final Iterator<Map.Entry<T, Integer>> _myIterator;

        MyIterator()
        {
            _myIterator = (Iterator<Map.Entry<T, Integer>>) _elements.entrySet().iterator();
        }

        @Override
        public boolean hasNext()
        {
            return _myIterator.hasNext() || (_current!=null && _indexElement < _current.getValue()-1);
        }

        @Override
        public T next()
        {
            if(_current==null)
            {
                if(_myIterator.hasNext())
                {
                    _current = _myIterator.next();
                    _indexElement = 0;
                    return _current.getKey();
                }
                else
                {
                    throw new IndexOutOfBoundsException();
                }
            }
            else if(_indexElement < _current.getValue()-1)
            {
                _indexElement++;
                return _current.getKey();
            }
            else if(_myIterator.hasNext())
            {
                _current = _myIterator.next();
                _indexElement = 0;
                return _current.getKey();
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
