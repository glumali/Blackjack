package blackjack;

import java.util.Scanner;

public class BlackjackGame {
    private blackjack.BlackjackDeck deck;
    private blackjack.BlackjackPlayer[] players;
    // true if player has not chosen to stand yet
    private int[] wagers;
    private int numPlayers;

    public BlackjackGame(int atTable, int buyIn) throws IllegalArgumentException {
        if (atTable < 1 || buyIn < 1) throw new IllegalArgumentException();

        // include the dealer as a player
        numPlayers = atTable + 1;

        deck = new blackjack.BlackjackDeck();
        players = new blackjack.BlackjackPlayer[numPlayers];

        // initialize players with the buy-in
        // dealer will be player 0
        // user will be player 1
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new blackjack.BlackjackPlayer(buyIn, new blackjack.BlackjackHand());
        }

        wagers = new int[numPlayers];
    }

    private void collectWagers() {
        Scanner scanner = new Scanner(System.in);

        // for each player, prompt amount they want to wager
        for (int i = 1; i < numPlayers; i++) {
            int availableFunds = players[i].getFunds();

            System.out.println("Player " + i + ", you have $" + availableFunds);
            System.out.print("How much do you want to wager?: ");

            int wager = scanner.nextInt();

            while (wager > availableFunds || wager < 0) {
                System.out.println("Invalid wager.");
                System.out.print("How much do you want to wager?: ");
                wager = scanner.nextInt();
            }
            System.out.println("Success! You bet $" + wager + " on the next hand.");
            System.out.println();

            wagers[i] = wager;
        }
    }

    public void newHand() {

        // collect wagers
        collectWagers();

        for (blackjack.BlackjackPlayer p : players) {
            // discard current hand
            p.getHand().discardCards();
        }
        // deck is reset
        deck.resetDeck();

        // deal new cards
        deck.dealHand(2, players);

        // finally, let dealer advance their hand
        advanceDealer();
    }

    public void advanceDealer() {
        blackjack.BlackjackHand dealerHand = players[0].getHand();

        while (dealerHand.getScore() < 17 || dealerHand.isSoft17()) {
            dealerHand.addCard(deck.dealCard());
        }
    }

    // returns false if game is over
    public void runGame() {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i < numPlayers; i++) {
            boolean advance = true;
            blackjack.BlackjackHand myHand = players[i].getHand();

            while (advance) {
                System.out.print("Player " + i + " - Hand: ");
                for (blackjack.BlackjackCard card : myHand.inHand) {
                    System.out.print(card + ", ");
                }
                System.out.println("Highest score: " + myHand.getScore());
                System.out.print("Do you want another card? (y/n): ");
                String response = scanner.next();

                while (!response.equals("y") && !response.equals("n")) {
                    System.out.println("Invalid response.");
                    System.out.print("Do you want another card? (y/n): ");
                    response = scanner.next();
                }

                boolean hitMe = response.equals("y");

                advance = advancePlayer(i, hitMe);
            }
            System.out.println();
        }
        endGame();
    }

    // returns false if busted or standing, true otherwise
    // hitMe is true if the player wants another card
    public boolean advancePlayer(int player, boolean hitMe) {
        blackjack.BlackjackHand myHand = players[player].getHand();

        if (hitMe) {
            players[player].drawCard(deck.dealCard());
            if (players[player].getHand().isBust()) {
                System.out.print("Player " + player + " - Hand: ");
                for (blackjack.BlackjackCard card : myHand.inHand) {
                    System.out.print(card + ", ");
                }
                System.out.println("\nYou busted!");
                return false;
            }
            return true;
        } else {
            System.out.println("Standing at score " + myHand.getScore() + ".");
            return false;
        }
    }

    public void endGame() {
        blackjack.BlackjackHand dealerHand = players[0].getHand();
        int dealerScore = dealerHand.getScore();
        boolean dealerBlackjack = dealerHand.isBlackjack();
        boolean dealerBust = dealerHand.isBust();
        String[] results = new String[numPlayers];

        // for all players
        for (int i = 1; i < numPlayers; i++) {
            blackjack.BlackjackPlayer thisPlayer = players[i];
            blackjack.BlackjackHand thisHand = thisPlayer.getHand();
            int thisScore = thisHand.getScore();

            // if win, add wager amount to funds
            if (thisHand.isBust() && dealerBust) results[i] = "DRAW";
            else if (dealerBust || (thisScore > dealerScore && !thisHand.isBust())) {
                results[i] = "WIN";
                thisPlayer.setFunds(thisPlayer.getFunds() + wagers[i]);
            }
            // if lose, subtract wager amount to funds
            else if ((thisScore < dealerScore && !dealerBust) || thisHand.isBust()) {
                results[i] = "LOSE";
                thisPlayer.setFunds(thisPlayer.getFunds() - wagers[i]);
            }
            // if draw compare if blackjack
            else if (thisScore == 21) {
                if (!thisHand.isBlackjack() && dealerBlackjack) results[i] = "LOSE";
                else if (thisHand.isBlackjack() && !dealerBlackjack) results[i] = "WIN";
                else results[i] = "DRAW";
            } else {
                results[i] = "DRAW";
            }
        }
        printGame(results);
    }

    public void printGame(String[] results) {

        // print results for dealer
        blackjack.BlackjackHand dealerHand = players[0].getHand();

        System.out.print("Dealer - ");
        System.out.print("Cards: ");
        for (blackjack.BlackjackCard card : dealerHand.inHand) {
            System.out.print(card + ", ");
        }
        System.out.print("Score: " + dealerHand.getScore());
        if (dealerHand.isBust()) System.out.print(". Busted!");
        if (dealerHand.is21()) System.out.print(". It's a 21!");
        if (dealerHand.isBlackjack()) System.out.print(". Blackjack!");
        System.out.print("\n\n");

        // print results for all other players
        for (int i = 1; i < numPlayers; i++) {

            blackjack.BlackjackHand thisHand = players[i].getHand();

            System.out.print("Player " + i + " - ");
            System.out.print("Cards: ");
            for (blackjack.BlackjackCard card : thisHand.inHand) {
                System.out.print(card + ", ");
            }
            System.out.print("Score: " + thisHand.getScore());
            if (thisHand.isBust()) System.out.print(". Busted!");
            if (thisHand.is21()) System.out.print(". It's a 21!");
            if (thisHand.isBlackjack()) System.out.print(". Blackjack!");
            System.out.println();

            String result = results[i];
            if (result.equals("WIN")) {
                System.out.print("Player " + i + " won!");
            } else if (result.equals("LOSE")) {
                System.out.print("Player " + i + " lost!");
            } else if (result.equals("DRAW")) {
                System.out.print("Dealer and Player " + i + " tied!");
            }
            System.out.println(" Funds: " + players[i].getFunds());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;
        int gameCounter = 1;

        System.out.println("\nWelcome to Blackjack!\n");


        System.out.print("How many players?: ");
        int atTable = scanner.nextInt();
        while (atTable < 1) {
            System.out.println("Invalid number of players.");
            System.out.print("How many players?: ");
            atTable = scanner.nextInt();
        }
        System.out.println("Confirmed " + atTable + " players.\n");

        System.out.print("How much is the table buy-in?: ");
        int buyIn = scanner.nextInt();
        while (buyIn < 1) {
            System.out.println("Invalid buy-in amount.");
            System.out.print("How much is the table buy-in?: ");
            buyIn = scanner.nextInt();
        }
        System.out.println("Confirmed $" + buyIn + " buy-in.\n");

        while (playAgain) {
            System.out.println("Starting game #" + gameCounter + "!\n");

            BlackjackGame game = new BlackjackGame(atTable, buyIn);

            // deal out a new hand
            game.newHand();

            // run simulation
            game.runGame();

            System.out.println("Finished game #" + gameCounter + "!\n");
            System.out.print("Would you like to play again? (y/n): ");

            String response = scanner.next();

            while (!response.equals("y") && !response.equals("n")) {
                System.out.println("Invalid response.");
                System.out.print("Do you want another card? (y/n): ");
                response = scanner.next();
            }

            playAgain = response.equals("y");
        }
        System.out.println("\nThanks for playing Blackjack!\n");
    }
}
