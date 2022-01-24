package com.dd.gui;


import javax.swing.*;
import java.awt.*;


public class LightweightPopup
{
    private final int _insetNorth = 60;
    private final int _inset = 15;

    private int _timerDelayInMillis = 2000;

    private /*final*/ int _orientation = GridBagConstraints.SOUTHEAST;

    private /*final*/ Dimension _popupDimension = new Dimension(300,125);

    private final Frame _parentFrame;

    public LightweightPopup(Frame parentFrame)
    {
        _parentFrame = parentFrame;
    }

    /**
     * @param parentFrame
     * @param orientation use constants defined in GridBagConstraints class : SOUTHEAST is used by default
     */
    public LightweightPopup(Frame parentFrame, int orientation)
    {
        _parentFrame = parentFrame;
        _orientation = orientation;
    }

    public LightweightPopup(Frame parentFrame, int orientation, Dimension popupDimension)
    {
        _parentFrame = parentFrame;
        _orientation = orientation;
        _popupDimension = popupDimension;
    }

    private void prepareFrame(Icon icon, String info)
    {
            String labelContent = wrapInHtmlForAutoLineBreak(info);

            final JDialog dialog = new JDialog(_parentFrame);
            dialog.setSize(_popupDimension);
            dialog.setAlwaysOnTop(true);
            dialog.setLocation(getXLocation(dialog), getYLocation(dialog));
            dialog.setUndecorated(true);
            dialog.setLayout(new GridBagLayout());


            GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1., 1., GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0);

            JLabel headingLabel = new JLabel(labelContent);
            headingLabel.setFont(new Font(headingLabel.getFont().getName(), headingLabel.getFont().getStyle(), 14));
            headingLabel.setIcon(icon);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(180, 180, 180));
            JPanel innerPanel = new JPanel(new GridBagLayout());
            innerPanel.add(headingLabel, new GridBagConstraints(0, 0, 1, 1, 1., 1., GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
            panel.add(innerPanel, new GridBagConstraints(0, 0, 1, 1, 1., 1., GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
            dialog.add(panel, constraints);

            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialog.setVisible(true);

            Fader fader = new Fader(dialog);
            Timer timer = new Timer(40, fader);
            fader.setTimer(timer);
            timer.start();

    }

    private String wrapInHtmlForAutoLineBreak(String origin)
    {
        return "<html><p>" + origin + "</p></html>";
    }

    public void setTimerDelayInMillis(int delayInMillis)
    {
        _timerDelayInMillis = delayInMillis;
    }

    public void hideStatus()
    {
    }

    public void showStatus(Icon icon, String informations)
    {
            prepareFrame(icon, informations);
    }

    private int getXLocation(JDialog dialog)
    {
        if (_parentFrame == null)
        {
            return 0;
        }
        if(_orientation==GridBagConstraints.SOUTHEAST || _orientation==GridBagConstraints.EAST  || _orientation==GridBagConstraints.NORTHEAST)
        {
            return (int)(_parentFrame.getLocation().getX() + _parentFrame.getWidth() - dialog.getWidth() - _inset);
        }
        else if(_orientation==GridBagConstraints.SOUTH || _orientation==GridBagConstraints.CENTER || _orientation==GridBagConstraints.NORTH)
        {
            return (int)(_parentFrame.getLocation().getX() + _parentFrame.getWidth() / 2 - dialog.getWidth() / 2 - _inset);
        }
        else if(_orientation==GridBagConstraints.SOUTHWEST || _orientation==GridBagConstraints.WEST || _orientation==GridBagConstraints.NORTHWEST)
        {
            return (int)(_parentFrame.getLocation().getX() + _inset);
        }
        else
        {
            throw new RuntimeException("not implemented");
        }
    }

    private int getYLocation(JDialog dialog)
    {
        if (_parentFrame == null)
        {
            return 0;
        }
        if(_orientation==GridBagConstraints.SOUTHEAST || _orientation==GridBagConstraints.SOUTH || _orientation==GridBagConstraints.SOUTHWEST)
        {
            return (int)(_parentFrame.getLocation().getY() + _parentFrame.getHeight() - dialog.getHeight() - _inset);
        }
        else if(_orientation==GridBagConstraints.WEST || _orientation==GridBagConstraints.CENTER || _orientation==GridBagConstraints.EAST)
        {
            return (int)(_parentFrame.getLocation().getY() + _parentFrame.getHeight() / 2 - dialog.getHeight() / 2 - _inset);
        }
        else if(_orientation==GridBagConstraints.NORTHWEST || _orientation==GridBagConstraints.NORTH || _orientation==GridBagConstraints.NORTHEAST)
        {
            return (int)(_parentFrame.getLocation().getY() + _insetNorth);
        }
        else
        {
            throw new RuntimeException("Notimplemented");
        }
    }
}

