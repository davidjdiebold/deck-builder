package com.dd.gui;


import com.dd.gui.actions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TabbedMenuBar implements IMenuBar
{

    private final int MAIN_MENU_WIDTH = 130;
    private final int ICON_SIZE = 40;

    private Data _data;

    private JFrame _frame;

    private Map<JComponent,Boolean> _components = new LinkedHashMap<JComponent, Boolean>();

    private JPanel _panel = new JPanel(new GridBagLayout());

    private JTabbedPane _pane = new JTabbedPane();

    private boolean _undecorated = true;

    private JButton _mainMenu;

    public TabbedMenuBar(final JFrame frame, Data data)
    {
        _data = data;

        ImageIcon image = new ImageIcon(ImageLoader.loadBufferedImage("logoCog_96.png").getScaledInstance(24, 24, -1));
        _mainMenu = new JButton("Deck Builder", image);
        _mainMenu.setForeground(Color.white);
        Dimension size = new Dimension(MAIN_MENU_WIDTH,30);
        _mainMenu.setPreferredSize(size);
        _mainMenu.setMinimumSize(size);
        _mainMenu.setMaximumSize(size);
        _mainMenu.setSize(size);
        _mainMenu.setBackground(Color.ORANGE.darker());
        _mainMenu.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                JPopupMenu menu = new JPopupMenu();
                menu.add(new ActionOpenWebBrowser(WebPanel.URL_MODE.INTERNET, Urls.WEBSITE, true));
                menu.add(new ActionReleaseNote());
                menu.show(_mainMenu, 2, _mainMenu.getHeight()-4);
            }
        });

        _frame = frame;
        _pane.setUI(new SynthTabbedPaneUI()
        {
            private final Insets borderInsets = new Insets(0, MAIN_MENU_WIDTH+20, 0, 40);

            @Override
            protected Insets getTabAreaInsets(final int tabPlacement)
            {
                return borderInsets;
            }
        });
    }

    @Override
    public void registerTab(final String iconON, final String iconOFF, final MenuBarAction view)
    {
        if(view instanceof MainFrame.ShowView)
        {
            ImageIcon icon = new ImageIcon(ImageLoader.loadBufferedImage(iconON).getScaledInstance(ICON_SIZE, ICON_SIZE, 0));
            View theView = ((MainFrame.ShowView) view).getView();
            String title = ((MainFrame.ShowView) view).getTitle();
            String htmlTitle = "<html><b>" + title + "     " + "</b></html>";
            _pane.addTab(htmlTitle, icon, theView.getComponent());
            _pane.setForegroundAt(_pane.getTabCount()-1, Palette.C_GOLD);
        }
    }

    @Override
    public void registerComponent(final JComponent component, final boolean anchorNorth)
    {
        _components.put(component, anchorNorth);
    }

    @Override
    public JComponent getComponent()
    {
        return _pane;
    }

    @Override
    public void selectTab(final int index)
    {
        _pane.setSelectedIndex(index);
        _data.getSettings().setSelectedTab(index);
        Logs.log("ACTION : selected tab : " + index);
    }

    @Override
    public int getCurrentView()
    {
        return _pane.getSelectedIndex();
    }

    @Override
    public void setUndecorated(final boolean undecorated)
    {
        _undecorated = undecorated;
    }

    @Override
    public void setFrame(final JFrame frame)
    {
        _frame = frame;
    }

    @Override
    public void initialize()
    {
        _pane.setForeground(Palette.C_GOLD);
        _pane.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                _data.getSettings().setSelectedTab(_pane.getSelectedIndex());
            }
        });

        _panel.add(_pane, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

        Container glassPane = (Container) _frame.getRootPane().getGlassPane();
        glassPane.setVisible(true);
        glassPane.setLayout(new GridBagLayout());

        boolean first = true;
        int index = 0;

        glassPane.add(_mainMenu, new GridBagConstraints(index++,0,1,1,1,1,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-1,10,0,0), 0, 0));
        glassPane.add(ComponentFactory.createLabel(""), new GridBagConstraints(index++,0,1,1,100,100,GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
        for(JComponent cmp : _components.keySet())
        {
            if(_undecorated || first)
            {
                int margin = (ICON_SIZE - cmp.getHeight()) / 2;
                Insets insets = new Insets(_components.get(cmp) ? 0 : margin,0,0,margin);
                glassPane.add(cmp, new GridBagConstraints(index++,0,1,1,1,1,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, insets, 0, 0));
            }
            first = false;
        }
    }
}
