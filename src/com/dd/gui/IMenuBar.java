package com.dd.gui;


import javax.swing.JComponent;
import javax.swing.JFrame;


public interface IMenuBar
{
    public void initialize();

    public void registerTab(String iconON, String iconOFF, MenuBarAction view);

    public void registerComponent(JComponent component, boolean anchorNorth);

    public JComponent getComponent();

    public void setFrame(JFrame frame);

    public void setUndecorated(boolean undecorated);

    public void selectTab(int index);

    public int getCurrentView();
}
