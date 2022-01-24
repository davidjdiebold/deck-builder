package com.dd.builder;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 2/24/14
 * Time: 9:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class BuilderException extends RuntimeException
{
    private final Integer _cardCost;

    public BuilderException(final String message, final Integer cardCost)
    {
        super(message);
        _cardCost = cardCost;
    }

    public Integer getCardCost()
    {
        return _cardCost;
    }
}
