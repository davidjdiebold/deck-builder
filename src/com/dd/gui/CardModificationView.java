package com.dd.gui;




import com.dd.model.Card;

import javax.swing.*;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;


public class CardModificationView
{
    private final Card _card;

    private JTextField _name;
    private JSpinner _cost;
    private JSpinner _attack;
    private JSpinner _health;
    private JSpinner _cardsDrawn;
    private JSpinner _overload;
    private JSpinner _removalPower;
    private JSpinner _extraDefinitiveMana;
    private JSpinner _instantMana;
    private JFormattedTextField _rating;

    private JPanel _panel = new JPanel(new GridBagLayout());

    public CardModificationView(final Card card)
    {
        _card = card;
    }

    public void initialize()
    {
        _name = new JTextField(_card.getName());
        _name.setEnabled(!_card.isReadonly());

        _cost = new JSpinner();
        _cost.setValue(_card.getCost());
        AutoFocusSpinner.installFocusListener(_cost);
        _cost.setEnabled(!_card.isReadonly());

        _attack = new JSpinner();
        _attack.setValue(_card.getPower());
        AutoFocusSpinner.installFocusListener(_attack);
        _attack.setEnabled(!_card.isReadonly());

        _health = new JSpinner();
        _health.setValue(_card.getRemainingLife());
        AutoFocusSpinner.installFocusListener(_health);
        _health.setEnabled(!_card.isReadonly());

        _cardsDrawn = new JSpinner();
        _cardsDrawn.setValue(_card.getCardsDrawn());
        _cardsDrawn.setToolTipText(Help.CARD_DRAWING);
        AutoFocusSpinner.installFocusListener(_cardsDrawn);
        _cardsDrawn.setEnabled(!_card.isReadonly());

        _overload = new JSpinner();
        _overload.setValue(_card.getOverload());
        _overload.setToolTipText(Help.OVERLOAD);
        AutoFocusSpinner.installFocusListener(_overload);
        _overload.setEnabled(!_card.isReadonly());

        _removalPower = new JSpinner();
        _removalPower.setValue(_card.getRemovalPower());
        _removalPower.setToolTipText(Help.REMOVAL_POWER);
        AutoFocusSpinner.installFocusListener(_removalPower);
        _removalPower.setEnabled(!_card.isReadonly());

        _extraDefinitiveMana = new JSpinner();
        _extraDefinitiveMana.setValue(_card.getExtraDefinitiveMana());
        _extraDefinitiveMana.setToolTipText("");
        AutoFocusSpinner.installFocusListener(_extraDefinitiveMana);
        _extraDefinitiveMana.setEnabled(true);

        _instantMana = new JSpinner();
        _instantMana.setValue(_card.get_extraMana());
        _instantMana.setToolTipText("");
        AutoFocusSpinner.installFocusListener(_instantMana);
        _instantMana.setEnabled(true);

        _rating = new JFormattedTextField(new FloatFormatter(2,3,0f,Float.POSITIVE_INFINITY));
        _rating.setValue(_card.getRawRating());
        _rating.setToolTipText(Help.CARD_RATING);
        _rating.addFocusListener(AutoFocusSpinner.SHARED_INSTANCE);

        layout();
    }

    private void layout()
    {
        _name.setColumns(20);
        _rating.setColumns(20);

        _panel.removeAll();
        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_NAME), new GridBagConstraints(0, 0, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_name, new GridBagConstraints(1, 0, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(ComponentFactory.createLabel(""), new GridBagConstraints(2, 0, 1, 1, 100, 1, WEST, BOTH, MainFrame.FINE_INSETS, 0, 0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_COST), new GridBagConstraints(0, 1, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_cost, new GridBagConstraints(1, 1, 1, 1, 1, 1, WEST, BOTH, MainFrame.FINE_INSETS, 0, 0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_ATTACK), new GridBagConstraints(0, 2, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_attack, new GridBagConstraints(1,2,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_HEALTH), new GridBagConstraints(0, 3, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_health, new GridBagConstraints(1,3,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_CARDS_DRAWN), new GridBagConstraints(0, 4, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_cardsDrawn, new GridBagConstraints(1,4,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_OVERLOAD), new GridBagConstraints(0, 5, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_overload, new GridBagConstraints(1,5,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_REMOVAL), new GridBagConstraints(0, 6, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_removalPower, new GridBagConstraints(1,6,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel(Messages.CARDDETAIL_LABEL_RATING), new GridBagConstraints(0, 7, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_rating, new GridBagConstraints(1,7,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel("Extra Def. Mana"), new GridBagConstraints(0, 8, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_extraDefinitiveMana, new GridBagConstraints(1,8,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(ComponentFactory.createLabel("Instant Mana"), new GridBagConstraints(0, 9, 1, 1, 1, 1, WEST, NONE, MainFrame.FINE_INSETS, 0, 0));
        _panel.add(_instantMana, new GridBagConstraints(1,9,1,1,1,1,WEST,BOTH, MainFrame.FINE_INSETS,0,0));

        _panel.add(new JLabel(), new GridBagConstraints(1,10,2,1,1,100,WEST,BOTH, MainFrame.FINE_INSETS,0,0));
    }

    public void initFocus()
    {
        _name.requestFocusInWindow();
        _name.selectAll();
    }

    public JComponent getComponent()
    {
        return _panel;
    }

    public void validate()
    {
        _card.setName(_name.getText());
        _card.setCost((Integer) _cost.getValue());
        _card.setPower((Integer) _attack.getValue());
        _card.setHealth((Integer) _health.getValue());
        _card.setCardsDrawn((Integer) _cardsDrawn.getValue());
        _card.setOverload((Integer) _overload.getValue());
        _card.setRemovalPower((Integer) _removalPower.getValue());
        _card.setExtraDefinitiveMana((Integer) _extraDefinitiveMana.getValue());
        _card.set_extraMana((Integer) _instantMana.getValue());
        Object val = _rating.getValue();
        _card.setRawRating(val instanceof Float ? ((Float) val).doubleValue() : (Double) val);
    }
}
