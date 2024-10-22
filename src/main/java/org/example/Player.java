package org.example;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Player implements Serializable {
    private final String name;
    private final List<Hand> playerHands;
    @Setter
    private double availableMoney;
    @Setter
    private double bet;
    private double currentBet;
    private double payOutRatio;
    @Setter
    private boolean doubledDown;
    @Setter
    private double insuranceBet;
    @Setter
    private boolean tookInsurance;
    @Setter
    private boolean continuePlaying;

    public Player(String name, double availableMoney) {

        this.name = name;
        this.playerHands = new ArrayList<>();
        this.availableMoney = availableMoney;
        this.insuranceBet = 0;
        this.bet = 0;
        this.tookInsurance = false;
    }
/*
    public void placeBet(double amount) {
        this.currentBet = amount; // Just set the bet amount, do not deduct from availableMoney yet
    }

 */

    public void placeBet(double amount) {
        if (amount > availableMoney) {
            System.out.println(name + " does not have enough money to place this bet.");
        } else if (amount < 10 || amount > 500) {
            System.out.println(name + " must place a bet between 10.0 and 500.0.");
        } else {
            this.currentBet = amount; // Set the bet amount
            // availableMoney -= amount; // Deduct the amount from available money
            System.out.println(name + " has placed a bet of " + currentBet + ".");
        }
    }

/*
    public boolean playerHasBlack() {
        for (Hand hand : playerHands) {
            return hand.hasBlackjack();
        }
        return false;
    }

    private void setInsuranceBet(double insuranceBet) {
        insuranceBet = currentBet / 2;
        this.insuranceBet = insuranceBet;
    }

 */

    // Method to place an insurance bet
    public void placeInsuranceBet(double amount) {
        if (amount <= availableMoney) {
            availableMoney -= amount; // Deduct the amount from available money
            insuranceBet += amount; // Track the insurance bet
            System.out.println(name + " places an insurance bet of " + amount);
        } else {
            System.out.println(name + " does not have enough money to place the insurance bet.");
        }
    }

    public boolean hasTakenInsurance() {
        return tookInsurance;
    }


    // Reset insurance bet at the start of a new round
    public void resetInsuranceBet() {
        insuranceBet = 0;
        tookInsurance = false; // Resetting the Insurance round for new round.
    }

    public void lostBet() {
        if (!doubledDown) {
            this.payOutRatio = 1.0;
        } else {
            this.payOutRatio = 2.0;
        }
        availableMoney -= (currentBet * payOutRatio);
    }


    public void wonBet() {
        // Check if the player has Blackjack
        if (hasPlayerBlackjack()) {
            this.payOutRatio = 1.5; // 3:2 payout for Blackjack
        }
        // Check if the player doubled down
        else if (doubledDown) {
            this.payOutRatio = 2.0; // Double down payout
        }
        // Normal win
        else {
            this.payOutRatio = 1.0; // Regular payout
        }
        availableMoney += (currentBet * payOutRatio);
    }


    public void refundBetStatement() {
        System.out.println("Refunding the bet of " + currentBet + " to " + this.name);
    }


    public boolean hasPlayerBlackjack() {
        if (playerHands.size() == 1) {
            Hand firstHand = playerHands.get(0);
            return firstHand.getCardsInHand().size() == 2 && firstHand.calculateValue() == 21;
        }
        return false;
    }

    public boolean isDoubleDownPossible() {
        // Iterate over all player hands
        for (Hand hand : getPlayerHands()) {
            // Check if hand value is between 9 and 11 (inclusive) and if there are exactly 2 cards in hand
            if (hand.calculateValue() >= 9 && hand.calculateValue() <= 11 && hand.getCardsInHand().size() == 2) {
                return true; // Return true as soon as a valid hand is found
            }
        }
        return false; // Return false if no hand satisfies the condition
    }


    public void showTotalValueOfPlayersHands() {
        int handIndex = 1;
        for (Hand hand : playerHands) {
            double handValue = hand.calculateValue();
            System.out.println("Total value of Hand " + handIndex + ": " + handValue);
            handIndex++;
        }
    }

    public void resetBet() {
        this.bet = 0; // Reset the bet to 0 for the new round
    }
}
