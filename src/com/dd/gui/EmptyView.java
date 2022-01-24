package com.dd.gui;


import javax.swing.JComponent;
import javax.swing.JLabel;


public class EmptyView implements View
{

    @Override
    public JComponent getComponent()
    {
        return ComponentFactory.createLabel("");
    }

    @Override
    public void search(String text)
    {
    }

    @Override
    public void enter()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
