package com.dd.gui;

public class UISettings
{
    private String _selectedDeck;

    private int _selectedTab;

    public UISettings()
    {
    }

    public String getSelectedDeck()
    {
        return _selectedDeck;
    }

    public void setSelectedDeck(final String selectedDeck)
    {
        _selectedDeck = selectedDeck;
    }

    public int getSelectedTab()
    {
        return _selectedTab;
    }

    public void setSelectedTab(final int selectedTab)
    {
        _selectedTab = selectedTab;
    }
}
