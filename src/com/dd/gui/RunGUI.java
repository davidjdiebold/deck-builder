package com.dd.gui;


import com.dd.exceptions.UncaughtExceptionHandler;
import com.dd.exceptions.WriteAccessException;
import com.dd.utils.MailSender;

import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;


public class RunGUI
{
    public static void main(String[] args)
    {
        try
        {
            NimbusLookAndFeel laf = new NimbusLookAndFeel();
            UIManager.setLookAndFeel(laf);
            UIDefaults defaults = laf.getDefaults();
        }
        catch (Exception e)
        {
        }

        File prefFiles = new File("../data/prefs.txt");
        if(!prefFiles.isFile())
        {
            displayHtmlView();
            try
            {
                prefFiles.createNewFile();
            }
            catch (IOException e)
            {
                throw new WriteAccessException(prefFiles.getParent());
            }
        }
        else
        {
            MainFrame frame = new MainFrame();
            frame.initialize();
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(frame.getFrame()));
        }
    }

    private static void displayHtmlView()
    {
        final JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(final WindowEvent e)
            {
                displayUI();
            }
        });
        frame.setTitle("Welcome to Deck Builder !");
        WebPanel home = null;

        home = new WebPanel(Urls.TUTORIAL, WebPanel.URL_MODE.FILESYSTEM, false);
        home.initialize();
        home.getHomeClosed().addObserver(new Observer()
        {
            @Override
            public void update(final Observable o, final Object arg)
            {
                frame.setVisible(false);
                frame.dispose();
                displayUI();
            }
        });

        frame.add(home.getPanel());


        //Scrollbar
        frame.setSize(new Dimension(815,600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void displayUI()
    {
        MainFrame frame = new MainFrame();
        frame.initialize();
    }
}
