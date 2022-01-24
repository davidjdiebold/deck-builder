package com.dd.exceptions;

import com.dd.gui.Logs;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;


/**
 * Used to catch all Throwables, and react appropriately. Some errors might be ignored whereas others should stop the application...
 * If no processor is found, error is ignored.
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{

    private static boolean _aboutToExit = false;

    private int _maxStackTraceSize = 5;

    private final List<ThrowableProcessor> _processors = new ArrayList<ThrowableProcessor>();

    private JFrame _mainFrame;

    public UncaughtExceptionHandler(final JFrame mainFrame)
    {
        _mainFrame = mainFrame;
        initializeProcessors();
    }

    protected void initializeProcessors()
    {
        //Because of a problem in Reporting view !
        _processors.add(new ThrowableProcessorPattern("javafx.embed.swing.JFXPanel", 5, true, false));
        _processors.add(new ThrowableProcessorClass(OutOfMemoryError.class, false, true));
        _processors.add(new ThrowableProcessorClass(NullPointerException.class, false, false));
        _processors.add(new ThrowableProcessorClass(ClassCastException.class, false, false));
    }

    public void addProcessor(ThrowableProcessor processor)
    {
        _processors.add(0, processor);
    }

    public void uncaughtException(final Thread t, final Throwable e)
    {
        uncaughtException(t, e, getDefaultParentComponent());
    }

    private Component getDefaultParentComponent()
    {
        return _mainFrame;
    }

    public void uncaughtException(final Thread t, final Throwable e, final Component triggeringComponent)
    {
        e.printStackTrace();

        String message = "Error Occured";
        Logs.log(message);
        if (SwingUtilities.isEventDispatchThread())
        {
            showException(e, triggeringComponent);
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    showException(e, triggeringComponent);
                }
            });
        }
    }


    /**
     * Opens a dialog where the exception is shown.
     *
     * @param throwable exception
     */
    public void showException(Throwable throwable, Component triggeringComponent)
    {
        if (_aboutToExit)
        {
            return;
        }
        try
        {
            ThrowableProcessor processor = getProcessor(throwable);
            if (processor.doQuit())
            {
                _aboutToExit = true;
            }

            if (processor.doIgnore())
            {
                return;
            }

            StringBuilder messageToDisplay = buildMessageToDisplay(throwable, processor.doQuit() ? _maxStackTraceSize : 0);
            JOptionPane.showMessageDialog(triggeringComponent, messageToDisplay, "", JOptionPane.ERROR_MESSAGE);
            Logs.log("Closing Application : " + messageToDisplay.toString());
            throwable.printStackTrace();

            if (processor.doQuit())
            {
                //TODO Sauvegarde quand même ?
                System.exit(1);
            }
        }
        catch (Throwable fatal)
        {
            // Nothing we can do...
            System.err.println("Fatal error - Aborting");
            fatal.printStackTrace(System.err);
            Logs.log("Closing application : " + fatal);
            System.exit(1);
        }
    }

    public static StringBuilder buildMessageToDisplay(Throwable throwable, int maxStackTraceSize)
    {
        StringBuilder messageToDisplay = new StringBuilder();
        appendLine(messageToDisplay, throwable.getClass().getSimpleName() + " : ");
        appendLine(messageToDisplay, "Unexpected error : ");
        appendLine(messageToDisplay, throwable);
        appendLine(messageToDisplay, "");

        for (int i = 0; i < Math.min(maxStackTraceSize - 1, throwable.getStackTrace().length); i++)
        {
            appendLine(messageToDisplay, throwable.getStackTrace()[i]);
        }
        return messageToDisplay;
    }

    public static void appendLine(StringBuilder builder, Object object)
    {
        builder.append(object);
        builder.append("\r\n");
    }

    private ThrowableProcessor getProcessor(Throwable throwable)
    {
        for (ThrowableProcessor processor : _processors)
        {
            if (processor.matches(throwable))
            {
                return processor;
            }
        }
        return new ThrowableProcessorAll();
    }

    public int getMaxStackTraceSize()
    {
        return _maxStackTraceSize;
    }

    public void setMaxStackTraceSize(final int maxStackTraceSize)
    {
        _maxStackTraceSize = maxStackTraceSize;
    }
}

