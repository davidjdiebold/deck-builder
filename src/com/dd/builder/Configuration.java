package com.dd.builder;

public class Configuration {

    public static Configuration HEARTHSTONE = new Configuration(30, true,3,10);
    public static Configuration MAGIC = new Configuration(60, false, 7, 7);

    private int _deckSize;
    private boolean _isHearthstone;
    private int _startingPlayerInitialCards;
    private int _maxCardsInHand;

    public Configuration(int deckSize, boolean isHearthstone, int startingPlayerInitialCards, int maxCardsInHand) {
        _isHearthstone = isHearthstone;
        _deckSize = deckSize;
        _startingPlayerInitialCards = startingPlayerInitialCards;
        _maxCardsInHand = maxCardsInHand;
    }

    //kryo
    public Configuration() {
    }

    public int getDeckSize() {
        return _deckSize;
    }

    public boolean isHearthstone() {
        return _isHearthstone;
    }

    public int getStartingPlayerInitialCards() {
        return _startingPlayerInitialCards;
    }

    public int getNbMaxSameCardInDeck(String name) {
        //HACK name
        return _isHearthstone ? 2 : (
                name.equals("Land") ? 60 :  4
        );
    }

    public int getMaxCardsInHand() {
        return _maxCardsInHand;
    }
}
