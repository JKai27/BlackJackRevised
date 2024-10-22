package org.example;

import lombok.Getter;

import java.io.*;
import java.util.*;

@Getter
public class GameLogic implements Serializable {
    private final Deck deck;
    private List<Player> players;
    private final Dealer dealer;
    private final double MAX_BET_AMOUNT = 500;
    private final double MIN_BET_AMOUNT = 10;

    public GameLogic() {
        deck = validNumberOfDecks();
        players = new ArrayList<>();
        dealer = new Dealer("Casino Royal");
    }

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";


/*

    public void startGame() {
        System.out.println("\nWelcome to " + dealer.getDealerName() + "'s Blackjack!");

        invitePlayers();

        List<Player> playersToContinue;
        do {
            // Reset hands and bets for each player before a new round starts
            for (Player player : players) {
                deck.renewDeckIfNeeded();

                player.getPlayerHands().clear();
                player.resetBet();
                player.resetInsuranceBet();
                player.setDoubledDown(false);

                if (player.getAvailableMoney() < MIN_BET_AMOUNT) {
                    System.out.println(player.getName() + ", you have insufficient funds.");
                    // Logic to handle insufficient funds
                    if (!handleInsufficientFunds(player)) {
                        players.remove(player); // Remove player after they decline to top up
                    }
                }
            }

            // Proceed if there are players left
            if (players.isEmpty()) {
                System.out.println("No players left in the game. Exiting...");
                break;
            }

            dealer.getDealerHand().getCardsInHand().clear(); // Reset dealer's hand

            promptPlayersForBets(); // Set bets
            dealInitialCards(); // Deal cards
            showInitialHands(); // Show hands

            // Step 1: Check for Blackjack immediately after initial hands are dealt
            playersToContinue = whoHasBlackJack(); // Check for blackjack winners

            if (playersToContinue.isEmpty()) {
                break; // End round if everyone has blackjack
            }

            // Step 2: If no Blackjack, continue the game flow with splits and insurance
            Scanner getInputForSplit = new Scanner(System.in);

            //Step 3: Offer insurance if the dealer's face-up card is an Ace, but exclude players with blackjack
            List<Player> playersEligibleForInsurance = players.stream()
                    .filter(player -> !player.hasPlayerBlackjack())
                    .toList();
            if (dealer.isFaceUpCardAce()) {
                offerInsurance(playersEligibleForInsurance); // Players decide on insurance
                dealer.revealHiddenCard(); // Reveal hidden card only for insurance purposes

                boolean roundShouldEnd = handleInsurance(); // Handle insurance payouts if applicable
                if (roundShouldEnd) {
                    System.out.println("The round ends as the dealer has Blackjack.");
                    continue; // Move to next round
                }
            }


            // Step 4: check if split possible
            for (Player player : playersToContinue) {
                // Check if the first hand can be split
                if (player.getPlayerHands().get(0).canSplit()) {
                    while (true) {
                        System.out.println("\n" + player.getName() + ", do you want to split your hand? (y/n)");
                        String userInput = getInputForSplit.nextLine().trim().toLowerCase();

                        // Handle 'y' (yes) case
                        if (userInput.equals("y")) {
                            split(player); // Ensure this method correctly splits the hands
                            System.out.println(player.getName() + " has split the hand.");
                            break; // Exit the loop after splitting
                        }
                        // Handle 'n' (no) case
                        else if (userInput.equals("n")) {
                            System.out.println(player.getName() + " chose not to split the hand.");
                            break; // Exit the loop and proceed
                        }
                        // Handle invalid input
                        else {
                            System.out.println("Invalid input! Please enter 'y' for YES or 'n' for NO.");
                        }
                    }
                }
            }


            // Step 5: Continue players' turn if no Blackjack was found
            for (Player player : playersToContinue) {
                playersTurn(player); // Player's turn
            }

            dealer.dealerPlays(deck); // Dealer's turn without revealing the hidden card

            // Compare each player's hand with the dealer's hand after dealer finishes playing
            for (Player player : playersToContinue) {
                for (Hand hand : player.getPlayerHands()) {
                    compareWithDealer(player, hand);
                }
            }

        } while (!playersToContinue.isEmpty() && askToContinue());
    }
*/

