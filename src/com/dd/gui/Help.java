package com.dd.gui;

public class Help
{
    public final static String TURN =
            "<html>Deck builder will find the mana curve that is the most efficient <br>"
            + "for the selected turn. Use a low value for a rush deck.</html>";

    public final static String PRECISION =
            "<html>The best mana curve found by the deck builder will be more accurate if "
            + "precision <br>is high. But it takes some more time.</html>";

    public final static String HERO_POWER =
            "<html>Some hero powers, such as the warlock card drawing abilities, <br>"
            + "may affect the shape of the best mana curve.</html>";

    public final static String ADD_FIXED_CARD =
            "<html>Use this component to force some cards. <br>"
            + "Deck builder will adapt the remaining of the <br>"
            + "mana curve based on these forced cards.</html>";

    public final static String ADD_REMOVE_CARD =
            "<html>Left click to add a card<br>Right click to remove a card.</html>";

    public final static String ADD_CARD_TO_LIBRARY =
            "<html>Add a new card to the library.</html>";

    public final static String RESET =
            "<html>Library is returned to its original state. All cards previously defined are removed.</html>";

    public final static String IMPORT =
            "<html>Imports cards from a csv file to the library.</html>";

    public final static String EXPORT =
            "<html>All cards in the library are written into a csv file.</html>";

    public final static String RENAME =
            "<html>Renames the selected deck.</html>";

    public final static String REMOVE_SELECTED =
            "<html>Deletes selected deck.</html>";

    public final static String CLEAR_UNNAMED =
            "<html>All unnamed decks are removed.</html>";

    public final static String CARD_DRAWING =
            "<html>The amount of cards drawn when this card is played.</html>";

    public final static String OVERLOAD =
            "<html>Overload cost.</html>";

    public final static String REMOVAL_POWER =
            "<html>Max Mana Cost Removed.</html>";

    public final static String CARD_RATING =
            "<html>When a card is played, we consider that its rating <br>"
            + "is added to the score each turn, till the end of the game.</html>";

}
