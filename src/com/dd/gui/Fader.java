package com.dd.gui;

import com.sun.awt.AWTUtilities;

import javax.swing.JDialog;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Fader implements ActionListener
{
    public static final int DELAY_BETWEEK_FADES = 40;
    public static final int TIME_BEFORE_FADE = 500;

    private final JDialog _dialog;

    private Timer _timer;

    public Fader(JDialog dialog)
    {
        _dialog = dialog;
    }

    private int _timeBeforeFade = TIME_BEFORE_FADE;

    public void actionPerformed(ActionEvent e)
    {
        float opacity = AWTUtilities.getWindowOpacity(_dialog);
        if (_timeBeforeFade > 0)
        {
            _timeBeforeFade -= DELAY_BETWEEK_FADES;
        }
        else if (opacity > 0.01f)
        {
            AWTUtilities.setWindowOpacity(_dialog, AWTUtilities.getWindowOpacity(_dialog) - 0.01f);
        }
        else
        {
            _dialog.dispose();
            _timer.stop();
        }
    }

    public void setTimer(Timer timer)
    {
        _timer = timer;
    }
}