    public void startGame() {
        int currentPlayerCount = players.size(); // Get the current number of players
        if (currentPlayerCount > 7) {
            System.out.println(RED + "Error: The total number of players cannot exceed 7. Current players: " + currentPlayerCount + RESET);
            return; // Prevent starting the game if player count exceeds 7
        }

        System.out.println(CYAN + "\nWelcome to " + dealer.getDealerName() + "'s Blackjack!" + RESET);

        // Calculate remaining spots
        int remainingSpots = 7 - currentPlayerCount;

        // Call invitePlayers method only if there are remaining spots
        if (remainingSpots > 0) {
            System.out.println("You can add up to " + remainingSpots + " more player(s).");
            invitePlayers(remainingSpots); // Call the updated invitePlayers method
        } else {
            System.out.println("No additional players can be added. Starting the game...");
        }

        List<Player> playersToContinue;
        do {
            // Reset hands and bets for each player before a new round starts
            for (Player player : players) {
                deck.renewDeckIfNeeded();

                player.getPlayerHands().clear();
                player.resetBet();
                player.resetInsuranceBet();
                player.setDoubledDown(false);
                this.playersWithBlackjack.clear();

                if (player.getAvailableMoney() < MIN_BET_AMOUNT) {
                    System.out.println(RED + player.getName() + ", you have insufficient funds." + RESET);
                    // Logic to handle insufficient funds
                    if (!handleInsufficientFunds(player)) {
                        players.remove(player); // Remove player after they decline to top up
                    }
                }
            }

            // Proceed if there are players left
            if (players.isEmpty()) {
                System.out.println(RED + "No players left in the game. Exiting..." + RESET);
                break;
            }

            dealer.getDealerHand().getCardsInHand().clear(); // Reset dealer's hand

            promptPlayersForBets(); // Set bets
            dealInitialCards(); // Deal cards
            showInitialHands(); // Show hands

            // Step 1: Check for Blackjack immediately after initial hands are dealt
            playersToContinue = whoHasBlackJack(); // Check for blackjack winners

            if (playersToContinue.isEmpty()) {
                break; // End round if everyone has blackjack
            }

            // Step 2: If no Blackjack, continue the game flow with splits and insurance
            Scanner getInputForSplit = new Scanner(System.in);

            // Step 3: Offer insurance if the dealer's face-up card is an Ace, but exclude players with blackjack
            List<Player> playersEligibleForInsurance = players.stream()
                    .filter(player -> !player.hasPlayerBlackjack())
                    .toList();
            if (dealer.isFaceUpCardAce()) {
                System.out.println(YELLOW + "The dealer is showing an Ace, would you like to take insurance?" + RESET);
                offerInsurance(playersEligibleForInsurance); // Players decide on insurance
                dealer.revealHiddenCard(); // Reveal hidden card only for insurance purposes

                boolean roundShouldEnd = handleInsurance(); // Handle insurance payouts if applicable
                if (roundShouldEnd) {
                    System.out.println(RED + "The round ends as the dealer has Blackjack." + RESET);
                    continue; // Move to next round
                }
            }

            // Step 4: Check if split possible
            for (Player player : playersToContinue) {
                // Check if the first hand can be split
                if (player.getPlayerHands().get(0).canSplit()) {
                    while (true) {
                        System.out.println(YELLOW + "\n" + player.getName() + ", do you want to split your hand? (y/n)" + RESET);
                        String userInput = getInputForSplit.nextLine().trim().toLowerCase();

                        // Handle 'y' (yes) case
                        if (userInput.equals("y")) {
                            split(player); // Ensure this method correctly splits the hands
                            System.out.println(BLUE + player.getName() + " has split the hand." + RESET);
                            break; // Exit the loop after splitting
                        }
                        // Handle 'n' (no) case
                        else if (userInput.equals("n")) {
                            System.out.println(CYAN + player.getName() + " chose not to split the hand." + RESET);
                            break; // Exit the loop and proceed
                        }
                        // Handle invalid input
                        else {
                            System.out.println(RED + "Invalid input! Please enter 'y' for YES or 'n' for NO." + RESET);
                        }
                    }
                }
            }

            // Step 5: Continue players' turn if no Blackjack was found
            for (Player player : playersToContinue) {
                System.out.println(CYAN + player.getName() + "'s turn to play!" + RESET);
                playersTurn(player); // Player's turn
            }

            dealer.dealerPlays(deck); // Dealer's turn without revealing the hidden card

            if (!playersWithBlackjack.isEmpty()) {
                displayPlayersWhoHasBlackjack(playersWithBlackjack);
            }

            // Compare each player's hand with the dealer's hand after dealer finishes playing
            for (Player player : playersToContinue) {
                for (Hand hand : player.getPlayerHands()) {
                    compareWithDealer(player, hand);
                }
            }

        } while (!playersToContinue.isEmpty() && askToContinue());
    }


