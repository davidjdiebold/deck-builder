package com.dd.gui;


import com.dd.model.Card;

import com.dd.model.CardFactory;
import com.dd.model.CardPowersDummy;
import com.dd.utils.CardListCsv;

import javax.swing.*;
import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LibraryView implements View
{

    //De quoi en voir sans scrollbar � taille minimale
    private static final int MAX_CARDS = 19;
    private MainFrame _frame;

    private Data _data;

    private JButton _validateModification;
    private JButton _backToLibrary;
    private JButton _removeCard;

    private JButton _addCard;
    private JButton _reset;
    private JButton _import;
    private JButton _export;

    private int _index = 0;
    private JButton _next;
    private JButton _previous;

    private JPanel _panel = new JPanel(new GridBagLayout());
    private CardModificationView _modificationView;

    private String _filter = "";


    public LibraryView(Data data, MainFrame frame)
    {
        _next = ComponentFactory.createButton("Next", "next.png", 16);
        _next.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                _index++;
                layoutLibrary();
                Logs.log("ACTION : next cards");
            }
        });
        ComponentFactory.nullifyMargin(_next);

        _previous = ComponentFactory.createButton("Previous", "previous.png", 16);
        _previous.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                _index = Math.max(_index - 1, 0);
                layoutLibrary();
                Logs.log("ACTION : previous cards");
            }
        });
        ComponentFactory.nullifyMargin(_previous);

        _addCard = ComponentFactory.createButton("Add Card", "add.png", 16);
        _addCard.setToolTipText(Help.ADD_CARD_TO_LIBRARY);
        _addCard.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final Card card = new CardFactory().newMinion("New Card...", 1, 1, 1, 1.);
                _data.listCards().add(card);
                _frame.getOptimizerView().refresh();
                openModificationView(card);
                Logs.log("ACTION : added card.");
            }
        });
        ComponentFactory.nullifyMargin(_addCard);

        _reset = ComponentFactory.createButton("Clean", "broom.png", 16);
        _reset.setToolTipText(Help.RESET);
        _reset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                if(JOptionPane.showConfirmDialog(null, "Library will be restored to its origin state. All user defined cards will be removed. Are you sure you want to clean the library ?",
                        "Warning", JOptionPane.INFORMATION_MESSAGE)==JOptionPane.OK_OPTION)
                {
                    _data.reset();
                    layoutLibrary();
                    _frame.getOptimizerView().refresh();
                }
            }
        });
        ComponentFactory.nullifyMargin(_reset);

        _import = ComponentFactory.createButton("Import", "import.jpg", 16);
        _import.setToolTipText(Help.IMPORT);
        _import.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                Logs.log("ACTION : import");
                final JFileChooser directoryDialog = new JFileChooser();
                directoryDialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnVal = directoryDialog.showOpenDialog(_panel);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    _data.listCards().clear();
                    new CardListCsv().read(directoryDialog.getSelectedFile().getAbsolutePath(), _data.listCards());
                    layoutLibrary();
                    _frame.getOptimizerView().refresh();
                    Logs.log("ACTION : import validated");
                }
            }
        });
        ComponentFactory.nullifyMargin(_import);

        _export = ComponentFactory.createButton("Export", "export.png", 16);
        _export.setToolTipText(Help.EXPORT);
        _export.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                Logs.log("ACTION : export");
                final JFileChooser directoryDialog = new JFileChooser();
                directoryDialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnVal = directoryDialog.showOpenDialog(_panel);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    new CardListCsv().write(directoryDialog.getSelectedFile().getAbsolutePath(), _data.listCards());
                    new LightweightPopup(_frame.getFrame(), GridBagConstraints.SOUTH, new Dimension(200,50)).showStatus(
                            new ImageIcon(ImageLoader.loadBufferedImage("mana.png")), "Library has been exported.");
                    Logs.log("ACTION : export validated");
                }
            }
        });
        ComponentFactory.nullifyMargin(_export);

        _frame = frame;
        _data = data;
        layoutLibrary();
        _panel.setBorder(BorderFactory.createTitledBorder("Library"));
    }

    @Override
    public void enter()
    {
        if(_modificationView!=null)
        {
            _modificationView.validate();
            layoutLibrary();
            _frame.getOptimizerView().refresh();
            Logs.log("ACTION : card saved");
        }
    }

    private void layoutLibrary()
    {
        _modificationView = null;

        _panel.removeAll();
        _panel.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new WrapLayout());

        _previous.setEnabled(listCards().size()>MAX_CARDS && _index>0);
        _previous.revalidate();
        _previous.repaint();
        _next.setEnabled((_index+1) * MAX_CARDS - 1 < listCards().size());
        _next.revalidate();
        List<Card> cards = listCards(_index);
        for (Card card : cards)
        {
            JButton button = new CardInLibrary(card.getCost());
            buildButton(card, button);
            panel.add(button);
        }

        _panel.add(panel, new GridBagConstraints(0,0,7,1,100,100,GridBagConstraints.CENTER, GridBagConstraints.BOTH, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(_addCard, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(_import, new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(_export, new GridBagConstraints(2,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(_reset, new GridBagConstraints(3,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(ComponentFactory.createLabel(""), new GridBagConstraints(4,1,1,1,100,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_previous, new GridBagConstraints(5,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));
        _panel.add(_next, new GridBagConstraints(6,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.BUTTON_GROUP_INSET, 0, 0));


        _panel.revalidate();
        _panel.repaint();
        panel.revalidate();
        panel.repaint();
    }

    private List<Card> listCards(int index)
    {
        List<Card> ret = listCards();
        return ret.subList(Math.min(index * MAX_CARDS, ret.size()), Math.min((index+1) * MAX_CARDS, ret.size()));
    }

    private List<Card> listCards()
    {
        List<Card> ret = new ArrayList<Card>();
        for(Card card : _data.listCards())
        {
            if (_filter.equals("") || card.getName().toUpperCase().contains(_filter.toUpperCase()))
            {
                ret.add(card);
            }
        }
        return ret;
    }

    public JComponent getComponent()
    {
        return _panel;
        //JScrollPane ret = new JScrollPane(_panel);
        //ret.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //return ret;
    }

    @Override
    public void search(final String text)
    {
        _filter = text;
        _index = 0;
        layoutLibrary();
    }

    private JButton buildButton(final Card card, JButton jButton)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<br>");
        builder.append("<b>");
        builder.append(card.getName().substring(0,Math.min(card.getName().length(),18)));
        builder.append("</b>");
        builder.append("<br>");
        builder.append("Cards Drawn : ");
        builder.append(card.getCardsDrawn());
        builder.append("<br>");
        double truncatedRating = Math.round(card.getRawRating() * 100.) / 100.;
        builder.append("Rating : ");
        builder.append(truncatedRating);
        builder.append("</html>");

        jButton.setText(builder.toString());
        jButton.setFocusable(false);

        int windowWidth = 800;//(int) _frame.getFrame().getBounds().getSize().getWidth();
        int width = windowWidth / 5 - 20;
        Dimension dim = new Dimension(width, 100 * width / 140);
        jButton.setSize(dim);
        jButton.setPreferredSize(dim);
        jButton.setMinimumSize(dim);
        jButton.setMaximumSize(dim);


        jButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                openModificationView(card);
            }
        });


        return jButton;
    }

    private void openModificationView(final Card card)
    {
        _panel.setLayout(new GridBagLayout());

        _modificationView = new CardModificationView(card);
        _modificationView.initialize();
        _panel.removeAll();

        Dimension ini = _panel.getSize();
        double height = ini.getHeight() * 0.5;
        Dimension size = new Dimension((int) ini.getWidth(), (int)height);
        _modificationView.getComponent().setSize(size);
        _modificationView.getComponent().setPreferredSize(size);
        _modificationView.getComponent().setMinimumSize(size);
        _modificationView.getComponent().setMaximumSize(size);

        _validateModification = ComponentFactory.createButton("Save", "ok.png", 16);
        _validateModification.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                _modificationView.validate();
                layoutLibrary();
                _frame.getOptimizerView().refresh();
                Logs.log("ACTION : card saved");
            }
        });

        _backToLibrary = ComponentFactory.createButton("Cancel", "cancel.png", 16);
        _backToLibrary.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                layoutLibrary();
                Logs.log("ACTION : back to library");
            }
        });

        _removeCard = ComponentFactory.createButton("Remove", "trash.png", 16);
        _removeCard.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                if(JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this card ?",
                        "Warning", JOptionPane.INFORMATION_MESSAGE)==JOptionPane.OK_OPTION)
                {
                    _data.listCards().remove(card);
                    layoutLibrary();
                    _frame.getOptimizerView().refresh();
                    Logs.log("ACTION : card was removed");
                }
            }
        });
        _removeCard.setEnabled(!card.isReadonly());

        _panel.add(_modificationView.getComponent(), new GridBagConstraints(0,0,4,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_validateModification, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_removeCard, new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_backToLibrary, new GridBagConstraints(2,1,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(ComponentFactory.createLabel(""), new GridBagConstraints(3,1,1,1,100,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, MainFrame.FINE_INSETS, 0, 0));
        _panel.revalidate();
        _panel.repaint();

        _modificationView.initFocus();
    }
}
