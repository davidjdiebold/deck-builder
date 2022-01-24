package com.dd.gui;


public class EmptyProgressBar implements ProgressBar {
    @Override
    public void setAdvancement(int adv) {
        System.out.println(""+adv);
    }
}