    public void split(Player player) {
        Hand originalHand = player.getPlayerHands().get(0); // Acting on the first hand

        // Check if the hand can be split
        if (originalHand.canSplit()) {
            Hand firstSplitHand = new Hand();
            Hand secondSplitHand = new Hand();

            // Get the cards and assign to split hands
            Card firstCard = originalHand.getCardsInHand().get(0);
            Card secondCard = originalHand.getCardsInHand().get(1);

            firstSplitHand.addCard(firstCard);
            secondSplitHand.addCard(secondCard);

            double currentBet = player.getBet();

            // Check balance for placing a bet on the second hand
            if (player.getAvailableMoney() >= currentBet) {
                // Place the bet for both hands
                //  player.placeBet(currentBet); // Deduct for the firsthand
                //  player.placeBet(currentBet); // Deduct for the secondhand as well

                // Remove the original hand
                player.getPlayerHands().remove(originalHand);
                // Add first and second split hands
                player.getPlayerHands().add(firstSplitHand);
                player.getPlayerHands().add(secondSplitHand);

                // Deal one card to each hand
                firstSplitHand.addCard(deck.drawCard());
                secondSplitHand.addCard(deck.drawCard());

                // Check if either hand has blackjack
                StringBuilder blackjackMessage = new StringBuilder();
                if (firstSplitHand.hasBlackjack()) {
                    blackjackMessage.append(player.getName()).append("'s first split hand has blackjack! ");
                }
                if (secondSplitHand.hasBlackjack()) {
                    blackjackMessage.append(player.getName()).append("'s second split hand has blackjack! ");
                }
                if (!blackjackMessage.isEmpty()) {
                    System.out.println(blackjackMessage.toString());
                }

                System.out.println(player.getName() + " now will continue this round with two hands.");
            } else {
                System.out.println(RED + "Not enough balance to split." + RESET);
            }
        } else {
            System.out.println(RED + "Cannot split this hand." + RESET);
        }
    }

    private boolean handleInsurance() {
        boolean dealerHasBlackJack = dealer.hasDealerBlackjack();

        if (dealerHasBlackJack) {
            System.out.println("Dealer has Blackjack!");

            for (Player player : players) {
                System.out.println(player.getName() + "'s previous balance: " + player.getAvailableMoney());

                if (player.hasTakenInsurance()) {
                    double insurancePayout = player.getInsuranceBet() * 2; // 2:1 payout for insurance
                    player.setAvailableMoney(player.getAvailableMoney() + insurancePayout);
                    System.out.println(GREEN + player.getName() + " wins " + insurancePayout + " from insurance. (2:1 payout on her " + player.getInsuranceBet() + " insurance bet.)" + RESET);
                } else {
                    System.out.println(player.getName() + " loses the original bet due to dealer's blackjack.");
                    player.lostBet(); // Deduct the regular bet from balance
                }
            }

            return true; // Return true to indicate the round should end
        }

        // Dealer doesn't have blackjack, deduct insurance bets from players who took insurance
        for (Player player : players) {
            if (player.hasTakenInsurance()) {
                System.out.println(player.getName() + " loses insurance bet of " + player.getInsuranceBet() + ".");
                player.setAvailableMoney(player.getAvailableMoney() - player.getInsuranceBet());
            }
        }

        return false; // Return false to indicate that the game should continue
    }


/*
    private void handleInsurance() {
        boolean dealerHasBlackJack = dealer.hasDealerBlackjack();

        for (Player player : players) {
            System.out.println(player.getName() + "'s previous balance: " + player.getAvailableMoney());

            if (dealerHasBlackJack) {
                if (player.hasTakenInsurance()) {
                    System.out.println(player.getName() + " wins insurance, payout will be handled accordingly at the end of the round.");
                } else {
                    System.out.println(player.getName() + " loses the bet due to dealer's blackjack.");
                }
                player.setContinuePlaying(false);  // Player cannot continue if the dealer has blackjack
            } else {
                System.out.println("Dealer does not have blackjack, " + player.getName() + " continues playing.");
                player.setContinuePlaying(true);   // Player continues if no dealer blackjack
            }
        }
    }

 */

