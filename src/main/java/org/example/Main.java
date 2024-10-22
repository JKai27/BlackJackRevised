package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameLogic gameLogic = null; // Initialize as null
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Start New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Save Game");
            System.out.println("4. Exit");

            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1: // Start a New Game
                    gameLogic = new GameLogic();
                    gameLogic.startGame();
                    break;
                case 2: // Load Game
                    gameLogic = GameLogic.loadGame("blackjack_save.dat");
                    if (gameLogic != null) {
                        System.out.println("Game loaded successfully.");

                        // check the number of players in the loaded game
                        int loadedPlayerCount = gameLogic.getPlayers().size();
                        System.out.println("Loaded game has " + loadedPlayerCount + " players.");

                        // Calculate remaining spots
                        int remainingSpots = 7 - loadedPlayerCount;
                        if (remainingSpots > 0) {
                            System.out.println("You can add up to " + remainingSpots + " more player(s).");

                            // Call the modified invitePlayers method with remaining spots
                            gameLogic.invitePlayers(remainingSpots);
                        } else {
                            System.out.println("Cannot add any new players. The loaded game already has the maximum of 7 players.");
                        }

                        // Start the game after adding players
                        gameLogic.startGame(); // Start the game with the loaded configuration
                    } else {
                        System.out.println("Could not load the game. Please start a new game.");
                    }
                    break;

                case 3: // Save Game
                    if (gameLogic != null) {
                        gameLogic.saveGame("blackjack_save.dat");
                    } else {
                        System.out.println("No game in progress to save.");
                    }
                    break;
                case 4: // Exit
                    exit = true;
                    System.out.println("Exiting the game.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // Method to validate user input and ensure it's a valid integer within the range 1-4
    private static int getValidChoice(Scanner scanner) {
        int choice = -1;
        while (true) {
            try {
                System.out.print("Please enter a choice (1-4): ");
                choice = scanner.nextInt();

                // Check if the choice is within the valid range
                if (choice >= 1 && choice <= 4) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                // Handle non-integer input
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input from the scanner
            }
        }
    }
}
