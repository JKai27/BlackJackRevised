package org.example;

import java.io.Serializable;

public record Card(Rank rank, Suit suit) implements Serializable {

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        String cardString = rank + " of " + suit;
        return cardString + " * ";
    }
}