    public void offerInsurance(List<Player> players) {
        for (Player player : players) {
            if (player.getAvailableMoney() >= player.getCurrentBet() / 2) {
                System.out.print(player.getName() + ", do you want to take insurance? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String decision = scanner.nextLine().trim().toLowerCase();

                if (decision.equals("y")) {
                    player.setTookInsurance(true);  // Mark the player as having taken insurance
                    player.setInsuranceBet(player.getCurrentBet() / 2); // Set the insurance bet but do not deduct it
                    System.out.println(player.getName() + " takes insurance.");
                } else {
                    System.out.println(player.getName() + " declines insurance.");
                    player.setTookInsurance(false); // Clear insurance status if they declined
                }
            } else {
                System.out.println(player.getName() + " cannot afford insurance.");
            }
        }
    }


    // Method to handle insufficient funds
    private boolean handleInsufficientFunds(Player player) {
        Scanner scanner = new Scanner(System.in);
        final double TOP_UP_AVAILABLE_BALANCE = 10.0; // Define minimum top-up amount

        // Top up logic here
        while (true) {
            System.out.print("Enter amount to top up: ");
            try {
                double amount = scanner.nextDouble();
                if (amount < TOP_UP_AVAILABLE_BALANCE) {
                    System.out.println("Top up amount should be minimum " + TOP_UP_AVAILABLE_BALANCE);
                } else {
                    player.setAvailableMoney(player.getAvailableMoney() + amount);
                    System.out.println(player.getName() + ", your new balance is: " + player.getAvailableMoney());
                    break; // Break out of the loop if input is valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid amount entered. Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }

        // Ask player if they want to continue playing
        System.out.print("Do you want to continue playing? (y/n): ");
        String response = scanner.next().trim().toLowerCase();
        return response.equals("y");
    }


    /* private void playersTurn(Player player) {
        Scanner scanner = new Scanner(System.in);

        // Iterate over each hand of the player (in case of splits)
        for (Hand hand : player.getPlayerHands()) {
            boolean turnActive = true;

            while (turnActive) {
                int playerHandValue = hand.calculateValue(); // Calculate hand value at the start of each loop
                System.out.println(player.getName() + "'s hand: " + hand.getCardsInHand());
                System.out.println("Total value of hand: " + playerHandValue);

                // Check if Double Down is possible
                if (player.isDoubleDownPossible()) {
                    System.out.print("\n" + player.getName() + ", Do you want to double down? (y/n): ");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (choice.equals("y")) {
                        player.setDoubledDown(true);
                        handleDoubleDown(player);
                        turnActive = false; // Turn ends after double down
                        break; // Move on to the next hand (if split)
                    } else if (choice.equals("n")) {
                        System.out.println("Continuing with your regular turn.");
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                        continue; // Ask again if input was invalid
                    }
                }

                // Ask for other actions (Hit, Stay, etc.)
                System.out.println("\n" + player.getName() + ", Do you want to Hit or Stay? (h/s): ");
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("h")) {
                    hand.addCard(deck.drawCard());
                    System.out.println(player.getName() + ", You drew " + hand.getCardsInHand().get(hand.getCardsInHand().size() - 1));

                    playerHandValue = hand.calculateValue(); // Update hand value after hitting

                    // If hand reaches 21, stop asking for further actions
                    if (playerHandValue == 21) {
                        System.out.println(player.getName() + " has 21! Turn ends.");
                        turnActive = false; // End turn immediately if hand value is 21
                    } else if (hand.isBust()) {
                        System.out.println(player.getName() + " busts!");
                        turnActive = false; // End turn if the player busts
                    }

                } else if (action.equals("s")) {
                    System.out.println(player.getName() + " stays.");
                    turnActive = false; // End turn if the player chooses to stay
                } else {
                    System.out.println("Invalid input. Please enter 'h' for Hit or 's' for Stay.");
                }
            }
        }
    } */
    private void playersTurn(Player player) {
        Scanner scanner = new Scanner(System.in);

        // Iterate over each hand of the player (in case of splits)
        List<Hand> playerHands = player.getPlayerHands();
        for (int i = 0; i < playerHands.size(); i++) {
            Hand hand = playerHands.get(i);
            boolean turnActive = true;
            int handNumber = i + 1; // Hand numbers start from 1

            System.out.println("\n--- Hand #" + handNumber + " ---"); // Display hand number

            while (turnActive) {
                int playerHandValue = hand.calculateValue(); // Calculate hand value at the start of each loop
                System.out.println(player.getName() + "'s hand: " + hand.getCardsInHand());
                System.out.println("Total value of hand: " + playerHandValue);

                // Check if Double Down is possible
                if (player.isDoubleDownPossible()) {
                    System.out.print(YELLOW + "\n" + player.getName() + ", Do you want to double down for Hand #" + handNumber + "? (y/n): " + RESET);
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (choice.equals("y")) {
                        player.setDoubledDown(true);
                        handleDoubleDown(player);
                        turnActive = false; // Turn ends after double down
                        break; // Move on to the next hand (if split)
                    } else if (choice.equals("n")) {
                        System.out.println("Continuing with your regular turn.");
                    } else {
                        System.out.println(RED + "Invalid input. Please enter 'y' or 'n'." + RESET);
                        continue; // Ask again if input was invalid
                    }
                }

                // Ask for other actions (Hit, Stay, etc.)
                System.out.println(YELLOW + "\n" + player.getName() + ", Do you want to Hit or Stay for Hand #" + handNumber + "? (h/s): " + RESET);
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("h")) {
                    hand.addCard(deck.drawCard());
                    System.out.println(BLUE + player.getName() + ", You drew " + hand.getCardsInHand().get(hand.getCardsInHand().size() - 1) + RESET);

                    playerHandValue = hand.calculateValue(); // Update hand value after hitting

                    // If hand reaches 21, stop asking for further actions
                    if (playerHandValue == 21) {
                        System.out.println(player.getName() + " has 21! Turn ends.");
                        turnActive = false; // End turn immediately if hand value is 21
                    } else if (hand.isBust()) {
                        System.out.println(RED + player.getName() + " busts!" + RESET);
                        turnActive = false; // End turn if the player busts
                    }

                } else if (action.equals("s")) {
                    System.out.println(player.getName() + " stays.");
                    turnActive = false; // End turn if the player chooses to stay
                } else {
                    System.out.println(RED + "Invalid input. Please enter 'h' for Hit or 's' for Stay." + RESET);
                }
            }
        }
    }


    private void compareWithDealer(Player player, Hand playerHand) {
        int dealerValue = dealer.getDealerHand().calculateValue();
        int playerHandValue = playerHand.calculateValue();
        double bet = player.getCurrentBet();
        double previousBalance = player.getAvailableMoney();
        double doubledBet = player.getCurrentBet() * 2;
        System.out.println();
        /*
        // Handle insurance payouts if the dealer has blackjack
        if (dealer.hasDealerBlackjack()) {
            if (player.hasTakenInsurance()) {
                double insurancePayout = player.getInsuranceBet() * 2; // 2:1 payout for insurance
                player.setAvailableMoney(player.getAvailableMoney() + insurancePayout);
                System.out.println(player.getName() + " wins " + insurancePayout + " from insurance.");
            } else {
                System.out.println(player.getName() + " loses the original bet due to dealer blackjack.");
                player.lostBet(); // Deduct regular bet from balance
            }
            return;  // End player's turn since dealer has blackjack
        }

        // Deduct insurance bet if player took insurance but dealer doesn't have blackjack
        if (player.hasTakenInsurance()) {
            System.out.println(player.getName() + " loses insurance bet of " + player.getInsuranceBet() + ".");
            player.setAvailableMoney(player.getAvailableMoney() - player.getInsuranceBet());
        }

         */   // handle insurance remove the code snippet later

        // Skip dealer comparison if the player has already busted
        if (playerHandValue > 21) {
            System.out.println(player.getName() + "'s Hand Value: " + playerHandValue + RED + " (Bust!)" + RESET);
            if (player.isDoubledDown()) {
                System.out.println(RED + player.getName() + " loses the doubled down bet of " + doubledBet + "." + RESET);
            } else {
                System.out.println(RED + player.getName() + " loses the bet of " + bet + "." + RESET);
            }
            player.lostBet();
        }
        // Dealer busts
        else if (dealerValue > 21) {
            System.out.println(player.getName() + "'s Hand Value: " + playerHandValue + " vs Dealer's Hand Value: " + dealerValue);
            if (player.isDoubledDown()) {
                System.out.println(RED + "Dealer busts! " + RESET + GREEN + player.getName() + " wins the doubled down bet of " + doubledBet + "." + RESET);
            } else {
                System.out.println(RED + "Dealer busts! " + RESET + GREEN + player.getName() + " wins the bet of " + bet + "." + RESET);
            }
            player.wonBet();
        }
        // Player wins
        else if (playerHandValue > dealerValue) {
            System.out.println(player.getName() + "'s Hand Value: " + playerHandValue + " vs Dealer's Hand Value: " + dealerValue);
            if (player.isDoubledDown()) {
                System.out.println(GREEN + player.getName() + " wins the doubled down bet of " + doubledBet + "." + RESET);
            } else {
                System.out.println(GREEN + player.getName() + " wins the bet of " + bet + "." + RESET);
            }
            player.wonBet();
        }
        // Player loses
        else if (playerHandValue < dealerValue) {
            System.out.println(player.getName() + "'s Hand Value: " + playerHandValue + " vs Dealer's Hand Value: " + dealerValue);
            if (player.isDoubledDown()) {
                System.out.println(RED + player.getName() + " loses the doubled down bet of " + doubledBet + "." + RESET);
            } else {
                System.out.println(RED + player.getName() + " loses the bet of " + bet + "." + RESET);
            }
            player.lostBet();
        }
        // Push
        else {
            System.out.println(player.getName() + "'s Hand Value: " + playerHandValue + " vs Dealer's Hand Value: " + dealerValue);
            System.out.println(GREEN + "It's a push! " + player.getName() + " gets back the bet of " + bet + "." + RESET);
            player.refundBetStatement();
        }

        // Condensed balance display
        System.out.println("Previous balance: " + previousBalance);
        System.out.println("Updated balance: " + player.getAvailableMoney());
        System.out.println(player.getName() + "'s balance updated from " + previousBalance + " to " + player.getAvailableMoney() + ".\n");
    }

    private void handleDoubleDown(Player player) {
        double doubledBet = player.getCurrentBet() * 2;

        for (Hand hand : player.getPlayerHands()) {
            // Draw one card for the double down
            hand.addCard(deck.drawCard());
            System.out.println("You drew " + hand.getCardsInHand().get(hand.getCardsInHand().size() - 1));

            // Check if the hand is bust
            if (hand.isBust()) {
                System.out.println(player.getName() + " busts and loses the doubled bet of " + doubledBet);
                player.setAvailableMoney(player.getAvailableMoney() - doubledBet);
            } else {
                System.out.println(player.getName() + " continues with a doubled bet.");
                player.setBet(doubledBet); // Update the player's bet
            }
        }
    }

    private List<Player> playersWithBlackjack = new ArrayList<>();

    public List<Player> whoHasBlackJack() {
        List<Player> playersWithoutBlackjack = new ArrayList<>();
        for (Player player : players) {
            if (player.hasPlayerBlackjack()) {
                System.out.println(GREEN + player.getName() + " has Blackjack and wins the round!" + RESET);
                // Handle payout for Blackjack (3:2)
                double beforeBalance = player.getAvailableMoney();
                player.wonBet();
                double netWinn = player.getAvailableMoney() - beforeBalance;

                System.out.println(player.getName() + " wins " + netWinn + " previous balance: " + beforeBalance + " updated balance: " + player.getAvailableMoney() + "\n");
                playersWithBlackjack.add(player);
            } else {
                playersWithoutBlackjack.add(player); // Add players who don't have Blackjack to continue the round
            }
        }
        return playersWithoutBlackjack;
    }

    private void displayPlayersWhoHasBlackjack(List<Player> playersWithBlackjack) {
        // Display the results for players who got blackjack
        System.out.println("\nFinal Results for players with Blackjack:");
        for (Player player : playersWithBlackjack) {
            System.out.println(GREEN + player.getName() + " had Blackjack! Final balance: " + player.getAvailableMoney() + RESET + "\n");
        }
    }


    public void showInitialHands() {
        // Show each player's hands
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s hand:");
            for (Hand hand : player.getPlayerHands()) {
                System.out.println(hand + "\n");
            }
            player.showTotalValueOfPlayersHands();
            System.out.println();
        }

        // Show dealer's first card and hide the second one
        System.out.println("Dealer's hand:");
        List<Card> dealerCards = dealer.getDealerHand().getCardsInHand();
        if (dealerCards.size() >= 2) {
            System.out.println("Dealer shows: " + dealerCards.get(0));
            System.out.println("Hidden Card");
        }
        System.out.println();
    }


    public void dealInitialCards() {
        // Ensure deck is shuffled
        deck.shuffle();

        // Deal 2 cards to each player
        for (Player player : players) {
            Hand playerHand = new Hand(); // Create a new hand for the player
            playerHand.addCard(deck.drawCard()); // Deal first card
            playerHand.addCard(deck.drawCard()); // Deal second card
            player.getPlayerHands().add(playerHand); // Add the hand to the player's hands
        }

        // Deal 2 cards to the dealer
        Hand dealerHand = new Hand();
        dealerHand.addCard(deck.drawCard()); // Deal first card to dealer
        dealerHand.addCard(deck.drawCard()); // Deal second card to dealer
        dealer.setDealerHand(dealerHand); // Set dealer's hand
    }


    private boolean askToContinue() {
        Scanner scanner = new Scanner(System.in);
        String response;

        while (true) {
            System.out.println("Do you want to play another round? (y/n)");
            response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y")) {
                return true; // Continue the loop
            } else if (response.equals("n")) {
                return false; // Exit the loop
            } else {
                System.out.println(RED + "Invalid response. Please enter 'y' to continue or 'n' to exit." + RESET);
            }
        }
    }


    public void invitePlayers(int remainingSpots) {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        // Set the maximum number of players to invite based on remaining spots
        int maxPlayersToInvite = Math.min(remainingSpots, 7 - players.size());

        while (true) {
            System.out.println("How many people are playing? (min. 1 and max. " + maxPlayersToInvite +").");
            String input = scanner.nextLine(); // Read input as a string

            try {
                userInput = Integer.parseInt(input); // Try to parse the input to an integer
                if (userInput >= 1 && userInput <= maxPlayersToInvite) {
                    break; // Valid input, exit the loop
                } else {
                    System.out.println(RED + "Please enter a valid number between 1 and " + maxPlayersToInvite + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input! Please enter a number." + RESET);
            }
        }

        // Loop to register players
        for (int i = 0; i < userInput; i++) {
            String playerName;
            while (true) {
                System.out.print("Enter name for Player " + (i + 1) + ": ");
                playerName = scanner.nextLine();
                // check if player name contains only characters and optional spaces
                if (playerName.matches("[a-zA-Z]+")) {
                    break; // valid name break the loop
                } else {
                    System.out.println(RED + "Invalid Input! Name should only contain alphabets" + RESET);
                }

            }
            double initialMoney = askForInitialBalance(scanner);
            players.add(new Player(playerName, initialMoney)); // Create a new Player with the entered name
        }

        // Confirm players have been registered
        System.out.println("\nPlayers on the Table:");
        for (Player player : players) {
            System.out.println("Name: " + player.getName() + ", Initial Balance: " + player.getAvailableMoney());
        }
    }

    private double askForInitialBalance(Scanner scanner) {
        double input = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("Please enter your initial balance, min. 10");
            String balanceInput = scanner.nextLine();
            try {
                input = Double.parseDouble(balanceInput);
                if (input >= MIN_BET_AMOUNT) {
                    valid = true;
                } else {
                    System.out.println(RED + "Minimum balance requirement is 10!" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input! Please enter a valid number." + RESET);
            }
        }
        return input;
    }


    private Deck validNumberOfDecks() {
        Scanner scanner = new Scanner(System.in);
        int userInput;
        Deck deck = null;
        boolean valid = false;

        while (!valid) {
            System.out.println(YELLOW + "How many decks would you like to play? minimum 2 and maximum 6 Decks are allowed." + RESET);
            try {
                userInput = scanner.nextInt();
                if (userInput >= 2 && userInput <= 6) {
                    deck = new Deck(userInput);
                    System.out.println("Game will be played with " + userInput + " decks");
                    valid = true;
                } else {
                    System.out.println(RED + "Please enter a valid number between 2 and 6." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number." + RESET);
                scanner.next(); // clear the invalid input
            }
        }
        return deck;
    }

    private void promptPlayersForBets() {
        Scanner scanner = new Scanner(System.in);

        for (Player player : players) {
            double betAmount = 0;
            boolean validBet = false;

            while (!validBet) {
                System.out.println("\n" + player.getName() + ", enter your bet amount (between " + MIN_BET_AMOUNT + " and " + MAX_BET_AMOUNT + "):");
                try {
                    betAmount = scanner.nextDouble();

                    if (betAmount < MIN_BET_AMOUNT) {
                        System.out.println(RED + "Bet amount cannot be less than " + MIN_BET_AMOUNT + RESET);
                    } else if (betAmount > MAX_BET_AMOUNT) {
                        System.out.println(RED + "Bet amount cannot exceed " + MAX_BET_AMOUNT + RESET);
                    } else if (betAmount > player.getAvailableMoney()) {
                        System.out.println(RED + "Insufficient funds. You only have " + player.getAvailableMoney() + RESET);
                    } else {
                        player.placeBet(betAmount); // Place the bet for the player
                        validBet = true; // Exit the loop if the bet is valid
                    }
                } catch (InputMismatchException e) {
                    System.out.println(RED + "Invalid input. Please enter a valid number." + RESET);
                    scanner.next(); // Clear the invalid input
                }
            }
        }
    }

    // Method to save game state.
    public void saveGame(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            System.out.println(GREEN + "Game saved successfully!" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error while saving game to " + e.getMessage() + RESET);
        }
    }

    // Load the game from a file
    public static GameLogic loadGame(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println(RED + "Error: Save file not found" + RESET);
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            GameLogic loadedGame = (GameLogic) in.readObject();

            // check if loaded game has more than 7 players
            if (loadedGame.getPlayers().size() > 7) {
                System.out.println(RED + "Error:Loaded game has more than 7 players." + RESET);
                return null; // preventing the game from loading
            }
            return loadedGame; // return loaded game if player count is valid

        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof InvalidClassException) {
                System.out.println(RED + "Error: The save file is incompatible with the current version of the game. Please start a new game." + RESET);
            } else {
                System.out.println("Error loading game: " + e.getMessage());
            }
            return null;
        }
    }

}


