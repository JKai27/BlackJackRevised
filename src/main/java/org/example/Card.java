package org.example;

public record Card(Rank rank, Suit suit) {

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        String cardString = rank + " of " + suit;
        return cardString + " * ";
    }
}
