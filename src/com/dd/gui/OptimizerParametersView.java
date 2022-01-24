package com.dd.gui;


import com.dd.usc.UseCaseOptimizeManaCurve;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

import static java.awt.GridBagConstraints.*;


public class OptimizerParametersView
{
    private JComboBox<UseCaseOptimizeManaCurve> _decks;

    private JTextField _name;

    private OptimizerView _parent;

    private Data _data;

    private JSlider _turn;

    private JSlider _games;
    private JComboBox _heroPower;

    private JPanel _panel = new JPanel(new GridBagLayout());

    public OptimizerParametersView(Data data, OptimizerView parent)
    {
        _data =data;
        _parent = parent;
        initialize();
        initFocus();
    }

    public void initFocus()
    {
        _name.addHierarchyListener(new HierarchyListener() {
                public void hierarchyChanged(HierarchyEvent e) {
                    final Component c = e.getComponent();
                    if (c.isShowing() && (e.getChangeFlags() &
                        HierarchyEvent.SHOWING_CHANGED) != 0) {
                        Window toplevel = SwingUtilities.getWindowAncestor(c);
                        toplevel.addWindowFocusListener(new WindowAdapter() {
                            public void windowGainedFocus(WindowEvent e) {
                                _name.requestFocus();
                                _name.selectAll();
                            }
                        });
                    }
                }
            });

        _name.requestFocusInWindow();
    }

    public void refreshDeck()
    {
        setDeck((UseCaseOptimizeManaCurve) _decks.getSelectedItem());
    }

    public void initialize()
    {
        initializeDeckCbbBox();

        _name = new JTextField();
        _name.setText(""+new Date());
        _name.setColumns(20);

        _turn = new JSlider(6,30,10);
        Font f = _turn.getFont();
        _turn.setPaintLabels(true);
        Dictionary<Integer,JComponent> dic = new Hashtable<Integer, JComponent>();
        for(int i = 6 ; i <= 30 ; i+=2)
        {
            dic.put(i,ComponentFactory.createLabel(""+i));
        }
        _turn.setLabelTable(dic);
        _turn.setToolTipText(Help.TURN);

        _games = new JSlider(100,1000,550);
        //_games.setExtent(100);
        _games.setPaintLabels(true);
        Dictionary<Integer,JComponent> dic2 = new Hashtable<Integer, JComponent>();
        dic2.put(100,ComponentFactory.createLabel("Low"));
        dic2.put(550,ComponentFactory.createLabel("Medium"));
        dic2.put(1000,ComponentFactory.createLabel("High"));
        _games.setLabelTable(dic2);
        _games.setToolTipText(Help.PRECISION);

        _heroPower = new JComboBox(new String[]{"Paladin", "Warlock"});
        ComponentFactory.adjust(_heroPower);
        _heroPower.setToolTipText(Help.HERO_POWER);

        layout();
    }

    private void layout()
    {
        int index = 0;
        _panel.removeAll();
        //_panel.add(ComponentFactory.createLabel("Deck Name"), new GridBagConstraints(0,index,1,1,1,1,WEST,NONE, MainFrame.FINE_INSETS,0,0));
        //_panel.add(_name, new GridBagConstraints(1,index++,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(ComponentFactory.createLabel("Hero Power"), new GridBagConstraints(0,index,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(_heroPower, new GridBagConstraints(1,index++,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(ComponentFactory.createLabel("Peak Turn"), new GridBagConstraints(0,index,1,1,1,1,WEST,NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(_turn, new GridBagConstraints(1,index++,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(ComponentFactory.createLabel("Precision"), new GridBagConstraints(0,index,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(_games, new GridBagConstraints(1,index++,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));

        if(_decks.getItemCount()>0)
        {
        _panel.add(ComponentFactory.createLabel("Initialize from"), new GridBagConstraints(0,index,1,1,1,1,WEST,NONE, MainFrame.FINE_INSETS,0,0));
        _panel.add(_decks, new GridBagConstraints(1,index++,1,1,1,1, WEST, NONE, MainFrame.FINE_INSETS,0,0));
        }
        _panel.add(ComponentFactory.createLabel(""), new GridBagConstraints(0,index++,3,1,100,100, CENTER, BOTH, MainFrame.FINE_INSETS,0,0));
        _panel.revalidate();
        _panel.repaint();
    }

    private void initializeDeckCbbBox()
    {
        Set<UseCaseOptimizeManaCurve> decks = _data.getUseCases();
        _decks = new JComboBox<UseCaseOptimizeManaCurve>(decks.toArray(new UseCaseOptimizeManaCurve[decks.size()]));
        _decks.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(final ItemEvent e)
            {
                if(e.getStateChange()==ItemEvent.SELECTED)
                {
                    refreshDeck();
                }
            }
        });
    }

    private void setDeck(final UseCaseOptimizeManaCurve useCase)
    {
        if(useCase!=null)
        {
            _games.setValue(useCase.getNbGames());
            _heroPower.setSelectedItem(useCase.getHero());
            _turn.setValue(useCase.getNB_MAX_TURNS());
            _name.setToolTipText(useCase.getConfigurationName());
            _parent.setDeck(useCase);
        }
    }

    public JComponent getComponent()
    {
        return _panel;
    }

    public int getTurn()
    {
        return _turn.getValue();
    }

    public int getGames()
    {
        return _games.getValue();
    }

    public String getHero()
    {
        return (String) _heroPower.getSelectedItem();
    }

    public String getConfigurationName()
    {
        return _name.getText();
    }

    public static void changeFont(Component component, Font font) {
            component.setFont(font);
            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    if(child.getName().equals("myComponentName")){
                    //set font to the component
                    }else{
                    changeFont(child, font);
                    }
                }
            }
        }

    public void refresh()
    {
        initializeDeckCbbBox();
        layout();
    }
}
