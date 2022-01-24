package com.dd.gui;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/25/14
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class HighlightTicker extends Thread
{
    public HighlightTicker(final Hoverable hoverable)
    {
        super(new Runnable()
        {
            @Override
            public void run()
            {
                double time = 1.5;
                hoverable.setHighLight(time/10./1.5);
                while (time > 0)
                {
                    try
                    {
                        sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    time -= 0.1;
                    hoverable.setHighLight(time / 10./ 1.5);
                }
            }
        });
    }
}
