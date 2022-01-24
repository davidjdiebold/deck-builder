package com.dd.gui;


import com.dd.model.Card;
import com.dd.model.Manacurve;
import com.dd.model.Stat;
import com.dd.usc.UseCaseOptimizeManaCurve;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;

import java.awt.event.*;
import java.util.Set;


public class OptimizerResultsView implements View
{
    private final Data _data;

    private final MainFrame _mainFrame;

    private JComboBox<UseCaseOptimizeManaCurve> _decks;
    private JButton _rename;
    private JButton _removeUnnamed;
    private JButton _remove;

    private CardListView _forcedCards;
    //private ManacurveView _curve;
    private JTable _table;

    private JPanel _panel = new JPanel(new GridBagLayout());


    public OptimizerResultsView(final Data data, MainFrame mainFrame)
    {
        _data = data;
        _mainFrame = mainFrame;
        if(_data.getUseCases().size()>0)
        {
            initialize(_data.getUseCases().iterator().next());
        }
    }

    public void initialize(UseCaseOptimizeManaCurve deck)
    {
        _rename = ComponentFactory.createButton("Rename", "rename.png", 16);
        _rename.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                rename();
            }
        });
        _rename.setToolTipText(Help.RENAME);

        _remove = ComponentFactory.createButton("Remove", "trash.png", 16);
        _remove.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                removeCurrentDeck();
            }
        });
        _remove.setToolTipText(Help.REMOVE_SELECTED);

        _removeUnnamed = ComponentFactory.createButton("Clean", "broom.png", 16);
        _removeUnnamed.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                removeUnnamed();
            }
        });
        _removeUnnamed.setToolTipText(Help.CLEAR_UNNAMED);

        if(deck == null && _decks!=null && _decks.getSelectedItem()!=null)
        {
            deck = (UseCaseOptimizeManaCurve) _decks.getSelectedItem();
        }
        Set<UseCaseOptimizeManaCurve> decks = _data.getUseCases();
        _decks = new JComboBox<UseCaseOptimizeManaCurve>(decks.toArray(new UseCaseOptimizeManaCurve[decks.size()]));
        if(deck!=null)
        {
            _decks.setSelectedItem(deck);
        }
        _decks.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(final ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    initialize((UseCaseOptimizeManaCurve) _decks.getSelectedItem());
                }
            }
        });


        TableModelOptimizerResults model = new TableModelOptimizerResults(this);
        _table = new JTable(model);
        _table.setRowHeight(80);
        _table.setFont(new Font(_table.getFont().getName(), _table.getFont().getStyle(), 36));
        _table.setDefaultRenderer(Manacurve.class, new ManacurveRenderer());
        _table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(final MouseEvent e)
            {
                int row = _table.getSelectedRow();
                selectRow(row);
            }
        });
        _table.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(final KeyEvent e)
            {
                final int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                {
                    final int row = _table.getSelectedRow();
                    selectRow(row);
                }
            }
        });

        //_curve = new ManacurveView();

        if(_data.getUseCases().size()>0)
        {
            _table.getSelectionModel().setSelectionInterval(0,0);
            selectRow(0);
        }

        JScrollPane pane = new JScrollPane(_table);

        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setBorder(BorderFactory.createTitledBorder("Best Mana Curve"));

        _forcedCards = new CardListView(_data);
        _forcedCards.setReadonly(true);
        _forcedCards.initialize();
        if(getUseCase()!=null)
        {
            for(Card card : getUseCase().getImposedCards())
            {
                _forcedCards.addCard(card, 1);
            }
        }
        _forcedCards.getComponent().setBorder(BorderFactory.createTitledBorder("Forced Cards"));


        StringBuilder builder = new StringBuilder();
        if(getUseCase()!=null)
        {
            builder.append("<html><br>");
            builder.append("Hero Power : ");
            builder.append(getUseCase().getHero());
            builder.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ");
            builder.append("Peak Turn : ");
            builder.append(getUseCase().getNB_MAX_TURNS());
            builder.append("<br>");

        boolean someGamesHaveDrawnToomuchCards = false;
        for(Stat stat : getUseCase().getResults().getResults().values())
        {
            someGamesHaveDrawnToomuchCards |= stat.getTooMuchCardsDrawn()>0;
        }
        if(someGamesHaveDrawnToomuchCards)
        {
            builder.append("<font color=\"red\">Some games have been ended prematurely. "
                           + "Maybe you should try to remove some card drawing abilities to improve your deck.</font>");
            builder.append("<br>");
        }
        builder.append("<br>");
        builder.append("</html>");
        }

        JLabel details = ComponentFactory.createLabel(builder.toString());
        details.setFont(new Font(details.getFont().getName(), details.getFont().getStyle(), details.getFont().getSize()+3));
        //details.setBorder(BorderFactory.createTitledBorder("Details"));

        JPanel runsPanel = new JPanel(new GridBagLayout());
        runsPanel.setBorder(BorderFactory.createTitledBorder("Decks"));
        runsPanel.add(ComponentFactory.createLabel("Deck"), new GridBagConstraints(0, 0, 1, 1, 1, 1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));
        runsPanel.add(_decks, new GridBagConstraints(1, 0, 1, 1, 1, 1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));
        runsPanel.add(_rename, new GridBagConstraints(2,0,1,1,1,1,CENTER, BOTH, MainFrame.FINE_INSETS,0,0));
        runsPanel.add(_remove, new GridBagConstraints(3,0,1,1,1,1,CENTER, BOTH, MainFrame.FINE_INSETS,0,0));
        runsPanel.add(_removeUnnamed, new GridBagConstraints(4,0,1,1,1,1,CENTER, BOTH, MainFrame.FINE_INSETS,0,0));
        runsPanel.add(ComponentFactory.createLabel(""), new GridBagConstraints(5,0,1,1,100,1,CENTER, BOTH, MainFrame.FINE_INSETS,0,0));
        runsPanel.add(details, new GridBagConstraints(0,1,6,1,1,1,CENTER, BOTH, MainFrame.FINE_INSETS,0,0));

        if(_data.getUseCases().size()==0)
        {
            _decks.setEnabled(false);
            _rename.setEnabled(false);
            _remove.setEnabled(false);
            _removeUnnamed.setEnabled(false);
        }

        _panel.removeAll();
        _panel.add(runsPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, CENTER, BOTH, MainFrame.MEDIUM_INSETS, 0, 0));
        //_panel.add(details, new GridBagConstraints(0, 1, 1, 1, 1, 1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(pane, new GridBagConstraints(0, 2, 1, 1, 100, 100, CENTER, BOTH, MainFrame.MEDIUM_INSETS, 0, 0));
        _panel.add(_forcedCards.getComponent(), new GridBagConstraints(1, 0, 1, 3, 1, 100, CENTER, BOTH, MainFrame.MEDIUM_INSETS, 0, 0));
    }

    private void removeCurrentDeck()
    {
        UseCaseOptimizeManaCurve item = (UseCaseOptimizeManaCurve) _decks.getSelectedItem();
        if(!item.isHasBeenRenamed() ||
            JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this deck ?",
                                "Warning", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION)
        {
            _data.getUseCases().remove(item);
            initialize((UseCaseOptimizeManaCurve) _decks.getSelectedItem());
            _mainFrame.getOptimizerView().refresh();

            new LightweightPopup(_mainFrame.getFrame(), GridBagConstraints.SOUTH, new Dimension(200,50)).showStatus(
                    new ImageIcon(ImageLoader.loadBufferedImage("mana.png")), "Deck has been removed.");
        }
    }

    private void removeUnnamed()
    {
        if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all unnamed decks ?",
                                "Warning", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION)
        {
            int nbDecksRemoved = _data.clearUnnamedDecks();
            new LightweightPopup(_mainFrame.getFrame(), GridBagConstraints.SOUTH, new Dimension(200,50)).showStatus(
                    new ImageIcon(ImageLoader.loadBufferedImage("mana.png")), nbDecksRemoved + " decks have been removed.");
            initialize((UseCaseOptimizeManaCurve) _decks.getSelectedItem());
            _mainFrame.getOptimizerView().refresh();
        }
    }

    private void rename()
    {
        String newName = JOptionPane.showInputDialog("Deck name : ");
        UseCaseOptimizeManaCurve selectedDeck = (UseCaseOptimizeManaCurve) _decks.getSelectedItem();
        selectedDeck.setConfigurationName(newName);
        selectedDeck.setHasBeenRenamed(true);

        initialize(selectedDeck);
        _mainFrame.getOptimizerView().refresh();
    }

    public void focusTable()
    {
        _table.requestFocusInWindow();
    }

    private void selectRow(final int row)
    {
        Manacurve curve = (Manacurve) getUseCase().getResults().getResults().keySet().toArray()[row];
        //_curve.setCurve(curve);
        _panel.revalidate();
        _panel.repaint();
    }

    public JComponent getComponent()
    {
        return _panel;
    }

    @Override
    public void search(final String text)
    {
    }

    @Override
    public void enter()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public UseCaseOptimizeManaCurve getUseCase()
    {
        return (UseCaseOptimizeManaCurve) _decks.getSelectedItem();
    }
}
