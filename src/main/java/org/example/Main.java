package org.example;

import org.example.AnsiColour;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameLogic gameLogic = null; // Initialize as null
        boolean exit = false;
        boolean isGameLoaded = false; // Flag to track if the game was loaded

        while (!exit) {
            System.out.println(AnsiColour.GREEN.getCode() + "1. Start New Game" + AnsiColour.RESET.getCode());
            System.out.println("2. Load Game");
            System.out.println(AnsiColour.BRIGHT_YELLOW.getCode() + "3. Save Game" + AnsiColour.RESET.getCode());
            System.out.println(AnsiColour.RED.getCode() + "4. Exit" + AnsiColour.RESET.getCode());

            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1: // Start a New Game
                    gameLogic = new GameLogic();
                    isGameLoaded = false; // Reset flag when starting a new game
                    gameLogic.startGame(isGameLoaded);
                    break;
                case 2: // Load Game
                    gameLogic = GameLogic.loadGame("blackjack_save.dat");
                    if (gameLogic != null) {
                        System.out.println(AnsiColour.GREEN.getCode() + "Game loaded successfully." + AnsiColour.RESET.getCode());

                        // Check the number of players in the loaded game
                        int loadedPlayerCount = gameLogic.getPlayers().size();
                        System.out.println(AnsiColour.BLUE.getCode() + "Loaded game has " + loadedPlayerCount + " players." + AnsiColour.RESET.getCode());

                        // Calculate remaining spots
                        int remainingSpots = 7 - loadedPlayerCount;
                        if (remainingSpots > 0) {
                            System.out.println(AnsiColour.YELLOW.getCode() + "You can add up to " + remainingSpots + " more player(s)." + AnsiColour.RESET.getCode());
                            // Call invitePlayers method to invite remaining players
                            gameLogic.invitePlayers(remainingSpots);
                        } else {
                            System.out.println(AnsiColour.RED.getCode() + "Cannot add any new players. The loaded game already has the maximum of 7 players." + AnsiColour.RESET.getCode());
                        }

                        // Set flag to indicate game was loaded
                        isGameLoaded = true;

                        // Start the game with loaded configuration
                        gameLogic.startGame(isGameLoaded); // Pass flag to skip player invites if game is loaded
                    } else {
                        System.out.println(AnsiColour.RED.getCode() + "Could not load the game. Please start a new game." + AnsiColour.RESET.getCode());
                    }
                    break;

                case 3: // Save Game
                    if (gameLogic != null) {
                        gameLogic.saveGame("blackjack_save.dat");
                    } else {
                        System.out.println(AnsiColour.RED.getCode() + "No game in progress to save." + AnsiColour.RESET.getCode());
                    }
                    break;
                case 4: // Exit
                    exit = true;
                    System.out.println(AnsiColour.MAGENTA.getCode() + "Exiting the game." + AnsiColour.RESET.getCode());
                    break;
                default:
                    System.out.println(AnsiColour.RED.getCode() + "Invalid choice. Please try again." + AnsiColour.RESET.getCode());
            }
        }
        scanner.close();
    }

    // Method to validate user input and ensure it's a valid integer within the range 1-4
    private static int getValidChoice(Scanner scanner) {
        int choice = -1;
        while (true) {
            try {
                System.out.print(AnsiColour.MAGENTA.getCode() + "Please enter a choice (1-4): " + AnsiColour.RESET.getCode());
                choice = scanner.nextInt();

                // Check if the choice is within the valid range
                if (choice >= 1 && choice <= 4) {
                    return choice;
                } else {
                    System.out.println(AnsiColour.RED.getCode() + "Invalid choice. Please enter a number between 1 and 4." + AnsiColour.RESET.getCode());
                }
            } catch (InputMismatchException e) {
                // Handle non-integer input
                System.out.println(AnsiColour.RED.getCode() + "Invalid input. Please enter a number." + AnsiColour.RESET.getCode());
                scanner.next(); // Clear the invalid input from the scanner
            }
        }
    }
}
