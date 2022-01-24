package com.dd.gui;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NONE;


public class MenuBar implements IMenuBar
{
    private boolean _silent = false;
    private final List<ButtonAndAction> _actions = new ArrayList<ButtonAndAction>();
    private final JPanel _panel = new JPanel(new GridBagLayout());
    private List<JComponent> _otherComponents = new ArrayList<JComponent>();

    public void registerTab(String icon, String iconWhenSelected, final MenuBarAction action)
    {
        int height = 48;
        final Image off = ImageLoader.loadBufferedImage(icon).getScaledInstance(height, height, Image.SCALE_DEFAULT);
        final Image on =  ImageLoader.loadBufferedImage(iconWhenSelected).getScaledInstance(height, height, Image.SCALE_DEFAULT);

        final JToggleButton button = new JToggleButton(new ImageIcon(off));
        Dimension size = new Dimension(height, height);
        button.setPreferredSize(size);
        button.setSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setTransferHandler(new TransferHandler("FixedCard"));

        ButtonAndAction toAdd = new ButtonAndAction(new ImageIcon(on), new ImageIcon(off), button, action);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                if(button.isSelected() && !_silent)
                {
                    _silent = true;
                    for(ButtonAndAction action : _actions)
                    {
                        if(action._button.equals(button))
                        {
                            action._button.setIcon(action._on);
                            action._action.onSelection();
                        }
                        else
                        {
                            action._button.setSelected(false);
                            action._button.setIcon(action._off);
                        }
                    }
                    _silent = false;
                }
            }
        });
        _actions.add(toAdd);
    }

    @Override
    public void selectTab(final int index)
    {
    }

    @Override
    public int getCurrentView()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initialize()
    {
        int index = 0;
        for(ButtonAndAction b : _actions)
        {
            _panel.add(b._button, new GridBagConstraints(index++,0,1,1,1,1, CENTER, NONE, MainFrame.FINE_INSETS, 0, 0));
        }
        _panel.add(ComponentFactory.createLabel(""), new GridBagConstraints(index++,0,1,1,100,1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));
        for(JComponent c : _otherComponents)
        {
            _panel.add(c, new GridBagConstraints(index++,0,1,1,1,1, CENTER, NONE, MainFrame.FINE_INSETS, 0, 0));
        }
    }

    public JComponent getComponent()
    {
        return _panel;
    }

    @Override
    public void setFrame(final JFrame frame)
    {
    }

    @Override
    public void setUndecorated(final boolean undecorated)
    {
    }

    @Override
    public void registerComponent(final JComponent component, final boolean anchorNorth)
    {
        _otherComponents.add(component);
    }

    class ButtonAndAction
    {
        private final ImageIcon _on;
        private final ImageIcon _off;
        private final JToggleButton _button;
        private final MenuBarAction _action;

        ButtonAndAction(final ImageIcon on, final ImageIcon off, final JToggleButton button, final MenuBarAction action)
        {
            _on = on;
            _off = off;
            _button = button;
            _action = action;
        }
    }

}
