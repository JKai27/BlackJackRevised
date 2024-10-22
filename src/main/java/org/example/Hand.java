package org.example;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Hand implements Serializable {
    private final List<Card> cardsInHand;

    public Hand() {
        cardsInHand = new ArrayList<>();
    }

    public void addCard(Card card) {
        cardsInHand.add(card);
    }

    public int calculateValue() {
        int totalValue = 0;
        int numberOfAces = 0;

        // First pass: calculate total and count Aces
        for (Card card : cardsInHand) {
            if (card.rank() == Rank.ACE) {
                numberOfAces++;
                totalValue += 11;  // Initially count each Ace as 11
            } else {
                totalValue += card.rank().getValue();
            }
        }

        // Second pass: adjust value if the total exceeds 21
        while (totalValue > 21 && numberOfAces > 0) {
            totalValue -= 10;  // Count one Ace as 1 instead of 11
            numberOfAces--;    // Adjust only for the number of Aces left
        }

        return totalValue;
    }

/*
    public int calculateValue() {
        int totalValue = 0;
        int numberOfAces = 0;
        for (Card card : cardsInHand) {
            totalValue += card.rank().getValue();
            if (card.rank() == Rank.ACE) {
                numberOfAces++;
            }
        }
        while (totalValue > 21 && numberOfAces > 0) {
            totalValue -= 10;
            numberOfAces--;
        }
        return totalValue;
    }

 */

    public boolean canSplit() {
        return cardsInHand.size() == 2 && cardsInHand.get(0).rank().getValue() == cardsInHand.get(1).rank().getValue();
    }

    public boolean hasBlackjack() {
        return cardsInHand.size() == 2 && calculateValue() == 21;
    }

    public String toString() {
        return cardsInHand.toString();
    }

    public boolean isBust() {
        return calculateValue() > 21;
    }
}
