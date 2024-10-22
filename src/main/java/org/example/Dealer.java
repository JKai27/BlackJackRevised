package org.example;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter

public class Dealer implements Serializable {
    private final String dealerName;
    @Setter
    private Hand dealerHand;
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";


    public Dealer(String dealerName) {
        this.dealerName = dealerName;
        this.dealerHand = new Hand();
    }

    public boolean isFaceUpCardAce() {
        return !dealerHand.getCardsInHand().isEmpty() &&
                dealerHand.getCardsInHand().get(0).rank() == Rank.ACE;
    }

    public void revealHiddenCard() {
        System.out.println(YELLOW + "\nDealer is about to reveal hidden card..." + RESET);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("An error occurred while delaying output: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println(BLUE + "Dealer reveals his hidden card... " + dealerHand.getCardsInHand().get(1) + RESET);
    }

    public boolean hasDealerBlackjack() {
        return isFaceUpCardAce() && dealerHand.getCardsInHand().size() == 2 && dealerHand.getCardsInHand().get(1).getValue() == 10;
    }

    public void dealerPlays(Deck deck) {
        // Dealer keeps drawing until hand value is at least 17
        while (dealerHand.calculateValue() < 17) {
            Card drawnCard = deck.drawCard();
            dealerHand.addCard(drawnCard);
            System.out.println("Dealer draws: " + drawnCard);
        }

        int dealerTotal = dealerHand.calculateValue();
        System.out.println(BLUE + "\nDealer stands with a value of: " + dealerTotal + RESET);
        System.out.println(dealerHand.toString());
        System.out.println(YELLOW + "----------------------------------------FINAL RESULTS--------------------------------------------------" + RESET);
    }

    @Override
    public String toString() {
        return dealerHand.toString();
    }

}
