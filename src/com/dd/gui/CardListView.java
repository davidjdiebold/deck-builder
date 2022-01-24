package com.dd.gui;


import com.dd.builder.DeckBuilder;
import com.dd.model.Card;
import com.dd.usc.UseCaseOptimizeManaCurve;

import javax.swing.*;

import static java.awt.GridBagConstraints.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.*;


public class CardListView {
    private boolean _readonly = false;

    private final Data _data;

    private SortedMap<Card, Integer> _selection = new TreeMap<Card, java.lang.Integer>();

    private JComboBox<Card> _cards;
    private JButton _addCard;
    private JButton _reset;

    private final JPanel _panel = new JPanel(new GridBagLayout());

    public CardListView(final Data data) {
        _data = data;
        initialize();
    }

    public void initialize() {
        refreshCombobox();

        _addCard = ComponentFactory.createButton(Color.GREEN, "add.png", 16);
        _addCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addSelectedCard();
            }
        });
        _addCard.setToolTipText(Help.ADD_FIXED_CARD);


        _reset = ComponentFactory.createButton(Messages.CARDLIST_BUTTON_RESET, "cancel.png", 16);
        _reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                _selection.clear();
                updateLayout();
                Logs.log("ACTION : forced cards list was reset");
            }
        });


        updateLayout();
    }

    private void refreshCombobox() {
        List<Card> cards = _data.listCards();
        _cards = new JComboBox<Card>(cards.toArray(new Card[cards.size()]));
        _cards.setMaximumRowCount(Math.min(cards.size(), 20));
        if (!_readonly) {
            _cards.setToolTipText(Help.ADD_FIXED_CARD);
            _cards.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(final ItemEvent e) {
                    addCard((Card)_cards.getSelectedItem(), 1);
                }
            });
        }
    }

    private void addSelectedCard() {
        Card card = (Card) _cards.getSelectedItem();
        addCard(card, +1);
    }

    public void addCard(final Card card, final int offset) {
        Logs.log("ACTION : added " + offset + " cards " + card.getName());
        Integer val = _selection.get(card);
        val = val == null ? offset : val + offset;
        if (val == 0) {
            _selection.remove(card);
        } else {
            int count = offset;
            for (Integer i : _selection.values()) {
                count += i;
            }
            //Todo Invalidate button ?
            if (count <= _data.getConfiguration().getDeckSize()) {
                _selection.put(card, val);
            }
        }
        updateLayout();
    }

    public void refresh() {
        refreshCombobox();
        updateLayout();
    }

    private void updateLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(ComponentFactory.createLabel(Messages.CARDLIST_LABEL_ADD), new GridBagConstraints(0, 0, 1, 1, 1, 1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));
        panel.add(_cards, new GridBagConstraints(1, 0, 1, 1, 100, 1, CENTER, BOTH, MainFrame.FINE_INSETS, 0, 0));

        Dimension size = new Dimension(250, 800);
        _panel.setMinimumSize(size);
        _panel.setMaximumSize(size);
        _panel.setPreferredSize(size);
        _panel.setSize(size);
        _panel.removeAll();
        if (!_readonly) {
            _panel.add(panel, new GridBagConstraints(0, 0, 4, 1, 1, 1, NORTH, HORIZONTAL, MainFrame.FINE_INSETS, 0, 0));
        }

        JPanel listContainer = new JPanel(new GridBagLayout());

        int index = 1;
        for (final Card card : _selection.keySet()) {
            JLabel label = ComponentFactory.createLabel(".");
            if (!_readonly) {
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        addCard(card, e.getButton() == MouseEvent.BUTTON1 ? +1 : -1);
                    }
                });
            }
            CardInList container = new CardInList(card.getName(), card.getCost(), _selection.get(card), _readonly);
            if (!_readonly) {
                container.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        addCard(card, e.getButton() == MouseEvent.BUTTON1 ? +1 : -1);
                    }
                });
            }
            container.add(label);
            if (!_readonly) {
                label.setToolTipText(Help.ADD_REMOVE_CARD);
                container.setToolTipText(Help.ADD_REMOVE_CARD);
            }

            listContainer.add(container, new GridBagConstraints(0, index++, 1, 1, 1, 1, CENTER, HORIZONTAL, MainFrame.FINE_INSETS, 0, 0));

        }

        listContainer.add(ComponentFactory.createLabel(""), new GridBagConstraints(0, index++, 1, 1, 100, 100, NORTH, BOTH, MainFrame.FINE_INSETS, 0, 0));

        JScrollPane pane = new JScrollPane(listContainer);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        _panel.add(listContainer, new GridBagConstraints(0, 1, 1, 1, 100, 100, NORTH, BOTH, MainFrame.FINE_INSETS, 0, 0));
        if (!_readonly) {
            _panel.add(_reset, new GridBagConstraints(0, 2, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        }
        _reset.setEnabled(_selection.size() > 0);
        _panel.revalidate();
        _panel.repaint();
    }

    public JComponent getComponent() {
        return _panel;
    }

    public List<Card> getFixedCards() {
        List<Card> ret = new ArrayList<Card>();
        for (Card drawer : _selection.keySet()) {
            for (int i = 0; i < _selection.get(drawer); i++) {
                ret.add(drawer);
            }
        }
        return ret;
    }

    public boolean isReadonly() {
        return _readonly;
    }

    public void setReadonly(final boolean readonly) {
        _readonly = readonly;
    }

    public void setDeck(final UseCaseOptimizeManaCurve useCase) {
        _selection.clear();
        List<Card> cards = useCase.getImposedCards();
        for (Card card : cards) {
            addCard(card, 1);
        }
        refresh();
    }
}
