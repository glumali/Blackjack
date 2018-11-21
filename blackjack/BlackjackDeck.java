package blackjack;

import java.util.ArrayList;

public class BlackjackDeck {
    private ArrayList<blackjack.BlackjackCard> allCards = new ArrayList<>();
    // cards shuffled, then dealt sequentially
    private int nextToDeal = 0;

    public BlackjackDeck() {
        initializeCards(blackjack.Suit.CLUB);
        initializeCards(blackjack.Suit.DIAMOND);
        initializeCards(blackjack.Suit.HEART);
        initializeCards(blackjack.Suit.SPADE);
        shuffle();
    }

    private void initializeCards(blackjack.Suit suit) {
        for (int val = 1; val <= 13; val++)
            allCards.add(new blackjack.BlackjackCard(val, suit));
    }

    public void resetDeck() {
        // mark all cards as available
        for (blackjack.BlackjackCard card : allCards) {
            card.markAvailable();
        }

        // reset index counter
        nextToDeal = 0;

        // reshuffle deck
        shuffle();
    }

    // Knuth Shuffle
    public void shuffle() {
        int n = allCards.size();
        for (int i = 0; i < n; i++) {
            // choose index uniformly in [0, i]
            int r = (int) (Math.random() * (i + 1));
            blackjack.BlackjackCard swap = allCards.get(r);
            allCards.set(r, allCards.get(i));
            allCards.set(i, swap);
        }
    }

    public int remainingCards() {
        return allCards.size() - nextToDeal;
    }

    public void dealHand(int numCards, blackjack.BlackjackPlayer[] players) {
        for (blackjack.BlackjackPlayer p : players) {
            blackjack.BlackjackHand hand = new blackjack.BlackjackHand();
            for (int i = 0; i < numCards; i++) {
                if (remainingCards() > 0) {
                    allCards.get(nextToDeal).markUnavailable();
                    hand.addCard(allCards.get(nextToDeal));
                    nextToDeal++;
                } else {
                    System.out.println("Not enough cards!");
                    return;
                }
            }
            p.setHand(hand);
        }
    }

    public blackjack.BlackjackCard dealCard() {
        if (remainingCards() > 0) {
            allCards.get(nextToDeal).markUnavailable();
            return allCards.get(nextToDeal++);
        } else {
            System.out.println("Not enough cards!");
            return null;
        }
    }

    // UNIT TESTS
    public static void main(String[] args) {
        // initialize deck
        blackjack.BlackjackDeck deck = new blackjack.BlackjackDeck();

        int numPlayers = 8;
        int initialFunds = 1000;

        // initialize with numPlayers players
        blackjack.BlackjackPlayer[] players = new blackjack.BlackjackPlayer[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new blackjack.BlackjackPlayer(initialFunds, new blackjack.BlackjackHand());
        }

        deck.dealHand(2, players);

        for (int i = 0; i < numPlayers; i++) {
            blackjack.BlackjackHand thisHand = players[i].getHand();

            System.out.print("Player " + i + " - ");
            System.out.print("Cards: ");
            for (blackjack.BlackjackCard card : thisHand.inHand) {
                System.out.print(card + ", ");
            }
            System.out.print("Score: " + thisHand.getScore());
            if (thisHand.isBust()) System.out.print(" Busted!");
            if (thisHand.is21()) System.out.print(" It's a 21!");
            if (thisHand.isBlackjack()) System.out.print(" Blackjack!");
            System.out.println();
        }

        System.out.println("\nEveryone under 17 hits...\n");

        for (int i = 0; i < numPlayers; i++) {
            if (players[i].getHand().getScore() < 17)
                players[i].drawCard(deck.dealCard());

            blackjack.BlackjackHand thisHand = players[i].getHand();

            System.out.print("Player " + i + " - ");
            System.out.print("Cards: ");
            for (blackjack.BlackjackCard card : thisHand.inHand) {
                System.out.print(card + ", ");
            }
            System.out.print("Score: " + thisHand.getScore());
            if (thisHand.isBust()) System.out.print(" Busted!");
            if (thisHand.is21()) System.out.print(" It's a 21!");
            if (thisHand.isBlackjack()) System.out.print(" Blackjack!");
            System.out.println();
        }
    }
}
