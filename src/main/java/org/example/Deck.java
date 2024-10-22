package org.example;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Deck implements Serializable {
    private final List<Card> cards;
    private static final int NUMBER_OF_CARDS_IN_A_DECK = 52;
    private final int allowedNumberOfDecksInTheGame;


    public Deck(int numberOfDecksInTheGame) {
        cards = new ArrayList<>(NUMBER_OF_CARDS_IN_A_DECK);
        this.allowedNumberOfDecksInTheGame = numberOfDecksInTheGame;
        initializeCards();
    }

    private void initializeCards() {
        cards.clear(); // in case of reinitializing clear the deck first.

        for (int i = 0; i < allowedNumberOfDecksInTheGame; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isDeckLowOnCards() {
        return cards.size() <= NUMBER_OF_CARDS_IN_A_DECK;
    }


    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1); // Draw from the end
        } else {
            System.out.println("Deck is empty! No cards to draw.");
            return null; // No cards left
        }
    }

    // Reinitialize the deck if it's low on cards
    public void renewDeckIfNeeded() {
        if (isDeckLowOnCards()) {
            System.out.println("Deck is running low, reshuffling to " + allowedNumberOfDecksInTheGame + " decks.");
            initializeCards();  // Reinitialize the deck
        }
    }
}
