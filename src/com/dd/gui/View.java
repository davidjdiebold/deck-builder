package com.dd.gui;


import javax.swing.JComponent;


public interface View
{
    public JComponent getComponent();

    public void search(String text);

    public void enter();
}
