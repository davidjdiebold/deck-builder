package com.dd.gui.actions;


import com.dd.exceptions.UncaughtExceptionHandler;
import com.dd.gui.Logs;
import com.dd.gui.WebPanel;

import javax.swing.AbstractAction;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;

public class ActionOpenWebBrowser extends AbstractAction
{
    private final WebPanel.URL_MODE _urlMode;
    private final String _url;
    private final boolean _showErrors;

    public ActionOpenWebBrowser(WebPanel.URL_MODE urlMode, String string, boolean showErrors)
    {
        super("Documentation");
        _urlMode = urlMode;
        _url = string;
        _showErrors = showErrors;
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        try
        {
            if(desktop!=null)
            {
                desktop.browse(_urlMode.createURL(_url).toURI());
            }
        }
        catch (Throwable t)
        {
            if(_showErrors)
            {
                //TODO FRAME
                new UncaughtExceptionHandler(null).showException(t, null);
            }
            else
            {
                Logs.log(t);
            }
        }
    }
}
