package com.dd.gui;

import com.esotericsoftware.minlog.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Logs
{
    private final static File _logs = new File("../data/logs.txt");

    private final static Logger _logger ;

    static {
        _logger = Logger.getLogger("logger");
        try
        {
            FileHandler handler = new FileHandler(_logs.getAbsolutePath());
            handler.setFormatter(new SimpleFormatter());
            _logger.addHandler(handler);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void log(Throwable t)
    {
        t.printStackTrace();
        _logger.log(Level.WARNING, "error", t);
    }

    public static void log(String string)
    {
        _logger.log(Level.INFO, string);
    }

}
